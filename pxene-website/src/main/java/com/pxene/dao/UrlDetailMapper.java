package com.pxene.dao;

import com.pxene.form.AppDetailForm;
import com.pxene.model.UrlDetail;
import com.pxene.model.UrlDetailExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UrlDetailMapper {
    int countByExample(UrlDetailExample example);

    int deleteByExample(UrlDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UrlDetail record);

    int insertSelective(UrlDetail record);

    List<UrlDetail> selectByExample(UrlDetailExample example);

    UrlDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UrlDetail record, @Param("example") UrlDetailExample example);

    int updateByExample(@Param("record") UrlDetail record, @Param("example") UrlDetailExample example);

    int updateByPrimaryKeySelective(UrlDetail record);

    int updateByPrimaryKey(UrlDetail record);

    List<UrlDetail> queryByExample(AppDetailForm appDetailForm);
}