package Test;

import org.junit.Test;

/**
 * Created by @author xu on @date 2018年 月 日 上午9:27:08
 */
public class GenerateCharacter {

	@Test
	public void testName() throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 2000; i++) {
			sb.append("a");
		}
		System.out.println(sb.length() + "-----------------\n" + sb.toString());
	}

	@Test
	public void testName1() throws Exception {
		char[] chs = new char[2000];
		char ch = 'a';
		for (int i = 0; i < 2000; i++) {
			chs[i] = ch;
		}

		for (int i = 0; i < 2000; i++) {
			System.out.print(chs[i]);
		}
	}

}
