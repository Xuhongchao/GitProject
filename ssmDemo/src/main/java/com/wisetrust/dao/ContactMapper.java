package com.wisetrust.dao;

import java.util.List;

import com.wisetrust.pojo.Contact;
import com.wisetrust.pojo.ContactExample;

public interface ContactMapper {
    int countByExample(ContactExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Contact record);

    int insertSelective(Contact record);

    List<Contact> selectByExample(ContactExample example);

    Contact selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Contact record);

    int updateByPrimaryKey(Contact record);
}