package com.wisetrust.dao;

import java.util.List;

import com.wisetrust.pojo.Work;
import com.wisetrust.pojo.WorkExample;

public interface WorkMapper {
    int countByExample(WorkExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Work record);

    int insertSelective(Work record);

    List<Work> selectByExample(WorkExample example);

    Work selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Work record);

    int updateByPrimaryKey(Work record);
}