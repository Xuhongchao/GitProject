package com.pxene.model;

import java.util.Date;

public class UrlDetail {
    private Integer id;

    private String urlcode;

    private String appcode;

    private String industrycode;

    private String behavior;

    private String path;

    private String pathtype;

    private String operatortype;

    private String param;

    private String regexurl;

    private String url;

    private String tip;

    private String state;

    private Date datetime;

    private App app;
    
    private Industry industry;
    
    public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Industry getIndustry() {
		return industry;
	}

	public void setIndustry(Industry industry) {
		this.industry = industry;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrlcode() {
        return urlcode;
    }

    public void setUrlcode(String urlcode) {
        this.urlcode = urlcode == null ? null : urlcode.trim();
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode == null ? null : appcode.trim();
    }

    public String getIndustrycode() {
        return industrycode;
    }

    public void setIndustrycode(String industrycode) {
        this.industrycode = industrycode == null ? null : industrycode.trim();
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior == null ? null : behavior.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getPathtype() {
        return pathtype;
    }

    public void setPathtype(String pathtype) {
        this.pathtype = pathtype == null ? null : pathtype.trim();
    }

    public String getOperatortype() {
        return operatortype;
    }

    public void setOperatortype(String operatortype) {
        this.operatortype = operatortype == null ? null : operatortype.trim();
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param == null ? null : param.trim();
    }

    public String getRegexurl() {
        return regexurl;
    }

    public void setRegexurl(String regexurl) {
        this.regexurl = regexurl == null ? null : regexurl.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip == null ? null : tip.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}