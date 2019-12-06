package com.flower.sea.userservice.utils;

import com.flower.sea.commonservice.utils.HttpUtils;
import com.flower.sea.commonservice.utils.JsonUtils;
import com.flower.sea.userservice.properties.WechatProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
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
    public static final String SESSION_KEY = "session_key";

    /**
     * 微信回调的openId标识
     */
    public static final String OPEN_ID = "openid";

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
     * 获取微信的回调数据
     *
     * @return Map<String, String>
     */
    public static Map<String, String> getWechatCallbackData(String wechatCode) {
        Map<String, String> map = new HashMap<>(2);
        Map wechatDataMap = JsonUtils.jsonToMap(HttpUtils.httpGet(getWechatOpenIdUrl(wechatCode)));
        if (wechatDataMap.containsKey(ERROR_CODE)) {
            log.error("通过微信code获取openId失败,参数wechatCode:{},微信回调状态码:{},微信回调失败提示信息:{}",
                    wechatCode, wechatDataMap.get(ERROR_CODE), wechatDataMap.get(ERROR_MESSAGE));
        } else {
            String openId = (String) wechatDataMap.get(OPEN_ID);
            if (StringUtils.isNotBlank(openId)) {
                map.put(OPEN_ID, openId);
                map.put(SESSION_KEY, (String) wechatDataMap.get(SESSION_KEY));
            } else {
                log.error("通过微信code获取openId失败,微信回调数据中没有errcode,但是获取的openid为空");
            }
        }
        return map;
    }

}
