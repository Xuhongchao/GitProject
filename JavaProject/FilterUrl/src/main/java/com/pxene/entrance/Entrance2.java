package com.pxene.entrance;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pxene.entity.Data;
import com.pxene.service.LoadNotEnterUrl;
import com.pxene.service.LoadReg;
import com.pxene.service.PreInputCheckCore;
import com.pxene.util.IOUtil;

/**
 * Created by @author xu on @date 2017年 12月 24日 下午2:14:30
 * 
 * <ul>
 * <li>录入前判断url的程序入口</li>
 * <li>注意事项：
 * <ul>
 * <li>1，首先需要在app_crawl_detail.xlsx文件中创建第二个（必须是第二个）sheet，并将要录入的数据放到这个sheet中</li>
 * <li>2，运行程序需要先在指定位置处建立same.txt和diff.txt两个文件</li>
 * <li>3，源文件地址为（如需修改请改IOUtil类中的SOURCE_PATH的值即可）：SOURCE_PATH =
 * "C:\\Users\\xuhongchao\\Desktop\\app_crawl_detail.xlsx";</li>
 * </ul>
 * </li>
 * </ul>
 */
public class Entrance2 {
	private static final String SAME_PATH = "C:\\Users\\xu\\Desktop\\same.txt";
	private static final String DIFF_PATH = "C:\\Users\\xu\\Desktop\\diff.txt";

	public static void main(String[] args) {
		IOUtil ioUtil = IOUtil.getInstance();
		
		List<Data> list = new LoadReg().readFromExcelToData(); // 拿正则

		List<String> noEnterUrl = new LoadNotEnterUrl().getNotEnterUrl(); // 待录入的url

		Map<List<String>, List<String>> map = new PreInputCheckCore().check(list, noEnterUrl); // 在录入前进行检查
		
		for(Entry<List<String>, List<String>> entry : map.entrySet()){
			List<String> same = entry.getKey();
			System.out.println(same.size());
			ioUtil.writeToFile(new File(SAME_PATH), same);
			
			List<String> diff = entry.getValue();
			System.out.println(diff.size());
			ioUtil.writeToFile(new File(DIFF_PATH), diff);
		}
	}

}
