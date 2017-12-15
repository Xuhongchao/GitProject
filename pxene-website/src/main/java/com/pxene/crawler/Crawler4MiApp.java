package com.pxene.crawler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
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
public class Crawler4MiApp extends BaseCrawler {

	public Crawler4MiApp() {
		super("/com/pxene/crawler/Crawler4MiAppList.json");
	}

	private static final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
	private Log logger = LogFactory.getLog(Crawler4MiApp.class);
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	private static final String APP_LIST = "^https://www.appannie.com/cn/apps/ios/top/china/(.*)/iphone/$";
	private static final String TEXT = "text";
	private static final String HREF = "href";
	private static final String APP_SOURCE = "4";
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
		String appName = "开心消消乐";
		Crawler4MiApp casl = new Crawler4MiApp();
		casl.startCrawler( appName, "6", null);
	}

	/**
	 * 解析AppList页面
	 * 
	 * @param url
	 */
	private void analyzeAppListPage(String appName,String source,String updateAppId) {
		// body > div.main > div > div.main-con > div:nth-child(1) > ul >
		// li:nth-child(1) > h5 > a
		boolean result = false;
		// 为查询的AppName编码
		String appNameEncode = "";
		String sourceStr=source+APP_SOURCE+",";
		try {
			appNameEncode = URLEncoder.encode(appName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Document list_doc = connectUrl("http://app.mi.com/search?keywords=" + appNameEncode);
		Elements detail_elements = list_doc.select("body > div.main > div > div.main-con > div:nth-child(1) > ul > li");
		Iterator<Element> detail_iterator = detail_elements.iterator();
		boolean crawlState=false;
		while (detail_iterator.hasNext()) {
			Element detail_element = detail_iterator.next();
			// 获取到AppName
			Elements element = detail_element.select(" h5 > a");
			if (element.text().contains(appName)) {

				String detail_href = element.attr("href");
				analyzeAppDetailPage("http://app.mi.com" + detail_href, sourceStr, updateAppId,appName);
				//break;
			}
		}
		
	/*	if(!crawlState){
			//更改状态为抓取失败
			String insertSql = "update app_crawl_wait set app_status= ? where app_id = ?";
			Object[] obj = new Object[] {3,appId};
			dao.update(insertSql, obj);
		}*/
	}

	/**
	 * 解析AppDetail页面
	 * 
	 * @param ranking
	 * 
	 * @param detail_href
	 */
	private void analyzeAppDetailPage(String url,String sourceStr,String updateAppId,String appName) {
		Integer resultCount = 0;
		logger.info("detail_url:" + url);
		Document detail_doc = connectUrl(url);
		// 1.获取appName
		String appNameCrawl = getTextBySelector(detail_doc,
				"body > div.main > div.container.cf > div.app-intro.cf > div.app-info > div > h3", TEXT, null);
		//body > div.main > div.container.cf > div.app-intro.cf > div.look-detail > div > ul.cf > li:nth-child(10)
		String appAppId= getTextBySelector(detail_doc,
				"body > div.main > div.container.cf > div.app-intro.cf > div.look-detail > div > ul.cf > li:nth-child(10)", TEXT, null);
		/*
		 * // 2.获取appApkName String appApkName = StringUtils.regexpExtract(url,
		 * "http://app\\.mi\\.com/details\\?id=(.*)");
		 */
		// 3.获取appCategoryId    body > div.main > div.container.cf > div.app-intro.cf > div.app-info > div > p.special-font.action
		//body > div.main > div.container.cf > div.app-intro.cf > div.app-info > div > p.special-font.action > b:nth-child(1)
		String categoryName = getTextBySelector(detail_doc,
					"body > div.main > div.container.cf > div.app-intro.cf > div.app-info "
					+ "> div > p.special-font.action", TEXT, "分类：([\u4e00-\u9fa5]+)|[\u4e00-\u9fa5]");
		// 4.获取appVersion  
		String appVersion = getTextBySelector(detail_doc,
				"body > div.main > div.container.cf > div.app-intro.cf > div.look-detail > div > ul.cf > li:nth-child(4)",
				TEXT, null);
		
		// 5.更新时间appUpdateTime
		String appPublishTime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
		// 6.获取来源appSource
		// String appSource = APP_SOURCE;
		// 7.获取logo地址appLogoUrl
		String appLogoUrl = getTextBySelector(detail_doc,
				"body > div.main > div.container.cf > div.app-intro.cf > div.app-info > img", "src", null);
		// 8.获取创建时间appCreateTime
		String appCreateDatetime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
		// 9.获取状态信息appStatus
		// int appStatus = 0;
		// 10.获取类别信息appType
		//int appType = 0;
		// 11.获取排名信息appRanking1
		//小米应用市场中没有下载量,用评分次数代替    ( 158540次评分 )
		Long appRanking1 = 0L;
		String downloadNum=getTextBySelector(detail_doc,
				" body > div.main > div.container.cf > div.app-intro.cf > div.app-info > div > span",
				TEXT, "([\\d]+)[\u4e00-\u9fa5]");
		appRanking1=Long.parseLong(downloadNum);
		
		// 12.获取下载地址appDownloadUrl
		String appDownloadUrl = "http://app.mi.com" + getTextBySelector(detail_doc,
				"body > div.main > div.container.cf > div.app-intro.cf > div.app-info > div > div.app-info-down > a",
				HREF, null);
		
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
			Object[] obj = new Object[] { appRanking1, appLogoUrl, appVersion,appPublishTime, appDownloadUrl,appVersionUpdate, appCrawlList.getApp_id() };
			resultCount = dao.update(updateSql, obj);
		}

		System.out.println("detail_url:appId:" + appAppId + ",appName:" + appName + ",appVersion:" + appVersion
				+ ",appLogoUrl:" + appLogoUrl + ",appApkName:" + ",appPublishTime:" + appPublishTime);

		/*if (resultCount == 0) {
			return false;
		} else {
			return true;
		}*/
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
