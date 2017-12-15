package com.pxene.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pxene.dao.AppMapper;
import com.pxene.dao.IndustryMapper;
import com.pxene.model.App;
import com.pxene.model.AppExample;
import com.pxene.model.IndustryExample;
import com.pxene.model.AppExample.Criteria;
import com.pxene.service.AppService;

@Service("appService")
public class AppServiceImpl implements AppService {

    @Autowired
    private AppMapper appMapper;

//	@Override
//	public App findByCode(int id) {
//		return appMapper.selectByPrimaryKey(id);
//	}


    @Override
    public List<App> queryByCode(String code) {
        AppExample appExample = new AppExample();
        Criteria criteria = appExample.createCriteria();
        criteria.andCodeEqualTo(code);
        return appMapper.selectByExample(appExample);
    }


    @Override
    public int add(App app) {

        return appMapper.insert(app);

    }

    @Override
    public App update(App app) {
        appMapper.updateByPrimaryKey(app);
        return app;
    }

    @Override
    public int delete(int id) {
        return appMapper.deleteByPrimaryKey(id);
    }

    @Override
    public String findByCode(App app) {

        List<App> list = appMapper.queryByExample(app);
        if (list != null && list.size() > 0) {
            return list.get(0).getCode();
        }
        return "";

    }

    @Override
    public List<App> findByPath(int pathType) {

        AppExample example = new AppExample();
        example.createCriteria().andTypeEqualTo(pathType);
        example.setOrderByClause("datetime desc");
        return appMapper.selectByExample(example);
    }


    @Override
    public List<App> findAll(int type) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", String.valueOf(type));
        return appMapper.findAll(map);
    }


}
