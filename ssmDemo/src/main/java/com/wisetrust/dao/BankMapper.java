package com.wisetrust.dao;

import java.util.List;

import com.wisetrust.pojo.Bank;
import com.wisetrust.pojo.BankExample;

public interface BankMapper {
    int countByExample(BankExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Bank record);

    int insertSelective(Bank record);

    List<Bank> selectByExample(BankExample example);

    Bank selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Bank record);

    int updateByPrimaryKey(Bank record);
}