package com.unicom.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unicom.utils.JDBCUtil;

/**
 * 
 * @ClassName BaseDaoImpl
 * @Description
 * @Reason
 * @date 2018年9月12日下午3:24:58
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public abstract class BaseDaoImpl implements BaseDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);

	private JDBCUtil jdbcUtil;

	public BaseDaoImpl(JDBCUtil jdbcUtil) {
		this.jdbcUtil = jdbcUtil;
	}

	@Override
	public <T> T executeQuery(String sql, ResultSetHandler<T> rsh) {
		Connection connection = jdbcUtil.getConnection();
		QueryRunner query = new QueryRunner();
		T result = null;
		try {
			result = query.query(connection, sql, rsh);
		} catch (SQLException e) {
			LOGGER.error("执行sql语句出错，sql语句是：" + sql);
		} finally {
			jdbcUtil.close(connection);
		}
		return result;
	}

	@Override
	public <T> T executeQuery(String sql, ResultSetHandler<T> rsh, Object... params) {
		Connection connection = jdbcUtil.getConnection();
		QueryRunner query = new QueryRunner();
		T result = null;
		try {
			result = query.query(connection, sql, rsh, params);
		} catch (SQLException e) {
			LOGGER.error("执行sql语句出错，sql语句是：" + sql + ",参数是：" + Arrays.asList(params).toString());
		} finally {
			jdbcUtil.close(connection);
		}
		return result;
	}

}
