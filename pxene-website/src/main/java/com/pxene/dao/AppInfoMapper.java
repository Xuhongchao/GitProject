package com.pxene.dao;

import java.util.List;
import java.util.Map;

import com.pxene.model.AppInfo;

public interface AppInfoMapper {
	
	int queryAll(Map<String,Object> map);
	String queryMaxAppInfoNum();
	int queryNum(Map<String,Object> map);
	boolean addAppInfo(Map<String,Object> map);
	boolean updateAppInfo(AppInfo app_info);
	boolean delAppInfo(Map<String,Object> map);
	List<Map<String, Object>> queryByPageAndNum(Map<String,Object> map);
	boolean updateExport(AppInfo appInfo);
	int queryByName(Map<String, Object> map);
	List<AppInfo> exportApp();
	boolean updateOsType(Map<String, Object> map);
	AppInfo queryMesByName(Map<String, Object> map);
	Map<String, Object> queryByAppName(Map<String, Object> map);
	boolean updateAppOs(Map<String, Object> appInfo);
	int queryByNum(Map appInfoNumMap);
	boolean updateName(Map appInfoMap);
	boolean updateOsAndName(Map updateOsAndMap);
}
