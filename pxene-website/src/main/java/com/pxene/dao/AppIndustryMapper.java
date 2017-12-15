package com.pxene.dao;

import com.pxene.model.AppIndustry;

import java.util.List;
import java.util.Map;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月19日
 */
public interface AppIndustryMapper {

	int queryNum(Map<String, Object> map);

	boolean add(Map<String, Object> map);

	int queryCrawlNum(Map<String, Object> map);

	List<Map<String, Object>> queryByPageAndNum(Map<String, Object> map);
	// m
	Integer queryIndustryAndContentExist(Map<String, Object> map);

	Integer queryIndustryID(Map<String, Object> map);

	boolean addIndustry(Map<String, Object> map);

	boolean addIndustryContent(Map<String, Object> map);

	Integer queryTotalCount(Map<String, Object> map);

	Integer queryContentDetailCount(Map<String, Object> map);

	boolean deleteIndustryContent(Map<String, Object> map);

	Integer queryContentCount(Map<String, Object> map);

	boolean deleteIndustry(Map<String, Object> map);

	List<Map<String, Object>> queryContentDetail(Map<String, Object> map);

	Integer queryContentDetailTotalCount(Map<String, Object> map);

	boolean addRegular(Map<String, Object> map);

	Integer queryParamReg(Map<String, Object> map);

	boolean modifyRegular(Map<String, Object> map);

	Integer queryRegularIsUsed(Map<String, Object> map);

	boolean deleteRegular(Map<String, Object> map);

	String queryIndustryName(Map<String, Object> industryIdMap);

	List<Map<String, Object>> queryAllIndustry();

	List<Map<String, Object>> queryAllIndustryWithAdID(Map<String, Object> adId);
	
	List<Map<String, Object>> queryIndustryContent(Map<String, Object> map);

	List<Map<String, Object>> queryIndustryContentDetail(Map<String, Object> query);

	String queryIndustryNumById(Map<String, Object> query);

	Integer queryIndustryTotalCount(Map<String, Object> map);

	List<Map<String, Object>> queryIndustryByPage(Map<String, Object> map);

	boolean checkReg(Map<String, Object> map);

	Integer queryIndusrtyAndOrder(Map<String, Object> map);

	String queryMaxIndustryNumDecimal();

	Integer queryIndustryCountById(Map<String, Object> query);


}
