package com.flower.sea.userservice.user.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flower.sea.entityservice.user.User;
import com.flower.sea.userservice.user.mapper.UserMapper;
import com.flower.sea.userservice.user.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhangLei
 * @since 2019-12-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
