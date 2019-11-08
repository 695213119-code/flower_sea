package com.flower.sea.commonservice.enumeration;

/**
 * 中间件枚举类
 *
 * @author zhangLei
 * @serial 2019/11/8
 */
public enum middlewareEnumeration {

    /**
     * 鉴权队列
     */
    AUTHENTICATION_QUEUE_TYPE("authentication_queue", "鉴权队列类型");

    private String type;

    private String remarks;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    middlewareEnumeration(String type, String remarks) {
        this.type = type;
        this.remarks = remarks;
    }
}
