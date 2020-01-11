package com.flower.sea.userservice.async;

import com.flower.sea.commonservice.exception.DbOperationException;
import com.flower.sea.commonservice.utils.JsonUtils;
import com.flower.sea.entityservice.user.UserThirdparty;
import com.flower.sea.userservice.dto.in.WeChatAppletLoginDTO;
import com.flower.sea.userservice.user.service.IUserThirdpartyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 用户异步方法处理类
 *
 * @author zhangLei
 * @serial 2020-01-11
 */
@Component
@Slf4j
public class UserAsync {

    private final IUserThirdpartyService userThirdpartyService;

    @Autowired
    public UserAsync(IUserThirdpartyService userThirdpartyService) {
        this.userThirdpartyService = userThirdpartyService;
    }

    @Async
    public void synchronizationUserWeChatData(WeChatAppletLoginDTO weChatAppletLoginDTO, Long userThirdpartyId) {
        log.info("开始同步微信用户的数据:{}", JsonUtils.object2Json(weChatAppletLoginDTO));
        UserThirdparty userThirdparty = new UserThirdparty();
        BeanUtils.copyProperties(weChatAppletLoginDTO, userThirdparty);
        userThirdparty.setId(userThirdpartyId);
        try {
            userThirdpartyService.updateById(userThirdparty);
        } catch (Exception e) {
            log.error("SQL==>update用户第三方表失败,原因:{}", e.getMessage());
            throw new DbOperationException("同步微信用户数据失败!");
        }
        log.info("微信用户:[{}]同步数据完成", weChatAppletLoginDTO.getNickName());
    }
}
