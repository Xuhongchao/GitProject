package com.pxene.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pxene.entity.Data;
import com.pxene.util.IOUtil;

/**
 * Created by @author xuhongchao on @date 2017年 月 日 下午1:33:52
 * 
 * 进行过滤
 */

public class Core {
	private static int count = 0;
	private IOUtil ioUtil = IOUtil.getInstance();

	/**
	 * 让所有的url过滤正则，过滤流程是所有url过滤一个正则
	 * 
	 * @param list
	 *            用来取正则
	 * @param map
	 *            用来取url
	 * @param START_PATH
	 *            第一次过滤出的文件路径
	 */
	public void filter(List<Data> list, Map<String, String> map,
			String START_PATH, Map<String, Data> line) {
		Map<String, String> container = null;
		// 初始化标识为假
		boolean b = false;

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		for (Data data : list) {
			container = new HashMap<String, String>();

			String domain = data.getDomain();
			String paramReg = data.getParam();
			String urlReg = data.getUrlReg();

			// 进行匹配
			String compile = domain + urlReg;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				/*
				 * 分两步走： 首先让每一个url过（domain + urlReg） ； 然后再一个一个的过参数正则（参数正则可能有多个）；
				 * 全过之后就+1；
				 */
				String url = decode(killBeginOfUrl(entry.getValue().replaceAll(
						"%(?![0-9a-fA-F]{2})", "%25")));
				Pattern p = Pattern.compile(compile);
				Matcher m = p.matcher(url);
				b = m.matches();
				if (b) {
					String[] params = paramReg.split("\t");
					if (params.length == 1 && "NULL".equals(params[0])) {
						// 如果参数只为NULL，该情况是主域名的情况，直接跳出输出b
						container.put(entry.getKey(), entry.getValue());
						continue;
					}
					for (int i = 0; i < params.length; i++) {
						if ("NULL".equals(params[i])) {
							continue;
						}
						try {
							p = Pattern.compile(params[i]);
						} catch (Exception e) {
							System.out.println(params[i]);
							e.printStackTrace();
						}
						m = p.matcher(url);
						b = m.find();
						if (!b) { // 有一个不同即为不同
							break;
						}
					}

					if (b) {
						// 记录一下通过的url和num
						container.put(entry.getKey(), entry.getValue());
					}
				}
			}
			if (container.size() > 1) {
				// 先写出去，然后后面会做删除动作
				ioUtil.writeToFile(new File(START_PATH + (count++) + ".txt"),
						container, line);

				// System.out.println("--" + container.size());
			} else if (container.size() == 1) {
				container.clear();
			}
		}
	}

	/**
	 * 对第一次输出的目录文件再次进行过滤，找出最终的结果
	 * 
	 * @param START_PATH
	 *            res1的目录
	 * @param TEMP_PATH
	 *            res2的目录
	 */
	public void secondFilter(String START_PATH, String TEMP_PATH) {
		File file = new File(START_PATH);
		File[] files = null;
		if (file.isDirectory()) {
			files = file.listFiles();
		}

		for (int i = 0; i < files.length; i++) {
			String numStr = ioUtil.readFromTxt(files[i]);
			// 处理逻辑
			String[] nums = numStr.split("-");
			String[] temp = new String[nums.length];
			for (int j = 0; j < nums.length; j++) {
				temp[j] = nums[j].substring(8, 11);
			}
			// true代表参数相同
			boolean flag = true;
			for (int k = 0; k < temp.length - 1; k++) {
				for (int p = 1; p < temp.length; p++) {
					if (!temp[k].equals(temp[p])) {
						flag = false;
						break;
					}
				}
				if (flag == false) {
					break;
				}
			}
			System.out.println(flag);
			if (flag) { // 将参数相同的文件移动到另一个文件夹下
				files[i].renameTo(new File(TEMP_PATH + files[i].getName()));
			}
		}
	}

	/**
	 * 将url开头的http或者https去掉
	 * 
	 * @param url
	 * @return 返回截取后的字符串
	 */
	private String killBeginOfUrl(String url) {
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
	private String decode(String str) {
		String value = null;
		try {
			value = URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value;
	}
}
