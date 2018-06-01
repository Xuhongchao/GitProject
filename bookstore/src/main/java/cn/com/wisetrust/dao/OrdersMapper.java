package cn.com.wisetrust.dao;

import cn.com.wisetrust.entity.Orders;
import cn.com.wisetrust.entity.OrdersExample;
import java.util.List;

public interface OrdersMapper {
    int countByExample(OrdersExample example);

    int deleteByPrimaryKey(String id);

    int insert(Orders record);

    int insertSelective(Orders record);

    List<Orders> selectByExample(OrdersExample example);

    Orders selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);
}