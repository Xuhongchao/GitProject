package com.pxene.model;

import java.util.Date;

/**
 * Created by chongaizhen on 2016/12/8.
 */
public class AppCrawlInfo {

    private int id;
    private int app_id;
    private String app_crawl_num;
    private int app_industry_id;
    private String app_crawl_behavior;
    private String app_crawl_domain;
    private String app_crawl_paramreg;
    private String app_crawl_urlreg;
    private String app_crawl_urlexample;
    private String app_crawl_comments;
    private Date app_crawl_createtime;
    private int app_status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public int getApp_status() {
        return app_status;
    }

    public void setApp_status(int app_status) {
        this.app_status = app_status;
    }

    public Date getApp_crawl_createtime() {
        return app_crawl_createtime;
    }

    public void setApp_crawl_createtime(Date app_crawl_createtime) {
        this.app_crawl_createtime = app_crawl_createtime;
    }

    public String getApp_crawl_comments() {
        return app_crawl_comments;
    }

    public void setApp_crawl_comments(String app_crawl_comments) {
        this.app_crawl_comments = app_crawl_comments;
    }

    public String getApp_crawl_urlexample() {
        return app_crawl_urlexample;
    }

    public void setApp_crawl_urlexample(String app_crawl_urlexample) {
        this.app_crawl_urlexample = app_crawl_urlexample;
    }

    public String getApp_crawl_urlreg() {
        return app_crawl_urlreg;
    }

    public void setApp_crawl_urlreg(String app_crawl_urlreg) {
        this.app_crawl_urlreg = app_crawl_urlreg;
    }

    public String getApp_crawl_paramreg() {
        return app_crawl_paramreg;
    }

    public void setApp_crawl_paramreg(String app_crawl_paramreg) {
        this.app_crawl_paramreg = app_crawl_paramreg;
    }

    public String getApp_crawl_domain() {
        return app_crawl_domain;
    }

    public void setApp_crawl_domain(String app_crawl_domain) {
        this.app_crawl_domain = app_crawl_domain;
    }

    public String getApp_crawl_behavior() {
        return app_crawl_behavior;
    }

    public void setApp_crawl_behavior(String app_crawl_behavior) {
        this.app_crawl_behavior = app_crawl_behavior;
    }

    public int getApp_industry_id() {
        return app_industry_id;
    }

    public void setApp_industry_id(int app_industry_id) {
        this.app_industry_id = app_industry_id;
    }

    public String getApp_crawl_num() {
        return app_crawl_num;
    }

    public void setApp_crawl_num(String app_crawl_num) {
        this.app_crawl_num = app_crawl_num;
    }
}
