package com.bird.spring.boot.jdbc;

import com.alibaba.druid.filter.config.ConfigFilter;
import com.alibaba.druid.filter.encoding.EncodingConvertFilter;
import com.alibaba.druid.filter.logging.CommonsLogFilter;
import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author youly
 * @version 1.0
 * 2018/7/6 9:31
 */
@ConfigurationProperties(prefix = "bird.jdbc.datasource")
public class DataSourceWrapper extends DruidDataSource implements InitializingBean {

    @Autowired
    DataSourceProperties basicProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (super.getUsername() == null) {
            super.setUsername(this.basicProperties.determineUsername());
        }

        if (super.getPassword() == null) {
            super.setPassword(this.basicProperties.determinePassword());
        }

        if (super.getUrl() == null) {
            super.setUrl(this.basicProperties.determineUrl());
        }

        if (super.getDriverClassName() == null) {
            super.setDriverClassName(this.basicProperties.determineDriverClassName());
        }
    }

    @Autowired(required = false)
    public void addStatFilter(StatFilter statFilter) {
        super.filters.add(statFilter);
    }

    @Autowired(required = false)
    public void addConfigFilter(ConfigFilter configFilter) {
        super.filters.add(configFilter);
    }

    @Autowired(required = false)
    public void addEncodingConvertFilter(EncodingConvertFilter encodingConvertFilter) {
        super.filters.add(encodingConvertFilter);
    }

    @Autowired(required = false)
    public void addSlf4jLogFilter(Slf4jLogFilter slf4jLogFilter) {
        super.filters.add(slf4jLogFilter);
    }

    @Autowired(required = false)
    public void addLog4jFilter(Log4jFilter log4jFilter) {
        super.filters.add(log4jFilter);
    }

    @Autowired(required = false)
    public void addLog4j2Filter(Log4j2Filter log4j2Filter) {
        super.filters.add(log4j2Filter);
    }

    @Autowired(required = false)
    public void addCommonsLogFilter(CommonsLogFilter commonsLogFilter) {
        super.filters.add(commonsLogFilter);
    }

    @Autowired(required = false)
    public void addWallFilter(WallFilter wallFilter) {
        super.filters.add(wallFilter);
    }
}
