package com.atguigu.gulimall.pms.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author heyijieyou
 * @date 2019-08-05 22:34
 */
@Data
public class SkuVO {
    // sku名字
    private String skuName;

    // sku标题
    private String skuDesc;

    // sku副标题
    private String skuTitle;

    // sku重量
    private BigDecimal weight;

    // 商品价格
    private BigDecimal price;

    // sku图片
    private String[] images;

    // 销售属性组合
    private List<SaleAttrVo> saleAttrs;


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
