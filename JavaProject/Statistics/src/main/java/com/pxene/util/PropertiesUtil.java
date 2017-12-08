package com.pxene.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by @author xuhongchao on @date 2017年 月 日 下午2:56:14
 */

public class PropertiesUtil {
	private String[] SheetNames = new String[3]; // 用来放配置文件中读出的文件名数据

	/**
	 * 读取配置文件，拿到里面的文件名信息
	 */
	public String[] getPropertiesValue() {
		Properties pro = new Properties();
		try {
			pro.load(PropertiesUtil.class
					.getResourceAsStream("/SheetName.properties"));
		} catch (IOException e) {
			throw new RuntimeException("读取配置文件出错");
		}

		String people1 = pro.getProperty("people1");
		String people2 = pro.getProperty("people2");
		String people3 = pro.getProperty("people3");

		SheetNames[0] = people1;
		SheetNames[1] = people2;
		SheetNames[2] = people3;

		return SheetNames;
	}

}
