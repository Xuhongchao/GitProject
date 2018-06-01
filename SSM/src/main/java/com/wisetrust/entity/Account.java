package com.wisetrust.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * Created by @author xu on @date 2018年 月 日 上午11:05:23
 */
@Data
public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6752323047796241335L;

	private Long id;
	private String loginName;
	private String cellphone;
	private String password;
	private String salt;
	private Byte type;
	private String pos;
	private Byte status;
	private Date lastLoginTime;
	private Date gmtCreate;
	private Date gmtModified;
	private String openId;

}
