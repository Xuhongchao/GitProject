package com.pxene.dao;

import java.util.List;
import java.util.Map;

public interface AppStatisticsUsersMapper {

	List<Map<String, Object>> queryUsersOneProvince(Map<String, Object> map);

	List<Map<String, Object>> queryUsersOneInfo(Map<String, Object> query);

	Integer queryUsersOneTotalCount(Map<String, Object> map);

	String queryProvinceName(Map<String, Object> provinceMap);

	List<Map<String, Object>> queryUsersTwo(Map<String, Object> map);

	Integer queryUsersTwoTotalCount(Map<String, Object> map);

	List<Map<String, Object>> queryUsersThree(Map<String, Object> map);

	Integer queryUsersThreeTotalCount(Map<String, Object> map);

	List<Map<String, Object>> queryUsersThreeInfo(Map<String, Object> query);

	List<Map<String, Object>> queryUsersFour(Map<String, Object> map);

	Integer queryUsersFourTotalCount(Map<String, Object> map);

	List<Map<String, Object>> queryUsersFive(Map<String, Object> map);

	Integer queryUsersFiveTotalCount(Map<String, Object> map);

	List<Map<String, Object>> queryUsersFiveInfo(Map<String, Object> query);

	List<Map<String, Object>> queryUsersSix(Map<String, Object> map);

	Integer queryUsersSixTotalCount(Map<String, Object> map);

	List<Map<String, Object>> queryUsersSixInfo(Map<String, Object> query);

	List<Map<String, Object>> queryWechatStatistics(Map<String, Object> map);

	Integer queryWechatStatisticsTotalCount(Map<String, Object> map);

}
