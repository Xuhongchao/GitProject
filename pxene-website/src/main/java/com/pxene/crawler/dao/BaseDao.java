package com.pxene.crawler.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pxene.utils.JDBCTools;
/**
 * 数据库访问层
 * @author zhuzi
 *
 */
public class BaseDao {

	/**
	 * INSERT, UPDATE, DELETE 操作都可以使用这个方法
	 * 
	 * @param sql	sql语句
	 * @param args	sql语句中需要的参数
	 */
	public Integer update(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int executeUpdate=0;
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}

			 executeUpdate = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, preparedStatement, connection);
		}
		return executeUpdate;
	}

	/**
	 * 查询一条记录, 返回SQL语句查询到的记录对应的Class类的对象
	 * 
	 * @param clazz	实体类
	 * @param sql	SQL语句
	 * @param args	SQL语句需要的参数
	 * @return	Class类的对象
	 */
	public <T> T get(Class<T> clazz, String sql, Object... args) {
		List<T> result = getForList(clazz, sql, args);
		if (result.size() > 0) {
			return result.get(0);
		}

		return null;
	}

	/**
	 * 传入 SQL语句和 Class对象, 返回 SQL语句查询到的记录对应的 Class类的对象的集合
	 * 
	 * @param clazz	对象的类型
	 * @param sql	SQL语句
	 * @param args	SQL语句需要的参数
	 * @return	Class类的对象
	 */
	public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {

		List<T> list = new ArrayList<T>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// 得到结果集
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}

			resultSet = preparedStatement.executeQuery();

			// 处理结果集,得到Map的List,其中一个Map对象就是一条记录.Map的key为reusltSet中列的别名,Map的value为列的值.
			List<Map<String, Object>> values = handleResultSetToMapList(resultSet);

			// 把Map的List转为clazz对应的List(其中Map的 key即为clazz对应的对象的propertyName,而Map的value即为clazz对应的对象的propertyValue)
			list = transfterMapListToBeanList(clazz, values);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(resultSet, preparedStatement, connection);
		}

		return list;
	}

	/**
	 * 转换List<Map> 为 List<T>
	 * 
	 * @param clazz	实体类
	 * @param values 数据库中查询出来的数据集				
	 * @return	实体类的集合
	 * @throws Exception
	 */
	public <T> List<T> transfterMapListToBeanList(Class<T> clazz,
			List<Map<String, Object>> values) throws Exception {

		List<T> result = new ArrayList<T>();

		T bean = null;

		if (values.size() > 0) {
			for (Map<String, Object> m : values) {
				// 通过反射创建一个其他类的对象
				bean = clazz.newInstance();

				for (Map.Entry<String, Object> entry : m.entrySet()) {
					String propertyName = entry.getKey();
					Object value = entry.getValue();

					Field f = bean.getClass().getDeclaredField(propertyName);
					f.setAccessible(true);
					f.set(bean, value);

				}
				// 把 Object 对象放入到 list 中.
				result.add(bean);
			}
		}

		return result;
	}

	
	/**
	 * 处理结果集,得到 Map的一个List,其中一个Map对象对应一条记录
	 * 
	 * @param resultSet 数据结果集
	 * @return	由数据结果集封装成的List<Map<String,Object>>
	 * @throws SQLException
	 */
	public List<Map<String, Object>> handleResultSetToMapList(
			ResultSet resultSet) throws SQLException {
		// 准备一个 List<Map<String, Object>> 键: 存放列的别名, 值: 存放列的值. 其中一个 Map对象对应着一条记录
		List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();

		List<String> columnLabels = getColumnLabels(resultSet);
		Map<String, Object> map = null;

		// 处理 ResultSet,使用 while循环
		while (resultSet.next()) {
			map = new HashMap<String, Object>();
			for (String columnLabel : columnLabels) {
				Object value = resultSet.getObject(columnLabel);
				map.put(columnLabel, value);
			}

			// 把一条记录的一个Map对象放入准备的List中
			values.add(map);
		}
		return values;
	}
	

	/**
	 * 获取结果集ColumnLabel对应的List
	 * 
	 * @param rs 数据结果集
	 * @return	结果集ColumnLabel对应的List
	 * @throws SQLException
	 */
	private List<String> getColumnLabels(ResultSet rs) throws SQLException {
		List<String> labels = new ArrayList<String>();

		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 0; i < rsmd.getColumnCount(); i++) {
			labels.add(rsmd.getColumnLabel(i + 1));
		}

		return labels;
	}
	

	/**
	 * 返回某条记录的某一个字段的值 或 一个统计的值(一共有多少条记录等.)
	 * 
	 * @param sql SQL语句
	 * @param args	SQL语序需要的参数
	 * @return	返回SQL语句的查询结果
	 */
	public <E> E getForValue(String sql, Object... args) {

		// 得到结果集: 该结果集应该只有一行, 且只有一列
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// 得到结果集
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return (E) resultSet.getObject(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JDBCTools.releaseDB(resultSet, preparedStatement, connection);
		}

		return null;
	}

}