package com.unicom.utils;

import java.math.BigInteger;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

/**
 * 
 * @ClassName WordUtil
 * @Description 通过poi操作word的一些方法
 * @Reason
 * @date 2018年9月14日下午4:33:30
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version v1
 * @since JDK 1.8
 */
public class WordUtil {
	private WordUtil() {
	}

	/**
	 * 通过段落的换行来是实现一个简单的换行操作
	 * 
	 * @param document
	 *            XWPFDocument
	 */
	public static void toBr(XWPFDocument document) {
		XWPFParagraph titleParagraph = document.createParagraph();
		titleParagraph.createRun().addCarriageReturn();
	}

	/**
	 * 设置文本段落格式
	 * 
	 * @param titleParagraph
	 *            XWPFParagraph
	 * @param title
	 *            文本内容
	 * @param bgcolor
	 *            文本颜色
	 * @param fontFamily
	 *            文本字体
	 * @param fontSize
	 *            文本字体大小
	 */
	public static void setParagraphStyle(XWPFParagraph titleParagraph, String title, String bgcolor, String fontFamily,
			int fontSize) {
		XWPFRun titleParagraphRun = titleParagraph.createRun();
		titleParagraphRun.setText(title);
		titleParagraphRun.setColor(bgcolor);
		titleParagraphRun.setFontFamily(fontFamily);
		titleParagraphRun.setFontSize(fontSize);
	}

	/**
	 * 设置表格第一行的样式和内容，默认单元格数目和单元格值数组的大小是一致的情况，不一致的情况就重置values全部为空
	 * 
	 * @param rowIndex
	 *            XWPFTableRow
	 * @param cellNum
	 *            第一行单元格数目
	 * @param values
	 *            每个单元格的内容
	 * @param bgcolor
	 *            单元格的颜色
	 * @param width
	 *            宽度
	 */
	public static void setFirstRowStyle(XWPFTableRow rowIndex, int cellNum, String[] values, String bgcolor,
			int width) {
		if (values == null || values.length == 0 || values.length != cellNum) {
			values = new String[cellNum];
		}

		for (int i = 0; i < cellNum; i++) {
			XWPFTableCell cell = rowIndex.getCell(i);
			WordUtil.setCellStyle(cell, values[i], bgcolor, width);
		}
	}

	/**
	 * 设置表格行宽
	 * 
	 * @param table
	 *            XWPFTable
	 * @param width
	 *            宽度
	 */
	public static void setTableWidth(XWPFTable table, String width) {
		CTTbl ttbl = table.getCTTbl();
		CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
		CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
		CTJc cTJc = tblPr.addNewJc();
		cTJc.setVal(STJc.Enum.forString("left"));
		tblWidth.setW(new BigInteger(width));
		tblWidth.setType(STTblWidth.DXA);
	}

	/**
	 * 设置单元格内容、背景色和宽度
	 * 
	 * @param cell
	 *            单元格
	 * @param value
	 *            单元格内容
	 * @param bgcolor
	 *            背景色
	 * @param width
	 *            宽度
	 */
	public static void setCellStyle(XWPFTableCell cell, String value, String bgcolor, int width) {
		CTP ctp = CTP.Factory.newInstance();
		XWPFParagraph p = new XWPFParagraph(ctp, cell);
		p.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun run = p.createRun();
		run.setText(value);

		CTTc cttc = cell.getCTTc();
		CTTcPr cellPr = cttc.addNewTcPr();
		cellPr.addNewTcW().setW(BigInteger.valueOf(width));
		cellPr.addNewVAlign().setVal(STVerticalJc.CENTER);
		CTShd ctshd = cellPr.addNewShd();
		ctshd.setFill(bgcolor);

		cell.setParagraph(p);
	}

	/**
	 * 设置单元格垂直居中样式
	 * 
	 * @param cell
	 *            单元格
	 * @param bgcolor
	 *            颜色
	 * @param width
	 *            宽度
	 * @param stMerge
	 */
	public static void createVSpanCell(XWPFTableCell cell, String bgcolor, int width, STMerge.Enum stMerge) {
		CTTc cttc = cell.getCTTc();
		CTTcPr cellPr = cttc.addNewTcPr();
		cellPr.addNewTcW().setW(BigInteger.valueOf(width));
		cell.setColor(bgcolor);
		cellPr.addNewVMerge().setVal(stMerge);
		cellPr.addNewVAlign().setVal(STVerticalJc.CENTER);
	}

}
