package com.pxene.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月19日
 */
public interface AppBehaviorService {

	Map<String, Object> queryByPageAndNum(int page, int num, String behaviorName, String categoryName);

	Map<String, Object> queryAppCategory();

	Map<String, Object> addBehavior(String behaviorName, List<Integer> appCategoryIdList);

	Map<String, Object> deleteBehavior(String behaviorId);
	//根据App对应的类型集合查询相关的行为
	Map<String, Object> queryAppCrawlDetail(String categoryIdStr, String behaviorName);

	Map<String, Object> queryAllBehavior();


}
