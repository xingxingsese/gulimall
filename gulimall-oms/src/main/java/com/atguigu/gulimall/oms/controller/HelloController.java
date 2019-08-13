package com.atguigu.gulimall.oms.controller;

import com.atguigu.gulimall.oms.feign.worldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author heyijieyou
 * @date 2019-08-02 14:57
 */
@RefreshScope
@RestController
public class HelloController {

    @Autowired
    worldService worldService;

    @Value("${spring.datasource.url}")
    String url;

    @GetMapping("/hello")
    public String hello(){
        String msg = "";
        //远程调用gulimall-pms服务的/world 请求对应的方法,并接受返回值
        //msg = worldService.world();
        return "hello" + msg+"==>"+url;
    }
}
