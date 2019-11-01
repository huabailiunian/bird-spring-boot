package com.bird.spring.boot.jdbc.properties;

import org.apache.ibatis.session.ExecutorType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author youly
 * @since 1.0
 * 2018/7/6 10:03
 */
@ConfigurationProperties(prefix = "bird.jdbc.mybatis")
public class MybatisProperties {

    /**
     * Locations of MyBatis mapper xml files.
     */
    private String[] mapperLocations;
    /**
     * Packages to search mapper interface. (Package delimiters are ","; \t\n")
     */
    private String interfacePackages;
    /**
     * Packages to search type aliases. (Package delimiters are ","; \t\n")
     */
    private String typeAliasesPackage;

    /**
     * Packages to search for type handlers. (Package delimiters are ","; \t\n")
     */
    private String typeHandlersPackage;
    /**
     * Execution mode for {@link org.mybatis.spring.SqlSessionTemplate}.
     */
    private ExecutorType executorType;

    /**
     * Externalized properties for MyBatis configuration.
     */
    private Properties configurationProperties;

    public String[] getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getInterfacePackages() {
        return interfacePackages;
    }

    public void setInterfacePackages(String interfacePackages) {
        this.interfacePackages = interfacePackages;
    }

    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public String getTypeHandlersPackage() {
        return typeHandlersPackage;
    }

    public void setTypeHandlersPackage(String typeHandlersPackage) {
        this.typeHandlersPackage = typeHandlersPackage;
    }

    public ExecutorType getExecutorType() {
        return executorType;
    }

    public void setExecutorType(ExecutorType executorType) {
        this.executorType = executorType;
    }

    public Properties getConfigurationProperties() {
        return configurationProperties;
    }

    public void setConfigurationProperties(Properties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public Resource[] resolveMapperLocations() {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();
        if (!ObjectUtils.isEmpty(mapperLocations)) {
            try {
                for (String mapperLocation : mapperLocations) {
                    Resource[] mappers = resourceResolver.getResources(mapperLocation);
                    resources.addAll(Arrays.asList(mappers));
                }
            } catch (IOException e) {
                //ignore
            }
        }
        return resources.toArray(new Resource[0]);
    }

}
