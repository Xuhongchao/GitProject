package com.pxene.service;

import java.util.List;

import com.pxene.model.App;

public interface AppService {
	
	String findByCode(App app);
	

	List<App> queryByCode(String code);
	
	int add(App app);
	
	App update(App app);

	int delete(int id);

	List<App> findByPath(int pathType);


	List<App> findAll(int type);

	
}
