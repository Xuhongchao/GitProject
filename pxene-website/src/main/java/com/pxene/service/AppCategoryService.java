package com.pxene.service;

import java.util.Map;

import com.pxene.model.AppCategory;

/**
 * Created by chongaizhen on 2016/12/8.
 */
public interface AppCategoryService {

	Map<String, Object> queryPageAndNum(int page, int num, String categoryName);

	Map<String, Object> addCategory(String parentCategory, String childCategory);

	Map<String, Object> queryFuzzyParentCategory(String parentCategory);

	Map<String, Object> modifyChildCategory(String childCategoryId, String childCategory);

	Map<String, Object> delChildCategory(String childCategoryId);

	Map<String, Object> queryChildCategoryIsUsed(String childCategoryId);
}
