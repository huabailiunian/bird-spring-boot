package com.bird.spring.boot.jdbc.auto;

import com.alibaba.druid.pool.DruidDataSource;
import com.bird.spring.boot.jdbc.DataSourceWrapper;
import com.bird.spring.boot.jdbc.SpringBootVFS;
import com.bird.spring.boot.jdbc.properties.MybatisProperties;
import com.bird.spring.boot.jdbc.properties.TransactionProperties;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @author youly
 * 2019/7/3 17:02
 */
@Configuration
@AutoConfigureBefore({DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({DataSourceProperties.class, MybatisProperties.class, TransactionProperties.class})
public class JdbcAutoConfiguration {

    private static Logger logger = LoggerFactory.getLogger(JdbcAutoConfiguration.class);

    private MybatisProperties mybatisProperties;
    private TransactionProperties transactionProperties;
    private final Interceptor[] interceptors;
    private final DatabaseIdProvider databaseIdProvider;

    public JdbcAutoConfiguration(MybatisProperties mybatisProperties, TransactionProperties transactionProperties, ObjectProvider<Interceptor[]> interceptors, ObjectProvider<DatabaseIdProvider> databaseIdProvider) {
        this.mybatisProperties = mybatisProperties;
        this.transactionProperties = transactionProperties;
        this.interceptors = interceptors.getIfAvailable();
        this.databaseIdProvider = databaseIdProvider.getIfAvailable();
    }

    @Bean(initMethod = "init")
    @ConditionalOnClass({DruidDataSource.class})
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        logger.info("init datasource type [{}]", DruidDataSource.class.getName());
        return new DataSourceWrapper();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({DataSource.class})
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setVfs(SpringBootVFS.class);

        if (this.mybatisProperties.getConfigurationProperties() != null) {
            factoryBean.setConfigurationProperties(this.mybatisProperties.getConfigurationProperties());
        }

        if (!ObjectUtils.isEmpty(this.interceptors)) {
            factoryBean.setPlugins(this.interceptors);
        }

        if (this.databaseIdProvider != null) {
            factoryBean.setDatabaseIdProvider(this.databaseIdProvider);
        }

        if (StringUtils.hasLength(this.mybatisProperties.getTypeAliasesPackage())) {
            factoryBean.setTypeAliasesPackage(this.mybatisProperties.getTypeAliasesPackage());
        }

        if (StringUtils.hasLength(this.mybatisProperties.getTypeHandlersPackage())) {
            factoryBean.setTypeHandlersPackage(this.mybatisProperties.getTypeHandlersPackage());
        }

        Resource[] resources = this.mybatisProperties.resolveMapperLocations();
        if (!ObjectUtils.isEmpty(resources)) {
            factoryBean.setMapperLocations(resources);
        }
        return factoryBean.getObject();
    }

    @Bean
    @ConditionalOnBean({SqlSessionFactory.class})
    @ConditionalOnMissingBean
    public SqlSession sqlSession(SqlSessionFactory sessionFactory) {
        ExecutorType executorType = this.mybatisProperties.getExecutorType();
        return executorType != null ? new SqlSessionTemplate(sessionFactory, executorType) : new SqlSessionTemplate(sessionFactory);
    }

    /**
     * {@link org.mybatis.spring.annotation.MapperScan} ultimately ends up
     * creating instances of {@link MapperFactoryBean}. If
     * {@link org.mybatis.spring.annotation.MapperScan} is used then this
     * auto-configuration is not needed. If it is _not_ used, however, then this
     * will bring in a bean registrar and automatically register components based
     * on the same component-scanning path as Spring Boot itself.
     */
    @Configuration
    @Import({AutoConfiguredMapperScannerRegistrar.class})
    @ConditionalOnMissingBean(MapperFactoryBean.class)
    public static class MapperScannerRegistrarNotFoundConfiguration {
        @PostConstruct
        public void afterPropertiesSet() {
            if (logger.isDebugEnabled()) {
                logger.debug("No {} found.", MapperFactoryBean.class.getName());
            }
        }
    }

    public static class AutoConfiguredMapperScannerRegistrar implements EnvironmentAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

        private Environment environment;
        private ResourceLoader resourceLoader;

        @Override
        public void setEnvironment(Environment environment) throws BeansException {
            this.environment = environment;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            if (logger.isDebugEnabled()) {
                logger.debug("Searching for mapper interfaces with packages");
            }
            String property = environment.getProperty("bird.jdbc.mybatis.interface-packages");
            String property2 = environment.getProperty("bird.jdbc.mybatis.interfacePackages");
            String[] strings = StringUtils.commaDelimitedListToStringArray(property);
            String[] strings2 = StringUtils.commaDelimitedListToStringArray(property2);
            String[] packages = StringUtils.concatenateStringArrays(strings, strings2);
            if (!ObjectUtils.isEmpty(packages)) {
                ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
                scanner.setSqlSessionTemplateBeanName("sqlSession");
                try {
                    if (logger.isDebugEnabled()) {
                        for (String pkg : packages) {
                            logger.debug("Using mapper configuration base package '{}'", pkg);
                        }
                    }
                    if (this.resourceLoader != null) {
                        scanner.setResourceLoader(this.resourceLoader);
                    }
                    scanner.registerFilters();
                    scanner.doScan(packages);
                } catch (IllegalStateException ex) {
                    logger.debug("Could not determine configuration package, automatic mapper scanning disabled.", ex);
                }
            }
        }
    }


}
