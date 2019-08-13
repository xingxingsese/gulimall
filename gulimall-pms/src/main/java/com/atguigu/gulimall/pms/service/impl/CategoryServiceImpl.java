package com.atguigu.gulimall.pms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import com.atguigu.gulimall.pms.dao.CategoryDao;
import com.atguigu.gulimall.pms.entity.CategoryEntity;
import com.atguigu.gulimall.pms.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageVo(page);
    }
    // 查询某个等级的所有分类
    @Override
    public List<CategoryEntity> getCategoryByLevel(Integer level) {

        // 创建查询条件的对象
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        if (level != 0) {

            // 传入参数和数据库中CategoryEntity这个表中这个cat_level列相等就存进去
            wrapper.eq("cat_level", level);
        }

        List<CategoryEntity> entities = categoryDao.selectList(wrapper);

        return entities;
    }

    // 获取指定分类的子分类
    @Override
    public List<CategoryEntity> getCategoryChildrensById(Integer catId) {

        // 创建查询条件的对象
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();

        // 根据传入的父类id 找到子类
        wrapper.eq("parent_cid",catId);

        // 把获取到的数据存入到集合中
        List<CategoryEntity> list = categoryDao.selectList(wrapper);

        return list;
    }

}