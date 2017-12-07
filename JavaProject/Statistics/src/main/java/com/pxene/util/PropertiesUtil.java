package com.pxene.util;

import java.io.IOException;
import java.util.Properties;

import com.pxene.core.impl.ProcessResult;

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
			pro.load(ProcessResult.class
					.getResourceAsStream("SheetName.properties"));
		} catch (IOException e) {
			throw new RuntimeException("读取配置文件出错");
		}

		String wq = pro.getProperty("wq");
		String ysa = pro.getProperty("ysa");
		String xiy = pro.getProperty("xiy");

		SheetNames[0] = wq;
		SheetNames[1] = ysa;
		SheetNames[2] = xiy;

		return SheetNames;
	}

}
