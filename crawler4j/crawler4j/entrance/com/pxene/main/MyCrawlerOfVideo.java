package com.pxene.main;

import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pxene.util.WRUtil;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * 自定义爬虫类
 * 
 * @author xuhongchao
 *
 */
public class MyCrawlerOfVideo extends WebCrawler {
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|gif|jpg" + "|png|zip|gz))$");

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&& href.startsWith("https://v.autohome.com.cn/v-");
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();

			// 得到文档对象
			Document doc = Jsoup.parse(html);

			Element video = doc.select("div[class=H5VideoContainer]").first();
			String str = video.select("video").first().attr("src");

			System.out.println(str);

			// ----------- 解析规则 -----------
			/*
			 * Element article = doc.getElementById("youkuplayer"); Elements
			 * videos = article.getElementsByTag("video"); for (Element video :
			 * videos) { String videoSrc = video.attr("src");
			 * System.out.println(videoSrc); }
			 */
			// System.out.println(str);
			// System.out.println("==============================");

			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());

		}
	}
}
