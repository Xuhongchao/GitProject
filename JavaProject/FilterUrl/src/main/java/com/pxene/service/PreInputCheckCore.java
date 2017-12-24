package com.pxene.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pxene.entity.Data;
import com.pxene.util.StringUtil;

/**
 * Created by @author xu on @date 2017年 12月 24日 下午3:42:45
 * 
 * 在输入前进行检查
 */
public class PreInputCheckCore {

	/**
	 * 在录入url进行判断该url在数据库中是否已经存在了
	 * 
	 * @param list
	 *            用来获取正则部分
	 * @param noEnterUrl
	 *            等待录入的url
	 * @return map - 存放已存在的url和未存在的url【map.put(same, diff)】
	 */
	public Map<List<String>, List<String>> check(List<Data> list, List<String> noEnterUrl) {
		Map<List<String>, List<String>> map = new HashMap<List<String>, List<String>>();
		List<String> diff = new ArrayList<String>();
		List<String> same = new ArrayList<String>();

		// 初始化标识为假
		boolean b = false;

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		for (String string : noEnterUrl) {
			String url = StringUtil.decode(StringUtil.killBeginOfUrl(string.replaceAll("%(?![0-9a-fA-F]{2})", "%25")));

			for (Data data : list) {
				String domain = data.getDomain();
				String paramReg = data.getParam();
				String urlReg = data.getUrlReg();
				// 进行匹配
				String compile = domain + urlReg;
				Pattern p = Pattern.compile(compile);
				Matcher m = p.matcher(url);
				b = m.matches();
				if (b) {
					String[] params = paramReg.split("\t");
					if (params.length == 1 && "NULL".equals(params[0])) {
						// 如果参数只为NULL，该情况是主域名的情况，直接跳出输出true
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
				}

				if (b) { // 满足主域名、url正则、所有参数正则的才算是true
					same.add(string);
					break; // 如果存在一个相同的，就可以退出循环
				}
			}

			/*
			 * 情况描述：
			 * 1，不满足主域名+url正则组合条件的
			 * 2，不满足参数正则条件的，只要有一个不满足的就算（可以满足其中的一个或者几个）
			 */
			if (!b) {
				diff.add(string);
			}
		}
		map.put(same, diff);
		return map;
	}
}
