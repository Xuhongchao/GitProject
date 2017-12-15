package com.pxene.appcategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ClassifyForKeyword {
	private static final Logger logger=LogManager.getLogger(ClassifyForKeyword.class);
	private static final String PREF_OF_ANDROID = "https://aso100.com/search/android/search/";
	private static final String PREF_OF_IOS[] = { "https://aso100.com/search/searchMore?page=",
			"&device=iphone&search=" };
	/*public static void main(String[] args) {

		String appName = "祎";
		List<String> type = getAppInfoForIOS(appName);
		System.out.println(type);
		System.out.println(type.size());
	}*/

	public static List<String> getAppInfoForIOS(String keyword) {
		List<String> appType=new ArrayList<>();
		
		int i=1;
		List<String> appTypeList = getAppInfoForIOSByPage(i, keyword);
		System.out.println("爬取关键词App集合: "+appTypeList);
		if(appTypeList.size()==20){
			appType.addAll(appTypeList);
			return appType;
		}else{
			while (appType.size()<20) {
				if(appTypeList.size()==0){
					appTypeList=getAppInfoForIOSByPage(++i, keyword);
					if(appTypeList.size()==0)
						break;
				}
//				for (String appTypeStr : appTypeList) {
//					appType.add(appTypeStr);
//					if(appType.size()==20)
//						break;
//					appTypeList.remove(appTypeStr);
//				}
				for (int j = 0; j < appTypeList.size(); j++) {
					appType.add(appTypeList.get(j));
					System.out.println("抓取的关键词类型集合大小: "+appType.size());
					System.out.println("存入关键词类型集合的 关键词类型: "+appTypeList.get(j));
					if(appType.size()==20)
						break;
					appTypeList.remove(appTypeList.get(j));
				}
			}
		}
		return appType;
	}

	/**
	 * 
	 * 在IOSAppName中通过页码,关键词获取对应类型集合
	 * @param pageNo
	 * @param keyword
	 * @return
	 */
	public static List<String> getAppInfoForIOSByPage(int pageNo,String keyword) {
		List<String> appTypeList=new ArrayList<>();
		// APP URL
		String url = PREF_OF_IOS[0] + pageNo + PREF_OF_IOS[1] + keyword;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		// 1.根据url获取doc对象
		Document detail_doc=null;
		try {
			detail_doc = connectUrl(url);
		} catch (IOException e) {
			logger.debug("getAppInfoForIOSByPage: doc获取失败!");
			e.printStackTrace();
			return	getAppInfoForIOSByPage(pageNo, keyword);
		}
		// 2.从doc对象中获取APPType
		Elements appListElements = detail_doc.select("body");
		if(StringUtils.contains(appListElements.text(),"访问太频繁了")){
			throw new RuntimeException("关键词爬取网站获取信息失败!");
		};
		Iterator<Element> appListIterator = appListElements.iterator();
		while (appListIterator.hasNext()) {
			Element appListElement = appListIterator.next();
			Elements appList_appNameElements = appListElement.select("div.media > div.media-body > h4 > a ");
			// #app-list > div:nth-child(1) > div.media-body > div.media-info >
			Iterator<Element> appList_appNameIterator = appList_appNameElements.iterator();
			while (appList_appNameIterator.hasNext()) {
				Element appList_appNameElement = appList_appNameIterator.next();
				String appName = appList_appNameElement.text();

				// 判断appName是否含有所选关键词
				if (org.apache.commons.lang.StringUtils.contains(appName, keyword)) {
					Elements appList_appTypeElements = appList_appNameElement.parent().parent()
							.select("div.media-body>div.media-info>span.media-info-category");
					String appType=appList_appTypeElements.text();
					System.out.println("app名称: "+appName);
					System.out.println("app类型: "+appType);
					appTypeList.add(appType);
				}
			}
		}
		return appTypeList;
	}
	
/**
 * 在AndroidAppName中通过关键词获取对应类型集合
 * @param keyword
 * @return
 */
	public static List<String> getAppInfoForAndroid(String keyword) {
		List<String> appTypeList=new ArrayList<>();
		// APP URL
		String url = PREF_OF_ANDROID + keyword;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		// 1.根据url获取doc对象
		Document detail_doc=null;
		try {
			detail_doc = connectUrl(url);
		} catch (IOException e) {
			logger.error("getAppInfoForAndroid: doc获取失败!");
			e.printStackTrace();
			return getAppInfoForAndroid(keyword);
		}
		// 2.从doc对象中获取APPType
		Elements appListElements = detail_doc.select("#app-list");
		Iterator<Element> appListIterator = appListElements.iterator();
		while (appListIterator.hasNext()) {
			Element appListElement = appListIterator.next();
			//#app-list > div:nth-child(1) > div.media-body > div.media-info > span
			Elements appList_appNameElements = appListElement.select("div.media > div.media-body > div.media-info > span.media-info-category ");
			// init :"div.media > div.media-body > div.media-info >
			Iterator<Element> appList_appNameIterator = appList_appNameElements.iterator();
			while (appList_appNameIterator.hasNext()) {
				Element appList_appNameElement = appList_appNameIterator.next();
				String appType = appList_appNameElement.text();
				appTypeList.add(appType);
			}
		}
		return appTypeList;
	}

	private static Document connectUrlNoProxy(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).userAgent("chrome").timeout(20000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	/**
	 * 请求数据
	 * 
	 * @param url
	 * @return
	 * @throws IOException 
	 */
	private static Document connectUrl(String url) throws IOException  {
		Document doc=null;
		String categoryString=null;
		HttpGet httpGet = new HttpGet("http://dynamic.goubanjia.com/dynamic/get/0f3d54c09a84a98a2223f35551c28e20.html");
		HttpClient httpClient1 = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient1.execute(httpGet);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			categoryString = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
		}
		String ipString = categoryString.trim();
		//TODO
		//若代理没有缴费,不使用代理
		if(StringUtils.contains(ipString, "msg")){
			return connectUrlNoProxy(url);
		}
			Connection connection = Jsoup.connect(url).userAgent("Chrome").timeout(20000);
			String[] tmpArr = ipString.split(":");
			if (tmpArr != null && tmpArr.length == 2)
			{
				connection.proxy(tmpArr[0], Integer.valueOf(tmpArr[1]));
			}
			doc = connection.get();
			if (doc.toString().contains("访问太频繁了")||doc==null) {
				Document document = connectUrl(url);
				return document;
			}
			return doc;
	}
}
