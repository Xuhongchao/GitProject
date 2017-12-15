package com.pxene.controller;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.pxene.model.App;
import com.pxene.service.AppService;

@Controller
@RequestMapping("/app")
public class AppController {

    @Autowired
    private AppService appService;

    @RequestMapping(value = "queryAll")
    @ResponseBody
    public List<App> queryAll(@RequestBody JSONObject params) {
        int type = params.getIntValue("type");
        return appService.findAll(type);
    }

    @RequestMapping(value = "findByPathType")
    @ResponseBody
    public List<App> findByPath(@RequestBody JSONObject params) {
        int type = params.getIntValue("type");
        return appService.findByPath(type);
    }

    @RequestMapping(value = "findByCode")
    @ResponseBody
    public String findByCode(@RequestBody JSONObject params) {
        App app = new App();
        app.setType(params.getIntValue("type"));
        app.setCode(params.getString("code"));
        return appService.findByCode(app);
    }

    //添加
    @RequestMapping(value = "add")
    @ResponseBody
    public void insertApp(@RequestBody JSONObject params) {
        App app = new App();

        app.setCode(params.getString("code"));
        app.setName(params.getString("name"));
        app.setType(params.getIntValue("type"));
        app.setUrl(params.getString("url"));
        app.setParam(params.getString("param"));
        app.setState(params.getString("state"));
        app.setRemarks(params.getString("remarks"));
        app.setDateTime(new Date());
        appService.add(app);
    }

    //修改
    @RequestMapping(value = "update")
    @ResponseBody
    public App update(@RequestBody JSONObject params) {
//		System.out.println(params.getIntValue("id"));
        App app = new App();
        app.setId(params.getIntValue("id"));
        app.setCode(params.getString("code"));
        app.setName(params.getString("name"));
        app.setType(params.getIntValue("type"));
        app.setUrl(params.getString("url"));
        app.setParam(params.getString("param"));
        app.setState(params.getString("state"));
        app.setRemarks(params.getString("remarks"));
        app.setDateTime(new Date());
        return appService.update(app);
    }

    //删除
    @RequestMapping(value = "delete")
    @ResponseBody
    public boolean delete(@RequestBody JSONObject params) {
        int id = params.getIntValue("id");
//    public boolean delete() {
//        int id=31;
        int result = appService.delete(id);
        if (result > 0) {
            return true;
        }
        return false;
    }
}
