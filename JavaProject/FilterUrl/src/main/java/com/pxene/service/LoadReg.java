package com.pxene.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.pxene.entity.Data;
import com.pxene.util.IOUtil;

/**
 * Created by @author xuhongchao on @date 2017年 月 日 上午11:22:17
 * 
 * 读取所有数据到data对象中，主要从中拿出正则部分
 */

public class LoadReg {
	private IOUtil ioUtil = IOUtil.getInstance();
	private XSSFSheet sheet = ioUtil.getWb().getSheetAt(0);
	private List<Data> list = new ArrayList<Data>();

	/**
	 * 读取所有数据到data对象中，不包含param为NULL并且urlReg为.*的情况
	 * 
	 * @throws IOException
	 */
	public List<Data> readFromExcelToData() {
		// 得到工作表的第一行和最后一行
		int firstLine = sheet.getFirstRowNum();
		int endLine = sheet.getLastRowNum();

		for (int i = firstLine; i <= endLine; i++) {
			// 每行的读取都单独创建一个data对象
			Data data = new Data();
			Row row = sheet.getRow(i);// 得到每一行

			// 得到每一个单元格cell
			Cell num = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
			Cell domain = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
			Cell paramReg = row.getCell(2, Row.CREATE_NULL_AS_BLANK);
			Cell urlReg = row.getCell(3, Row.CREATE_NULL_AS_BLANK);
			Cell urlExam = row.getCell(4, Row.CREATE_NULL_AS_BLANK);

			/*
			 * 注释原因：在这里过滤掉了url正则为'.*'并且参数正则为NULL情况的url
			 * 
			 * 
			 * if ("NULL".equals(paramReg) && ".*".equals(urlReg)) {
			 * System.out.println("检验结果：" + ioUtil.turn(paramReg) + "-" +
			 * ioUtil.turn(urlReg)); }
			 */

			// 得到每一个单元格的数据并封装到data对象中
			data.setNum(ioUtil.turn(num));
			data.setDomain(ioUtil.turn(domain));
			if ("NULL".equals(ioUtil.turn(paramReg))) {
				data.setParam(".");
			} else {
				data.setParam(ioUtil.turn(paramReg));
			}
			data.setUrlReg(ioUtil.turn(urlReg));
			data.setUrlExam(ioUtil.turn(urlExam));

			// System.out.println(data.toString());

			list.add(data);
		}
		return list;
	}
}
