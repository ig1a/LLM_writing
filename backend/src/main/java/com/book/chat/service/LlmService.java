package com.book.chat.service;

import com.book.chat.dto.ChatRequest;
import com.book.chat.dto.ChatResponse;
import reactor.core.publisher.Mono;

/**
 * LLM服务接口
 * 提供与大语言模型交互的方法
 * 
 * 修改内容：
 * 1. 确保正确导入相关DTO类
 * 2. 提供大语言模型聊天服务
 */
public interface LlmService {
    
    /**
     * 发送消息到LLM并获取回复
     *
     * @param request 聊天请求对象，包含用户消息和历史记录
     * @return 聊天响应对象
     */
    Mono<ChatResponse> chat(ChatRequest request);
}
