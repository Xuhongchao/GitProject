package com.pxene.service;

import java.util.Map;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年5月4日
 */
public interface AppStatisticsService {

	Map<String, Object> queryIndustryStatistics(Integer page, Integer num, String categoryId, String appSource,
			Integer appBehavior, Integer indusrtyId, java.sql.Date startDate, java.sql.Date endDate);

	Map<String, Object> queryAppCategoryStatistics(Integer page, Integer num, String appSource, Integer appBehavior,
			Integer indusrtyId, java.sql.Date startDate, java.sql.Date endDate);

	Map<String, Object> queryAppSourceStatistics(Integer page, Integer num, Integer categoryId, Integer appBehavior,
			Integer indusrtyId, java.sql.Date startDate, java.sql.Date endDate);

	Map<String, Object> queryAppBehaviorStatistics(Integer page, Integer num, Integer categoryId, String appSource,
			Integer indusrtyId, Integer behaviorId, java.sql.Date startDate, java.sql.Date endDate);

	Map<String, Object> queryUsersOne(Integer page, Integer num, java.sql.Date queryDate);

	Map<String, Object> queryUsersTwo(Integer page, Integer num, java.sql.Date queryDate, Integer categoryId,
			Integer indusrtyId, String appName, String appSource, Integer appBehavior);

	Map<String, Object> queryUsersThree(Integer page, Integer num, java.sql.Date queryDate, Integer categoryId,
			Integer indusrtyId, String appName, String appSource, Integer appBehavior);

	Map<String, Object> queryUsersFour(Integer page, Integer num, java.sql.Date queryDate, Integer categoryId,
			Integer indusrtyId, String appName, String appSource, Integer appBehavior);

	Map<String, Object> queryUsersFive(Integer page, Integer num, java.sql.Date queryDate, Integer categoryId,
			Integer indusrtyId, String appName, String appSource, Integer appBehavior);

	Map<String, Object> queryUsersSix(Integer page, Integer num, java.sql.Date queryDate, Integer indusrtyId);

	Map<String, Object> queryWechatStatistics(Integer page, Integer num, java.sql.Date queryDate);

	Map<String, Object> queryStatisticsCrawlDetail(Integer page, Integer num, Integer industryId, Integer behaviorId,
			String appName, Integer categoryId, java.sql.Date startDate, java.sql.Date endDate);
}
