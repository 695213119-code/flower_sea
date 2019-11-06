package com.flower.sea.commonservice.recurrence;

import org.springframework.http.HttpStatus;

/**
 * 返回类
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
public class AbstractResponse<T> {

    static final String SUCCESS = "success";
    int status = -1;
    String message;
    private String requestUri;
    String path;

    public static String getSUCCESS() {
        return SUCCESS;
    }

    public int getStatus() {
        return status;
    }

    void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean checkOk() {
        return this.status == HttpStatus.OK.value();
    }
}
