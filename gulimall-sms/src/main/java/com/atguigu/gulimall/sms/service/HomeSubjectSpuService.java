package com.atguigu.gulimall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.sms.entity.HomeSubjectSpuEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;


/**
 * 专题商品
 *
 * @author heyijieyou
 * @email zuihou098@qq.com
 * @date 2019-08-01 20:13:11
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

    PageVo queryPage(QueryCondition params);
}

