package com.atguigu.gulimall.sms.config;

import com.zaxxer.hikari.HikariDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author heyijieyou
 * @date 2019-08-07 00:44
 */
@Configuration
public class SmsGlobalTransactionConfig {
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource originDataSource(@Value("${spring.datasource.url}") String url){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(url);
        return hikariDataSource;
    }


    @Bean
    @Primary  //这个返回的对象是默认的数据源
    public DataSource dataSource(DataSource dataSource){

        return new DataSourceProxy(dataSource);
    }
}
