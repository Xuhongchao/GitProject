package com.unicom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unicom.dao.AnalysisDatabaseStructureImpl;
import com.unicom.entity.DatabaseEntity;
import com.unicom.entity.FieldEntity;
import com.unicom.entity.TableEntity;
import com.unicom.utils.JDBCUtil;

/**
 * 
 * @ClassName AnalysisDatabaseStructureService
 * @Description 装填Table实体类
 * @Reason
 * @date 2018年9月12日下午7:44:57
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public class GetMessageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(GetMessageService.class);

	private JDBCUtil jdbcUtil;
	private AnalysisDatabaseStructureImpl adsImpl;

	public GetMessageService(JDBCUtil jdbcUtil) {
		this.jdbcUtil = jdbcUtil;
		adsImpl = new AnalysisDatabaseStructureImpl(this.jdbcUtil);
	}

	/**
	 * 获取整个实例中全部库的全部表的信息
	 * 
	 * @return TableEntity集合
	 */
	public List<TableEntity> setTableEntity() {
		List<TableEntity> tableEntities = null;
		try {
			List<Map<String, Object>> tables = adsImpl.getTables();
			tableEntities = parseTablesMap(tables);
			for (TableEntity entity : tableEntities) {
				String tableName = entity.getTableName();
				entity.setFields(
						parseTableFieldsMap(adsImpl.getTableFields(tableName, entity.getDatabaseEntity().getDbName())));
			}
		} catch (Exception e) {
			LOGGER.error("AnalysisDatabaseStructureService服务的setTableEntity出现错误", e);
			throw new RuntimeException("获取数据出错");
		}
		return tableEntities;
	}

	/**
	 * 获取指定库中全部表的信息
	 * 
	 * @param db
	 *            数据库名
	 * 
	 * @return TableEntity集合
	 */
	public List<TableEntity> setTableEntity(String db) {
		List<TableEntity> tableEntities = null;
		try {
			tableEntities = parseTablesMap(adsImpl.getTablesBydbname(db));
			for (TableEntity entity : tableEntities) {
				String tableName = entity.getTableName();
				entity.setFields(
						parseTableFieldsMap(adsImpl.getTableFields(tableName, entity.getDatabaseEntity().getDbName())));
			}
		} catch (Exception e) {
			LOGGER.error("AnalysisDatabaseStructureService服务的setTableEntity(db)出现错误：", e);
			throw new RuntimeException("获取数据出错");
		}
		return tableEntities;
	}

	/**
	 * 解析装有表字段信息的List< Map< String,Object>>，map中的 string值有COLUMN_NAME（列名）,
	 * IS_NULLABLE（是否为空）, COLUMN_TYPE（字段类型）, COLUMN_COMMENT（字段描述）,
	 * COLUMN_KEY（索引信息）
	 * 
	 * @param fields
	 *            存放字段信息的集合
	 * @return List<FieldEntity>
	 */
	private List<FieldEntity> parseTableFieldsMap(List<Map<String, Object>> fields) throws Exception {
		List<FieldEntity> fieldEntities = new ArrayList<>();
		FieldEntity fieldEntity = null;

		for (int i = 0; i < fields.size(); i++) {
			Map<String, Object> fieldMap = fields.get(i);
			fieldEntity = new FieldEntity();
			for (int j = 0; j < fieldMap.size(); j++) {
				fieldEntity.setName(fieldMap.get("COLUMN_NAME").toString());
				fieldEntity.setType(fieldMap.get("COLUMN_TYPE").toString());
				fieldEntity.setNullable(fieldMap.get("IS_NULLABLE") + "");
				fieldEntity.setComment(fieldMap.get("COLUMN_COMMENT") + "");
				fieldEntity.setKey(fieldMap.get("COLUMN_KEY") + "");
			}
			fieldEntities.add(fieldEntity);
		}
		return fieldEntities;
	}

	/**
	 * 解析装有表信息的List< Map< String,Object>>，map中的 string值有 TABLE_SCHEMA（数据库名）,
	 * TABLE_NAME（表名）, TABLE_TYPE（类型，视图/普通表）, CREATE_TIME(表创建时间),
	 * TABLE_COMMENT（表描述信息）
	 * 
	 * @param tables
	 *            数据库下所有的表状态信息集合
	 * @return List<TableEntity>
	 */
	private List<TableEntity> parseTablesMap(List<Map<String, Object>> tables) throws Exception {
		List<TableEntity> tableEntities = new ArrayList<>();
		TableEntity tableEntity = null;

		for (int i = 0; i < tables.size(); i++) {
			Map<String, Object> tableMap = tables.get(i);
			tableEntity = new TableEntity();
			for (int j = 0; j < tableMap.size(); j++) {
				tableEntity.setDatabaseEntity(new DatabaseEntity(tableMap.get("TABLE_SCHEMA").toString()));
				tableEntity.setTableName(tableMap.get("TABLE_NAME").toString());
				tableEntity.setTableComment(parseTableCommentInfo(tableMap.get("TABLE_COMMENT") + ""));
				tableEntity.setCreateTime(tableMap.get("CREATE_TIME") + "");
				tableEntity.setTableType(tableMap.get("TABLE_TYPE").toString());
			}
			tableEntities.add(tableEntity);
		}

		return tableEntities;
	}

	/**
	 * 后期table的comment信息会进行统一的制定，暂时先留出方法，后期进行具体的处理
	 * 
	 * @todo 具体的操作等comment信息确定下来再进行
	 * @param comment
	 *            表描述信息
	 * @return 解析后的信息
	 */
	private String parseTableCommentInfo(String comment) {
		/**
		 * @todo 进行更多操作
		 */
		return comment;
	}

}
