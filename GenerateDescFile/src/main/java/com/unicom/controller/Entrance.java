package com.unicom.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unicom.dao.AnalysisDatabaseStructureImpl;
import com.unicom.entity.TableEntity;
import com.unicom.service.GetMessageService;
import com.unicom.service.WriteData2DocsService;
import com.unicom.utils.JDBCUtil;

/**
 * 
 * @ClassName Entrance
 * @Description 程序入口
 * @Reason
 * @date 2018年9月10日下午3:35:44
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public class Entrance {

	private static final Logger LOGGER = LoggerFactory.getLogger(Entrance.class);

	public static void main(String[] args) {
		LOGGER.info("程序开始");

		String ip = "127.0.0.1";
		String port = "3306";
		String username = "root";
		String password = "root";
		String db = "";

		String url = "";
		if ("".equals(db.trim()) || db == null) {
			url = "jdbc:mysql://" + ip + ":" + port + "?useUnicode=true&characterEncoding=utf-8";
		} else {
			url = "jdbc:mysql://" + ip + ":" + port + "/" + db + "?useUnicode=true&characterEncoding=utf-8";
		}

		GetMessageService service = null;
		WriteData2DocsService writeData2Docs = null;
		try {
			JDBCUtil jdbcUtil = new JDBCUtil(url, username, password);
//			AnalysisDatabaseStructureImpl adsi = new AnalysisDatabaseStructureImpl(connection);
//			adsi.getTableFields("admin", "bookstore");
			
			service = new GetMessageService(jdbcUtil);
			writeData2Docs = new WriteData2DocsService(jdbcUtil);

			File file = new File("D://result.docx");
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file, true);

			if ("".equals(db.trim()) || db == null) {
				List<TableEntity> tableEntities = service.setTableEntity();
				writeData2Docs.exportSTToWord(fos, tableEntities);
			} else {
				List<TableEntity> tableEntities = service.setTableEntity(db);
				writeData2Docs.exportSTToWord(fos, tableEntities, db);
			}

		} catch (Exception e) {
			LOGGER.error("文件下载失败", e);
		}

		LOGGER.info("程序结束");
	}
}
