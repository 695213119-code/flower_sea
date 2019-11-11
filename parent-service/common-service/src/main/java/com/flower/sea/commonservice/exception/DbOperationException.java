package com.flower.sea.commonservice.exception;

import org.springframework.http.HttpStatus;

/**
 * @author zhangLei
 * @serial 2019/11/11
 */
public class DbOperationException extends BusinessException {
    public DbOperationException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public DbOperationException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "数据库操作失败: " + message);
    }
}
