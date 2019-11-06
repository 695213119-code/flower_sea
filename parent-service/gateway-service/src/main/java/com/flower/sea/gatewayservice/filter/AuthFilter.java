package com.flower.sea.gatewayservice.filter;


import com.flower.sea.gatewayservice.utils.HttpServletRequestUtils;
import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 鉴权拦截器
 *
 * @author zhangLei
 * @serial 2019/11/5
 */
@Component
@Slf4j
public class AuthFilter extends ZuulFilter {

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

        String requestUrl = HttpServletRequestUtils.getRequestUrl();
        log.info("获取到的请求URI======>:{}", requestUrl);

        //TODO 此处判断哪些api不需要拦截


        return true;
    }

    @Override
    public Object run() {

        return null;
    }


}
