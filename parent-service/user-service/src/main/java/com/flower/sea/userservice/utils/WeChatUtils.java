package com.flower.sea.userservice.utils;

import com.flower.sea.commonservice.utils.HttpUtils;
import com.flower.sea.commonservice.utils.JsonUtils;
import com.flower.sea.userservice.properties.WechatProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 微信工具类
 *
 * @author zhangLei
 * @serial 2019/12/6 14:26
 */
@Slf4j
public class WeChatUtils {

    private WeChatUtils() {

    }

    /**
     * 由于对工具类的定义(都是静态方法,且不通过注入的方式使用) 这里使用SpringContextUtils获取微信的配置类信息
     */
    private static final WechatProperties WECHAT_PROPERTIES = SpringContextUtils.getBean(WechatProperties.class);

    /**
     * 获取微信openId的固定请求地址
     */
    private static final String WECHAT_OPEN_ID_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 微信回调的错误码标识
     */
    private static final String ERROR_CODE = "errcode";

    /**
     * 微信回调的错误提示标识
     */
    private static final String ERROR_MESSAGE = "errmsg";

    /**
     * 微信回调的session_key标识
     */
    private static final String SESSION_KEY = "session_key";

    /**
     * 微信回调的openId标识
     */
    private static final String OPEN_ID = "openid";

    /**
     * 方法成功的状态码
     */
    public static final String SUCCESS = "0";

    /**
     * 方法失败的状态码
     */
    private static final String FAIL = "500";

    /**
     * 获取微信的appId
     *
     * @return String
     */
    private static String getWeChatAppId() {
        return WECHAT_PROPERTIES.getAppId();
    }

    /**
     * 获取微信的appSecret
     *
     * @return String
     */
    private static String getWeChatSecret() {
        return WECHAT_PROPERTIES.getAppSecret();
    }


    /**
     * 拼接请求路径
     *
     * @param weChatCode 微信code
     * @return String
     */
    private static String getWeChatOpenIdUrl(String weChatCode) {
        return WECHAT_OPEN_ID_URL + "?appid=" + getWeChatAppId() + "&secret=" + getWeChatSecret() + "&js_code=" + weChatCode + "&grant_type=authorization_code";
    }

    /**
     * 回调类
     */
    @Data
    public static
    class WeChatCallback {

        /**
         * 返回的code
         */
        private String code;

        /**
         * 返回的提示信息
         */
        private String message;

        /**
         * 微信的 sessionKey
         */
        private String sessionKey;

        /**
         * 微信的  openId
         */
        private String openId;
    }


    /**
     * 获取微信的回调数据
     *
     * @return WeChatCallback
     */
    public static WeChatCallback getWeChatCallbackData(String weChatCode) {
        WeChatCallback wechatCallback = new WeChatCallback();
        Map weChatDataMap = JsonUtils.jsonToMap(HttpUtils.httpGet(getWeChatOpenIdUrl(weChatCode)));
        if (weChatDataMap.containsKey(ERROR_CODE)) {
            log.error("通过微信code获取openId失败==>参数wechatCode:{},微信回调状态码:{},微信回调失败提示信息:{}",
                    weChatCode, weChatDataMap.get(ERROR_CODE), weChatDataMap.get(ERROR_MESSAGE));
            wechatCallback.setCode(String.valueOf((weChatDataMap.get(ERROR_CODE))));
            wechatCallback.setMessage((String) weChatDataMap.get(ERROR_MESSAGE));
        } else {
            String openId = (String) weChatDataMap.get(OPEN_ID);
            if (StringUtils.isNotBlank(openId)) {
                wechatCallback.setCode(SUCCESS);
                wechatCallback.setMessage("SUCCESS");
                wechatCallback.setOpenId(openId);
                wechatCallback.setSessionKey((String) weChatDataMap.get(SESSION_KEY));
            } else {
                log.error("通过微信code获取openId失败==>微信回调数据中没有errCode,但是获取的openid为空");
                wechatCallback.setCode(FAIL);
                wechatCallback.setMessage("openId为空!");
            }
        }
        return wechatCallback;
    }

}
