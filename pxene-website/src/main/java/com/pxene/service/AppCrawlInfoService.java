package com.pxene.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pxene.model.AppCrawlInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created by chongaizhen on 2016/12/8.
 */
public interface AppCrawlInfoService {

	Map<String,Object> addAppCrawlInfo(AppCrawlInfo app_crawlInfo,int appOs,String appNum,String industryNum);
	Map<String,Object> updateAppCrawlInfo(AppCrawlInfo app_crawlInfo,int appOs,String appNum,String industryNum);
	Map<String,Object> delAppCrawlInfo(int id);
	//点击行业下的详情进行查询
	Map<String,Object> queryByIndustryId(int industryId,java.sql.Date startDate,java.sql.Date endDate,int page, int num,int categoryId,String search);
	//点击app抓取下的详情进行查询
	Map<String,Object> queryByAppInfoId(int appInfoId,java.sql.Date startDate,java.sql.Date endDate,int page, int num,int industryId);
	Map<String,Object> testAppCrawlInfo(AppCrawlInfo appCrawlInfo);
	HSSFWorkbook exportAdid();
	HSSFWorkbook exportSearch();
	HSSFWorkbook exportApiList();
	HSSFWorkbook exportRules();
}
