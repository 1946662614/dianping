package com.hmdp.utils;

import ch.qos.logback.core.util.TimeUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName SimpleRedisLock
 * @Description Redis实现分布式锁
 * @Author 嘻精
 * @Date 2023/3/1 19:46
 * @Version 1.0
 */

public class SimpleRedisLock implements ILock{
    private String name;
    private StringRedisTemplate stringRedisTemplate;
    private static final String KEY_PREFIX = "lock";
    
    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }
    
    @Override
    public boolean tryLock(long timeoutSec) {
        // 获取线程的标识
        long threadId = Thread.currentThread().getId();
        // 获取锁
        Boolean success = stringRedisTemplate.opsForValue()
                                   .setIfAbsent(KEY_PREFIX + name, threadId + "", timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }
    
    @Override
    public void unLock() {
        //释放锁
        stringRedisTemplate.delete(KEY_PREFIX + name);
    }
}
