package com.wisetrust.dao;

import java.util.List;

import com.wisetrust.pojo.CityData;
import com.wisetrust.pojo.CityDataExample;

public interface CityDataMapper {
    int countByExample(CityDataExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CityData record);

    int insertSelective(CityData record);

    List<CityData> selectByExample(CityDataExample example);

    CityData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CityData record);

    int updateByPrimaryKey(CityData record);
}