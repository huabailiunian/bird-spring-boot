package com.bird.spring.boot.zookeeper.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author youly
 * 2019/10/25 15:19
 */
@ConfigurationProperties(prefix = ZkProperties.PREFIX)
public class ZkProperties {

    public static final String PREFIX = "bird.zk";

    /**
     * 是否启用zk
     */
    private boolean enable = true;
    /**
     * 连接地址
     */
    private String address;
    /**
     * 根路径
     */
    private String group;
    /**
     * 会话超时时间
     */
    private int sessionTimeout;
    /**
     * 连接超时时间
     */
    private int connectionTimeout;
    /**
     * 重试等待时间
     */
    private int retrySleepTime = 3000;
    /**
     * 最大重试次数
     */
    private int maxRetries = 3;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getRetrySleepTime() {
        return retrySleepTime;
    }

    public void setRetrySleepTime(int retrySleepTime) {
        this.retrySleepTime = retrySleepTime;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public String toString() {
        return "ZkProperties{" +
                "enable=" + enable +
                ", address='" + address + '\'' +
                ", group='" + group + '\'' +
                ", sessionTimeout=" + sessionTimeout +
                ", connectionTimeout=" + connectionTimeout +
                ", retrySleepTime=" + retrySleepTime +
                ", maxRetries=" + maxRetries +
                '}';
    }
}
