package com.pxene.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pxene.model.AppCrawlInfo;

public interface AppCrawlInfoMapper {

	int queryAllByAppId(Map<String,Object> map);
	int queryAllByIndustryId(Map<String,Object> map);
	int queryDomainAndUrlRegByAppId(Map<String,Object> map);
	String queryCrawlInfoNum(Map<String,Object> map);
	boolean addAppCrawlInfo(Map<String,Object> map);
	AppCrawlInfo queryById(AppCrawlInfo app_crawlInfo);
	boolean updateAppCrawlInfo(AppCrawlInfo app_crawlInfo);
	boolean delAppCrawlInfo(Map<String,Object> map);
	boolean delByAppId(Map<String,Object> map);
	List<Map<String, Object>> queryByIndustryId(Map<String,Object> map);
	List<Map<String, Object>> queryByAppInfoId(Map<String,Object> map);
	boolean testAppCrawlInfo(AppCrawlInfo appCrawlInfo);
	List<AppCrawlInfo> exportAdid();
	List<AppCrawlInfo> exportSearch();
	List<AppCrawlInfo> exportApiList();
}
