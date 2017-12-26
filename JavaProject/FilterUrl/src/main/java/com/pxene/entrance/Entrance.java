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
 * <ul>
 * <li>本程序的入口</li>
 * <li>注意事项：
 * <ul>
 * <li>1，运行程序需要先在指定位置处建立res和res2两个文件夹</li>
 * <li>2，源文件地址为（如需修改请改IOUtil类中的SOURCE_PATH的值即可）：SOURCE_PATH =
 * "C:\\Users\\xuhongchao\\Desktop\\app_crawl_detail.xlsx";</li>
 * </ul>
 * </li>
 * </ul>
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
