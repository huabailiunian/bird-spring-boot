package com.bird.spring.boot.jdbc.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author youly
 * @version 1.0
 * 2018/7/5 11:01
 */
@ConfigurationProperties(prefix = "bird.jdbc.transaction")
public class TransactionProperties {

    private Boolean enable;

    /**
     * 切点表达式
     * eg: execution(* com.spring.service.BusinessObject.*(..))
     */
    private String pointcutExpression;

    /**
     * 方法名字匹配事务配置
     * eg: 'query*:ISOLATION_DEFAULT,PROPAGATION_REQUIRED,timeout_20,readOnly,-Exception'
     */
    private String[] methodAttributes;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }


    public String getPointcutExpression() {
        return pointcutExpression;
    }

    public void setPointcutExpression(String pointcutExpression) {
        this.pointcutExpression = pointcutExpression;
    }

    public String[] getMethodAttributes() {
        return methodAttributes;
    }

    public void setMethodAttributes(String[] methodAttributes) {
        this.methodAttributes = methodAttributes;
    }
}
