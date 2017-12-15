package com.pxene.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.ansj.domain.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.pxene.appcategory.ClassifyForApp;

@Controller
@RequestMapping("/appInfo")
public class ClassifyForAppController {
	@Autowired
	ClassifyForApp classifyForApp;

	@RequestMapping(value = "classifyApp")
	@ResponseBody
	public Map<String, Object> classifyApp(@RequestParam("appName") String appName) throws IOException {
		// String appName="1别踩白块儿2__bannar";
		// String appName="宝宝";
		appName = URLDecoder.decode(appName,"utf-8");
		Map<String, Object> returnMap = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		appName = new String(appName.getBytes("ISO8859-1"), "utf-8");
		String appType = classifyForApp.classifyApp(appName);
		if (!StringUtils.contains(appType, ",")) {
			if (StringUtils.contains(appType, ":")) {
				Map<String, Object> map = new HashMap<>();
				String[] appTypeUnitArr = StringUtils.split(appType, ":");
				map.put("appCategory", appTypeUnitArr[0]);
				map.put("appWeight", appTypeUnitArr[1]);
				list.add(map);
				returnMap.put("resultCode", 1);
				returnMap.put("appCategoryList", list);
			} else if(StringUtils.equals(appType, "2")){
				returnMap.put("resultCode", 2);
				returnMap.put("appCategoryList", list);
			}else if(StringUtils.equals(appType, "3")){
				returnMap.put("resultCode", 3);
				returnMap.put("appCategoryList", list);
			}
		} else {
			String[] appTypeArr = StringUtils.split(appType, ",");
			for (String appTypeStr : appTypeArr) {
				Map<String, Object> map = new HashMap<>();
				String[] appTypeUnitArr = StringUtils.split(appTypeStr, ":");
				map.put("appCategory", appTypeUnitArr[0]);
				map.put("appWeight", appTypeUnitArr[1]);
				list.add(map);
			}
			returnMap.put("resultCode",1);
			returnMap.put("appCategoryList", list);
		}
		return returnMap;
	}
}
