package com.book.chat.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求DTO
 */
public class LoginRequest {
    
    @NotBlank(message = "用户名不能为空")
    private String username;  // 用户名
    
    @NotBlank(message = "密码不能为空")
    private String password;  // 密码
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
