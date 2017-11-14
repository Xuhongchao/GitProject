package com.pxene.odata;

import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

/**
 * Created by @author xuhongchao on @date 2017年 月 日 下午7:45:53
 */

public class WriteDataTest {
	@Test
	public void test1() throws Exception {
		// 创建工作簿---->XSSF代表10版的Excel(HSSF是03版的Excel)
		XSSFWorkbook wb = new XSSFWorkbook();
		// 工作表
		XSSFSheet sheet = wb.createSheet("会员列表");
		// 标头行，代表第一行
		XSSFRow header = sheet.createRow(0);
		// 创建单元格，0代表第一行第一列
		XSSFCell cell0 = header.createCell(0);
		cell0.setCellValue("会员级别");
		header.createCell(1).setCellValue("会员编号");
		header.createCell(2).setCellValue("会员姓名");
		header.createCell(3).setCellValue("推荐人编号");
		header.createCell(4).setCellValue("负责人编号");
		header.createCell(5).setCellValue("地址编号");
		header.createCell(6).setCellValue("注册时间");
		// 设置列的宽度
		/*
		 * // getPhysicalNumberOfCells()代表这行有多少包含数据的列 for (int i = 0; i <
		 * header.getPhysicalNumberOfCells(); i++) { //
		 * POI设置列宽度时比较特殊，它的基本单位是1/255个字符大小， // 因此我们要想让列能够盛的下20个字符的话，就需要用255*20
		 * sheet.setColumnWidth(i, 255 * 20); }
		 */
		// 设置行高，行高的单位就是像素，因此30就是30像素的意思
		header.setHeightInPoints(30);
		// 上面设置好了内容，我们当然是要输出到某个文件的，输出就需要有输出流
		FileOutputStream fos = new FileOutputStream(
				"C:\\Users\\xuhongchao\\Desktop\\2010.xlsx");
		// 向指定文件写入内容
		wb.write(fos);
		fos.close();
	}
}
