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
public class MyCrawlerOfArt extends WebCrawler {
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|gif|jpg" + "|png|mp3|mp4|zip|gz))$");

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&& href.startsWith("https://www.autohome.com.cn/news/201711/");
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

			// ----------- 解析规则 -----------
			// 得到时间的元素节点
			Elements times = doc
					.select("#articlewrap > div.article-info > span:nth-child(1)");
			int i = 0;
			for (Element t : times) {
				String time = t.text();
				if (!"2017年11月09日".equals(time.split(" ")[0])) {
					continue;
				}
				// System.out.println(time);
			}

			System.out.println("==============================");

			Element article = doc.getElementById("articleContent");
			Elements words = article.getElementsByTag("p");
			String str = "";
			for (Element word : words) {
				str += word.text();
			}
			// System.out.println(str);
			// 将爬到的数据写到文件中
			WRUtil.write2File("C:\\a\\a.txt", str.toCharArray());

			str = "";

			System.out.println("==============================");

			/*
			 * System.out.println("Text length: " + text.length());
			 * System.out.println("Html length: " + html.length());
			 * System.out.println("Number of outgoing links: " + links.size());
			 */
		}
	}
}
