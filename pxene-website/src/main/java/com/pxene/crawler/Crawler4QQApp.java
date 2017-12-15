package com.pxene.crawler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
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
import com.pxene.utils.StringUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

@Service
public class Crawler4QQApp extends BaseCrawler {

	public Crawler4QQApp() {
		super("/com/pxene/crawler/Crawler4QQAppList.json");
	}

	private static final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
	private Log logger = LogFactory.getLog(Crawler4QQApp.class);
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	private static final String APP_LIST = "^http://sj\\.qq\\.com/myapp/category.htm\\?orgame=([\\d]*)&categoryId=([\\d]*)$|"
			+ "^http://sj\\.qq\\.com/myapp/category\\.htm\\?categoryId=([\\d]*)&orgame=([\\d]*)$";
	private static final String APP_LIST_JSON = "http://sj.qq.com/myapp/cate/appList.htm";
	private static final String APP_DETAIL = "http://sj\\.qq\\.com/myapp/detail\\.htm\\?apkName=(.*)";
	private static final String APP_DETAIL_PREIX = "http://sj.qq.com/myapp/";
	private static final int PAGE_SIZE = 20;
	private static final int MAX_SIZE = 100;
	private static final String TEXT = "text";
	private static final String HREF = "href";
	private static final String APP_SOURCE = "1";// 腾讯应用宝
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
		Crawler4QQApp casl = new Crawler4QQApp();
		casl.startCrawler(appName,"6",null);
	}

	/**
	 * 对App进行新增或更新
	 * @param newAppName
	 * @param source
	 * @param updateAppId 为null表示新增,不为null表示更新
	 * @return
	 */
	private void analyzeAppListPage(String appName,String source,String updateAppId) {
		Integer resultCount = -1;
		// 为查询的AppName编码
		String appNameEncode = "";
		String sourceStr=source+APP_SOURCE+",";
		try {
			appNameEncode = URLEncoder.encode(appName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Document list_doc = connectUrl("http://sj.qq.com/myapp/searchAjax.htm?kw=" + appNameEncode + "&pns=&sid=");
		String jsonStr = getTextBySelector(list_doc, "html > body", TEXT, "");
		

		JSONArray results=new JSONArray();
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			JSONObject jsonObjectNode = new JSONObject(jsonObject.get("obj").toString());
			results = (JSONArray) jsonObjectNode.get("appDetails");
		} catch (JSONException e) {
			logger.error("应用宝解析json出错!"+e.getMessage());
			e.printStackTrace();
			//return false;
		}
		//System.out.println(jsonObject.toString());
		// JSONObject jsonObjectNode = (JSONObject) jsonObject.get("obj");
		System.out.println(results.length());
		for (int j = 0; j < results.length(); j++) {
			JSONObject object = (JSONObject) results.get(j);
			if (object != null) {
				if (object.get("appName").toString().contains(appName)) {
					System.out.println(object.get("appName").toString());
					String appNameCrawl = object.get("appName").toString();
					// 查询时的App名称

					// String appCategory =
					// object.get("categoryName").toString();
					String appAppId=object.get("appId").toString();
					String categoryName=object.get("categoryName").toString();
					String appVersion = object.get("versionName").toString();
					String appPublishDatetime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
					String appLogoUrl = object.get("iconUrl").toString();
					// String appApkName = object.get("pkgName").toString();
					String appDownloadUrl = object.get("apkUrl").toString();
					String pkgName = object.get("pkgName").toString();
					//int rank = getAppRank("http://sj.qq.com/myapp/detail.htm?apkName=" + pkgName, appName);
					Long rank=object.getLong("appDownCount") ;

					String app_create_datetime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
					
					String select_sql ="select * from app_crawl_wait where app_appId=? and app_source like ?"; 
					AppCrawlList appCrawlList = dao.get(AppCrawlList.class,
					select_sql, new Object[] { appAppId,"%,"+APP_SOURCE+",%" });
					
					// 新增App || 更新时不新增已经删除的App
					if(appCrawlList == null&&updateAppId==null) {
						
						String insertSql="insert into app_crawl_wait(app_priority,app_ranking1,app_logo_url,app_name,app_name_crawl,app_category_name,app_version,app_update_time,app_source,app_create_time,app_status,app_is_blacklist,app_type,app_download_url,app_appId,app_version_update) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
						Object[] obj = new Object[] { 2, rank, appLogoUrl,appName,appNameCrawl,categoryName,
								appVersion, appPublishDatetime, sourceStr, app_create_datetime, 1, 0,2,appDownloadUrl, appAppId,appVersion};
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
						Object[] obj = new Object[] { rank, appLogoUrl, appVersion,appPublishDatetime, appDownloadUrl, appVersionUpdate,appCrawlList.getApp_id() };
						resultCount = dao.update(updateSql, obj);
					}

				
					System.out.println("detail_url:appId:" + appAppId + ",appNameCrawl:" + appNameCrawl + ",appVersion:"
							+ appVersion + ",appPublishTime:" + appPublishDatetime + ",app_source:" + APP_SOURCE
							+ ",appLogoUrl:" + appLogoUrl);
					
					//break;
					}
			}
		}
		
//		if (resultCount > 0) {
//			return true;
//		} else {
//			//更改状态为抓取失败
//			String insertSql = "update app_crawl_wait set app_status= ? where app_id = ?";
//			Object[] obj = new Object[] {3,appId};
//			dao.update(insertSql, obj);
//			return false;
//		}
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
		Document doc = connectUrl(url);

		String href = getTextBySelector(doc, "#J_DetCate", "href", "");
		System.out.println(href);
		Document list_doc = connectUrl("http://sj.qq.com/myapp/" + href);
		// body > div.category-wrapper.clearfix > div.main > ul > li
		// :nth-child(2) > div > div > a.name.ofh

		Elements detail_elements = list_doc.select("body > div.category-wrapper.clearfix > div.main > ul > li");
		Iterator<Element> detail_iterator = detail_elements.iterator();
		while (detail_iterator.hasNext()) {
			Element detail_element = detail_iterator.next();
			// 获取到AppName
			String name = detail_element.select(" div > div > a.name.ofh").text();
			if (org.apache.commons.lang.StringUtils.equals(appName, name)) {
				return ranking;
			}
			ranking++;
		}

		// //若网站爬取不到排名,设置为500
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
