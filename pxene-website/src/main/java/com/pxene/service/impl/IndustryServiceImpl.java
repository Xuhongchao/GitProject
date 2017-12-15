package com.pxene.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aj.org.objectweb.asm.Type;

import com.pxene.dao.IndustryMapper;
import com.pxene.model.Industry;
import com.pxene.model.IndustryExample;
import com.pxene.model.IndustryExample.Criteria;
import com.pxene.service.IndustryService;

@Service("industryService")
public class IndustryServiceImpl implements IndustryService {
    @Autowired
    private IndustryMapper industryMapper;

    @Override
    public List<Industry> findAll() {
        IndustryExample example = new IndustryExample();
        example.setOrderByClause("datetime desc");
        return industryMapper.selectByExample(example);
    }

    @Override
    public int add(Industry industry) {
        return industryMapper.insert(industry);
    }

    @Override
    public Industry update(Industry industry) {
        industryMapper.updateByPrimaryKey(industry);
        return industry;
    }

    @Override
    public int delete(int id) {
        // TODO Auto-generated method stub
        return industryMapper.deleteByPrimaryKey(id);

    }

    @Override
    public String findByCode(String code) {
        IndustryExample example = new IndustryExample();
        example.createCriteria().andCodeEqualTo(code);
        List<Industry> list = industryMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0).getCode();
        }
        return "";
    }

    @Override
    public List<Industry> findByType(int type) {
        IndustryExample example = new IndustryExample();
        example.setOrderByClause("datetime desc");
        Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(type);
        return industryMapper.selectByExample(example);
    }


}
