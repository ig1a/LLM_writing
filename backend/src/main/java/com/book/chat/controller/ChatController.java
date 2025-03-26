package com.book.chat.controller;

import com.book.chat.dto.ChatRequest;
import com.book.chat.dto.ChatResponse;
import com.book.chat.service.LlmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * 聊天控制器
 * 处理与聊天相关的HTTP请求
 * 
 * 修改内容：
 * 1. 修复导入依赖问题
 * 2. 确保正确处理聊天请求和响应
 * 3. 添加详细的中文注释
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    /**
     * 日志记录器
     */
    private final Logger logger = LoggerFactory.getLogger(ChatController.class);
    
    /**
     * LLM服务，用于处理AI聊天功能
     */
    @Autowired
    private LlmService llmService;
    
    /**
     * 发送聊天消息
     * 接收用户的聊天消息，并获取AI回复
     *
     * @param request 聊天请求对象，包含书籍ID、用户消息和历史记录
     * @return 聊天响应对象，包含AI回复内容
     */
    @PostMapping("/messages")
    public Mono<ResponseEntity<ChatResponse>> sendMessage(@RequestBody ChatRequest request) {
        // 记录接收到的用户消息
        logger.info("接收到聊天请求: bookId={}, content={}", request.getBookId(), request.getContent());
        
        // 调用LLM服务获取回复
        return llmService.chat(request)
                .map(response -> {
                    // 记录AI的回复内容
                    logger.info("AI回复: {}", response.getMessage());
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    // 记录错误并返回错误响应
                    logger.error("处理聊天请求失败", e);
                    ChatResponse errorResponse = ChatResponse.of("服务器处理请求失败: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(500).body(errorResponse));
                });
    }
}
