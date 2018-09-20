package com.unicom.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unicom.dao.AnalysisDatabaseStructureImpl;
import com.unicom.entity.FieldEntity;
import com.unicom.entity.TableEntity;
import com.unicom.utils.JDBCUtil;
import com.unicom.utils.WordUtil;

/**
 * 生成一张表的说明表格
 * 
 * @ClassName WriteData2Docs
 * @Description 将数据按照格式写到doc文件
 * @Reason
 * @date 2018年9月13日下午8:06:36
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public class WriteData2DocsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WriteData2DocsService.class);

	private JDBCUtil jdbcUtil;
	private AnalysisDatabaseStructureImpl adsImpl;

	public WriteData2DocsService(JDBCUtil jdbcUtil) {
		this.jdbcUtil = jdbcUtil;
		adsImpl = new AnalysisDatabaseStructureImpl(this.jdbcUtil);
	}

	/**
	 * 将数据写到doc中
	 * 
	 * @param stream
	 *            输出流
	 * @param titleName
	 *            标题名称
	 * @param fieldEntities
	 *            字段信息集合
	 * @throws IOException
	 */
	public void exportSTToWord(OutputStream stream, List<TableEntity> tableEntities) {
		LOGGER.info("正生成表的说明文档");

		// 创建document对象
		XWPFDocument document = new XWPFDocument();

		try {
			// 将数据分类，视图view的一类，普通表的一类
			List<TableEntity> baseTableList = getBaseTableList(tableEntities);
			List<TableEntity> viewList = getViewList(tableEntities);

			// 往文档中写入数据库名称为分隔符
			List<Object[]> dbs = adsImpl.getDBs();
			for (int e = 0; e < dbs.size(); e++) {
				// 数据库分隔符
				XWPFParagraph titleParagraph = document.createParagraph();
				titleParagraph.setAlignment(ParagraphAlignment.LEFT);
				WordUtil.setParagraphStyle(titleParagraph, dbs.get(e)[0].toString(), "CD0000", "黑体", 18);

				for (int k = 0; k < baseTableList.size(); k++) {
					if ((baseTableList.get(k).getDatabaseEntity().getDbName()).equals(dbs.get(e)[0].toString())) {
						List<FieldEntity> fieldEntities = baseTableList.get(k).getFields();
						String tableName = "表名：" + baseTableList.get(k).getTableName();
						String createTime = "创建时间：" + baseTableList.get(k).getCreateTime();
						String tableComment = "表信息：" + baseTableList.get(k).getTableComment();

						drawTable(document, fieldEntities, tableName, createTime, tableComment);
					}
				}
			}

			// 加入分隔标志
			if (viewList.size() > 0) {
				XWPFParagraph separator = document.createParagraph();
				separator.setAlignment(ParagraphAlignment.LEFT);
				WordUtil.setParagraphStyle(separator, "-------------VIEW-----------", "000000", "黑体", 22);

				for (int e = 0; e < dbs.size(); e++) {
					// 数据库分隔符
					XWPFParagraph titleParagraph = document.createParagraph();
					titleParagraph.setAlignment(ParagraphAlignment.LEFT);
					WordUtil.setParagraphStyle(titleParagraph, dbs.get(e)[0].toString(), "CD0000", "黑体", 18);
					for (int k = 0; k < viewList.size(); k++) {
						if ((viewList.get(k).getDatabaseEntity().getDbName()).equals(dbs.get(e)[0].toString())) {
							List<FieldEntity> fieldEntities = viewList.get(k).getFields();
							String tableName = "表名：" + viewList.get(k).getTableName();
							String createTime = "创建时间：" + viewList.get(k).getCreateTime();
							String tableComment = "表信息：" + viewList.get(k).getTableComment();

							drawTable(document, fieldEntities, tableName, createTime, tableComment);
						}
					}
				}
			}
			document.write(stream);

		} catch (Exception e) {
			LOGGER.error("WriteData2DocsService服务的exportSTToWord出现错误", e);
			throw new RuntimeException("写入过程出错");
		} finally {
			try {
				document.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将数据写到doc中
	 * 
	 * @param stream
	 *            输出流
	 * @param titleName
	 *            标题名称
	 * @param fieldEntities
	 *            字段信息集合
	 * @param db
	 *            数据库名
	 * @throws IOException
	 */
	public void exportSTToWord(OutputStream stream, List<TableEntity> tableEntities, String db) {
		LOGGER.info("正生成表的说明文档");

		// 创建document对象
		XWPFDocument document = new XWPFDocument();

		try {
			// 将数据分类，视图view的一类，普通表的一类
			List<TableEntity> baseTableList = getBaseTableList(tableEntities);
			List<TableEntity> viewList = getViewList(tableEntities);

			for (int k = 0; k < baseTableList.size(); k++) {
				List<FieldEntity> fieldEntities = baseTableList.get(k).getFields();
				String tableName = "表名：" + baseTableList.get(k).getTableName();
				String createTime = "创建时间：" + baseTableList.get(k).getCreateTime();
				String tableComment = "表信息：" + baseTableList.get(k).getTableComment();

				drawTable(document, fieldEntities, tableName, createTime, tableComment);
			}

			// 加入分隔标志
			if (viewList.size() > 0) {
				XWPFParagraph separator = document.createParagraph();
				separator.setAlignment(ParagraphAlignment.LEFT);
				WordUtil.setParagraphStyle(separator, "-------------VIEW-----------", "000000", "黑体", 22);

				for (int k = 0; k < viewList.size(); k++) {
					List<FieldEntity> fieldEntities = viewList.get(k).getFields();
					String tableName = "表名：" + viewList.get(k).getTableName();
					String createTime = "创建时间：" + viewList.get(k).getCreateTime();
					String tableComment = "表信息：" + viewList.get(k).getTableComment();

					drawTable(document, fieldEntities, tableName, createTime, tableComment);
				}
			}

			document.write(stream);

		} catch (Exception e) {
			LOGGER.error("WriteData2DocsService服务的exportSTToWord出现错误", e);
			throw new RuntimeException("写入过程出错");
		} finally {
			try {
				document.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 从tableEntities中挑选出类型为BaseTable的表
	 * 
	 * @param tableEntities
	 * @return
	 */
	private List<TableEntity> getBaseTableList(List<TableEntity> tableEntities) {
		List<TableEntity> newTableEntities = new ArrayList<>();
		for (TableEntity tableEntity : tableEntities) {
			if ("BASE TABLE".equals(tableEntity.getTableType())) {
				newTableEntities.add(tableEntity);
			}
		}
		return newTableEntities;
	}

	/**
	 * 从tableEntities中挑选出类型为View的表
	 * 
	 * @param tableEntities
	 * @return
	 */
	private List<TableEntity> getViewList(List<TableEntity> tableEntities) {
		List<TableEntity> newTableEntities = new ArrayList<>();
		for (TableEntity tableEntity : tableEntities) {
			if ("VIEW".equals(tableEntity.getTableType())) {
				newTableEntities.add(tableEntity);
			}
		}
		return newTableEntities;
	}

	/**
	 * drawTable具体操作
	 * 
	 * @param document
	 * @param fieldEntities
	 * @param tableName
	 * @param createTime
	 * @param tableComment
	 * @throws Exception
	 */
	private void drawTable(XWPFDocument document, List<FieldEntity> fieldEntities, String tableName, String createTime,
			String tableComment) throws Exception {
		// 添加标题
		XWPFParagraph titleParagraph1 = document.createParagraph();
		titleParagraph1.setAlignment(ParagraphAlignment.LEFT);
		WordUtil.setParagraphStyle(titleParagraph1, tableName, "", "宋体", 13);
		titleParagraph1.createRun().addBreak(); // 换行
		WordUtil.setParagraphStyle(titleParagraph1, createTime, "", "宋体", 13);
		titleParagraph1.createRun().addBreak(); // 换行
		WordUtil.setParagraphStyle(titleParagraph1, tableComment, "", "宋体", 13);

		// 创建表格
		XWPFTable table = document.createTable(fieldEntities.size() + 1, 5);
		WordUtil.setTableWidth(table, "8345");

		// 设置表格第一行样式和内容
		XWPFTableRow rowIndex = table.getRow(0);
		rowIndex.setHeight(580);
		WordUtil.setFirstRowStyle(rowIndex, 5, new String[] { "字段名", "数据类型", "是否允许为空", "字段描述", "是否索引" }, "1E90FF",
				2000);

		// 设置其他行的样式和内容
		for (int i = 1; i <= fieldEntities.size(); i++) {
			XWPFTableRow rowIndex1 = table.getRow(i);
			rowIndex1.setHeight(450);

			FieldEntity fieldEntity = fieldEntities.get(i - 1);
			for (int j = 0; j < 5; j++) {
				XWPFTableCell cell = rowIndex1.getCell(j);
				// WordUtil.createHSpanCell(cell, "", 2000, STMerge.RESTART);

				switch (j) {
				case 0:
					WordUtil.createVSpanCell(cell, "", 2000, STMerge.RESTART);
					cell.setText(fieldEntity.getName());
					break;
				case 1:
					WordUtil.setCellStyle(cell, fieldEntity.getType(), "", 2000);
					break;
				case 2:
					WordUtil.setCellStyle(cell, fieldEntity.getNullable(), "", 2000);
					break;
				case 3:
					WordUtil.createVSpanCell(cell, "", 2000, STMerge.RESTART);
					cell.setText(fieldEntity.getComment());
					break;
				case 4:
					WordUtil.setCellStyle(cell, fieldEntity.getKey(), "", 2000);
					break;
				}
			}
		}
		// 换行
		WordUtil.toBr(document);
	}

}
