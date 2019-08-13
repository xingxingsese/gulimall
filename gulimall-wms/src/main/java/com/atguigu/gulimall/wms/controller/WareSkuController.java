package com.atguigu.gulimall.wms.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuStockVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.wms.entity.WareSkuEntity;
import com.atguigu.gulimall.wms.service.WareSkuService;


/**
 * 商品库存
 *
 * @author heyijieyou
 * @email zuihou098@qq.com
 * @date 2019-08-01 20:19:24
 */
@Api(tags = "商品库存 管理")
@RestController
@RequestMapping("wms/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    @PostMapping("/skus")
    public Resp<List<SkuStockVo>> skuWareInfo(@RequestBody List<Long> skuId) {

        List<WareSkuEntity> list = wareSkuService.list(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId));

        List<SkuStockVo> vos = new ArrayList<>();

        list.forEach(item -> {
            SkuStockVo vo = new SkuStockVo();
            BeanUtils.copyProperties(item, vo);
            vos.add(vo);
        });
        return Resp.ok(vos);
    }

    @ApiOperation("获取某个sku的库存信息")
    @GetMapping("sku/{skuId}")
    public Resp<List<WareSkuEntity>> skuWareInfo(@PathVariable("skuId") Long skuId) {

        List<WareSkuEntity> list = wareSkuService.list(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId));


        return Resp.ok(list);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('wms:waresku:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = wareSkuService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('wms:waresku:info')")
    public Resp<WareSkuEntity> info(@PathVariable("id") Long id) {
        WareSkuEntity wareSku = wareSkuService.getById(id);

        return Resp.ok(wareSku);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('wms:waresku:save')")
    public Resp<Object> save(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.save(wareSku);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('wms:waresku:update')")
    public Resp<Object> update(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.updateById(wareSku);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('wms:waresku:delete')")
    public Resp<Object> delete(@RequestBody Long[] ids) {
        wareSkuService.removeByIds(Arrays.asList(ids));

        return Resp.ok(null);
    }

}
