package com.flower.sea.commonservice.recurrence;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * 返回类
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
public class ResponseObject<T> extends AbstractResponse<T> {
    private T data;

    public static final ResponseObject UNAUTHORIZED = unauthorized();
    public static final ResponseObject NOT_FOUND = notFound();

    /**
     * 成功
     */
    public static <S> ResponseObject<S> success() {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.setStatus(HttpStatus.OK.value());
        responseObject.setMessage(SUCCESS);
        return responseObject;
    }

    /**
     * 请求成功并携带有效载荷
     *
     * @param payload 有效载荷
     */
    public static <S> ResponseObject<S> success(S payload) {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.setStatus(HttpStatus.OK.value());
        responseObject.setMessage(SUCCESS);
        responseObject.setData(payload);
        return responseObject;
    }

    public static <S> ResponseObject<S> internalError() {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.setStatus(INTERNAL_SERVER_ERROR.value());
        return responseObject;
    }

    private static <S> ResponseObject<S> unauthorized() {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.setStatus(HttpStatus.UNAUTHORIZED.value());
        return responseObject;
    }

    public static <S> ResponseObject<S> unauthorized(String message) {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.setStatus(HttpStatus.UNAUTHORIZED.value());
        responseObject.setMessage(message);
        return responseObject;
    }

    public static <S> ResponseObject<S> remoteServiceUnavailable() {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        responseObject.setMessage("远程服务不可用");
        return responseObject;
    }


    public static <S> ResponseObject<S> remoteServiceUnavailable(String message) {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        responseObject.setMessage(message);
        return responseObject;
    }


    private static <S> ResponseObject<S> notFound() {
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.setStatus(HttpStatus.NOT_FOUND.value());
        return responseObject;
    }

    /**
     * 内部错误(各种内部异常信息,无法提供正常的业务处理的情况下使用)
     */
    public static <S> ResponseObject<S> internalError(String message) {
        Preconditions.checkArgument(message != null && !Strings.isNullOrEmpty(message), "message不能为空");
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.setStatus(INTERNAL_SERVER_ERROR.value());
        responseObject.setMessage(message);
        return responseObject;
    }

    /**
     * 请求错误(参数校验失败等场景使用)
     */
    public static <S> ResponseObject<S> badRequest(String message) {
        Preconditions.checkArgument(message != null && !Strings.isNullOrEmpty(message), "message不能为空");
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.setStatus(BAD_REQUEST.value());
        responseObject.setMessage(message);
        return responseObject;
    }

    /**
     * 请求失败(baseRequest和internalError无法满足的情况下使用)
     *
     * @param code 错误状态码,请使用 {@link HttpStatus}
     */
    public static <S> ResponseObject<S> fail(int code, String message) {
        Preconditions.checkArgument(code > 0, "code必须大于0,请参考http状态的定义");
        Preconditions.checkArgument(message != null && !Strings.isNullOrEmpty(message), "message不能为空");
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.setStatus(code);
        responseObject.setMessage(message);
        return responseObject;
    }

    /**
     * 请求失败(baseRequest和internalError无法满足的情况下使用)
     *
     * @param code 错误状态码,请使用尽量 {@link HttpStatus}
     */
    public static <S> ResponseObject<S> fail(int code, S payload, String message) {
        Preconditions.checkArgument(code > 0, "code必须大于0,请参考http状态的定义");
        Preconditions.checkArgument(message != null && !Strings.isNullOrEmpty(message), "message不能为空");
        ResponseObject<S> responseObject = new ResponseObject<>();
        responseObject.setStatus(code);
        responseObject.setData(payload);
        responseObject.setMessage(message);
        return responseObject;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseObject{" +
                "data=" + data +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

}
