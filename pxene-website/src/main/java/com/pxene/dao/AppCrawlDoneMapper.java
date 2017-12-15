package com.pxene.dao;

import java.util.List;
import java.util.Map;

public interface AppCrawlDoneMapper {

	boolean addAppCrawlDone(Map<String, Object> map);

	boolean addAppWaitDone(Map<String, Object> map);

	String queryMaxAppInfoNum();

	boolean updateAppCrawlDone(Map<String, Object> map);

	String queryAppNum(Map<String, Object> map);

	List<Map<String, Object>> queryAppNumAndCrawlId(Map<String, Object> map);

	List<Map<String, Object>> queryCrawlDetailId(Map<String, Object> map);

	boolean updateAppCrawlDetail(Map<String, Object> map);

	// 已爬取App模块
	Integer queryTotalCount(Map<String, Object> map);

	List<Map<String, Object>> queryByPageAndNum(Map<String, Object> map);

	boolean modifyAppCrawlerDone(Map<String, Object> map);

	Integer queryAppCrawlDetailCount(Map<String, Object> map);
	
	Integer queryAppCrawlDomainCount(Map<String, Object> map);

	boolean deleteAppCrawlerDone(Map<String, Object> map);

	boolean updateExport(Map<String, Object> map);

	boolean updateCrawlBehaviorCount(Map<String, Object> update);

	Integer queryTotalVisits(Map<String, Object> query);

	Integer queryAppSourceTotalCount(Map<String, Object> map);

	List<Map<String, Object>> queryAppSourceByPage(Map<String, Object> map);

	List<Map<String, Object>> queryAppSource();

	List<Map<String, Object>> queryExportAppDetail();

	List<Map<String, Object>> queryExportRulesDetail();

	String queryDeadine(Map<String, Object> query);

	boolean deleteAppCrawlerDetail(Map<String, Object> map);

	boolean deleteAppWaitDone(Map<String, Object> map);

	boolean updateWaitAppStatus(Map<String, Object> map);

	Integer queryAppCrawlerDoneExist(Map<String, Object> map);

	Integer queryCrawlerDetailDataExist4Four(Map<String, Object> map);

	Integer queryCrawlerDetailDataExist4Five(Map<String, Object> map);

	List<Map<String, Object>> queryExportRuleDetail(Map<String, Object> query);

	List<Map<String, Object>> queryExportApilistDetail();

	List<Map<String, Object>> queryCrawlAppDoneByAppId(Map<String, Object> map);

	List<String> queryCrawlAppOS(Map<String, Object> map);

	List<Map<String, Object>> queryRelatedApp(Map<String, Object> query);

	List<Map<String, Object>> queryTransferType(Map<String, Object> map);

	void addDomain(Map<String, Object> map);

	void delDomain(Map<String, Object> map);

	List<Map<String, Object>> queryDomainByPageAndNum(Map<String, Object> map);

	Integer queryDomainTotalCount(Map<String, Object> map);

	Integer queryDomainExist(Map<String, Object> map);

	List<Map<String, Object>> queryDomainTypeNum(Map<String, Object> map);

	void modifyDomain(Map<String, Object> map);

	void updateDomainExport(Map<String, Object> map);

	Integer queryCrawlAppExportStatus(Map<String, Object> map);

	Integer queryIsExport(Map<String, Object> map);

	List<Map<String, Object>> queryAppIdByCrawlId(Map<String, Object> map);

}
