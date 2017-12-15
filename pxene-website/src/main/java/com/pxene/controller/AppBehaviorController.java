package com.pxene.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pxene.service.AppBehaviorService;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月19日
 */
@Controller
@RequestMapping("/appBehavior")
public class AppBehaviorController {
	@Autowired
	AppBehaviorService appBehaviorService;
	
	@RequestMapping(value = "queryAllBehavior")
	@ResponseBody
	public Map<String, Object> queryAllBehavior() {
		return appBehaviorService.queryAllBehavior();
	}

	@RequestMapping(value = "queryByPageAndNum")
	@ResponseBody
	public Map<String, Object> queryByPageAndNum(@RequestBody JSONObject params) {
		String behaviorName = params.getString("behavior_name");
		String categoryName = params.getString("category_name");
		int page = params.getIntValue("startPage");
		int num = params.getIntValue("pageSize");
		return appBehaviorService.queryByPageAndNum(page, num, behaviorName, categoryName);
	}

	@RequestMapping(value = "queryAppCategory")
	@ResponseBody
	public Map<String, Object> queryAppCategory() {
		return appBehaviorService.queryAppCategory();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "addBehavior")
	@ResponseBody
	public Map<String, Object> addBehavior(@RequestBody JSONObject params) {
		String behaviorName = (params.getString("behavior_name"));
		//String appCategoryIdList = ();
		List<Integer> appCategoryIdList= JSON.parseObject(params.getString("app_category_id_list"),List.class);
		return appBehaviorService.addBehavior(behaviorName, appCategoryIdList);
	}

	@RequestMapping(value = "deleteBehavior")
	@ResponseBody
	public Map<String, Object> deleteBehavior(@RequestBody JSONObject params) {
		String behaviorId = (params.getString("behavior_id"));
		return appBehaviorService.deleteBehavior(behaviorId);
	}
}
