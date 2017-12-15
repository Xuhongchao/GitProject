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
public class Crawler4BaiduApp extends BaseCrawler {

	public Crawler4BaiduApp() {
		super("/com/pxene/crawler/Crawler4BaiduAppList.json");
	}

	private static final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
	private Log logger = LogFactory.getLog(Crawler4BaiduApp.class);
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	private static final String APP_CATEGORY_LIST = "http://shouji\\.baidu\\.com/software/(.*)";
	private static final String APP_DETAIL = "http://shouji\\.baidu\\.com/software/([\\d]*).html";
	private static final String TEXT = "text";
	private static final String HREF = "href";
	private static final String APP_SOURCE = "2";// baidu
	private static int repeat = 0;
	private static BaseDao dao = new BaseDao();
	private static List<AppCategory> appCategorylist = new ArrayList<AppCategory>();

	/*
	 * static { String app_sql = "select * from app_category;"; appCategorylist
	 * = dao.getForList(AppCategory.class, app_sql); }
	 */

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && href.matches(APP_CATEGORY_LIST);
	}

	public void startCrawler(String appName,String source,String updateAppId) {
		 analyzeAppListPage(appName,source,updateAppId);
	}

	public static void main(String[] args) {
		String appName = "开心消消乐";
		Crawler4BaiduApp casl = new Crawler4BaiduApp();
		casl.startCrawler(appName,"6",null);
	}

	/**
	 * 解析AppList页面
	 * 
	 * @param url
	 */
	private void analyzeAppListPage(String appName,String source,String updateAppId) {
			boolean result=false;
			//为查询的AppName编码
			String appNameEncode="";
			String sourceStr=source+APP_SOURCE+",";
			try {
				appNameEncode = URLEncoder.encode(appName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			Document list_doc = connectUrl(
					"http://shouji.baidu.com/s?wd=" + appNameEncode + "&data_type=app&f=header_app%40input%40btn_search");

			Elements detail_elements = list_doc.select("#doc > div.yui3-g > div > div > ul > li");
			Iterator<Element> detail_iterator = detail_elements.iterator();
			boolean crawlState=false;
			while (detail_iterator.hasNext()) {
				Element detail_element = detail_iterator.next();
				// 获取到AppName
				Elements element = detail_element.select("div > div.info > div.top >a");
				if (element.text().contains(appName)) {
					
					String detail_href =element.attr("href");
					analyzeAppDetailPage("http://shouji.baidu.com" + detail_href,sourceStr,updateAppId,appName);
//					if(result){
//						crawlState=true;
//					}
					//break;
				}
		}
			
//			if(!crawlState){
//				//更改状态为抓取失败
//				String insertSql = "update app_crawl_wait set app_status= ? where app_id = ?";
//				Object[] obj = new Object[] {3,appId};
//				dao.update(insertSql, obj);
//			}
			//return result;
	}

	/**
	 * 解析AppDetail页面
	 * 
	 * @param ranking
	 * 
	 * @param detail_href
	 */
	private void analyzeAppDetailPage(String url,String sourceStr,String updateAppId,String appName) {
		Integer resultCount=0;
		logger.info("app_detail_url:" + url);
		Document detailDoc = connectUrl(url);
		if (detailDoc == null) {
			repeat++;
			analyzeAppDetailPage(url,sourceStr,updateAppId,appName);
		}
		//#doc > div.app-nav > div > span:nth-child(5) > a
		
		// 1.获取AppName
		String appNameCrawl = getTextBySelector(detailDoc,
				"#doc > div.yui3-g > div > div.app-intro > div > div.content-right > h1 > span", TEXT, null);
		String appAppId= getTextBySelector(detailDoc,
				"#doc > div.yui3-g > div > div.app-intro > div > div.area-download > a", "data-tj", ("[a-z]+_(\\d+)_"));
		String href = getTextBySelector(detailDoc,
				"#doc > div.app-nav > div > span:nth-child(5) > a", "href","");
		//int appRank = getAppRank("http://shouji.baidu.com" + href, appName);
		
		// 2.获取AppApkName
		String appApkName = StringUtils.regexpExtract(url, APP_DETAIL);
		// 3.获取AppCategoryId    #doc > div.app-nav > div > span:nth-child(5) > a
		String categoryName = getTextBySelector(detailDoc,
				"#doc > div.app-nav > div > span:nth-child(5) > a", "text","");
		// 4.获取AppVersion
		String appVersion = getTextBySelector(detailDoc,
				"#doc > div.yui3-g > div > div.app-intro > div > div.content-right > div.detail > span.version", TEXT,
				"版本:(.*)");
		// 5.获取AppLogoUrl
		String appLogoUrl = getTextBySelector(detailDoc,
				"#doc > div.yui3-g > div > div.app-intro > div > div.content-left > div > img", "src", null);
		// 6.获取AppRanking1
		Long appRanking1 = 0L;
		String downloadNum = (getTextBySelector(detailDoc,
				"#doc > div.yui3-g > div > div.app-intro > div > div.content-right > div.detail > span.download-num", TEXT, null));
		String[] split = org.apache.commons.lang.StringUtils.split(downloadNum,":");
		if(org.apache.commons.lang.StringUtils.contains(split[1], "万")){
			downloadNum=org.apache.commons.lang.StringUtils.substring(split[1], 0,split[1].indexOf("万"));
			appRanking1=Long.parseLong(org.apache.commons.lang.StringUtils.trim(downloadNum))*10000;
		}else if(org.apache.commons.lang.StringUtils.contains(split[1], "亿")){
			downloadNum=org.apache.commons.lang.StringUtils.substring(split[1],0, split[1].indexOf("亿"));
			appRanking1=(long) ((Double.parseDouble(org.apache.commons.lang.StringUtils.trim(downloadNum)))*10000*10000);
		}else {
			appRanking1=(long) ((Double.parseDouble(org.apache.commons.lang.StringUtils.trim(split[1]))));
		}
		// 7.获取AppCreateDatetime
		String appCreateDatetime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
		// 8.获取AppPublishTime
		String appPublishTime = timeStamp2Date(System.currentTimeMillis() + "", null, false);
		// 9.获取AppDownloadUrl
		String appDownloadUrl = getTextBySelector(detailDoc,
				"#doc > div.yui3-g > div > div.app-intro > div > div.area-one-setup > span", "data_url", null);
		
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

		System.out.println("detail_url:appId:" + appAppId + ",appNameCrawl:" + appNameCrawl + ",appVersion:" + appVersion
				+ ",appLogoUrl:" + appLogoUrl + ",appApkName:" + appApkName + ",appPublishTime:" + appPublishTime);
/*		if(resultCount==0){
			return false;
		}else{
			return true;
		}*/
	}
	
	
	/**
	 * 根据app名称在指定url中获取app排名
	 * @param url
	 * @param appName
	 * @return
	 */
	public int getAppRank(String url,String appName){
		
		int ranking=1;
		Document list_doc = connectUrl(url);
		Elements detail_elements = list_doc
				.select("#doc > div.list.big-app-wrap > div.sec-app.clearfix > div > ul > li > a");
		Iterator<Element> detail_iterator = detail_elements.iterator();
		while (detail_iterator.hasNext()) {
			Element detail_element = detail_iterator.next();
			//#doc > div.list.big-app-wrap > div.sec-app.clearfix > div > ul > li:nth-child(2) > a > div.app-detail > p.name
			String name =detail_element.select("div.app-detail > p.name").text();
			if(org.apache.commons.lang.StringUtils.equals(appName, name)){
				return ranking;
			}
			ranking++;
		}
		//若网站爬取不到排名,设置为500
		ranking=500;
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
