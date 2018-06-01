package cn.com.wisetrust.dao;

import cn.com.wisetrust.entity.Book;
import cn.com.wisetrust.entity.BookExample;
import java.util.List;

public interface BookMapper {
	int countByExample(BookExample example);

	int deleteByPrimaryKey(String id);

	int insert(Book record);

	int insertSelective(Book record);

	List<Book> selectByExample(BookExample example);

	Book selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(Book record);

	int updateByPrimaryKey(Book record);
}