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
public class WechatUtils {

    private WechatUtils() {

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
    private static String getWechatAppId() {
        return WECHAT_PROPERTIES.getAppId();
    }

    /**
     * 获取微信的appSecret
     *
     * @return String
     */
    private static String getWechatSecret() {
        return WECHAT_PROPERTIES.getAppSecret();
    }


    /**
     * 拼接请求路径
     *
     * @param wechatCode 微信code
     * @return String
     */
    private static String getWechatOpenIdUrl(String wechatCode) {
        return WECHAT_OPEN_ID_URL + "?appid=" + getWechatAppId() + "&secret=" + getWechatSecret() + "&js_code=" + wechatCode + "&grant_type=authorization_code";
    }

    /**
     * 回调类
     */
    @Data
    public static
    class WechatCallback {

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
     * @return Map<String, Object>
     */
    public static WechatCallback getWechatCallbackData(String wechatCode) {
        WechatCallback wechatCallback = new WechatCallback();
        Map wechatDataMap = JsonUtils.jsonToMap(HttpUtils.httpGet(getWechatOpenIdUrl(wechatCode)));
        if (wechatDataMap.containsKey(ERROR_CODE)) {
            log.error("通过微信code获取openId失败==>参数wechatCode:{},微信回调状态码:{},微信回调失败提示信息:{}",
                    wechatCode, wechatDataMap.get(ERROR_CODE), wechatDataMap.get(ERROR_MESSAGE));
            wechatCallback.setCode(String.valueOf((wechatDataMap.get(ERROR_CODE))));
            wechatCallback.setMessage((String) wechatDataMap.get(ERROR_MESSAGE));
        } else {
            String openId = (String) wechatDataMap.get(OPEN_ID);
            if (StringUtils.isNotBlank(openId)) {
                wechatCallback.setCode(SUCCESS);
                wechatCallback.setMessage("SUCCESS");
                wechatCallback.setOpenId(openId);
                wechatCallback.setSessionKey((String) wechatDataMap.get(SESSION_KEY));
            } else {
                log.error("通过微信code获取openId失败==>微信回调数据中没有errCode,但是获取的openid为空");
                wechatCallback.setCode(FAIL);
                wechatCallback.setMessage("openId为空!");
            }
        }
        return wechatCallback;
    }

}
