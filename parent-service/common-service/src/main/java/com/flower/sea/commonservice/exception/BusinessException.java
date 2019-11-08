package com.flower.sea.commonservice.exception;

import org.springframework.http.HttpStatus;

/**
 * 业务异常类
 *
 * @author zhangLei
 * @serial 2019/11/8
 */
public class BusinessException extends RuntimeException {

    private int httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public BusinessException() {
        super("系统异常");
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus.value();
    }

    public int getHttpStatus() {
        return httpStatus;
    }

}
