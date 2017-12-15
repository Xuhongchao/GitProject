package com.pxene.scheduler;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pxene.crawler.Crawler4360AppList;
import com.pxene.crawler.Crawler4AppStoreList;
import com.pxene.crawler.Crawler4BaiduAppList;
import com.pxene.crawler.Crawler4MiAppList;
import com.pxene.crawler.Crawler4QQAppList;

public class CrawlerListTriggerService {
	private Log logger = LogFactory.getLog(CrawlerListTriggerService.class);
	
	public void doIt() throws Exception{
		logger.info("定时任务:开始执行了...");
		Crawler4QQAppList qqList=new Crawler4QQAppList();
		logger.info("腾讯应用宝:榜单开始抓取了...");
		qqList.startCrawler();
		
		Crawler4BaiduAppList baiduList=new Crawler4BaiduAppList();
		logger.info("百度手机助手:榜单开始抓取了...");
		baiduList.startCrawler();
		
		Crawler4MiAppList miList=new Crawler4MiAppList();
		logger.info("小米应用市场:榜单开始抓取了...");
		miList.startCrawler();
		
		Crawler4360AppList listFor360=new Crawler4360AppList();
		logger.info("360手机助手:榜单开始抓取了...");
		listFor360.startCrawler();
		
		Crawler4AppStoreList appStore=new Crawler4AppStoreList();
		logger.info("AppStore:榜单开始抓取了...");
		appStore.startCrawler();
		
		System.out.println("定时任务CrawlerListTriggerService执行完毕:  " + new Date());
	}
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("conf/spring-mybatis.xml");
	}
}
