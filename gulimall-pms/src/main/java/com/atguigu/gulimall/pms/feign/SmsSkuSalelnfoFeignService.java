package com.atguigu.gulimall.pms.feign;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuSaleInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author heyijieyou
 * @date 2019-08-06 00:56
 */
@FeignClient("gulimall-sms")
public interface SmsSkuSalelnfoFeignService {
    @PostMapping("/sms/skubounds/saleinfo/save")
    public Resp<Object> saveSkuSaleInfos(@RequestBody List<SkuSaleInfoTo> to);
}
