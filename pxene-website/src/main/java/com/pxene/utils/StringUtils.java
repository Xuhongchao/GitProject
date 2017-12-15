package com.pxene.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * 给字符串左补齐ch总长度len
     *
     * @param data
     * @param ch
     * @param len
     * @return
     */
    public static String leftFullChar(String data, char ch, int len) {
        StringBuilder sb = new StringBuilder();
        if (data == null || "".equals(data)) {
            for (int i = 0; i < len; i++) {
                sb.append(ch);
            }
        } else {
            sb.append(data);
            for (int i = data.length(); i < len; i++) {
                sb.insert(0, ch);
            }
        }
        return sb.toString();
    }
    
    public static String regexpExtract(String str, String regex) {
		if (str == null || regex == null) {
			return "";
		}
		Pattern REGEX_PATTERN = Pattern.compile(regex);
		Matcher matcher = REGEX_PATTERN.matcher(str);
		if (matcher.find()) {
			return matcher.group(1).trim();
		}
		return "";
	}
    
    /**
	 * 查看是否包含中文字符
	 * @param str
	 * @return
	 */
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
   
   
	public static String removePunctuations(String str) {
		if (str == null) {
			return "";
		}
		return str.replaceAll("[\\pP\\pZ\\pS]", "");
	}
	
	public static String removeLineBreak(String str) {
        if (str == null) {
            return "";
        }
        return str.replaceAll("[\\n\\r]", "");
    }
	
	/**
	 * 时间戳转换成日期格式字符串
	 * 
	 * @param seconds
	 *            精确到秒的字符串
	 * @param formatStr
	 * @return
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty())
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	/**
	 * 日期格式字符串转换成时间戳
	 * 
	 * @param date
	 *            字符串日期
	 * @param format
	 *            如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String date2TimeStamp(String date_str, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(date_str).getTime() / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 在原有的字符串上插入一个新的字符串
	 * @param original
	 * @param insert
	 * @param index
	 * @return
	 */
	public static String stringinsert(String original,String insert,int index){     
	    return original.substring(0,index)+insert+original.substring(index,original.length());
	} 
	
	 
    /**
     * 去掉所有空格和标点符号
     * @param str
     * @return
     */
   public static String removePunctAndSpace(String str){
	   
	   return str.replaceAll("[\\p{Punct}\\p{Space}]+", ""); 
   }

}
