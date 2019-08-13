package com.atguigu.gulimall.pms.vo;

import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.entity.AttrGroupEntity;
import lombok.Data;

/**
 * @author heyijieyou
 * @date 2019-08-04 19:47
 */
@Data
public class AttrWithGroupVo extends AttrEntity {

    private AttrGroupEntity group;
}
