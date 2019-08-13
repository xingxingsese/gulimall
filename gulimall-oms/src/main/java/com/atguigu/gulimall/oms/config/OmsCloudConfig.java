package com.atguigu.gulimall.oms.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author heyijieyou
 * @date 2019-08-02 14:50
 */
@Configuration // 配置类
@EnableDiscoveryClient // 开启服务注册发现功能
@EnableFeignClients(basePackages = "com.atguigu.gulimall.oms.feign")// 开启feign远程调用功能并指定接口位置
public class OmsCloudConfig {




}
