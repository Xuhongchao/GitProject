package com.pxene.model;

import java.util.Date;

/**
 * Created by chongaizhen on 2016/12/8.
 */
public class AppCategory {

    private int id;
    private String app_category_num;
    private String app_category_name;
    private Date create_time;

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApp_category_num() {
        return app_category_num;
    }

    public void setApp_category_num(String app_category_num) {
        this.app_category_num = app_category_num;
    }

    public String getApp_category_name() {
        return app_category_name;
    }

    public void setApp_category_name(String app_category_name) {
        this.app_category_name = app_category_name;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

}
