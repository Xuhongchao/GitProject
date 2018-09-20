package com.unicom.entity;

import java.io.Serializable;

/**
 * 
 * @ClassName FieldEntity
 * @Description 表字段实体
 * @Reason
 * @date 2018年9月11日下午7:47:32
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public class FieldEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7948231051352551419L;
	private String name; // 字段名
	private String type; // 字段类型
	private String comment; // 字段描述
	private String nullable;// 字段是否为空
	private String key; // 索引

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		if ("PRI".equals(key)) {
			this.key = "主键索引";
		} else if ("MUL".equals(key)) {
			this.key = "是";
		} else {
			this.key = "";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nullable == null) ? 0 : nullable.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		FieldEntity other = (FieldEntity) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nullable == null) {
			if (other.nullable != null)
				return false;
		} else if (!nullable.equals(other.nullable))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FieldEntity [name=" + name + ", type=" + type + ", comment=" + comment + ", nullable=" + nullable
				+ ", key=" + key + "]";
	}

}
