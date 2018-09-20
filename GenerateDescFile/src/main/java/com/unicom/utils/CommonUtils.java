package com.unicom.utils;

import java.util.Calendar;

/**
 * 
 * @ClassName CommonUtils
 * @Description 通用
 * @Reason
 * @date 2018年9月19日下午5:27:51
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public class CommonUtils {
	private CommonUtils() {
	}

	/**
	 * 返回时间字符串，格式为 年月日时分秒
	 * 
	 * @return
	 */
	public static String geCurrentTime() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);

		return "" + year + month + day + hour + minute + second;
	}

}
