package com.flower.sea.authservice.auth.service.impl;


import com.flower.sea.authservice.auth.mapper.AppMapper;
import com.flower.sea.authservice.auth.service.IAppService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flower.sea.entityservice.auth.App;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2019-11-11
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements IAppService {

}
