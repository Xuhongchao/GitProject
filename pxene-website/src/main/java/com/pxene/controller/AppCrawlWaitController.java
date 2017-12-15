package com.pxene.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.pxene.appcategory.ClassifyForApp;
import com.pxene.service.AppCrawlDoneService;
import com.pxene.service.AppCrawlWaitService;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年4月21日
 */
@Controller
@RequestMapping("/appCrawlerWait")
public class AppCrawlWaitController {
	private Log logger = LogFactory.getLog(AppCrawlWaitController.class);
	@Autowired
	AppCrawlWaitService appCrawlerWaitService;
	@Autowired
	ClassifyForApp classifyForApp;
	@Autowired
	AppCrawlDoneService appInfoService;
	
	@RequestMapping(value = "queryAppCategoryByAppId")
	@ResponseBody
	public Map<String, Object> queryAppCategoryByAppId(@RequestBody JSONObject params) {
		Integer appId = params.getInteger("app_id");
		return appCrawlerWaitService.queryAppCategoryByAppId(appId);
	}

	@RequestMapping(value = "delCrawlWaitApp")
	@ResponseBody
	public Map<String, Object> delCrawlWaitApp(@RequestBody JSONObject params) {
		Integer appId = params.getInteger("app_id");
		return appCrawlerWaitService.delCrawlWaitApp(appId);
	}

	@RequestMapping(value = "queryAppChildCategory")
	@ResponseBody
	public Map<String, Object> queryAppChildCategory(@RequestBody JSONObject params) {
		Integer categoryId = params.getInteger("category_id");
		return appCrawlerWaitService.queryAppChildCategory(categoryId);
	}

	@RequestMapping(value = "queryAppParentCategory")
	@ResponseBody
	public Map<String, Object> queryAppParentCategory() {
		return appCrawlerWaitService.queryAppParentCategory();
	}

	@RequestMapping(value = "queryCrawlAppCategory")
	@ResponseBody
	public Map<String, Object> queryCrawlAppCategory(@RequestBody JSONObject params) {
		Integer appType=params.getInteger("appType");
		String appSource=params.getString("source");
		return appCrawlerWaitService.queryCrawlAppCategory(appType,appSource);
	}

	@RequestMapping(value = "appTransfer")
	@ResponseBody
	public Map<String, Object> appTransfer(@RequestBody JSONObject params) {
		Map<String, Object> transfer = new HashMap<>();
		int appId = params.getIntValue("app_id");
		int searchId = params.getIntValue("searchId");
		int categoryId = params.getIntValue("category_id");
		transfer.put("appId", appId);
		transfer.put("searchId", searchId);
		transfer.put("categoryId", categoryId);
		return appCrawlerWaitService.appTransfer(transfer);
	}

	@RequestMapping(value = "queryTransferAppByName")
	@ResponseBody
	public Map<String, Object> queryTransferAppByName() {
		return appCrawlerWaitService.queryTransferAppByName();
	}

	@RequestMapping(value = "updateBlacklist")
	@ResponseBody
	public Map<String, Object> updateBlacklist(@RequestBody JSONObject params) {
		int appId = params.getIntValue("app_id");
		int isBlacklist = params.getIntValue("app_is_blacklist");
		return appCrawlerWaitService.updateBlacklist(appId, isBlacklist);
	}

	// app导入
	@RequestMapping(value = "importApp", produces = { "application/json" }, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doPost(@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "appType") Integer appType, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<>();
		String filePath;
		if (file != null) {
			String realPath = request.getSession().getServletContext().getRealPath("/") + "appInfo\\files";
			File path = new File(realPath);
			if (!path.exists()) {
				path.mkdirs();
			}
			String fileName = System.nanoTime() + "_" + file.getOriginalFilename();
			filePath = realPath + "\\" + fileName;
			file.transferTo(new File(filePath));
			// result.put("fileUrl", "/appInfo/files/" + fileName);
		} else {
			// 导入失败
			result.put("resultCode", "1");
			return result;
		}
		// TODO
		// 读取导入的文件，对文件中的数据进行处理
		Map<String, Object> importResult = new HashMap<>();
		importResult.put("resultCode", 1);
		 InputStream is = null;
		 Workbook wb = null;
		try {
			// 构建Workbook对象, 只读Workbook对象
			// 直接从本地文件创建Workbook
			InputStream instream = new FileInputStream(filePath);
			 String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
			 if (fileType.equals("xls")) {
		            wb = new HSSFWorkbook(instream);
		        } else if (fileType.equals("xlsx")) {
		            wb = new XSSFWorkbook(instream);
		        } else {
		           throw new Exception("读取的不是excel文件");
		        }
			
			 int sheetSize = wb.getNumberOfSheets();
			for (int k = 0; k < sheetSize; k++) {
				// Sheet的下标是从0开始
				// 获取第一张Sheet表
				Sheet readsheet = wb.getSheetAt(k);
				// 获取Sheet表中所包含的总行数
				int rsRows = readsheet.getLastRowNum() + 1;
				// 获取Sheet表中所包含的总列数;
				for (int i=0;i<rsRows;i++) {
					int rsColumns = readsheet.getRow(i).getLastCellNum();
					// 根据需求添加文件内容中的列数判断
					if (rsColumns != 2) {
						// 状态2表示导入的文件内容不符合规范
						result.put("resultCode", "2");
						return result;
					}
				}
				//int rsRows = readsheet.getRows();
				// 获取指定单元格的对象引用
				System.out.println("a");
				for (int i = 0; i < rsRows; i++) {
				 String appName = readsheet.getRow(i).getCell(0).toString();
				 String appSource = readsheet.getRow(i).getCell(1).toString();
					// 添加App
					try {
						importResult = appCrawlerWaitService.addAppOrWeb(appType, appName, appSource);
					} catch (Exception e) {
						logger.error(
								"appType:" + appType + " appName:" + appName + " appSource:" + appSource + "添加失败!");
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 if (wb != null) {
		            wb.close();
		        }
		        if (is != null) {
		            is.close();
		        }
		}
		System.out.println(importResult.get("resultCode"));
		if (importResult.get("resultCode").toString().equals("0")) {
			// 0表示导入成功
			result.put("resultCode", "0");
			logger.debug("app导入成功了!");
		} else{
			// 1表示导入失败
			result.put("resultCode", "1");
		}
		return result;
	}

	@RequestMapping(value = "addAppOrWeb")
	@ResponseBody
	public Map<String, Object> addAppOrWeb(@RequestBody JSONObject params) {
		Integer appType = params.getInteger("app_type");
		String appName = params.getString("app_name");
		String appSource = params.getString("app_source");
		return appCrawlerWaitService.addAppOrWeb(appType, appName, appSource);
	}

	@RequestMapping(value = "modifyPriority")
	@ResponseBody
	public Map<String, Object> modifyPriority(@RequestBody JSONObject params) {
		int appId = params.getIntValue("app_id");
		int appPriority = params.getIntValue("app_priority");
		return appCrawlerWaitService.modifyPriority(appId, appPriority);
	}

	@RequestMapping(value = "modifyWaitCrawlAppName")
	@ResponseBody
	public Map<String, Object> modifyWaitCrawlAppName(@RequestBody JSONObject params) {
		int appId = params.getIntValue("app_id");
		String appName = params.getString("app_name");
		return appCrawlerWaitService.modifyWaitCrawlAppName(appId, appName);
	}

	@RequestMapping(value = "queryAppSource")
	@ResponseBody
	public List<Map<String, Object>> queryAppSource(@RequestBody JSONObject params) {
		Map<String, Object> map = new HashMap<>();
		Integer appType = params.getInteger("appType");
		return appCrawlerWaitService.queryAppSource(appType);
	}

	@RequestMapping(value = "queryAppCategory")
	@ResponseBody
	public List<Map<String, Object>> queryAppCategory() {
		return appCrawlerWaitService.queryAppCategory();
	}

	@RequestMapping(value = "queryWebByPageAndNum")
	@ResponseBody
	public Map<String, Object> queryWebByPageAndNumBy(@RequestBody JSONObject params) {
		int page = params.getIntValue("startPage");
		int num = params.getIntValue("pageSize");
		String appSource = params.getString("source");
		int status = params.getIntValue("status");
		String appName = params.getString("appName");
		int appType = params.getIntValue("appType");
		return appCrawlerWaitService.queryWebPageAndNum(page, num, appSource, status, appName, appType);
	}

	@RequestMapping(value = "queryByPageAndNum")
	@ResponseBody
	public Map<String, Object> queryByPageAndNum(@RequestBody JSONObject params) {
		int page = params.getIntValue("startPage");
		int num = params.getIntValue("pageSize");
		String categoryName = params.getString("appCategoryName");
		String appSource = params.getString("source");
		int status = params.getIntValue("status");
		String appName = params.getString("appName");
		int appType = params.getIntValue("appType");
		return appCrawlerWaitService.queryPageAndNum(page, num, categoryName, appSource, status, appName, appType);
	}

	@RequestMapping(value = "updateWaitCrawlApp")
	@ResponseBody
	public void updateCrawlWaitApp() {
		appCrawlerWaitService.updateCrawlWaitApp();
	}

	@RequestMapping(value = "checkAppInfo")
	@ResponseBody
	public Map<String, Object> checkAppInfo(@RequestBody JSONObject params) {
		int id = params.getIntValue("id");
		return appCrawlerWaitService.checkAppInfo(id);
	}

}
