package com.pxene.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.pxene.service.AppStatisticsService;
/**
 * 
 * Created by @author wangzhenlin on @date 2017年5月4日
 */


@Controller
@RequestMapping("/appStatistics")
public class AppStatisticsController {
	@Autowired
	AppStatisticsService appStatisticsService;
	
	//统计信息  微信统计
			@RequestMapping(value = "queryWechatStatistics")
			@ResponseBody
			public Map<String, Object> queryWechatStatistics(@RequestBody JSONObject params) {
				
				Integer page=params.getIntValue("startPage");
				Integer num=params.getIntValue("pageSize");
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				try {
					date = simpleDateFormat.parse(params.getString("date"));
				} catch (Exception e) {
				}
				java.sql.Date queryDate = new java.sql.Date(date.getTime());
				return appStatisticsService.queryWechatStatistics(page,num,queryDate);
			}
	
	//用户数统计 按Url,省份和数据类型
		@RequestMapping(value = "queryUsersSix")
		@ResponseBody
		public Map<String, Object> queryUsersSix(@RequestBody JSONObject params) {
			
			Integer page=params.getIntValue("startPage");
			Integer num=params.getIntValue("pageSize");
			Integer indusrtyId=params.getInteger("indusrtyId");
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			try {
				date = simpleDateFormat.parse(params.getString("date"));
			} catch (Exception e) {
			}
			java.sql.Date queryDate = new java.sql.Date(date.getTime());
			return appStatisticsService.queryUsersSix(page,num,queryDate,indusrtyId);
		}
	
	//用户数统计 按Url,省份和数据类型
	@RequestMapping(value = "queryUsersFive")
	@ResponseBody
	public Map<String, Object> queryUsersFive(@RequestBody JSONObject params) {
		
		Integer page=params.getIntValue("startPage");
		Integer num=params.getIntValue("pageSize");
		Integer categoryId=params.getInteger("categoryId");
		Integer indusrtyId=params.getInteger("indusrtyId");
		String appName=params.getString("appName");
		String appSource=params.getString("source");
		Integer appBehavior=params.getInteger("appBehavior");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(params.getString("date"));
		} catch (Exception e) {
		}
		java.sql.Date queryDate = new java.sql.Date(date.getTime());
		return appStatisticsService.queryUsersFive(page,num,queryDate,categoryId,indusrtyId,appName,appSource,appBehavior);
		
	}
	
			//用户数统计 按Url
			@RequestMapping(value = "queryUsersFour")
			@ResponseBody
			public Map<String, Object> queryUsersFour(@RequestBody JSONObject params) {
				
				Integer page=params.getIntValue("startPage");
				Integer num=params.getIntValue("pageSize");
				Integer categoryId=params.getInteger("categoryId");
				Integer indusrtyId=params.getInteger("indusrtyId");
				String appName=params.getString("appName");
				String appSource=params.getString("source");
				Integer appBehavior=params.getInteger("appBehavior");
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				try {
					date = simpleDateFormat.parse(params.getString("date"));
				} catch (Exception e) {

				}
				java.sql.Date queryDate = new java.sql.Date(date.getTime());
				
				
				return appStatisticsService.queryUsersFour(page,num,queryDate,categoryId,indusrtyId,appName,appSource,appBehavior);
				
			}
	
	//用户数统计 按App,省份和数据类型
		@RequestMapping(value = "queryUsersThree")
		@ResponseBody
		public Map<String, Object> queryUsersThree(@RequestBody JSONObject params) {
			
			Integer page=params.getIntValue("startPage");
			Integer num=params.getIntValue("pageSize");
			Integer categoryId=params.getInteger("categoryId");
			Integer indusrtyId=params.getInteger("indusrtyId");
			String appName=params.getString("appName");
			String appSource=params.getString("source");
			Integer appBehavior=params.getInteger("appBehavior");
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			try {
				date = simpleDateFormat.parse(params.getString("date"));
			} catch (Exception e) {

			}
			java.sql.Date queryDate = new java.sql.Date(date.getTime());
			
			
			return appStatisticsService.queryUsersThree(page,num,queryDate,categoryId,indusrtyId,appName,appSource,appBehavior);
		}
	
	//用户数统计 按App
	@RequestMapping(value = "queryUsersTwo")
	@ResponseBody
	public Map<String, Object> queryUsersTwo(@RequestBody JSONObject params) {
		
		Integer page=params.getIntValue("startPage");
		Integer num=params.getIntValue("pageSize");
		Integer categoryId=params.getInteger("categoryId");
		Integer indusrtyId=params.getInteger("indusrtyId");
		String appName=params.getString("appName");
		String appSource=params.getString("source");
		Integer appBehavior=params.getInteger("appBehavior");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(params.getString("date"));
		} catch (Exception e) {

		}
		java.sql.Date queryDate = new java.sql.Date(date.getTime());
		
		
		return appStatisticsService.queryUsersTwo(page,num,queryDate,categoryId,indusrtyId,appName,appSource,appBehavior);
	}
	
	//用户数统计 按省份和数据类型
	@RequestMapping(value = "queryUsersOne")
	@ResponseBody
	public Map<String, Object> queryUsersOne(@RequestBody JSONObject params) {
		
		Integer page=params.getIntValue("startPage");
		Integer num=params.getIntValue("pageSize");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(params.getString("date"));
		} catch (Exception e) {

		}
		java.sql.Date queryDate = new java.sql.Date(date.getTime());
		
		
		return appStatisticsService.queryUsersOne(page,num,queryDate);
	}
	
	@RequestMapping(value = "queryAppBehavior")
	@ResponseBody
	public Map<String, Object> queryAppBehaviorStatistics(@RequestBody JSONObject params) {
		
		Integer page=params.getIntValue("startPage");
		Integer num=params.getIntValue("pageSize");
		Integer categoryId=params.getInteger("categoryId");
		String appSource=params.getString("source");
		Integer indusrtyId=params.getInteger("indusrtyId");
		Integer behaviorId=params.getInteger("appBehavior");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(params.getString("startDate"));
		} catch (Exception e) {

		}
		java.sql.Date startDate = new java.sql.Date(date.getTime());
		try {
			date = simpleDateFormat.parse(params.getString("endDate"));
		} catch (Exception e) {

		}
		java.sql.Date endDate = new java.sql.Date(date.getTime());
		
		return appStatisticsService.queryAppBehaviorStatistics(page,num,categoryId,appSource,indusrtyId,behaviorId,startDate,endDate);
	}
	
	@RequestMapping(value = "queryAppSource")
	@ResponseBody
	public Map<String, Object> queryAppSourceStatistics(@RequestBody JSONObject params) {
		
		Integer page=params.getIntValue("startPage");
		Integer num=params.getIntValue("pageSize");
		Integer categoryId=params.getInteger("categoryId");
		Integer appBehavior=params.getInteger("appBehavior");
		Integer indusrtyId=params.getInteger("indusrtyId");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(params.getString("startDate"));
		} catch (Exception e) {

		}
		java.sql.Date startDate = new java.sql.Date(date.getTime());
		try {
			date = simpleDateFormat.parse(params.getString("endDate"));
		} catch (Exception e) {

		}
		java.sql.Date endDate = new java.sql.Date(date.getTime());
		
		return appStatisticsService.queryAppSourceStatistics(page,num,categoryId,appBehavior,indusrtyId,startDate,endDate);
	}
	
	
	@RequestMapping(value = "queryAppCategory")
	@ResponseBody
	public Map<String, Object> queryAppCategoryStatistics(@RequestBody JSONObject params) {
		
		Integer page=params.getIntValue("startPage");
		Integer num=params.getIntValue("pageSize");
		String appSource=params.getString("source");
		Integer appBehavior=params.getInteger("appBehavior");
		Integer indusrtyId=params.getInteger("indusrtyId");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(params.getString("startDate"));
		} catch (Exception e) {

		}
		java.sql.Date startDate = new java.sql.Date(date.getTime());
		try {
			date = simpleDateFormat.parse(params.getString("endDate"));
		} catch (Exception e) {

		}
		java.sql.Date endDate = new java.sql.Date(date.getTime());
		
		return appStatisticsService.queryAppCategoryStatistics(page,num,appSource,appBehavior,indusrtyId,startDate,endDate);
	}
	/*
	 * 参数分类统计
	 */
	@RequestMapping(value = "queryIndustry")
	@ResponseBody
	public Map<String, Object> queryIndustryStatistics(@RequestBody JSONObject params) {
	
		Integer page=params.getIntValue("startPage");
		Integer num=params.getIntValue("pageSize");
		String categoryId=params.getString("categoryId");
		String appSource=params.getString("source");
		Integer appBehavior=params.getInteger("appBehavior");
		Integer indusrtyId=params.getInteger("indusrtyId");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(params.getString("startDate"));
		} catch (Exception e) {

		}
		java.sql.Date startDate = new java.sql.Date(date.getTime());
		try {
			date = simpleDateFormat.parse(params.getString("endDate"));
		} catch (Exception e) {

		}
		java.sql.Date endDate = new java.sql.Date(date.getTime());
		
		return appStatisticsService.queryIndustryStatistics(page,num,categoryId,appSource,appBehavior,indusrtyId,startDate,endDate);
	}
	
	@RequestMapping(value = "queryStatisticsCrawlDetail")
	@ResponseBody
	public Map<String, Object> queryStatisticsCrawlDetail(@RequestBody JSONObject params) {
		Integer page=params.getIntValue("startPage");
		Integer num=params.getIntValue("pageSize");
		Integer industryId=params.getInteger("industry_id");
		Integer behaviorId=params.getInteger("behavior_id");
		String appName=params.getString("crawl_app_name");
		Integer categoryId=params.getInteger("category_id");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(params.getString("startDate"));
		} catch (Exception e) {

		}
		java.sql.Date startDate = new java.sql.Date(date.getTime());
		try {
			date = simpleDateFormat.parse(params.getString("endDate"));
		} catch (Exception e) {

		}
		java.sql.Date endDate = new java.sql.Date(date.getTime());
		return appStatisticsService.queryStatisticsCrawlDetail(page,num,industryId,behaviorId,appName,categoryId,startDate,endDate);
	}
}
