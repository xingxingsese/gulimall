package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.pms.vo.AttrSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import com.atguigu.gulimall.pms.dao.AttrDao;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrDao attrDao;

    @Autowired
    AttrAttrgroupRelationDao relationDao;
    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageVo(page);
    }

    // 查询某个三级分类下的所有基本属性
    @Override
    public PageVo queryPageCatelogBaseAttrs(QueryCondition queryCondition, Long catId,Integer attrType) {
        // 获取封装的分页条件
        IPage<AttrEntity> page = new Query<AttrEntity>().getPage(queryCondition);
        // 获取查询条件
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catId)
                .eq("attr_type", attrType);

        IPage<AttrEntity> data = this.page(page, wrapper);

        return new PageVo(data);
    }

    // 属性保存
    @Transactional // 添加事务 因为下面步骤1和步骤2都成功才能保存
    @Override
    public void saveAttrAndRelation(AttrSaveVo attr) {

        // 1 先把属性保存在属性表中
        AttrEntity attrEntity = new AttrEntity();
        // 属性对拷
        BeanUtils.copyProperties(attr,attrEntity);
        // 存入数据库
        attrDao.insert(attrEntity);

        // 2 建立关联关系
        Long attrId = attrEntity.getAttrId(); // 获取它的id
        Long attrGroupId = attr.getAttrGroupId(); // 获取它的分组id

        // 创建关联关系表对象
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(attrId);
        relationEntity.setAttrGroupId(attrGroupId);
        relationDao.insert(relationEntity);
    }


}