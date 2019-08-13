package com.atguigu.gulimall.oms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author heyijieyou
 * @date 2019-08-01 22:42
 */

@MapperScan(basePackages = "com.atguigu.gulimall.oms.dao")
@SpringBootApplication
@RefreshScope
public class GulimallOmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallOmsApplication.class,args);
    }
}
