package com.pxene.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by chongaizhen on 2016/12/21.
 */
public interface AppInfoDetailMapper {
    void addDetail(Map<String,Object> map);
    void delDetail(Map<String,Object> map);
    List<Map<String,Object>> queryByNum(Map<String,Object> map);
	List<Map<String,Object>> queryByAppCrawlId(Map appCrawlMap);
	boolean updateIsMajor(Map appInfoDetailMap);
}
