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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pxene.appcategory.ClassifyForApp;
import com.pxene.crawler.dao.BaseDao;
import com.pxene.crawler.model.AppCategory;
import com.pxene.crawler.model.AppCrawlList;
import com.pxene.utils.AppNameUtil;
import com.pxene.utils.ConfigUtil;
import com.pxene.utils.StringUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
@Service
public class Crawler4AppStore extends BaseCrawler {

	public Crawler4AppStore() {
		super("/com/pxene/crawler/Crawler4AppStoreList.json");
	}

	private static final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
	private Log logger = LogFactory.getLog(Crawler4AppStore.class);
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	private static final String APP_LIST = "^https://www.appannie.com/cn/apps/ios/top/china/(.*)/iphone/$";
	private static final String TEXT = "text";
	private static final String HREF = "href";
	private static final String APP_SOURCE = "5";// APPstore
	private static int ranking = 0;
	private static BaseDao dao = new BaseDao();

	/*
	 * private static List<AppCategory> appCategorylist = new
	 * ArrayList<AppCategory>();
	 * 
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
		String appName = "优拜单车";
		Crawler4AppStore casl = new Crawler4AppStore();
		casl.startCrawler(appName,"6",null);
	}

	/**
	 * 解析AppList页面
	 * 
	 * @param url
	 */
	private void analyzeAppListPage(String appName,String source,String updateAppId) {
		Integer resultCount=-1;
		String appNameEncode="";
		String sourceStr=source+APP_SOURCE+",";
		try {
			appNameEncode = URLEncoder.encode(appName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = "https://www.appannie.com/mkt/api/search?lang=cn&term=" + appNameEncode;
		Document listDoc = connectUrl(url);

		String jsonStr = getTextBySelector(listDoc, "html > body", TEXT, "");
		JSONArray results = new JSONArray(jsonStr);
		for (int j = 0; j < results.length(); j++) {
			JSONObject object = (JSONObject) results.get(j);
			if (object != null&&object.toString().contains("product_name")) {
				if (object.get("product_name").toString().contains(appName)) {
					//IOS中抓取单个App 排名都为500
					Integer appRanking1=500;
					String appNameCrawl=object.get("product_name").toString();
					String appAppId=object.get("product_id").toString();
					
					String appApkName=object.get("product_id").toString();
					String appLogoUrl=object.get("icon").toString();
					
					//HTTP error fetching URL. Status=404
					String appDownloadUrl = "https://itunes.apple.com/app/id" + appApkName;
					Document download_doc = connectUrl(appDownloadUrl);
					
					String appVersion="";
					try {
						appVersion = getTextBySelector(
								download_doc,
								"#left-stack > div.lockup.product.application > ul > li:nth-child(4) > span:nth-child(2)",
								TEXT, null);
					} catch (Exception e) {
						logger.error("appVersion获取失败: "+e.getMessage());
						e.printStackTrace();
					}
					String categoryName="";
					try {
						categoryName=getTextBySelector(download_doc,"#left-stack > div.lockup.product.application > ul > li.genre > a > span",
								TEXT, null);
					} catch (Exception e) {
						logger.error("appCategory获取失败: "+e.getMessage());
						e.printStackTrace();
					}
					
					String appCreateDatetime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
					String appPublishTime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
					
					if (!appNameCrawl.equals("***")) {
						String select_sql ="select * from app_crawl_wait where app_appId=? and app_source like ?"; 
						AppCrawlList appCrawlList = dao.get(AppCrawlList.class,
						select_sql, new Object[] { appAppId,"%,"+APP_SOURCE+",%" });
						// 若为未爬取 或爬取失败,对App信息进行添加
						if(appCrawlList == null&&updateAppId==null) {
							String insertSql="insert into app_crawl_wait(app_priority,app_ranking1,app_logo_url,app_name,app_name_crawl,app_category_name,app_version,app_update_time,app_source,app_create_time,app_status,app_is_blacklist,app_type,app_download_url,app_appId,app_version_update) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
							Object[] obj = new Object[] { 2, appRanking1, appLogoUrl,appName,appNameCrawl,categoryName,
									appVersion, appPublishTime, sourceStr, appCreateDatetime, 1, 0,3,appDownloadUrl, appAppId,appVersion};
							resultCount = dao.update(insertSql, obj);
						} 
						//更新App
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
						
						System.out.println("detail_url:appId:" + appAppId + ",appNameCrawl:" + appNameCrawl + ",appVersion:"
								+ appVersion + ",appLogoUrl:" + appLogoUrl + ",appApkName:" + appApkName
								+ ",appPublishTime:" + appPublishTime);
					}
					//break;
				} 
			}
		}
		

	/*	if (resultCount > 0) {
			return true;
		} else {
			//更改状态为抓取失败
			String insertSql = "update app_crawl_wait set app_status= ? where app_id = ?";
			Object[] obj = new Object[] {3,appId};
			dao.update(insertSql, obj);
			return false;
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
