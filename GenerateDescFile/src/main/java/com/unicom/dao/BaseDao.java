package com.unicom.dao;

import org.apache.commons.dbutils.ResultSetHandler;

/**
 * 
 * @ClassName BaseDao
 * @Description 操作数据库的接口，可执行sql语句
 * @Reason
 * @date 2018年9月12日下午3:19:26
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public interface BaseDao {

	/**
	 * 执行sql进行查询
	 * 
	 * @param sql
	 *            被执行的sql语句
	 * @param rsh
	 *            The handler that converts the results into an object.
	 * @return T
	 */
	public <T> T executeQuery(String sql, ResultSetHandler<T> rsh);

	/**
	 * 执行sql进行查询
	 * 
	 * @param sql
	 *            被执行的sql语句
	 * @param rsh
	 *            The handler that converts the results into an object.
	 * @param params
	 *            可变参数
	 * @return T
	 */
	public <T> T executeQuery(String sql, ResultSetHandler<T> rsh, Object... params);

	/**
	 * 进行update、insert、delete操作
	 * 
	 * @param sql
	 *            被执行的sql语句
	 * @param params
	 *            参数
	 * @return 返回受影响的行
	 * @throws SQLException
	 */
	// public int executeUpdate(String sql, Object[] params);

	/**
	 * 进行update、insert、delete操作
	 * 
	 * @param sql
	 *            被执行的sql语句
	 * @return 返回受影响的行
	 * @throws SQLException
	 */
	// public int executeUpdate(String sql);

}
