package com.bot.botconnector.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().and()
                .authorizeRequests()
                .antMatchers("/actuator/info", "/actuator/health").permitAll()
                .antMatchers("/actuator/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions().disable();
        //.contentSecurityPolicy("frame-ancestors 'none'");
    }
}

