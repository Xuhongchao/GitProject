package com.wisetrust.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetrust.dao.AccountMapper;
import com.wisetrust.pojo.Account;
import com.wisetrust.pojo.AccountExample;
import com.wisetrust.service.IAccountService;

/**
 * Created by @author xu on @date 2018年 1月 15日 下午3:08:27
 * 
 * <br/>
 * 账户业务
 */
@Service()
public class AccountServiceImpl implements IAccountService {

	@Autowired
	private AccountMapper accountDao;

	@Override
	public Account getAccountByID(Long id) {
		return accountDao.selectByPrimaryKey(id);
	}

	@Override
	public List<Account> getAccountList() {
		AccountExample example = new AccountExample();
		example.setOrderByClause("gmt_create");
		List<Account> accounts = accountDao.selectByExample(example);
		return accounts;
	}

}
