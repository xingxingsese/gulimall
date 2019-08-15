package com.atguigu.gulimall.pms.controller.api;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.pms.entity.CategoryEntity;
import com.atguigu.gulimall.pms.service.CategoryService;
import com.atguigu.gulimall.pms.vo.CategoryWithChildrensVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author heyijieyou
 * @date 2019-08-15 11:52
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @ApiOperation("获取所有的一级分类")
    @GetMapping("/cates")
    public Resp<Object> level1Catelogs(){

        List<CategoryEntity> childrens = categoryService.getCategoryChildrensById(0);

        return Resp.ok(childrens);
    }

    @ApiOperation("获取所有的一级分类的子分类")
    @GetMapping("/cates/{id}")
    public Resp<Object> catelogChildrens(@PathVariable Integer id){

        List<CategoryWithChildrensVo> childrens = categoryService.getCategoryChildrensAndSubsById(id);

        return Resp.ok(childrens);
    }
}
