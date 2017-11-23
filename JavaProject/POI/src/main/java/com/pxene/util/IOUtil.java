package com.pxene.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by @author xuhongchao on @date 2017年 11月 13日 下午7:44:15
 * 
 * 读取excel、写入txt的工具类
 */

@SuppressWarnings("deprecation")
public class IOUtil {
	private final static String READ_PATH = "C:\\Users\\xuhongchao\\Desktop\\app_crawl_detail.xlsx";
	private final static IOUtil IO_UTIL = new IOUtil();
	private XSSFWorkbook wb;
	private XSSFSheet sheet;

	private IOUtil() {
		try {
			wb = new XSSFWorkbook(READ_PATH);
		} catch (IOException e) {
			throw new RuntimeException("加载XSSFWorkbook失败 -- IOUtil");
		}
		// 工作簿
		sheet = wb.getSheetAt(0);// 工作表
	}

	public static IOUtil getInstance() {
		return IO_UTIL;
	}

	/**
	 * 得到一个XSSFSheet
	 * 
	 * @return XSSFSheet
	 */
	public XSSFSheet getSheet() {
		return sheet;
	}

	/**
	 * 将map中数据写到文件中
	 * 
	 * @param file
	 *            文件
	 * @param map
	 *            map数据
	 */
	public void writeToFile(File file, Map<String, String> map) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));

			for (Map.Entry<String, String> entry : map.entrySet()) {
				String value = entry.getKey() + "\t" + entry.getValue()
						+ "\r\n";
				bw.append(value);
				bw.flush();
			}

		} catch (Exception e) {
			throw new RuntimeException("写到文件中出错 -- IOUtil");
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				throw new RuntimeException("关闭写入流出错 -- IOUtil");
			}
		}
	}

	/**
	 * 用来对读取的数据进行转化
	 * 
	 * @param cell
	 * @return 自定义输出的值
	 */
	public String turn(Cell cell) {
		String value = null;
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
