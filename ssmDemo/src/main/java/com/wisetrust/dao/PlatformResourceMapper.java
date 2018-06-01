package com.wisetrust.dao;

import java.util.List;

import com.wisetrust.pojo.PlatformResource;
import com.wisetrust.pojo.PlatformResourceExample;

public interface PlatformResourceMapper {
    int countByExample(PlatformResourceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PlatformResource record);

    int insertSelective(PlatformResource record);

    List<PlatformResource> selectByExample(PlatformResourceExample example);

    PlatformResource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlatformResource record);

    int updateByPrimaryKey(PlatformResource record);
}