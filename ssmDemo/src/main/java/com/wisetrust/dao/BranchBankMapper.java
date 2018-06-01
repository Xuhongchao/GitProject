package com.wisetrust.dao;

import java.util.List;

import com.wisetrust.pojo.BranchBank;
import com.wisetrust.pojo.BranchBankExample;

public interface BranchBankMapper {
    int countByExample(BranchBankExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BranchBank record);

    int insertSelective(BranchBank record);

    List<BranchBank> selectByExample(BranchBankExample example);

    BranchBank selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BranchBank record);

    int updateByPrimaryKey(BranchBank record);
}