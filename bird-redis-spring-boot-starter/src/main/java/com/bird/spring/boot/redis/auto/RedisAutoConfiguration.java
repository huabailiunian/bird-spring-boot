package com.bird.spring.boot.redis.auto;

import com.bird.core.executor.ExecutorConfig;
import com.bird.core.executor.ExecutorFactory;
import com.bird.redis.client.RedisClient;
import com.bird.redis.codec.CodecFactory;
import com.bird.redis.message.ConsumerContainer;
import com.bird.redis.message.RedisConsumer;
import com.bird.redis.message.impl.DefaultRedisConsumerContainer;
import com.bird.redis.message.impl.RedisMQClient;
import com.bird.spring.boot.redis.properties.RedisProperties;
import com.bird.spring.boot.redis.validator.AddressValidator;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * Redis 客户端配置类
 *
 * @author youly
 * @date 2019/1/2 14:32
 */
@Configuration
@ConditionalOnClass({RedissonClient.class, Redisson.class})
@ConditionalOnProperty(prefix = "bird.redis", name = "enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({RedisProperties.class})
public class RedisAutoConfiguration {

    private RedisProperties redisProperties;

    public RedisAutoConfiguration(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public RedissonClient redissonClient() {
        return Redisson.create(getRedisConfig());
    }

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public RedisClient redisClient(RedissonClient redissonClient) {
        RedisClient redisClient = new RedisClient(redissonClient);
        String keyGroup = redisProperties.getKeyGroup();
        if (StringUtils.hasText(keyGroup)) {
            redisClient.setKeyGroup(keyGroup);
        }
        return redisClient;
    }

    private Config getRedisConfig() {
        Config config = new Config();
        config.setCodec(CodecFactory.defaultCodec());
        String password = redisProperties.getPassword();
        RedisProperties.Cluster cluster = redisProperties.getCluster();
        if (Objects.nonNull(cluster)) {
            ClusterServersConfig clusterServers = config.useClusterServers();
            String[] nodes = cluster.getNodes().stream().map(AddressValidator::validate).toArray(String[]::new);
            clusterServers.addNodeAddress(nodes);
            clusterServers.setScanInterval(cluster.getScanInterval()).setReadMode(cluster.getReadMode());
            clusterServers.setMasterConnectionMinimumIdleSize(cluster.getMasterPool().getMinIdleSize())
                    .setMasterConnectionPoolSize(cluster.getMasterPool().getPoolSize())
                    .setSlaveConnectionMinimumIdleSize(cluster.getSlavePool().getMinIdleSize())
                    .setSlaveConnectionPoolSize(cluster.getSlavePool().getPoolSize());
            if (StringUtils.hasText(password)) {
                clusterServers.setPassword(password);
            }
        } else {
            RedisProperties.Single single = redisProperties.getSingle();
            if (Objects.nonNull(single)) {
                SingleServerConfig singleServer = config.useSingleServer();
                singleServer.setAddress(AddressValidator.validate(single.getHost()))
                        .setConnectionMinimumIdleSize(single.getPool().getMinIdleSize())
                        .setConnectionPoolSize(single.getPool().getPoolSize());
                if (StringUtils.hasText(password)) {
                    singleServer.setPassword(password);
                }
            }
        }
        return config;
    }

    @Bean
    @ConditionalOnProperty(prefix = "bird.redis.mq", name = "client", havingValue = "true", matchIfMissing = true)
    public RedisMQClient redisMQClient(RedisClient redisClient) {
        RedisMQClient client = new RedisMQClient(redisClient);
        client.setTimeout(redisProperties.getMq().getTimeout());
        return client;
    }

    @Bean
    @Qualifier(value = "redisMQExecutor")
    @ConditionalOnBean({RedisConsumer.class})
    @ConditionalOnProperty(prefix = "bird.redis.mq", name = "consumer", havingValue = "true", matchIfMissing = true)
    public AsyncListenableTaskExecutor redisMQExecutor() {
        ExecutorConfig config = redisProperties.getMq().getExecutorConfig();
        TaskExecutor executor = ExecutorFactory.createExecutor(config);
        return (AsyncListenableTaskExecutor) executor;
    }

    @Bean
    @ConditionalOnBean({RedisConsumer.class})
    @ConditionalOnProperty(prefix = "bird.redis.mq", name = "consumer", havingValue = "true", matchIfMissing = true)
    public ConsumerContainer redisConsumerContainer(RedisClient redisClient, @Qualifier(value = "redisMQExecutor") AsyncListenableTaskExecutor taskExecutor,
                                                    List<RedisConsumer> redisConsumers) {
        DefaultRedisConsumerContainer consumerContainer = new DefaultRedisConsumerContainer(redisClient);
        consumerContainer.setListenerTaskExecutor(taskExecutor);
        consumerContainer.setConsumers(redisConsumers);
        consumerContainer.setInstanceNum(redisProperties.getMq().getInstanceNumber());
        consumerContainer.setTimeout(redisProperties.getMq().getTimeout());
        return consumerContainer;
    }
}
