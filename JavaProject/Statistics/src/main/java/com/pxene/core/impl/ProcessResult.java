package com.pxene.core.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pxene.core.ProcessResultImpl;

/**
 * Created by @author xuhongchao on @date 2017年 12月 05日 下午5:35:17
 */

@SuppressWarnings("deprecation")
public class ProcessResult implements ProcessResultImpl {
	private final static String READ_PATH = "C:\\Users\\xuhongchao\\Desktop\\1.xlsx";
	private static final XSSFWorkbook WB;
	private XSSFSheet SHEET;
	private String[] SheetNames = new String[3]; // 用来放配置文件中读出的文件名数据
	private List<String> list = new ArrayList<String>(); // 临时存放key
	private Map<String, Integer> map = new LinkedHashMap<String, Integer>(); // 最终结果

	static {
		try {
			WB = new XSSFWorkbook(READ_PATH); // 工作簿
		} catch (IOException e) {
			throw new ExceptionInInitializerError("获取工作簿对象失败");
		}
	}

	public ProcessResult() {
		super();
		this.getPropertiesValue();
	}

	public Map<String, Integer> getStatisticResult() {
		/*
		 * 读取多个sheet，完成全部统计任务
		 */

		SHEET = WB.getSheetAt(0); // 工作表
		// 得到工作表的最后一行
		int end = SHEET.getLastRowNum();
		int count = 0;
		String result1 = null;
		// 读取数据
		for (int i = 1; i <= end; i++) {
			// 得到每一行
			Row row = SHEET.getRow(i);

			// 读取每个单元格的内容
			Cell cell = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
			result1 = turn(cell);
			if (!"".equals(result1.trim())) {
				// 将app名称读进list中，并且每次保证存的是两个，但是从map中放入第一个位置的值，之后再将第一个位置处的删除掉
				list.add(result1);
				if (list.size() > 1) {
					map.put(list.get(0), count);
					list.remove(0); // 将第一个位置处的数据删除
					count = 0;
				}
			}

			String result2 = turn(cell);
			if (result2 != null || !"".equals(result2)) {
				count++;
			}

			if (i == end) {
				map.put(list.get(0), count);
			}
		}
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

	/**
	 * 读取配置文件，拿到里面的文件名信息
	 */
	private void getPropertiesValue() {
		Properties pro = new Properties();
		try {
			pro.load(ProcessResult.class
					.getResourceAsStream("SheetName.properties"));
		} catch (IOException e) {
			throw new RuntimeException("读取配置文件出错");
		}

		String wq = pro.getProperty("wq");
		String ysa = pro.getProperty("ysa");
		String xiy = pro.getProperty("xiy");

		SheetNames[0] = wq;
		SheetNames[1] = ysa;
		SheetNames[2] = xiy;
	}
}
