package com.pxene.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pxene.dao.AppCategoryMapper;
import com.pxene.service.AppCategoryService;

/**
 * Created by chongaizhen on 2016/12/8.
 */

@Service("appCategoryService")
public class AppCategoryServiceImpl implements AppCategoryService{

	@Autowired
	AppCategoryMapper appCategoryMapper;
	
	@Override
	public Map<String, Object> queryPageAndNum(int page, int num, String categoryName) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page",page);
		map.put("num",num);
		map.put("category_name",categoryName);
		result.put("totalcount",appCategoryMapper.queryCategoryTotalCount(map));
		List<Map<String, Object>> list = appCategoryMapper.queryByPageAndNum(map);
		result.put("list", list);
		return result;
	}

	@Override
	public Map<String, Object> addCategory(String parentCategory, String childCategory) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> map = new HashMap<>();

		map.put("parent_category", parentCategory);
		map.put("child_category", childCategory);
		Integer count=appCategoryMapper.queryChildCategoryExist(map);
		if(count>0){
			//该子级分类已存在,无法添加
			result.put("resultCode", 2);
			return result;
		}
		List<Map<String, Object>> parentCategoryList=appCategoryMapper.queryParentCategory(map);
		if(parentCategoryList.size()==0){
			appCategoryMapper.addParentCategory(map);
			parentCategoryList=appCategoryMapper.queryParentCategory(map);
		}
		map.put("category_pid", parentCategoryList.get(0).get("category_id"));
		try {
			appCategoryMapper.addChildCategory(map);
			//添加成功
			result.put("resultCode", 0);
		} catch (Exception e) {
			e.printStackTrace();
			//添加失败
			result.put("resultCode", 1);
		}
		return result;
	}

	@Override
	public Map<String, Object> queryFuzzyParentCategory(String parentCategory) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		map.put("parent_category", parentCategory);
		List<Map<String, Object>> parentCategoryList=appCategoryMapper.queryFuzzyParentCategory(map);
		result.put("parentCategoryList", parentCategoryList);
		return result;
	}
	
	//根据产品需求添加App分类是否关联App的查询
	@Override
	public Map<String, Object> queryChildCategoryIsUsed(String childCategoryId) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		map.put("child_category_id", childCategoryId);
		Integer isUsed=appCategoryMapper.queryChildCategoryIsUsed(map);
		if(isUsed>0){
			//3表示该App分类下已关联App
			result.put("resultCode", 3);
		}else{
			//4表示该App分类下没有关联App
			result.put("resultCode", 4);
		}
		return result;
	}
	
	@Override
	public Map<String, Object> modifyChildCategory(String childCategoryId, String childCategory) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		map.put("child_category_id", childCategoryId);
		map.put("child_category", childCategory);
		Integer count=appCategoryMapper.queryChildCategoryExist(map);
		if(count>0){
			//该子级分类已存在,无法修改
			result.put("resultCode", 2);
			return result;
		}
		try {
			appCategoryMapper.modifyChildCategory(map);
			result.put("resultCode", 0);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("resultCode", 1);
		}
		return result;
	}

	@Override
	public Map<String, Object> delChildCategory(String childCategoryId) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		map.put("child_category_id", childCategoryId);
		Integer count=appCategoryMapper.queryChildCategoryIsUsed(map);
		if(count>0){
			//该App分类已经关联App,无法删除
			result.put("resultCode", 2);
			return result;
		}
		try {
			List<Map<String,Object>> list=appCategoryMapper.queryCategoryByChildCategoryId(map);
			appCategoryMapper.delChildCategory(map);
			if(list.size()==1){
				Map<String,Object> idMap=new HashMap<>();
				idMap.put("parent_category_id", list.get(0).get("parent_category_id"));
				appCategoryMapper.delParentCategory(idMap);
			}
			result.put("resultCode", 0);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("resultCode", 1);
		}
		return result;
	}
}
