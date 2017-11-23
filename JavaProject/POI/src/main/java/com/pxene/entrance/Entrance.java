package com.pxene.entrance;

import java.util.List;
import java.util.Map;

import com.pxene.entity.Data;
import com.pxene.service.Core;
import com.pxene.service.LoadLine;
import com.pxene.service.LoadUrlExam;

/**
 * Created by @author xuhongchao on @date 2017年 11月 13日 下午7:42:00
 */

public class Entrance {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		List<Data> list = new LoadLine().readFromExcelToData(); // 拿正则
		Map<String, String> map = new LoadUrlExam().getAllUrl(); // 拿url
		Core core = new Core();
		core.filter(list, map); // 核心方法
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		long end = System.currentTimeMillis();
		System.out.println("运行时间：" + (end - start) / 1000 + "s");
	}
}
