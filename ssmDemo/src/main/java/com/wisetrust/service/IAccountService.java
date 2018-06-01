package com.wisetrust.service;

import java.util.List;

import com.wisetrust.pojo.Account;

/**
 * Created by @author xu on @date 2018年 1月 15日 下午3:05:11
 * 
 * 账户业务类
 */
public interface IAccountService {

	Account getAccountByID(Long id);

	List<Account> getAccountList();

}
