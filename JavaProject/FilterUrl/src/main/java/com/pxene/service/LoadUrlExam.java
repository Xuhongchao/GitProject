package com.pxene.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.pxene.util.IOUtil;

/**
 * Created by @author xuhongchao on @date 2017年 月 日 上午11:44:37
 * 
 * 加载所有的url
 */

public class LoadUrlExam {
	private IOUtil ioUtil = IOUtil.getInstance();
	private XSSFSheet sheet = ioUtil.getWb().getSheetAt(0);
	private Map<String, String> map = new HashMap<String, String>();

	/**
	 * 按照num-url的格式加载所有的url
	 * 
	 * @return map集合
	 */
	public Map<String, String> getAllUrl() {
		// 得到工作表的第一行和最后一行
		int firstLine = sheet.getFirstRowNum();
		int endLine = sheet.getLastRowNum();

		for (int i = firstLine; i <= endLine; i++) {
			Row row = sheet.getRow(i);// 得到每一行
			// 得到每一个单元格cell
			Cell num = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
			Cell urlExam = row.getCell(4, Row.CREATE_NULL_AS_BLANK);

			map.put(ioUtil.turn(num), ioUtil.turn(urlExam));
		}
		return map;
	}
}
