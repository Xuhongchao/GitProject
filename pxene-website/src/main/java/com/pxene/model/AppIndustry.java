package com.pxene.model;

import java.util.Date;

/**
 * Created by chongaizhen on 2016/12/8.
 */
public class AppIndustry {

    private int id;
    private String app_industry_num;
    private String app_industry_name;
    private String app_fetch_content;
    private Date create_time;
    private int app_paramreg_num;

    public int getApp_paramreg_num() {
        return app_paramreg_num;
    }

    public void setApp_paramreg_num(int app_paramreg_num) {
        this.app_paramreg_num = app_paramreg_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApp_industry_num() {
        return app_industry_num;
    }

    public void setApp_industry_num(String app_industry_num) {
        this.app_industry_num = app_industry_num;
    }

    public String getApp_industry_name() {
        return app_industry_name;
    }

    public void setApp_industry_name(String app_industry_name) {
        this.app_industry_name = app_industry_name;
    }

    public String getApp_fetch_content() {
        return app_fetch_content;
    }

    public void setApp_fetch_content(String app_fetch_content) {
        this.app_fetch_content = app_fetch_content;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
