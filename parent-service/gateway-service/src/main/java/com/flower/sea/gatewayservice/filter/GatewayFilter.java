package com.flower.sea.gatewayservice.filter;


import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 网关拦截器
 *
 * @author zhangLei
 * @serial 2019/11/5
 */
@Component
@Slf4j
public class GatewayFilter extends ZuulFilter {

    /**
     * 过滤器类型，前置过滤器
     *
     * @return String
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }


    /**
     * 过滤器顺序，越小越先执行
     *
     * @return int
     */
    @Override
    public int filterOrder() {
        return 4;
    }

    /**
     * 过滤器是否生效 这里所有的服务接口都进行拦截
     *
     * @return boolean
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 此方法为网关拦截的具体实现方法
     *
     * @return Object
     */
    @Override
    public Object run() {

        return null;
    }


}
