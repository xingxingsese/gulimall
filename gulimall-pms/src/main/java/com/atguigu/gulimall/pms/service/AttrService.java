package com.atguigu.gulimall.pms.service;

import com.atguigu.gulimall.pms.vo.AttrSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;


/**
 * 商品属性
 *
 * @author leifengyang
 * @email lfy@atguigu.com
 * @date 2019-08-01 15:52:32
 */
public interface AttrService extends IService<AttrEntity> {

    PageVo queryPage(QueryCondition params);

    // 查询某个三级分类下的所有基本属性
    PageVo queryPageCatelogBaseAttrs(QueryCondition queryCondition, Long catId,Integer attrType);

    void saveAttrAndRelation(AttrSaveVo attr);
}

