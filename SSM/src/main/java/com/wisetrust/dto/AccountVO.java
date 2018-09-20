package com.wisetrust.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * Created by @author xu on @date 2018年 月 日 下午3:05:14
 * 
 * 用来在web层和service层传输账户信息
 */
@Data
public class AccountVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6320253323418626476L;

	private Long id;
	private String name;
	private String phone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(String gmtModified) {
		this.gmtModified = gmtModified;
	}

	private String post;
	private Byte status;
	private String lastLoginTime;
	private String gmtModified;

}
