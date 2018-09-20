package com.unicom.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @ClassName TableEntity
 * @Description 表实体类
 * @Reason
 * @date 2018年9月11日下午7:46:22
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public class TableEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3180280272072327099L;
	private String tableName; // 表名
	private String tableType; // 类型：base_table（基础表）和view（视图）
	private String createTime; // 表创建时间
	private String tableComment; // 表描述
	private List<FieldEntity> fields = new ArrayList<>(); // 表中所有字段信息
	private DatabaseEntity databaseEntity;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		if ("".equals(createTime.trim()) || createTime == null || "null".equals(createTime)) {
			this.createTime = createTime;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			try {
				date = (Date) sdf.parse(createTime);
				this.createTime = sdf.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	public List<FieldEntity> getFields() {
		return fields;
	}

	public void setFields(List<FieldEntity> fields) {
		this.fields = fields;
	}

	public DatabaseEntity getDatabaseEntity() {
		return databaseEntity;
	}

	public void setDatabaseEntity(DatabaseEntity databaseEntity) {
		this.databaseEntity = databaseEntity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((databaseEntity == null) ? 0 : databaseEntity.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableEntity other = (TableEntity) obj;
		if (databaseEntity == null) {
			if (other.databaseEntity != null)
				return false;
		} else if (!databaseEntity.equals(other.databaseEntity))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TableEntity [tableName=" + tableName + ", tableType=" + tableType + ", createTime=" + createTime
				+ ", tableComment=" + tableComment + ", fields=" + fields + ", databaseEntity=" + databaseEntity + "]";
	}

}
