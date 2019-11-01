package com.bird.spring.boot.redis.properties;

import com.bird.core.executor.ExecutorConfig;
import org.redisson.config.ReadMode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

/**
 * redis配置类
 *
 * @author youly
 * 2018/12/29 11:07
 */
@ConfigurationProperties(prefix = "bird.redis")
public class RedisProperties {

    private boolean enable;
    private String keyGroup = "dev";
    private String password;

    private Single single = new Single();
    private Cluster cluster;
    private MQ mq = new MQ();

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getKeyGroup() {
        return keyGroup;
    }

    public void setKeyGroup(String keyGroup) {
        this.keyGroup = keyGroup;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Single getSingle() {
        return single;
    }

    public void setSingle(Single single) {
        this.single = single;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public MQ getMq() {
        return mq;
    }

    public void setMq(MQ mq) {
        this.mq = mq;
    }

    public static class Pool {

        private int minIdleSize = 10;

        private int poolSize = 50;

        public int getMinIdleSize() {
            return minIdleSize;
        }

        public void setMinIdleSize(int minIdleSize) {
            this.minIdleSize = minIdleSize;
        }

        public int getPoolSize() {
            return poolSize;
        }

        public void setPoolSize(int poolSize) {
            this.poolSize = poolSize;
        }
    }

    /**
     * 单机模式
     */
    public static class Single {

        private String host = "127.0.0.1:6379";

        /**
         * 连接池参数
         */
        private Pool pool = new Pool();

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Pool getPool() {
            return pool;
        }

        public void setPool(Pool pool) {
            this.pool = pool;
        }
    }

    /**
     * 主从集群模式
     */
    public static class Cluster {

        /**
         * 集群节点 HOST:PORT
         */
        private List<String> nodes;

        /**
         * Redis 集群 扫描间隔(毫秒)
         */
        private Integer scanInterval = 1000;

        /**
         * 集群读取模式
         */
        private ReadMode readMode = ReadMode.SLAVE;

        /**
         * 主节点连接池参数
         */
        private Pool masterPool = new Pool();

        /**
         * 从节点连接池参数
         */
        private Pool slavePool = new Pool();

        public List<String> getNodes() {
            return nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }

        public Integer getScanInterval() {
            return scanInterval;
        }

        public void setScanInterval(Integer scanInterval) {
            this.scanInterval = scanInterval;
        }

        public ReadMode getReadMode() {
            return readMode;
        }

        public void setReadMode(ReadMode readMode) {
            this.readMode = readMode;
        }

        public Pool getMasterPool() {
            return masterPool;
        }

        public void setMasterPool(Pool masterPool) {
            this.masterPool = masterPool;
        }

        public Pool getSlavePool() {
            return slavePool;
        }

        public void setSlavePool(Pool slavePool) {
            this.slavePool = slavePool;
        }
    }

    public static class MQ {
        private boolean client;
        private boolean consumer;
        private long timeout = 10000L;
        private int instanceNumber = 1;
        @NestedConfigurationProperty
        private ExecutorConfig executorConfig = new ExecutorConfig();

        public boolean isClient() {
            return client;
        }

        public void setClient(boolean client) {
            this.client = client;
        }

        public boolean isConsumer() {
            return consumer;
        }

        public void setConsumer(boolean consumer) {
            this.consumer = consumer;
        }

        public long getTimeout() {
            return timeout;
        }

        public void setTimeout(long timeout) {
            this.timeout = timeout;
        }

        public int getInstanceNumber() {
            return instanceNumber;
        }

        public void setInstanceNumber(int instanceNumber) {
            this.instanceNumber = instanceNumber;
        }

        public ExecutorConfig getExecutorConfig() {
            return executorConfig;
        }

        public void setExecutorConfig(ExecutorConfig executorConfig) {
            this.executorConfig = executorConfig;
        }
    }
}
