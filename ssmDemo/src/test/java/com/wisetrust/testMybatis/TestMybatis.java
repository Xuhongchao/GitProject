package com.wisetrust.testMybatis;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.wisetrust.pojo.Account;
import com.wisetrust.service.IAccountService;

/**
 * Created by @author xu on @date 2018年 1月 15日 下午3:45:35
 * 
 * 測試spring和mybatis的整合
 */
@RunWith(SpringJUnit4ClassRunner.class) // 表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestMybatis {
	private static Logger logger = Logger.getLogger(TestMybatis.class);

	@Resource
	private IAccountService accountService;

	@Test
	public void test() throws Exception {
		Account account = accountService.getAccountByID(1L);
		logger.info(JSON.toJSONString(account));
	}
}