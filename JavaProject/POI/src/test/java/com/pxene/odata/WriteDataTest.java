package com.pxene.odata;

import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

/**
 * Created by @author xuhongchao on @date 2017�� �� �� ����7:45:53
 */

public class WriteDataTest {
	@Test
	public void test1() throws Exception {
		// ����������---->XSSF����10���Excel(HSSF��03���Excel)
		XSSFWorkbook wb = new XSSFWorkbook();
		// ������
		XSSFSheet sheet = wb.createSheet("��Ա�б�");
		// ��ͷ�У������һ��
		XSSFRow header = sheet.createRow(0);
		// ������Ԫ��0�����һ�е�һ��
		XSSFCell cell0 = header.createCell(0);
		cell0.setCellValue("��Ա����");
		header.createCell(1).setCellValue("��Ա���");
		header.createCell(2).setCellValue("��Ա����");
		header.createCell(3).setCellValue("�Ƽ��˱��");
		header.createCell(4).setCellValue("�����˱��");
		header.createCell(5).setCellValue("��ַ���");
		header.createCell(6).setCellValue("ע��ʱ��");
		// �����еĿ��
		/*
		 * // getPhysicalNumberOfCells()���������ж��ٰ������ݵ��� for (int i = 0; i <
		 * header.getPhysicalNumberOfCells(); i++) { //
		 * POI�����п��ʱ�Ƚ����⣬���Ļ�����λ��1/255���ַ���С�� // �������Ҫ�������ܹ�ʢ����20���ַ��Ļ�������Ҫ��255*20
		 * sheet.setColumnWidth(i, 255 * 20); }
		 */
		// �����иߣ��иߵĵ�λ�������أ����30����30���ص���˼
		header.setHeightInPoints(30);
		// �������ú������ݣ����ǵ�Ȼ��Ҫ�����ĳ���ļ��ģ��������Ҫ�������
		FileOutputStream fos = new FileOutputStream(
				"C:\\Users\\xuhongchao\\Desktop\\2010.xlsx");
		// ��ָ���ļ�д������
		wb.write(fos);
		fos.close();
	}
}
