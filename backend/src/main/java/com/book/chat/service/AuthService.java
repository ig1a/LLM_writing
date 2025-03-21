package com.book.chat.service;

import java.util.Map;

/**
 * 用户认证服务接口
 */
public interface AuthService {
    
    /**
     * 处理微信登录
     * 
     * @param code 微信登录凭证
     * @param userInfo 用户信息
     * @return 包含token和用户信息的结果
     */
    Map<String, Object> wxLogin(String code, Map<String, Object> userInfo);
    
    /**
     * 检查用户登录状态
     * 
     * @param token 用户token
     * @return 登录状态及用户信息
     */
    Map<String, Object> checkLogin(String token);
}
