package com.flower.sea.userservice.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flower.sea.commonservice.constant.CommonConstant;
import com.flower.sea.commonservice.exception.BusinessException;
import com.flower.sea.commonservice.exception.DbOperationException;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.commonservice.utils.*;
import com.flower.sea.entityservice.user.User;
import com.flower.sea.entityservice.user.UserExtra;
import com.flower.sea.entityservice.user.UserThirdparty;
import com.flower.sea.interfaceservice.authentication.IAuthorityCallInterface;
import com.flower.sea.userservice.async.UserAsync;
import com.flower.sea.userservice.constant.PlatformConstant;
import com.flower.sea.userservice.dto.in.user.EditUserInfoDTO;
import com.flower.sea.userservice.dto.in.user.ThirdPartyBindingUserDTO;
import com.flower.sea.userservice.dto.in.user.WeChatAppletLoginDTO;
import com.flower.sea.userservice.dto.out.user.UserDetailsDTO;
import com.flower.sea.userservice.dto.out.user.UserLoginResponseDTO;
import com.flower.sea.userservice.dto.out.wechat.WechatCallbackDTO;
import com.flower.sea.userservice.mapper.UserCentreMapper;
import com.flower.sea.userservice.service.IUserCentreService;
import com.flower.sea.userservice.user.service.IUserExtraService;
import com.flower.sea.userservice.user.service.IUserService;
import com.flower.sea.userservice.user.service.IUserThirdpartyService;
import com.flower.sea.userservice.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Date;
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
    private final RedisUtils redisUtils;
    private final IUserThirdpartyService thirdpartyService;
    private final IUserThirdpartyService userThirdpartyService;
    private final IAuthorityCallInterface authorityInterface;
    private final UserAsync userAsync;
    private final UserUtils userUtils;
    private final UserCentreMapper userCentreMapper;

    @Autowired
    public UserCentreServiceImpl(
            IUserService userService,
            IUserExtraService userExtraService,
            RedisUtils redisUtils,
            IUserThirdpartyService thirdpartyService,
            IUserThirdpartyService userThirdpartyService,
            IAuthorityCallInterface authorityInterface, UserAsync userAsync, UserUtils userUtils, UserCentreMapper userCentreMapper) {
        this.userService = userService;
        this.userExtraService = userExtraService;
        this.redisUtils = redisUtils;
        this.thirdpartyService = thirdpartyService;
        this.userThirdpartyService = userThirdpartyService;
        this.authorityInterface = authorityInterface;
        this.userAsync = userAsync;
        this.userUtils = userUtils;
        this.userCentreMapper = userCentreMapper;
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
        //获取用户
        Long userId = generateUser(thirdPartyBindingUserDTO.getPhone());
        //兼容用户附属数据
        compatibleUserAffiliation(userId, thirdPartyBindingUserDTO);
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
    public ResponseObject weChatAppletLogin(WeChatAppletLoginDTO weChatAppletLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseObject.businessFailure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        //判断该微信号是否绑定了用户
        UserThirdparty userThirdparty = userThirdpartyService.selectOne(new EntityWrapper<UserThirdparty>().eq("union_id", weChatAppletLoginDTO.getWeChatOpenId()));
        if (null == userThirdparty) {
            return ResponseObject.failure(User.UserEnum.WECHAT_UNBOUND_USER.getCode(), User.UserEnum.WECHAT_UNBOUND_USER.getMessage());
        }
        //异步同步用户的微信数据
        userAsync.synchronizationUserWeChatData(weChatAppletLoginDTO, userThirdparty.getId());
        return userLoginEncapsulation(userThirdparty.getUserId(), PlatformConstant.LOGIN_PLATFORM_SMALL_PROGRAM);
    }

    @Override
    public ResponseObject getUserDetails() {
        UserDetailsDTO userDetails = userCentreMapper.getUserDetails(userUtils.getUserId());
        if (null != userDetails) {
            //计算生日的天数间隔
            userDetails.setBirthdayDays(DateUtils.getDaysBetween(new Date(), LocalDateTimeUtils.convertLDTToDate(userDetails.getBirthAnother())));
        }
        return ResponseObject.success(userDetails);
    }

    @Override
    public ResponseObject editUserInfo(EditUserInfoDTO editUserInfoDTO) {
        Long userId = userUtils.getUserId();
        User userOld = userService.selectOne(new EntityWrapper<User>().eq("id", userId).eq(CommonConstant.IS_DELETE, CommonConstant.NOT_DELETE));
        if (null == userOld) {
            log.error("No user found according to user ID parameter:{}", userId);
            return ResponseObject.businessFailure("未获取到正确的用户!");
        }
        UserExtra userExtraOld = userExtraService.selectOne(new EntityWrapper<UserExtra>().eq("user_id", userId).eq(CommonConstant.IS_DELETE, CommonConstant.NOT_DELETE));
        if (null == userExtraOld) {
            log.error("No user attached data is queried according to user ID parameter:{}", userId);
            return ResponseObject.businessFailure("未获取到正确的用户!");
        }
        //用户
        User userEdit = new User();
        userEdit.setId(userOld.getId());
        userEdit.setUserName(editUserInfoDTO.getUserName());
        userEdit.setUpdateTime(new Date());
        userEdit.setUpdateBy(userId);

        //用户附属
        UserExtra userExtraEdit = new UserExtra();
        userExtraEdit.setId(userExtraOld.getId());
        BeanUtils.copyProperties(editUserInfoDTO, userExtraEdit);
        userExtraEdit.setBirth(LocalDateTimeUtils.convertDateToLDT(DateUtils.parseDate(editUserInfoDTO.getBirth())));
        userExtraEdit.setUpdateTime(new Date());
        userExtraEdit.setUpdateBy(userId);
        //设置用户的生日(阳历)
        if (null != editUserInfoDTO.getBirth()) {
            //TODO 正则校验
            userExtraEdit.setBirthAnother(getBirthAnother(editUserInfoDTO.getBirth()));
        }
        try {
            userService.updateById(userEdit);
        } catch (Exception e) {
            log.error("SQL==>update用户表失败,原因:{}", e.getMessage());
            throw new BusinessException("Failed to update user information");
        }
        try {
            userExtraService.updateById(userExtraEdit);
        } catch (Exception e) {
            log.error("SQL==>update用户附属表失败,原因:{}", e.getMessage());
            throw new BusinessException("Failed to update user attachment information");
        }
        return ResponseObject.success();
    }

    /**
     * 获取阳历的日期
     *
     * @param birth 日期(农历)
     * @return 阳历时间
     */
    private LocalDateTime getBirthAnother(String birth) {
        String[] split = birth.split("-");
        //需要将身份证的农历日期转为当前年份,才可以计算出当前年份的生日时间
        int year = Integer.parseInt(DateUtils.getYear());
        int month = Integer.parseInt(split[1]);
        int monthDay = Integer.parseInt(split[2]);
        LocalDateTime localDateTime = LocalDateTimeUtils.convertDateToLDT(DateUtils.parseDate(LunarCalendarUtils.getGregorianCalendarDate(year, month, monthDay)));
        //判断该用户的生日是否已经过去,如果已经过了今年的生日时间,年份需要加1
        Integer timeDifference = LocalDateTimeUtils.determineWhetherTheCurrentTimeHasPassed(DateUtils.formatDate(LocalDateTimeUtils.convertLDTToDate(localDateTime), DateUtils.DATE_FORMAT));
        if (timeDifference > 0) {
            year++;
            localDateTime = LocalDateTimeUtils.convertDateToLDT(DateUtils.parseDate(LunarCalendarUtils.getGregorianCalendarDate(year, month, monthDay)));
        }
        return localDateTime;
    }

    /**
     * 兼容用户的附属数据
     * 不存在有则创建,存在则进行修改
     *
     * @param userId                   用户id
     * @param thirdPartyBindingUserDTO 第三方用户的数据
     */
    private void compatibleUserAffiliation(Long userId, ThirdPartyBindingUserDTO thirdPartyBindingUserDTO) {
        UserExtra userExtra = userExtraService.selectOne(new EntityWrapper<UserExtra>().eq("user_id", userId).eq(CommonConstant.IS_DELETE, CommonConstant.NOT_DELETE));
        if (null != userExtra) {
            int count = 0;
            if (StringUtils.isBlank(userExtra.getAvatar())) {
                userExtra.setAvatar(thirdPartyBindingUserDTO.getAvatar());
                count++;
            }
            if (StringUtils.isBlank(userExtra.getNickName())) {
                userExtra.setNickName(thirdPartyBindingUserDTO.getNickName());
                count++;
            }
            if (null == userExtra.getAge()) {
                userExtra.setAge(thirdPartyBindingUserDTO.getAge());
                count++;
            }
            if (null == userExtra.getGender()) {
                userExtra.setGender(thirdPartyBindingUserDTO.getGender());
                count++;
            }
            if (count > 0) {
                try {
                    userExtra.updateById();
                } catch (Exception e) {
                    log.error("SQL==>update用户附属表失败,原因:{}", e.getMessage());
                    throw new DbOperationException("更新用户信息失败!");
                }
            }
        } else {
            UserExtra userExtraNew = new UserExtra();
            BeanUtils.copyProperties(thirdPartyBindingUserDTO, userExtraNew);
            userExtraNew.setId(IdUtils.generateId());
            userExtraNew.setUserId(userId);
            try {
                userExtraNew.insert();
            } catch (Exception e) {
                log.error("SQL==>insert用户附属表失败,原因:{}", e.getMessage());
                throw new DbOperationException("保存用户信息失败!");
            }
        }
    }


    /**
     * 根据手机号生成用户
     * 用户不存在则直接生成一个用户
     * 用户存在则返回用户id
     *
     * @param phone 用户手机号
     * @return 用户id（Long）
     */
    private Long generateUser(String phone) {
        Long userId;
        User user = userService.selectOne(new EntityWrapper<User>().eq("phone", phone).eq(CommonConstant.IS_DELETE, CommonConstant.NOT_DELETE));
        if (null != user) {
            userId = user.getId();
        } else {
            userId = IdUtils.generateId();
            User userNew = new User();
            userNew.setId(userId);
            userNew.setPhone(phone);
            userNew.setUserName(phone);
            try {
                userNew.insert();
            } catch (Exception e) {
                log.error("SQL==>insert用户数据失败,原因:{}", e.getMessage());
                throw new DbOperationException("新增用户失败!");
            }
        }
        return userId;
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
        return ResponseObject.success(userLoginResponse);
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
        String userIdLoginPlatformRedisKey = userLoginRedisHead + "_" + userId + "_" + loginPlatform;
        //保存本次的token之前必须删除上一次的token,(一个账号在同一个端只能在一处登录)
        String userOldToken = redisUtils.get(userIdLoginPlatformRedisKey);
        if (StringUtils.isNotBlank(userOldToken)) {
            redisUtils.delete(userOldToken);
            redisUtils.delete(userIdLoginPlatformRedisKey);
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
        ResponseObject<String> userTokenResponseObject = authorityInterface.generateUserToken(invalidTime, userId);
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
