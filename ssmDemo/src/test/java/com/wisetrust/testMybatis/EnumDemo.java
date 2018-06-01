package com.wisetrust.testMybatis;

import org.junit.Test;

/**
 * Created by @author xu on @date 2018年 1月 17日 下午3:57:16
 * 
 * 枚举用法
 */
public class EnumDemo {

	// 使用关键字enum来创建枚举类型，enum继承了java.lang.Enum类，所以隐含了所创建的类型都是 java.lang.Enum
	// 类的子类，
	// 因为java单继承机制，所以不能再继承别的类
	enum AccountType {

		channel((byte) 1, "渠道经理"), market((byte) 2, "市场经理"), department((byte) 3, "部门总监"), company((byte) 4, "公司高管");

		/*
		 * 给enum自定义属性和方法
		 */
		// 定义变量
		public byte code;
		public String des;

		// 构造方法，枚举类型只能为私有
		AccountType(byte code, String des) {
			this.code = code;
			this.des = des;
		}

		public static AccountType geAccountType(byte code) {
			/*
			 * 重点说一下，枚举类型的values()方法。看到别人使用这个方法，就研究了下， 结果在Oracle的在线文档中找到了解释， The
			 * enum declaration defines a class (called an enum type). The enum
			 * class body can include methods and other fields. The compiler
			 * automatically adds some special methods when it creates an enum.
			 * 原来是编译器自动生成的这个方法。
			 * 
			 */
			AccountType[] accountTypes = AccountType.values();
			for (AccountType accountType : accountTypes) {
				// System.out.println(accountType.code); // 输出1，2，3，4
				if (accountType.code == code) {
					return accountType;
				}
			}
			return null;
		}
	}

	@Test
	public void test1() throws Exception {
		System.out.println(AccountType.geAccountType((byte) 1).name()); // 输出channel
		System.out.println(AccountType.geAccountType((byte) 1).des); // 输出 渠道经理
	}

}
