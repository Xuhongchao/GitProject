package com.pxene.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by @author xu on @date 2017年 12月 24日 下午8:57:34
 */
public class StringUtil {
	/**
	 * 将url开头的http或者https去掉
	 * 
	 * @param url
	 * @return 返回截取后的字符串
	 */
	public static String killBeginOfUrl(String url) {
		if (url.startsWith("http://")) {
			url = url.split("http://")[1];
		} else if (url.startsWith("https://")) {
			url = url.split("https://")[1];
		}
		return url;
	}

	/**
	 * 对url进行解码
	 * 
	 * @param str
	 *            原字符串
	 * @return 解码后的字符串
	 */
	public static String decode(String str) {
		String value = null;
		try {
			value = URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value;
	}
}
