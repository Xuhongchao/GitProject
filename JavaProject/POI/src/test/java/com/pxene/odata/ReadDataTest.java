package com.pxene.odata;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

/**
 * Created by @author xuhongchao on @date 2017年 11月 13日 下午7:52:18
 */

public class ReadDataTest {
	private final static String READ_PATH = "C:\\Users\\xuhongchao\\Desktop\\2010.xlsx";

	@SuppressWarnings("deprecation")
	@Test
	public void test1() throws Exception {
		// 工作簿
		XSSFWorkbook wb = new XSSFWorkbook(READ_PATH);
		// 工作表
		XSSFSheet sheet = wb.getSheetAt(0);
		// 得到工作表的第一行和最后一行
		int first = sheet.getFirstRowNum();
		int end = sheet.getLastRowNum();
		// 读取数据
		for (int i = first; i <= end; i++) {
			// 得到每一行
			Row row = sheet.getRow(i);
			// 得到每行的第一个单元格和最后一个单元格
			int firstCell = row.getFirstCellNum();
			int endCell = row.getLastCellNum();

			String value = null;
			for (int j = firstCell; j < endCell; j++) {
				// 读取每个单元格的内容
				Cell cell = row.getCell(j, Row.CREATE_NULL_AS_BLANK);
				// 判断单元格内容
				switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_NUMERIC: // 数字
					value = cell.getNumericCellValue() + "";
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						Date date = cell.getDateCellValue();
						if (date != null) {
							value = new SimpleDateFormat("yyyy-MM-dd")
									.format(date);
						} else {
							value = "";
						}
					} else {
						value = new DecimalFormat("0").format(cell
								.getNumericCellValue());
					}
					break;
				case HSSFCell.CELL_TYPE_STRING: // 字符串
					value = cell.getStringCellValue();
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					value = cell.getBooleanCellValue() + "";
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // 公式
					value = cell.getCellFormula() + "";
					break;
				case HSSFCell.CELL_TYPE_BLANK: // 空值
					value = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // 故障
					value = "非法字符";
					break;
				default:
					value = "未知类型";
					break;
				}
			}
		}

	}
}
