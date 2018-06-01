package cn.com.wisetrust.dao;

import cn.com.wisetrust.entity.Admin;
import cn.com.wisetrust.entity.AdminExample;
import java.util.List;

public interface AdminMapper {
    int countByExample(AdminExample example);

    int deleteByPrimaryKey(String id);

    int insert(Admin record);

    int insertSelective(Admin record);

    List<Admin> selectByExample(AdminExample example);

    Admin selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);
}