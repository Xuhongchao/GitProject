package com.pxene.dao;

import java.util.List;
import java.util.Map;

public interface ClassifyForAppMapper {
	List<String> queryKeywordType(Map<String,Object> keyword);

	String adjustAppType(Map<String,Object> map);

	boolean addKeyword(Map<String, Object> map);

	boolean addUnknownMap(Map<String, Object> keywordMap);

	String queryAppTypeName(Map<String, Object> map);

	Integer queryCategoryId(Map<String, Object> idMap);

}
