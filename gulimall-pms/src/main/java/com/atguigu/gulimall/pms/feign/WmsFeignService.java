package com.atguigu.gulimall.pms.feign;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author heyijieyou
 * @date 2019-08-10 20:05
 */
@FeignClient("gulimall-wms")
public interface WmsFeignService {
    @PostMapping("/wms/waresku/skus")
    public Resp<List<SkuStockVo>> skuWareInfo(@RequestBody List<Long> skuId);
}
