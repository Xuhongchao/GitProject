package com.wisetrust.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Created by @author xu on @date 2018年 1月 15日 下午4:38:59
 * 
 * <br/>
 * 校验账户请求数据的类
 */
@Data
public class AccountRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "用户ID不能为空")
	private String id;
}
