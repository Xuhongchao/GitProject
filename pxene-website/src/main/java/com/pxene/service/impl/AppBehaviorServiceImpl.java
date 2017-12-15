package com.pxene.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pxene.dao.AppBehaviorMapper;
import com.pxene.dao.AppCategoryMapper;
import com.pxene.service.AppBehaviorService;
import com.pxene.utils.CodeNumUtils;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月19日
 */
@Service
public class AppBehaviorServiceImpl implements AppBehaviorService {
	@Autowired
	AppBehaviorMapper appBehaviorMapper;
	@Autowired
	AppCategoryMapper appCategoryMapper;

	@Override
	public Map<String, Object> queryAllBehavior() {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> allBehavior = appBehaviorMapper.queryAllBehavior();
		result.put("allBehavior", allBehavior);
		return result;
	}
	
	@Override
	public Map<String, Object> queryAppCrawlDetail(String categoryIdStr, String behaviorName) {
		Map<String, Object> result = new HashMap<String, Object>();
		Set<Map<String, Object>> behaviorList = new HashSet<>();
		// ,1,2,3,
		String[] categoryIdArr = StringUtils.split(categoryIdStr, ",");
		for (int i = 0; i < categoryIdArr.length; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("category_id", categoryIdArr[i]);
			map.put("behavior_name", behaviorName);
			List<Map<String, Object>> list = appBehaviorMapper.queryAppBehaviorByCategoryId(map);
			behaviorList.addAll(list);
		}
		result.put("behaviorList", behaviorList);
		return result;
	}

	@Override
	public Map<String, Object> queryByPageAndNum(int page, int num, String behaviorName, String categoryName) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("num", num);
		map.put("behavior_name", behaviorName);
		map.put("category_name", categoryName);
		result.put("totalcount", appBehaviorMapper.queryTotalCount(map));
		List<Map<String, Object>> list = appBehaviorMapper.queryByPageAndNum(map);
		for (Map<String, Object> behavior : list) {
			Map<String, Object> behaviorMap = new HashMap<>();
			behaviorMap.put("behavior_name", behavior.get("behavior_name"));
			int appBehaviorId = appBehaviorMapper.queryAppBehaviorId(behaviorMap);
			behavior.put("behavior_id", appBehaviorId);
			List<Map<String, Object>> behaviorCategoryList = appBehaviorMapper.queryBehaviorCategory(behaviorMap);
			List<Map<String, Object>> allAppCategory = appCategoryMapper.queryAppCategoryByPage(behaviorMap);
			if (StringUtils.equals(behaviorCategoryList.toString(), allAppCategory.toString())) {
				List<Map<String, Object>> categoryList=new ArrayList<>();
				Map<String, Object> categoryMap=new HashMap<>();
				categoryMap.put("category_name", "全部");
				categoryList.add(categoryMap);
				behavior.put("appCategoryList",categoryList);
			} else {
				behavior.put("appCategoryList", behaviorCategoryList);
			}
		}
		result.put("list", list);
		return result;
	}

	@Override
	public Map<String, Object> queryAppCategory() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("appCategoryList", appBehaviorMapper.queryAppCategory());
		return result;
	}
	
	@Override
	public Map<String, Object> addBehavior(String behaviorName, List<Integer> appCategoryIdList) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("behavior_name", behaviorName);
		List<Map<String, Object>> behaviorCategoryList = appBehaviorMapper.queryBehaviorCategory(map);
		List<Integer> appCategoryList = new ArrayList<>();
		boolean addResult = false;
		// 若行为不存在,新增行为
		if (behaviorCategoryList.size() == 0) {
			//添加App行为编码
			String oldNum = appBehaviorMapper.queryMaxBehaviorCodeDecimal();
			String newNum = "000001";
			if (oldNum != null) {
				int num = Integer.valueOf(oldNum) + 1;
				newNum = String.format("%06d", num);
			}
			String behaviorCode=CodeNumUtils._10_to_62(Long.parseLong(newNum), 3);
			map.put("behavior_code",behaviorCode);
			map.put("behavior_code_decimal",newNum);
			
			boolean appBehavior = appBehaviorMapper.addAppBehavior(map);

			if (appBehavior) {
				int appBehaviorId = appBehaviorMapper.queryAppBehaviorId(map);
				
				for (Integer appCategoryId : appCategoryIdList) {
					map.put("behavior_id", appBehaviorId);
					map.put("category_id", appCategoryId);
					addResult = appBehaviorMapper.addAppBehaviorCategory(map);
				}
			}

		} else {
			// 若行为已经添加,判断是否符合修改条件
			for (Integer appCategoryCheck : appCategoryIdList) {
				boolean check = true;
				for (Map<String, Object> behaviorCategoryMap : behaviorCategoryList) {
					// 判断行为对应的App分类是否已经存在
					if (StringUtils.equals(appCategoryCheck.toString(), behaviorCategoryMap.get("category_id").toString())) {
						check = false;
					}
					;
				}
				// 将不存在的添加到集合中
				if (check) {
					appCategoryList.add(appCategoryCheck);
				}
			}
			// 若appCategoryList为空,无法添加行为
			if (appCategoryList.size() == 0) {
				// 2表示行为对应的App分类已存在,无法添加
				result.put("resultCode", 2);
				return result;
			}
			// 若appCategoryList不为空,符合修改条件
			int appBehaviorId = appBehaviorMapper.queryAppBehaviorId(map);

			for (Integer appCategoryId : appCategoryList) {
				map.put("behavior_id", appBehaviorId);
				map.put("category_id", appCategoryId);
				addResult = appBehaviorMapper.addAppBehaviorCategory(map);
			}
		}
		if (addResult) {
			// 0表示添加成功
			result.put("resultCode", 0);
		} else {
			// 1表示添加失败
			result.put("resultCode", 1);
		}
		return result;
	}

	@Override
	public Map<String, Object> deleteBehavior(String behaviorId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		boolean deleteResult = false;
		map.put("behavior_id", behaviorId);
		int count = appBehaviorMapper.queryBehaviorIsUsed(map);
		if (count > 0) {
			// 2该行为被使用,无法删除
			result.put("resultCode", 2);
		} else {
			appBehaviorMapper.deleteBehaviorCategory(map);
			deleteResult = appBehaviorMapper.deleteBehavior(map);

			if (deleteResult) {
				// 0删除成功
				result.put("resultCode", 0);
			} else {
				// 1删除失败
				result.put("resultCode", 1);
			}
		}
		return result;
	}
}
