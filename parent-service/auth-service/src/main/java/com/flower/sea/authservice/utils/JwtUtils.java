package com.flower.sea.authservice.utils;

import cn.hutool.core.collection.CollUtil;
import com.flower.sea.authservice.pojo.bo.JwtTokenBO;
import com.flower.sea.commonservice.enumeration.SystemEnumeration;
import com.flower.sea.commonservice.exception.BusinessException;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.commonservice.utils.JsonUtils;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
@Slf4j
public class JwtUtils {

    private JwtUtils() {
    }

    /**
     * 密匙
     */
    private static final byte[] JWT_SECRET_BYTES = "qs%O6cY$bMPWt%0vrO%meKUB6^P7SWLSZ6eakDG0dBPsJ*j3gC&PHz!opDe46r7TgC9RBS".getBytes();

    /**
     * 用户id-map-key
     */
    private static final String UID = "uid";

    /**
     * 生成时间-map-key
     */
    private static final String STA = "sta";

    /**
     * 过期时间-map-key
     */
    private static final String EXP = "exp";

    /**
     * 返回key
     */
    private static final String RESULT = "Result";

    /**
     * 返回key
     */
    private static final String DATA = "data";

    /**
     * 校验通过
     */
    private static final Integer CALIBRATION_PASS = 0;

    /**
     * 校验失败,token错误
     */
    private static final Integer CHECK_FAILED_TOKEN_ERROR = 1;

    /**
     * 校验失败,token过期
     */
    private static final Integer CHECK_FAILED_TOKEN_OVERDUE = 2;


    /**
     * 生成token
     *
     * @param userId      用户id
     * @param invalidTime 过期时间(毫秒值)
     * @return String
     */
    public static String generateToken(Long userId, Long invalidTime) {
        String token;
        Map<String, Long> map = new HashMap<>(8);
        long currentTimeMillis = System.currentTimeMillis();
        map.put(UID, userId);
        map.put(STA, currentTimeMillis);
        map.put(EXP, currentTimeMillis + invalidTime);
        try {
            token = creatToken(map);
        } catch (JOSEException e) {
            log.error("创建Token失败,异常信息:{}", e.getMessage());
            throw new BusinessException(SystemEnumeration.BUSINESS_EXCEPTION.getMessage());
        }
        return token;
    }

    /**
     * 解析token
     *
     * @param token token
     * @return Map<String, Object>
     */
    public static ResponseObject<JwtTokenBO> analysisToken(String token) {
        Map<String, Object> valid;
        try {
            valid = valid(token);
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
            log.error("校验Token失败,异常信息:{}", e.getMessage());
            throw new BusinessException(SystemEnumeration.BUSINESS_EXCEPTION.getMessage());
        }
        if (CollUtil.isEmpty(valid)) {
            log.error("解析到的JwtToken数据为空,数据为:{}", valid);
            throw new BusinessException(SystemEnumeration.BUSINESS_EXCEPTION.getMessage());
        }
        JwtTokenBO jwtToken = new JwtTokenBO();
        if (CollUtil.isNotEmpty(valid)) {
            Integer result = (Integer) valid.get(RESULT);
            if (null != result && !CALIBRATION_PASS.equals(result)) {
                return ResponseObject.failure(HttpStatus.UNAUTHORIZED.value(), CHECK_FAILED_TOKEN_OVERDUE.equals(result) ? "token已过期" : "无效的token");
            }
            jwtToken = JsonUtils.json2Object((String) valid.get(DATA), JwtTokenBO.class);
        }
        return ResponseObject.success(jwtToken);
    }

    /**
     * 创建token
     *
     * @param payloadMap 参数map
     * @return String
     * @throws JOSEException JOSEException
     */
    private static String creatToken(Map<String, Long> payloadMap) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        Payload payload = new Payload(new JSONObject(payloadMap));
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        JWSSigner jwsSigner = new MACSigner(JWT_SECRET_BYTES);
        jwsObject.sign(jwsSigner);
        return jwsObject.serialize();
    }

    /**
     * 解析token
     * Result-0 正确 Result-1 错误   Result-2 超时
     *
     * @param token token
     * @return Map<String, Object>
     * @throws ParseException ParseException
     * @throws JOSEException  JOSEException
     */
    private static Map<String, Object> valid(String token) throws ParseException, JOSEException {
        JWSObject jwsObject = JWSObject.parse(token);
        Payload payload = jwsObject.getPayload();
        JWSVerifier jwsVerifier = new MACVerifier(JWT_SECRET_BYTES);
        Map<String, Object> resultMap = new HashMap<>(8);
        if (jwsObject.verify(jwsVerifier)) {
            resultMap.put(RESULT, CALIBRATION_PASS);
            JSONObject jsonObject = payload.toJSONObject();
            resultMap.put(DATA, jsonObject.toJSONString());
            if (jsonObject.containsKey(EXP)) {
                Long expTime = Long.valueOf(jsonObject.get(EXP).toString());
                Long nowTime = System.currentTimeMillis();
                if (nowTime > expTime) {
                    resultMap.clear();
                    resultMap.put(RESULT, CHECK_FAILED_TOKEN_OVERDUE);
                }
            }
        } else {
            resultMap.put(RESULT, CHECK_FAILED_TOKEN_ERROR);
        }
        return resultMap;
    }
}
