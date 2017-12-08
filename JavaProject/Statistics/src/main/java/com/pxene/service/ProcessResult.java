package com.pxene.service;

import java.util.Map;

/**
 * Created by @author xuhongchao on @date 2017年 12月 6日 下午5:53:49
 */

public interface ProcessResult {
	public abstract Map<String, Integer> getStatisticResult(int sheetNum);
}
