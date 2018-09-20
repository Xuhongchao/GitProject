package com.unicom.dao;

import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName AnalysisDatabaseStructure
 * @Description
 * @Reason
 * @date 2018年9月12日下午2:42:51
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public interface AnalysisDatabaseStructureDao extends BaseDao {

	/**
	 * 获取某个实例下的所有的数据库名字
	 * 
	 * @return Object[]
	 */
	public List<Object[]> getDBs();

	/**
	 * 获取实例下所有数据库的所有的表信息;
	 * 
	 * @return List< Map< String,Object>>，其中Map<String,
	 *         Object>中的string代表column（列名）,object代表value（列值）, 每一行数据存储在一个list中
	 */
	public List<Map<String, Object>> getTables();

	/**
	 * 获取某个数据库下所有表的信息
	 * 
	 * @param db
	 *            数据库名
	 *
	 * @return List< Map< String,Object>>，其中Map<String,
	 *         Object>中的string代表column（列名）,object代表value（列值）, 每一行数据存储在一个list中
	 */
	public List<Map<String, Object>> getTablesBydbname(String db);

	/**
	 * 获取某张表的结构信息，包括COLUMN_NAME（字段名）, COLUMN_TYPE（字段类型）, IS_NULLABLE（是否为空）,
	 * COLUMN_COMMENT(字段描述)、 COLUMN_KEY（索引）
	 * 
	 * @param tableName
	 *            表名
	 * @param db
	 *            数据库名
	 * @return
	 */
	public List<Map<String, Object>> getTableFields(String tableName, String db);

}
