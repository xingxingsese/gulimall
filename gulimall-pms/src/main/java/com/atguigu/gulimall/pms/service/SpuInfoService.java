package com.atguigu.gulimall.pms.service;

import com.atguigu.gulimall.pms.vo.BaseAttrsVO;
import com.atguigu.gulimall.pms.vo.SkuVO;
import com.atguigu.gulimall.pms.vo.SpuAllSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.pms.entity.SpuInfoEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * spu信息
 *
 * @author leifengyang
 * @email lfy@atguigu.com
 * @date 2019-08-01 15:52:32
 */
@Transactional// 事务
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageVo queryPage(QueryCondition params);

    // 根据分类查出分页信息
    PageVo queryPageByCatId(QueryCondition queryCondition, Long catId);

    void spuBigSaveAll(SpuAllSaveVo spuInfo);

    // 保存spu基本信息
    Long saveSpuBaseInfo(SpuAllSaveVo spuAllSaveVo);
    // 保存spu的图片信息
    void saveSpuInfoImages(Long spuId, String[] spuImages);

    // 保存spu的基本属性信息
    void saveSpuBaseAttrs(Long spuId, List<BaseAttrsVO> baseAttrs);
    // 保存
    void saveSkuinfos(Long spuId, List<SkuVO> skus);
    // 商品上下架功能
    void updateSpuStatus(Long spuId, Integer status);
}

