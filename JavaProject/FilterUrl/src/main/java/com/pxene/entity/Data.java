package com.pxene.entity;

/**
 * Created by @author xuhongchao on @date 2017年 月 日  10:21:41
 */

public class Data {
	private String num;
	private String domain;
	private String param;
	private String urlReg;
	private String urlExam;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getUrlReg() {
		return urlReg;
	}

	public void setUrlReg(String urlReg) {
		this.urlReg = urlReg;
	}

	public String getUrlExam() {
		return urlExam;
	}

	public void setUrlExam(String urlExam) {
		this.urlExam = urlExam;
	}

	@Override
	public String toString() {
		return "Data [num=" + num + ", domain=" + domain + ", param=" + param
				+ ", urlReg=" + urlReg + ", urlExam=" + urlExam + "]";
	}

}
