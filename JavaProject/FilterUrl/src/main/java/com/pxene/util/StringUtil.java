package com.pxene.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by @author xu on @date 2017�� 12�� 24�� ����8:57:34
 */
public class StringUtil {
	/**
	 * ��url��ͷ��http����httpsȥ��
	 * 
	 * @param url
	 * @return ���ؽ�ȡ����ַ���
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
	 * ��url���н���
	 * 
	 * @param str
	 *            ԭ�ַ���
	 * @return �������ַ���
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
