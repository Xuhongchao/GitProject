package cn.com.wisetrust.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;



public abstract class Validators {


	/**
	 * 正则：邮编
	 */
	public static final String REGEXP_ZIP = "^[0-9]{6}$";
	/**
	 * 正则：手机号
	 */
	public static final String REGEXP_PHONENUMBER = "^1[3|5|7|8|][0-9]{9}$";
	/**
	 * 正则：身份证
	 */
	public static final String REGEXP_IDCARD = "^(\\d{18})|(\\d{15})$";
	/**
	 * 正则：邮箱
	 */
	public static final String REGEXP_EMAIL = "^\\s*$|^([a-zA-Z0-10]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}$";
	/**
	 * 正则：银行卡
	 */
	public static final String REGEXP_BANKCARD = "^[0-9]+$";
	/**
	 * 正则：中文
	 */
	public static final String REGEXP_CHINESE = "^[\u4e00-\u9fa5]+$";
	/**
	 * 正则：QQ号
	 */
	public static final String REGEXP_QQ = "^[1-9][0-9]{4,10}$";

	/**
	 * 正则：密码
	 */
	public static final String REGEXP_PWD = "^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]+$";

	/**
	 * 添加参数校验错误到输出消息
	 * 
	 * @param output
	 * @param result
	 * @param messageSource
	 * @param locale
	 */
	public static void addParameterErrors(OutputMessage output, BindingResult result) {
		List<String> errors = new ArrayList<String>();
		for (FieldError error : result.getFieldErrors()) {
			String[] codes = error.getCodes();
			if (codes != null) {
				throw new ServiceException(StringUtils.isEmpty(error.getDefaultMessage())?("参数校验失败"+codes):error.getDefaultMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
			}
			errors.add(StringUtils.isEmpty(error.getDefaultMessage())?("参数校验失败"+codes):error.getDefaultMessage());
		}
		output.setParameterErrors(errors);
	}
}
