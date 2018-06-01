package com.wisetrust.dao;

import java.util.List;

import com.wisetrust.pojo.AreaData;
import com.wisetrust.pojo.AreaDataExample;

public interface AreaDataMapper {
    int countByExample(AreaDataExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AreaData record);

    int insertSelective(AreaData record);

    List<AreaData> selectByExample(AreaDataExample example);

    AreaData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AreaData record);

    int updateByPrimaryKey(AreaData record);
}