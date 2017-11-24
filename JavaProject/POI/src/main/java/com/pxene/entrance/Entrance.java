package com.pxene.entrance;

import java.util.List;
import java.util.Map;

import com.pxene.entity.Data;
import com.pxene.service.Core;
import com.pxene.service.LoadLine;
import com.pxene.service.LoadReg;
import com.pxene.service.LoadUrlExam;

/**
 * Created by @author xuhongchao on @date 2017年 11月 13日 下午7:42:00
 * 
 * 程序入口
 */

public class Entrance {
	private static final String START_PATH = "C:\\Users\\xuhongchao\\Desktop\\res\\";
	private static final String TEMP_PATH = "C:\\Users\\xuhongchao\\Desktop\\res2\\";

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		List<Data> list = new LoadReg().readFromExcelToData(); // 拿正则
		Map<String, String> map = new LoadUrlExam().getAllUrl(); // 拿url
		Map<String, Data> line = new LoadLine().readAll(); // 拿到一行
		Core core = new Core();
		core.filter(list, map, START_PATH, line); // 第一次过滤
		core.secondFilter(START_PATH, TEMP_PATH); // 第二次过滤
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		long end = System.currentTimeMillis();
		System.out.println("运行时间：" + (end - start) / 1000 + "s");
	}
}
