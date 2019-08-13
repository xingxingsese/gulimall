package com.atguigu.gulimall.pms.vo;

import com.atguigu.gulimall.pms.entity.SpuInfoEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author heyijieyou
 * @date 2019-08-05 20:24
 */
@Data
public class SpuAllSaveVo extends SpuInfoEntity {

    // 当前Spu的所有基本属性值
    private List<BaseAttrsVO> baseAttrs;

    // 当前spu对应的所有sku信息
    private List<SkuVO> skus;

    // 当前spu的详情图
    private String[] spuImages;
}

