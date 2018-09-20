package com.unicom.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unicom.entity.TableEntity;
import com.unicom.service.GetMessageService;
import com.unicom.service.WriteData2DocsService;
import com.unicom.utils.CommonUtils;
import com.unicom.utils.JDBCUtil;

/**
 * 
 * @ClassName DownLoadServlet
 * @Description
 * @Reason
 * @date 2018年9月19日下午4:13:00
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public class DownLoadServlet extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(DownLoadServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 8568850611958735927L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doGet(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		String ip = request.getParameter("ip");
		String port = request.getParameter("port");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String db = request.getParameter("db");

		String url = "";
		if ("".equals(db.trim()) || db == null) {
			url = "jdbc:mysql://" + ip + ":" + port + "?useUnicode=true&characterEncoding=utf-8";
		} else {
			url = "jdbc:mysql://" + ip + ":" + port + "/" + db + "?useUnicode=true&characterEncoding=utf-8";
		}

		JDBCUtil jdbcUtil = null;
		FileOutputStream fos = null;
		FileInputStream fis = null;
		ServletOutputStream out = null;
		try {
			jdbcUtil = new JDBCUtil(url, username, password);
			out = response.getOutputStream();

			// 执行操作，生成文件
			GetMessageService service = new GetMessageService(jdbcUtil);

			// 生成文件写入的地址和文件名
			String path = setDocxPath();
			LOGGER.info("文件地址:" + path);
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			// 将数据写入到文件
			WriteData2DocsService writeData2Docs = new WriteData2DocsService(jdbcUtil);
			fos = new FileOutputStream(file, true);
			if ("".equals(db.trim()) || db == null) {
				List<TableEntity> tableEntities = service.setTableEntity();
				writeData2Docs.exportSTToWord(fos, tableEntities);
			} else {
				List<TableEntity> tableEntities = service.setTableEntity(db);
				writeData2Docs.exportSTToWord(fos, tableEntities, db);
			}
			LOGGER.info("生成文件完毕");

			// 下载文件
			String filename = URLEncoder.encode(file.getName(), "utf-8"); // 解决中文文件名下载后乱码的问题
			response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");

			fis = new FileInputStream(file);
			int bytesend = 0;
			byte[] buff = new byte[1024];
			while ((bytesend = fis.read(buff)) != -1) {
				out.write(buff, 0, bytesend);
			}
			LOGGER.info("下载文件完毕");

		} catch (Exception e) {
			out.write("文件下载失败".getBytes("UTF-8"));
			LOGGER.error("文件下载失败");
		} finally {
			if (fos != null) {
				fos.close();
			}
			if (fis != null) {
				fis.close();
			}
			if (out != null) {
				out.close();
			}
		}

	}

	/**
	 * 生成写入文件地址和文件名
	 * 
	 * @return String path
	 * @throws FileNotFoundException
	 */
	private String setDocxPath() throws FileNotFoundException {
		String path = this.getServletContext().getRealPath("/");
		return path + "\\" + CommonUtils.geCurrentTime() + ".docx";
	}

}
