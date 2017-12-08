package com.pxene.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pxene.service.ProcessResult;

/**
 * Created by @author xuhongchao on @date 2017年 12月 05日 下午5:35:17
 * 
 * 核心类
 */

@SuppressWarnings("deprecation")
public class ProcessResultImpl implements ProcessResult {
	private final static String READ_PATH = "C:\\Users\\xuhongchao\\Desktop\\日报汇总.xlsx";
	private static final XSSFWorkbook WB;
	private XSSFSheet SHEET;
	private List<String> list = new ArrayList<String>(); // 临时存放key
	private Map<String, Integer> map = new LinkedHashMap<String, Integer>(); // 最终结果

	static {
		try {
			WB = new XSSFWorkbook(READ_PATH); // 工作簿
		} catch (IOException e) {
			throw new ExceptionInInitializerError("获取工作簿对象失败");
		}
	}

	public ProcessResultImpl() {
		super();
	}

	/**
	 * 核心方法
	 * 
	 * 思路：以每行的方式进行读取，从第一列appName不为空开始计，以后该列所有为空的行（包括那行不为空的），判断第四列url不为空的就加一，
	 * 碰到下一行第一列不为空的就表示遇到另一个app了，将上一个app的名称和url数目存到map中，以此类推
	 */
	public Map<String, Integer> getStatisticResult(int sheetNum) {
		SHEET = WB.getSheetAt(sheetNum); // 工作表
		// 得到工作表的最后一行
		int end = SHEET.getLastRowNum();
		int count = 0;
		String result1 = null;
		String result2 = null;
		// 读取数据
		for (int i = 1; i <= end; i++) {
			// 得到每一行
			Row row = SHEET.getRow(i);
			// 读取每个单元格的内容
			Cell cell0 = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
			result1 = turn(cell0);
			if (!"".equals(result1.trim())) {
				// 将app名称读进list中，并且每次保证存的是两个，但是从map中放入第一个位置的值，之后再将第一个位置处的删除掉
				list.add(result1);
				if (list.size() > 1) {
					map.put(list.get(0), count);
					list.remove(0); // 将第一个位置处的数据删除
					count = 0;
				}
			}

			Cell cell4 = row.getCell(4, Row.CREATE_NULL_AS_BLANK);
			result2 = turn(cell4);
			if (!"".equals(result2.trim()) && result2 != null) {
				// System.out.println(result2);
				count++;
			}

			if (i == end) {
				map.put(list.get(0), count);
			}
		}
		list.clear();
		return map;
	}

	/**
	 * 对从excel中读取的数据处理的工具
	 * 
	 * @param cell
	 * @return excel中读到的数据并进行处理后的结果
	 */
	private String turn(Cell cell) {
		String value;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC: // 数字
			value = cell.getNumericCellValue() + "";
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				if (date != null) {
					value = new SimpleDateFormat("yyyy-MM-dd").format(date);
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
		return value;
	}

}
