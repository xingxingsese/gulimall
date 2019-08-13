package com.guigu.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 解决跨域问题
 * @author heyijieyou
 * @date 2019-08-03 16:48
 */
@Configuration // 配置文件
public class GulimallGateWayConfig {

    @Bean
    public CorsWebFilter corsWebFilter(){

        // 跨域的配置
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*"); // 允许所有方法
        config.addAllowedHeader("*");
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);// 允许带cookie跨域
        // 创建一个基于url地址的跨域配置对象数据源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",config); // 注册跨域配置
        CorsWebFilter filter = new CorsWebFilter(source);

        return filter;
    }
}
