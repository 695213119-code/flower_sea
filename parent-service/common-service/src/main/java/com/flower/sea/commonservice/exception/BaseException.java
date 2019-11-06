package com.flower.sea.commonservice.exception;

/**
 * base异常类
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
class BaseException extends RuntimeException {

    BaseException(String message) {
        super(message);
    }

    BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    BaseException(Throwable cause) {
        super(cause);
    }

    BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
