package com.atguigu.gulimall.commons.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author heyijieyou
 * @date 2019-08-06 00:42
 */
@Data
public class SkuSaleInfoTo {

    private Long skuId;
    private BigDecimal growBounds;

    private BigDecimal buyBounds;

    private Integer[] work;
    // 以上是 积分设置的信息

    // 满几件
    private Integer fullCount;

    // 打几折
    private BigDecimal discount;

    // 阶梯价格
    private Integer ladderAddOther;


    private BigDecimal fullPrice;
    private BigDecimal reducePrice;

    private BigDecimal fullAddOther;
}
