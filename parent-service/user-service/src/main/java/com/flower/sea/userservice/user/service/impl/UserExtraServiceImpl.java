package com.flower.sea.userservice.user.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flower.sea.entityservice.user.UserExtra;
import com.flower.sea.userservice.user.mapper.UserExtraMapper;
import com.flower.sea.userservice.user.service.IUserExtraService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户额外信息表 服务实现类
 * </p>
 *
 * @author zhangLei
 * @since 2019-12-07
 */
@Service
public class UserExtraServiceImpl extends ServiceImpl<UserExtraMapper, UserExtra> implements IUserExtraService {

}
