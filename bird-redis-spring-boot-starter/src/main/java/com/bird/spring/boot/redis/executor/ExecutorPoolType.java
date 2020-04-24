package com.bird.spring.boot.redis.executor;

/**
 * @author youly
 * 2019/5/17 16:07
 */
public enum ExecutorPoolType {
    /**
     * 每次都会启用新线程执行任务，执行完成后线程被丢弃
     */
    TEMP,
    /**
     * 真正的线程池
     */
    CACHE
}
