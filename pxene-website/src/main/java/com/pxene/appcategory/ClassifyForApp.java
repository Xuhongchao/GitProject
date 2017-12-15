package com.pxene.appcategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.ansj.domain.Result;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pxene.dao.ClassifyForAppMapper;
import com.pxene.utils.ConfigUtil;
import com.pxene.utils.WordSegUtil;

/**
 * 
 * Created by @author wangzhenlin on @date 2017年3月24日
 */
@Service
public class ClassifyForApp {
	private static final Logger logger=LogManager.getLogger(ClassifyForApp.class);
	@Autowired
	ClassifyForAppMapper classifyForAppMapper;

	/**
	 * 
	 * @param appName
	 * @return
	 */
	public String classifyApp(String appName) {
		boolean recognize = false;
		if (!StringUtils.equals(appName.trim(), "")) {
			if (!StringUtils.contains(appName, "?")) {
				recognize = true;
			} else {
				if (com.pxene.utils.StringUtils.isContainChinese(appName)) {
					recognize = true;
				} else {
					recognize = false;
				}
			}
		}
		System.out.println(appName);
		if (recognize) {
			// 对appName进行分词
			//String name = com.pxene.utils.StringUtils.removePunctAndSpace(appName);
			
			//在ansj中添加自定义词典
			String name=appName;
			Properties properties = ConfigUtil.loadConfigFileByPath("/keywordAdjust.properties");
			Set<Object> keySet = properties.keySet();
			for (Object object : keySet) {
				UserDefineLibrary.insertWord(object.toString());
			}
			//对appName进行分词
			//Result parse = ToAnalysis.parse(name);
			String stringWithOutNature=WordSegUtil.chnSeg(name);
			//String stringWithOutNature = parse.toStringWithOutNature(" ");
			String[] words = StringUtils.split(stringWithOutNature, " ");
			// app对应所有关键词类型的集合
			//处理关键词为标点符号的情况
			List<String> appTypeList = new ArrayList<>();
			for (String word : words) {
				System.out.println(word);
				//若关键词为标点符号,不做处理
				if(StringUtils.equals(com.pxene.utils.StringUtils.removePunctAndSpace(word), "")){
					continue;
				}
				//判断关键词词性
				
				Map<String, Object> map = new HashMap<>();
				map.put("keyword_name", word);
				List<String> keywordTypeStr = classifyForAppMapper.queryKeywordType(map);
				// 若关键词在 分词表中不存在,则抓取该关键词 对应的类型
				if (keywordTypeStr == null || keywordTypeStr.size() == 0) {
					// 查询关键词对应的类型
					 List<String> keywordTypeStrList=new ArrayList<>();
					try {
						keywordTypeStrList = ClassifyForKeyword.getAppInfoForIOS(word);
						//关键词的分类依据ios映射关系，如果ios中没有爬到此关键词的任何信息，则采用android映射中的类型
						if(keywordTypeStrList.size()==0){
							keywordTypeStrList = ClassifyForKeyword.getAppInfoForAndroid(word);
						}
					} catch (RuntimeException e) {
						logger.error(e.getMessage());
						e.printStackTrace();
						//2表示无法获取分词网站信息App分类失败!
						return "2";
					}
					/*List<String> keywordTypeStrList = new ArrayList<>();
					keywordTypeStrList.add("健康健美");
					keywordTypeStrList.add("生活");
					keywordTypeStrList.add("教育");
					keywordTypeStrList.add("摄影与录像");
					keywordTypeStrList.add("教育");
					keywordTypeStrList.add("健康健美");
					keywordTypeStrList.add("美食佳饮");
					keywordTypeStrList.add("健康健美");
					keywordTypeStrList.add("教育");
					keywordTypeStrList.add("教育");
					keywordTypeStrList.add("教育");
					keywordTypeStrList.add("教育");
					keywordTypeStrList.add("教育");
					keywordTypeStrList.add("教育");
					keywordTypeStrList.add("教育");
					keywordTypeStrList.add("教育");
					keywordTypeStrList.add("教育");
					keywordTypeStrList.add("教育");
					keywordTypeStrList.add("教育");
					keywordTypeStrList.add("教育");*/

					List<String> keywordTypeList = new ArrayList<>();
					// 关键词对应的类型集合
					List<String> keywordType = new ArrayList<>();

					for (String alltype : keywordTypeStrList) {
						if (!keywordTypeList.contains(alltype)) {
							keywordTypeList.add(alltype);
						}
					}

					int maxTypeNum = 0;
					for (int i = 0; i < keywordTypeList.size(); i++) {
						int typeNum = 0;
						for (String alltype : keywordTypeStrList) {
							if (StringUtils.equals(alltype, keywordTypeList.get(i))) {
								typeNum++;
							}
						}
						if (typeNum > maxTypeNum) {
							maxTypeNum = typeNum;
							keywordType.clear();
							keywordType.add(keywordTypeList.get(i));
						} else if (typeNum == maxTypeNum) {
							keywordType.add(keywordTypeList.get(i));
						}
					}
					for (String type : keywordType) {
						Map<String, Object> keywordMap=new HashMap<>();
						keywordMap.put("adjust_app_type", type);
						// 对关键词对应类型进行微调
						type=adjustAppType(type);
						//若 关键词对应的类型在 类型调整表中不存在,就将它归为 "未知"类(25)中
						if(type==null){
							//将"未知"类 和 关键词对应的类型 的映射 存入 app_category_adjust表中
							keywordMap.put("adjust_category_id", 25);
							classifyForAppMapper.addUnknownMap(keywordMap);
							type="25";
						}
						//将 关键词对应的类型存入  app_category_keyword表中
						keywordMap.put("keyword_name", word);
						keywordMap.put("keyword_category_id",Integer.parseInt(type));
						classifyForAppMapper.addKeyword(keywordMap);
						// 将该关键词对应类型 添加到 app对应所有关键词类型的集合中
						appTypeList.add(type);
					}
				} else {
					for (String type : keywordTypeStr) {
						appTypeList.add(type);
					}
				}
				
				// app对应所有关键词类型的集合初始化完毕

				logger.debug("app分类关键词类型集合"+appTypeList);
			}
			
			// 根据 所有关键词类型的集合 确定 appName对应的类型集合
			List<String> appTypeStrList = new ArrayList<>();
			StringBuilder returnAppTypeBuilder=new StringBuilder();
			String returnAppType="";
			for (String appType : appTypeList) {
				if(!appTypeStrList.contains(appType)){
					appTypeStrList.add(appType);
				}
			}
			
			for (String appTypeStr : appTypeStrList) {
				int weight=0;
				String appTypeName;
				for (String appType : appTypeList) {
					if(StringUtils.equals(appTypeStr, appType)){
						weight++;
					}
				}
				Map<String,Object> map=new HashMap<>();
				map.put("category_id", appTypeStr);
				appTypeName=classifyForAppMapper.queryAppTypeName(map);
				
				returnAppTypeBuilder.append(appTypeName).append(":").append(weight).append(",");
				/*Map<String, Object> appTypeMap=new HashMap<>();
				appTypeMap.put("", appTypeStr);
				appTypeMap.put("", weight);
				boolean flag=classifyForAppMapper.saveAppType(appTypeMap);*/
			}
			if(returnAppTypeBuilder.length()!=0){
				returnAppType=returnAppTypeBuilder.substring(0, returnAppTypeBuilder.lastIndexOf(","));
			}
			//若AppName都是标点符号则为不可识别类返回"3"
			if(StringUtils.equals(returnAppType, "")){
				return "3";
			}
			logger.debug("appTypeList: "+returnAppType);
			return returnAppType;
		} else {
			//若App不可识别,返回空字符串
			//3表示该appName不可识别无法分类!
			return "3";
		}
	}
	
	/**
	 * 根据App类型微调规则调整App类型
	 * @param appType
	 * @return
	 */
	public String adjustAppType(String appType){
		Map<String,Object> map=new HashMap<>();
		map.put("adjust_app_type", appType);
		String newAppType=classifyForAppMapper.adjustAppType(map);
		return newAppType;
	}

	/**
	 * 
	 * @param keyword
	 * @return
	 */
	public boolean updateKeywordCategory(String keyword) {

		return false;
	}
}
