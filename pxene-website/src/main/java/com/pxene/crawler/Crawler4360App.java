package com.pxene.crawler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import org.springframework.stereotype.Service;

import com.pxene.crawler.dao.BaseDao;
import com.pxene.crawler.model.AppCategory;
import com.pxene.crawler.model.AppCrawlList;
import com.pxene.utils.AppNameUtil;
import com.pxene.utils.ConfigUtil;
import com.pxene.utils.StringUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

@Service
public class Crawler4360App extends BaseCrawler {

	public Crawler4360App() {
		super("/com/pxene/crawler/Crawler4360AppList.json");
	}

	private static final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
	private Log logger = LogFactory.getLog(Crawler4360App.class);
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

	public void startCrawler(String appName,String source,String updateAppId) {
		 analyzeAppListPage(appName,source,updateAppId);
	}

	public static void main(String[] args) {
		String appName = "2345天气王";
		Crawler4360App casl = new Crawler4360App();
		casl.startCrawler(appName,"6",null);
	}

	/**
	 * 解析AppList页面
	 * 
	 * @param url
	 */
	private void analyzeAppListPage(String appName,String source,String updateAppId) {
		// body > div.warp > div.main > div > ul > li :nth-child(5) > dl > dd >
		// h3 > a
		boolean result = false;
		// 为查询的AppName编码
		String appNameEncode = "";
		String sourceStr=source+APP_SOURCE+",";
		try {
			appNameEncode = URLEncoder.encode(appName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Document list_doc = connectUrl("http://zhushou.360.cn/search/index/?kw=" + appNameEncode);

		Elements detail_elements = list_doc.select("body > div.warp > div.main > div > ul> li");
		Iterator<Element> detail_iterator = detail_elements.iterator();
		
		boolean crawlState=false;
		while (detail_iterator.hasNext()) {
			Element detail_element = detail_iterator.next();
			// 获取到AppName
			Elements element = detail_element.select(" dl > dd > h3 > a");
			if (element.text().contains(appName)) {
				String detail_href = element.attr("href");
				 analyzeAppDetailPage("http://zhushou.360.cn" + detail_href, sourceStr, updateAppId,appName);
				//break;
			}
			
		}
		/*if(!crawlState){
			//更改状态为抓取失败
			String insertSql = "update app_crawl_wait set app_status= ? where app_id = ?";
			Object[] obj = new Object[] {3,appId};
			dao.update(insertSql, obj);
		}
		return result;*/
	}

	/**
	 * 解析AppDetail页面
	 * 
	 * @param ranking
	 * 
	 * @param detail_href
	 */
	private void analyzeAppDetailPage(String url,String sourceStr,String updateAppId,String appName) {
		Properties properties = ConfigUtil.loadConfigFileByPath("/conf/360phone.properties");
		Integer resultCount = 0;
		logger.info("detail_url:" + url);
		Document detail_doc = connectUrl(url);
		// 1.获取appName
		String appNameCrawl = getTextBySelector(detail_doc, "#app-name > span", TEXT, null);
		
		String detailElementStr = detail_doc
				.select("head > script:nth-child(18)").toString();
		int startIndex = org.apache.commons.lang.StringUtils.indexOf(detailElementStr, "'sid':");
		int endIndex = org.apache.commons.lang.StringUtils.indexOf(detailElementStr, ",", startIndex+7);
		String appAppId = org.apache.commons.lang.StringUtils.substring(detailElementStr, startIndex+7, endIndex);
		
		// // 2.获取appApkName
		// String appApkName = StringUtils.regexpExtract(url,
		// "http://zhushou\\.360\\.cn/detail/index/soft_id/(.*)");
		// 3.获取appCategoryId  
		int startCategoryIndex = org.apache.commons.lang.StringUtils.indexOf(detailElementStr, "'cid2':");
		int endCategoryIndex = org.apache.commons.lang.StringUtils.indexOf(detailElementStr, "\"", startCategoryIndex+9);
		String appCategoryId = org.apache.commons.lang.StringUtils.substring(detailElementStr, startCategoryIndex+9, endCategoryIndex);
		String categoryName="";
		if(properties.get(appCategoryId)!=null){
			categoryName=properties.get(appCategoryId).toString();
		}
		// 4.获取appVersion
		//	#sdesc > div > div > table > tbody > tr:nth-child(2) > td:nth-child(1)
		String appVersion = getTextBySelector(detail_doc,
				"#sdesc > div > div > table > tbody > tr:nth-child(2) > td:nth-child(1)", TEXT, "版本：(.*)");
		// 5.更新时间appUpdateTime
		String appPublishTime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
		// 6.获取来源appSource
		// String appSource = APP_SOURCE;
		// 7.获取logo地址appLogoUrl
		String appLogoUrl = getTextBySelector(detail_doc, "#app-info-panel > div > dl > dt > img", "src", null);
		// 8.获取创建时间appCreateTime
		String appCreateDatetime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
		// 9.获取状态信息appStatus
		//int appStatus = 0;
		// 10.获取类别信息appType
		int appType = 0;
		// 11.获取排名信息appRanking1
		Long appRanking1 = 0L;
		
		//#app-info-panel > div > dl > dd > div > span:nth-child(3)
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
		
		// 12.获取下载地址appDownloadUrl
		String appDownloadUrl = getTextBySelector(detail_doc, "#app-info-panel > div > dl > dd > a", HREF, null);
		appDownloadUrl = "http" + org.apache.commons.lang.StringUtils.substringAfterLast(appDownloadUrl, "http");
		
		String select_sql ="select * from app_crawl_wait where app_appId=? and app_source like ?"; 
		AppCrawlList appCrawlList = dao.get(AppCrawlList.class,
		select_sql, new Object[] { appAppId,"%,"+APP_SOURCE+",%" });
		
		
		// 若为未爬取 或爬取失败,对App信息进行添加
		
		if(appCrawlList == null&&updateAppId==null) {
			
			String insertSql="insert into app_crawl_wait(app_priority,app_ranking1,app_logo_url,app_name,app_name_crawl,app_category_name,app_version,app_update_time,app_source,app_create_time,app_status,app_is_blacklist,app_type,app_download_url,app_appId,app_version_update) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			Object[] obj = new Object[] { 2, appRanking1, appLogoUrl,appName,appNameCrawl,categoryName,
					appVersion, appPublishTime, sourceStr, appCreateDatetime, 1, 0,2,appDownloadUrl, appAppId,appVersion};
			resultCount = dao.update(insertSql, obj);
		} 
		
		if(appCrawlList!=null&&updateAppId.equals(appCrawlList.getApp_appId())){
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
			resultCount = dao.update(updateSql, obj);
		}
	
		System.out.println("detail_url:appId:" + appAppId + ",appName:" + appName + ",appVersion:" + appVersion
				+ ",appLogoUrl:" + appLogoUrl + ",appPublishTime:" + appPublishTime);

/*		if (resultCount == 0) {
			return false;
		} else {
			return true;
		}*/
	}

	/**
	 * 根据app名称在指定url中获取app排名
	 * 
	 * @param url
	 * @param appName
	 * @return
	 */
	public int getAppRank(String url, String appName) {

		int ranking = 1;
		Document list_doc = connectUrl(url);
		Elements detail_elements = list_doc
				.select("#doc > div.list.big-app-wrap > div.sec-app.clearfix > div > ul > li > a");
		Iterator<Element> detail_iterator = detail_elements.iterator();
		while (detail_iterator.hasNext()) {
			Element detail_element = detail_iterator.next();
			// #doc > div.list.big-app-wrap > div.sec-app.clearfix > div > ul >
			// li:nth-child(2) > a > div.app-detail > p.name
			String name = detail_element.select("div.app-detail > p.name").text();
			if (org.apache.commons.lang.StringUtils.equals(appName, name)) {
				return ranking;
			}
			ranking++;
		}
		// 若网站爬取不到排名,设置为500
		ranking = 500;
		return ranking;
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
