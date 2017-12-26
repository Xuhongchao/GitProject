package cn.com.wisetrust;

/**
 * Created by @author xu on @date 2017年 月 日 上午11:00:27
 */
public class TestCls {
	public static void main(String[] args) {
		String str1 = "LOGIN_NAME,CELLPHONE,PASSWORD,SALT,TYPE,POS,STATUS,LAST_LOGIN_TIME,GMT_CREATE,GMT_MODIFIED,OPEN_ID";
		String str2 = "#{LOGIN_NAME},#{CELLPHONE},#{PASSWORD},#{SALT},#{TYPE},#{POS},#{STATUS},#{LAST_LOGIN_TIME},#{GMT_CREATE},#{GMT_MODIFIED},#{OPEN_ID}";

		String[] strs1 = str1.split(",");
		String[] strs2 = str2.split(",");

		for (int i = 0; i <= 10; i++) {
			// System.out.println(strs1[i] + "=" + strs2[i] + ",");
		}

		String str3 = "1	lizehui	13520098765	AC5A972A00B321047A4F06AA6B28B22FBEEECBB0	1FA058D354B79CA0	1	开发	1	2017-12-04 10:50:06	2017-11-27 13:35:42	2017-12-12 09:57:51";
		String[] strs3 = str3.split("\t");
		for (int i = 0; i < strs3.length; i++) {
			System.out.println(strs3[i] + ",");
		}
	}
}
