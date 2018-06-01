package cn.com.wisetrust.util;

public enum ErrorCode {
    OK(0, 200),

    BAD_REQUEST(400, 400),

    UNAUTHORIZED(401, 401),

    FORBIDDEN(403, 403),

    INTERNAL_SERVER_ERROR(500, 500),
    
    SYSTEM_SERVER_ERROR(501, 501),
    
    BASE_INFO_NOT_FOUND(502, 502),
    
    FILE_TYPE_ERROR(503, 503),
    
    DATA_ALREADY_EXITE(504, 504),

    ACCOUNT_NOT_EXISTS(101, 403),

    ACCOUNT_INVALID(102, 403)
    ;

    public int code;
    public int httpStatus;

    ErrorCode(int code, int httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

}
