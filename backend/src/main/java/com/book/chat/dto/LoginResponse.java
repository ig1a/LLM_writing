package com.book.chat.dto;

/**
 * 登录响应DTO
 */
public class LoginResponse {
    
    private String token;     // JWT令牌
    
    private UserDTO user;     // 用户信息
    
    // 无参构造函数
    public LoginResponse() {
    }
    
    // 全参构造函数
    public LoginResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }
    
    // 手动添加getter和setter方法，解决Lombok可能未生效的问题
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
