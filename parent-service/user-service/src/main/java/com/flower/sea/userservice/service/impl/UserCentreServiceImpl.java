package com.flower.sea.userservice.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flower.sea.commonservice.constant.CommonConstant;
import com.flower.sea.commonservice.enumeration.SystemEnumeration;
import com.flower.sea.commonservice.exception.DbOperationException;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.commonservice.utils.IdUtils;
import com.flower.sea.commonservice.utils.JsonUtils;
import com.flower.sea.commonservice.utils.RedisUtils;
import com.flower.sea.entityservice.user.User;
import com.flower.sea.entityservice.user.UserExtra;
import com.flower.sea.entityservice.user.UserThirdparty;
import com.flower.sea.userservice.call.auth.IAuthUserFeign;
import com.flower.sea.userservice.constant.PlatformConstant;
import com.flower.sea.userservice.dto.in.ThirdPartyBindingUserDTO;
import com.flower.sea.userservice.dto.in.UserLoginDTO;
import com.flower.sea.userservice.dto.out.user.UserLoginResponseDTO;
import com.flower.sea.userservice.dto.out.wechat.WechatCallbackDTO;
import com.flower.sea.userservice.service.IUserCentreService;
import com.flower.sea.userservice.strategy.key.UserStrategyKey;
import com.flower.sea.userservice.strategy.scene.UserScene;
import com.flower.sea.userservice.user.service.IUserExtraService;
import com.flower.sea.userservice.user.service.IUserService;
import com.flower.sea.userservice.user.service.IUserThirdpartyService;
import com.flower.sea.userservice.utils.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final IAuthUserFeign authUserFeign;
    private final RedisUtils redisUtils;
    private final IUserThirdpartyService thirdpartyService;

    @Autowired
    public UserCentreServiceImpl(UserScene userScene, IUserService userService, IUserExtraService userExtraService, @Qualifier("IAuthUserFeign") IAuthUserFeign authUserFeign, RedisUtils redisUtils, IUserThirdpartyService thirdpartyService) {
        this.userScene = userScene;
        this.userService = userService;
        this.userExtraService = userExtraService;
        this.authUserFeign = authUserFeign;
        this.redisUtils = redisUtils;
        this.thirdpartyService = thirdpartyService;
    }

    @Override
    public ResponseObject getWechatOpenId(String wechatCode) {
        WechatCallbackDTO wechatCallbackDto = new WechatCallbackDTO();
        WechatUtils.WechatCallback wechatCallback = WechatUtils.getWechatCallbackData(wechatCode);
        if (!WechatUtils.SUCCESS.equals(wechatCallback.getCode())) {
            return ResponseObject.businessFailure("获取openId失败!");
        }
        BeanUtils.copyProperties(wechatCallback, wechatCallbackDto);
        return ResponseObject.success(wechatCallbackDto);
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

        UserLoginResponseDTO userLoginResponse = new UserLoginResponseDTO();

        //生成用户token -根据字典获取
        final long invalidTime = 183 * 24 * 60 * 60 * 1000L;
        ResponseObject userTokenResponseObject = authUserFeign.generateUserToken(invalidTime, user.getId());
        if ((null == userTokenResponseObject) || (!userTokenResponseObject.getCode().equals(HttpStatus.OK.value()))) {
            return ResponseObject.businessFailure("获取用户Token失败!");
        }
        String userToken = (String) userTokenResponseObject.getData();
        userLoginResponse.setUserToken(userToken);

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
            BeanUtils.copyProperties(userExtra, userLoginResponse);
        }

        return ResponseObject.success(userLoginResponse);
    }

    @Override
    public ResponseObject thirdPartyBindingUser(ThirdPartyBindingUserDTO thirdPartyBindingUserDTO) {

        UserThirdparty userThirdparty = thirdpartyService.selectOne(new EntityWrapper<UserThirdparty>().eq("union_id", thirdPartyBindingUserDTO.getUnionId())
                .eq(CommonConstant.IS_DELETE, CommonConstant.NOT_DELETE));
        if (null != userThirdparty) {
            return ResponseObject.businessFailure("该用户已经绑定,请勿重复绑定!");
        }

        //生成用户
        Long userId = IdUtils.generateId();
        User user = new User();
        user.setId(userId);
        user.setPhone(thirdPartyBindingUserDTO.getPhone());
        user.setUserName(thirdPartyBindingUserDTO.getPhone());
        try {
            user.insert();
        } catch (Exception e) {
            log.error("保存用户数据失败,原因:{}", e.getMessage());
            throw new DbOperationException("保存用户数据失败");
        }

        //绑定第三方
        UserThirdparty userThirdpartyNew = new UserThirdparty();
        userThirdpartyNew.setUserId(userId);
        BeanUtils.copyProperties(thirdPartyBindingUserDTO, userThirdpartyNew);
        try {
            userThirdpartyNew.insert();
        } catch (Exception e) {
            log.error("保存用户第三方数据失败,原因:{}", e.getMessage());
            throw new DbOperationException("保存用户第三方数据失败");
        }

        return userLoginEncapsulation(userId, PlatformConstant.LOGIN_PLATFORM_SMALL_PROGRAM);
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
