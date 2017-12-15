package com.pxene.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pxene.dao.AppCategoryMapper;
import com.pxene.dao.AppCrawlInfoMapper;
import com.pxene.dao.AppIndustryMapper;
import com.pxene.dao.AppInfoMapper;
import com.pxene.model.AppCrawlInfo;
import com.pxene.service.AppCrawlInfoService;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chongaizhen on 2016/12/8.
 */
@Service("appCrawlInfoService")
public class AppCrawlInfoServiceImpl implements AppCrawlInfoService{

	@Autowired
	AppCrawlInfoMapper appCrawlInfoMapper;

	@Override
	public Map<String, Object> addAppCrawlInfo(AppCrawlInfo app_crawlInfo,int appOs,String appNum,String industryNum) {
		Map addCrawlInfo=new HashMap();
		addCrawlInfo.put("appCrawlBehavior", app_crawlInfo.getApp_crawl_behavior());
		addCrawlInfo.put("appCrawlDomain",app_crawlInfo.getApp_crawl_domain());
		addCrawlInfo.put("appCrawlParamreg",app_crawlInfo.getApp_crawl_paramreg());
		addCrawlInfo.put("appCrawlUrlreg",app_crawlInfo.getApp_crawl_urlreg());
		addCrawlInfo.put("appCrawlUrlexample",app_crawlInfo.getApp_crawl_urlexample());
		addCrawlInfo.put("appCrawlComments", app_crawlInfo.getApp_crawl_comments());
		addCrawlInfo.put("appCrawlCreatetime",app_crawlInfo.getApp_crawl_createtime());
		addCrawlInfo.put("appStatus", app_crawlInfo.getApp_status());
		String startNum=appOs+industryNum+appNum+"000";
		String endNum=appOs+industryNum+appNum+"999";
		Map numMap=new HashMap();
		numMap.put("startNum",startNum);
		numMap.put("endNum",endNum);
		String oldNum=appCrawlInfoMapper.queryCrawlInfoNum(numMap);
		String newNum="";
		int num=1;
		if (oldNum!=null){
			oldNum=oldNum.substring(9,12);
			num=Integer.valueOf(oldNum)+1;
		}
		newNum =appOs+industryNum+appNum+String.format("%03d", num);
		System.out.println("NewNum is "+newNum);
		addCrawlInfo.put("appCrawlNum", newNum);
		addCrawlInfo.put("appId",app_crawlInfo.getApp_id());
		addCrawlInfo.put("appIndustryId",app_crawlInfo.getApp_industry_id());
		Map result=new HashMap();
		int queryNum=0;
		queryNum=appCrawlInfoMapper.queryDomainAndUrlRegByAppId(addCrawlInfo);
		boolean addResult=false;
		if (0==queryNum){
			addResult = appCrawlInfoMapper.addAppCrawlInfo(addCrawlInfo);
		}
		if (addResult){
			result.put("resultCode",0);
		}else {
			result.put("resultCode",1);
		}
		return result;
	}

	@Override
	public Map<String, Object> updateAppCrawlInfo(AppCrawlInfo app_crawlInfo,int appOs,String appNum,String industryNum) {
		Map result=new HashMap();
		String startNum=appOs+industryNum+appNum+"000";
		String endNum=appOs+industryNum+appNum+"999";
		Map numMap=new HashMap();
		numMap.put("startNum",startNum);
		numMap.put("endNum",endNum);
		String oldNum=appCrawlInfoMapper.queryCrawlInfoNum(numMap);
		String newNum="";
		int num=1;
		if (oldNum!=null){
			oldNum=oldNum.substring(9,12);
			num=Integer.valueOf(oldNum)+1;
		}
		newNum =appOs+industryNum+appNum+String.format("%03d", num);
		app_crawlInfo.setApp_crawl_num(newNum);
		AppCrawlInfo appCrawlInfo=appCrawlInfoMapper.queryById(app_crawlInfo);
		if ((app_crawlInfo.getApp_crawl_paramreg().equals(appCrawlInfo.getApp_crawl_paramreg())) && (app_crawlInfo.getApp_crawl_urlreg().equals(appCrawlInfo.getApp_crawl_urlreg()))){
			app_crawlInfo.setApp_status(-1);
		}
		else {
			app_crawlInfo.setApp_status(0);
		}
		boolean updateResult = appCrawlInfoMapper.updateAppCrawlInfo(app_crawlInfo);
		if (updateResult){
			result.put("resultCode",0);
		}else {
			result.put("resultCode",1);
		}
		return result;
	}

	@Override
	public Map<String, Object> delAppCrawlInfo(int id) {
		Map idMap=new HashMap();
		idMap.put("id",id);
		Map result=new HashMap();
		boolean delResult = appCrawlInfoMapper.delAppCrawlInfo(idMap);
		if (delResult){
			result.put("resultCode",0);
		}else {
			result.put("resultCode",1);
		}
		return result;
	}

	@Override
	public Map<String, Object> queryByIndustryId(int industryId, java.sql.Date startDate, java.sql.Date endDate, int page, int num,int categoryId,String search) {
		Map map=new HashMap();
		map.put("page",page);
		map.put("num",num);
		map.put("startDate",startDate);
		map.put("endDate",endDate);
		map.put("industryId",industryId);
		map.put("categoryId",categoryId);
		map.put("search",search);
		Map result=new HashMap<String, Object>();
		result.put("totalcount",appCrawlInfoMapper.queryAllByIndustryId(map));
		List<Map<String, Object>> list = appCrawlInfoMapper.queryByIndustryId(map);
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
		for (int i=0;i<list.size();i++){
			Map<String,Object> queryMap=list.get(i);
			for (String key : queryMap.keySet()) {
				if("app_crawl_createtime".equals(key)){
					String time=String.valueOf(queryMap.get(key));
					Date date=new Date();
					try{
						date = format.parse(time);
					}catch (Exception e){

					}
					String newTime = format.format(date);
					queryMap.put(key,newTime);
				}
				if("statistics_time".equals(key)){
					String time=String.valueOf(queryMap.get(key));
					Date date=new Date();
					try{
						date = format.parse(time);
					}catch (Exception e){

					}
					String newTime = format.format(date);
					queryMap.put(key,newTime);
				}
			}
		}
		result.put("list",list);
		return result;
	}

	@Override
	public Map<String, Object> queryByAppInfoId(int appInfoId, java.sql.Date startDate, java.sql.Date endDate, int page, int num,int industryId) {
		Map map=new HashMap();
		map.put("page",page);
		map.put("num",num);
		map.put("startDate",startDate);
		map.put("endDate",endDate);
		map.put("appInfoId",appInfoId);
		map.put("industryId",industryId);
		Map result=new HashMap<String, Object>();
		result.put("totalcount",appCrawlInfoMapper.queryAllByAppId(map));
		List<Map<String, Object>> list = appCrawlInfoMapper.queryByAppInfoId(map);
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
		for (int i=0;i<list.size();i++){
			Map<String,Object> queryMap=list.get(i);
			for (String key : queryMap.keySet()) {
				if("app_crawl_createtime".equals(key)){
					String time=String.valueOf(queryMap.get(key));
					Date date=new Date();
					try{
						date = format.parse(time);
					}catch (Exception e){

					}
					String newTime = format.format(date);
					queryMap.put(key,newTime);
				}
				if("statistics_time".equals(key)){
					String time=String.valueOf(queryMap.get(key));
					Date date=new Date();
					try{
						date = format.parse(time);
					}catch (Exception e){

					}
					String newTime = format.format(date);
					queryMap.put(key,newTime);
				}
			}
		}
		result.put("list",list);
		return result;
	}

	@Override
	public Map<String, Object> testAppCrawlInfo(AppCrawlInfo appCrawlInfo) {
		Map result=new HashMap();
		boolean updateResult = appCrawlInfoMapper.testAppCrawlInfo(appCrawlInfo);
		if (updateResult){
			result.put("resultCode",0);
		}else {
			result.put("resultCode",1);
		}
		return result;
	}

	@Override
	public HSSFWorkbook exportAdid() {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("ExportAdid");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		List<AppCrawlInfo> appCrawlInfoList=appCrawlInfoMapper.exportAdid();

		for (int i = 0; i < appCrawlInfoList.size(); i++) {
			row = sheet.createRow(i);
			AppCrawlInfo appCrawlInfo = appCrawlInfoList.get(i);
			row.createCell(0).setCellValue(appCrawlInfo.getApp_crawl_num());
			row.createCell(1).setCellValue(appCrawlInfo.getApp_crawl_domain());
			row.createCell(2).setCellValue(appCrawlInfo.getApp_crawl_urlreg());
			row.createCell(2).setCellValue(appCrawlInfo.getApp_crawl_paramreg());
		}
		return wb;
	}

	@Override
	public HSSFWorkbook exportSearch() {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("ExportSearch");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		List<AppCrawlInfo> appCrawlInfoList=appCrawlInfoMapper.exportSearch();

		for (int i = 0; i < appCrawlInfoList.size(); i++) {
			row = sheet.createRow(i);
			AppCrawlInfo appCrawlInfo = appCrawlInfoList.get(i);
			row.createCell(0).setCellValue(appCrawlInfo.getApp_crawl_num());
			row.createCell(1).setCellValue(appCrawlInfo.getApp_crawl_domain());
			row.createCell(2).setCellValue(appCrawlInfo.getApp_crawl_urlreg());
			row.createCell(2).setCellValue(appCrawlInfo.getApp_crawl_paramreg());
		}
		return wb;
	}

	@Override
	public HSSFWorkbook exportApiList() {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("ExportApiList");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		List<AppCrawlInfo> appCrawlInfoList=appCrawlInfoMapper.exportApiList();

		for (int i = 0; i < appCrawlInfoList.size(); i++) {
			row = sheet.createRow(i);
			AppCrawlInfo appCrawlInfo = appCrawlInfoList.get(i);
			row.createCell(0).setCellValue(appCrawlInfo.getApp_crawl_num());
			row.createCell(1).setCellValue(appCrawlInfo.getApp_crawl_domain());
			row.createCell(2).setCellValue(appCrawlInfo.getApp_crawl_urlreg());
			row.createCell(2).setCellValue(appCrawlInfo.getApp_crawl_paramreg());
		}
		return wb;
	}

	@Override
	public HSSFWorkbook exportRules() {
		HSSFWorkbook wb = new HSSFWorkbook();
		for (int i=0;i<3;i++){
			if (i==0){
				HSSFSheet sheet = wb.createSheet("adid");
				HSSFRow row = sheet.createRow((int) 0);
				HSSFCellStyle style = wb.createCellStyle();
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

				List<AppCrawlInfo> appCrawlInfoList=appCrawlInfoMapper.exportAdid();

				for (int j = 0; j < appCrawlInfoList.size(); j++) {
					row = sheet.createRow(j);
					AppCrawlInfo appCrawlInfo = appCrawlInfoList.get(j);
					row.createCell(0).setCellValue(appCrawlInfo.getApp_crawl_num());
					row.createCell(1).setCellValue(appCrawlInfo.getApp_crawl_domain());
					row.createCell(2).setCellValue(appCrawlInfo.getApp_crawl_urlreg());
					row.createCell(3).setCellValue(appCrawlInfo.getApp_crawl_paramreg());
				}
			}
			if (i==1){
				HSSFSheet sheet = wb.createSheet("search");
				HSSFRow row = sheet.createRow((int) 0);
				HSSFCellStyle style = wb.createCellStyle();
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

				List<AppCrawlInfo> appCrawlInfoList=appCrawlInfoMapper.exportSearch();

				for (int j = 0; j < appCrawlInfoList.size(); j++) {
					row = sheet.createRow(j);
					AppCrawlInfo appCrawlInfo = appCrawlInfoList.get(j);
					row.createCell(0).setCellValue(appCrawlInfo.getApp_crawl_num());
					row.createCell(1).setCellValue(appCrawlInfo.getApp_crawl_domain());
					row.createCell(2).setCellValue(appCrawlInfo.getApp_crawl_urlreg());
					row.createCell(3).setCellValue(appCrawlInfo.getApp_crawl_paramreg());
				}
			}
			if (i==2){
				HSSFSheet sheet = wb.createSheet("apiList");
				HSSFRow row = sheet.createRow((int) 0);
				HSSFCellStyle style = wb.createCellStyle();
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

				List<AppCrawlInfo> appCrawlInfoList=appCrawlInfoMapper.exportApiList();

				for (int j = 0; j < appCrawlInfoList.size(); j++) {
					row = sheet.createRow(j);
					AppCrawlInfo appCrawlInfo = appCrawlInfoList.get(j);
					row.createCell(0).setCellValue(appCrawlInfo.getApp_crawl_num());
					row.createCell(1).setCellValue(appCrawlInfo.getApp_crawl_domain());
					row.createCell(2).setCellValue(appCrawlInfo.getApp_crawl_urlreg());
					row.createCell(3).setCellValue(appCrawlInfo.getApp_crawl_paramreg());
				}
			}
		}
		return wb;
	}
}
