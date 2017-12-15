package com.pxene.crawler;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Options;

import com.pxene.utils.CookieList;
import com.pxene.utils.CrawlerConfig;
import com.pxene.utils.CrawlerConfig.LoginConf;
import com.pxene.utils.FileUtils;
import com.pxene.utils.SeedParser;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.CrawlController.WebCrawlerFactory;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.crawler.authentication.AuthInfo;
import edu.uci.ics.crawler4j.crawler.authentication.FormAuthInfo;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class CrawlerManager {

	public static void main(String[] args) throws Exception {
		CrawlerManager cm = new CrawlerManager();
		cm.startCrawler("com.pxene.crawler.Crawler4QQAppList");
	}
	
	public void startCrawler(final String className){
		// 基本配置
		String crawlStorageFolder = "temp";
		int numberOfCrawlers = 1;
		// 默认的ua
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
		// 命令行配置
		Options options = new Options();
		options.addOption("crawlPages", true, "input class name of crawler");
		options.addOption("runner", true,
				"input full class name of Custom crawler");

		// className:auto.Crawler4Autohome
		CrawlConfig config = new CrawlConfig();
		// 读取配置文件
		String confPath = "/" + (className).replace(".", "/") + ".json";
		CrawlerConfig conf = CrawlerConfig.load(confPath);
		LoginConf loginConf = conf.getLoginConf();
		// 初始化cookie
		CookieList.init(className, loginConf);

		if (loginConf.isEnable()) {
			List<AuthInfo> infos = new ArrayList<AuthInfo>();
			AuthInfo info = null;
			try {
				info = new FormAuthInfo(loginConf.getUsername(),
						loginConf.getPassword(), loginConf.getUrl(),
						loginConf.getUsrkey(), loginConf.getPwdkey());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			infos.add(info);
			config.setAuthInfos(infos);
		}
		config.setUserAgentString(userAgent);

		// 抓取深度
		// config.setMaxDepthOfCrawling(5);
		// 最大网页数
		config.setMaxPagesToFetch(1000000);
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setSocketTimeout(20000);
		config.setConnectionTimeout(20000);
		// 可恢复的爬取数据(如果爬虫意外终止或者想要实现增量爬取，可以通过设置这个属性来进行爬取数据)
		// config.setResumableCrawling(true);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		// 默认关闭robot协议
		robotstxtConfig.setEnabled(false);
		robotstxtConfig.setUserAgentName(userAgent);
		RobotstxtServer robotstxtServer = new RobotstxtServer(
				robotstxtConfig, pageFetcher);
		CrawlController controller = null;
		try {
			controller = new CrawlController(config,
					pageFetcher, robotstxtServer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 加载种子
		for (String seed : conf.getSeeds()) {
			for (String s : SeedParser.parse(seed)) {
				controller.addSeed(s);
			}
		}

		/*List<String> seeds = FileUtils.readFile("/category.txt");
		if(seeds.size() > 0){
			for(String seed:seeds){
				controller.addSeed(seed);
			}
		}*/

		controller.start(new WebCrawlerFactory<WebCrawler>() {
			@Override
			public WebCrawler newInstance() throws Exception {
				return (WebCrawler) Class.forName(className).newInstance();
			}
		}, numberOfCrawlers);
	}
}