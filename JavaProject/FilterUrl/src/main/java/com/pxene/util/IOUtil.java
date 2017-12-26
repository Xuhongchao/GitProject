package com.pxene.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pxene.entity.Data;

/**
 * Created by @author xuhongchao on @date 2017年 11月 13日 下午7:44:15
 * 
 * 读取excel、写入txt的工具类
 */

@SuppressWarnings("deprecation")
public class IOUtil {
	private final static String SOURCE_PATH = "C:\\Users\\xu\\Desktop\\app_crawl_detai.xlsx";
	private final static IOUtil IO_UTIL = new IOUtil();
	private XSSFWorkbook wb;
	// private XSSFSheet sheet;

	private IOUtil() {
		try {
			wb = new XSSFWorkbook(SOURCE_PATH);
		} catch (IOException e) {
			throw new RuntimeException("加载XSSFWorkbook失败 -- IOUtil");
		}
		// 工作簿
		// sheet = wb.getSheetAt(0);// 工作表
	}

	public static IOUtil getInstance() {
		return IO_UTIL;
	}
	
	/**
	 * 得到XSSFWorkbook对象
	 * 
	 * @return XSSFWorkbook
	 */
	public XSSFWorkbook getWb() {
		return wb;
	}

	/**
	 * 得到一个XSSFSheet
	 * 
	 * @return XSSFSheet
	 */
	/* 注释掉，同时存在多个sheet的原因
	 * 
	 * public XSSFSheet getSheet() {
		return sheet;
	}*/

	/**
	 * 将map中数据写到文件中
	 * 
	 * @param file
	 *            文件
	 * @param map
	 *            只含有num-urlExam的数据
	 * @param line
	 *            num-data(除num外的其他数据)形式的数据
	 */
	public void writeToFile(File file, Map<String, String> map,
			Map<String, Data> line) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));

			for (Map.Entry<String, String> entry : map.entrySet()) {
				Data data = line.get(entry.getKey());
				if (data == null) {
					continue;
				}
				String value = entry.getKey() + "\t" + data.getDomain() + "\t"
						+ data.getParam() + "\t" + data.getUrlReg() + "\t"
						+ data.getUrlExam() + "\r\n";
				bw.append(value);
				bw.flush();
			}

		} catch (Exception e) {
			throw new RuntimeException("写到文件中出错 -- IOUtil  >>  "
					+ e.getMessage());
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				throw new RuntimeException("关闭写入流出错 -- IOUtil");
			}
		}
	}
	
	/**
	 * 将list中数据写到文件中
	 * 
	 * @param file
	 *            要写入的文件
	 * @param list
	 *            要写入的数据集合
	 */
	public void writeToFile(File file, List<String> list) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));

			for (String url : list) {
				String value = url + "\r\n";

				bw.append(value);
				bw.flush();
			}

		} catch (Exception e) {
			throw new RuntimeException("写到文件中出错 -- IOUtil  >>  " + e.getMessage());
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				throw new RuntimeException("关闭写入流出错 -- IOUtil");
			}
		}
	}

	/**
	 * 从文件中读数据
	 * 
	 * @param file
	 *            要读取的文件
	 */
	public String readFromTxt(File file) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
			// 一次读取一行
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line.split("\t")[0] + "-");
			}
			return sb.substring(0, sb.lastIndexOf("-")).toString();
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("出错的语句是：" + sb.toString());
			throw new RuntimeException("读取文件出现错误 -- IOUtil");
		}catch (FileNotFoundException e) {
			throw new RuntimeException("读取文件找不到 -- IOUtil");
		} catch (IOException e) {
			throw new RuntimeException("读取文件出现io错误 -- IOUtil");
		}  finally {
			try {
				br.close();
			} catch (IOException e) {
				throw new RuntimeException("关闭读取流出错 -- IOUtil");
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
