package com.pxene.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.pxene.dao.AppCrawlDetailMapper;
import com.pxene.dao.AppIndustryMapper;
import com.pxene.model.AppCrawlDetail;
import com.pxene.service.AppIndustryService;
import com.pxene.utils.CodeNumUtils;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月18日
 */
@Service("appIndustryService")
public class AppIndustryServiceImpl implements AppIndustryService {

	@Autowired
	AppIndustryMapper appIndustryMapper;
	@Autowired
	AppCrawlDetailMapper appCrawlDetailMapper;

	@Override
	public void checkReg(String regList) {
		JSONArray jsonArray = JSONArray.parseArray(regList);
		List<Map<String, Object>> list = (List<Map<String, Object>>) JSONArray.toJavaObject(jsonArray, Object.class);
		for (Map<String, Object> regMap : list) {
			Integer content_detail_id = (Integer) regMap.get("content_detail_id");
			Integer check_state = (Integer) regMap.get("check_state");
			Map<String, Object> map = new HashMap<>();
			map.put("content_detail_id", content_detail_id);
			map.put("check_state", check_state);
			appIndustryMapper.checkReg(map);
		}
	}

	@Override
	public Map<String, Object> queryAllIndustry() {
		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> allIndustry = appIndustryMapper.queryAllIndustry();
		result.put("allIndustry", allIndustry);
		return result;
	}

	@Override
	public Map<String, Object> add(String industryName, String contentName, int contentOrder) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> map = new HashMap<>();

		map.put("industry_name", industryName);
		map.put("content_name", contentName);
		map.put("content_order", contentOrder);
		// 判断参数分类和顺序
		Integer exist = appIndustryMapper.queryIndusrtyAndOrder(map);
		if (exist > 0) {
			// 该参数顺序已存在，添加参数失败
			result.put("resultCode", 3);
			return result;
		}

		int count = appIndustryMapper.queryIndustryAndContentExist(map);
		if (count > 0) {
			// 2表示该分类和分类类型已存在,添加分类失败
			result.put("resultCode", 2);
		} else {
			boolean contentResult = false;
			// 判断参数分类是否已经存在
			Integer industryID = appIndustryMapper.queryIndustryID(map);
			if (industryID == null) {
				
				//添加参数分类编码
				String oldNum = appIndustryMapper.queryMaxIndustryNumDecimal();
				String newNum = "000001";
				if (oldNum != null) {
					int num = Integer.valueOf(oldNum) + 1;
					newNum = String.format("%06d", num);
				}
				String industryNum=CodeNumUtils._10_to_62(Long.parseLong(newNum), 3);
				map.put("industry_num",industryNum);
				map.put("industry_num_decimal",newNum);
				
				
				boolean industryResult = appIndustryMapper.addIndustry(map);
				if (industryResult) {
					industryID = appIndustryMapper.queryIndustryID(map);
					map.put("content_industry_id", industryID);
					contentResult = appIndustryMapper.addIndustryContent(map);
				}
			} else {
				map.put("content_industry_id", industryID);
				contentResult = appIndustryMapper.addIndustryContent(map);
			}

			if (contentResult) {
				// 0表示添加分类成功
				result.put("resultCode", 0);
			} else {
				// 1表示添加分类失败
				result.put("resultCode", 1);
			}
		}

		return result;
	}

	@Override
	public Map<String, Object> queryByPageAndNum(int page, int num, String industryNum, String industryName,
			String orderName, String orderRule) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("num", num);
		map.put("industry_num", industryNum);
		map.put("industry_name", industryName);
		map.put("orderRule", orderRule);
		result.put("totalcount", appIndustryMapper.queryTotalCount(map));
		List<Map<String, Object>> list = appIndustryMapper.queryByPageAndNum(map);
		result.put("list", list);
		return result;
	}

	@Override
	public Map<String, Object> deleteIndustryContent(int contentId, String industryNum) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content_id", contentId);
		int count = appIndustryMapper.queryContentDetailCount(map);
		if (count > 0) {
			// 2参数详情中存在内容,无法删除
			result.put("resultCode", 2);
		} else {
			boolean deleteResult = appIndustryMapper.deleteIndustryContent(map);
			map.put("industry_num", industryNum);
			int contentCount = appIndustryMapper.queryContentCount(map);
			if (contentCount == 0) {
				appIndustryMapper.deleteIndustry(map);
			}

			if (deleteResult) {
				// 0表示删除成功
				result.put("resultCode", 0);
			} else {
				// 1表示删除失败
				result.put("resultCode", 1);
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> queryContentDetail(Integer contentId, Integer page, Integer num) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content_id", contentId);
		map.put("page", page);
		map.put("num", num);
		result.put("totalcount", appIndustryMapper.queryContentDetailTotalCount(map));
		List<Map<String, Object>> list = appIndustryMapper.queryContentDetail(map);
		result.put("list", list);
		return result;
	}

	@Override
	public Map<String, Object> addRegular(int contentId, String urlExample, String paramReg) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content_id", contentId);
		urlExample = StringUtils.replace(urlExample, "\\", "\\\\");
		urlExample = StringUtils.replace(urlExample, "\"", "\\\"");
		map.put("url_example", urlExample);
		paramReg = StringUtils.replace(paramReg, "\\", "\\\\");
		paramReg = StringUtils.replace(paramReg, "\"", "\\\"");
		map.put("param_reg", paramReg);

		int paramRegCount = appIndustryMapper.queryParamReg(map);
		if (paramRegCount > 0) {
			// 2表示 参数正则已存在,不能重复添加
			result.put("resultCode", 2);
		} else {
			boolean addResult = appIndustryMapper.addRegular(map);
			if (addResult) {
				// 0表示添加成功
				result.put("resultCode", 0);
			} else {
				// 1表示添加失败
				result.put("resultCode", 1);
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> modifyRegular(int contentId, int contentDetailId, String urlExample, String paramReg) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content_id", contentId);
		map.put("content_detail_id", contentDetailId);
		urlExample = StringUtils.replace(urlExample, "\\", "\\\\");
		map.put("url_example", urlExample);
		paramReg = StringUtils.replace(paramReg, "\\", "\\\\");
		map.put("param_reg", paramReg);
		int paramRegCount = appIndustryMapper.queryParamReg(map);
		if (paramRegCount > 0) {
			// 2表示 该参数正则已存在,修改失败
			result.put("resultCode", 2);
		} else {
			boolean modifyResult = appIndustryMapper.modifyRegular(map);
			if (modifyResult) {
				// 0表示修改成功
				result.put("resultCode", 0);
			} else {
				// 1表示修改失败
				result.put("resultCode", 1);
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> deleteRegular(int contentDetailId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content_detail_id", contentDetailId);
		int count = appIndustryMapper.queryRegularIsUsed(map);
		if (count > 0) {
			// 该参数正则已经被使用,无法删除!
			result.put("resultCode", 2);
		} else {
			boolean deleteResult = appIndustryMapper.deleteRegular(map);
			if (deleteResult) {
				// 0表示删除成功
				result.put("resultCode", 0);
			} else {
				// 1表示删除失败
				result.put("resultCode", 1);
			}
		}
		return result;
	}

	/**
	 * 添加抓取信息时,查询参数信息
	 * 
	 * @return
	 */
	@Override
	public Map<String, Object> queryAppIndustryInfo(Integer crawlDetailId) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> allIndustry = appIndustryMapper.queryAllIndustry();
		for (Map<String, Object> industryMap : allIndustry) {
			Map<String, Object> map = new HashMap<String, Object>();
			int industryId = (int) industryMap.get("industry_id");
			map.put("industry_id", industryId);
			List<Map<String, Object>> industryContentList = appIndustryMapper.queryIndustryContent(map);
			List<Map<String, Object>> newIndustryContentList = new ArrayList<>(industryContentList);
			// 按照设定好的参数顺序排序
			for (Map<String, Object> industryContent : industryContentList) {
				newIndustryContentList.set((int) industryContent.get("content_order") - 1, industryContent);
			}
			for (Map<String, Object> IndustryContent : newIndustryContentList) {
				Map<String, Object> query = new HashMap<String, Object>();
				query.put("content_id", (int) IndustryContent.get("content_id"));
				List<Map<String, Object>> contentDetailList = appIndustryMapper.queryIndustryContentDetail(query);
				IndustryContent.put("contentDetailList", contentDetailList);
			}
			industryMap.put("industryContentList", newIndustryContentList);
		}
		result.put("allIndustry", allIndustry);
	
//		if (crawlDetailId != null) {
//			Map<String, Object> query = new HashMap<>();
//			query.put("crawl_detail_id", crawlDetailId);
//			String AdIDExist = appCrawlDetailMapper.queryAdIDExist(query);
//			String[] AdIDArr = StringUtils.split(AdIDExist, ",");
//			if (AdIDArr != null && AdIDArr.length == 1) {
//				Map<String, Object> queryContentName = new HashMap<>();
//				queryContentName.put("content_detail_id", AdIDArr[0]);
//				String contentName = appCrawlDetailMapper.queryIndustryContentName(queryContentName);
//				if (StringUtils.equals(contentName, "IDFA") || StringUtils.equals(contentName, "IMEI")
//						|| StringUtils.equals(contentName, "AndroidID") || StringUtils.equals(contentName, "MAC")) {
//					result.put("AdIdContent", contentName);
//				}
//			}
//			String regList = appCrawlDetailMapper.queryCrawlDetailReg(query);
//
//			result.put("reg", regList);
//
//		}
		return result;
	}

	@Override
	public Map<String, Object> queryModifyAppIndustryInfo(Integer crawlDetailId) {

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> adId = new HashMap<>();
		adId.put("industry_name", "\"IDFA\",\"IMEI\",\"AndroidID\",\"MAC\"");
		adId.put("adIdExist", 2);
		// 先判断所选正则是否含有广告ID
		// 查询crawl_detail_id
		//if (crawlDetailId != null) {
			Map<String, Object> query = new HashMap<>();
			query.put("crawl_detail_id", crawlDetailId);
			String AdIDExist = appCrawlDetailMapper.queryAdIDExist(query);
			String[] AdIDArr = StringUtils.split(AdIDExist, ",");
			AppCrawlDetail appCrawlDetail = appCrawlDetailMapper.selectByPrimaryKey(crawlDetailId);
			if (AdIDArr != null && AdIDArr.length == 1) {
				Map<String, Object> queryIndustryName = new HashMap<>();
				queryIndustryName.put("content_detail_id", AdIDArr[0]);
				String industryName = appCrawlDetailMapper.queryIndustryName(queryIndustryName);
				if (StringUtils.equals(industryName, "IDFA") || StringUtils.equals(industryName, "IMEI")
						|| StringUtils.equals(industryName, "AndroidID") || StringUtils.equals(industryName, "MAC")) {
					result.put("AdIdContent", industryName);
					adId.put("adIdExist", 1);
					String paramReg=appCrawlDetail.getCrawlDetailParamreg();
					String paramNum = StringUtils.substring(paramReg, paramReg.lastIndexOf("\t")+2, paramReg.length());
					result.put("paramNum", paramNum);
				}
			}
			String regList =appCrawlDetail.getCrawlDetailReg();
			result.put("reg", regList);
		//}

		List<Map<String, Object>> allIndustry = appIndustryMapper.queryAllIndustryWithAdID(adId);
		for (Map<String, Object> industryMap : allIndustry) {
			Map<String, Object> map = new HashMap<String, Object>();
			int industryId = (int) industryMap.get("industry_id");
			map.put("industry_id", industryId);
			List<Map<String, Object>> industryContentList = appIndustryMapper.queryIndustryContent(map);
			List<Map<String, Object>> newIndustryContentList = new ArrayList<>(industryContentList);
			// 按照设定好的参数顺序排序
			for (Map<String, Object> industryContent : industryContentList) {
				newIndustryContentList.set((int) industryContent.get("content_order") - 1, industryContent);
			}
			for (Map<String, Object> IndustryContent : newIndustryContentList) {
				Map<String, Object> queryMap = new HashMap<String, Object>();
				queryMap.put("content_id", (int) IndustryContent.get("content_id"));
				List<Map<String, Object>> contentDetailList = appIndustryMapper.queryIndustryContentDetail(queryMap);
				IndustryContent.put("contentDetailList", contentDetailList);
			}
			industryMap.put("industryContentList", newIndustryContentList);
		}
		result.put("allIndustry", allIndustry);

		return result;

	}
}
