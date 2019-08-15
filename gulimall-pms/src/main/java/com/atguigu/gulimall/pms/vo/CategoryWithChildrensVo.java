package com.atguigu.gulimall.pms.vo;

import com.atguigu.gulimall.pms.entity.CategoryEntity;
import lombok.Data;

import java.util.List;

/**
 * @author heyijieyou
 * @date 2019-08-15 13:58
 */
@Data
public class CategoryWithChildrensVo extends CategoryEntity {

    private List<CategoryEntity> subs;
}
