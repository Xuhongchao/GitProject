package cn.com.wisetrust.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Created by @author xu on @date 2018年 2月 8日 下午5:28:47<br/>
 * 
 * 用来接收创建用户请求数据
 */
@Data
public class UserCreateRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "昵称不能为空")
	private String username;
	@NotNull(message = "密码不能为空")
	private String password;
	@NotNull(message = "电话不能为空")
	private String cellphone;
	@NotNull(message = "地址不能为空")
	private String address;
	
	private String phone;
	private String email;
}
