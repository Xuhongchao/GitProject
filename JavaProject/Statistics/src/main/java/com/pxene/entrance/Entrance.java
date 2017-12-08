package com.pxene.entrance;

import java.util.Map;

import com.pxene.service.ProcessResult;
import com.pxene.service.impl.ProcessResultImpl;
import com.pxene.util.PropertiesUtil;

/**
 * Created by @author xuhongchao on @date 2017年 12月 05日 下午6:25:52
 * 
 * <ul>
 * 注意：
 * <li>1，excel文件的命名：在ProcessResultImpl类中的 String READ_PATH
 * ="C:\\Users\\xuhongchao\\Desktop\\日报汇总.xlsx";</li>
 * <li>2，excel中sheet的命名规范：分为三个sheet,王琪、杨松奥、胥辛雨</li>
 * <li>3，如果以后换人的话，只需要修改properties中的value的值就可以，然后将value和sheet的名称相对应</li>
 * <li>4，必须保证将app的版本号加上，不能存在重复的第一列（即appName那一列不能重复）</li>
 * </ul>
 * 
 */

public class Entrance {
	public static void main(String[] args) {
		System.out.println("分割线从上往下分别代表的是：王琪，杨松奥，胥辛雨" + "\n"
				+ "-------------------------");
		ProcessResult rpi = new ProcessResultImpl();
		String[] strs = new PropertiesUtil().getPropertiesValue();
		for (int i = 0; i < strs.length; i++) {
			String appName = null;
			int urlNum = 0;
			Map<String, Integer> map = rpi.getStatisticResult(i);
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				appName = entry.getKey();
				urlNum = entry.getValue();
				System.out.println(appName + " - " + urlNum);
			}
			map.clear();
			System.out.println("----------------------------");
		}
	}
}
