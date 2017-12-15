package com.pxene.dao;

import java.util.List;
import java.util.Map;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月19日
 */
public interface AppBehaviorMapper {

	List<Map<String, Object>> queryByPageAndNum(Map<String, Object> map);

	Integer queryTotalCount(Map<String, Object> map);

	List<Map<String, Object>> queryBehaviorCategory(Map<String, Object> behaviorMap);

	List<Map<String, Object>> queryAppCategory();

	boolean addAppBehavior(Map<String, Object> map);

	Integer queryAppBehaviorId(Map<String, Object> map);

	boolean addAppBehaviorCategory(Map<String, Object> map);

	Integer queryBehaviorIsUsed(Map<String, Object> map);

	boolean deleteBehaviorCategory(Map<String, Object> map);

	boolean deleteBehavior(Map<String, Object> map);

	String queryAppBehaviorName(Map<String, Object> industryIdMap);

	List<Map<String, Object>> queryAppBehaviorByCategoryId(Map<String, Object> query);

	Integer queryAppBehaviorTotalCount(Map<String, Object> map);

	List<Map<String, Object>> queryAppBehaviorByPage(Map<String, Object> map);

	List<Map<String, Object>> queryAllBehavior();

	String queryMaxBehaviorCodeDecimal();

	String queryBehaviorCodeById(Map<String, Object> query);

	String queryMaxBehaviorNum(Map<String, Object> query);
}
