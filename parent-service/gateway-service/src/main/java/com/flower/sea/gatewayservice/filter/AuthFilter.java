package com.flower.sea.gatewayservice.filter;


import cn.hutool.core.util.ObjectUtil;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.commonservice.utils.JsonUtils;
import com.flower.sea.gatewayservice.call.auth.IAuthCall;
import com.flower.sea.gatewayservice.constant.AuthConstant;
import com.flower.sea.gatewayservice.utils.GatewayUtils;
import com.flower.sea.gatewayservice.vo.Gateway;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 网关统一鉴权拦截器
 *
 * @author zhangLei
 * @serial 2019/11/5
 */
@Component
@Slf4j
public class AuthFilter extends ZuulFilter {

    private final IAuthCall authService;

    @Autowired
    public AuthFilter(IAuthCall authCall) {
        this.authService = authCall;
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }


    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        String requestUrl = RequestContext.getCurrentContext().getRequest().getRequestURI();
        if (requestUrl.endsWith("v2/api-docs")) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() {

        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String requestUrl = request.getRequestURI();
        log.info("获取到的请求URI======>:{}", requestUrl);

        Gateway gateway = GatewayUtils.getGateway(requestUrl);
        ResponseObject verificationIsToken = authService.verificationIsToken(gateway);
        if (ObjectUtil.isNull(verificationIsToken) || HttpStatus.OK.value() != verificationIsToken.getCode()) {
            failureRequest(currentContext, verificationIsToken);
            return null;
        }
        LinkedHashMap data = (LinkedHashMap) verificationIsToken.getData();
        //此接口需要token验证
        if (AuthConstant.AUTHENTICATION_VERIFICATION_NEED_TOKEN.equals(data.get(AuthConstant.AUTHENTICATION_VERIFICATION_RETURN_KEY))) {
            //未获取到token,验证失败
            String token = getToken(request);
            if (StringUtils.isEmpty(token)) {
                failureRequest(currentContext, ResponseObject.failure(HttpStatus.UNAUTHORIZED.value(), "未获取到Token"));
                return null;
            }
            //验证token的合法性
            ResponseObject verificationTokenIsCorrect = authService.verificationTokenIsCorrect(token);
            if (ObjectUtil.isNull(verificationTokenIsCorrect) || HttpStatus.OK.value() != verificationTokenIsCorrect.getCode()) {
                failureRequest(currentContext, ResponseObject.failure(HttpStatus.UNAUTHORIZED.value(), "无效的Token"));
                return null;
            }
        }
        return null;
    }


    /**
     * 获取token
     *
     * @param request request对象
     * @return String
     */
    private String getToken(HttpServletRequest request) {
        String accessToken = AuthConstant.ACCESS_TOKEN;
        String token = request.getHeader(accessToken);
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(accessToken);
        }
        return token;
    }


    /**
     * 失败的请求
     *
     * @param currentContext currentContext对象
     */
    private void failureRequest(RequestContext currentContext, ResponseObject respRecurrence) {
        currentContext.set("isSuccess", false);
        currentContext.setSendZuulResponse(false);
        currentContext.setResponseBody(JsonUtils.object2Json(respRecurrence));
        currentContext.getResponse().setContentType("application/json;charset=UTF-8");
    }

}
