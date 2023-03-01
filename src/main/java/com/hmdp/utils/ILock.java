package com.hmdp.utils;

/**
 * @ClassName ILock
 * @Description TODO
 * @Author 嘻精
 * @Date 2023/3/1 19:45
 * @Version 1.0
 */

public interface ILock {
    /**
     * 尝试获取锁
     * @param timeoutSec
     * @return t：获取锁成功 f:失败
     */
    boolean tryLock(long timeoutSec);
    
    /**
     * 释放锁
     */
    void unLock();
}
