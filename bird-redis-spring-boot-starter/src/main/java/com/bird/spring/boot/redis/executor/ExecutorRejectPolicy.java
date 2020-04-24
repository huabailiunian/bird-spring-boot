package com.bird.spring.boot.redis.executor;

import java.util.concurrent.RejectedExecutionException;

/**
 * @author youly
 * 2019/5/17 16:09
 */
public enum ExecutorRejectPolicy {

    /**
     * 抛出 {@link RejectedExecutionException}
     */
    ABORT,

    /**
     * 在当前线程中立即运行
     */
    CALLER_RUNS,

    /**
     * 放弃最久的等待者
     */
    DISCARD_OLDEST,

    /**
     * 放弃当前
     */
    DISCARD;
}
