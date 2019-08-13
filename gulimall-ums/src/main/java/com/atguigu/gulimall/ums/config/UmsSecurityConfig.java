package com.atguigu.gulimall.ums.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author heyijieyou
 * @date 2019-08-01 23:30
 */
@Configuration
public class UmsSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);

        http.authorizeRequests().anyRequest().permitAll();
        http.csrf().disable();
    }
}
