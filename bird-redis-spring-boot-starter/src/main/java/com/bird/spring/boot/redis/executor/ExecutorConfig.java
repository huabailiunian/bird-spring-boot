package com.bird.spring.boot.redis.executor;

/**
 * 线程池配置
 * 对于{@link ExecutorPoolType#CACHE}类型的线程池
 * 1.如果此时线程池中的数量小于{@link #corePoolSize}，即使线程池中的线程都处于空闲状态，也要创建新的线程来处理被添加的任务
 * 2.如果此时线程池中的数量等于{@link #corePoolSize}，但是缓冲队列的任务数小于{@link #queueCapacity}，那么任务被放入缓冲队列
 * 3.如果此时线程池中的数量大于{@link #corePoolSize}，缓冲队列任务数等于{@link #queueCapacity}，并且线程池中的数量小于{@link #maxPoolSize}，建新的线程来处理被添加的任务
 * 4.如果此时线程池中的数量大于{@link #corePoolSize}，缓冲队列任务数等于{@link #queueCapacity}，并且线程池中的数量等于{@link #maxPoolSize}，那么通过{@link #executorRejectPolicy}所指定的策略来处理此任务
 * 5.当线程池中的线程数量大于{@link #corePoolSize}时，如果某线程空闲时间超过{@link #keepAliveSeconds}，线程将被终止
 * 对于{@link ExecutorPoolType#TEMP}类型的线程池
 * 1.线程在有新任务时被创建，线程执行完后被是否
 * 2.使用{@link #concurrencyLimit}进行并发数限制
 *
 * @author youly
 * 2019/5/17 16:07
 */
public class ExecutorConfig {

    /**
     * 线程池的类型
     */
    private ExecutorPoolType executorPoolType = ExecutorPoolType.TEMP;

    /**
     * 线程池名称前缀
     */
    private String prefix;

    /**
     * 常存池大小
     */
    private int corePoolSize = 1;

    /**
     * 池最大值
     */
    private int maxPoolSize = 5;

    /**
     * 空闲线程保留时间 s
     */
    private int keepAliveSeconds = 30;

    /**
     * 等待队列最大限制
     */
    private int queueCapacity;

    /**
     * 超出 {@link #queueCapacity} 后对新提交请求的处理策略
     */
    private ExecutorRejectPolicy executorRejectPolicy = ExecutorRejectPolicy.ABORT;

    /**
     * 并发总数限制，当超过线程并发总数限制时，阻塞新的调用，直到有位置被释放
     */
    private int concurrencyLimit;


    public ExecutorPoolType getExecutorPoolType() {
        return executorPoolType;
    }

    public void setExecutorPoolType(ExecutorPoolType executorPoolType) {
        this.executorPoolType = executorPoolType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public ExecutorRejectPolicy getExecutorRejectPolicy() {
        return executorRejectPolicy;
    }

    public void setExecutorRejectPolicy(ExecutorRejectPolicy executorRejectPolicy) {
        this.executorRejectPolicy = executorRejectPolicy;
    }

    public int getConcurrencyLimit() {
        return concurrencyLimit;
    }

    public void setConcurrencyLimit(int concurrencyLimit) {
        this.concurrencyLimit = concurrencyLimit;
    }
}
