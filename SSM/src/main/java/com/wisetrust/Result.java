package com.wisetrust;

import lombok.Data;

/**
 * Created by @author xu on @date 2018年 月 日 上午10:05:12
 */
@Data
public class Result<T> {
	private boolean success; // 是否成功标志
	private T data; // 成功时返回的数据
	private String error; // 错误信息

	public Result() {
	}

	// 成功时的构造器
	public Result(boolean success, T data) {
		this.success = success;
		this.data = data;
	}

	// 错误时的构造器
	public Result(boolean success, String error) {
		this.success = success;
		this.error = error;
	}

}
