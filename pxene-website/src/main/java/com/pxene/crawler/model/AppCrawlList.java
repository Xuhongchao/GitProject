package com.pxene.crawler.model;

import java.sql.Timestamp;

public class AppCrawlList {
	
	private Integer app_id;
	private Integer app_priority;
	private Long app_ranking1;
	private Integer app_ranking2;
	private String app_logo_url;
	private String app_name;
	private String app_name_crawl;
	private String app_category_name;
	private String app_version;
	private Timestamp app_update_time;
	private String app_source;
	private Timestamp app_create_time;
	private Integer app_status;
	private Integer app_is_blacklist;
	private Integer app_type;
	private String app_download_url;
	private String app_appId;
	private String app_version_update;
	
	public String getApp_version_update() {
		return app_version_update;
	}
	public void setApp_version_update(String app_version_update) {
		this.app_version_update = app_version_update;
	}
	public String getApp_name_crawl() {
		return app_name_crawl;
	}
	public void setApp_name_crawl(String app_name_crawl) {
		this.app_name_crawl = app_name_crawl;
	}
	public String getApp_category_name() {
		return app_category_name;
	}
	public void setApp_category_name(String app_category_name) {
		this.app_category_name = app_category_name;
	}
	public String getApp_appId() {
		return app_appId;
	}
	public void setApp_appId(String app_appId) {
		this.app_appId = app_appId;
	}
	public Integer getApp_priority() {
		return app_priority;
	}
	public Integer getApp_id() {
		return app_id;
	}
	public void setApp_id(Integer app_id) {
		this.app_id = app_id;
	}
	public void setApp_priority(Integer app_priority) {
		this.app_priority = app_priority;
	}
	
	public Long getApp_ranking1() {
		return app_ranking1;
	}
	public void setApp_ranking1(Long app_ranking1) {
		this.app_ranking1 = app_ranking1;
	}
	public Integer getApp_ranking2() {
		return app_ranking2;
	}
	public void setApp_ranking2(Integer app_ranking2) {
		this.app_ranking2 = app_ranking2;
	}
	public String getApp_logo_url() {
		return app_logo_url;
	}
	public void setApp_logo_url(String app_logo_url) {
		this.app_logo_url = app_logo_url;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getApp_version() {
		return app_version;
	}
	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}
	public String getApp_source() {
		return app_source;
	}
	public void setApp_source(String app_source) {
		this.app_source = app_source;
	}
	public Integer getApp_status() {
		return app_status;
	}
	public void setApp_status(Integer app_status) {
		this.app_status = app_status;
	}
	public Integer getApp_is_blacklist() {
		return app_is_blacklist;
	}
	public void setApp_is_blacklist(Integer app_is_blacklist) {
		this.app_is_blacklist = app_is_blacklist;
	}
	public Integer getApp_type() {
		return app_type;
	}
	public void setApp_type(Integer app_type) {
		this.app_type = app_type;
	}
	public String getApp_download_url() {
		return app_download_url;
	}
	public void setApp_download_url(String app_download_url) {
		this.app_download_url = app_download_url;
	}
	public Timestamp getApp_update_time() {
		return app_update_time;
	}
	public void setApp_update_time(Timestamp app_update_time) {
		this.app_update_time = app_update_time;
	}
	public Timestamp getApp_create_time() {
		return app_create_time;
	}
	public void setApp_create_time(Timestamp app_create_time) {
		this.app_create_time = app_create_time;
	}
	
}
