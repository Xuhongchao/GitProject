package com.pxene.dao;

import java.util.List;
import java.util.Map;

import com.pxene.crawler.model.AppCrawlList;

public interface AppCrawlWaitMapper {

	
	int queryAllNum(Map map);
	int queryIosAllNum(Map map);
	List<Map<String, Object>> queryByName(Map<String, String> map);
	
	void updateStatusById(Map<String,Object> map);
	String queryNameById(Map<String,Object> map);
	boolean updateExport(AppCrawlList appCrawlList);
	//m
	List<Map<String, Object>> queryAppSource(Map<String, Object> map);
	List<Map<String, Object>> queryAppCategory();
	Integer queryCrawlerWaitTotalCount(Map<String, Object> map);
	List<Map<String, Object>> queryByPageAndNum(Map<String,Object> map);
	String queryAppCategoryName(Map<String, Object> map);
	boolean modifyPriority(Map<String, Object> map);
	boolean addWeb(Map<String, Object> map);
	boolean updateBlacklist(Map<String, Object> map);
	List<Map<String, Object>> queryByAppId(Map<String, Object> map);
	boolean updateAppStatus(Map<String, Object> map);
	Integer querySource(Map<String, Object> map);
	boolean addSource(Map<String, Object> map);
	List<Map<String, Object>> queryAppExist(Map<String, Object> map);
	boolean updateApp(Map<String, Object> map);
	List<Map<String, Object>> queryAppNameExist(Map<String, Object> map);
	String querySourceName(Map<String, Object> queryMap);
	Integer queryAppStatus(Map<String, Object> map);
	List<Map<String, Object>> queryCrawlerWaitApp();
	List<Map<String, Object>> queryUpdateApp();
	Integer queryCrawlerWaitWebTotalCount(Map<String, Object> map);
	List<Map<String, Object>> queryWebByPageAndNum(Map<String, Object> map);
	List<Map<String, Object>> queryAppNameEmptyExist(Map<String, Object> map);
	void modifyWaitCrawlAppName(Map<String, Object> map);
	List<Map<String, Object>> queryCrawlAppCategory(Map<String, Object> map);
	List<Map<String, Object>> queryAppParentCategory();
	List<Map<String, Object>> queryAppChildCategory(Map<String, Object> map);
	void delCrawlWaitApp(Map<String, Object> map);
	List<Map<String, Object>> queryCrawlWaitAppForUpdate(Map<String, Object> map);
	List<Map<String, Object>> queryTransferAppByName();
	String querySourceByAppId(Map<String, Object> map);
	List<Map<String, Object>> queryAppCategoryByAppId(Map<String, Object> map);
	List<Map<String, Object>> queryCrawlAppByCrawlId(Map<String, Object> map);
	void modifyVersionUpdate(Map<String, Object> versionMap);
}
