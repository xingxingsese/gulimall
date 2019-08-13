package com.atguigu.gulimall.oms.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author heyijieyou
 * @date 2019-08-02 14:54
 */
@Service
@FeignClient(name = "gulimall-pms") // 被远程调用的服务名
public interface worldService {
    @GetMapping("/world") // 被远程调用的请求方法和映射
    public String world(); // 被远程调用的方法名
}
