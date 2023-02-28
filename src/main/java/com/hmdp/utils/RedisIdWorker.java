package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName RedisIdWorker
 * @Description ID生成器
 * @Author 嘻精
 * @Date 2023/2/28 16:17
 * @Version 1.0
 */
@Component
public class RedisIdWorker {
    /**
     *  开始时间戳
     */
    private static final long BEGIN_TIMESTAMP = 1672531200L;
    /**
     *  序列号位数
     */
    private static final int COUNT_BITS = 32;
    
    private StringRedisTemplate stringRedisTemplate;
    
    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    
    public long nextId(String keyPrefix) {
        // 1.生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        // 2.生成序列号
        // 2.1获取当前日期，精确到天
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 2.2自增类
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);
        // 3.拼接并返回
        return  timestamp << COUNT_BITS | count;
    }
    
    
}
