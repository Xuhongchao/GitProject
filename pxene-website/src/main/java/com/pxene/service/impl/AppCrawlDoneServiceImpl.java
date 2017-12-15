package com.pxene.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.pxene.dao.AppBehaviorMapper;
import com.pxene.dao.AppCategoryMapper;
import com.pxene.dao.AppCrawlDetailMapper;
import com.pxene.dao.AppCrawlDoneMapper;
import com.pxene.dao.AppCrawlWaitMapper;
import com.pxene.dao.AppIndustryMapper;
import com.pxene.dao.AppInfoDetailMapper;
import com.pxene.dao.AppInfoMapper;
import com.pxene.model.AppCrawlDetail;
import com.pxene.model.AppInfo;
import com.pxene.service.AppCrawlDoneService;
import com.pxene.utils.CodeNumUtils;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月27日
 */
@Service("appInfoService")
public class AppCrawlDoneServiceImpl implements AppCrawlDoneService {
	private Log logger = LogFactory.getLog(AppCrawlWaitServiceImpl.class);
	@Autowired
	AppCrawlWaitMapper appCrawlerWaitMapper;
	@Autowired
	AppBehaviorMapper appBehaviorMapper;
	@Autowired
	AppIndustryMapper appIndustryMapper;
	@Autowired
	AppCrawlDetailMapper appCrawlDetailMapper;
	@Autowired
	AppCrawlDoneMapper appCrawlDoneMapper;
	@Autowired
	AppInfoMapper appInfoMapper;
	@Autowired
	AppCategoryMapper appCategoryMapper;
	@Autowired
	AppInfoDetailMapper appInfoDetailMapper;

	@Override
	public HSSFWorkbook generateRulesWorkBook() {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet1 = workbook.createSheet("adidlist");
		Map<String, Object> query = new HashMap<>();
		query.put("num", "0055");
		List<Map<String, Object>> exportAppDetail1 = appCrawlDoneMapper.queryExportRuleDetail(query);
		int i = 0;
		for (Map<String, Object> exportAppMap : exportAppDetail1) {
			HSSFRow frow2 = sheet1.createRow(i++);
			frow2.createCell(0).setCellValue(exportAppMap.get("crawl_detail_num").toString());
			frow2.createCell(1).setCellValue(exportAppMap.get("crawl_detail_domain").toString());
			frow2.createCell(2).setCellValue(exportAppMap.get("crawl_detail_urlreg").toString());
			frow2.createCell(3).setCellValue(exportAppMap.get("crawl_detail_paramreg").toString());
		}

		HSSFSheet sheet2 = workbook.createSheet("searchlist");
		query.put("num", "0057");
		List<Map<String, Object>> exportAppDetail2 = appCrawlDoneMapper.queryExportRuleDetail(query);
		int j = 0;
		for (Map<String, Object> exportAppMap : exportAppDetail2) {
			HSSFRow frow2 = sheet2.createRow(j++);
			frow2.createCell(0).setCellValue(exportAppMap.get("crawl_detail_num").toString());
			frow2.createCell(1).setCellValue(exportAppMap.get("crawl_detail_domain").toString());
			frow2.createCell(2).setCellValue(exportAppMap.get("crawl_detail_urlreg").toString());
			frow2.createCell(3).setCellValue(exportAppMap.get("crawl_detail_paramreg").toString());
		}

		HSSFSheet sheet3 = workbook.createSheet("apilist");
		List<Map<String, Object>> exportAppDetail3 = appCrawlDoneMapper.queryExportApilistDetail();
		int k = 0;
		for (Map<String, Object> exportAppMap : exportAppDetail3) {
			HSSFRow frow2 = sheet3.createRow(k++);
			frow2.createCell(0).setCellValue(exportAppMap.get("crawl_detail_num").toString());
			frow2.createCell(1).setCellValue(exportAppMap.get("crawl_detail_domain").toString());
			frow2.createCell(2).setCellValue(exportAppMap.get("crawl_detail_urlreg").toString());
			frow2.createCell(3).setCellValue(exportAppMap.get("crawl_detail_paramreg").toString());
		}
		return workbook;
	}

	@Override
	public HSSFWorkbook generateAppWorkBook() {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("App表");

		List<Map<String, Object>> exportAppDetail = appCrawlDoneMapper.queryExportAppDetail();
		int i = 0;
		for (Map<String, Object> exportAppMap : exportAppDetail) {
			HSSFRow frow = sheet.createRow(i++);
			frow.createCell(0).setCellValue(exportAppMap.get("crawl_app_num").toString());
			frow.createCell(1).setCellValue(exportAppMap.get("crawl_app_domain") == null ? "无"
					: exportAppMap.get("crawl_app_domain").toString());
			frow.createCell(2).setCellValue(exportAppMap.get("crawl_app_attach_param") == null ? "无"
					: exportAppMap.get("crawl_app_attach_param").toString());
		}
		return workbook;
	}

	@Override
	public void checkReg(String regList) {
		JSONArray jsonArray = JSONArray.parseArray(regList);
		List<Map<String, Object>> list = (List<Map<String, Object>>) JSONArray.toJavaObject(jsonArray, Object.class);
		for (Map<String, Object> regMap : list) {
			Integer crawl_detail_id = (Integer) regMap.get("crawl_detail_id");
			Integer crawl_detail_status = (Integer) regMap.get("crawl_detail_status");
			Map<String, Object> map = new HashMap<>();
			map.put("crawl_detail_id", crawl_detail_id);
			map.put("crawl_detail_status", crawl_detail_status);
			appCrawlDetailMapper.checkReg(map);
		}
	}

	@Override
	public Map<String, Object> queryAppSource() {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = appCrawlDoneMapper.queryAppSource();
		result.put("appSource", list);
		return result;
	}

	@Override
	public Map<String, Object> modifyCrawlInfo(AppCrawlDetail appCrawlDetail) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		boolean modifyResult = false;
		map.put("crawl_detail_id", appCrawlDetail.getCrawlDetailId());
		String urlexample = appCrawlDetail.getCrawlDetailUrlexample();
		urlexample = StringUtils.replace(urlexample, "\\", "\\\\");
		urlexample = StringUtils.replace(urlexample, "\"", "\\\"");
		map.put("crawl_detail_urlexample", urlexample);
		map.put("crawl_detail_industry_id", appCrawlDetail.getCrawlDetailIndustryId());
		map.put("crawl_detail_behavior_id", appCrawlDetail.getCrawlDetailBehaviorId());
		String urlreg = appCrawlDetail.getCrawlDetailUrlreg();
		urlreg = StringUtils.replace(urlreg, "\\", "\\\\");
		urlreg = StringUtils.replace(urlreg, "\"", "\\\"");
		map.put("crawl_detail_urlreg", urlreg);
		String domain = appCrawlDetail.getCrawlDetailDomain();
		domain = StringUtils.replace(domain, "\\", "\\\\");
		domain = StringUtils.replace(domain, "\"", "\\\"");
		map.put("crawl_detail_domain", domain);
		// 对正则进行转义处理
		String paramReg = appCrawlDetail.getCrawlDetailParamreg();
		paramReg = StringUtils.replace(paramReg, "\\", "\\\\");
		paramReg = StringUtils.replace(paramReg, "\"", "\\\"");
		map.put("crawl_detail_paramreg", paramReg);
		map.put("crawl_detail_comments", appCrawlDetail.getCrawlDetailComments());
		String crawlDetailReg = appCrawlDetail.getCrawlDetailReg();
		crawlDetailReg = StringUtils.replace(crawlDetailReg, "\\", "\\\\");
		crawlDetailReg = StringUtils.replace(crawlDetailReg, "\"", "\\\"");
		map.put("crawl_detail_reg", crawlDetailReg);
		map.put("crawl_content_detail_id", appCrawlDetail.getCrawlContentDetailId());
		// 若只修改备注状态不变
		AppCrawlDetail selectAppCrawlDetail = appCrawlDetailMapper
				.selectByPrimaryKey(appCrawlDetail.getCrawlDetailId());
		System.out.println(selectAppCrawlDetail.getCrawlDetailCrawlId());
		if (StringUtils.equals(appCrawlDetail.getCrawlDetailUrlexample(),
				selectAppCrawlDetail.getCrawlDetailUrlexample())
				&& appCrawlDetail.getCrawlDetailBehaviorId() == selectAppCrawlDetail.getCrawlDetailBehaviorId()
				&& StringUtils.equals(appCrawlDetail.getCrawlDetailUrlreg(),
						selectAppCrawlDetail.getCrawlDetailUrlreg())
				&& StringUtils.equals(appCrawlDetail.getCrawlDetailDomain(),
						selectAppCrawlDetail.getCrawlDetailDomain())
				&& StringUtils.equals(appCrawlDetail.getCrawlDetailParamreg(),
						selectAppCrawlDetail.getCrawlDetailParamreg())) {
			modifyResult = appCrawlDetailMapper.modifyAppCrawlDetailComments(map);
		} else {
			// 根据需求变更添加判断
			Integer count = appCrawlDetailMapper.queryAppCrawlDetailExist(map);
			if (count > 0) {
				// 2表示 该详情下的域名,参数正则,url正则已存在 无法添加!
				result.put("resultCode", 2);
				return result;
			}
			// 修改对应参数分类编码
			if (appCrawlDetail.getCrawlDetailIndustryId() != selectAppCrawlDetail.getCrawlDetailIndustryId()) {
				Map<String, Object> query = new HashMap<>();
				query.put("industry_id", appCrawlDetail.getCrawlDetailIndustryId());
				String industryNum = appIndustryMapper.queryIndustryNumById(query);
				Integer industryCount = appIndustryMapper.queryIndustryCountById(query);
				String crawlDetailNum = selectAppCrawlDetail.getCrawlDetailNum();
				String newCrawlDetailNum = StringUtils.substring(crawlDetailNum, 0, 8) + industryNum + industryCount;
				map.put("crawl_detail_num", newCrawlDetailNum);
			}
			modifyResult = appCrawlDetailMapper.modifyAppCrawlDetail(map);
		}

		if (modifyResult) {
			result.put("resultCode", 0);
		} else {
			result.put("resultCode", 1);
		}
		return result;
	}

	@Override
	public boolean updateCrawlBehaviorCount(int crawlId) {
		Map<String, Object> update = new HashMap<>();
		update.put("crawl_id", crawlId);
		int crawlBehaviorCount = appCrawlDetailMapper.queryCrawlBehaviorCount(update);
		update.put("crawl_behavior_num", crawlBehaviorCount);
		return appCrawlDoneMapper.updateCrawlBehaviorCount(update);
	}

	@Override
	public Map<String, Object> addCrawlInfo(AppCrawlDetail appCrawlDetail, Integer appOs, String appNum,
			String behaviorCode, String behaviorNum) {
		// 编码规则 系统(1)App(2-4)行为(5-7)行为标识(8)参数分类(9-11)参数个数(12)
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> query = new HashMap<>();
		query.put("industry_id", appCrawlDetail.getCrawlDetailIndustryId());
		query.put("behavior_id", appCrawlDetail.getCrawlDetailBehaviorId());
		String industryNum = appIndustryMapper.queryIndustryNumById(query);
		Integer industryCount = appIndustryMapper.queryIndustryCountById(query);
		String industryName = appIndustryMapper.queryIndustryName(query);

		// 计算抓取详情编号
		String newNum = "";
		newNum = appCrawlDetail.getCrawlDetailType() + appNum + behaviorCode + behaviorNum + industryNum
				+ industryCount;
		System.out.println("NewNum is " + newNum);

		Map<String, Object> map = new HashMap<>();
		map.put("crawl_detail_crawl_id", appCrawlDetail.getCrawlDetailCrawlId());
		map.put("crawl_detail_num", newNum);
		map.put("crawl_detail_type", appCrawlDetail.getCrawlDetailType());
		map.put("crawl_detail_industry_id", appCrawlDetail.getCrawlDetailIndustryId());
		map.put("crawl_detail_behavior_id", appCrawlDetail.getCrawlDetailBehaviorId());
		map.put("crawl_detail_behavior_num_decimal", appCrawlDetail.getCrawlDetailBehaviorNumDecimal());
		String domain = appCrawlDetail.getCrawlDetailDomain();
		domain = StringUtils.replace(domain, "\\", "\\\\");
		domain = StringUtils.replace(domain, "\"", "\\\"");
		map.put("crawl_detail_domain", domain);
		String paramReg = appCrawlDetail.getCrawlDetailParamreg();
		paramReg = StringUtils.replace(paramReg, "\\", "\\\\");
		paramReg = StringUtils.replace(paramReg, "\"", "\\\"");
		map.put("crawl_detail_paramreg", paramReg);
		map.put("crawl_content_detail_id", appCrawlDetail.getCrawlContentDetailId());
		String urlreg = appCrawlDetail.getCrawlDetailUrlreg();
		urlreg = StringUtils.replace(urlreg, "\\", "\\\\");
		urlreg = StringUtils.replace(urlreg, "\"", "\\\"");
		map.put("crawl_detail_urlreg", urlreg);
		String urlexample = appCrawlDetail.getCrawlDetailUrlexample();
		urlexample = StringUtils.replace(urlexample, "\\", "\\\\");
		urlexample = StringUtils.replace(urlexample, "\"", "\\\"");
		map.put("crawl_detail_urlexample", urlexample);
		map.put("crawl_detail_comments", appCrawlDetail.getCrawlDetailComments());
		String crawlDetailReg = appCrawlDetail.getCrawlDetailReg();
		crawlDetailReg = StringUtils.replace(crawlDetailReg, "\\", "\\\\");
		crawlDetailReg = StringUtils.replace(crawlDetailReg, "\"", "\\\"");
		map.put("crawl_detail_reg", crawlDetailReg);
		Integer exportType;
		if (StringUtils.equals(industryName, "IDFA") || StringUtils.equals(industryName, "IMEI")
				|| StringUtils.equals(industryName, "AndroidID") || StringUtils.equals(industryName, "MAC")) {
			// 3:广告Id
			exportType = 3;
		} else if (StringUtils.equals(industryName, "搜索")) {
			// 4:search
			exportType = 4;
		} else {
			// 2其它 1主域名
			exportType = 2;
		}
		map.put("crawl_detail_export_type", exportType);
		// 根据需求变更添加判断
		Integer count = appCrawlDetailMapper.queryAppCrawlDetailExist(map);
		if (count > 0) {
			// 2表示已存在无法添加
			result.put("resultCode", 2);
			return result;
		}
		boolean addResult = appCrawlDetailMapper.addAppCrawlDetail(map);
		if (addResult) {
			result.put("resultCode", 0);
		} else {
			result.put("resultCode", 1);
		}
		return result;
	}

	@Override
	public Map<String, Object> queryAppCrawlDetail(String startDate, String endDate, int crawlId, String industryName,
			Integer behaviorId, int page, int num, String orderName, String orderRule) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("crawl_id", crawlId);
		map.put("industry_name", industryName);
		map.put("behavior_id", behaviorId);
		map.put("page", page);
		map.put("num", num);
		if (StringUtils.equals(orderName, "编号")) {
			orderName = "crawl_detail_num";
		}
		if (StringUtils.equals(orderName, "创建时间")) {
			orderName = "crawl_detail_create_time";
		}
		if (StringUtils.equals(orderName, "状态")) {
			orderName = "crawl_detail_status";
		}
		map.put("orderName", orderName);
		map.put("orderRule", orderRule);
		result.put("totalcount", appCrawlDetailMapper.queryTotalCount(map));
		List<Map<String, Object>> list = appCrawlDetailMapper.queryByPageAndNum(map);
		for (Map<String, Object> appCrawlDetail : list) {
			int crawlDetailIndustryId = (int) appCrawlDetail.get("crawl_detail_industry_id");
			int detailBehaviorId = (int) appCrawlDetail.get("crawl_detail_behavior_id");
			Map<String, Object> industryIdMap = new HashMap<>();
			// 处理数据得到参数分类名称
			industryIdMap.put("industry_id", crawlDetailIndustryId);
			String showIndustryName = appIndustryMapper.queryIndustryName(industryIdMap);
			appCrawlDetail.put("crawl_industry_name", showIndustryName);
			// 处理数据得到指定时间格式
			Date time = (Date) appCrawlDetail.get("crawl_detail_create_time");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String createTime = sdf.format(time);
			appCrawlDetail.put("crawl_detail_create_time", createTime);
			// 处理数据得到行为名称
			industryIdMap.put("behavior_id", detailBehaviorId);
			String behaviorName = appBehaviorMapper.queryAppBehaviorName(industryIdMap);
			if (behaviorName == null) {
				behaviorName = "";
			}
			appCrawlDetail.put("crawl_detail_behavior_name", behaviorName);
		}
		result.put("list", list);
		return result;
	}

	@Override
	public Map<String, Object> updateExport(int crawlId, int isexport) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("crawl_id", crawlId);
		map.put("crawl_app_isexport", isexport);
		if (isexport == 1) {
			Integer isCanExport = appCrawlDoneMapper.queryIsExport(map);
			if (isCanExport < 1) {
				// 主域名和附加参数为空,无法导出
				result.put("resultCode", 2);
				return result;
			}
		}
		try {
			appCrawlDoneMapper.updateExport(map);
			// 修改对应App下主域名的导出状态
			appCrawlDoneMapper.updateDomainExport(map);
		} catch (Exception e) {
			logger.error("导出状态修改异常!");
			e.printStackTrace();
			result.put("resultCode", 1);
			return result;
		}
		result.put("resultCode", 0);
		return result;
	}

	@Override
	public Map<String, Object> deleteAppCrawlerDetail(Integer crawlDetailId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("crawl_detail_id", crawlDetailId);
		Integer crawlId = appCrawlDetailMapper.queryCrawlId(map);
		Integer count4 = appCrawlDoneMapper.queryCrawlerDetailDataExist4Four(map);
		Integer count5 = appCrawlDoneMapper.queryCrawlerDetailDataExist4Five(map);
		if (count4 > 0 || count5 > 0) {
			// 2,该抓取信息中存在统计数据,无法删除
			result.put("resultCode", 2);
		} else {
			boolean delResult = appCrawlDoneMapper.deleteAppCrawlerDetail(map);
			updateCrawlBehaviorCount(crawlId);
			if (delResult) {
				// 删除成功
				result.put("resultCode", 0);
			} else {
				// 删除失败
				result.put("resultCode", 1);
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> deleteAppCrawlerDone(int crawlId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("crawl_id", crawlId);
		Integer count = appCrawlDoneMapper.queryAppCrawlDetailCount(map);
		Integer domainCount = appCrawlDoneMapper.queryAppCrawlDomainCount(map);
		if (count > 0||domainCount>0) {
			// 2表示该App详情中存在内容,无法删除
			result.put("resultCode", 2);
		} else {
			try {
				// 修改待抓取App的优先级
				List<Map<String, Object>> appIdList = appCrawlDoneMapper.queryAppIdByCrawlId(map);
				for (Map<String, Object> appIdMap : appIdList) {
					map.put("app_id", appIdMap.get("app_id"));
					String source = appCrawlerWaitMapper.querySourceByAppId(map);
					String[] sourceArr = StringUtils.split(source, ",");
					// 若为App
					if ((Integer) appIdMap.get("app_os") == 2 || (Integer) appIdMap.get("app_os") == 3) {
						if (sourceArr.length > 1) {
							map.put("app_priority", 2);
						} else {
							map.put("app_priority", 3);
						}
					} else {
						map.put("app_priority", 2);
					}
					appCrawlerWaitMapper.modifyPriority(map);
				}
				// 修改待抓取App状态
				appCrawlDoneMapper.updateWaitAppStatus(map);
				appCrawlDoneMapper.deleteAppWaitDone(map);
				appCrawlDoneMapper.deleteAppCrawlerDone(map);
			} catch (Exception e) {
				logger.error("删除已抓取App异常!");
				e.printStackTrace();
				result.put("resultCode", 1);
				return result;
			}
			result.put("resultCode", 0);
		}
		return result;
	}

	@Override
	public Map<String, Object> modifyAppCrawlerDone(int crawlId, String version, int categoryId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		if(version==null){
			version="";
		}
		map.put("crawl_id", crawlId);
		map.put("crawl_app_version", version);
		map.put("crawl_app_category_id", categoryId);
		
		try {
			appCrawlDoneMapper.modifyAppCrawlerDone(map);
			List<Map<String, Object>> crawlAppList = appCrawlerWaitMapper.queryCrawlAppByCrawlId(map);
			String[] updateVersionArr = StringUtils.split(version, ",");
			for (String updateVersion : updateVersionArr) {
				for (Map<String, Object> crawlApp : crawlAppList) {
					String versionUpdate=crawlApp.get("app_version_update") == null ? "" : crawlApp.get("app_version_update").toString();
					if (StringUtils.contains(
							versionUpdate,
							updateVersion)) {
						int index=StringUtils.indexOf(versionUpdate, updateVersion);
						versionUpdate=StringUtils.substring(versionUpdate, 0, index)+StringUtils.substring(versionUpdate, index+updateVersion.length()+1);
						Map<String,Object> versionMap=new HashMap<>();
						versionMap.put("app_id", crawlApp.get("app_id"));
						versionMap.put("app_version_update", versionUpdate);
						appCrawlerWaitMapper.modifyVersionUpdate(versionMap);
					}
				}
			}
		} catch (Exception e) {
			result.put("resultCode", 1);
			logger.error("已抓取App编辑异常!");
			e.printStackTrace();
			return result;
		}
		result.put("resultCode", 0);
		return result;
	}

	@Override
	public Map<String, Object> queryByPageAndNum(java.sql.Date startDate, java.sql.Date endDate,
			Integer parentCategoryId, Integer childCategoryId, String appName, String appNumber, int page, int num,
			String orderName, String orderRule) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("parent_category_id", parentCategoryId);
		map.put("crawl_app_category_id", childCategoryId);
		map.put("crawl_app_name", appName);
		map.put("crawl_app_num", appNumber);
		map.put("page", page);
		map.put("num", num);
		if (orderName != null) {
			if (StringUtils.equals(orderName, "APP编号")) {
				orderName = "crawl_app_num_decimal";
			}
			if (StringUtils.equals(orderName, "创建时间")) {
				orderName = "crawl_app_create_time";
			}
		}
		map.put("orderName", orderName);
		map.put("orderRule", orderRule);
		result.put("totalcount", appCrawlDoneMapper.queryTotalCount(map));
		List<Map<String, Object>> list = appCrawlDoneMapper.queryByPageAndNum(map);
		for (Map<String, Object> app : list) {
			// 处理主App来源
			String[] sourceIdArr = StringUtils.split(app.get("crawl_app_source").toString(), ",");
			StringBuffer sourceNameSb = new StringBuffer();
			String sourceStr = "";
			for (String sourceId : sourceIdArr) {
				Map<String, Object> queryMap = new HashMap<>();
				queryMap.put("source_id", Integer.parseInt(sourceId));
				String sourceName = appCrawlerWaitMapper.querySourceName(queryMap);
				sourceNameSb.append(sourceName).append(",");
				sourceStr = sourceNameSb.toString();
			}
			sourceStr = StringUtils.substring(sourceStr, 0, sourceStr.length() - 1);
			app.put("crawl_app_source", sourceStr);
			// 处理应用市场App名称
			Map<String, Object> queryRelatedAppMap = new HashMap<>();
			queryRelatedAppMap.put("crawl_app_num", app.get("crawl_app_num"));
			List<Map<String, Object>> relatedAppList = appCrawlDoneMapper.queryRelatedApp(queryRelatedAppMap);
			String relatedAppName = "";
			String relatedAppCategoryName = "";
			String relatedAppVersion = "";
			String relatedAppType = "";
			for (Map<String, Object> relatedAppMap : relatedAppList) {
				relatedAppName += relatedAppMap.get("app_name_crawl").toString() + ",";
				if (relatedAppMap.get("app_category_name") != null) {
					relatedAppCategoryName += relatedAppMap.get("app_category_name").toString() + ",";
				}
				relatedAppType += relatedAppMap.get("app_type").toString() + ",";
			}
			app.put("relatedAppName", StringUtils.substring(relatedAppName, 0, relatedAppName.length() - 1));
			app.put("relatedAppCategoryName",
					StringUtils.substring(relatedAppCategoryName, 0, relatedAppCategoryName.length() - 1));
			app.put("crawl_app_os", StringUtils.substring(relatedAppType, 0, relatedAppType.length() - 1));
			// 添加访问数总和
			Map<String, Object> query = new HashMap<>();
			query.put("app_num", app.get("crawl_app_num"));
			String deadline = appCrawlDoneMapper.queryDeadine(query);
			query.put("create_time", deadline);
			Integer totalVisits = appCrawlDoneMapper.queryTotalVisits(query);

			Date createTime = (Date) app.get("crawl_app_create_time");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			app.put("crawl_app_create_time", sdf.format(createTime));

			if (deadline == null) {
				app.put("deadline", "");
			} else {
				app.put("deadline", deadline);
			}

			if (totalVisits == null) {
				app.put("totalVisits", "");
			} else {
				app.put("totalVisits", totalVisits);
			}
			if (app.get("crawl_app_domain") == null) {
				app.put("crawl_app_domain", "");
			}
			if (app.get("crawl_app_attach_param") == null) {
				app.put("crawl_app_attach_param", "");
			}
			if (app.get("crawl_behavior_num") == null) {
				app.put("crawl_behavior_num", 0);
			}
			if (app.get("relatedAppName") == null) {
				app.put("relatedAppName", "");
			}
			if (app.get("crawl_app_logo_url") == null) {
				app.put("crawl_app_logo_url", "");
			}
		}
		result.put("list", list);
		return result;
	}

	@Override
	public Map<String, Object> addAppInfo(AppInfo app_info, int choseId, String choseName) {
		// TODO Auto-generated method stub
		Map addInfo = new HashMap();
		int queryNum = 0;
		String newNum = "0001";
		Map result = new HashMap();
		addInfo.put("app_name", choseName);
		queryNum = appInfoMapper.queryNum(addInfo);
		if (queryNum != 0) {
			AppInfo appInfo = appInfoMapper.queryMesByName(addInfo);
			if (0 == appInfo.getApp_os()) {
				result.put("resultCode", 2);
				return result;
			} else {
				addInfo.put("osType", appInfo.getApp_os());
				boolean updateOs = appInfoMapper.updateOsType(addInfo);
				Map noMajor = new HashMap();
				noMajor.put("app_info_num", appInfo.getApp_num());
				noMajor.put("app_is_major", 0);
				noMajor.put("app_crawl_id", -1);
				if (app_info.getApp_os() == 2) {
					noMajor.put("app_os", 3);
				} else {
					noMajor.put("app_os", 2);
				}
				appInfoDetailMapper.addDetail(noMajor);
				if (updateOs) {
					result.put("resultCode", 3);
				} else {
					result.put("resultCode", 1);
				}
				return result;
			}
		} else {
			addInfo.put("app_name", app_info.getApp_name());
			addInfo.put("app_category_id", app_info.getApp_category_id());
			addInfo.put("app_domain", app_info.getApp_domain());
			// if ((null == app_info.getApp_attach_param()) ||
			// ("".equals(app_info.getApp_attach_param()))){
			// addInfo.put("app_attach_param","NULL");
			// }else {
			// addInfo.put("app_attach_param",app_info.getApp_attach_param());
			// }
			addInfo.put("app_attach_param", app_info.getApp_attach_param());

			addInfo.put("app_logo_url", app_info.getApp_logo_url());
			String oldNum = appInfoMapper.queryMaxAppInfoNum();

			if (oldNum != null) {
				int num = Integer.valueOf(oldNum) + 1;
				newNum = String.format("%04d", num);
				System.out.println("newNum is " + newNum);
			}
			addInfo.put("app_num", newNum);
			addInfo.put("app_create_time", app_info.getApp_create_time());
			addInfo.put("app_source", app_info.getApp_source());
			addInfo.put("app_isexport", 1);

			queryNum = appInfoMapper.queryNum(addInfo);
			boolean addResult = false;
			if (0 == queryNum) {
				if (choseId != -1) {
					Map choseIdMap = new HashMap();
					choseIdMap.put("id", choseId);
					// appCrawlerListMapper.updateStatus(choseIdMap);
					Map isMajor = new HashMap();
					isMajor.put("app_info_num", newNum);
					isMajor.put("app_is_major", 1);
					isMajor.put("app_crawl_id", -1);
					isMajor.put("app_os", app_info.getApp_os());
					appInfoDetailMapper.addDetail(isMajor);
					Map noMajor = new HashMap();
					noMajor.put("app_info_num", newNum);
					noMajor.put("app_is_major", 0);
					noMajor.put("app_crawl_id", choseId);
					if (app_info.getApp_os() == 2) {
						noMajor.put("app_os", 3);
					} else {
						noMajor.put("app_os", 2);
					}
					appInfoDetailMapper.addDetail(noMajor);
					addInfo.put("app_os", 0);
				} else {
					Map isMajor = new HashMap();
					isMajor.put("app_info_num", newNum);
					isMajor.put("app_is_major", 1);
					isMajor.put("app_crawl_id", -1);
					isMajor.put("app_os", app_info.getApp_os());
					appInfoDetailMapper.addDetail(isMajor);
					addInfo.put("app_os", app_info.getApp_os());
				}
				addResult = appInfoMapper.addAppInfo(addInfo);
				if (addResult) {
					result.put("resultCode", 0);
					result.put("app_num", newNum);
				} else {
					result.put("resultCode", 1);
				}
				return result;
			} else {
				result.put("resultCode", 2);
				return result;
			}
		}
	}

	@Override
	public Map<String, Object> updateAppInfo(AppInfo app_info, int choseId) {
		// TODO Auto-generated method stub
		if (choseId != -1) {
			Map map = new HashMap();
			map.put("id", choseId);
			// appCrawlerListMapper.updateStatus(map);
			map.put("app_name", app_info.getApp_name());
			map.put("osType", app_info.getApp_os());
			appInfoMapper.updateOsType(map);
			Map noMajor = new HashMap();
			noMajor.put("app_info_num", app_info.getApp_num());
			noMajor.put("app_is_major", 0);
			noMajor.put("app_crawl_id", choseId);
			if (app_info.getApp_os() == 2) {
				noMajor.put("app_os", 3);
			} else {
				noMajor.put("app_os", 2);
			}
			appInfoDetailMapper.addDetail(noMajor);
			app_info.setApp_os(0);
		}
		Map result = new HashMap();
		boolean updateResult = appInfoMapper.updateAppInfo(app_info);
		if (updateResult) {
			result.put("resultCode", 0);
		} else {
			result.put("resultCode", 1);
		}
		return result;
	}

	/*
	 * @Override public Map<String,Object> delAppInfo(int id,String appNum) { //
	 * TODO Auto-generated method stub Map map=new HashMap(); map.put("id",id);
	 * map.put("appNum",appNum); Map result=new HashMap();
	 * appCrawlerListMapper.updateStatusById(map);
	 * appInfoDetailMapper.delDetail(map); appCrawlInfoMapper.delByAppId(map);
	 * boolean delResult = appInfoMapper.delAppInfo(map); if (delResult){
	 * result.put("resultCode",0); }else { result.put("resultCode",1); } return
	 * result; }
	 */

	@Override
	public int queryByName(String appName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appName", appName);
		return appInfoMapper.queryByName(map);
	}

	@Override
	public HSSFWorkbook exportApp() {
		// String[] excelHeader = { "编号", "主域名", "附加参数"};
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("AppInfo");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// for (int i = 0; i < excelHeader.length; i++) {
		// HSSFCell cell = row.createCell(i);
		// cell.setCellValue(excelHeader[i]);
		// cell.setCellStyle(style);
		// sheet.autoSizeColumn(i);
		// // sheet.SetColumnWidth(i, 100 * 256);
		// }

		List<AppInfo> appInfoList = appInfoMapper.exportApp();

		for (int i = 0; i < appInfoList.size(); i++) {
			row = sheet.createRow(i);
			AppInfo appInfo = appInfoList.get(i);
			row.createCell(0).setCellValue(appInfo.getApp_num());
			System.out.println(appInfo.getApp_num());
			row.createCell(1).setCellValue(appInfo.getApp_domain());
			row.createCell(2).setCellValue(appInfo.getApp_attach_param());
		}
		return wb;
	}

	@Override
	public Map<String, Object> queryByAppName(String appName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appName", appName);
		return appInfoMapper.queryByAppName(map);
	}

	@Override
	public Map<String, Object> updateAppOs(Map<String, Object> appInfo) {
		Map result = new HashMap();
		boolean updateResult = appInfoMapper.updateAppOs(appInfo);
		if (updateResult) {
			result.put("resultCode", 0);
		} else {
			result.put("resultCode", 1);
		}
		return result;
	}

	@Override
	public Map<String, Object> addTransfer(AppInfo app_info) {
		Map addInfo = new HashMap();
		// addInfo.put("id",app_info.getId());
		addInfo.put("app_name", app_info.getApp_name());
		addInfo.put("app_category_id", app_info.getApp_category_id());
		addInfo.put("app_domain", app_info.getApp_domain());
		addInfo.put("app_attach_param", app_info.getApp_attach_param());
		addInfo.put("app_os", app_info.getApp_os());
		addInfo.put("app_logo_url", app_info.getApp_logo_url());
		String oldNum = appInfoMapper.queryMaxAppInfoNum();
		String newNum = "0001";
		if (oldNum != null) {
			int num = Integer.valueOf(oldNum) + 1;
			newNum = String.format("%04d", num);
			System.out.println("newNum is " + newNum);
		}
		addInfo.put("app_num", newNum);
		addInfo.put("app_create_time", app_info.getApp_create_time());
		addInfo.put("app_source", app_info.getApp_source());
		addInfo.put("app_isexport", 1);
		boolean addResult = false;
		// if (-1==choseId){
		// Map choseIdMap = new HashMap();
		// choseIdMap.put("id", app_info.getId());
		// appCrawlerListMapper.updateStatus(choseIdMap);
		// Map isMajor=new HashMap();
		// isMajor.put("app_info_num",newNum);
		// isMajor.put("app_is_major", 1);
		// isMajor.put("app_crawl_id", app_info.getId());
		// isMajor.put("app_os", app_info.getApp_os());
		// appInfoDetailMapper.addDetail(isMajor);
		// }else {
		// Map choseIdMap = new HashMap();
		// choseIdMap.put("id", app_info.getId());
		// appCrawlerListMapper.updateStatus(choseIdMap);
		// choseIdMap.put("id", choseId);
		// appCrawlerListMapper.updateStatus(choseIdMap);
		// Map isMajor=new HashMap();
		// isMajor.put("app_info_num",newNum);
		// isMajor.put("app_is_major", 1);
		// isMajor.put("app_crawl_id", app_info.getId());
		// isMajor.put("app_os", app_info.getApp_os());
		// appInfoDetailMapper.addDetail(isMajor);
		// Map noMajor=new HashMap();
		// noMajor.put("app_info_num",newNum);
		// noMajor.put("app_is_major", 0);
		// noMajor.put("app_crawl_id", choseId);
		// if (2==app_info.getApp_os()){
		// noMajor.put("app_os", 3);
		// }else {
		// noMajor.put("app_os", 2);
		// }
		// appInfoDetailMapper.addDetail(noMajor);
		// addInfo.put("app_os", 0);
		// }
		addResult = appInfoMapper.addAppInfo(addInfo);
		Map result = new HashMap();
		if (addResult) {
			result.put("resultCode", 0);
			result.put("app_num", newNum);
		} else {
			result.put("resultCode", 1);
		}
		return result;
	}

	@Override
	public Map<String, Object> delAppInfo(int id, String appNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryCrawlInfoNum(Map numMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> queryTransferType(String crawlAppNum) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("crawl_app_num", crawlAppNum);
		List<Map<String, Object>> list = appCrawlDoneMapper.queryTransferType(map);
		result.put("list", list);
		return result;
	}

	@Override
	public Map<String, Object> addDomain(String crawlAppNum, String domain, String attachParam, String typeNum) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("crawl_app_num", crawlAppNum);
		domain = StringUtils.replace(domain, "\\", "\\\\");
		domain = StringUtils.replace(domain, "\"", "\\\"");
		map.put("domain_domain", domain);
		attachParam = StringUtils.replace(attachParam, "\\", "\\\\");
		attachParam = StringUtils.replace(attachParam, "\"", "\\\"");
		map.put("domain_attach_param", attachParam);
		map.put("domain_code", typeNum + crawlAppNum);

		Integer count = appCrawlDoneMapper.queryDomainExist(map);
		if (count > 0) {
			// 2表示主域名和附加参数已存在,无法添加!
			result.put("resultCode", 2);
			return result;
		}
		List<Map<String, Object>> typeNumList = appCrawlDoneMapper.queryDomainTypeNum(map);
		for (Map<String, Object> typeNumMap : typeNumList) {
			if (StringUtils.equals(typeNumMap.get("type_num").toString(), typeNum)) {
				// 3表示该主域名对应的端已存在,无法添加!
				result.put("resultCode", 3);
				return result;
			}
		}
		map.put("domain_code", typeNum + crawlAppNum);
		Integer exportStatus = appCrawlDoneMapper.queryCrawlAppExportStatus(map);
		map.put("domain_isexport", exportStatus);
		try {
			appCrawlDoneMapper.addDomain(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加域名失败!");
			result.put("resultCode", 1);
			return result;
		}
		result.put("resultCode", 0);
		return result;
	}

	@Override
	public Map<String, Object> delDomain(Integer domainId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("domain_id", domainId);
		try {
			appCrawlDoneMapper.delDomain(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除主域名出错!");
			result.put("resultCode", 1);
			return result;
		}
		result.put("resultCode", 0);
		return result;
	}

	@Override
	public Map<String, Object> queryDomainByPageAndNum(String crawlAppNum, int page, int num) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("crawl_app_num", crawlAppNum);
		map.put("page", page);
		map.put("num", num);
		result.put("totalcount", appCrawlDoneMapper.queryDomainTotalCount(map));
		result.put("list", appCrawlDoneMapper.queryDomainByPageAndNum(map));
		return result;
	}

	@Override
	public Map<String, Object> modifyDomain(int domainId, String crawlAppNum, String domain, String attachParam,
			String typeNum) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("domain_id", domainId);
		map.put("crawl_app_num", crawlAppNum);
		domain = StringUtils.replace(domain, "\\", "\\\\");
		domain = StringUtils.replace(domain, "\"", "\\\"");
		map.put("domain_domain", domain);
		attachParam = StringUtils.replace(attachParam, "\\", "\\\\");
		attachParam = StringUtils.replace(attachParam, "\"", "\\\"");
		map.put("domain_attach_param", attachParam);
		Integer count = appCrawlDoneMapper.queryDomainExist(map);
		if (count > 0) {
			// 2表示主域名和附加参数已存在,无法修改!
			result.put("resultCode", 2);
			return result;
		}
		List<Map<String, Object>> typeNumList = appCrawlDoneMapper.queryDomainTypeNum(map);
		for (Map<String, Object> typeNumMap : typeNumList) {
			if (StringUtils.equals(typeNumMap.get("type_num").toString(), typeNum)) {
				// 3表示该主域名对应的端已存在,无法修改!
				result.put("resultCode", 3);
				return result;
			}
		}
		map.put("domain_code", typeNum + crawlAppNum);

		try {
			appCrawlDoneMapper.modifyDomain(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改域名失败!");
			result.put("resultCode", 1);
			return result;
		}
		result.put("resultCode", 0);
		return result;
	}
}
