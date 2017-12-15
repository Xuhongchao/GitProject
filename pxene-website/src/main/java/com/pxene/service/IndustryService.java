package com.pxene.service;

import java.util.List;

import com.pxene.model.Industry;

public interface IndustryService {
	List<Industry> findAll();

	int add(Industry industry);

	Industry update(Industry industry);

	int delete(int id);

	String findByCode(String code);

	List<Industry> findByType(int type);
}
