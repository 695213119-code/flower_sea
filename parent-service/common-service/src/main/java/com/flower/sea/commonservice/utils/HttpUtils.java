package com.flower.sea.commonservice.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http请求工具类
 *
 * @author zhangLei
 * @serial 2019/12/4 22:45
 */
@Slf4j
public class HttpUtils {

    private HttpUtils() {
    }

    /**
     * Get请求
     *
     * @param url 请求路径
     * @return String
     */
    public static String httpGet(String url) {
        log.info("Get请求:URL===>{}", url);
        String result = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Get请求:返回值===>{}", result);
        return result;
    }


}
