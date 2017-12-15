package com.pxene.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.pxene.model.AppCrawlInfo;
import com.pxene.service.AppCategoryService;
import com.pxene.service.AppCrawlInfoService;
import com.pxene.service.AppIndustryService;

/**
 * Created by chongaizhen on 2016/12/8.
 */
@Controller
@RequestMapping("/appCrawlInfo")
public class AppCrawlInfoController {
	
    @Autowired
    AppCrawlInfoService appCrawlInfoService;
    @Autowired
    AppCategoryService appCategoryService;
    @Autowired
    AppIndustryService appIndustryService;

    @RequestMapping(value = "addAppCrawlInfo")
    @ResponseBody
    public Map<String,Object> addAppCrawlInfo(@RequestBody JSONObject params){
        AppCrawlInfo appCrawlInfo=new AppCrawlInfo();
        appCrawlInfo.setApp_id(params.getIntValue("appId"));
        appCrawlInfo.setApp_industry_id(params.getIntValue("appIndustryId"));
        appCrawlInfo.setApp_crawl_behavior(params.getString("appCrawlBehavior"));
        appCrawlInfo.setApp_crawl_domain(params.getString("appCrawlDomain"));
        appCrawlInfo.setApp_crawl_paramreg(params.getString("appCrawlParamreg").replace("\\", "\\\\"));
        appCrawlInfo.setApp_crawl_urlreg(params.getString("appCrawlUrlreg").replace("\\", "\\\\"));
        appCrawlInfo.setApp_crawl_urlexample(params.getString("appCrawlUrlexample"));
        appCrawlInfo.setApp_crawl_comments(params.getString("appCrawlComments"));
        appCrawlInfo.setApp_crawl_createtime(new java.sql.Date(new Date().getTime()));
        appCrawlInfo.setApp_status(0);
        int appOs=params.getIntValue("appOs");
        String appNum=params.getString("appNum");
        String industryNum=params.getString("industryNum");
        return appCrawlInfoService.addAppCrawlInfo(appCrawlInfo, appOs, appNum, industryNum);
    }

    @RequestMapping(value = "updateAppCrawlInfo")
    @ResponseBody
    public Map<String,Object> updateAppCrawlInfo(@RequestBody JSONObject params){
        AppCrawlInfo appCrawlInfo=new AppCrawlInfo();
        appCrawlInfo.setId(params.getIntValue("appCrawlInfoId"));
        appCrawlInfo.setApp_industry_id(params.getIntValue("appIndustryId"));
        appCrawlInfo.setApp_crawl_behavior(params.getString("appCrawlBehavior"));
        appCrawlInfo.setApp_crawl_domain(params.getString("appCrawlDomain"));
        appCrawlInfo.setApp_crawl_paramreg(params.getString("appCrawlParamreg"));
        appCrawlInfo.setApp_crawl_urlreg(params.getString("appCrawlUrlreg"));
        appCrawlInfo.setApp_crawl_urlexample(params.getString("appCrawlUrlexample"));
        appCrawlInfo.setApp_crawl_comments(params.getString("appCrawlComments"));
        int appOs=params.getIntValue("appOs");
        String appNum=params.getString("appNum");
        String industryNum=params.getString("industryNum");
        return appCrawlInfoService.updateAppCrawlInfo(appCrawlInfo, appOs, appNum, industryNum);
    }

    @RequestMapping(value = "testAppCrawlInfo")
    @ResponseBody
    public Map<String,Object> testAppCrawlInfo(@RequestBody JSONObject params){
        AppCrawlInfo appCrawlInfo=new AppCrawlInfo();
        List list=params.getJSONArray("AppCrawlStatus");
        for (int i=0;i<list.size();i++){
            JSONObject jsonObject= (JSONObject) list.get(i);
            appCrawlInfo.setId(jsonObject.getIntValue("appCrawlInfoId"));
            appCrawlInfo.setApp_status(jsonObject.getIntValue("status"));
            appCrawlInfoService.testAppCrawlInfo(appCrawlInfo);
        }
        return  null;
    }

    @RequestMapping(value = "delAppCrawlInfo")
    @ResponseBody
    public Map<String,Object> delAppInfo(@RequestBody JSONObject params){
        int crawlInfoId=params.getIntValue("crawlInfoId");
        return appCrawlInfoService.delAppCrawlInfo(crawlInfoId);
    }

 /*   @RequestMapping(value = "queryIndustry")
    @ResponseBody
    public Map<String,Object> queryIndustry(){
        return appIndustryService.queryMes();
    }*/

    @RequestMapping(value = "queryByAppInfoId")
    @ResponseBody
    public Map<String,Object> queryByAppInfoId(@RequestBody JSONObject params){
        int industryId=params.getIntValue("industryId");
        int appInfoId=params.getIntValue("id");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        try {
            date=simpleDateFormat.parse(params.getString("startDate"));
        }catch (Exception e){

        }
        java.sql.Date startDate = new java.sql.Date(date.getTime());
        try {
            date=simpleDateFormat.parse(params.getString("endDate"));
        }catch (Exception e){

        }
        java.sql.Date endDate = new java.sql.Date(date.getTime());
        int page=params.getIntValue("startPage");
        int num=params.getIntValue("pageSize");
        return appCrawlInfoService.queryByAppInfoId(appInfoId, startDate, endDate, page, num, industryId);
    }

/*    @RequestMapping(value = "queryCategory")
    @ResponseBody
    public Map<String,Object> queryCategory(){
        return appCategoryService.queryMes();
    }*/

    @RequestMapping(value = "queryByIndustryId")
    @ResponseBody
    public Map<String,Object> queryByIndustryId(@RequestBody JSONObject params){
        int categoryId=params.getIntValue("categoryId");
        int industryId=params.getIntValue("industryId");
        String search=params.getString("search");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        try {
            date=simpleDateFormat.parse(params.getString("startDate"));
        }catch (Exception e){

        }
        java.sql.Date startDate = new java.sql.Date(date.getTime());
        try {
            date=simpleDateFormat.parse(params.getString("endDate"));
        }catch (Exception e){

        }
        java.sql.Date endDate = new java.sql.Date(date.getTime());
        int page=params.getIntValue("startPage");
        int num=params.getIntValue("pageSize");
        return appCrawlInfoService.queryByIndustryId(industryId,startDate, endDate, page, num,categoryId,search);
    }

//    @RequestMapping(value = "exportAdid")
//    @ResponseBody
//    public void exportAdid(HttpServletRequest request, HttpServletResponse response)
//            throws Exception {
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-disposition", "attachment;filename=adid.xls");
//        HSSFWorkbook wb = appCrawlInfoService.exportAdid();
//        OutputStream ouputStream = response.getOutputStream();
//        wb.write(ouputStream);
//        ouputStream.flush();
//        ouputStream.close();
//    }
//
//    @RequestMapping(value = "exportSearch")
//    @ResponseBody
//    public void exportSearch(HttpServletRequest request, HttpServletResponse response)
//            throws Exception {
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-disposition", "attachment;filename=search.xls");
//        HSSFWorkbook wb = appCrawlInfoService.exportSearch();
//        OutputStream ouputStream = response.getOutputStream();
//        wb.write(ouputStream);
//        ouputStream.flush();
//        ouputStream.close();
//    }
//
//    @RequestMapping(value = "exportApiList")
//    @ResponseBody
//    public void exportApiList(HttpServletRequest request, HttpServletResponse response)
//            throws Exception {
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-disposition", "attachment;filename=apiList.xls");
//        HSSFWorkbook wb = appCrawlInfoService.exportApiList();
//        OutputStream ouputStream = response.getOutputStream();
//        wb.write(ouputStream);
//        ouputStream.flush();
//        ouputStream.close();
//    }

    @RequestMapping(value = "exportRules")
    @ResponseBody
    public void exportRules(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=rules.xls");
        HSSFWorkbook exportRules = appCrawlInfoService.exportRules();
        OutputStream osExportRules = response.getOutputStream();
        exportRules.write(osExportRules);
        osExportRules.flush();
        osExportRules.close();
    }

}
