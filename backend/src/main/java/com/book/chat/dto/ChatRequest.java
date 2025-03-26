package com.book.chat.dto;

import java.util.List;

/**
 * 聊天请求DTO
 * 用于接收前端发送的聊天请求
 * 
 * 修改内容：
 * 1. 移除Lombok依赖，改为手动实现getter/setter方法
 * 2. 添加中文注释说明
 */
public class ChatRequest {
    
    /**
     * 书籍ID
     */
    private String bookId;
    
    /**
     * 用户消息内容
     */
    private String content;
    
    /**
     * 聊天历史记录
     */
    private List<ChatMessageDTO> history;
    
    /**
     * 获取书籍ID
     * @return 书籍ID
     */
    public String getBookId() {
        return bookId;
    }
    
    /**
     * 设置书籍ID
     * @param bookId 书籍ID
     */
    public void setBookId(String bookId) {
        this.bookId = bookId;
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
     * 获取聊天历史记录
     * @return 聊天历史记录列表
     */
    public List<ChatMessageDTO> getHistory() {
        return history;
    }
    
    /**
     * 设置聊天历史记录
     * @param history 聊天历史记录列表
     */
    public void setHistory(List<ChatMessageDTO> history) {
        this.history = history;
    }
}
