package com.pxene.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pxene.dao.AppBehaviorMapper;
import com.pxene.dao.AppCategoryMapper;
import com.pxene.dao.AppCrawlDetailMapper;
import com.pxene.dao.AppCrawlDoneMapper;
import com.pxene.dao.AppIndustryMapper;
import com.pxene.dao.AppStatisticsUsersMapper;
import com.pxene.service.AppStatisticsService;
/**
 * 
 * Created by @author wangzhenlin on @date 2017年5月4日
 */
import com.pxene.utils.ConfigUtil;

@Service
public class AppStatisticsServiceImpl implements AppStatisticsService {
	@Autowired
	AppStatisticsUsersMapper appStatisticsUsersMapper;
	@Autowired
	AppBehaviorMapper appBehaviorMapper;
	@Autowired
	AppCrawlDoneMapper appCrawlDoneMapper;
	@Autowired
	AppCategoryMapper appCategoryMapper;
	@Autowired
	AppIndustryMapper appIndustryMapper;
	@Autowired
	AppCrawlDetailMapper appCrawlDetailMapper;

	@Override
	public Map<String, Object> queryWechatStatistics(Integer page, Integer num, Date queryDate) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("num", num);
		map.put("create_time", queryDate);
		result.put("totalcount", appStatisticsUsersMapper.queryWechatStatisticsTotalCount(map));
		List<Map<String, Object>> list = appStatisticsUsersMapper.queryWechatStatistics(map);
		result.put("list", list);
		return result;
	}

	@Override
	public Map<String, Object> queryUsersSix(Integer page, Integer num, Date queryDate, Integer indusrtyId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 记录所有返回的字段
		List<String> provinceList = new ArrayList<>();
		map.put("page", page);
		map.put("num", num);
		map.put("create_time", queryDate);
		map.put("industry_id", indusrtyId);
		result.put("totalcount", appStatisticsUsersMapper.queryUsersSixTotalCount(map));
		List<Map<String, Object>> allIndustryList = appStatisticsUsersMapper.queryUsersSix(map);
		for (Map<String, Object> allIndustryMap : allIndustryList) {
			Map<String, Object> query = new HashMap<String, Object>();
			query.put("users_industry_num", allIndustryMap.get("users_industry_num").toString());
			query.put("users_type", (int) allIndustryMap.get("users_type"));

			List<Map<String, Object>> infoList = appStatisticsUsersMapper.queryUsersSixInfo(query);
			for (Map<String, Object> infoMap : infoList) {
				String provinceName = infoMap.get("province_name").toString();
				if (infoMap.get("province_name") != null && !provinceList.contains(provinceName)) {
					provinceList.add(provinceName);
				}
				allIndustryMap.put(provinceName, (int) infoMap.get("users_count"));
			}
		}
		result.put("fieldList", provinceList);
		result.put("list", allIndustryList);
		return result;
	}

	@Override
	public Map<String, Object> queryUsersFive(Integer page, Integer num, Date queryDate, Integer categoryId,
			Integer indusrtyId, String appName, String appSource, Integer appBehavior) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 记录所有返回的字段
		List<String> provinceList = new ArrayList<>();
		map.put("page", page);
		map.put("num", num);
		map.put("create_time", queryDate);
		map.put("category_id", categoryId);
		map.put("industry_id", indusrtyId);
		map.put("app_name", appName);
		map.put("app_source", appSource);
		map.put("behavior_id", appBehavior);
		result.put("totalcount", appStatisticsUsersMapper.queryUsersFiveTotalCount(map));
		List<Map<String, Object>> allUrlList = appStatisticsUsersMapper.queryUsersFive(map);
		for (Map<String, Object> allUrlMap : allUrlList) {
			Map<String, Object> query = new HashMap<String, Object>();
			query.put("users_crawl_detail_num", allUrlMap.get("users_crawl_detail_num").toString());
			query.put("users_type", (int) allUrlMap.get("users_type"));

			List<Map<String, Object>> infoList = appStatisticsUsersMapper.queryUsersFiveInfo(query);
			for (Map<String, Object> infoMap : infoList) {
				String provinceName = infoMap.get("province_name").toString();
				if (infoMap.get("province_name") != null && !provinceList.contains(provinceName)) {
					provinceList.add(provinceName);
				}
				allUrlMap.put(provinceName, (int) infoMap.get("users_count"));
			}
		}
		result.put("fieldList", provinceList);
		result.put("list", allUrlList);
		return result;
	}

	@Override
	public Map<String, Object> queryUsersFour(Integer page, Integer num, Date queryDate, Integer categoryId,
			Integer indusrtyId, String appName, String appSource, Integer appBehavior) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("num", num);
		map.put("create_time", queryDate);
		map.put("category_id", categoryId);
		map.put("industry_id", indusrtyId);
		map.put("app_name", appName);
		map.put("app_source", appSource);
		map.put("behavior_id", appBehavior);
		result.put("totalcount", appStatisticsUsersMapper.queryUsersFourTotalCount(map));
		List<Map<String, Object>> allAppList = appStatisticsUsersMapper.queryUsersFour(map);
		result.put("list", allAppList);
		return result;
	}

	@Override
	public Map<String, Object> queryUsersThree(Integer page, Integer num, Date queryDate, Integer categoryId,
			Integer indusrtyId, String appName, String appSource, Integer appBehavior) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 记录所有返回的字段
		List<String> provinceList = new ArrayList<>();
		map.put("page", page);
		map.put("num", num);
		map.put("create_time", queryDate);
		map.put("category_id", categoryId);
		map.put("industry_id", indusrtyId);
		map.put("app_name", appName);
		map.put("app_source", appSource);
		map.put("behavior_id", appBehavior);
		result.put("totalcount", appStatisticsUsersMapper.queryUsersThreeTotalCount(map));
		List<Map<String, Object>> allAppList = appStatisticsUsersMapper.queryUsersThree(map);
		for (Map<String, Object> allAppMap : allAppList) {
			Map<String, Object> query = new HashMap<String, Object>();
			query.put("users_app_num", allAppMap.get("users_app_num").toString());
			query.put("users_type", (int) allAppMap.get("users_type"));

			List<Map<String, Object>> infoList = appStatisticsUsersMapper.queryUsersThreeInfo(query);
			for (Map<String, Object> infoMap : infoList) {
				String provinceName = infoMap.get("province_name").toString();
				if (infoMap.get("province_name") != null && !provinceList.contains(provinceName)) {
					provinceList.add(provinceName);
				}
				allAppMap.put(provinceName, (int) infoMap.get("users_count"));
			}
		}
		result.put("fieldList", provinceList);
		result.put("list", allAppList);
		return result;
	}

	@Override
	public Map<String, Object> queryUsersTwo(Integer page, Integer num, Date queryDate, Integer categoryId,
			Integer indusrtyId, String appName, String appSource, Integer appBehavior) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("num", num);
		map.put("create_time", queryDate);
		map.put("category_id", categoryId);
		map.put("industry_id", indusrtyId);
		map.put("app_name", appName);
		map.put("app_source", appSource);
		map.put("behavior_id", appBehavior);
		result.put("totalcount", appStatisticsUsersMapper.queryUsersTwoTotalCount(map));
		List<Map<String, Object>> list = appStatisticsUsersMapper.queryUsersTwo(map);
		result.put("list", list);
		return result;
	}

	@Override
	public Map<String, Object> queryUsersOne(Integer page, Integer num, Date queryDate) {

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("num", num);
		map.put("create_time", queryDate);
		result.put("totalcount", appStatisticsUsersMapper.queryUsersOneTotalCount(map));
		List<Map<String, Object>> allProvinceList = appStatisticsUsersMapper.queryUsersOneProvince(map);
		for (Map<String, Object> provinceMap : allProvinceList) {
			Map<String, Object> query = new HashMap<String, Object>();

			query.put("users_province", provinceMap.get("users_province"));
			query.put("create_time", queryDate);
			List<Map<String, Object>> infoList = appStatisticsUsersMapper.queryUsersOneInfo(query);
			for (Map<String, Object> info : infoList) {
				if ((int) info.get("users_type") == 1) {
					provinceMap.put("3G", (int) info.get("users_count"));
				}
				if ((int) info.get("users_type") == 2) {
					provinceMap.put("4G", (int) info.get("users_count"));
				}
			}
		}
		result.put("list", allProvinceList);
		return result;
	}

	@Override
	public Map<String, Object> queryAppBehaviorStatistics(Integer page, Integer num, Integer categoryId,
			String appSource, Integer indusrtyId, Integer behaviorId, Date startDate, Date endDate) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("num", num);
		map.put("behavior_id", behaviorId);
		result.put("totalcount", appBehaviorMapper.queryAppBehaviorTotalCount(map));
		List<Map<String, Object>> allBehaviorList = appBehaviorMapper.queryAppBehaviorByPage(map);
		for (Map<String, Object> allBehaviorMap : allBehaviorList) {

			Map<String, Object> query = new HashMap<String, Object>();
			// 获取App数量
			query.put("behavior_id", (int) allBehaviorMap.get("behavior_id"));
			query.put("category_id", categoryId);
			query.put("app_source", appSource);
			query.put("industry_id", indusrtyId);
			query.put("startDate", startDate);
			query.put("endDate", endDate);
			Integer appCount = appCrawlDetailMapper.queryAppCountStatistics(query);
			if (appCount == null) {
				appCount = 0;
			}
			// 获取抓取行为数
			Integer crawlBehaviorCount = appCrawlDetailMapper.queryCrawlBehaviorCountStatisticsDetail(query);
			if (crawlBehaviorCount == null) {
				crawlBehaviorCount = 0;
			}
			allBehaviorMap.put("appCount", appCount);
			allBehaviorMap.put("crawlBehaviorCount", crawlBehaviorCount);
		}
		result.put("list", allBehaviorList);
		return result;
	}

	@Override
	public Map<String, Object> queryAppSourceStatistics(Integer page, Integer num, Integer categoryId,
			Integer appBehavior, Integer indusrtyId, Date startDate, Date endDate) {

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("num", num);
		result.put("totalcount", appCrawlDoneMapper.queryAppSourceTotalCount(map));
		List<Map<String, Object>> allSourceList = appCrawlDoneMapper.queryAppSourceByPage(map);
		// 显示 source_name crawl_app_source
		for (Map<String, Object> allSourceMap : allSourceList) {

			Map<String, Object> query = new HashMap<String, Object>();
			// 获取App数量
			query.put("app_source", allSourceMap.get("source_id").toString());
			query.put("category_id", categoryId);
			query.put("behavior_id", appBehavior);
			query.put("industry_id", indusrtyId);
			query.put("startDate", startDate);
			query.put("endDate", endDate);
			Integer appCount = appCrawlDetailMapper.queryAppCountStatistics(query);
			if (appCount == null) {
				appCount = 0;
			}
			// 获取抓取行为数
			Integer crawlBehaviorCount = appCrawlDetailMapper.queryCrawlBehaviorCountStatisticsDetail(query);
			if (crawlBehaviorCount == null) {
				crawlBehaviorCount = 0;
			}
			allSourceMap.put("appCount", appCount);
			allSourceMap.put("crawlBehaviorCount", crawlBehaviorCount);
		}
		result.put("list", allSourceList);
		return result;
	}

	@Override
	public Map<String, Object> queryAppCategoryStatistics(Integer page, Integer num, String appSource,
			Integer appBehavior, Integer indusrtyId, Date startDate, Date endDate) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("num", num);
		result.put("totalcount", appCategoryMapper.queryAppCategoryTotalCount(map));
		List<Map<String, Object>> allCategoryList = appCategoryMapper.queryAppCategoryByPage(map);
		for (Map<String, Object> allCategoryMap : allCategoryList) {

			Map<String, Object> query = new HashMap<String, Object>();
			// 获取App数量
			query.put("category_id", (Integer) allCategoryMap.get("category_id"));
			query.put("app_source", appSource);
			query.put("behavior_id", appBehavior);
			query.put("industry_id", indusrtyId);
			query.put("startDate", startDate);
			query.put("endDate", endDate);
			Integer appCount = appCrawlDetailMapper.queryAppCountStatistics(query);
			if (appCount == null) {
				appCount = 0;
			}
			// 获取抓取行为数
			Integer crawlBehaviorCount = appCrawlDetailMapper.queryCrawlBehaviorCountStatisticsDetail(query);
			if (crawlBehaviorCount == null) {
				crawlBehaviorCount = 0;
			}
			allCategoryMap.put("appCount", appCount);
			allCategoryMap.put("crawlBehaviorCount", crawlBehaviorCount);
		}
		result.put("list", allCategoryList);
		return result;
	}

	@Override
	public Map<String, Object> queryIndustryStatistics(Integer page, Integer num, String categoryId, String appSource,
			Integer appBehavior, Integer indusrtyId, Date startDate, Date endDate) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("num", num);
		map.put("industry_id", indusrtyId);
		result.put("totalcount", appIndustryMapper.queryIndustryTotalCount(map));
		List<Map<String, Object>> allIndustryList = appIndustryMapper.queryIndustryByPage(map);
		for (Map<String, Object> allIndustryMap : allIndustryList) {
			Map<String, Object> query = new HashMap<String, Object>();
			// 获取App数量
			query.put("industry_id", (Integer) allIndustryMap.get("industry_id"));
			query.put("category_id", categoryId);
			query.put("app_source", appSource);
			query.put("behavior_id", appBehavior);
			query.put("startDate", startDate);
			query.put("endDate", endDate);
			Integer appCount = appCrawlDetailMapper.queryAppCountStatistics(query);
			if (appCount == null) {
				appCount = 0;
			}
			// 获取抓取行为数
			Integer crawlBehaviorCount = appCrawlDetailMapper.queryCrawlBehaviorCountStatisticsDetail(query);
			if (crawlBehaviorCount == null) {
				crawlBehaviorCount = 0;
			}
			allIndustryMap.put("appCount", appCount);
			allIndustryMap.put("crawlBehaviorCount", crawlBehaviorCount);
		}
		result.put("list", allIndustryList);
		return result;
	}
	
	@Override
	public Map<String, Object> queryStatisticsCrawlDetail(Integer page, Integer num, Integer industryId,
			Integer behaviorId, String appName, Integer categoryId, Date startDate, Date endDate) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("num", num);
		map.put("industry_id", industryId);
		map.put("behavior_id", behaviorId);
		map.put("crawl_app_name", appName);
		map.put("crawl_app_category_id", categoryId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		Integer totalCount = appCrawlDetailMapper.queryStatisticsIndustryDetailTotalCount(map);
		List<Map<String, Object>> list = appCrawlDetailMapper.queryStatisticsIndustryDetail(map);
		for (Map<String, Object> industryDetailMap : list) {
			//处理创建时间数据
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String createTime = sdf.format(industryDetailMap.get("crawl_detail_create_time"));
			industryDetailMap.put("crawl_detail_create_time", createTime);
			// 添加访问数总和
			Map<String, Object> query = new HashMap<>();
			query.put("app_num", industryDetailMap.get("crawl_app_num"));
			String deadline = appCrawlDoneMapper.queryDeadine(query);
			query.put("create_time", deadline);
			Integer totalVisits = appCrawlDoneMapper.queryTotalVisits(query);
			if (deadline == null) {
				industryDetailMap.put("deadline", "");
			} else {
				industryDetailMap.put("deadline", deadline);
			}

			if (totalVisits == null) {
				industryDetailMap.put("totalVisits", "");
			} else {
				industryDetailMap.put("totalVisits", totalVisits);
			}
		}
		result.put("totalcount", totalCount);
		result.put("list", list);
		return result;
	}
}
