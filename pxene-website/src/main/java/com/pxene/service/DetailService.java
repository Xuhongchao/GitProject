package com.pxene.service;

import java.util.List;

import com.pxene.form.AppDetailForm;
import com.pxene.model.UrlDetail;

public interface DetailService {

	List<UrlDetail> findAll();

	void insert(UrlDetail detail);

	UrlDetail update(UrlDetail detail);

	int delete(int id);

	List<UrlDetail> queryByForm(AppDetailForm appDetailForm);

	String findByCode(String code);
	
}
