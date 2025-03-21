package com.book.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置类
 * 配置API访问权限和认证规则
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * 配置HTTP安全规则
     * 允许微信登录和检查登录状态的API无需认证访问
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF，因为这是API服务
            .csrf(csrf -> csrf.disable())
            // 配置请求授权规则
            .authorizeHttpRequests(auth -> auth
                // 允许微信登录相关接口无需认证
                .requestMatchers("/api/auth/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            // 禁用HTTP Basic认证弹窗
            .httpBasic(basic -> basic.disable());
        
        return http.build();
    }
}
