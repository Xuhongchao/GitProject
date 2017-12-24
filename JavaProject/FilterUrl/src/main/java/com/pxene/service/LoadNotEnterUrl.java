package com.pxene.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import com.pxene.util.IOUtil;

/**
 * Created by @author xu on @date 2017�� 12�� 24�� ����2:24:37
 * 
 * ���ؼ���Ҫ¼���url
 */
public class LoadNotEnterUrl {
	private IOUtil ioUtil = IOUtil.getInstance();
	private XSSFSheet sheet = ioUtil.getWb().getSheetAt(1); // ��¼���url��ͬһ��excel�ĵڶ���sheet��

	public List<String> getNotEnterUrl() {
		LinkedList<String> list = new LinkedList<String>();
		// �õ�������ĵ�һ�к����һ��
		int firstLine = sheet.getFirstRowNum();
		int endLine = sheet.getLastRowNum();

		for (int i = firstLine; i <= endLine; i++) {
			Row row = sheet.getRow(i);// �õ�ÿһ��
			// �õ�ÿһ����Ԫ��cell
			Cell url = row.getCell(0, Row.CREATE_NULL_AS_BLANK);

			list.add(ioUtil.turn(url));
		}
		return list;
	}
}
