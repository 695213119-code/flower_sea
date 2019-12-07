package com.flower.sea.commonservice.recurrence;

import com.flower.sea.commonservice.enumeration.SystemEnumeration;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 返回类
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
@Data
public class ResponseObject<T> {

    private static final long serialVersionUID = 2169140102785610954L;

    @ApiModelProperty("状态码, 200表示成功")
    private Integer code;

    @ApiModelProperty("提示消息")
    private String message;

    @ApiModelProperty("返回数据")
    private T data;

    private ResponseObject() {
    }

    public static <S> ResponseObject<S> success() {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.code = SystemEnumeration.SUCCESS_CODE.getCode();
        responseObject.message = SystemEnumeration.SUCCESS_CODE.getMessage();
        return responseObject;
    }


    public static <S> ResponseObject<S> success(S data) {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.data = data;
        responseObject.code = SystemEnumeration.SUCCESS_CODE.getCode();
        responseObject.message = SystemEnumeration.SUCCESS_CODE.getMessage();
        return responseObject;
    }

    public static <S> ResponseObject<S> success(S data, String message) {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.code = SystemEnumeration.SUCCESS_CODE.getCode();
        responseObject.message = message;
        responseObject.data = data;
        return responseObject;
    }

    public static <S> ResponseObject<S> failure() {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.code = SystemEnumeration.FAIL_CODE.getCode();
        responseObject.message = SystemEnumeration.FAIL_CODE.getMessage();
        return responseObject;
    }


    public static <S> ResponseObject<S> failure(String message) {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.code = SystemEnumeration.FAIL_CODE.getCode();
        responseObject.message = message;
        return responseObject;
    }

    public static <S> ResponseObject<S> failure(Integer code, String message) {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.code = code;
        responseObject.message = message;
        return responseObject;
    }


    public static <S> ResponseObject<S> businessFailure(String message) {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.code = SystemEnumeration.BUSINESS_EXCEPTION.getCode();
        responseObject.message = message;
        return responseObject;
    }


    @Override
    public String toString() {
        return "RespRecurrence{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }


}
