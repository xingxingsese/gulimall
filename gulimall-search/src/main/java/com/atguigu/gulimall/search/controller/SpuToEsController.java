package com.atguigu.gulimall.search.controller;

import com.atguigu.gulimall.commons.bean.Constant;
import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.es.EsSkuVo;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author heyijieyou
 * @date 2019-08-10 17:40
 */
@RestController
@RequestMapping("/es")
public class SpuToEsController {

    @Autowired
    JestClient jestClient;

    /**
     * 商品上架
     */
    @PostMapping("/spu/up")
    public Resp<Object> spuUp(@RequestBody List<EsSkuVo> vo) {

        vo.forEach(vo1 -> {
            Index index = new Index.Builder(vo1).index(Constant.ES_SPU_INDEX)
                    .type(Constant.ES_SPU_TYPE)
                    .id(vo1.getId().toString())
                    .build();
            try {
                jestClient.execute(index);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return Resp.ok(null);
    }
}
