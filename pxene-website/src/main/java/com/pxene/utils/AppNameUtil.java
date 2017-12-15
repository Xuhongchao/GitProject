package com.pxene.utils;

public class AppNameUtil {

	public static String getAppName(String str) {
		String appName = "";
		// String removeSpace = removeSpace(str);
		if (str.contains(" ")) {
			int indexOf = str.indexOf(" ");
			appName = str.substring(0, indexOf);
		} else if (str.contains("(")) {
			int indexOf = str.indexOf("(");
			appName = str.substring(0, indexOf);
		} else if (str.contains("（")) {
			int indexOf = str.indexOf("（");
			appName = str.substring(0, indexOf);
		} else if (str.contains("—")) {
			int indexOf = str.indexOf("—");
			appName = str.substring(0, indexOf);
		} else if (str.contains("-")) {
			int indexOf = str.indexOf("-");
			appName = str.substring(0, indexOf);
		} else if (str.contains("－")) {
			int indexOf = str.indexOf("－");
			appName = str.substring(0, indexOf);
		} else {
			appName = str;
		}
		return appName;
	}

	/**
	 * 去掉所有空格
	 * 
	 * @param str
	 * @return
	 */
	public static String removeSpace(String str) {

		return str.replaceAll("[\\p{Space}]+", "");
	}

	public static void main(String[] args) {
		String app = "QQ同步助手 刷机必备通讯录短信一键备份";
		String appName = getAppName(app);
		System.out.println(appName);
	}
}
