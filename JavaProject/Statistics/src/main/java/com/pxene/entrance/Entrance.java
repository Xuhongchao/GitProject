package com.pxene.entrance;

import java.util.Map;

import com.pxene.core.ProcessResultImpl;
import com.pxene.core.impl.ProcessResult;
import com.pxene.util.PropertiesUtil;

/**
 * Created by @author xuhongchao on @date 2017年 12月 05日 下午6:25:52
 */

public class Entrance {
	public static void main(String[] args) {
		ProcessResultImpl rpi = new ProcessResult();
		String[] strs = new PropertiesUtil().getPropertiesValue();
		for (int i = 0; i < strs.length; i++) {
			Map<String, Integer> map = rpi.getStatisticResult(i);
			System.out.println(map.toString());
		}
	}
}
