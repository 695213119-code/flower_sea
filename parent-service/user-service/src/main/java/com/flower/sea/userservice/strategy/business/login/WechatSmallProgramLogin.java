package com.flower.sea.userservice.strategy.business.login;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.entityservice.user.User;
import com.flower.sea.entityservice.user.UserThirdparty;
import com.flower.sea.userservice.dto.in.UserLoginDTO;
import com.flower.sea.userservice.service.IUserCentreService;
import com.flower.sea.userservice.user.service.IUserThirdpartyService;
import com.flower.sea.userservice.utils.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信小程序登录
 *
 * @author zhangLei
 * @serial 2019/12/7 22:02
 */
@Service
@Slf4j
public class WechatSmallProgramLogin implements IUserLoginStrategy {

    private final IUserThirdpartyService userThirdpartyService;
    private final IUserCentreService userCentreService;


    @Autowired
    public WechatSmallProgramLogin(IUserThirdpartyService userThirdpartyService, IUserCentreService userCentreService) {
        this.userThirdpartyService = userThirdpartyService;
        this.userCentreService = userCentreService;
    }


    @Override
    public ResponseObject login(UserLoginDTO userLoginDTO) {

        log.info("=====微信小程序登录!=====");

        if (StringUtils.isBlank(userLoginDTO.getWechatCode()) && StringUtils.isBlank(userLoginDTO.getOpenId())) {
            return ResponseObject.businessFailure("wechatCode或者openId必须传递一个值");
        }

        String openId;
        if (StringUtils.isNotBlank(userLoginDTO.getOpenId())) {
            openId = userLoginDTO.getOpenId();
        } else {
            WechatUtils.WechatCallback wechatCallback = WechatUtils.getWechatCallbackData(userLoginDTO.getWechatCode());
            if (!WechatUtils.SUCCESS.equals(wechatCallback.getCode())) {
                return ResponseObject.businessFailure("获取用户信息失败!");
            }
            openId = wechatCallback.getOpenId();
        }

        UserThirdparty userThirdparty = userThirdpartyService.selectOne(new EntityWrapper<UserThirdparty>().eq("union_id", openId));
        if (null == userThirdparty) {
            return ResponseObject.failure(User.UserEnum.USER_DOES_NOT_BIND_THE_PHONE.getCode(), User.UserEnum.USER_DOES_NOT_BIND_THE_PHONE.getMessage());
        }

        return userCentreService.userLoginEncapsulation(userThirdparty.getUserId(), userLoginDTO.getLoginPlatform());
    }
}
