package com.atguigu.gulimall.pms.feign;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.es.EsSkuVo;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author heyijieyou
 * @date 2019-08-10 17:46
 */
@FeignClient("gulimall-search")
public interface EsFeignService {
    @PostMapping("/es/spu/up")
    public Resp<Object> spuUp(@RequestBody List<EsSkuVo> vo);
}
