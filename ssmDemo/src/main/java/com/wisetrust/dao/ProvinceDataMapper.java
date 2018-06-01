package com.wisetrust.dao;

import java.util.List;

import com.wisetrust.pojo.ProvinceData;
import com.wisetrust.pojo.ProvinceDataExample;

public interface ProvinceDataMapper {
    int countByExample(ProvinceDataExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ProvinceData record);

    int insertSelective(ProvinceData record);

    List<ProvinceData> selectByExample(ProvinceDataExample example);

    ProvinceData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProvinceData record);

    int updateByPrimaryKey(ProvinceData record);
}