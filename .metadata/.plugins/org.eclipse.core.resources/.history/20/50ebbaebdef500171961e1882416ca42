package com.wisetrust.request;

import javax.validation.constraints.NotEmpty;

/**
 * Created by @author xu on @date 2018年 1月 9日 下午5:55:06
 */
public class AccountUpdateRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	@NotEmpty(message = "密码不能为空")
	private String password;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
}
