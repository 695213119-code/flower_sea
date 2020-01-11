package com.flower.sea.userservice.mapper;

import com.flower.sea.userservice.dto.out.user.UserDetailsDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zhangLei
 * @serial 2020-01-11
 */
@Repository
public interface UserCentreMapper {

    /**
     * 根据userId获取用户详情
     *
     * @param userId 用户id
     * @return UserDetailsDTO
     */
    UserDetailsDTO getUserDetails(@Param("userId") Long userId);
}
