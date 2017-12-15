package com.pxene.dao;

import java.util.List;
import java.util.Map;

import com.pxene.crawler.model.AppCrawlList;
import com.pxene.model.AppCrawlDetail;

public interface AppCrawlDetailMapper {

	List<Map<String, Object>> queryByPageAndNum(Map<String, Object> map);

	Integer queryTotalCount(Map<String, Object> map);

	String queryCrawlDetailNum(Map<String, Object> query);

	boolean addAppCrawlDetail(Map<String, Object> map);

	Integer queryCrawlBehaviorCount(Map<String, Object> update);

	boolean modifyAppCrawlDetail(Map<String, Object> map);

	Integer queryAppCountStatistics(Map<String, Object> query);

	Integer queryCrawlBehaviorCountStatistics(Map<String, Object> query);

	void checkReg(Map<String, Object> map);

	String queryCrawlDetailReg(Map<String, Object> query);

	Integer queryCrawlBehaviorCountStatisticsDetail(Map<String, Object> query);

	Integer queryCrawlId(Map<String, Object> map);

	AppCrawlDetail selectByPrimaryKey(Integer crawlDetailId);

	boolean modifyAppCrawlDetailComments(Map<String, Object> map);

	Integer queryAppCrawlDetailExist(Map<String, Object> map);

	String queryAdIDExist(Map<String, Object> query);

	String queryIndustryName(Map<String, Object> queryIndustryName);

	List<Map<String, Object>> queryExportDomainDetail();

	List<String> queryExportRegDetailNum();

	List<Map<String, Object>> queryExportRegDetail(Map<String, Object> query);

	List<Map<String, Object>> queryExportParamDetail(Map<String, Object> query);

	String queryCrawlDetailParamReg(Map<String, Object> query);

	List<Map<String, Object>> queryCrawlDetailBehavior();

	List<Map<String, Object>> queryStatisticsIndustryDetail(Map<String, Object> map);

	Integer queryStatisticsIndustryDetailTotalCount(Map<String, Object> map);
}
