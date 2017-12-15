package com.pxene.utils;

import java.util.Iterator;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月10日
 */
public class WordSegUtil {

	private static final Logger logger = LogManager.getLogger(WordSegUtil.class);

	// 中文分词
	public static String chnSeg(String content) {
		String strResult = "";
		try {
			// 分词
			Result segWords = ToAnalysis.parse(content);
			// 提取分词
			Iterator<Term> segTerms = segWords.iterator();
			StringBuffer strbuf = new StringBuffer();
			while (segTerms.hasNext()) {
				Term tm = segTerms.next();
				String strNs = tm.getNatureStr();// 获取词性
				System.out.println(strNs);
				char cns = strNs.charAt(0);// 取词性第一个字母
				System.out.println(cns);
				// 介词p、连词c、助词u、叹词e、拟声词o、语气词y,代词r,中文标点符号w
				if (cns != 'p' && cns != 'c' && cns != 'u' && cns != 'e' && cns != 'o' && cns != 'y' && cns != 'r'
						&& cns != 'w' || strNs.equals("en") || strNs.equals("userDefine")) {
					String strNm = tm.getName();
					strbuf.append(strNm + " ");
				}
				// strbuf.append("\r\n");//换行
			}
			strResult = strbuf.toString();
			strResult = strResult.substring(0, strResult.length() - 1);// 截取最后一个字符
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.debug("分词失败");
		}
		return strResult;
	}
}
