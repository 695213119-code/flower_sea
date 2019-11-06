package com.flower.sea.commonservice.enumeration;

/**
 * 系统枚举类
 *
 * @author zhangLei
 * @serial 2019/10/14
 */
public enum SystemEnumeration {
    /**
     * 业务异常/服务异常
     */
    BUSINESS_EXCEPTION(600, "服务器报错啦");


    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    SystemEnumeration(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
