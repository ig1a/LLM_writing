package com.book.chat.dto.llm;

/**
 * LLM消息对象
 * 用于构建发送给大模型API的消息格式
 * 
 * 修改功能:
 * 1. 移除Lombok依赖，改为手动实现构造函数和getter/setter方法
 * 2. 确保工厂方法正确调用构造函数
 */
public class LlmMessage {
    /**
     * 消息角色：system系统提示，user用户消息，assistant助手回复
     */
    private String role;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 无参数构造函数
     */
    public LlmMessage() {
    }
    
    /**
     * 全参数构造函数
     * 
     * @param role 消息角色
     * @param content 消息内容
     */
    public LlmMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
    
    /**
     * 获取消息角色
     * @return 消息角色
     */
    public String getRole() {
        return role;
    }
    
    /**
     * 设置消息角色
     * @param role 消息角色
     */
    public void setRole(String role) {
        this.role = role;
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
     * 创建用户消息
     * 
     * @param content 消息内容
     * @return 用户消息对象
     */
    public static LlmMessage userMessage(String content) {
        // 使用全参数构造器创建用户消息对象
        return new LlmMessage("user", content);
    }
    
    /**
     * 创建系统消息
     * 
     * @param content 消息内容
     * @return 系统消息对象
     */
    public static LlmMessage systemMessage(String content) {
        // 使用全参数构造器创建系统消息对象
        return new LlmMessage("system", content);
    }
    
    /**
     * 创建助手消息
     * 
     * @param content 消息内容
     * @return 助手消息对象
     */
    public static LlmMessage assistantMessage(String content) {
        // 使用全参数构造器创建助手消息对象
        return new LlmMessage("assistant", content);
    }
}
