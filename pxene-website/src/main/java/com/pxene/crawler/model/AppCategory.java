package com.pxene.crawler.model;

import java.sql.Date;

public class AppCategory {
	
	private int id;
	private String app_category_num;
	private String app_category_name;
	private String app_category_baiduname;
	private String app_category_appstore;
	private String app_category_360name;
	private String app_category_miname;
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
	public String getApp_category_baiduname() {
		return app_category_baiduname;
	}
	public void setApp_category_baiduname(String app_category_baiduname) {
		this.app_category_baiduname = app_category_baiduname;
	}
	public String getApp_category_appstore() {
		return app_category_appstore;
	}
	public void setApp_category_appstore(String app_category_appstore) {
		this.app_category_appstore = app_category_appstore;
	}
	public String getApp_category_360name() {
		return app_category_360name;
	}
	public void setApp_category_360name(String app_category_360name) {
		this.app_category_360name = app_category_360name;
	}
	public String getApp_category_miname() {
		return app_category_miname;
	}
	public void setApp_category_miname(String app_category_miname) {
		this.app_category_miname = app_category_miname;
	}
	
	
	
}
