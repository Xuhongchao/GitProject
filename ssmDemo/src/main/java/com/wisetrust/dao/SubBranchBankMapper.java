package com.wisetrust.dao;

import java.util.List;

import com.wisetrust.pojo.SubBranchBank;
import com.wisetrust.pojo.SubBranchBankExample;

public interface SubBranchBankMapper {
    int countByExample(SubBranchBankExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SubBranchBank record);

    int insertSelective(SubBranchBank record);

    List<SubBranchBank> selectByExample(SubBranchBankExample example);

    SubBranchBank selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SubBranchBank record);

    int updateByPrimaryKey(SubBranchBank record);
}