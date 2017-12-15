package com.pxene.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.pxene.form.AppDetailForm;
import com.pxene.model.UrlDetail;
import com.pxene.service.DetailService;
import com.pxene.utils.DateUtil;

@Controller
@RequestMapping("/detail")
public class AppDetailController {
    @Autowired
    private DetailService detailService;

    //查询所有的
    @RequestMapping(value = "queryAll")
    @ResponseBody
    public List<UrlDetail> queryAll() {
        return detailService.findAll();
    }

    @RequestMapping(value = "findByCode")
    @ResponseBody
    public String findByCode(@RequestBody JSONObject params) {
        String code = params.getString("code");
        return detailService.findByCode(code);
    }

    //添加
    @RequestMapping(value = "add")
    @ResponseBody
    public void insert(@RequestBody JSONObject params) {
        UrlDetail appDetail = new UrlDetail();
        appDetail.setUrlcode(params.getString("urlcode"));
        appDetail.setAppcode(params.getString("appcode"));
        appDetail.setIndustrycode(params.getString("industrycode"));
        appDetail.setBehavior(params.getString("behavior"));
        appDetail.setPath(params.getString("path"));
//		appDetail.setOperatorType(params.getString("operatorType"));
        appDetail.setParam(params.getString("param"));
        appDetail.setPathtype(params.getString("pathtype"));
        appDetail.setUrl(params.getString("url"));
        appDetail.setRegexurl(params.getString("regexurl"));
        appDetail.setTip(params.getString("tip"));
        appDetail.setState(params.getString("state"));
        appDetail.setDatetime(new Date(params.getLong("datetime")));
        detailService.insert(appDetail);
    }

    //修改
    @RequestMapping(value = "update")
    @ResponseBody
    public UrlDetail update(@RequestBody JSONObject params) {
        UrlDetail detail = new UrlDetail();
//		System.out.println(params.getString("urlcode"));
        detail.setUrlcode(params.getString("urlcode"));
        detail.setId(params.getIntValue("id"));
        detail.setAppcode(params.getString("appcode"));
        detail.setIndustrycode(params.getString("industrycode"));
        detail.setBehavior(params.getString("behavior"));
        detail.setParam(params.getString("param"));
        detail.setPath(params.getString("path"));
        detail.setPathtype(params.getString("pathtype"));
//		detail.setOperatorType(params.getString("operatorType"));
        detail.setRegexurl(params.getString("regexurl"));
        detail.setUrl(params.getString("url"));
        detail.setState(params.getString("state"));
        detail.setTip(params.getString("tip"));
        detail.setDatetime(new Date(params.getLong("datetime")));
        return detailService.update(detail);
    }

    //删除
    @RequestMapping(value = "delete")
    @ResponseBody
    public boolean delete(@RequestBody JSONObject params) {
        int result = detailService.delete(params.getIntValue("id"));
        if (result > 0) {
            return true;
        }
        return false;
    }

    @RequestMapping(value = "query")
    @ResponseBody
    public List<UrlDetail> query(@RequestBody JSONObject params) {

        AppDetailForm appDetailForm = new AppDetailForm();
        String pathType = params.getString("pathType");
        Long startTime = params.getLong("startTime");
        String appCode = params.getString("appCode");
        String industryCode = params.getString("industryCode");
        Long endTime = params.getLong("endTime");
        String state = params.getString("state");
        appDetailForm.setPathType(pathType);
        if (state != null && !"-1".equals(state)) {
            appDetailForm.setState(state);
        }
        if (industryCode != null && !"-1".equals(industryCode)) {
            appDetailForm.setIndustryCode(industryCode);
        }
        if (appCode != null && !"-1".equals(appCode)) {
            appDetailForm.setAppCode(appCode);
        }

        appDetailForm.setStartTime(DateUtil.formatDate(startTime));
        appDetailForm.setEndTime(DateUtil.formatDate(endTime));

        List<UrlDetail> list = detailService.queryByForm(appDetailForm);
        return list;
    }
}
