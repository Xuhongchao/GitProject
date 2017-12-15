package com.pxene.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author John
 *
 */
public class FileUtils {
	
	/**
	 * 写文件
	 * @param filePath
	 * @param content
	 */
	public static void writeFile(String filePath,String content){
		//1.创建源
		File file = new File(filePath);
		//2.选择流
		OutputStream os = null;
		BufferedWriter bw = null;
		try {
			os = new FileOutputStream(file,true);
			//3.操作
			os.write((content+"\r\n").getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取文件
	 * @param filePath
	 */
	public static List<String> readFile(String inFilePath){
		List<String> list = new ArrayList<String>();
		//解码
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					FileUtils.class.getResourceAsStream(inFilePath)));
			String line = null;
			while(null != (line = reader.readLine())){
				list.add(line);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] args) {
		String relativelyPath=System.getProperty("user.dir"); 
		final List<String> readFile = readFile(relativelyPath+"\\src\\main\\resources\\category.txt");
		System.out.println(readFile.size());
	}
}
