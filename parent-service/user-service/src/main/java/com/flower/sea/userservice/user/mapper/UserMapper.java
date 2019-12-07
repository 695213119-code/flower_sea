package com.flower.sea.userservice.user.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.flower.sea.entityservice.user.User;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zhangLei
 * @since 2019-12-07
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
}
