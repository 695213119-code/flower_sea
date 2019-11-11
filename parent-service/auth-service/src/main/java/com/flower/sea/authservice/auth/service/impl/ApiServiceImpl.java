package com.flower.sea.authservice.auth.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flower.sea.authservice.auth.mapper.ApiMapper;
import com.flower.sea.authservice.auth.service.IApiService;
import com.flower.sea.entityservice.auth.Api;
import org.springframework.stereotype.Service;
/**
 * <p>
 * api表 服务实现类
 * </p>
 *
 * @author zhangLei
 * @since 2019-11-11
 */
@Service
public class ApiServiceImpl extends ServiceImpl<ApiMapper, Api> implements IApiService {

}
