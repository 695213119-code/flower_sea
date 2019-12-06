package com.flower.sea.userservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.flower.sea.commonservice.enumeration.SystemEnumeration;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.userservice.dto.in.UserLoginDTO;
import com.flower.sea.userservice.dto.out.WechatCallbackDTO;
import com.flower.sea.userservice.service.IUserCentreService;
import com.flower.sea.userservice.strategy.key.UserStrategyKey;
import com.flower.sea.userservice.utils.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangLei
 * @serial 2019/12/4 22:37
 */
@Service
@Slf4j
public class UserCentreServiceImpl implements IUserCentreService {

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

        return null;
    }


    /**
     * 获取用户的策略key
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
