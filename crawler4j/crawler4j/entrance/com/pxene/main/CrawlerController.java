package com.pxene.main;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class CrawlerController {
	public static void main(String[] args) throws Exception {

		String crawlStorageFolder = "C:\\a";
		int numberOfCrawlers = 2;

		CrawlConfig config = new CrawlConfig();
		config.setMaxDepthOfCrawling(1);
		config.setCrawlStorageFolder(crawlStorageFolder);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher,
				robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		controller.addSeed("https://v.autohome.com.cn/~html");
		controller.addSeed("https://v.autohome.com.cn/");

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.startNonBlocking(MyCrawlerOfVideo.class, numberOfCrawlers);

		// Thread.sleep(30 * 1000);

		// Send the shutdown request and then wait for finishing
		// controller.shutdown();
		controller.waitUntilFinish(); // 直到完成
		System.out.println("爬虫任务结束");
	}
}
