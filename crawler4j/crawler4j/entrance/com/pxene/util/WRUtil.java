package com.pxene.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

public class WRUtil {
	/**
	 * 写到文件中
	 * 
	 * @param fileName
	 */
	public static void write2File(String fileName, char[] content) {
		File file;
		try {
			file = new File(fileName);
			if (!file.exists()) {
				throw new RuntimeException();
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			// bw.write(content);
			bw.write(content);
			bw.flush();
			bw.close();

		} catch (Exception e) {
		}
	}
}
