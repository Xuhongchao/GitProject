package com.wisetrust.test;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wisetrust.dao.AccountDao;
import com.wisetrust.entity.Account;

/**
 * Created by @author xu on @date 2018年 月 日 下午2:13:25
 */
public class DaoTest extends BaseTest {

	@Autowired
	private AccountDao dao;

	@Test
	public void query() throws Exception {
		System.out.println("id为 1 的账户信息：" + dao.queryByID(1L));
	}

	@Test
	public void queryAll() throws Exception {
		List<Account> accounts = dao.queryAll();
		for (Account account : accounts) {
			System.out.println("id为 " + account.getId() + " 的账户信息：" + account);
		}
	}

	@Test
	public void update() throws Exception {
		Account account = dao.queryByID(13L);
		account.setCellphone("15092563579");
		dao.update(account);
	}

	@Test
	public void add() throws Exception {
		Account account = new Account();
		account.setLoginName("testPhone");
		account.setCellphone("15110232314");
		account.setPassword("111111");
		account.setType((byte) 1);
		account.setPos("渠道經理");
		account.setStatus((byte) 1);
		account.setGmtCreate(new Date());
		account.setGmtModified(new Date());
		dao.add(account);
	}

}
