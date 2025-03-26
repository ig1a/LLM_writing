package com.book.chat.dto;

/**
 * 聊天消息DTO
 * 用于表示聊天中的单条消息
 */
public class ChatMessageDTO {
    
    /**
     * 消息类型：user用户消息，ai机器人消息
     */
    private String type;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息时间
     */
    private String time;
    
    /**
     * 获取消息类型
     * @return 消息类型
     */
    public String getType() {
        return type;
    }
    
    /**
     * 设置消息类型
     * @param type 消息类型
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * 获取消息内容
     * @return 消息内容
     */
    public String getContent() {
        return content;
    }
    
    /**
     * 设置消息内容
     * @param content 消息内容
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * 获取消息时间
     * @return 消息时间
     */
    public String getTime() {
        return time;
    }
    
    /**
     * 设置消息时间
     * @param time 消息时间
     */
    public void setTime(String time) {
        this.time = time;
    }
}
