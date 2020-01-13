package com.flower.sea.commonservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author zhangLei
 * @serial 2018-01-28 上午11:03:50
 */
@Component
public class RedisUtils {
    
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisUtils(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

//    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public StringRedisTemplate getRedisTemplate() {
//        return this.redisTemplate;
//    }

    /**
     * 删除数据
     *
     * @param key key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 设置数据
     *
     * @param key   key
     * @param value value
     * @return boolean
     */
    public boolean set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存过期时间的数据
     *
     * @param key     key
     * @param value   value
     * @param timeout 过期时间
     * @param unit    时间单位, 天:TimeUnit.DAYS 小时:TimeUnit.HOURS 分钟:TimeUnit.MINUTES
     *                秒:TimeUnit.SECONDS 毫秒:TimeUnit.MILLISECONDS
     * @return boolean
     */
    public boolean set(String key, String value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据key获取val
     *
     * @param key key
     * @return String
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


}