package com.atguigu.gulimall.pms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * 1、配置文件中
 * mybatis-plus:
 *   mapper-locations: classpath:/mapper/pms/*.xml
 *
 * 2、使用@MapperScan扫描所有mapper接口
 *
 */
@EnableAspectJAutoProxy(exposeProxy = true) // 暴漏代理对象
@EnableTransactionManagement// 开启基于注解的事务功能
@RefreshScope
@MapperScan(basePackages = "com.atguigu.gulimall.pms.dao")
@SpringBootApplication
public class GulimallPmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallPmsApplication.class, args);
    }

}
