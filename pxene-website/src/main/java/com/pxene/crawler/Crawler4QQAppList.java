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
import org.json.JSONArray;
import org.json.JSONObject;
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

public class Crawler4QQAppList extends BaseCrawler {

	public Crawler4QQAppList() {
		super("/" + Crawler4QQAppList.class.getName().replace(".", "/")
				+ ".json");
	}

	private static final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
	private Log logger = LogFactory.getLog(Crawler4QQAppList.class);
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	private static final String APP_LIST = "^http://sj\\.qq\\.com/myapp/category.htm\\?orgame=([\\d]*)&categoryId=([\\d]*)$|"
			+ "^http://sj\\.qq\\.com/myapp/category\\.htm\\?categoryId=([\\d]*)&orgame=([\\d]*)$";
	private static final String APP_LIST_JSON = "http://sj.qq.com/myapp/cate/appList.htm";
	private static final String APP_DETAIL = "http://sj\\.qq\\.com/myapp/detail\\.htm\\?apkName=(.*)";
	private static final String APP_DETAIL_PREIX = "http://sj.qq.com/myapp/";
	private static final int PAGE_SIZE = 20;
	private static final int MAX_SIZE = 100;
	private static final String TEXT = "text";
	private static final String HREF = "href";
	private static final String APP_SOURCE = "1";//腾讯应用宝
	private static int ranking = 0;
	private static BaseDao dao = new BaseDao();
	private static List<AppCategory> appCategorylist = new ArrayList<AppCategory>();

	/*static {
		String app_sql = "select * from app_category;";
		appCategorylist = dao.getForList(AppCategory.class, app_sql);
	}*/

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && href.matches(APP_LIST);
	}

/*	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		if (url.matches(APP_LIST)) {
//			analyzeAppListPage(url);
			analyzeAppListJson(url);
//		} else if (url.matches(APP_DETAIL)) {
//			analyzeAppDetailPage(url);
		}
	}*/
	
	public void startCrawler() {
		Properties properties = ConfigUtil.loadConfigFileByPath("/conf/qqphone.properties");
		Set<Entry<Object, Object>> entrySet = properties.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			String url=entry.getValue().toString();
			try {
				analyzeAppListJson(url);
			} catch (Exception e) {
				logger.error("抓取 "+ url+" 出错!");
				e.printStackTrace();
			}
		}
		properties.clear();
	}

	public static void main(String[] args) {
		Crawler4QQAppList casl = new Crawler4QQAppList();
		casl.startCrawler();
	}

	
	/**
	 * 解析AppList页面JSON数据
	 * 
	 * @param url
	 */
	private void analyzeAppListJson(String url) {
		//ApplicationContext context = new ClassPathXmlApplicationContext("conf/spring-mybatis.xml");
		// AppCrawlWaitService appCrawlWaitService = context.getBean(AppCrawlWaitService.class);
		String sourceStr=","+APP_SOURCE+",";
		String url_suffix = url.substring(url.indexOf("?"));
		for (int i = 0; i < MAX_SIZE; i += PAGE_SIZE) {
			Document list_doc = connectUrl(APP_LIST_JSON + url_suffix
					+ "&pageSize=" + PAGE_SIZE + "&pageContext=" + i);
			String jsonStr = getTextBySelector(list_doc, "html > body", TEXT,
					"");
			try{
				JSONObject jsonObject = new JSONObject(jsonStr);
				JSONArray results = (JSONArray) jsonObject.get("obj");
				for (int j = 0; j < results.length(); j++) {
					JSONObject object = (JSONObject) results.get(j);
					if (object != null) {
						String appNameCrawl = object.get("appName").toString();
						String appAppId=object.get("appId").toString();
						String categoryName = object.get("categoryName").toString();
						String appVersion = object.get("versionName").toString();
						String appPublishDatetime = timeStamp2Date(
								System.currentTimeMillis() + "", null, false);
						String appLogoUrl = object.get("iconUrl").toString();
						//String appApkName = object.get("pkgName").toString();
						String appDownloadUrl = object.get("apkUrl").toString();
						//int appCategoryId = 0;
						/*for (AppCategory entity : appCategorylist) {
							if (entity.getApp_category_name().equals(appCategory)) {
								appCategoryId = entity.getId();
								break;
							}
						}*/
						Long rank=object.getLong("appDownCount") ;
						String app_create_datetime = timeStamp2Date(
								System.currentTimeMillis() + "", null, false);
						//int rank = i + j + 1;
						
						String select_sql ="select * from app_crawl_wait where app_appId=? and app_source like ?"; 
						AppCrawlList appCrawlList = dao.get(AppCrawlList.class,
						select_sql, new Object[] { appAppId,"%,"+APP_SOURCE+",%" });
						
						// 若为未爬取 或爬取失败,对App信息进行添加
						if(appCrawlList == null) {
							
							String insertSql="insert into app_crawl_wait(app_priority,app_ranking1,app_logo_url,app_name,app_name_crawl,app_category_name,app_version,app_update_time,app_source,app_create_time,app_status,app_is_blacklist,app_type,app_download_url,app_appId,app_version_update) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
							Object[] obj = new Object[] { 3, rank, appLogoUrl,"",appNameCrawl,categoryName,
									appVersion, appPublishDatetime, sourceStr, app_create_datetime, 1, 0,2,appDownloadUrl, appAppId,appVersion};
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
							Object[] obj = new Object[] { rank, appLogoUrl, appVersion,appPublishDatetime, appDownloadUrl, appVersionUpdate,appCrawlList.getApp_id() };
							 dao.update(updateSql, obj);
						}
						System.out.println("detail_url:appAppId:" + appAppId + ",appNameCrawl:" + appNameCrawl + ",appVersion:" + appVersion + ",appPublishTime:" + appPublishDatetime + ",app_source:" + APP_SOURCE + ",appLogoUrl:" + appLogoUrl);
					}
				}
			}catch (Exception e){

			}
		}
	}
	
	/**
	 * 解析AppDetail页面数据
	 * 
	 * @param url
	 *//*
	private void analyzeAppDetailPage(String url) {
		logger.info("detail_url:" + url);
		Document detail_doc = connectUrl(url);
		ranking++;
		// 1.获取appName
		String appName = getTextBySelector(
				detail_doc,
				"#J_DetDataContainer > div > div.det-ins-container.J_Mod > div.det-ins-data > div.det-name > div.det-name-int",
				TEXT, null);
		// 2.获取appCategory
		String appCategory = getTextBySelector(detail_doc, "#J_DetCate", TEXT,
				null);
		// 3.获取appVersion
		String appVersion = getTextBySelector(
				detail_doc,
				"#J_DetDataContainer > div > div.det-othinfo-container.J_Mod > div:nth-child(2)",
				TEXT, "v(.*)");
		// 4.获取appPublishTime
		String appPublishDatetime = timeStamp2Date(System.currentTimeMillis()
				+ "", null, false);
		// 5.获取appLogoUrl
		String appLogoUrl = getTextBySelector(
				detail_doc,
				"#J_DetDataContainer > div > div.det-ins-container.J_Mod > div.det-icon > img",
				"src", null);
		// 6.获取app包名
		Pattern pattern = Pattern.compile(APP_DETAIL);
		Matcher matcher = pattern.matcher(url);
		String appApkName = "";
		if (matcher.find()) {
			appApkName = matcher.group(1);
		}
		int app_category_id = 0;
		for (AppCategory entity : appCategorylist) {
			if (entity.getApp_category_name().equals(appCategory)) {
				app_category_id = entity.getId();
				break;
			}
		}
		// 7.获取下载地址
		String appDownloadUrl = getTextBySelector(detail_doc, "#J_DetDataContainer > div > div.det-ins-container.J_Mod > div.det-ins-btn-box > a.det-ins-btn", "ex_url", null);
		
		String app_create_datetime = timeStamp2Date(System.currentTimeMillis()
				+ "", null, false);
		String select_sql = "select * from app_crawl_list where app_name = ?";
		AppCrawlList appCrawlList = dao.get(AppCrawlList.class, select_sql,
				new Object[] { appName });
		if (appCrawlList == null) {// insert
			String insertSql = "insert into app_crawl_wait (app_priority,app_ranking1,app_logo_url,app_name,app_version,app_update_time,app_source,app_create_time,app_status,app_is_blacklist,app_type,app_download_url) values (?,?,?,?,?,?,?,?,?,?,?,?)";
			Object[] obj = new Object[] { 3, ranking, appLogoUrl, appName, appVersion, appPublishDatetime, APP_SOURCE,
					app_create_datetime, 1, 0, 2, appDownloadUrl };
			dao.update(insertSql, obj);
		} else if(appCrawlList.getApp_source().equals(APP_SOURCE)){// update
			String updateSql = "update app_crawl_wait set app_ranking1 = ?,app_ranking2 = ?,app_logo_url= ?,app_version = ?,app_update_time = ?,app_source=?,app_download_url = ?  where app_id = ?";
			Object[] obj = new Object[] { ranking, appCrawlList.getApp_ranking1(), appLogoUrl, appVersion,
					appPublishDatetime,APP_SOURCE ,appDownloadUrl, appCrawlList.getApp_id() };
			dao.update(updateSql, obj);
		}
		System.out.println("detail_url:appRanking:" + ranking + ",appName:" + appName + ",appCategory:" + appCategory + ",appVersion:" + appVersion + ",appPublishTime:" + appPublishDatetime + ",appLogoUrl:" + appLogoUrl + ",appApkName:" + appApkName);

	}
*/

	/**
	 * 解析AppList页面
	 * 
	 * @param url
	 *//*
	private void analyzeAppListPage(String url) {
		logger.info("list_url:" + url);
		Document list_doc = connectUrl(url);
		Elements list_div_elements = list_doc
				.select("body > div.category-wrapper.clearfix > div.main");
		Iterator<Element> list_div_iterator = list_div_elements.iterator();
		while (list_div_iterator.hasNext()) {
			Element list_div_element = list_div_iterator.next();
			Elements list_div_a_elements = list_div_element
					.select("div.main > ul > li > div > a");
			Iterator<Element> list_div_a_iterator = list_div_a_elements
					.iterator();
			while (list_div_a_iterator.hasNext()) {
				Element list_div_a_element = list_div_a_iterator.next();
				String detail_url = list_div_a_element.attr(HREF);
				myController.addSeed(APP_DETAIL_PREIX + detail_url);
				System.out.println("detail_url:" + APP_DETAIL_PREIX
						+ detail_url);
			}
		}

	}*/

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
