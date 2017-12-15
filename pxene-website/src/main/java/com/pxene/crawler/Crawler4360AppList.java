package com.pxene.crawler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pxene.crawler.dao.BaseDao;
import com.pxene.crawler.model.AppCategory;
import com.pxene.crawler.model.AppCrawlList;
import com.pxene.service.AppCrawlWaitService;
import com.pxene.utils.AppNameUtil;
import com.pxene.utils.ConfigUtil;
import com.pxene.utils.StringUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler4360AppList extends BaseCrawler {

	public Crawler4360AppList() {
		super("/" + Crawler4360AppList.class.getName().replace(".", "/") + ".json");
	}

	private static final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
	private Log logger = LogFactory.getLog(Crawler4360AppList.class);
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	private static final String APP_LIST = "^https://www.appannie.com/cn/apps/ios/top/china/(.*)/iphone/$";
	private static final String TEXT = "text";
	private static final String HREF = "href";
	private static final String APP_SOURCE = "3";// 360
	private static int ranking = 0;
	private static BaseDao dao = new BaseDao();
	private static List<AppCategory> appCategorylist = new ArrayList<AppCategory>();

	/*
	 * static { String app_sql = "select * from app_category;"; appCategorylist
	 * = dao.getForList(AppCategory.class, app_sql); }
	 */

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && href.matches(APP_LIST);
	}

	public void startCrawler() {
		Properties appStore = ConfigUtil.loadConfigFileByPath("/conf/360phone.properties");
//		Set<Object> keySet = appStore.keySet();
//		for (Object obj : keySet) {
//			String value = appStore.get(obj.toString()).toString();
//			if (value == null)
//				return;
//			String[] values = value.split(",");
//			String urlParam = values[0];
//			String appCategoryId = values[1];
//			String urlStr = "http://zhushou.360.cn/list/index/cid/" + urlParam;
//			System.out.println("urlStr:" + urlStr);
//			analyzeAppListPage(urlStr, appCategoryId);
//		}
		Set<Entry<Object, Object>> entrySet = appStore.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			String urlParam = entry.getKey().toString();
			String appCategoryId = "";
			String urlStr = "http://zhushou.360.cn/list/index/cid/" + urlParam;
			try {
				analyzeAppListPage(urlStr, appCategoryId);
			} catch (Exception e) {
				logger.error("抓取 "+ urlStr+" 出错!");
				e.printStackTrace();
			}
		}
		appStore.clear();
	}

	public static void main(String[] args) {
		Crawler4360AppList casl = new Crawler4360AppList();
		casl.startCrawler();
	}

	/**
	 * 解析AppList页面
	 * 
	 * @param url
	 */
	private void analyzeAppListPage(String url, String appCategoryId) {
		int page = 1;
		int ranking = 1;
		logger.info("list_url:" + url);
		while (ranking <= 100) {
			Document list_doc = connectUrl(url + "/?page=" + page);
			Elements detail_elements = list_doc.select("#iconList > li > a:nth-child(1)");
			Iterator<Element> detail_iterator = detail_elements.iterator();
			while (detail_iterator.hasNext()) {
				Element detail_element = detail_iterator.next();
				String detail_href = detail_element.attr("href");
				analyzeAppDetailPage("http://zhushou.360.cn" + detail_href, appCategoryId, ranking);
				System.out.println("detailUrlOfList:" + "http://zhushou.360.cn/" + detail_href);
				ranking++;
			}
			if (detail_elements.size() < 49) {
				ranking = 101;
			}
			page++;
		}
	}

	/**
	 * 解析AppDetail页面
	 * 
	 * @param ranking
	 * 
	 * @param detail_href
	 */
	private void analyzeAppDetailPage(String url, String appCategoryId, int ranking) {
		logger.info("detail_url:" + url);
		Document detail_doc = connectUrl(url);
		// 1.获取appName
		String appNameCrawl = getTextBySelector(detail_doc, "#app-name > span", TEXT, null);
		String detailElementStr = detail_doc
				.select("head > script:nth-child(18)").toString();
		int startIndex = org.apache.commons.lang.StringUtils.indexOf(detailElementStr, "'sid':");
		int endIndex = org.apache.commons.lang.StringUtils.indexOf(detailElementStr, ",", startIndex+7);
		String appAppId = org.apache.commons.lang.StringUtils.substring(detailElementStr, startIndex+7, endIndex);
		
		// 2.获取appApkName
		//String appApkName = StringUtils.regexpExtract(url, "http://zhushou\\.360\\.cn/detail/index/soft_id/(.*)");
		// 3.获取appCategoryId
		int startCategoryIndex = org.apache.commons.lang.StringUtils.indexOf(detailElementStr, "'cid2':");
		int endCategoryIndex = org.apache.commons.lang.StringUtils.indexOf(detailElementStr, "\"", startCategoryIndex+9);
		String appCategoryCId = org.apache.commons.lang.StringUtils.substring(detailElementStr, startCategoryIndex+9, endCategoryIndex);
		String categoryName="";
		Properties properties = ConfigUtil.loadConfigFileByPath("/conf/360phone.properties");
		if(properties.get(appCategoryCId)!=null){
			categoryName=properties.get(appCategoryCId).toString();
		}
		// 4.获取appVersion
		String appVersion = getTextBySelector(detail_doc,
				"#sdesc > div > div > table > tbody > tr:nth-child(2) > td:nth-child(1)", TEXT, "版本：(.*)");
		// 5.更新时间appUpdateTime
		String appPublishTime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
		// 6.获取来源appSource
		String sourceStr=","+APP_SOURCE+",";
		// 7.获取logo地址appLogoUrl
		String appLogoUrl = getTextBySelector(detail_doc, "#app-info-panel > div > dl > dt > img", "src", null);
		// 8.获取创建时间appCreateTime
		String appCreateDatetime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
		// 9.获取状态信息appStatus
		//int appStatus = 0;
		// 10.获取类别信息appType
		//int appType = 0;
		// 11.获取排名信息appRanking1
		//int appRanking1 = ranking;
		
		Long appRanking1 = 0L;
		String downloadNum = (getTextBySelector(detail_doc,
				"#app-info-panel > div > dl > dd > div > span:nth-child(3)", TEXT, null));
		String[] split = org.apache.commons.lang.StringUtils.split(downloadNum,"：");
		if(org.apache.commons.lang.StringUtils.contains(split[1], "万")){
			downloadNum=org.apache.commons.lang.StringUtils.substring(split[1], 0,split[1].indexOf("万"));
			appRanking1=Long.parseLong(org.apache.commons.lang.StringUtils.trim(downloadNum))*10000;
		}else if(org.apache.commons.lang.StringUtils.contains(split[1], "亿")){
			downloadNum=org.apache.commons.lang.StringUtils.substring(split[1],0, split[1].indexOf("亿"));
			appRanking1=(long) ((Double.parseDouble(org.apache.commons.lang.StringUtils.trim(downloadNum)))*10000*10000);
		}else if(org.apache.commons.lang.StringUtils.contains(split[1], "次")){
			downloadNum=org.apache.commons.lang.StringUtils.substring(split[1],0, split[1].indexOf("次"));
			appRanking1=(Long.parseLong(org.apache.commons.lang.StringUtils.trim(downloadNum)));
		}
		
		if (ranking > 100)
			return;
		// 12.获取下载地址appDownloadUrl
		String appDownloadUrl = getTextBySelector(detail_doc, "#app-info-panel > div > dl > dd > a", HREF, null);
		
		
		String select_sql ="select * from app_crawl_wait where app_appId=? and app_source like ?"; 
		AppCrawlList appCrawlList = dao.get(AppCrawlList.class,
		select_sql, new Object[] { appAppId,"%,"+APP_SOURCE+",%" });
		
		
		// 若为未爬取 或爬取失败,对App信息进行添加
		
		if(appCrawlList == null) {
			
			String insertSql="insert into app_crawl_wait(app_priority,app_ranking1,app_logo_url,app_name,app_name_crawl,app_category_name,app_version,app_update_time,app_source,app_create_time,app_status,app_is_blacklist,app_type,app_download_url,app_appId,app_version_update) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			Object[] obj = new Object[] { 3, appRanking1, appLogoUrl,"",appNameCrawl,categoryName,
					appVersion, appPublishTime, sourceStr, appCreateDatetime, 1, 0,2,appDownloadUrl, appAppId,appVersion};
			 dao.update(insertSql, obj);
		} 
		
		if(appCrawlList!=null){
			//不存在  新增App时,已存在榜单抓取App的情况
			// 对App信息进行抓取更新
			 String appVersionUpdate="";
				if(!org.apache.commons.lang.StringUtils.contains(appCrawlList.getApp_version(), appVersion)){
					if(!org.apache.commons.lang.StringUtils.equals(appCrawlList.getApp_version(), "")){
						if(appCrawlList.getApp_version_update()==null||org.apache.commons.lang.StringUtils.equals(appCrawlList.getApp_version_update(), "")){
							appVersionUpdate=appVersion;
						}else{
							appVersionUpdate=appCrawlList.getApp_version_update()+","+appVersion;
						}
						appVersion=appCrawlList.getApp_version()+","+appVersion;
					}else{
						appVersionUpdate=appVersion;
					}
				}else{
					appVersionUpdate=appCrawlList.getApp_version_update();
					appVersion=appCrawlList.getApp_version();
				}
				String updateSql = "update app_crawl_wait set app_ranking1=?,app_logo_url=?,app_version=?,app_update_time=?,app_download_url=?,app_version_update=? where app_id=?";
				Object[] obj = new Object[] { appRanking1, appLogoUrl, appVersion,appPublishTime, appDownloadUrl, appVersionUpdate,appCrawlList.getApp_id() };
				 dao.update(updateSql, obj);
		}
		
	
		System.out.println("detail_url:appRanking:" + appRanking1 + ",appName:" + appNameCrawl + ",appCategory:"
				+ appCategoryId + ",appVersion:" + appVersion + ",appLogoUrl:" + appLogoUrl  + ",appPublishTime:" + appPublishTime);
	}

	/**
	 * 根据选择器获得相应的内容
	 * 
	 * @param doc
	 * @param selector
	 * @param attribute
	 * @param regexp
	 * @return
	 */
	private String getTextBySelector(Document doc, String selector, String attribute, String regexp) {
		String text = "";
		Elements elements = doc.select(selector);
		Iterator<Element> iterator = elements.iterator();
		while (iterator.hasNext()) {
			Element element = iterator.next();
			String element_comment = "";
			if (attribute.equals(TEXT)) {
				element_comment = element.text();
			} else {
				element_comment = element.attr(attribute);
			}
			if (org.apache.commons.lang.StringUtils.isNotBlank(regexp)) {
				text = StringUtils.regexpExtract(element_comment, regexp);
			} else {
				text = element_comment;
			}
		}
		return text.trim();
	}

	public static String timeStamp2Date(String seconds, String format, boolean flag) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty())
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (flag) {
			return sdf.format(new Date(Long.valueOf(seconds + "000")));
		}
		return sdf.format(new Date(Long.valueOf(seconds)));
	}

	protected Document connectUrl(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).ignoreContentType(true).userAgent("chrome1").timeout(50000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return doc;
	}

}
