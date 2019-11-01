package com.bird.spring.boot.zookeeper.auto;

import com.bird.spring.boot.zookeeper.properties.ZkProperties;
import com.bird.zookeeper.service.ZkService;
import com.bird.zookeeper.service.ZkServiceImpl;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

/**
 * @author youly
 * 2019/10/25 15:22
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass({CuratorFramework.class, ZooKeeper.class})
@EnableConfigurationProperties({ZkProperties.class})
@ConditionalOnProperty(prefix = ZkProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class ZkAutoConfiguration {

    private ZkProperties zkProperties;

    public ZkAutoConfiguration(ZkProperties zkProperties) {
        this.zkProperties = zkProperties;
    }

    @Bean
    public CuratorFramework curatorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zkProperties.getRetrySleepTime(), zkProperties.getMaxRetries());
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zkProperties.getAddress())
                .sessionTimeoutMs(zkProperties.getSessionTimeout())
                .connectionTimeoutMs(zkProperties.getConnectionTimeout())
                .retryPolicy(retryPolicy);
        if (StringUtils.hasText(zkProperties.getGroup())) {
            builder.namespace(zkProperties.getGroup());
        }
        CuratorFramework framework = builder.build();
        framework.start();
        return framework;
    }

    @Bean
    public ZkService zkService(CuratorFramework curatorFramework) {
        return new ZkServiceImpl(curatorFramework);
    }

}
