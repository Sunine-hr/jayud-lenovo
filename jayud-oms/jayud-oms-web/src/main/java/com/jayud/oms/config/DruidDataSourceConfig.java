//package com.jayud.oms.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import io.seata.rm.datasource.DataSourceProxy;
//import org.apache.ibatis.session.SqlSession;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//import javax.xml.crypto.Data;
//
//@Configuration
//public class DruidDataSourceConfig {
//
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        DruidDataSource dataSource = new DruidDataSource();
//        return dataSource;
//    }
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Bean
//    public SqlSessionFactoryBean sqlSessionFactoryBean() {
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        return sqlSessionFactoryBean;
//    }
//
//    @Bean
//    @ConditionalOnBean(DataSource.class)
//    public DataSourceProxy dataSourceProxy() {
//        return new DataSourceProxy(dataSource);
//    }
//}
