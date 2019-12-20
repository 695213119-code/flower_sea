package com.flower.sea.commonservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * https请求工具类
 * <p>
 * 需要忽略ssl证书
 * 添加maven依赖
 * <dependency>
 * <groupId>org.apache.httpcomponents</groupId>
 * <artifactId>httpcore</artifactId>
 * <version>4.4.10</version>
 * </dependency>
 * <dependency>
 * <groupId>org.apache.httpcomponents</groupId>
 * <artifactId>httpclient</artifactId>
 * <version>4.5.6</version>
 * </dependency>
 *
 * @author zhangLei
 * @serial 2019/12/17 9:18
 */
@Slf4j
public class HttpsUtils {

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static SSLConnectionSocketFactory sslConnectionSocketFactory = null;
    private static PoolingHttpClientConnectionManager cm = null;

    static {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, (x509Certificates, s) -> true);
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslConnectionSocketFactory)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setConnectionManager(cm)
                .setConnectionManagerShared(true)
                .build();
    }

    /**
     * https 发送post请求
     *
     * @param url    请求路径
     * @param params 参数(json)
     * @return String
     */
    public static String post(String url, String params) {
        log.info("HTTPS-->POST-->:[url:{},params:{}]", url, params);
        String result = "";
        final String encoding = "utf-8";
        CloseableHttpClient httpClient = null;
        try {
            httpClient = getHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json;charset=" + encoding);
            httpPost.setEntity(new StringEntity(params, encoding));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity resEntity = httpResponse.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity);
            }
        } catch (Exception e) {
            try {
                throw e;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("result:{}", result);
        return result;
    }


}
