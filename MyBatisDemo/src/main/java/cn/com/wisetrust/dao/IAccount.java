package cn.com.wisetrust.dao;

import java.util.List;

import cn.com.wisetrust.entity.Account;

/**
 * Created by @author xu on @date 2017年 12月 22日 上午9:52:29
 */
public interface IAccount {

	// 使用接口的方式
	// @Select("select * from account where id = #{id}")
	// public abstract Account getAccountByID(int id);

	/*
	 * 接口和xml文件一起使用（包括增删改查）
	 */
	/*public abstract List<Account> getAccountList();

	public abstract void addAcount(Account account);

	public abstract void updateAccount(Account account);

	public abstract void deleteAccount(int id);*/

	public abstract Account getAccountByID(int id);

}
