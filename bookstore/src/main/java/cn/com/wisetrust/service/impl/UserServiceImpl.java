package cn.com.wisetrust.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.wisetrust.dao.UserMapper;
import cn.com.wisetrust.entity.User;
import cn.com.wisetrust.entity.UserExample;
import cn.com.wisetrust.entity.UserExample.Criteria;
import cn.com.wisetrust.service.UserService;

/**
 * Created by @author xu on @date 2018年 月 日 下午2:57:54
 */
@Service("UserService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public int add(User user) {
		return userMapper.insertSelective(user);
	}

	@Override
	public int update(User user) {
		return userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public User queryByID(String id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public User queryByName(String username) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);

		List<User> users = userMapper.selectByExample(example);
		if (users != null && !users.isEmpty()) {
			return users.get(0);
		}
		return null;
	}

	@Override
	public User login(UserFilter filter) {
		UserExample example = new UserExample();
		convert(filter, example);
		List<User> users = userMapper.selectByExample(example);
		if (users != null && !users.isEmpty()) {
			return users.get(0);
		}
		return null;
	}

	/**
	 * 在条件不为空时为查询语句加条件
	 * 
	 * @param filter
	 * @param example
	 */
	private void convert(UserFilter filter, UserExample example) {
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(filter.getUsername()) && StringUtils.isNotEmpty(filter.getPassword())) {
			criteria.andUsernameEqualTo(filter.getUsername());
			criteria.andPasswordEqualTo(filter.getPassword());
		}
	}
}
