package cn.com.wisetrust.util;

import java.io.Serializable;
import java.util.List;

public class OutputMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private ErrorCode resultCode = ErrorCode.OK;
	private ErrorResult errorCode;
	private Object payload;

	/**
	 * 消息内容体
	 * 
	 * @return
	 */
	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public ErrorCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(ErrorCode resultCode) {
		this.resultCode = resultCode;
	}

	public ErrorResult getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorResult errorCode) {
		this.errorCode = errorCode;
	}

	public void setParameterErrors(List<String> errors) {
		setResultCode(ErrorCode.BAD_REQUEST);
		ErrorResult result = new ErrorResult(ErrorCode.BAD_REQUEST.code, errors == null ? "" : errors.toString());
		setErrorCode(result);
	}

	public void setBusinessErrors(String errors, ErrorCode errorCode) {
		if (errorCode != null) {
			setResultCode(errorCode);
		} else {
			setResultCode(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		ErrorResult result = new ErrorResult(resultCode.code, errors == null ? "" : errors);
		setErrorCode(result);
	}

	public void setSystemError() {
		setResultCode(ErrorCode.SYSTEM_SERVER_ERROR);
		ErrorResult result = new ErrorResult(resultCode.code, "系统处理错误");
		setErrorCode(result);
	}

	public boolean isSuccess() {
		return ErrorCode.OK.equals(getResultCode());
	}

}
