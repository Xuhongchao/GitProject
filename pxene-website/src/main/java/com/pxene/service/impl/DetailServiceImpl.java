package com.pxene.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pxene.dao.UrlDetailMapper;
import com.pxene.form.AppDetailForm;
import com.pxene.model.App;
import com.pxene.model.UrlDetail;
import com.pxene.model.UrlDetailExample;
import com.pxene.model.UrlDetailExample.Criteria;
import com.pxene.service.DetailService;

@Service
public class DetailServiceImpl implements DetailService {
    @Autowired
    private UrlDetailMapper detailMapper;

    @Override
    public List<UrlDetail> findAll() {
        return detailMapper.selectByExample(null);
    }

    @Override
    public void insert(UrlDetail detail) {

        detailMapper.insert(detail);
    }

    @Override
    public UrlDetail update(UrlDetail detail) {

        detailMapper.updateByPrimaryKey(detail);
        return detail;
    }

    @Override
    public int delete(int id) {
        // TODO Auto-generated method stub
        return detailMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<UrlDetail> queryByForm(AppDetailForm appDetailForm) {


        return detailMapper.queryByExample(appDetailForm);
    }

    @Override
    public String findByCode(String code) {
        UrlDetailExample appExample = new UrlDetailExample();
        Criteria criteria = appExample.createCriteria();
        criteria.andUrlcodeEqualTo(code);
        List<UrlDetail> list = detailMapper.selectByExample(appExample);
        if (list != null && list.size() > 0) {
            return list.get(0).getUrlcode();
        }
        return "";
    }

}
