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
	private String post;
	private Byte status;
	private String lastLoginTime;
	private String gmtModified;

}
