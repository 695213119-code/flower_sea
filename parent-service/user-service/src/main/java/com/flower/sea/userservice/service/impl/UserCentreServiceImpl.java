package com.flower.sea.userservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flower.sea.commonservice.constant.CommonConstant;
import com.flower.sea.commonservice.enumeration.SystemEnumeration;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.commonservice.utils.JsonUtils;
import com.flower.sea.commonservice.utils.RedisUtils;
import com.flower.sea.entityservice.user.User;
import com.flower.sea.entityservice.user.UserExtra;
import com.flower.sea.userservice.call.auth.IAuthCall;
import com.flower.sea.userservice.dto.in.UserLoginDTO;
import com.flower.sea.userservice.dto.out.UserLoginResponseDTO;
import com.flower.sea.userservice.dto.out.WechatCallbackDTO;
import com.flower.sea.userservice.service.IUserCentreService;
import com.flower.sea.userservice.strategy.key.UserStrategyKey;
import com.flower.sea.userservice.strategy.scene.UserScene;
import com.flower.sea.userservice.user.service.IUserExtraService;
import com.flower.sea.userservice.user.service.IUserService;
import com.flower.sea.userservice.utils.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangLei
 * @serial 2019/12/4 22:37
 */
@Service
@Slf4j
public class UserCentreServiceImpl implements IUserCentreService {

    private final UserScene userScene;
    private final IUserService userService;
    private final IUserExtraService userExtraService;
    private final IAuthCall authCall;
    private final RedisUtils redisUtils;

    @Autowired
    public UserCentreServiceImpl(UserScene userScene, IUserService userService, IUserExtraService userExtraService, IAuthCall authCall, RedisUtils redisUtils) {
        this.userScene = userScene;
        this.userService = userService;
        this.userExtraService = userExtraService;
        this.authCall = authCall;
        this.redisUtils = redisUtils;
    }

    @Override
    public ResponseObject getWechatOpenId(String wechatCode) {
        Map<String, String> wechatCallbackDataMap = WechatUtils.getWechatCallbackData(wechatCode);
        if (CollUtil.isNotEmpty(wechatCallbackDataMap)) {
            WechatCallbackDTO wechatCallbackDto = new WechatCallbackDTO();
            wechatCallbackDto.setOpenId(wechatCallbackDataMap.get(WechatUtils.OPEN_ID));
            wechatCallbackDto.setSessionKey(wechatCallbackDataMap.get(WechatUtils.SESSION_KEY));
            return ResponseObject.success(wechatCallbackDto);
        }
        return ResponseObject.failure("获取微信回调数据失败!");
    }

    @Override
    public ResponseObject login(UserLoginDTO userLoginDTO) {
        //TODO 参数校验

        ResponseObject<String> userStrategyKeyResponseObject = getUserStrategyKey(userLoginDTO.getLoginPlatform(), userLoginDTO.getLoginType());
        if (HttpStatus.OK.value() != userStrategyKeyResponseObject.getCode()) {
            return ResponseObject.failure(SystemEnumeration.BUSINESS_EXCEPTION.getCode(), userStrategyKeyResponseObject.getMessage());
        }
        String userStrategyKey = userStrategyKeyResponseObject.getData();
        return userScene.login(userLoginDTO, userStrategyKey);
    }

    @Override
    public ResponseObject userLoginEncapsulation(Long userId, Integer loginPlatform) {

        User user = userService.selectOne(new EntityWrapper<User>().eq("id", userId)
                .eq(CommonConstant.IS_DELETE, CommonConstant.NOT_DELETE));

        //判断是否被禁用
        if (User.UserEnum.USER_DISABLE.getCode().equals(user.getStatus())) {
            return ResponseObject.businessFailure(User.UserEnum.USER_DISABLE.getMessage());
        }

        UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO();

        //生成用户token -根据字典获取
        final long invalidTime = 183 * 24 * 60 * 60 * 1000L;
        ResponseObject userTokenResponseObject = authCall.generateUserToken(invalidTime, user.getId());
        if (null == userTokenResponseObject) {
            return ResponseObject.businessFailure("获取用户Token失败!");
        }
        String userToken = (String) userTokenResponseObject.getData();
        userLoginResponseDTO.setUserToken(userToken);

        //将token存入redis-删除上一个用户token
        String userIdLoginPlatformRedisKey = user.getId() + "_" + loginPlatform;
        String userOldTokenRedis = redisUtils.get(userIdLoginPlatformRedisKey);
        if (StringUtils.isNotBlank(userOldTokenRedis)) {
            redisUtils.delete(userOldTokenRedis);
        }
        redisUtils.set(userToken, JsonUtils.object2Json(user), invalidTime, TimeUnit.MILLISECONDS);
        redisUtils.set(userIdLoginPlatformRedisKey, userToken, invalidTime, TimeUnit.MILLISECONDS);

        UserExtra userExtra = userExtraService.selectOne(new EntityWrapper<UserExtra>().eq("user_id", user.getId()).
                eq(CommonConstant.IS_DELETE, CommonConstant.NOT_DELETE));
        if (null != userExtra) {
            BeanUtils.copyProperties(userExtra, userLoginResponseDTO);
        }

        return ResponseObject.success(userLoginResponseDTO);
    }


    /**
     * 获取用户登录的策略key
     *
     * @param loginPlatform 登录平台
     * @param loginType     登录方式
     * @return String
     */
    private ResponseObject<String> getUserStrategyKey(Integer loginPlatform, Integer loginType) {

        String loginPlatformStrategyKey;
        String loginTypeStrategyKey;

        //保存登录平台的策略key
        Map<Integer, String> loginPlatformStrategyKeyMap = new HashMap<>(8);
        loginPlatformStrategyKeyMap.put(1, UserStrategyKey.LOGIN_PLATFORM_PC);
        loginPlatformStrategyKeyMap.put(2, UserStrategyKey.LOGIN_PLATFORM_APP);
        loginPlatformStrategyKeyMap.put(3, UserStrategyKey.LOGIN_PLATFORM_SMALL_PROGRAM);

        //保存登录方式的策略key
        Map<Integer, String> loginTypeStrategyKeyMap = new HashMap<>(8);
        loginTypeStrategyKeyMap.put(1, UserStrategyKey.LOGIN_TYPE_ACCOUNT_AND_PASSWORD);
        loginTypeStrategyKeyMap.put(2, UserStrategyKey.LOGIN_TYPE_PHONE_SHORT_CODE);
        loginTypeStrategyKeyMap.put(3, UserStrategyKey.LOGIN_TYPE_WECHAT_CODE);

        loginPlatformStrategyKey = loginPlatformStrategyKeyMap.get(loginPlatform);
        if (StringUtils.isBlank(loginPlatformStrategyKey)) {
            return ResponseObject.failure("获取登录平台参数异常");
        }

        loginTypeStrategyKey = loginTypeStrategyKeyMap.get(loginType);
        if (StringUtils.isBlank(loginTypeStrategyKey)) {
            return ResponseObject.failure("获取登录方式参数异常");
        }
        return ResponseObject.success(loginPlatformStrategyKey + loginTypeStrategyKey);
    }

}
