package com.flower.sea.commonservice.exception;

import org.springframework.http.HttpStatus;

/**
 * 业务异常类
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
public class BusinessException extends BaseException {

    private int httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();

    private BusinessException(String code, int httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException() {
        super("系统异常");
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus.value();
    }

    public BusinessException(HttpStatus httpStatus) {
        super("系统异常");
        this.httpStatus = httpStatus.value();
    }

    public BusinessException(HttpStatus httpStatus, String message) {
        this(httpStatus.name(), httpStatus.value(), message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
