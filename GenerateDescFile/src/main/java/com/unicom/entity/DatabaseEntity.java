package com.unicom.entity;

import java.io.Serializable;

/**
 * 
 * @ClassName DatabaseEntity
 * @Description 数据库实体类
 * @Reason
 * @date 2018年9月17日下午3:48:07
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public class DatabaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6052598239153889944L;

	private String dbName;

	public DatabaseEntity() {
		super();
	}

	public DatabaseEntity(String dbName) {
		super();
		this.dbName = dbName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	@Override
	public String toString() {
		return "DatabaseEntity [dbName=" + dbName + "]";
	}

}
