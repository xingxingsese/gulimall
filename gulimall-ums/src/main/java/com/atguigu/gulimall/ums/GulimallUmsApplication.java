package com.atguigu.gulimall.ums;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RefreshScope
@MapperScan(basePackages = "com.atguigu.gulimall.ums.dao")
@SpringBootApplication
public class GulimallUmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallUmsApplication.class, args);
    }

}
