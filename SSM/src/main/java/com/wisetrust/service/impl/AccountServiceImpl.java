package com.wisetrust.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetrust.dao.AccountDao;
import com.wisetrust.entity.Account;
import com.wisetrust.service.AccountService;

/**
 * Created by @author xu on @date 2018年 月 日 下午3:13:39
 */

@Service(value = "accountService")
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountDao accountDao;

	@Override
	public Account queryAccountByID(Long id) {
		return accountDao.queryByID(id);
	}

	@Override
	public List<Account> queryAccountAll() {
		return accountDao.queryAll();
	}

	@Override
	public void addAccount(Account account) {
		accountDao.add(account);
	}

	@Override
	public void updateAccount(Account account) {
		accountDao.update(account);
	}

}