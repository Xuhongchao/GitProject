package cn.com.wisetrust.util;


public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -8634700792767837033L;

	public ErrorCode errorCode;

	public ServiceException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public ServiceException(ErrorCode errorCode, String errorMessageTemplate, Object... args) {
		this(String.format(errorMessageTemplate, args), errorCode);
	}

}
