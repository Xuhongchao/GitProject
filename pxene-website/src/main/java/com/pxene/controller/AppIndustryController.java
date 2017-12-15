package com.pxene.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.pxene.model.AppIndustry;
import com.pxene.service.AppIndustryService;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年5月11日
 */
@Controller
@RequestMapping("/appIndustry")
public class AppIndustryController {

	@Autowired
	private AppIndustryService appIndustryService;
	
	@RequestMapping(value = "checkReg")
	@ResponseBody
	public void checkReg(@RequestBody JSONObject params) {
		
		String regList = params.getString("regList");
		appIndustryService.checkReg(regList);
	}

	@RequestMapping(value = "queryAllIndustry")
	@ResponseBody
	public Map<String, Object> queryAllIndustry() {
		return appIndustryService.queryAllIndustry();
	}
	
	@RequestMapping(value = "addIndustry")
	@ResponseBody
	public Map<String, Object> addIndustry(@RequestBody JSONObject params) {
		String industryName = (params.getString("industry_name"));
		String contentName = (params.getString("content_name"));
		int contentOrder = (params.getIntValue("content_order"));
		return appIndustryService.add(industryName, contentName, contentOrder);
	}
	
	@RequestMapping(value = "queryByPageAndNum")
	@ResponseBody
	public Map<String, Object> queryByPageAndNum(@RequestBody JSONObject params) {
		String industryNum = params.getString("industryNum");
		String industryName = params.getString("industryName");
		int page = params.getIntValue("startPage");
		int num = params.getIntValue("pageSize");
		String orderName = params.getString("orderName");
		String orderRule = params.getString("orderRule");
		return appIndustryService.queryByPageAndNum(page, num, industryNum, industryName,orderName,orderRule);
	}

	@RequestMapping(value = "deleteIndustry")
	@ResponseBody
	public Map<String, Object> deleteIndustry(@RequestBody JSONObject params) {
		int contentId = params.getIntValue("content_id");
		String industryNum = params.getString("industry_num");
		return appIndustryService.deleteIndustryContent(contentId,industryNum);
	}
	
	@RequestMapping(value = "queryContentDetail")
	@ResponseBody
	public Map<String, Object> queryContentDetail(@RequestBody JSONObject params) {
		
		int contentId = params.getIntValue("content_id");
		int page = params.getIntValue("startPage");
		int num = params.getIntValue("pageSize");
		return appIndustryService.queryContentDetail(contentId,page,num);
	}
	
	@RequestMapping(value = "addRegular")
	@ResponseBody
	public Map<String, Object> addRegular(@RequestBody JSONObject params) {
		
		Integer contentId = params.getInteger("content_id");
		String urlExample = params.getString("url_example");
		String paramReg = params.getString("param_reg");
		return appIndustryService.addRegular(contentId,urlExample,paramReg);
	}
	
	@RequestMapping(value = "modifyRegular")
	@ResponseBody
	public Map<String, Object> modifyRegular(@RequestBody JSONObject params) {
		
		int contentId = params.getIntValue("content_id");
		int contentDetailId = params.getIntValue("content_detail_id");
		String urlExample = params.getString("url_example");
		String paramReg = params.getString("param_reg");
		return appIndustryService.modifyRegular(contentId,contentDetailId,urlExample,paramReg);
	}
	
	@RequestMapping(value = "deleteRegular")
	@ResponseBody
	public Map<String, Object> deleteRegular(@RequestBody JSONObject params) {
		int contentDetailId = params.getIntValue("content_detail_id");
		return appIndustryService.deleteRegular(contentDetailId);
	}
}
