package com.pxene.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.uci.ics.crawler4j.url.WebURL;

public class UtilTest {
	@Test
	public void byteArray2Long() throws Exception {
		byte[] b = new byte[] { '1', '2', '3', '4' };
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (8 - 1 - i) * 8;
			value += (b[i] & 0x000000FF) << shift;
		}
		System.out.println(value);
	}

	@Test
	public void long2ByteArray() throws Exception {
		byte[] array = new byte[8];
		int i;
		int shift;
		long l = 3l;
		for (i = 0, shift = 56; i < 8; i++, shift -= 8) {
			array[i] = (byte) (0xFF & (l >> shift));
		}
		for (int j = 0; j < array.length; j++) {
			System.out.print(array[j]);
		}

	}

	@Test
	public void test3() throws Exception {

		File file = new File("C:\\Users\\xuhongchao\\Desktop\\appֻת��δץȡ.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));

		String line = "";
		while ((line = br.readLine()) != null) {
			System.out.println(new String(line.getBytes("GBK"), "UTF-8"));
		}
	}

	@Test
	public void test4() throws Exception {
		URL url = new URL("https://www.qiushibaike.com/");
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line = "";
		while ((line = br.readLine()) != null) {
			System.out.println(new String(line.getBytes("GBK"), "UTF-8"));
		}
	}

	@Test
	public void test5() throws Exception {
		String url = "http://www.baidu.com?id=12345";
		WebURL webURL = new WebURL();
		webURL.setURL(url);
		System.out.println(webURL.getURL());
		System.out.println(webURL.getSubDomain());
		System.out.println(webURL.getDomain());
		System.out.println(webURL.getPath());
	}

	@Test
	public void test6() throws Exception {
		char[] c = { 'a', 'b' };
		System.out.println(c.toString());
	}

}
