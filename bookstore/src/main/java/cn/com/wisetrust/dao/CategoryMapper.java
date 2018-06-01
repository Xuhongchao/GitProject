package cn.com.wisetrust.dao;

import cn.com.wisetrust.entity.Category;
import cn.com.wisetrust.entity.CategoryExample;
import java.util.List;

public interface CategoryMapper {
    int countByExample(CategoryExample example);

    int deleteByPrimaryKey(String id);

    int insert(Category record);

    int insertSelective(Category record);

    List<Category> selectByExample(CategoryExample example);

    Category selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
}