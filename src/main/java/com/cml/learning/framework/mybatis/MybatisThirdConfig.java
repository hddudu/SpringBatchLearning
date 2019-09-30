package com.cml.learning.framework.mybatis;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 第三个数据源 读取数据库配置
 * Created by dudu on 2019/9/30.
 */
@Configuration
public class MybatisThirdConfig {
    protected static Log log = LogFactory.getLog(MybatisRConfig.class);

    @Bean(name = "thirdSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("thirdDataSource")DataSource thirdDataSource,
            @Qualifier("thirdConfiguration") MybatisThirdConfigProperties thirdConfiguration) throws Exception {
        log.info("*************************sqlSessionFactory:begin***********************" + thirdConfiguration);

        VFS.addImplClass(SpringBootVFS.class);

        //mybati 结合spring的中间类
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(thirdDataSource);//设置数据源
        sessionFactory.setTypeAliasesPackage(thirdConfiguration.typeAliasesPackage);//设置类型别名包
        sessionFactory.setTypeHandlersPackage(thirdConfiguration.typeHandlerPackage);//设置处理器类型包

        org.apache.ibatis.session.Configuration configuration=new org.apache.ibatis.session.Configuration();//定时mybatis的 配置类
        configuration.setMapUnderscoreToCamelCase(true);//
        sessionFactory.setConfiguration(configuration);//将配置设置到： mybatis-spring 的sql'Session'factoryBean中去

        //资源模板解析器
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();//路径匹配
        if (thirdConfiguration.mapperLocations.contains(",")) {
            List<Resource> resources = new ArrayList<>();
            //将所有的文件添加到资源中
            for (String s : thirdConfiguration.mapperLocations.split(",")) {
                resources.addAll(Arrays.asList(resolver.getResources(s)));
            }
            //将资源 xml文件（mapper的映射文件） 添加到sqlSessionFactoryBean中
            sessionFactory.setMapperLocations(resources.toArray(new Resource[] {}));
        } else {
            //只有一个路径： 直接添加到资源文件中给 sqlSessionFactoryBean
            sessionFactory.setMapperLocations(resolver.getResources(thirdConfiguration.mapperLocations));
        }

        // sessionFactory
        // .setConfigLocation(new
        // PathMatchingResourcePatternResolver().getResource(properties.configLocation));
        /*
          public SqlSessionFactory getObject() throws Exception {
                if(this.sqlSessionFactory == null) {
                    this.afterPropertiesSet();//如果sqlSessionFactory为空， 那么在初始化这个SqlSessionFactoryBean之后，再对SqlSessionFactory进行 注入
                    调用到： 这个方法；
                        事先做了一些必要的检测 ： Assert 使用断言
                     protected SqlSessionFactory buildSqlSessionFactory() throws IOException {}
                }
                return this.sqlSessionFactory;
            }
         */
        SqlSessionFactory resultSessionFactory = sessionFactory.getObject();

        log.info("*************************sqlSessionFactory:successs:" + resultSessionFactory + "***********************" + thirdConfiguration);

        return resultSessionFactory;

    }

    @Bean(destroyMethod = "close", name = "thirdDataSource")
    public DataSource dataSource(@Qualifier("thirdDataSourceProperties")DataSourceProperties thirdDataSource ) {
        log.info("*************************dataSource***********************");

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(thirdDataSource.driverClassName);
        dataSource.setUrl(thirdDataSource.url);
        dataSource.setUsername(thirdDataSource.username);
        dataSource.setPassword(thirdDataSource.password);
        dataSource.setMaxIdle(thirdDataSource.maxIdle);
        dataSource.setMaxActive(thirdDataSource.maxActive);
        dataSource.setMaxWait(thirdDataSource.maxWait);
        dataSource.setInitialSize(thirdDataSource.initialSize);
        dataSource.setValidationQuery(thirdDataSource.validationQuery);
        dataSource.setRemoveAbandoned(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(30000);
        dataSource.setNumTestsPerEvictionRun(30);
        dataSource.setMinEvictableIdleTimeMillis(1800000);
        return dataSource;
    }

    /**
     * mybatis的配置
     */
    @ConfigurationProperties(prefix = "db.mybatis.third")
    @Component("thirdConfiguration")
    public static class MybatisThirdConfigProperties {
        private String typeAliasesPackage;
        private String typeHandlerPackage;
        private String mapperLocations;
        private String configLocation;

        public String getConfigLocation() {
            return configLocation;
        }

        public void setConfigLocation(String configLocation) {
            this.configLocation = configLocation;
        }

        public String getTypeAliasesPackage() {
            return typeAliasesPackage;
        }

        public void setTypeAliasesPackage(String typeAliasesPackage) {
            this.typeAliasesPackage = typeAliasesPackage;
        }

        public String getTypeHandlerPackage() {
            return typeHandlerPackage;
        }

        public void setTypeHandlerPackage(String typeHandlerPackage) {
            this.typeHandlerPackage = typeHandlerPackage;
        }

        public String getMapperLocations() {
            return mapperLocations;
        }

        public void setMapperLocations(String mapperLocations) {
            this.mapperLocations = mapperLocations;
        }

        @Override
        public String toString() {
            return "MybatisConfigurationProperties [typeAliasesPackage=" + typeAliasesPackage + ", typeHandlerPackage=" + typeHandlerPackage
                    + ", mapperLocations=" + mapperLocations + ", configLocation=" + configLocation + "]";
        }

    }

    /**
     * 数据源的配置
     */
    @Component("thirdDataSourceProperties")
    @ConfigurationProperties(prefix = "db.mybatis.third.jdbc")
    public static class DataSourceProperties {
        public String driverClassName;
        public String url;
        public String username;
        public String password;
        public int maxActive;
        public int maxIdle;
        public int minIdle;
        public int maxWait;
        public int initialSize;
        public String validationQuery;

        @Override
        public String toString() {
            return "DataSourceProperties [driverClassName=" + driverClassName + ", url=" + url + ", username=" + username + ", password=" + password
                    + ", maxActive=" + maxActive + ", maxIdle=" + maxIdle + ", minIdle=" + minIdle + ", maxWait=" + maxWait + ", initialSize="
                    + initialSize + ", validationQuery=" + validationQuery + "]";
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getMaxActive() {
            return maxActive;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public int getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(int maxWait) {
            this.maxWait = maxWait;
        }

        public int getInitialSize() {
            return initialSize;
        }

        public void setInitialSize(int initialSize) {
            this.initialSize = initialSize;
        }

        public String getValidationQuery() {
            return validationQuery;
        }

        public void setValidationQuery(String validationQuery) {
            this.validationQuery = validationQuery;
        }

    }
}
