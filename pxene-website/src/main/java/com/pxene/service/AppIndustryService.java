package com.pxene.service;

import com.pxene.model.AppIndustry;

import java.util.List;
import java.util.Map;

/**
 * Created by chongaizhen on 2016/12/8.
 */
public interface AppIndustryService {

	Map<String, Object> add(String industryName, String contentName, int contentOrder);
	Map<String, Object> deleteIndustryContent(int id, String industryNum);
	Map<String, Object> queryContentDetail(Integer contentId,Integer page, Integer num);
	Map<String, Object> addRegular(int contentId, String urlExample, String paramReg);
	Map<String, Object> modifyRegular(int contentId, int contentDetailId, String urlExample, String paramReg);
	Map<String, Object> deleteRegular(int contentDetailId);
	Map<String, Object> queryAppIndustryInfo(Integer crawlDetailId);
	Map<String, Object> queryAllIndustry();
	void checkReg(String regList);
	Map<String, Object> queryByPageAndNum(int page, int num, String industryNum, String industryName, String orderName,
			String orderRule);
	Map<String, Object> queryModifyAppIndustryInfo(Integer crawlDetailId);
}
