package com.atguigu.gulimall.pms.service;

import com.atguigu.gulimall.pms.vo.CategoryWithChildrensVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.pms.entity.CategoryEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import java.util.List;


/**
 * 商品三级分类
 *
 * @author leifengyang
 * @email lfy@atguigu.com
 * @date 2019-08-01 15:52:32
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageVo queryPage(QueryCondition params);

    // 获取某个等级下的所有分类
    List<CategoryEntity> getCategoryByLevel(Integer level);

    // 获取指定分类的子分类
    List<CategoryEntity> getCategoryChildrensById(Integer catId);

    // 获取子分类的子分类
    List<CategoryWithChildrensVo> getCategoryChildrensAndSubsById(Integer id);
}

