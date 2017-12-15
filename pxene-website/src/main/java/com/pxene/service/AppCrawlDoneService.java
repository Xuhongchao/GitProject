package com.pxene.service;

import java.util.Map;

import com.pxene.crawler.model.AppCrawlList;
import com.pxene.model.AppCrawlDetail;
import com.pxene.model.AppInfo;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月28日
 */
public interface AppCrawlDoneService {

	Map<String,Object> addAppInfo(AppInfo app_info,int choseId,String choseName);
	Map<String,Object> updateAppInfo(AppInfo app_info,int choseId);
	Map<String,Object> delAppInfo(int id,String appNum);
	
	int queryByName(String appName);
	HSSFWorkbook exportApp();
	Map<String,Object> queryByAppName(String appName);
	Map<String,Object> updateAppOs(Map<String, Object> appInfo);
	Map<String,Object> addTransfer(AppInfo app_info);
	//m
	Map<String, Object> modifyAppCrawlerDone(int crawlId, String version, int categoryId);
	Map<String, Object> deleteAppCrawlerDone(int crawlId);
	Map<String, Object> updateExport(int crawlId, int isexport);
	String queryCrawlInfoNum(Map numMap);
	Map<String, Object> addCrawlInfo(AppCrawlDetail appCrawlDetail, Integer appOs, String appNum, String behaviorCode,
			String behaviorNum);
	boolean updateCrawlBehaviorCount(int crawlId);
	Map<String, Object> modifyCrawlInfo(AppCrawlDetail appCrawlDetail);
	Map<String, Object> queryAppSource();
	void checkReg(String regList);
	HSSFWorkbook generateAppWorkBook();
	HSSFWorkbook generateRulesWorkBook();
	Map<String, Object> deleteAppCrawlerDetail(Integer crawlDetailId);
	Map<String, Object> queryByPageAndNum(java.sql.Date startDate, java.sql.Date endDate, Integer parentCategoryId,
			Integer childCategoryId, String appName, String appNumber, int page, int num, String orderName,
			String orderRule);
	Map<String, Object> queryTransferType(String crawlAppNum);
	Map<String, Object> addDomain(String crawlAppNum, String domain, String attachParam, String typeNum);
	Map<String, Object> delDomain(Integer domainId);
	Map<String, Object> queryDomainByPageAndNum(String crawlAppNum, int page, int num);
	Map<String, Object> modifyDomain(int domainId, String crawlAppNum, String domain, String attachParam,
			String typeNum);
	Map<String, Object> queryAppCrawlDetail(String startDate, String endDate, int crawlId, String industryName,
			Integer behaviorId, int page, int num, String orderName, String orderRule);
}
