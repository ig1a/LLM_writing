1.pom文件
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.chat</groupId>
    <artifactId>chat_demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>chat_demo</name>
    <description>chat_demo</description>
    <properties>
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <spring-boot.version>2.6.13</spring-boot.version>
    </properties>
    <dependencies>

        <!-- 添加Thymeleaf依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.8.35</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.30</version>
            <scope>provided</scope>
        </dependency>


        <!-- HTTP客户端 -->
        <dependency>
            <groupId>com.squareup.okhttp3</groupId>
            <artifactId>okhttp</artifactId>
            <version>4.9.0</version>
        </dependency>



        <dependency>
            <groupId>com.mashape.unirest</groupId>
            <artifactId>unirest-java</artifactId>
            <version>1.4.9</version>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.3.6</version>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpasyncclient</artifactId>
            <version>4.0.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpmime</artifactId>
            <version>4.3.6</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${spring-boot.version}</version>
                <configuration>
                    <mainClass>com.chat.chat_demo.ChatDemoApplication</mainClass>
                    <skip>true</skip>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>

2.springboot配置文件 application.yaml
ai:
  config:
    deepseek:
      apiKey: 填写官网申请的Key
      baseUrl: https://api.deepseek.com/chat/completions
server:
  port: 8080

spring:
  thymeleaf:
    prefix: classpath:/templates/
    suffix: .html

3.编写controller接口
package com.chat.chat_demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("deepSeek")
@Slf4j
public class OpenAIController {

    @Value("${ai.config.deepseek.apiKey}")
    private String API_KEY;

    @Value("${ai.config.deepseek.baseUrl}")
    private String API_URL;

    // 用于保存每个用户的对话历史
    //https://api.deepseek.com/chat/completions 此接口为无状态接口,需要上下文连贯对话需要将历史聊天记录一并发送至接口中
    private final Map<String, List<Map<String, String>>> sessionHistory = new ConcurrentHashMap<>();

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping()
    public ModelAndView chat(ModelAndView modelAndView) {
        modelAndView.setViewName("chat");
        return modelAndView;
    }


    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(
//            @RequestHeader("Authorization") String token,
            @RequestBody String question) {
        // 假设 token 是用户的唯一标识
//        String userId = token; // 或者从 token 中解析出用户 ID
        String userId = "123"; // 或者从 token 中解析出用户 ID

        SseEmitter emitter = new SseEmitter(-1L);
        executorService.execute(() -> {
            try {
                log.info("流式回答开始, 问题: {}", question);

                // 获取当前用户的对话历史
                List<Map<String, String>> messages = sessionHistory.getOrDefault(userId, new ArrayList<>());

                // 添加用户的新问题到对话历史
                Map<String, String> userMessage = new HashMap<>();
                userMessage.put("role", "user");
                userMessage.put("content", question);

                Map<String, String> systemMessage = new HashMap<>();
                systemMessage.put("role", "system");
                systemMessage.put("content", "聚城网络科技有限公司的物业管理助手");
                messages.add(userMessage);
                messages.add(systemMessage);

                // 调用 Deepseek API
                try (CloseableHttpClient client = HttpClients.createDefault()) {
                    HttpPost request = new HttpPost(API_URL);
                    request.setHeader("Content-Type", "application/json");
                    request.setHeader("Authorization", "Bearer " + API_KEY);

                    Map<String, Object> requestMap = new HashMap<>();
                    requestMap.put("model", "deepseek-chat");
//                    requestMap.put("model", "deepseek-reasoner");
                    requestMap.put("messages", messages); // 包含对话历史
                    requestMap.put("stream", true);

                    String requestBody = objectMapper.writeValueAsString(requestMap);
                    request.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));

                    try (CloseableHttpResponse response = client.execute(request);
                         BufferedReader reader = new BufferedReader(
                                 new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))) {
                        StringBuilder aiResponse = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("data: ")) {
                                System.err.println(line);
                                String jsonData = line.substring(6);
                                if ("[DONE]".equals(jsonData)) {
                                    break;
                                }
                                JsonNode node = objectMapper.readTree(jsonData);
                                String content = node.path("choices")
                                        .path(0)
                                        .path("delta")
                                        .path("content")
                                        .asText("");
                                if (!content.isEmpty()) {
                                    emitter.send(content);
                                    aiResponse.append(content); // 收集 AI 的回复
                                }
                            }
                        }

                        // 将 AI 的回复添加到对话历史
                        Map<String, String> aiMessage = new HashMap<>();
                        aiMessage.put("role", "assistant");
                        aiMessage.put("content", aiResponse.toString());
                        messages.add(aiMessage);

                        // 更新会话状态
                        sessionHistory.put(userId, messages);

                        log.info("流式回答结束, 问题: {}", question);
                        emitter.complete();
                    }
                } catch (Exception e) {
                    log.error("处理 Deepseek 请求时发生错误", e);
                    emitter.completeWithError(e);
                }
            } catch (Exception e) {
                log.error("处理 Deepseek 请求时发生错误", e);
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}

4.编写前端界面 chat.html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DeepSeek Chat</title>
    <style>
        :root {
            --primary-color: #5b8cff;
            --user-bg: linear-gradient(135deg, #5b8cff 0%, #3d6ef7 100%);
            --bot-bg: linear-gradient(135deg, #f0f8ff 0%, #e6f3ff 100%);
            --shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
        }

        body {
            font-family: 'Segoe UI', system-ui, -apple-system, sans-serif;
            margin: 0;
            padding: 20px;
            display: flex;
            justify-content: center;
            min-height: 100vh;
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        }

        .chat-container {
            width: 100%;
            max-width: 800px;
            height: 90vh;
            background: rgba(255, 255, 255, 0.95);
            border-radius: 20px;
            box-shadow: var(--shadow);
            backdrop-filter: blur(10px);
            display: flex;
            flex-direction: column;
            overflow: hidden;
        }

        .chat-header {
            padding: 24px;
            background: var(--primary-color);
            color: white;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        .chat-header h1 {
            margin: 0;
            font-size: 1.8rem;
            font-weight: 600;
            letter-spacing: -0.5px;
        }

        .chat-messages {
            flex: 1;
            padding: 20px;
            overflow-y: auto;
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .chat-message {
            max-width: 75%;
            padding: 16px 20px;
            border-radius: 20px;
            line-height: 1.5;
            animation: messageAppear 0.3s ease-out;
            position: relative;
            word-break: break-word;
        }

        .chat-message.user {
            background: var(--user-bg);
            color: white;
            align-self: flex-end;
            border-bottom-right-radius: 4px;
            box-shadow: var(--shadow);
        }

        .chat-message.bot {
            background: var(--bot-bg);
            color: #2d3748;
            align-self: flex-start;
            border-bottom-left-radius: 4px;
            box-shadow: var(--shadow);
        }

        .chat-input {
            padding: 20px;
            background: rgba(255, 255, 255, 0.9);
            border-top: 1px solid rgba(0, 0, 0, 0.05);
            display: flex;
            gap: 12px;
        }

        .chat-input input {
            flex: 1;
            padding: 14px 20px;
            border: 2px solid rgba(0, 0, 0, 0.1);
            border-radius: 16px;
            font-size: 1rem;
            transition: all 0.2s ease;
            background: rgba(255, 255, 255, 0.8);
        }

        .chat-input input:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(91, 140, 255, 0.2);
        }

        .chat-input button {
            padding: 12px 24px;
            border: none;
            border-radius: 16px;
            background: var(--primary-color);
            color: white;
            font-size: 1rem;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.2s ease;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .chat-input button:hover {
            background: #406cff;
            transform: translateY(-1px);
        }

        .chat-input button:disabled {
            background: #c2d1ff;
            cursor: not-allowed;
            transform: none;
        }

        .chat-input button svg {
            width: 18px;
            height: 18px;
            fill: currentColor;
        }

        @keyframes messageAppear {
            from {
                opacity: 0;
                transform: translateY(10px);
            }

            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .typing-indicator {
            display: inline-flex;
            gap: 6px;
            padding: 12px 20px;
            background: var(--bot-bg);
            border-radius: 20px;
            align-self: flex-start;
        }

        .typing-dot {
            width: 8px;
            height: 8px;
            background: rgba(0, 0, 0, 0.3);
            border-radius: 50%;
            animation: typing 1.4s infinite ease-in-out;
        }

        .typing-dot:nth-child(2) {
            animation-delay: 0.2s;
        }

        .typing-dot:nth-child(3) {
            animation-delay: 0.4s;
        }

        @keyframes typing {

            0%,
            100% {
                transform: translateY(0);
            }

            50% {
                transform: translateY(-4px);
            }
        }

        @media (max-width: 640px) {
            body {
                padding: 10px;
            }

            .chat-container {
                height: 95vh;
                border-radius: 16px;
            }

            .chat-message {
                max-width: 85%;
            }
        }
    </style>
</head>

<body>
    <div class="chat-container">
        <div class="chat-header">
            <h1>聚城网络科技有限公司 DeepSeek Chat</h1>
        </div>
        <div class="chat-messages" id="chatMessages"></div>
        <div class="chat-input">
            <input type="text" id="questionInput" placeholder="输入消息..." onkeydown="handleKeyDown(event)">
            <button id="sendButton" disabled>
                <svg viewBox="0 0 24 24">
                    <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z" />
                </svg>
                发送
            </button>
        </div>
    </div>

    <script>
        const questionInput = document.getElementById('questionInput');
        const sendButton = document.getElementById('sendButton');
        const chatMessages = document.getElementById('chatMessages');
        let isBotResponding = false;

        // 输入验证
        questionInput.addEventListener('input', () => {
            sendButton.disabled = questionInput.value.trim().length === 0;
        });

        // 回车发送
        function handleKeyDown(e) {
            if (e.key === 'Enter' && !sendButton.disabled && !isBotResponding) {
                sendButton.click();
            }
        }

        // 修改后的消息处理逻辑
        let currentBotMessage = null; // 当前正在更新的AI消息

        async function handleBotResponse(response) {
            const reader = response.body.getReader();
            const decoder = new TextDecoder();
            let buffer = '';

            currentBotMessage = displayMessage('bot', '');

            try {
                while (true) {
                    const { done, value } = await reader.read();
                    if (done) {
                        // 处理最后剩余的数据
                        if (buffer) processLine(buffer);
                        break;
                    }

                    buffer += decoder.decode(value, { stream: true });
                    const lines = buffer.split('\n');

                    // 保留未完成的行在缓冲区
                    buffer = lines.pop() || '';

                    lines.forEach(line => {
                        if (line.startsWith('data:')) {
                            const data = line.replace(/^data:\s*/g, '').trim();
                            if (data === '[DONE]') return;
                            currentBotMessage.textContent += data;
                        }
                    });

                    chatMessages.scrollTop = chatMessages.scrollHeight;
                }
            } finally {
                currentBotMessage = null;
            }
        }
        // 发送逻辑
        sendButton.addEventListener('click', async () => {
            if (isBotResponding) return;

            const question = questionInput.value.trim();
            if (!question) return;

            questionInput.value = '';
            sendButton.disabled = true;
            isBotResponding = true;

            // 显示用户消息（新消息始终出现在下方）
            displayMessage('user', question);

            try {
                // 显示加载状态
                const typingIndicator = createTypingIndicator();

                const response = await fetch('/deepSeek/chat', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'text/event-stream'
                    },
                    body: JSON.stringify({ question }),
                });

                typingIndicator.remove();
                await handleBotResponse(response); // 处理流式响应

            } catch (error) {
                displayMessage('bot', '暂时无法处理您的请求，请稍后再试');
            } finally {
                isBotResponding = false;
                questionInput.focus();
            }
        });

        // 创建消息元素
        function displayMessage(sender, content) {
            const messageDiv = document.createElement('div');
            messageDiv.className = `chat-message ${sender}`;
            messageDiv.textContent = content;
            chatMessages.appendChild(messageDiv);
            chatMessages.scrollTop = chatMessages.scrollHeight;
            return messageDiv;
        }

        // 创建输入指示器
        function createTypingIndicator() {
            const container = document.createElement('div');
            container.className = 'typing-indicator';
            container.innerHTML = `
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
            `;
            chatMessages.appendChild(container);
            chatMessages.scrollTop = chatMessages.scrollHeight;
            return container;
        }

        // 更新 AI 消息
        // let currentBotMessage = null;
        // function updateBotMessage(content) {
        //     if (!currentBotMessage) {
        //         currentBotMessage = displayMessage('bot', content);
        //     } else {
        //         currentBotMessage.textContent = content;
        //     }
        //     chatMessages.scrollTop = chatMessages.scrollHeight;
        // }
    </script>
</body>

</html>
