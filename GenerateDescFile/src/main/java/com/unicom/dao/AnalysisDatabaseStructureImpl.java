package com.unicom.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.unicom.utils.JDBCUtil;

/**
 * 
 * @ClassName AnalysisDatabaseStructure
 * @Description 分析数据库中的所有表结构，获取库中所有表中需要的信息，并将表的信息存放到TableEntity中，
 *              将表中字段的信息放到FieldEntity中
 * @Reason
 * @date 2018年9月11日下午3:24:29
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public class AnalysisDatabaseStructureImpl extends BaseDaoImpl implements AnalysisDatabaseStructureDao {

	public AnalysisDatabaseStructureImpl(JDBCUtil jdbcUtil) {
		super(jdbcUtil);
	}

	@Override
	public List<Object[]> getDBs() {
		return executeQuery(
				"SELECT SCHEMA_NAME FROM information_schema.SCHEMATA " + "WHERE 1=1 AND SCHEMA_NAME <> 'mysql' "
						+ "AND SCHEMA_NAME <> 'information_schema' AND SCHEMA_NAME <> 'performance_schema'"
						+ " AND SCHEMA_NAME <> 'sys';",
				new ArrayListHandler());
	}

	@Override
	public List<Map<String, Object>> getTables() {
		return executeQuery(
				"SELECT TABLE_SCHEMA, TABLE_NAME, TABLE_TYPE, CREATE_TIME, TABLE_COMMENT FROM information_schema.TABLES"
						+ " WHERE 1=1 AND TABLE_SCHEMA <> 'mysql' AND TABLE_SCHEMA <> 'information_schema' AND TABLE_SCHEMA <> 'performance_schema' AND TABLE_SCHEMA <> 'sys'; ",
				new MapListHandler());
	}

	@Override
	public List<Map<String, Object>> getTablesBydbname(String db) {
		return executeQuery(
				"SELECT TABLE_SCHEMA,TABLE_NAME, TABLE_TYPE, CREATE_TIME, TABLE_COMMENT FROM information_schema.TABLES"
						+ " WHERE 1=1 AND TABLE_SCHEMA = ? ;",
				new MapListHandler(), db);
	}

	@Override
	public List<Map<String, Object>> getTableFields(String tableName, String db) {
		return executeQuery(
				"SELECT COLUMN_NAME, IS_NULLABLE, COLUMN_TYPE, COLUMN_COMMENT, COLUMN_KEY FROM information_schema.COLUMNS "
						+ " WHERE 1=1 AND table_name = ? AND table_schema = ? ;",
				new MapListHandler(), tableName, db);
	}

}
