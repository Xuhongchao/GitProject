package cn.com.wisetrust.service;

import java.io.Serializable;
import cn.com.wisetrust.entity.User;
import lombok.Data;

/**
 * Created by @author xu on @date 2018年 2月 8日 下午2:20:03<br/>
 * 
 * 用户service，包含增、改、查
 */
public interface UserService {

	@Data
	class UserFilter implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String username;
		private String password;
	}

	public abstract int add(User user);

	public abstract int update(User user);

	public abstract User queryByID(String id);

	/**
	 * 做登陆使用
	 * 
	 * @param filter
	 *            维护用户名和密码
	 * @return User/null 如果能根据用户名和密码找到相应的用户;就返回该用户，如果没有返回null
	 */
	public abstract User login(UserFilter filter);

	/**
	 * 通过用户名判断用户是否已经存在
	 * 
	 * @param filter
	 *            维护用户名和密码
	 * @return
	 */
	public abstract User queryByName(String username);

}
