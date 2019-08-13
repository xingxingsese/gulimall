package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.pms.dao.AttrDao;
import com.atguigu.gulimall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.vo.AttrGroupWithAttrsVo;
import com.atguigu.gulimall.pms.vo.AttrWithGroupVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import com.atguigu.gulimall.pms.dao.AttrGroupDao;
import com.atguigu.gulimall.pms.entity.AttrGroupEntity;
import com.atguigu.gulimall.pms.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    AttrDao attrDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageVo(page);
    }

    // 查询某个三级分类下的所有属性分组
    @Override
    public PageVo queryPageAttrGroupsByCatId(QueryCondition queryCondition, Long catId) {
        // 获取封装的分页条件
        IPage<AttrGroupEntity> page = new Query<AttrGroupEntity>().getPage(queryCondition);

        // 获取查询条件
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId);

        IPage<AttrGroupEntity> data = this.page(page, wrapper);

        // 查出的所有分组
        List<AttrGroupEntity> records = data.getRecords();

        // 将要返回出去的带分组信息和他的属性信息 的对象
        List<AttrGroupWithAttrsVo> groupWithAttrs = new ArrayList<>(records.size());

        for (AttrGroupEntity record : records) {
            // 创建一个vo准备收集所有数据
            AttrGroupWithAttrsVo vo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(record, vo);

            Long groupId = record.getAttrGroupId();
            // 获取当前分组的所有属性
            List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", groupId));

            if (relationEntities != null && relationEntities.size() > 0) {

                List<Long> attrIds = new ArrayList<>();
                for (AttrAttrgroupRelationEntity entity : relationEntities) {

                    attrIds.add(entity.getAttrId());
                }
                // 查出当前分组所有真正的属性
                List<AttrEntity> attrEntities = attrDao.selectList(new QueryWrapper<AttrEntity>().in("attr_id", attrIds));
                vo.setAttrEntities(attrEntities);
            }

            groupWithAttrs.add(vo);
        }

        return new PageVo(groupWithAttrs, data.getTotal(), data.getSize(), data.getCurrent());
    }

    /**
     * 根据属性id查出属性所在分组信息
     *
     * @param attrId
     * @return
     */
    @Override
    public AttrGroupEntity getGroupInfoByAttrId(Long attrId) {

        AttrGroupEntity attrGroupEntity = null;
        // 根据attrId去中间表查出在哪个组
        AttrAttrgroupRelationEntity one = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id", attrId));


        // 根据分组的id找到分组信息
        if (one != null) {
            Long groupId = one.getAttrGroupId();
            attrGroupEntity = attrGroupDao.selectById(groupId);
        }
//        Long groupId = one.getAttrGroupId();
//            attrGroupEntity = attrGroupDao.selectById(groupId);
        return attrGroupEntity;
    }


}