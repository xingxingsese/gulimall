package com.atguigu.gulimall.sms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author heyijieyou
 * @date 2019-08-01 23:16
 */
@Configuration
public class smsSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http);
        http.authorizeRequests().anyRequest().permitAll();
        http.csrf().disable();
    }
}
