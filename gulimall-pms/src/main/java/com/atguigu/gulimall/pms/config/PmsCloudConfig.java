package com.atguigu.gulimall.pms.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
@EnableFeignClients(basePackages = "com.atguigu.gulimall.pms.feign")
@Configuration
@EnableDiscoveryClient
public class PmsCloudConfig {


}
