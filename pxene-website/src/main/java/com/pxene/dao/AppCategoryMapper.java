package com.pxene.dao;

import java.util.List;
import java.util.Map;

import com.pxene.model.AppCategory;

public interface AppCategoryMapper {

	List<Map<String, Object>> queryByPageAndNum(Map<String,Object> map);
	Integer queryCategoryTotalCount(Map<String, Object> map);
	Integer queryAppCount(Map<String, Object> appCountMap);
	List<Map<String, Object>> queryKeywordList(Map<String, Object> appCountMap);
	List<Map<String, Object>> queryAppCategoryByPage(Map<String, Object> map);
	Integer queryAppCategoryTotalCount(Map<String, Object> map);
	Integer queryChildCategoryExist(Map<String, Object> map);
	List<Map<String, Object>> queryParentCategory(Map<String, Object> map);
	boolean addParentCategory(Map<String, Object> map);
	boolean addChildCategory(Map<String, Object> map);
	List<Map<String, Object>> queryFuzzyParentCategory(Map<String, Object> map);
	boolean modifyChildCategory(Map<String, Object> map);
	Integer queryChildCategoryIsUsed(Map<String, Object> map);
	boolean delChildCategory(Map<String, Object> map);
	List<Map<String, Object>> queryCategoryByChildCategoryId(Map<String, Object> map);
	void delParentCategory(Map<String, Object> idMap);
}
