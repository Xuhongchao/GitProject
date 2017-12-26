package com.pxene.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.pxene.entity.Data;
import com.pxene.util.IOUtil;

/**
 * Created by @author xuhongchao on @date 2017年 月 日 上午10:28:04
 * 
 * 按照kv的格式来加载一行数据到map中，形成字典，以便通过k来找值
 */

public class LoadLine {
	private IOUtil ioUtil = IOUtil.getInstance();
	private XSSFSheet sheet = ioUtil.getWb().getSheetAt(0);
	private Map<String, Data> map = new HashMap<String, Data>();

	/**
	 * 从Excel中读取一行数据，按照num-data(除num的其他部分)存放到map中
	 * 
	 * @return map集合，其中key为num，value为data对象
	 */
	public Map<String, Data> readAll() {
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
			 * if ("NULL".equals(ioUtil.turn(paramReg)) &&
			 * ".*".equals(ioUtil.turn(urlReg))) { continue; }
			 */

			// 得到每一个单元格的数据并封装到data对象中
			data.setDomain(ioUtil.turn(domain));
			data.setParam(ioUtil.turn(paramReg));
			data.setUrlReg(ioUtil.turn(urlReg));
			data.setUrlExam(ioUtil.turn(urlExam));

			map.put(ioUtil.turn(num), data);
		}
		return map;
	}

}
