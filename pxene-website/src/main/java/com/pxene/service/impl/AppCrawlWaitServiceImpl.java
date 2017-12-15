package com.pxene.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pxene.appcategory.ClassifyForApp;
import com.pxene.crawler.Crawler4360App;
import com.pxene.crawler.Crawler4AppStore;
import com.pxene.crawler.Crawler4BaiduApp;
import com.pxene.crawler.Crawler4MiApp;
import com.pxene.crawler.Crawler4QQApp;
import com.pxene.dao.AppCrawlDoneMapper;
import com.pxene.dao.AppCrawlWaitMapper;
import com.pxene.dao.AppInfoDetailMapper;
import com.pxene.dao.AppInfoMapper;
import com.pxene.dao.ClassifyForAppMapper;
import com.pxene.service.AppCrawlDoneService;
import com.pxene.service.AppCrawlWaitService;
import com.pxene.utils.CodeNumUtils;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月21日
 */

@Service
public class AppCrawlWaitServiceImpl implements AppCrawlWaitService {
	private Log logger = LogFactory.getLog(AppCrawlWaitServiceImpl.class);
	private static final String TX = "腾讯应用宝";
	private static final String BD = "百度手机助手";
	private static final String SL = "360手机助手";
	private static final String XM = "小米应用商店";
	private static final String APPSTORE = "appstore";

	private static final String WZY = "未转移";
	private static final String YZY = "已转移";

	private static final String ANDROID = "0";
	private static final String IOS = "1";

	@Autowired
	AppCrawlWaitMapper appCrawlerWaitMapper;
	@Autowired
	AppCrawlDoneMapper appCrawlDoneMapper;
	@Autowired
	ClassifyForApp classifyForApp;
	@Autowired
	ClassifyForAppMapper classifyForAppMapper;

	@Autowired
	AppCrawlDoneService appInfoService;
	@Autowired
	AppInfoMapper appInfoMapper;
	@Autowired
	AppInfoDetailMapper appInfoDetailMapper;

	@Autowired
	Crawler4QQApp crawler4QQApp;
	@Autowired
	Crawler4BaiduApp crawler4BaiduApp;
	@Autowired
	Crawler4MiApp crawler4MiApp;
	@Autowired
	Crawler4360App crawler4360App;
	@Autowired
	Crawler4AppStore crawler4AppStore;

	/*
	 * public void updateApp(){ List<Map<String,Object>>
	 * appList=appCrawlerWaitMapper.queryUpdateApp(); for (Map<String, Object>
	 * app : appList) { Integer appId = (Integer) app.get("app_id"); String
	 * appName = app.get("app_name").toString(); String appType =
	 * app.get("app_type").toString(); Integer appStatus=(Integer)
	 * app.get("app_status"); crawlApp(appId, appName, appType, appStatus); } }
	 * 
	 * @Override public void crawlImportApp() { List<Map<String,Object>>
	 * appList=appCrawlerWaitMapper.queryCrawlerWaitApp(); for (Map<String,
	 * Object> app : appList) { Integer appId = (Integer) app.get("app_id");
	 * String appName = app.get("app_name").toString(); String appType =
	 * app.get("app_type").toString(); Integer appStatus=(Integer)
	 * app.get("app_status"); crawlApp(appId, appName, appType, appStatus); } }
	 * 
	 * @Override public void crawlApp(Integer appId, String appName, String
	 * appType, Integer appStatus) { List<String> appCategory = new
	 * ArrayList<>(); // 若未爬取,进行App分类 if (appStatus == 0) { appCategory =
	 * getAppCategory(appName); }
	 * 
	 * // boolean result = false; // result = crawler4QQApp.startCrawler(appId,
	 * appName, appStatus, appCategory.get(0), appCategory.get(1));
	 * 
	 * if (StringUtils.equals(appType, "2")) { boolean result = false; try {
	 * result = crawler4QQApp.startCrawler(appId, appName, appStatus,
	 * appCategory.get(0), appCategory.get(1)); } catch (Exception e) {
	 * logger.error("应用宝抓取异常:" +e.getMessage()); e.printStackTrace(); } if
	 * (!result) { try { result=crawler4BaiduApp.startCrawler(appId, appName,
	 * appStatus, appCategory.get(0), appCategory.get(1)); } catch (Exception e)
	 * { logger.error("百度应用市场抓取异常:" +e.getMessage()); e.printStackTrace(); } }
	 * if (!result) { try { result=crawler4MiApp.startCrawler(appId, appName,
	 * appStatus, appCategory.get(0), appCategory.get(1)); } catch (Exception e)
	 * { logger.error("小米应用市场抓取异常:" +e.getMessage()); e.printStackTrace(); } }
	 * if (!result) { try { result=crawler4360App.startCrawler(appId, appName,
	 * appStatus, appCategory.get(0), appCategory.get(1)); } catch (Exception e)
	 * { logger.error("360应用市场抓取异常:" +e.getMessage()); e.printStackTrace(); } }
	 * }
	 * 
	 * if (StringUtils.equals(appType, "3")) { try {
	 * crawler4AppStore.startCrawler(appId, appName, appStatus,
	 * appCategory.get(0), appCategory.get(1)); } catch (Exception e) {
	 * logger.error("AppStore抓取出现异常:"+e.getMessage()); e.printStackTrace(); } }
	 * }
	 */

	@Override
	public List<String> getAppCategory(String appName) {
		List<String> categoryList = new ArrayList<>();
		StringBuffer appCategoryIdStr = new StringBuffer(",");
		StringBuffer appCategoryWeightStr = new StringBuffer(",");

		// 为该app分类
		String classifyApp = classifyForApp.classifyApp(appName);
		// classifyApp=摄影与录像:1,游戏:2
		if (StringUtils.equals(classifyApp, "2")) {
			// 2表示无法获取分词网站信息App分类失败!
		} else if (StringUtils.equals(classifyApp, "3")) {
			// 若AppName都是标点符号则为不可识别类返回"3"

		} else {
			String[] appTypeArr = new String[1];
			// 若app分类只有一个
			if (!StringUtils.contains(classifyApp, ",")) {
				System.out.println(classifyApp);
				appTypeArr[0] = classifyApp;
			} else {
				appTypeArr = StringUtils.split(classifyApp, ",");
			}

			for (String appTypeStr : appTypeArr) {
				String[] typeArr = StringUtils.split(appTypeStr, ":");
				// TODO
				// 游戏:2!!!!!
				Map<String, Object> idMap = new HashMap<>();
				idMap.put("category_name", typeArr[0]);
				System.out.println(typeArr[0]);
				Integer categoryId = classifyForAppMapper.queryCategoryId(idMap);
				appCategoryIdStr.append(categoryId).append(",");
				appCategoryWeightStr.append(typeArr[1]).append(",");

				System.out.println();
				System.out.println();
			}
		}
		categoryList.add(appCategoryIdStr.toString());
		categoryList.add(appCategoryWeightStr.toString());
		return categoryList;
	}

	@Override
	public Map<String, Object> appTransfer(Map<String, Object> transfer) {
		int appId = (int) transfer.get("appId");
		int searchId = (int) transfer.get("searchId");
		int categoryId = (int) transfer.get("categoryId");
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("app_id", appId);
		boolean transferResult = false;
		// 若 选择无匹配项
		List<Map<String, Object>> appMap = appCrawlerWaitMapper.queryByAppId(map);
		if (StringUtils.equals(appMap.get(0).get("app_name").toString(), "")) {
			// 3表示该App名称为空,无法转移
			result.put("resultCode", 3);
			return result;
		}
		if (searchId == -1) {
			map.put("crawl_logo_url", appMap.get(0).get("app_logo_url"));
			map.put("crawl_app_name", appMap.get(0).get("app_name"));
			map.put("crawl_app_id", appMap.get(0).get("app_id").toString());
			map.put("crawl_app_category_id", categoryId);
			map.put("crawl_app_version", "");
			map.put("crawl_app_source", appMap.get(0).get("app_source"));
			map.put("crawl_app_os", appMap.get(0).get("app_type"));

			Integer exist = appCrawlDoneMapper.queryAppCrawlerDoneExist(map);
			if (exist > 0) {
				// 4表示该App名称已存在,无法转移
				result.put("resultCode", 4);
				return result;
			}
			// 更新app状态
			appCrawlerWaitMapper.updateAppStatus(map);
			// 添加参数分类编码
			String oldNum = appCrawlDoneMapper.queryMaxAppInfoNum();
			String newNum = "000001";
			if (oldNum != null) {
				int num = Integer.valueOf(oldNum) + 1;
				newNum = String.format("%06d", num);
			}
			String crawl_app_num = CodeNumUtils._10_to_62(Long.parseLong(newNum), 3);
			map.put("crawl_app_num", crawl_app_num);
			map.put("crawl_app_num_decimal", newNum);

			// 添加到已爬取表中
			boolean crawlDoneResult = appCrawlDoneMapper.addAppCrawlDone(map);
			if (crawlDoneResult) {
				map.put("crawl_app_num", crawl_app_num);
				map.put("app_is_major", 1);
				map.put("app_id", appMap.get(0).get("app_id").toString());
				map.put("app_os", appMap.get(0).get("app_type"));
				transferResult = appCrawlDoneMapper.addAppWaitDone(map);
			}
		} else {
			map.put("app_id", searchId);
			List<Map<String, Object>> appDoneMapList = appCrawlDoneMapper.queryCrawlAppDoneByAppId(map);
			map.put("crawl_app_num", appDoneMapList.get(0).get("crawl_app_num"));
			List<String> searchCrawlAppOSList = appCrawlDoneMapper.queryCrawlAppOS(map);
			for (String searchCrawlAppOS : searchCrawlAppOSList) {
				if (StringUtils.equals(searchCrawlAppOS, appMap.get(0).get("app_type").toString())) {
					// 2表示该App系统已经转移,转移失败
					result.put("resultCode", 2);
					return result;
				}
			}
			map.put("app_is_major", 0);
			// app_id为要转移的AppId
			map.put("app_id", appMap.get(0).get("app_id").toString());
			map.put("app_os", appMap.get(0).get("app_type"));
			// 更新app状态
			appCrawlerWaitMapper.updateAppStatus(map);
			transferResult = appCrawlDoneMapper.addAppWaitDone(map);
			// 在app_crawl_done中修改对应版本号
			//String version = "";
			// if(appDoneMapList.get(0).get("crawl_app_version")!=null){
			// version=appDoneMapList.get(0).get("crawl_app_version").toString();
			// }
			// if(appMap.get(0).get("app_version")!=null&&!StringUtils.equals(appMap.get(0).get("app_version").toString(),
			// "")){
			// if(StringUtils.equals(version, "")){
			// version+=appMap.get(0).get("app_version");
			// }else{
			// version+=","+appMap.get(0).get("app_version");
			// }
			// }
			// 在app_crawl_done中修改对应来源
			String source = appDoneMapList.get(0).get("crawl_app_source").toString();
			String[] sourceArr = StringUtils.split(appMap.get(0).get("app_source").toString(), ",");
			for (String sourceStr : sourceArr) {
				if (!StringUtils.contains(appDoneMapList.get(0).get("crawl_app_source").toString(),
						"," + sourceStr + ",")) {
					source += sourceStr + ",";
				}
			}
			// 若转移的主App为网站,则使用从App的logo
			if (appDoneMapList.get(0).get("crawl_app_logo_url") == null) {
				map.put("crawl_app_logo_url",
						appMap.get(0).get("app_logo_url"));
			}
			// app_id为主AppId
			map.put("app_id", searchId);
			//map.put("app_version", version);
			map.put("app_source", source);
			appCrawlDoneMapper.updateAppCrawlDone(map);
		}

		if (transferResult) {
			// 若App已转移将优先级修改为4
			map.put("app_priority", 4);
			appCrawlerWaitMapper.modifyPriority(map);
			result.put("resultCode", 0);
		} else {
			result.put("resultCode", 1);
		}
		return result;
	}

	@Override
	public Map<String, Object> queryTransferAppByName() {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = appCrawlerWaitMapper.queryTransferAppByName();
		result.put("list", list);
		return result;
	}

	@Override
	public Map<String, Object> updateBlacklist(int appId, int isBlacklist) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("app_id", appId);
		map.put("app_is_blacklist", isBlacklist);

		try {
			appCrawlerWaitMapper.updateBlacklist(map);
			// 加入黑名称后,App优先级调整为4
			if (isBlacklist == 1) {
				map.put("app_priority", 4);
			} else {
				String source = appCrawlerWaitMapper.querySourceByAppId(map);
				String[] sourceArr = StringUtils.split(source, ",");
				if (sourceArr.length > 1) {
					map.put("app_priority", 2);
				} else {
					map.put("app_priority", 3);
				}
			}
			appCrawlerWaitMapper.modifyPriority(map);
		} catch (Exception e) {
			logger.error("App加入黑名单异常!");
			e.printStackTrace();
			result.put("resultCode", 1);
			return result;
		}
		result.put("resultCode", 0);
		return result;
	}

	@Override
	public Map<String, Object> addAppOrWeb(Integer appType, String appName, String appSource) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		boolean addResult = false;
		map.put("app_name", appName);
		map.put("source_name", appSource);

		if (appType == 2 || appType == 3) {
			map.put("app_type_list", "2,3");
		}
		if (appType == 4 || appType == 5) {
			map.put("app_type_list", "4,5");
		}

		// 若App已转移则无法添加
		Integer appStatus = appCrawlerWaitMapper.queryAppStatus(map);
		if (appStatus == 2) {
			// 3表示该App已转移无法添加
			result.put("resultCode", 3);
			return result;
		}
		Integer sourceId = appCrawlerWaitMapper.querySource(map);

		if (sourceId == null) {
			appCrawlerWaitMapper.addSource(map);
			sourceId = appCrawlerWaitMapper.querySource(map);
		}
		map.put("app_source", sourceId);

		// 判断是否符合添加条件
		List<Map<String, Object>> appExist = appCrawlerWaitMapper.queryAppExist(map);
		Set<Map<String, Object>> appExistSet = new HashSet<>();
		appExistSet.addAll(appExist);
		if (appExistSet.size() == 2) {
			// 2表示该App已存在,无法添加
			result.put("resultCode", 2);
		} else {

			List<Map<String, Object>> appList = appCrawlerWaitMapper.queryAppNameExist(map);
			List<Map<String, Object>> appNameEmptyList = appCrawlerWaitMapper.queryAppNameEmptyExist(map);
			if (appList.size() < 2) {
				// 若应用市场
				StringBuffer appSourceStr = new StringBuffer(",");
				appSourceStr.append(sourceId).append(",");
				map.put("app_source_str", appSourceStr.toString());

				if (appType == 2 || appType == 3) {
					try {
						crawler4QQApp.startCrawler(appName, appSourceStr.toString(), null);
					} catch (Exception e) {
						logger.error("应用宝抓取异常:" + e.getMessage());
						e.printStackTrace();
					}
					try {
						crawler4BaiduApp.startCrawler(appName, appSourceStr.toString(), null);
					} catch (Exception e) {
						logger.error("百度应用市场抓取异常:" + e.getMessage());
						e.printStackTrace();
					}
					try {
						crawler4MiApp.startCrawler(appName, appSourceStr.toString(), null);
					} catch (Exception e) {
						logger.error("360应用市场抓取异常:" + e.getMessage());
						e.printStackTrace();
					}
					try {
						crawler4360App.startCrawler(appName, appSourceStr.toString(), null);
					} catch (Exception e) {
						logger.error("小米应用市场抓取异常:" + e.getMessage());
						e.printStackTrace();
					}
					try {
						crawler4AppStore.startCrawler(appName, appSourceStr.toString(), null);
					} catch (Exception e) {
						logger.error("AppStore抓取异常:" + e.getMessage());
						e.printStackTrace();
					}
					//判断是否抓取到相关App,若没有则添加
					List<Map<String, Object>> isFailed = appCrawlerWaitMapper.queryAppNameExist(map);
					if(isFailed.size()==0){
						Map<String,Object> appMap=new HashMap<>();
						appMap.put("app_name", appName);
						appMap.put("app_source_str", appSourceStr);
						map.put("app_type", 2);
						addResult = appCrawlerWaitMapper.addWeb(map);
						map.put("app_type", 3);
						addResult = appCrawlerWaitMapper.addWeb(map);
					}
					else if(isFailed.size()==1&&isFailed.get(0).get("app_appId")==null){
						Map<String,Object> appMap=new HashMap<>();
						appMap.put("app_name", appName);
						appMap.put("app_source_str", appSourceStr);
						//App应用市场抓取App失败,单独添加App
						if ((Integer) appList.get(0).get("app_type") != 2) {
							appMap.put("app_type", 2);
							addResult = appCrawlerWaitMapper.addWeb(appMap);
						}
						if ((Integer) appList.get(0).get("app_type") != 3) {
							appMap.put("app_type", 3);
							addResult = appCrawlerWaitMapper.addWeb(appMap);
						}
					}
					addResult = true;
				}

				// 4PC网站5移动网站
				if (appType == 4 || appType == 5) {
					if (appList.size() == 1) {
						if ((Integer) appList.get(0).get("app_type") != 4) {
							map.put("app_type", 4);
							addResult = appCrawlerWaitMapper.addWeb(map);
						}
						if ((Integer) appList.get(0).get("app_type") != 5) {
							map.put("app_type", 5);
							addResult = appCrawlerWaitMapper.addWeb(map);
						}
					} else {
						map.put("app_type", 4);
						addResult = appCrawlerWaitMapper.addWeb(map);
						map.put("app_type", 5);
						addResult = appCrawlerWaitMapper.addWeb(map);
					}
				}
				// appList = appCrawlerWaitMapper.queryAppNameExist(map);
				// result.put("appList", appList);
				if (addResult) {
					// 0表示新增成功
					result.put("resultCode", 0);
				} else {
					// 1表示新增失败
					result.put("resultCode", 1);
				}

			}
			// 若App名称已存在 为其更新优先级和来源 ,若 由于应用市场抓取App而名称不存在时 为其新增App名称并更新优先级和来源
 			if (appList.size() > 0 || appNameEmptyList.size() > 0) {
				for (Map<String, Object> appMap : appList) {
					//已转移App的来源,优先级不做改变
					if ((Integer) appMap.get("app_status") != 2) {
						map.put("app_id", (int) appMap.get("app_id"));
						StringBuffer appSourceStr = new StringBuffer(appMap.get("app_source").toString());
						if (!StringUtils.contains(appMap.get("app_source").toString(), "," + sourceId + ",")) {
							appSourceStr.append(sourceId).append(",");
						}
						map.put("app_source_str", appSourceStr.toString());
						map.put("app_name", appName);
						addResult = appCrawlerWaitMapper.updateApp(map);
					}
				}
				for (Map<String, Object> appMap : appNameEmptyList) {
					if ((Integer) appMap.get("app_status") != 2) {
						map.put("app_id", (int) appMap.get("app_id"));
						StringBuffer appSourceStr = new StringBuffer(appMap.get("app_source").toString());
						if (!StringUtils.contains(appMap.get("app_source").toString(), "," + sourceId + ",")) {
							appSourceStr.append(sourceId).append(",");
						}
						map.put("app_source_str", appSourceStr.toString());
						map.put("app_name", appName);
						addResult = appCrawlerWaitMapper.updateApp(map);
					}
				}

				if (addResult) {
					// 4表示该APP已存在，已为其更新优先级和来源
					result.put("resultCode", 4);
				} else {
					result.put("resultCode", 1);
				}
			}

		}
		return result;
	}

	@Override
	public Map<String, Object> modifyPriority(int appId, int appPriority) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("app_id", appId);
		map.put("app_priority", appPriority);
		boolean modifyResult = appCrawlerWaitMapper.modifyPriority(map);
		if (modifyResult) {
			result.put("resultCode", 0);
		} else {
			result.put("resultCode", 1);
		}
		return result;
	}

	@Override
	public Map<String, Object> modifyWaitCrawlAppName(int appId, String appName) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("app_id", appId);
		map.put("app_name", appName);
		try {
			appCrawlerWaitMapper.modifyWaitCrawlAppName(map);
		} catch (Exception e) {
			logger.error("修改App名称出错!");
			e.printStackTrace();
			result.put("resultCode", 1);
			return result;
		}
		result.put("resultCode", 0);
		return result;
	}

	@Override
	public List<Map<String, Object>> queryAppSource(Integer appType) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sourceId = "";
		if (appType == 2) {
			sourceId = "5";
		}
		if (appType == 3) {
			sourceId = "1,2,3,4";
		}
		if (appType == 4 || appType == 5) {
			sourceId = "1,2,3,4,5";
		}
		map.put("source_id", sourceId);
		return appCrawlerWaitMapper.queryAppSource(map);
	}

	@Override
	public List<Map<String, Object>> queryAppCategory() {
		return appCrawlerWaitMapper.queryAppCategory();
	}

	@Override
	public Map<String, Object> queryPageAndNum(int page, int num, String categoryName, String appSource, int status,
			String appName, int appType) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("num", num);

		map.put("category_name", categoryName);
		map.put("app_source", appSource);
		map.put("app_status", status);
		map.put("app_name", appName);
		map.put("app_type", appType);
		result.put("totalcount", appCrawlerWaitMapper.queryCrawlerWaitTotalCount(map));
		List<Map<String, Object>> list = appCrawlerWaitMapper.queryByPageAndNum(map);

		for (Map<String, Object> app : list) {
			if (app.get("app_logo_url") == null) {
				app.put("app_logo_url", "");
			}
			// 处理App来源
			String[] sourceIdArr = StringUtils.split(app.get("app_source").toString(), ",");
			StringBuffer sourceNameSb = new StringBuffer();
			String sourceStr = "";
			for (String sourceId : sourceIdArr) {
				Map<String, Object> queryMap = new HashMap<>();
				queryMap.put("source_id", Integer.parseInt(sourceId));
				String sourceName = appCrawlerWaitMapper.querySourceName(queryMap);
				sourceNameSb.append(sourceName).append(",");
				sourceStr = sourceNameSb.toString();
			}
			sourceStr = StringUtils.substring(sourceStr, 0, sourceStr.length() - 1);
			app.put("app_source", sourceStr);
			// 处理app排名数据
			Long ranking1 = Long
					.parseLong((app.get("app_ranking1") == null) ? "0" : app.get("app_ranking1").toString());
			Long ranking2;
			if (app.get("app_ranking2") == null) {
				ranking2 = 0L;
			} else {
				ranking2 = Long.parseLong(app.get("app_ranking2").toString());
			}
			if (ranking2 == 0) {
				// 0表示app为新加入app
				app.put("app_flag", 0);
				app.put("app_value", "");
			} else if (ranking1 == ranking2) {
				// 1表示app排名不变
				app.put("app_flag", 1);
				app.put("app_value", 0);
			} else if (ranking1 < ranking2) {
				// 2表示app排名下降
				app.put("app_flag", 2);
				app.put("app_value", ranking2 - ranking1);
			} else if (ranking1 > ranking2) {
				// 3表示app排名上升
				app.put("app_flag", 3);
				app.put("app_value", ranking1 - ranking2);
			}
			// 处理时间显示数据
			Date updateTime = (Date) app.get("app_update_time");
			Date createTime = (Date) app.get("app_create_time");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			app.put("app_update_time", updateTime == null ? "" : sdf.format(updateTime));
			app.put("app_create_time", sdf.format(createTime));
		}

		result.put("list", list);
		return result;
	}

	@Override
	public Map<String, Object> queryWebPageAndNum(int page, int num, String appSource, int status, String appName,
			int appType) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("num", num);

		map.put("app_source", appSource);

		map.put("app_status", status);
		map.put("app_name", appName);
		map.put("app_type", appType);
		result.put("totalcount", appCrawlerWaitMapper.queryCrawlerWaitWebTotalCount(map));
		List<Map<String, Object>> list = appCrawlerWaitMapper.queryWebByPageAndNum(map);
		for (Map<String, Object> webMap : list) {
			// 处理App来源
			String[] sourceIdArr = StringUtils.split(webMap.get("app_source").toString(), ",");
			StringBuffer sourceNameSb = new StringBuffer();
			String sourceStr = "";
			for (String sourceId : sourceIdArr) {
				Map<String, Object> queryMap = new HashMap<>();
				queryMap.put("source_id", Integer.parseInt(sourceId));
				String sourceName = appCrawlerWaitMapper.querySourceName(queryMap);
				sourceNameSb.append(sourceName).append(",");
				sourceStr = sourceNameSb.toString();
			}
			sourceStr = StringUtils.substring(sourceStr, 0, sourceStr.length() - 1);
			webMap.put("app_source", sourceStr);
			// 处理日期数据
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String format = sdf.format(webMap.get("app_create_time"));
			webMap.put("app_create_time", format);
		}
		result.put("list", list);
		return result;
	}

	@Override
	public Map<String, Object> queryById(Integer id) {
		/*
		 * Map<String, Integer> map = new HashMap<String, Integer>();
		 * map.put("id", id); return appCrawlerWaitMapper.queryById(map);
		 */
		return new HashMap<>();
	}

	@Override
	public List<Map<String, Object>> queryByName(String appName) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("appName", appName);
		return appCrawlerWaitMapper.queryByName(map);
	}

	@Override
	public Map<String, Object> checkAppInfo(int id) {
		/*
		 * Map result = new HashMap(); Map<String, Object> entityById =
		 * queryById(id); if (entityById != null) { // 获取appName String appName
		 * = entityById.get("app_name").toString(); List<Map<String, Object>>
		 * entitysByName = queryByName(appName); // 获取appOs int appOs = -1; if
		 * (entitysByName != null && entitysByName.size() > 0) { if
		 * (entitysByName.size() == 1) {// 要么是ios,要么是android,所以直接进行转移操作 Integer
		 * appType = Integer.valueOf(entitysByName.get(0).get("app_type") + "");
		 * if (appType == 0) {// android appOs = 2; } else if (appType == 1) {//
		 * ios appOs = 1; } if (appInfoService.queryByName(appName) == 0) { //
		 * 更新appStatus updateStatus(id); // 获取appCategoryId int appCategoryId =
		 * Integer.valueOf(entityById.get("app_category_id") + ""); //
		 * 获取appSource String appSource = entityById.get("app_source") + "";
		 * String appLogoUrl = entityById.get("app_logo_url") + ""; AppInfo
		 * appInfo = new AppInfo(); appInfo.setApp_name(appName);
		 * appInfo.setApp_category_id(appCategoryId);
		 * appInfo.setApp_logo_url(appLogoUrl); appInfo.setApp_os(appOs);
		 * appInfo.setApp_create_time(new java.sql.Date(new Date().getTime()));
		 * appInfo.setApp_source(appSource); appInfo.setApp_domain("");
		 * appInfo.setApp_attach_param(""); appInfo.setApp_isexport(0); return
		 * appInfoService.addAppInfo(appInfo, -1, ""); } else {
		 * result.put("resultCode", 2); return result; } } else if
		 * (entitysByName.size() == 2) { Integer appType1 =
		 * Integer.valueOf(entitysByName.get(0).get("app_type") + ""); Integer
		 * appType2 = Integer.valueOf(entitysByName.get(1).get("app_type") +
		 * ""); if (appType1 != appType2) { result.put("resultCode", 3);
		 * result.put("statusAPPType", entityById.get("app_type")); return
		 * result; } } } } result.put("resultCode", 1); return result;
		 */
		return new HashMap<>();
	}
	
	@Override
	public Map<String, Object> queryCrawlAppCategory(Integer appType, String appSource) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<>();
		map.put("app_type", appType);
		map.put("app_source", appSource);
		List<Map<String, Object>> list = appCrawlerWaitMapper.queryCrawlAppCategory(map);
		result.put("crawlAppCategoryList", list);
		return result;
	}
	

	@Override
	public Map<String, Object> queryAppParentCategory() {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = appCrawlerWaitMapper.queryAppParentCategory();
		result.put("appParentCategoryList", list);
		return result;
	}

	@Override
	public Map<String, Object> queryAppChildCategory(Integer categoryId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<>();
		map.put("category_pid", categoryId);
		List<Map<String, Object>> list = appCrawlerWaitMapper.queryAppChildCategory(map);
		result.put("appChildCategoryList", list);
		return result;
	}

	@Override
	public Map<String, Object> delCrawlWaitApp(Integer appId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<>();
		map.put("app_id", appId);
		try {
			appCrawlerWaitMapper.delCrawlWaitApp(map);
		} catch (Exception e) {
			logger.debug("待抓取App删除出错!");
			e.printStackTrace();
			result.put("resultCode", 1);
			return result;
		}
		result.put("resultCode", 0);
		return result;
	}

	@Override
	public void updateCrawlWaitApp() {
		Map<String, Object> map = new HashMap<>();
		map.put("app_source", "1");
		List<Map<String, Object>> appListForQQ = appCrawlerWaitMapper.queryCrawlWaitAppForUpdate(map);
		for (Map<String, Object> appMap : appListForQQ) {
			try {
				crawler4QQApp.startCrawler(appMap.get("app_name_crawl").toString(), ",",
						appMap.get("app_appId").toString());
			} catch (Exception e) {
				logger.error("App:" + appMap.get("app_name_crawl") + " 应用宝更新失败!");
				e.printStackTrace();
			}
		}
		map.put("app_source", "2");
		List<Map<String, Object>> appListForBaidu = appCrawlerWaitMapper.queryCrawlWaitAppForUpdate(map);
		for (Map<String, Object> appMap : appListForBaidu) {
			try {
				crawler4BaiduApp.startCrawler(appMap.get("app_name_crawl").toString(), ",",
						appMap.get("app_appId").toString());
			} catch (Exception e) {
				logger.error("App:" + appMap.get("app_name_crawl") + " 百度应用市场更新失败!");
				e.printStackTrace();
			}
		}
		map.put("app_source", "3");
		List<Map<String, Object>> appListFor360 = appCrawlerWaitMapper.queryCrawlWaitAppForUpdate(map);
		for (Map<String, Object> appMap : appListFor360) {
			try {
				crawler4360App.startCrawler(appMap.get("app_name_crawl").toString(), ",",
						appMap.get("app_appId").toString());
			} catch (Exception e) {
				logger.error("App:" + appMap.get("app_name_crawl") + " 360应用市场更新失败!");
				e.printStackTrace();
			}
		}
		map.put("app_source", "4");
		List<Map<String, Object>> appListForMi = appCrawlerWaitMapper.queryCrawlWaitAppForUpdate(map);
		for (Map<String, Object> appMap : appListForMi) {
			try {
				crawler4MiApp.startCrawler(appMap.get("app_name_crawl").toString(), ",",
						appMap.get("app_appId").toString());
			} catch (Exception e) {
				logger.error("App:" + appMap.get("app_name_crawl") + " 小米应用市场更新失败!");
				e.printStackTrace();
			}
		}
		map.put("app_source", "5");
		List<Map<String, Object>> appListForApp = appCrawlerWaitMapper.queryCrawlWaitAppForUpdate(map);
		for (Map<String, Object> appMap : appListForApp) {
			try {
				crawler4AppStore.startCrawler(appMap.get("app_name_crawl").toString(), ",",
						appMap.get("app_appId").toString());
			} catch (Exception e) {
				logger.error("App:" + appMap.get("app_name_crawl") + " AppStore更新失败!");
				e.printStackTrace();
			}
		}
	}

	@Override
	public Map<String, Object> queryAppCategoryByAppId(Integer appId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<>();
		map.put("app_id", appId);
		List<Map<String, Object>> list = appCrawlerWaitMapper.queryAppCategoryByAppId(map);
		result.put("appCategory", list);
		return result;
	}
}
