package com.bird.spring.boot.redis.executor;

import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author youly
 * 2019/5/17 16:30
 */
public class ExecutorFactory {

    private static final Map<ExecutorRejectPolicy, RejectedExecutionHandler> POLICIES;

    static {
        POLICIES = new HashMap<>();
        POLICIES.put(ExecutorRejectPolicy.ABORT, new ThreadPoolExecutor.AbortPolicy());
        POLICIES.put(ExecutorRejectPolicy.CALLER_RUNS, new ThreadPoolExecutor.CallerRunsPolicy());
        POLICIES.put(ExecutorRejectPolicy.DISCARD_OLDEST, new ThreadPoolExecutor.DiscardOldestPolicy());
        POLICIES.put(ExecutorRejectPolicy.DISCARD, new ThreadPoolExecutor.DiscardPolicy());
    }

    public static TaskExecutor createExecutor(ExecutorConfig config) {
        if (ExecutorPoolType.TEMP.equals(config.getExecutorPoolType())) {
            SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
            if (StringUtils.hasText(config.getPrefix())) {
                taskExecutor.setThreadNamePrefix(config.getPrefix() + "-");
            }
            if (config.getConcurrencyLimit() > 0) {
                taskExecutor.setConcurrencyLimit(config.getConcurrencyLimit());
            }
            return taskExecutor;
        } else {
            ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
            if (StringUtils.hasText(config.getPrefix())) {
                taskExecutor.setThreadNamePrefix(config.getPrefix() + "-");
            }
            taskExecutor.setCorePoolSize(config.getCorePoolSize());
            taskExecutor.setMaxPoolSize(config.getMaxPoolSize());
            taskExecutor.setKeepAliveSeconds(config.getKeepAliveSeconds());
            if (config.getQueueCapacity() > 0) {
                taskExecutor.setQueueCapacity(config.getQueueCapacity());
            }
            RejectedExecutionHandler rejectedExecutionHandler = POLICIES.get(config.getExecutorRejectPolicy());
            taskExecutor.setRejectedExecutionHandler(rejectedExecutionHandler);
            return taskExecutor;
        }
    }

}
