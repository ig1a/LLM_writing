package com.book.chat.dto;

import java.time.LocalDateTime;

/**
 * 聊天响应DTO
 * 用于返回给前端的聊天响应数据
 */
public class ChatResponse {
    
    /**
     * 响应消息内容
     */
    private String message;
    
    /**
     * 时间戳
     */
    private String timestamp;
    
    /**
     * 获取响应消息内容
     * @return 响应消息内容
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * 设置响应消息内容
     * @param message 响应消息内容
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * 获取时间戳
     * @return 时间戳
     */
    public String getTimestamp() {
        return timestamp;
    }
    
    /**
     * 设置时间戳
     * @param timestamp 时间戳
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * 创建一个包含消息和当前时间的响应对象
     * 
     * @param message 响应消息
     * @return 响应对象
     */
    public static ChatResponse of(String message) {
        ChatResponse response = new ChatResponse();
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now().toString());
        return response;
    }
}
