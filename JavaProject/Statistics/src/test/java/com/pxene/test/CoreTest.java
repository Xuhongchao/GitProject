package com.pxene.test;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created by @author xuhongchao on @date 2017�� �� �� ����5:38:47
 */

public class CoreTest {
	@Test
	public void test1() throws Exception {
		String[] strs = { "��1.0.0��", "��1.1.0��", "��1.2.0��", "��4.0.0��",
				"��4.0.5��", "��4.1.0��", "��4.1.3��", "��4.2.0��", "��4.2.2��",
				"��5.0.0��", "��5.1.0��", "��5.1.1��", "��5.2.0��", "��5.3.0��",
				"��5.4.0��" };

		for (String str : strs) {
			System.out.println("����" + str);
		}
	}
}
