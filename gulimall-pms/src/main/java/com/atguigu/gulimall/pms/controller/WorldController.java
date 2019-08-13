package com.atguigu.gulimall.pms.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorldController {



    @GetMapping("/world")
    public String world(){

        //省略service调用
        String msg = "world ";
        return msg;
    }
}
