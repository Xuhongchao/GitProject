package com.pxene.service;

import com.pxene.crawler.model.AppCrawlList;

import java.util.List;
import java.util.Map;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月25日
 */
public interface AppCrawlWaitService {

	Map<String, Object> queryById(Integer id);

	List<Map<String, Object>> queryByName(String appName);

	Map<String, Object> checkAppInfo(int id);

	// m
	List<Map<String, Object>> queryAppSource(Integer appType);

	List<Map<String, Object>> queryAppCategory();

	Map<String, Object> queryPageAndNum(int page, int num, String categoryName, String appSource, int status,
			String appName, int appType);

	Map<String, Object> modifyPriority(int appId, int appPriority);

	Map<String, Object> updateBlacklist(int appId, int isBlacklist);

	Map<String, Object> appTransfer(Map<String, Object> map);

	List<String> getAppCategory(String appName);

	Map<String, Object> addAppOrWeb(Integer appType, String appName, String appSource);

	Map<String, Object> queryWebPageAndNum(int page, int num, String appSource, int status, String appName,
			int appType);

	Map<String, Object> modifyWaitCrawlAppName(int appId, String appName);

	Map<String, Object> queryCrawlAppCategory(Integer appType, String appSource);

	Map<String, Object> queryAppParentCategory();

	Map<String, Object> queryAppChildCategory(Integer categoryId);

	Map<String, Object> delCrawlWaitApp(Integer appId);

	void updateCrawlWaitApp();

	Map<String, Object> queryTransferAppByName();

	Map<String, Object> queryAppCategoryByAppId(Integer appId);
}
