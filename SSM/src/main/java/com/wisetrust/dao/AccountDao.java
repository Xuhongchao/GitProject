package com.wisetrust.dao;

import java.util.List;

import com.wisetrust.entity.Account;

/**
 * Created by @author xu on @date 2018年 月 日 上午11:29:22
 * 
 */
public interface AccountDao {

	/**
	 * 通过id查找账户
	 * 
	 * @param id
	 *            账户id
	 * @return 账户信息
	 */
	Account queryByID(Long id);

	/**
	 * 获取全部账户
	 * 
	 * @return list
	 */
	List<Account> queryAll();

	/**
	 * 添加账户
	 * 
	 * @param account
	 *            要添加的账户信息
	 */
	void add(Account account);

	/**
	 * 更新账户信息
	 * 
	 * @param account
	 */
	void update(Account account);

}
