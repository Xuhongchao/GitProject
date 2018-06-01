package com.wisetrust.dao;

import java.util.List;

import com.wisetrust.pojo.Account;
import com.wisetrust.pojo.AccountExample;

public interface AccountMapper {
    int countByExample(AccountExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Account record);

    int insertSelective(Account record);

    List<Account> selectByExample(AccountExample example);

    Account selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);
}

