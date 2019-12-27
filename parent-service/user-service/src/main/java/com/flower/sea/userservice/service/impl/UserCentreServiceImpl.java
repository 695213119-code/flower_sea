package com.flower.sea.userservice.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flower.sea.commonservice.constant.CommonConstant;
import com.flower.sea.commonservice.exception.BusinessException;
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
import com.flower.sea.userservice.dto.out.user.UserLoginResponseDTO;
import com.flower.sea.userservice.dto.out.wechat.WechatCallbackDTO;
import com.flower.sea.userservice.service.IUserCentreService;
import com.flower.sea.userservice.user.service.IUserExtraService;
import com.flower.sea.userservice.user.service.IUserService;
import com.flower.sea.userservice.user.service.IUserThirdpartyService;
import com.flower.sea.userservice.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangLei
 * @serial 2019/12/4 22:37
 */
@Service
@Slf4j
public class UserCentreServiceImpl implements IUserCentreService {

    private final IUserService userService;
    private final IUserExtraService userExtraService;
    private final IAuthUserFeign authUserFeign;
    private final RedisUtils redisUtils;
    private final IUserThirdpartyService thirdpartyService;
    private final IUserThirdpartyService userThirdpartyService;

    @Autowired
    public UserCentreServiceImpl(
            IUserService userService,
            IUserExtraService userExtraService,
            @Qualifier("IAuthUserFeign") IAuthUserFeign authUserFeign,
            RedisUtils redisUtils,
            IUserThirdpartyService thirdpartyService, IUserThirdpartyService userThirdpartyService) {
        this.userService = userService;
        this.userExtraService = userExtraService;
        this.authUserFeign = authUserFeign;
        this.redisUtils = redisUtils;
        this.thirdpartyService = thirdpartyService;
        this.userThirdpartyService = userThirdpartyService;
    }

    @Override
    public ResponseObject getWeChatOpenId(String weChatCode) {
        WechatCallbackDTO wechatCallbackDto = new WechatCallbackDTO();
        WeChatUtils.WeChatCallback weChatCallback = WeChatUtils.getWeChatCallbackData(weChatCode);
        if (!WeChatUtils.SUCCESS.equals(weChatCallback.getCode())) {
            return ResponseObject.businessFailure("获取openId失败!");
        }
        BeanUtils.copyProperties(weChatCallback, wechatCallbackDto);
        return ResponseObject.success(wechatCallbackDto);
    }


    @Override
    public ResponseObject thirdPartyBindingUser(ThirdPartyBindingUserDTO thirdPartyBindingUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseObject.businessFailure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserThirdparty userThirdparty = thirdpartyService.selectOne(new EntityWrapper<UserThirdparty>().eq("union_id", thirdPartyBindingUserDTO.getUnionId())
                .eq(CommonConstant.IS_DELETE, CommonConstant.NOT_DELETE));
        if (null != userThirdparty) {
            return ResponseObject.businessFailure("该用户已经绑定,请勿重复绑定!");
        }
        //若用户不存在则直接生成一个用户
        Long userId;
        User userCheck = userService.selectOne(new EntityWrapper<User>().eq("phone", thirdPartyBindingUserDTO.getPhone())
                .eq(CommonConstant.IS_DELETE, CommonConstant.NOT_DELETE));
        if (null != userCheck) {
            userId = userCheck.getId();
        } else {
            userId = IdUtils.generateId();
            User user = new User();
            user.setId(userId);
            user.setPhone(thirdPartyBindingUserDTO.getPhone());
            user.setUserName(thirdPartyBindingUserDTO.getPhone());
            try {
                user.insert();
            } catch (Exception e) {
                log.error("SQL==>insert用户数据失败,原因:{}", e.getMessage());
                throw new DbOperationException("新增用户失败!");
            }
        }

        //兼容用户附属数据
        UserExtra userExtraCheck = userExtraService.selectOne(new EntityWrapper<UserExtra>().eq("user_id", userId).eq(CommonConstant.IS_DELETE, CommonConstant.NOT_DELETE));
        if (null != userExtraCheck) {
            int count = 0;
            if (StringUtils.isBlank(userExtraCheck.getAvatar())) {
                userExtraCheck.setAvatar(thirdPartyBindingUserDTO.getAvatar());
                count++;
            }
            if (StringUtils.isBlank(userExtraCheck.getNickName())) {
                userExtraCheck.setNickName(thirdPartyBindingUserDTO.getNickName());
                count++;
            }
            if (null == userExtraCheck.getAge()) {
                userExtraCheck.setAge(thirdPartyBindingUserDTO.getAge());
                count++;
            }
            if (null == userExtraCheck.getGender()) {
                userExtraCheck.setGender(thirdPartyBindingUserDTO.getGender());
                count++;
            }
            if (count > 0) {
                try {
                    userExtraCheck.updateById();
                } catch (Exception e) {
                    log.error("SQL==>update用户附属表失败,原因:{}", e.getMessage());
                    throw new DbOperationException("更新用户信息失败!");
                }
            }
        } else {
            UserExtra userExtra = new UserExtra();
            BeanUtils.copyProperties(thirdPartyBindingUserDTO, userExtra);
            userExtra.setId(IdUtils.generateId());
            userExtra.setUserId(userId);
            try {
                userExtra.insert();
            } catch (Exception e) {
                log.error("SQL==>insert用户附属表失败,原因:{}", e.getMessage());
                throw new DbOperationException("保存用户信息失败!");
            }
        }

        //绑定第三方
        UserThirdparty userThirdPartyNew = new UserThirdparty();
        userThirdPartyNew.setId(IdUtils.generateId());
        userThirdPartyNew.setUserId(userId);
        BeanUtils.copyProperties(thirdPartyBindingUserDTO, userThirdPartyNew);
        try {
            userThirdPartyNew.insert();
        } catch (Exception e) {
            log.error("保存用户第三方数据失败,原因:{}", e.getMessage());
            throw new DbOperationException("保存用户第三方数据失败");
        }

        return userLoginEncapsulation(userId, PlatformConstant.LOGIN_PLATFORM_SMALL_PROGRAM);
    }

    @Override
    public ResponseObject weChatAppletLogin(String weChatOpenId) {
        //判断该微信号是否绑定了用户
        UserThirdparty userThirdparty = userThirdpartyService.selectOne(new EntityWrapper<UserThirdparty>().eq("union_id", weChatOpenId));
        if (null == userThirdparty) {
            return ResponseObject.failure(User.UserEnum.WECHAT_UNBOUND_USER.getCode(), User.UserEnum.WECHAT_UNBOUND_USER.getMessage());
        }
        return userLoginEncapsulation(userThirdparty.getUserId(), PlatformConstant.LOGIN_PLATFORM_SMALL_PROGRAM);
    }


    /**
     * 处理用户登录的公共方法
     *
     * @param userId        用户id
     * @param loginPlatform 登录端 1-pc 2-app 3-小程序
     * @return ResponseObject
     */
    private ResponseObject userLoginEncapsulation(Long userId, Integer loginPlatform) {
        User user = userService.selectOne(new EntityWrapper<User>().eq("id", userId)
                .eq(CommonConstant.IS_DELETE, CommonConstant.NOT_DELETE));
        //判断是否被禁用
        if (User.UserEnum.USER_DISABLE.getCode().equals(user.getStatus())) {
            return ResponseObject.businessFailure(User.UserEnum.USER_DISABLE.getMessage());
        }
        UserLoginResponseDTO userLoginResponse = new UserLoginResponseDTO();
        //获取token的过期时间
        Long invalidTime = getUserTokenExpirationDate(loginPlatform);
        //获取用户token
        String userToken = getUserToken(invalidTime, user.getId());
        userLoginResponse.setUserToken(userToken);
        //操作对应用户的redis
        operationCorrespondingUserRedis(user.getId(), loginPlatform, user, invalidTime, userToken);
        //获取用户的详细数据
        userDetailed(userLoginResponse, user.getId());
        return ResponseObject.success(userLoginResponse);
    }

    /**
     * 获取用户登录时的详情
     *
     * @param userLoginResponse 用户数据返回类
     * @param userId            用户id
     */
    private void userDetailed(UserLoginResponseDTO userLoginResponse, Long userId) {
        UserExtra userExtra = userExtraService.selectOne(new EntityWrapper<UserExtra>().eq("user_id", userId).
                eq(CommonConstant.IS_DELETE, CommonConstant.NOT_DELETE));
        if (null != userExtra) {
            BeanUtils.copyProperties(userExtra, userLoginResponse);
        }
    }

    /**
     * 操作对应用户的redis
     *
     * @param userId        用户id
     * @param loginPlatform 登录端 1-pc 2-app 3-小程序
     * @param user          用户对象
     * @param invalidTime   token过期时间
     * @param userToken     token
     */
    private void operationCorrespondingUserRedis(Long userId, Integer loginPlatform, User user, Long invalidTime, String userToken) {
        //根据(固定头部_用户id_登录平台)组成redis的key
        final String userLoginRedisHead = "user_login_redis_head";
        String userIdLoginPlatformRedisKey = userLoginRedisHead + "-" + userId + "-" + loginPlatform;
        //保存本次的token之前必须删除上一次的token,(一个账号在同一个端只能在一处登录)
        String userOldToken = redisUtils.get(userIdLoginPlatformRedisKey);
        if (StringUtils.isNotBlank(userOldToken)) {
            redisUtils.delete(userOldToken);
        }
        redisUtils.set(userToken, JsonUtils.object2Json(user), invalidTime, TimeUnit.MILLISECONDS);
        redisUtils.set(userIdLoginPlatformRedisKey, userToken, invalidTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取用户token
     *
     * @param invalidTime token的持续时间 毫秒值
     * @param userId      用户id
     * @return String
     */
    private String getUserToken(Long invalidTime, Long userId) {
        ResponseObject<String> userTokenResponseObject = authUserFeign.generateUserToken(invalidTime, userId);
        log.info("调用服务端接口[获取token],返回的响应数据为:{}", JsonUtils.object2Json(userTokenResponseObject));
        if ((null == userTokenResponseObject) || (!userTokenResponseObject.getCode().equals(HttpStatus.OK.value()))) {
            throw new BusinessException("获取token失败!");
        }
        return userTokenResponseObject.getData();
    }

    /**
     * 根据登录端获取token过期时间
     *
     * @param loginPlatform 登录端 1-pc 2-app 3-小程序
     * @return Long(毫秒值)
     */
    private Long getUserTokenExpirationDate(Integer loginPlatform) {
        return 183 * 24 * 60 * 60 * 1000L;
    }

}
