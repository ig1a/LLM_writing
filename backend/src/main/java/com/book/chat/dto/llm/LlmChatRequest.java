package com.book.chat.dto.llm;

import java.util.List;

/**
 * LLM聊天请求对象
 * 用于构建发送给大模型API的请求格式
 */
public class LlmChatRequest {
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 消息列表，包含系统提示、用户输入和历史对话
     */
    private List<LlmMessage> messages;
    
    /**
     * 温度参数，控制输出的随机性，值越高回复越随机
     */
    private Float temperature = 0.7f;
    
    /**
     * 最大生成的token数量
     */
    private Integer max_tokens = 2000;
    
    /**
     * 获取模型名称
     * @return 模型名称
     */
    public String getModel() {
        return model;
    }
    
    /**
     * 设置模型名称
     * @param model 模型名称
     */
    public void setModel(String model) {
        this.model = model;
    }
    
    /**
     * 获取消息列表
     * @return 消息列表
     */
    public List<LlmMessage> getMessages() {
        return messages;
    }
    
    /**
     * 设置消息列表
     * @param messages 消息列表
     */
    public void setMessages(List<LlmMessage> messages) {
        this.messages = messages;
    }
    
    /**
     * 获取温度参数
     * @return 温度参数
     */
    public Float getTemperature() {
        return temperature;
    }
    
    /**
     * 设置温度参数
     * @param temperature 温度参数
     */
    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }
    
    /**
     * 获取最大token数量
     * @return 最大token数量
     */
    public Integer getMax_tokens() {
        return max_tokens;
    }
    
    /**
     * 设置最大token数量
     * @param max_tokens 最大token数量
     */
    public void setMax_tokens(Integer max_tokens) {
        this.max_tokens = max_tokens;
    }
}
