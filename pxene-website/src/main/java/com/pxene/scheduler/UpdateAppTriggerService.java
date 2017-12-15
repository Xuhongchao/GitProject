package com.pxene.scheduler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pxene.service.impl.AppCrawlWaitServiceImpl;

public class UpdateAppTriggerService {
	private Log logger = LogFactory.getLog(CrawlerListTriggerService.class);

	public void doIt() throws Exception {
		logger.info("定时任务:开始执行了...");
		ApplicationContext ioc=new ClassPathXmlApplicationContext("conf/spring-mybatis.xml");
		AppCrawlWaitServiceImpl appCrawlWaitService = ioc.getBean(AppCrawlWaitServiceImpl.class);
		appCrawlWaitService.updateCrawlWaitApp();
		logger.info("updateCrawlWaitApp:App更新完毕!");
		//System.out.println("method:doIt:  " + new Date());
	}

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("conf/spring-mybatis.xml");
	}
}
