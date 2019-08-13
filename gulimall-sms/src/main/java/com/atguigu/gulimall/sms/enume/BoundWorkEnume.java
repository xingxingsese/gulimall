package com.atguigu.gulimall.sms.enume;

import lombok.Getter;

/**
 * @author heyijieyou
 * @date 2019-08-06 01:19
 */
@Getter
public enum BoundWorkEnume {

    ALLNO(0,"任何情况都无优惠"),
    ALLNOBUTGROWTHYES(1,"成长积分无论如何都送");

    private Integer code;

    private String msg;

    BoundWorkEnume(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
