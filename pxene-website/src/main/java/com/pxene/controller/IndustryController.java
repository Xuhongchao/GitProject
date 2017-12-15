package com.pxene.controller;

import java.util.Date;
import java.util.List;

import com.pxene.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.pxene.model.App;
import com.pxene.model.Industry;
import com.pxene.service.IndustryService;

@Controller
@RequestMapping("/industry")
public class IndustryController {
    @Autowired
    private IndustryService industryService;

    //查询所有的行业
    @RequestMapping(value = "queryAll")
    @ResponseBody
    public List<Industry> queryAll() {
        return industryService.findAll();
    }

    //通过行业id查询行业的名字
    @RequestMapping(value = "findByCode")
    @ResponseBody
    public String findByCode(@RequestBody JSONObject params) {
        String code = params.getString("code");
        return industryService.findByCode(code);
    }

    @RequestMapping(value = "findByType")
    @ResponseBody
    public List<Industry> findByType(@RequestBody JSONObject params) {
        int type = params.getIntValue("type");
        return industryService.findByType(type);
    }

    //添加
    @RequestMapping(value = "add")
    @ResponseBody
    public void insert(@RequestBody JSONObject params) {
        Industry industry = new Industry();
        industry.setCode(params.getString("code"));
        industry.setName(params.getString("name"));
        industry.setContent(params.getString("content"));
        industry.setDateTime(new Date());
        industryService.add(industry);
    }

    //修改
    @RequestMapping(value = "update")
    @ResponseBody
    public Industry update(@RequestBody JSONObject params) {
        Industry industry = new Industry();
        industry.setCode(params.getString("code"));
        industry.setName(params.getString("name"));
        industry.setId(params.getInteger("id"));
        industry.setContent(params.getString("content"));
        return industryService.update(industry);
    }

    //删除
    @RequestMapping(value = "delete")
    @ResponseBody
    public boolean delete(@RequestBody JSONObject params) {
        int id = params.getIntValue("id");
//		System.out.println("进入删除方法"+id);
        int result = industryService.delete(id);
        if (result > 0) {
            return true;
        }
        return false;
    }
}
