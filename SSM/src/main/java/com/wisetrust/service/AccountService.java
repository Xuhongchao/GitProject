package com.wisetrust.service;

import java.util.List;

import com.wisetrust.entity.Account;

/**
 * Created by @author xu on @date 2018年 月 日 下午3:12:16
 */
public interface AccountService {

	/**
	 * 通过id查找账户
	 * 
	 * @param id
	 *            账户id
	 * @return 账户信息
	 */
	Account queryAccountByID(Long id);

	/**
	 * 获取全部账户
	 * 
	 * @return list
	 */
	List<Account> queryAccountAll();

	/**
	 * 添加账户
	 * 
	 * @param account
	 *            要添加的账户信息
	 */
	void addAccount(Account account);

	/**
	 * 更新账户信息
	 * 
	 * @param account
	 */
	void updateAccount(Account account);
}
