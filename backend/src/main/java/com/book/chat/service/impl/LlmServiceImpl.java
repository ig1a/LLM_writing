package com.book.chat.service.impl;

import com.book.chat.dto.ChatMessageDTO;
import com.book.chat.dto.ChatRequest;
import com.book.chat.dto.ChatResponse;
import com.book.chat.dto.llm.LlmChatRequest;
import com.book.chat.dto.llm.LlmChatResponse;
import com.book.chat.dto.llm.LlmMessage;
import com.book.chat.service.LlmService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LLM服务实现类
 * 提供与DeepSeek和通义千问等大语言模型交互的能力
 * 
 * 修改内容：
 * 1. 确保所有DTO导入正确
 * 2. 修复编译错误
 * 3. 调整DeepSeek API请求体格式，符合最新文档要求
 * 4. 优化消息转换，正确处理历史消息
 * 5. 改进错误处理逻辑
 */
@Service
public class LlmServiceImpl implements LlmService {
    
    private final Logger logger = LoggerFactory.getLogger(LlmServiceImpl.class);
    
    /**
     * LLM提供商：deepseek 或 qwen
     */
    @Value("${app.llm.provider}")
    private String provider;
    
    /**
     * DeepSeek API地址
     */
    @Value("${app.llm.deepseek.api-url}")
    private String deepseekApiUrl;
    
    /**
     * DeepSeek API密钥
     */
    @Value("${app.llm.deepseek.api-key}")
    private String deepseekApiKey;
    
    /**
     * DeepSeek模型名称
     */
    @Value("${app.llm.deepseek.model}")
    private String deepseekModel;
    
    /**
     * 通义千问API地址
     */
    @Value("${app.llm.qwen.api-url}")
    private String qwenApiUrl;
    
    /**
     * 通义千问API密钥
     */
    @Value("${app.llm.qwen.api-key}")
    private String qwenApiKey;
    
    /**
     * 通义千问模型名称
     */
    @Value("${app.llm.qwen.model}")
    private String qwenModel;
    
    /**
     * API调用超时时间
     */
    @Value("${app.llm.timeout}")
    private long timeout;
    
    /**
     * WebClient实例，用于发送HTTP请求
     */
    private final WebClient webClient;
    
    /**
     * 构造函数，初始化WebClient
     */
    public LlmServiceImpl() {
        this.webClient = WebClient.builder().build();
    }
    
    /**
     * 发送消息到LLM并获取回复
     *
     * @param request 聊天请求对象，包含用户消息和历史记录
     * @return 聊天响应对象
     */
    @Override
    public Mono<ChatResponse> chat(ChatRequest request) {
        // 构建LLM请求对象
        LlmChatRequest llmRequest = buildLlmRequest(request);
        
        // 根据提供商选择不同的API
        if ("deepseek".equals(provider)) {
            return callDeepSeekApi(llmRequest, request);
        } else if ("qwen".equals(provider)) {
            return callQwenApi(llmRequest);
        } else {
            return Mono.just(ChatResponse.of("不支持的LLM提供商: " + provider));
        }
    }
    
    /**
     * 构建发送给LLM的请求对象
     *
     * @param request 前端发送的聊天请求
     * @return LLM请求对象
     */
    private LlmChatRequest buildLlmRequest(ChatRequest request) {
        // 创建消息列表
        List<LlmMessage> messages = new ArrayList<>();
        
        // 添加系统提示
        messages.add(LlmMessage.systemMessage("你是一个智能助手，可以回答用户关于书籍的问题。请用简洁、专业的语言回答。"));
        
        // 添加历史消息
        if (request.getHistory() != null) {
            for (ChatMessageDTO msg : request.getHistory()) {
                if ("user".equals(msg.getType())) {
                    messages.add(LlmMessage.userMessage(msg.getContent()));
                } else if ("ai".equals(msg.getType())) {
                    messages.add(LlmMessage.assistantMessage(msg.getContent()));
                }
            }
        }
        
        // 添加当前用户消息
        messages.add(LlmMessage.userMessage(request.getContent()));
        
        // 创建并返回LLM请求对象
        LlmChatRequest llmRequest = new LlmChatRequest();
        
        // 根据当前使用的提供商设置模型名称
        if ("deepseek".equals(provider)) {
            llmRequest.setModel(deepseekModel);
        } else if ("qwen".equals(provider)) {
            llmRequest.setModel(qwenModel);
        }
        
        llmRequest.setMessages(messages);
        return llmRequest;
    }
    
    /**
     * 将我们的消息格式转换为DeepSeek所需格式
     * @param request 聊天请求
     * @return DeepSeek格式的消息列表
     */
    private List<Map<String, String>> convertToDeepSeekMessages(ChatRequest request) {
        // 创建DeepSeek格式的消息列表
        List<Map<String, String>> messages = new ArrayList<>();
        
        // 添加系统角色提示
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "你是一个图书聊天助手，能够回答用户关于《" + request.getBookId() + "》的问题。");
        messages.add(systemMessage);
        
        // 添加历史消息（如果有）
        if (request.getHistory() != null) {
            for (ChatMessageDTO msg : request.getHistory()) {
                Map<String, String> historyMsg = new HashMap<>();
                // 转换消息类型为DeepSeek格式
                if ("user".equals(msg.getType())) {
                    historyMsg.put("role", "user");
                } else {
                    historyMsg.put("role", "assistant");
                }
                historyMsg.put("content", msg.getContent());
                messages.add(historyMsg);
            }
        }
        
        // 添加当前用户消息
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", request.getContent());
        messages.add(userMessage);
        
        return messages;
    }
    
    /**
     * 调用DeepSeek API
     *
     * @param request LLM请求对象
     * @param originalRequest 原始聊天请求，包含更多信息如书籍ID
     * @return 聊天响应对象
     */
    private Mono<ChatResponse> callDeepSeekApi(LlmChatRequest request, ChatRequest originalRequest) {
        // 构建符合DeepSeek API要求的请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", deepseekModel);
        requestBody.put("messages", convertToDeepSeekMessages(originalRequest)); // 使用转换后的消息格式
        // requestBody.put("stream", false); // 默认非流式响应，如需流式可设为true
        
        logger.info("发送到DeepSeek的请求: {}", requestBody);
        
        return webClient.post()
                .uri(deepseekApiUrl)
                .header("Authorization", "Bearer " + deepseekApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody) // 使用新的请求体格式
                .retrieve()
                .bodyToMono(Map.class) // 使用Map接收JSON响应
                .timeout(Duration.ofMillis(timeout))
                .map(response -> {
                    // 从Map中提取结果
                    logger.info("DeepSeek API 原始响应: {}", response);
                    
                    try {
                        // 解析响应内容
                        String content = extractContentFromDeepSeekResponse(response);
                        logger.info("DeepSeek API 提取的内容: {}", content);
                        return ChatResponse.of(content);
                    } catch (Exception e) {
                        logger.error("解析DeepSeek响应失败", e);
                        return ChatResponse.of("解析AI回复失败: " + e.getMessage());
                    }
                })
                .onErrorResume(e -> {
                    logger.error("调用DeepSeek API失败", e);
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException ex = (WebClientResponseException) e;
                        String errorBody = ex.getResponseBodyAsString();
                        logger.error("DeepSeek API 错误响应: {}", errorBody);
                        return Mono.just(ChatResponse.of("调用AI服务失败: " + ex.getMessage()));
                    }
                    return Mono.just(ChatResponse.of("调用AI服务失败: " + e.getMessage()));
                });
    }
    
    /**
     * 从DeepSeek响应中提取内容
     * 根据API文档，提取返回JSON中的消息内容
     *
     * @param response DeepSeek响应Map
     * @return 提取的文本内容
     */
    @SuppressWarnings("unchecked")
    private String extractContentFromDeepSeekResponse(Map<String, Object> response) {
        try {
            // 尝试提取choices[0].message.content
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> choice = choices.get(0);
                Map<String, Object> message = (Map<String, Object>) choice.get("message");
                if (message != null) {
                    return (String) message.get("content");
                }
            }
            
            // 如果上面的路径失败，尝试其他可能的路径
            if (response.containsKey("content")) {
                return (String) response.get("content");
            }
            
            // 如果都失败，返回错误信息
            return "无法从API响应中提取内容";
        } catch (Exception e) {
            logger.error("解析DeepSeek响应格式失败", e);
            return "解析AI回复格式失败: " + e.getMessage();
        }
    }
    
    /**
     * 调用通义千问API
     *
     * @param request LLM请求对象
     * @return 聊天响应对象
     */
    private Mono<ChatResponse> callQwenApi(LlmChatRequest request) {
        return webClient.post()
                .uri(qwenApiUrl)
                .header("Authorization", "Bearer " + qwenApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LlmChatResponse.class)
                .timeout(Duration.ofMillis(timeout))
                .map(response -> {
                    String content = response.getReplyContent();
                    logger.info("通义千问 API 响应: {}", content);
                    return ChatResponse.of(content);
                })
                .onErrorResume(e -> {
                    logger.error("调用通义千问 API失败", e);
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException ex = (WebClientResponseException) e;
                        String errorBody = ex.getResponseBodyAsString();
                        logger.error("通义千问 API 错误响应: {}", errorBody);
                        return Mono.just(ChatResponse.of("调用AI服务失败: " + ex.getMessage()));
                    }
                    return Mono.just(ChatResponse.of("调用AI服务失败: " + e.getMessage()));
                });
    }
}
