package com.book.chat.config;

import com.book.chat.service.impl.AuthServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * JWT Token过滤器
 * 用于处理HTTP请求中的JWT令牌，并设置Spring Security上下文
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AuthServiceImpl authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 获取Authorization头
        String authHeader = request.getHeader("Authorization");
        
        // 打印请求路径和认证头，用于调试
        System.out.println("请求路径: " + request.getRequestURI());
        System.out.println("Authorization头: " + (authHeader != null ? authHeader.substring(0, Math.min(authHeader.length(), 20)) + "..." : "null"));
        
        // 跳过不需要认证的路径
        if (request.getRequestURI().startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 检查是否有Authorization头且以Bearer开头
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("没有有效的Authorization头，拒绝访问");
            filterChain.doFilter(request, response);
            return;
        }
        
        // 从Authorization头中提取token
        String token = authHeader.substring(7);
        
        try {
            // 从AuthService获取session信息
            Map<String, Object> sessionInfo = authService.getSessionInfo(token);
            
            if (sessionInfo != null) {
                // 获取openid
                String openid = (String) sessionInfo.get("openid");
                
                if (openid != null) {
                    // 创建认证对象并设置到SecurityContext
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            openid, // principal
                            null, // credentials (不需要)
                            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")) // 授权
                        );
                    
                    // 设置认证到上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("认证成功: " + openid.substring(0, Math.min(openid.length(), 6)) + "...");
                }
            } else {
                System.out.println("无效的token: " + token.substring(0, Math.min(token.length(), 10)) + "...");
            }
        } catch (Exception e) {
            // 处理异常
            System.err.println("处理token时出错: " + e.getMessage());
        }
        
        // 继续执行过滤器链
        filterChain.doFilter(request, response);
    }
}
