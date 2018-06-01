package com.wisetrust.pojo;

public class ProvinceData extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String provinceId;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}