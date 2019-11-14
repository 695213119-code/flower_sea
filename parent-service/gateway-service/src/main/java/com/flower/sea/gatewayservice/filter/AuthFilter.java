package com.flower.sea.gatewayservice.filter;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.flower.sea.commonservice.constant.AuthorityConstant;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.commonservice.utils.JsonUtils;
import com.flower.sea.gatewayservice.call.auth.IAuthCall;
import com.flower.sea.gatewayservice.utils.GatewayUtils;
import com.flower.sea.gatewayservice.utils.GsonUtils;
import com.flower.sea.gatewayservice.utils.RequestUtils;
import com.flower.sea.gatewayservice.vo.Gateway;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;

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
    private final HttpSession session;

    @Value("${swagger.allow}")
    private boolean allow;

    @Autowired
    public AuthFilter(IAuthCall authCall, HttpSession session) {
        this.authService = authCall;
        this.session = session;
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
        if (allow) {
            String requestUrl = RequestUtils.getRequestUrl();
            List<SwaggerResource> swaggerResources = GsonUtils.getSwaggerResources((String) session.getAttribute("swagger-allow"));
            boolean sign = false;
            if (CollUtil.isNotEmpty(swaggerResources)) {
                for (SwaggerResource resource : swaggerResources) {
                    if (requestUrl.equals(resource.getUrl())) {
                        sign = true;
                        break;
                    }
                }
            }
            return !sign;
        }
        return true;
    }

    @Override
    public Object run() {

        String requestUrl = RequestUtils.getRequestUrl();
        log.info("获取到的请求URI======>:{}", requestUrl);

        Gateway gateway = GatewayUtils.getGateway(requestUrl);
        ResponseObject verificationIsToken = authService.verificationIsToken(gateway);
        if (ObjectUtil.isNull(verificationIsToken) || HttpStatus.OK.value() != verificationIsToken.getCode()) {
            failureRequest(ResponseObject.failure(HttpStatus.UNAUTHORIZED.value(), "无效的请求"));
            return null;
        }
        LinkedHashMap data = (LinkedHashMap) verificationIsToken.getData();
        if (AuthorityConstant.AUTHENTICATION_VERIFICATION_NEED_TOKEN.equals(data.get(AuthorityConstant.AUTHENTICATION_VERIFICATION_RESULT_KEY))) {
            //未获取到token,验证失败
            String token = getToken();
            if (StringUtils.isEmpty(token)) {
                failureRequest(ResponseObject.failure(HttpStatus.UNAUTHORIZED.value(), "未获取到Token"));
                return null;
            }
            //验证token的合法性
            ResponseObject verificationTokenIsCorrect = authService.verificationTokenIsCorrect(token);
            if (ObjectUtil.isNull(verificationTokenIsCorrect) || HttpStatus.OK.value() != verificationTokenIsCorrect.getCode()) {
                failureRequest(ResponseObject.failure(HttpStatus.UNAUTHORIZED.value(), "无效的Token"));
                return null;
            }
        }
        return null;
    }


    /**
     * 获取token
     *
     * @return String
     */
    private String getToken() {
        String accessToken = AuthorityConstant.AUTHORITY_ACCESS_TOKEN;
        HttpServletRequest request = RequestUtils.getRequest();
        String token = request.getHeader(accessToken);
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(accessToken);
        }
        return token;
    }


    /**
     * 返回失败的请求
     */
    private void failureRequest(ResponseObject respRecurrence) {
        RequestContext currentContext = RequestUtils.getCurrentContext();
        currentContext.set("isSuccess", false);
        currentContext.setSendZuulResponse(false);
        currentContext.setResponseBody(JsonUtils.object2Json(respRecurrence));
        currentContext.getResponse().setContentType("application/json;charset=UTF-8");
    }

}
