package com.pxene.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import com.pxene.util.IOUtil;

/**
 * Created by @author xu on @date 2017年 12月 24日 下午2:24:37
 * 
 * 加载即将要录入的url
 */
public class LoadNotEnterUrl {
	private IOUtil ioUtil = IOUtil.getInstance();
	private XSSFSheet sheet = ioUtil.getWb().getSheetAt(1); // 待录入的url在同一个excel的第二个sheet上

	public List<String> getNotEnterUrl() {
		LinkedList<String> list = new LinkedList<String>();
		// 得到工作表的第一行和最后一行
		int firstLine = sheet.getFirstRowNum();
		int endLine = sheet.getLastRowNum();

		for (int i = firstLine; i <= endLine; i++) {
			Row row = sheet.getRow(i);// 得到每一行
			// 得到每一个单元格cell
			Cell url = row.getCell(0, Row.CREATE_NULL_AS_BLANK);

			list.add(ioUtil.turn(url));
		}
		return list;
	}
}
