package com.pxene.odata;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.pxene.service.LoadUrlExam;

/**
 * Created by @author xuhongchao on @date 2017�� �� �� ����2:00:53
 */

public class CoreTest {
	@Test
	public void testName() throws Exception {
		String[] strs = { "auto.com?s", "auto.com?s", "auto.com?s" };

		String compile = ".*auto.com.*";
		Pattern p = Pattern.compile(compile);
		for (String str : strs) {
			Matcher m = p.matcher(str);
			boolean b = m.matches();
			System.out.println(b);
		}
	}

	@Test
	public void test() throws Exception {
		String paramReg = "NULL";
		String[] params = paramReg.split("\t");
		if (params.length == 1 && "NULL".equals(params[0])) {
			System.out.println("1");
		}
	}

	@Test
	public void test2() throws Exception {
		HashMap<String, String> container = new HashMap<String, String>();
		container.put("1", "1");
		container.put("2", "2");
		container.put("3", "3");
		System.out.println(container.size());
		container.clear();
		System.out.println(container.size());
	}

	@Test
	public void test3() throws Exception {
		String string = "https://btrace.qq.com/kvcollect?BossI";
		System.out.println(string.startsWith("https"));

	}

	@Test
	public void test4() throws Exception {
		HashMap<String, String> container = new HashMap<String, String>();
		Map<String, String> map = new LoadUrlExam().getAllUrl();
		System.out.println(map.size());

		for (int i = 0; i < 3; i++) {
			System.out.println("1");
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println("2");
			}
		}
	}

}
