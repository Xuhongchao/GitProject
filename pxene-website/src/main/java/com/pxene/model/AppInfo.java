package com.pxene.model;

import java.util.Date;

/**
 * Created by chongaizhen on 2016/12/8.
 */
public class AppInfo {

    private int id;
    private String app_num;
    private String app_name;
    private int app_category_id;
    private String app_source;
    private String app_domain;
    private String app_attach_param;
    private int app_os;
    private Date app_create_time;
    private int app_isexport;
    private String app_logo_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApp_logo_url() {
        return app_logo_url;
    }

    public void setApp_logo_url(String app_logo_url) {
        this.app_logo_url = app_logo_url;
    }

    public int getApp_isexport() {
        return app_isexport;
    }

    public void setApp_isexport(int app_isexport) {
        this.app_isexport = app_isexport;
    }

    public Date getApp_create_time() {
        return app_create_time;
    }

    public void setApp_create_time(Date app_create_time) {
        this.app_create_time = app_create_time;
    }

    public int getApp_os() {
        return app_os;
    }

    public void setApp_os(int app_os) {
        this.app_os = app_os;
    }

    public String getApp_attach_param() {
        return app_attach_param;
    }

    public void setApp_attach_param(String app_attach_param) {
        this.app_attach_param = app_attach_param;
    }

    public String getApp_domain() {
        return app_domain;
    }

    public void setApp_domain(String app_domain) {
        this.app_domain = app_domain;
    }

    public String getApp_source() {
        return app_source;
    }

    public void setApp_source(String app_source) {
        this.app_source = app_source;
    }

    public int getApp_category_id() {
        return app_category_id;
    }

    public void setApp_category_id(int app_category_id) {
        this.app_category_id = app_category_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_num() {
        return app_num;
    }

    public void setApp_num(String app_num) {
        this.app_num = app_num;
    }
}
