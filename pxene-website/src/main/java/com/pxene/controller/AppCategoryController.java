package com.pxene.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.pxene.model.AppCategory;
import com.pxene.service.AppCategoryService;
import com.pxene.service.impl.AppCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月20日
 */
@Controller
@RequestMapping("/appCategory")
public class AppCategoryController {

	@Autowired
	AppCategoryService appCategoryService;
	
	@RequestMapping(value = "delChildCategory")
	@ResponseBody
	public Map<String,Object> delChildCategory(@RequestBody JSONObject params){
		String childCategoryId=params.getString("child_category_id");
		return appCategoryService.delChildCategory(childCategoryId);
	}
	
	
	@RequestMapping(value = "queryChildCategoryIsUsed")
	@ResponseBody
	public Map<String,Object> queryChildCategoryIsUsed(@RequestBody JSONObject params){
		String childCategoryId=params.getString("child_category_id");
		return appCategoryService.queryChildCategoryIsUsed(childCategoryId);
	}
	
	@RequestMapping(value = "modifyChildCategory")
	@ResponseBody
	public Map<String,Object> modifyChildCategory(@RequestBody JSONObject params){
		String childCategoryId=params.getString("child_category_id");
		String childCategory=params.getString("child_category");
		return appCategoryService.modifyChildCategory(childCategoryId, childCategory);
	}
	
	@RequestMapping(value = "queryByPageAndNum")
	@ResponseBody
	public Map<String,Object> queryByPageAndNum(@RequestBody JSONObject params){
		String categoryName=params.getString("category_name");
		int page=params.getIntValue("startPage");
		int num=params.getIntValue("pageSize");
		return appCategoryService.queryPageAndNum(page, num,categoryName);
	}
	
	@RequestMapping(value = "addCategory")
	@ResponseBody
	public Map<String,Object> addCategory(@RequestBody JSONObject params){
		String parentCategory=params.getString("parentCategory");
		String childCategory=params.getString("childCategory");
		return appCategoryService.addCategory(parentCategory, childCategory);
	}
	
	@RequestMapping(value = "queryFuzzyParentCategory")
	@ResponseBody
	public Map<String,Object> queryFuzzyParentCategory(@RequestBody JSONObject params){
		String parentCategory=params.getString("category_name");
		return appCategoryService.queryFuzzyParentCategory(parentCategory);
	}
}
