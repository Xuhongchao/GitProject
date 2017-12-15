package com.pxene.crawler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

import com.pxene.appcategory.ClassifyForApp;
import com.pxene.crawler.dao.BaseDao;
import com.pxene.crawler.model.AppCrawlList;
import com.pxene.service.AppCrawlWaitService;
import com.pxene.utils.AppNameUtil;
import com.pxene.utils.ConfigUtil;
import com.pxene.utils.StringUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler4AppStoreList extends BaseCrawler {

	public Crawler4AppStoreList() {
		super("/" + Crawler4AppStoreList.class.getName().replace(".", "/")
				+ ".json");
	}

	private static final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
	private Log logger = LogFactory.getLog(Crawler4AppStoreList.class);
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	private static final String APP_LIST = "^https://www.appannie.com/cn/apps/ios/top/china/(.*)/iphone/$";
	private static final String TEXT = "text";
	private static final String HREF = "href";
	private static final String APP_SOURCE = "5";// APPstore
	private static int ranking = 0;
	private static BaseDao dao = new BaseDao();
	private AppCrawlWaitService appCrawlWaitService;
	
	/*private static List<AppCategory> appCategorylist = new ArrayList<AppCategory>();

	static {
		String app_sql = "select * from app_category;";
		appCategorylist = dao.getForList(AppCategory.class, app_sql);
	}*/
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && href.matches(APP_LIST);
	}
	public void startCrawler() {
		Properties appStore = ConfigUtil
				.loadConfigFileByPath("/conf/appStore.properties");
		Set<Object> keySet = appStore.keySet();
		for (Object obj : keySet) {
			String value = appStore.get(obj.toString()).toString();
			if (value == null)
				return;
			String[] values = value.split(",");
			String urlParam = values[0];
			String categoryName=values[0];
			String urlStr = "https://www.appannie.com/cn/apps/ios/top/china/"
					+ urlParam + "/iphone/";
			System.out.println("urlStr:" + urlStr);
			try {
				analyzeAppListPage(urlStr, categoryName);
			} catch (Exception e) {
				logger.error("抓取 "+ urlStr+" 出错!");
				e.printStackTrace();
			}
		}
		appStore.clear();
	}
	public static void main(String[] args) {
		Crawler4AppStoreList casl = new Crawler4AppStoreList();
		casl.startCrawler();
	}

	/**
	 * 解析AppList页面
	 * 
	 * @param url
	 */
	private void analyzeAppListPage(String url, String categoryName) {
		Document listDoc = connectUrl(url);
		Elements list_elements = listDoc.select("#free > div.list > div.item");
		Iterator<Element> list_iterator = list_elements.iterator();
		while (list_iterator.hasNext()) {
			Element list_element = list_iterator.next();
			Elements list_product_elements = list_element
					.select("div.item > a > span.product-name");
			Iterator<Element> list_product_iterator = list_product_elements
					.iterator();
			String appNameCrawl = "";
			String appApkName = "";
			String appAppId = "";
			String appDownloadUrl = "";
			Long appRanking1 = 0L;
			String appLogoUrl = "";
			String appVersion = "";
			while (list_product_iterator.hasNext()) {
				Element list_product_element = list_product_iterator.next();
				appNameCrawl = list_product_element.text();
				System.out.println("product_name:" + appNameCrawl);
			}
			Elements list_a_elements = list_element.select("div.item > a");
			Iterator<Element> list_a_iterator = list_a_elements.iterator();
			while (list_a_iterator.hasNext()) {
				Element list_a_element = list_a_iterator.next();
				String list_a_href = list_a_element.attr("href");
				if(list_a_href.isEmpty()) break;
				appAppId=appApkName = StringUtils.regexpExtract(list_a_href,
						"/cn/apps/ios/app/([\\d]*)/");
				
				appDownloadUrl = "https://itunes.apple.com/app/id" + appApkName;
				Document download_doc = connectUrl(appDownloadUrl);
				if (download_doc == null) break;
				appVersion = getTextBySelector(
						download_doc,
						"#left-stack > div.lockup.product.application > ul > li:nth-child(4) > span:nth-child(2)",
						TEXT, null);
				System.out.println("detail_url:" + list_a_href);
				System.out.println("download_url:" + appDownloadUrl);
				System.out.println("version:" + appVersion);
			}
			String sourceStr=","+APP_SOURCE+",";
//			String categoryName="";
//			try {
//				Document download_doc = connectUrl(appDownloadUrl);
//				categoryName=getTextBySelector(download_doc,"#left-stack > div.lockup.product.application > ul > li.genre > a > span",
//						TEXT, null);
//			} catch (Exception e) {
//				logger.error("appCategory获取失败: "+e.getMessage());
//				e.printStackTrace();
//			}
			
			appRanking1=500L;
		/*	Elements number_elements = list_element
					.select("div.item > a > span.number");
			Iterator<Element> number_iterator = number_elements.iterator();
			while (number_iterator.hasNext()) {
				Element number_element = number_iterator.next();
				appRanking1 = number_element.text();
				System.out.println("number:" + appRanking1);
			}*/
			Elements logo_elements = list_element
					.select("div.item > a > span.icon > img");
			Iterator<Element> logo_iterator = logo_elements.iterator();
			while (logo_iterator.hasNext()) {
				Element logo_element = logo_iterator.next();
				appLogoUrl = logo_element.attr("src");
				System.out.println("logo_url:" + appLogoUrl);
			}
			String appCreateDatetime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
			String appPublishTime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
			
			if (!appNameCrawl.equals("***")) {
				String select_sql ="select * from app_crawl_wait where app_appId=? and app_source like ?"; 
				AppCrawlList appCrawlList = dao.get(AppCrawlList.class,
				select_sql, new Object[] { appAppId,"%,"+APP_SOURCE+",%" });
				// 若为未爬取 或爬取失败,对App信息进行添加
				if(appCrawlList == null) {
					String insertSql="insert into app_crawl_wait(app_priority,app_ranking1,app_logo_url,app_name,app_name_crawl,app_category_name,app_version,app_update_time,app_source,app_create_time,app_status,app_is_blacklist,app_type,app_download_url,app_appId,app_version_update) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
					Object[] obj = new Object[] { 3, appRanking1, appLogoUrl,"",appNameCrawl,categoryName,
							appVersion, appPublishTime, sourceStr, appCreateDatetime, 1, 0,3,appDownloadUrl, appAppId,appVersion};
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
			}
			
					System.out.println("detail_url:appRanking:" + appRanking1
							+ ",appName:" + appNameCrawl  
							+ ",appVersion:" + appVersion + ",appLogoUrl:" + appLogoUrl
							+ ",appApkName:" + appApkName + ",appPublishTime:"
							+ appPublishTime);
		}	
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
	private String getTextBySelector(Document doc, String selector,
			String attribute, String regexp) {
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

	public static String timeStamp2Date(String seconds, String format,
			boolean flag) {
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
			doc = Jsoup.connect(url).ignoreContentType(true)
					.userAgent("chrome1").timeout(50000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return doc;
	}

}
