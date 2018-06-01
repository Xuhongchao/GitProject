package com.wisetrust.pojo;

public class CityData extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String cityId;

	private String name;

	private String provinceId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
}