package com.unicom.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName JDBCUtil
 * @Description 数据库链接工具类(获取数据库链接,关闭数据库链接)
 * @Reason
 * @date 2018年9月10日上午11:47:36
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version v1
 * @since JDK 1.8
 */
public class JDBCUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(JDBCUtil.class);

	private final String DRIVER = "com.mysql.jdbc.Driver";
	private String URL;
	private String USERNAME;
	private String PASSWORD;

	private final String DBCPDRIVER = "jdbc:apache:commons:dbcp:";
	private final String POOLNAME = "MysqlPool"; // 连接池名称
	private GenericObjectPool connectionPool = null; // 数据库连接池

	public JDBCUtil(String url, String username, String password) {
		this.URL = url;
		this.USERNAME = username;
		this.PASSWORD = password;
	}

	/**
	 * 加载MYSQL驱动程序
	 */
	private synchronized void initDataSource() {
		LOGGER.info("加载MYSQL驱动程序");
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			LOGGER.error("加载MYSQL驱动程序失败!", e);
		}
	}

	/**
	 * 获取数据库链接
	 * 
	 * @return 数据库链接Connection
	 * @throws ClassNotFoundException
	 */
	public synchronized Connection getConnection() {
		Connection conn = null;
		if (connectionPool == null)
			startPool();

		try {
			conn = DriverManager.getConnection(DBCPDRIVER + POOLNAME);
		} catch (SQLException e) {
			LOGGER.error("获取数据库链接失败!", e);
			throw new RuntimeException("获取数据库链接失败!");
		}
		return conn;
	}

	/**
	 * 关闭连接
	 * 
	 * @throws SQLException
	 */
	public void close(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("关闭数据库链接失败!", e);
		}
	}

	/**
	 * 启动数据库连接池
	 */
	private void startPool() {
		initDataSource();
		if (connectionPool != null) {
			shutdownPool();
		}

		LOGGER.info("开启数据库连接池");
		try {
			connectionPool = new GenericObjectPool();
			ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(URL, USERNAME, PASSWORD);
			PoolableConnectionFactory pConnectionFactory = new PoolableConnectionFactory(connectionFactory,
					connectionPool, null, null, false, true);

			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			PoolingDriver driver = (PoolingDriver) DriverManager.getDriver(DBCPDRIVER);

			driver.registerPool(POOLNAME, pConnectionFactory.getPool());
		} catch (ClassNotFoundException e) {
			LOGGER.error("开启数据库连接池失败", e);
		} catch (SQLException e) {
			LOGGER.error("开启数据库连接池失败", e);
		}
	}

	/**
	 * 关闭数据库连接池
	 */
	private void shutdownPool() {
		LOGGER.info("关闭数据库连接池");
		try {
			PoolingDriver driver = (PoolingDriver) DriverManager.getDriver(DBCPDRIVER);
			driver.closePool(POOLNAME);
		} catch (SQLException e) {
			LOGGER.error("关闭数据库连接池失败", e);
		}
	}
}
