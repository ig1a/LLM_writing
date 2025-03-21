package com.book.chat.dto;

import java.util.Map;

/**
 * 微信登录请求DTO
 * 用于接收小程序发送的登录请求参数
 */
public class WxLoginRequest {
    /**
     * 微信登录后获取的临时code
     */
    private String code;
    
    /**
     * 用户信息(可选)
     */
    private Map<String, Object> userInfo;
    
    /**
     * 获取登录code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 设置登录code
     */
    public void setCode(String code) {
        this.code = code;
    }
    
    /**
     * 获取用户信息
     */
    public Map<String, Object> getUserInfo() {
        return userInfo;
    }
    
    /**
     * 设置用户信息
     */
    public void setUserInfo(Map<String, Object> userInfo) {
        this.userInfo = userInfo;
    }
    
    @Override
    public String toString() {
        return "WxLoginRequest{" +
                "code='" + (code != null ? code.substring(0, Math.min(code.length(), 5)) + "..." : "null") + '\'' +
                ", userInfo=" + (userInfo != null ? "包含用户信息" : "无用户信息") +
                '}';
    }
}
