package cn.com.wisetrust.dao;

import cn.com.wisetrust.entity.OrderItem;
import cn.com.wisetrust.entity.OrderItemExample;
import java.util.List;

public interface OrderItemMapper {
    int countByExample(OrderItemExample example);

    int deleteByPrimaryKey(String id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    List<OrderItem> selectByExample(OrderItemExample example);

    OrderItem selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
}