package com.flower.sea.authservice.mapper;

import com.flower.sea.authservice.pojo.bo.verification.ApiBO;
import com.flower.sea.authservice.pojo.bo.verification.GatewayBO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zhangLei
 * @serial 2019/11/13
 */
@Repository
public interface AuthorityMapper {

    /**
     * 根据服务名称和请求路径获取api的数据
     *
     * @param gateway 参数类
     * @return ApiBO
     */
    ApiBO findApi(@Param("gateway") GatewayBO gateway);
}
