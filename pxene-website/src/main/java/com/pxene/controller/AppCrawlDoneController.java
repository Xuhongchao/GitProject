package com.pxene.controller;

import java.io.BufferedOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pxene.dao.AppBehaviorMapper;
import com.pxene.dao.AppCrawlDetailMapper;
import com.pxene.model.AppCrawlDetail;
import com.pxene.service.AppBehaviorService;
import com.pxene.service.AppCategoryService;
import com.pxene.service.AppCrawlDoneService;
import com.pxene.service.AppIndustryService;
import com.pxene.utils.CodeNumUtils;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月28日
 */
@Controller
@RequestMapping("/appCrawlerDone")
public class AppCrawlDoneController {
	@Autowired
	AppIndustryService appIndustryService;
	@Autowired
	AppBehaviorService appBehaviorService;
	@Autowired
	AppCrawlDoneService appCrawlDoneService;
	@Autowired
	AppCategoryService appCategoryService;
	@Autowired
	AppCrawlDetailMapper appCrawlDetailMapper;
	@Autowired
	AppBehaviorMapper appBehaviorMapper;
	
	@RequestMapping(value = "queryCrawlDetailBehavior")
	@ResponseBody
	public Map<String, Object> queryCrawlDetailBehavior() {
		Map<String, Object> result = new HashMap<>();
		List<Map<String,Object>> list=appCrawlDetailMapper.queryCrawlDetailBehavior();
		result.put("list", list);
		return result;
	}

	@RequestMapping(value = "checkReg")
	@ResponseBody
	public void checkReg(@RequestBody JSONObject params) {

		String regList = params.getString("regList");
		appCrawlDoneService.checkReg(regList);
	}

	@RequestMapping(value = "queryAppSource")
	@ResponseBody
	public Map<String, Object> queryAppSource() {
		return appCrawlDoneService.queryAppSource();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "modifyCrawlInfo")
	@ResponseBody
	public Map<String, Object> modifyCrawlInfo(@RequestBody JSONObject params) {
		Integer crawlDetailId = params.getIntValue("crawl_detail_id");
		String urlExample = params.getString("urlExample");
		Integer behaviorId = params.getIntValue("grabBehavior");
		String urlreg = params.getString("urlregexp");
		String domain = params.getString("domain");
		String comments = null;
		if (params.get("remarks") != null) {
			comments = params.get("remarks").toString();
		}
		// 获取参数正则
		String industryContentList = params.getString("industryContentList");
		Map<String, Object> industryContentMap = JSON.parseObject(industryContentList);

		List<Map<String, Object>> contentDetailList = (List<Map<String, Object>>) industryContentMap
				.get("industryContentList");
		Integer industryId = (Integer) industryContentMap.get("industry_id");
		String paramReg = "";
		String reg = "";
		StringBuffer crawlContentDetailId = new StringBuffer(",");
		for (Map<String, Object> contentDetailMap : contentDetailList) {
			// 若前缀传为null,为广告ID并且为未选参数,前缀传为"",为非广告ID或广告ID中的所选参数
			if (contentDetailMap.get("param_key") == null) {
				continue;
			}
			String paramNum="";
			if(contentDetailMap.get("param_num")!=null){
				paramNum=contentDetailMap.get("param_num").toString();
			}
			String paramRegPrefix = contentDetailMap.get("param_key").toString();
			Map<String, Object> paramRegMap = (Map<String, Object>) contentDetailMap.get("param_reg");

			// 添加对参数分类为广告ID情况的特殊判断
//			String contentName = contentDetailMap.get("content_name").toString();
//			if (StringUtils.equals(contentName, "IDFA") || StringUtils.equals(contentName, "IMEI")
//					|| StringUtils.equals(contentName, "AndroidID") || StringUtils.equals(contentName, "MAC")) {
//				// 若广告ID的正则传为NULL,则删除该正则
//				if (StringUtils.equals(paramRegMap.get("param_reg").toString(), "NULL")) {
//					return appCrawlDoneService.deleteAppCrawlerDetail(crawlDetailId);
//				}
//			}

			String paramRegPostfix = contentDetailMap.get("param_content").toString();
			// 添加 参数类型为广告ID的特殊情况
//			if (StringUtils.equals(contentName, "IDFA")) {
//				paramReg += paramRegPrefix + paramRegMap.get("param_reg") + paramRegPostfix + "\t" + 1;
//			} else if (StringUtils.equals(contentName, "IMEI")) {
//				paramReg += paramRegPrefix + paramRegMap.get("param_reg") + paramRegPostfix + "\t" + 2;
//			} else if (StringUtils.equals(contentName, "AndroidID")) {
//				paramReg += paramRegPrefix + paramRegMap.get("param_reg") + paramRegPostfix + "\t" + 3;
//			} else if (StringUtils.equals(contentName, "MAC")) {
//				paramReg += paramRegPrefix + paramRegMap.get("param_reg") + paramRegPostfix + "\t" + 4;
//			} else {
//				// 参数类型不是广告ID的情况
//				paramReg += paramRegPrefix + paramRegMap.get("param_reg") + paramRegPostfix + "\t";
//			}
			
			paramReg += paramRegPrefix + paramRegMap.get("param_reg") + paramRegPostfix + "\t"+paramNum;
			reg += paramRegMap.get("param_reg").toString() + "\t";

			crawlContentDetailId.append(paramRegMap.get("content_detail_id").toString()).append(",");
		}
		paramReg = StringUtils.trim(paramReg);
		// paramReg = StringUtils.replace(paramReg, "\\", "\\\\");
		reg = StringUtils.trim(reg);
		// reg = StringUtils.replace(reg, "\\", "\\\\");

		AppCrawlDetail appCrawlDetail = new AppCrawlDetail();
		appCrawlDetail.setCrawlDetailId(crawlDetailId);
		appCrawlDetail.setCrawlDetailUrlexample(urlExample);
		appCrawlDetail.setCrawlDetailIndustryId(industryId);
		appCrawlDetail.setCrawlDetailBehaviorId(behaviorId);
		appCrawlDetail.setCrawlDetailUrlreg(urlreg);
		appCrawlDetail.setCrawlDetailDomain(domain);
		appCrawlDetail.setCrawlDetailParamreg(paramReg);
		appCrawlDetail.setCrawlContentDetailId(crawlContentDetailId.toString());
		appCrawlDetail.setCrawlDetailComments(comments);
		appCrawlDetail.setCrawlDetailReg(reg);
		return appCrawlDoneService.modifyCrawlInfo(appCrawlDetail);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "addCrawlInfo")
	@ResponseBody
	public Map<String, Object> addCrawlInfo(@RequestBody JSONObject params) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> addResult = new HashMap<>();
		Integer appOs = params.getIntValue("crawl_app_os");
		String appNum = params.getString("crawl_app_num");
		String industryContentList = params.getString("industryContentList");
		List<Map<String, Object>> list=JSON.parseObject(industryContentList, List.class);

		String urlExample = params.getString("urlExample");
		// urlExample = StringUtils.replace(urlExample, "\\", "\\\\");
		Integer behaviorId = params.getIntValue("grabBehavior");
		String urlreg = params.getString("urlregexp");
		// urlreg = StringUtils.replace(urlreg, "\\", "\\\\");
		String domain = params.getString("domain");
		Integer crawlDetailType = params.getInteger("crawl_detail_type");
		
		//生成行为编码(5-8位)
		Map<String,Object> query=new HashMap<>();
		query.put("behavior_id", behaviorId);
		String behaviorCode = appBehaviorMapper.queryBehaviorCodeById(query);
		String behaviorNum = "1";
		query.put("crawl_detail_type", crawlDetailType);
		query.put("app_num", appNum);
		query.put("behavior_code", behaviorCode);
		String maxBehaviorNumDecimal = appBehaviorMapper.queryMaxBehaviorNum(query);
		if (maxBehaviorNumDecimal != null) {
			Integer behaviorNumDecimal = Integer.parseInt(maxBehaviorNumDecimal) + 1;
			maxBehaviorNumDecimal = String.format("%02d", behaviorNumDecimal);
			behaviorNum = CodeNumUtils._10_to_62(behaviorNumDecimal, 1);
		}else{
			maxBehaviorNumDecimal="01";
		}
		
		for (Map<String, Object> industryContentMap : list) {

			// 一个参数分类
			int industryId = (int) industryContentMap.get("industry_id");
			String crawl_detail_comments;
			if (industryContentMap.get("remarks") == null) {
				crawl_detail_comments = "";
			} else {
				crawl_detail_comments = industryContentMap.get("remarks").toString();
			}
			List<Map<String, Object>> contentDetailList = (List<Map<String, Object>>) industryContentMap
					.get("industryContentList");
			String paramReg = "";
			String reg = "";
			StringBuffer crawlContentDetailId = new StringBuffer(",");

			// 根据需求变更添加广告ID的特殊情况
			String industryName = industryContentMap.get("industry_name").toString();
//			if (StringUtils.equals(industryName, "广告ID")) {
//				// 获取每个参数
//				int i = 1;
//				for (Map<String, Object> contentDetail : contentDetailList) {
//					String ADIdparamReg = "";
//					String ADIdReg = "";
//					StringBuffer ADIdCrawlContentDetailId = new StringBuffer(",");
//
//					String paramRegPrefix = contentDetail.get("param_key").toString();
//
//					Map<String, Object> paramRegMap = (Map<String, Object>) contentDetail.get("param_reg");
//					String paramRegPostfix = contentDetail.get("param_content").toString();
//					if (StringUtils.equals(paramRegMap.get("param_reg").toString(), "NULL")) {
//						i++;
//						continue;
//					}
//					ADIdparamReg = paramRegPrefix + paramRegMap.get("param_reg").toString() + paramRegPostfix + "\t"
//							+ i++;
//					ADIdReg = paramRegMap.get("param_reg").toString();
//
//					ADIdCrawlContentDetailId.append(paramRegMap.get("content_detail_id").toString()).append(",");
//					System.out.println(reg);
//					ADIdparamReg = StringUtils.trim(ADIdparamReg);
//					// ADIdparamReg = StringUtils.replace(ADIdparamReg, "\\",
//					// "\\\\");
//					// ADIdReg = StringUtils.replace(ADIdReg, "\\", "\\\\");
//					AppCrawlDetail appCrawlDetail = new AppCrawlDetail();
//					appCrawlDetail.setCrawlDetailCrawlId(params.getIntValue("crawl_id"));
//					appCrawlDetail.setCrawlDetailType(crawlDetailType);
//					appCrawlDetail.setCrawlDetailIndustryId(industryId);
//					appCrawlDetail.setCrawlDetailBehaviorId(behaviorId);
//					appCrawlDetail.setCrawlDetailDomain(domain);
//					appCrawlDetail.setCrawlDetailParamreg(ADIdparamReg);
//					appCrawlDetail.setCrawlContentDetailId(ADIdCrawlContentDetailId.toString());
//					appCrawlDetail.setCrawlDetailUrlreg(urlreg);
//					appCrawlDetail.setCrawlDetailUrlexample(urlExample);
//					appCrawlDetail.setCrawlDetailComments(crawl_detail_comments);
//					appCrawlDetail.setCrawlDetailReg(ADIdReg);
//					addResult = appCrawlDoneService.addCrawlInfo(appCrawlDetail, appOs, appNum);
//				}
//			} else {
			
				// 获取每个参数
				for (Map<String, Object> contentDetail : contentDetailList) {
					String paramRegPrefix = contentDetail.get("param_key").toString();

					Map<String, Object> paramRegMap = (Map<String, Object>) contentDetail.get("param_reg");
					String paramRegPostfix = contentDetail.get("param_content").toString();
					String paramNum="";
					if(contentDetail.get("param_num")!=null){
						paramNum=contentDetail.get("param_num").toString();
					}
					paramReg += paramRegPrefix + paramRegMap.get("param_reg").toString() + paramRegPostfix + "\t"+paramNum;
					System.out.println(paramReg);
					reg += paramRegMap.get("param_reg").toString() + "\t";

					crawlContentDetailId.append(paramRegMap.get("content_detail_id").toString()).append(",");
					System.out.println(reg);
				}
				paramReg = StringUtils.trim(paramReg);
				// paramReg = StringUtils.replace(paramReg, "\\", "\\\\");
				reg = StringUtils.trim(reg);
				// reg = StringUtils.replace(reg, "\\", "\\\\");

				// 添加抓取详情
				AppCrawlDetail appCrawlDetail = new AppCrawlDetail();
				appCrawlDetail.setCrawlDetailCrawlId(params.getIntValue("crawl_id"));
				appCrawlDetail.setCrawlDetailType(crawlDetailType);
				appCrawlDetail.setCrawlDetailIndustryId(industryId);
				appCrawlDetail.setCrawlDetailBehaviorId(behaviorId);
				appCrawlDetail.setCrawlDetailBehaviorNumDecimal(maxBehaviorNumDecimal);
				appCrawlDetail.setCrawlDetailDomain(domain);
				appCrawlDetail.setCrawlDetailParamreg(paramReg);
				appCrawlDetail.setCrawlContentDetailId(crawlContentDetailId.toString());
				appCrawlDetail.setCrawlDetailUrlreg(urlreg);
				appCrawlDetail.setCrawlDetailUrlexample(urlExample);
				appCrawlDetail.setCrawlDetailComments(crawl_detail_comments);
				appCrawlDetail.setCrawlDetailReg(reg);
				addResult = appCrawlDoneService.addCrawlInfo(appCrawlDetail, appOs, appNum,behaviorCode,behaviorNum);
			//}
		}

		if ((Integer) addResult.get("resultCode") == 0) {
			// 在添加抓取详情成功后,更新抓取行为数
			appCrawlDoneService.updateCrawlBehaviorCount(params.getIntValue("crawl_id"));
			// 0表示保存成功
			result.put("resultCode", 0);
		} else if ((Integer) addResult.get("resultCode") == 1) {
			// 1表示保存失败
			result.put("resultCode", 1);
		} else {
			// 2表示 该详情下的域名,参数正则,url正则已存在 无法添加!
			result.put("resultCode", 2);
		}

		return result;
	}

	@RequestMapping(value = "queryModifyAppIndustryInfo")
	@ResponseBody
	public Map<String, Object> queryModifyAppIndustryInfo(@RequestBody JSONObject params) {
		Integer crawlDetailId = params.getInteger("crawl_detail_id");
		return appIndustryService.queryModifyAppIndustryInfo(crawlDetailId);
	}

	@RequestMapping(value = "queryAppIndustryInfo")
	@ResponseBody
	public Map<String, Object> queryAppIndustryInfo(@RequestBody JSONObject params) {
		Integer crawlDetailId = params.getInteger("crawl_detail_id");
		return appIndustryService.queryAppIndustryInfo(crawlDetailId);
	}

	@RequestMapping(value = "queryAppCrawlBehavior")
	@ResponseBody
	public Map<String, Object> queryAppCrawlBehavior(@RequestBody JSONObject params) {
		String categoryIdStr = params.getString("crawl_app_category_id_str");
		String behaviorName = params.getString("search");
		return appBehaviorService.queryAppCrawlDetail(categoryIdStr, behaviorName);
	}

	@RequestMapping(value = "queryAppCrawlDetail")
	@ResponseBody
	public Map<String, Object> queryAppCrawlDetail(@RequestBody JSONObject params) {
		String startDate=params.getString("startDate");
		String endDate=params.getString("endDate");
		int crawlId = params.getIntValue("crawl_id");
		String industryName = params.getString("industry_id");
		Integer behaviorId = params.getInteger("behavior_id");
		int page = params.getIntValue("startPage");
		int num = params.getIntValue("pageSize");
		String orderName = params.getString("orderName");
		String orderRule = params.getString("orderRule");
		return appCrawlDoneService.queryAppCrawlDetail(startDate,endDate,crawlId, industryName,behaviorId, page, num, orderName, orderRule);
	}

	@RequestMapping(value = "updateExport")
	@ResponseBody
	public Map<String, Object> updateExport(@RequestBody JSONObject params) {
		int crawlId = params.getIntValue("crawl_id");
		int isexport = params.getIntValue("isexport");
		return appCrawlDoneService.updateExport(crawlId, isexport);
	}

	@RequestMapping(value = "deleteAppCrawlerDetail")
	@ResponseBody
	public Map<String, Object> deleteAppCrawlerDetail(@RequestBody JSONObject params) {
		Integer crawlDetailId = params.getInteger("crawl_detail_id");
		return appCrawlDoneService.deleteAppCrawlerDetail(crawlDetailId);
	}

	@RequestMapping(value = "modifyAppCrawlerDone")
	@ResponseBody
	public Map<String, Object> modifyAppCrawlerDone(@RequestBody JSONObject params) {
		int crawlId = params.getIntValue("crawl_id");
		String version = params.getString("crawl_app_version");
		int categoryId = params.getIntValue("crawl_app_category_id");
		return appCrawlDoneService.modifyAppCrawlerDone(crawlId, version, categoryId);
	}

	@RequestMapping(value = "deleteAppCrawlerDone")
	@ResponseBody
	public Map<String, Object> deleteAppCrawlerDone(@RequestBody JSONObject params) {
		int crawlId = params.getIntValue("crawl_id");
		return appCrawlDoneService.deleteAppCrawlerDone(crawlId);
	}

	@RequestMapping(value = "modifyDomain")
	@ResponseBody
	public Map<String, Object> modifyDomain(@RequestBody JSONObject params) {
		int domainId = params.getIntValue("domain_id");
		String crawlAppNum = params.getString("crawl_app_num");
		String domain = params.getString("crawl_app_domain");
		String attachParam = params.getString("crawl_app_attach_param");
		String typeNum = params.getString("type_num");
		return appCrawlDoneService.modifyDomain(domainId, crawlAppNum, domain, attachParam, typeNum);
	}

	@RequestMapping(value = "queryTransferType")
	@ResponseBody
	public Map<String, Object> queryTransferType(@RequestBody JSONObject params) {
		String crawlAppNum = params.getString("crawl_app_num");
		return appCrawlDoneService.queryTransferType(crawlAppNum);
	}

	@RequestMapping(value = "queryDomainByPageAndNum")
	@ResponseBody
	public Map<String, Object> queryDomainByPageAndNum(@RequestBody JSONObject params) {
		String crawlAppNum = params.getString("crawl_app_num");
		int page = params.getIntValue("startPage");
		int num = params.getIntValue("pageSize");
		return appCrawlDoneService.queryDomainByPageAndNum(crawlAppNum, page, num);
	}

	@RequestMapping(value = "delDomain")
	@ResponseBody
	public Map<String, Object> delDomain(@RequestBody JSONObject params) {
		Integer domainId = params.getInteger("domain_id");
		return appCrawlDoneService.delDomain(domainId);
	}

	@RequestMapping(value = "addDomain")
	@ResponseBody
	public Map<String, Object> addDomain(@RequestBody JSONObject params) {
		String crawlAppNum = params.getString("crawl_app_num");
		String domain = params.getString("crawl_app_domain");
		String attachParam = params.getString("crawl_app_attach_param");
		String typeNum = params.getString("type_num");
		return appCrawlDoneService.addDomain(crawlAppNum, domain, attachParam, typeNum);
	}

	@RequestMapping(value = "queryByPageAndNum")
	@ResponseBody
	public Map<String, Object> queryByPageAndNum(@RequestBody JSONObject params) {
		Integer parentCategoryId = params.getInteger("parentCategoryId");
		Integer childCategoryId = params.getInteger("childCategoryId");
		String appName = params.getString("appName");
		String appNumber = params.getString("appNumber");
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
		if (params.getString("startDate") == null) {
			startDate = null;
		}
		int page = params.getIntValue("startPage");
		int num = params.getIntValue("pageSize");
		String orderName = params.getString("orderName");
		String orderRule = params.getString("orderRule");
		return appCrawlDoneService.queryByPageAndNum(startDate, endDate, parentCategoryId, childCategoryId, appName,appNumber,
				page, num, orderName, orderRule);
	}

	@RequestMapping(value = "exportApp")
	@ResponseBody
	public void exportReg(HttpServletResponse response) throws Exception {
		//HSSFWorkbook workbook = appCrawlDoneService.generateAppWorkBook();
		// response.setContentType("application/vnd.ms-excel");
		response.setContentType("text/plain");
		String fileName = System.nanoTime() + ".txt";
		response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		// OutputStream ouputStream = response.getOutputStream();
		// workbook.write(ouputStream);

		BufferedOutputStream buff = null;
		StringBuffer write = new StringBuffer();
		String enter = "\r\n";
		ServletOutputStream outSTr = null;
		//添加主域名数据
		List<Map<String, Object>> demainDetailList = appCrawlDetailMapper.queryExportDomainDetail();
		for (Map<String, Object> demainDetailMap : demainDetailList) {
			write.append(demainDetailMap.get("domain_code")).append("\t").append(demainDetailMap.get("domain_domain"))
					.append("\t").append(demainDetailMap.get("domain_attach_param")).append("\t").append("1").append(enter);
		}
		//添加抓取详情数据
		List<String> DetailNumList =appCrawlDetailMapper.queryExportRegDetailNum();
		for (String detailNum : DetailNumList) {
			Map<String,Object> query=new HashMap<>();
			query.put("detailNum", detailNum);
			List<Map<String, Object>> regDetailList =appCrawlDetailMapper.queryExportRegDetail(query);
			write.append(detailNum).append("\t").append(regDetailList.get(0).get("crawl_detail_domain")).append("\t").append(regDetailList.get(0).get("crawl_detail_urlreg")).append("\t").append(regDetailList.get(0).get("crawl_detail_export_type")).append("\t");
			for(int i=0;i<regDetailList.size();i++){
				if(i<regDetailList.size()-1){
					write.append(regDetailList.get(i).get("crawl_detail_paramreg")).append("\t");
				}else if(i==regDetailList.size()-1){
					write.append(regDetailList.get(i).get("crawl_detail_paramreg"));
				}
			}
			write.append(enter);
		}
		
		try {
			outSTr = response.getOutputStream(); // 建立
			buff = new BufferedOutputStream(outSTr);
			//write.append(result.replaceAll("\n", enter));
			buff.write(write.toString().getBytes("UTF-8"));
			buff.flush();
			buff.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buff.close();
				outSTr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@RequestMapping(value = "exportRules")
	@ResponseBody
	public void exportParam(HttpServletResponse response) throws Exception {
				response.setContentType("text/plain");
				String fileName = System.nanoTime() + ".txt";
				response.setHeader("Content-disposition", "attachment;filename=" + fileName);

				BufferedOutputStream buff = null;
				StringBuffer write = new StringBuffer();
				String enter = "\r\n";
				ServletOutputStream outSTr = null;
				//添加抓取详情数据
				List<String> DetailNumList =appCrawlDetailMapper.queryExportRegDetailNum();
				for (String detailNum : DetailNumList) {
					Map<String,Object> query=new HashMap<>();
					query.put("detailNum", detailNum);
					List<Map<String, Object>> paramDetailList =appCrawlDetailMapper.queryExportParamDetail(query);
					write.append(detailNum).append("=");
					for(int i=0;i<paramDetailList.size();i++){
						if(i<paramDetailList.size()-1){
							write.append(paramDetailList.get(i).get("industry_num")).append(",");
						}else if(i==paramDetailList.size()-1){
							write.append(paramDetailList.get(i).get("industry_num"));
						}
					}
					write.append(enter);
				}
				try {
					outSTr = response.getOutputStream(); // 建立
					buff = new BufferedOutputStream(outSTr);
					buff.write(write.toString().getBytes("UTF-8"));
					buff.flush();
					buff.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						buff.close();
						outSTr.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
	}
}
