package com.book.chat.controller;

import com.book.chat.dto.WxLoginRequest;
import com.book.chat.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理认证相关的请求
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 处理微信登录请求
     * @param request 包含code和用户信息的请求
     * @return 包含token和用户信息的响应
     */
    @PostMapping("/wx-login")
    public ResponseEntity<?> wxLogin(@RequestBody WxLoginRequest request) {
        try {
            // 输出日志，帮助调试
            System.out.println("接收到微信登录请求：" + request);
            
            // 参数验证
            if (request.getCode() == null || request.getCode().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "登录失败: code不能为空");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // 调用服务处理微信登录
            Map<String, Object> loginResult = authService.wxLogin(request.getCode(), request.getUserInfo());
            return ResponseEntity.ok(loginResult);
        } catch (Exception e) {
            // 打印详细错误信息到控制台
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "登录失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 检查用户登录状态
     * @param authorization 用户token (从请求头中获取)
     * @return 登录状态及用户信息
     */
    @GetMapping("/check-login")
    public ResponseEntity<?> checkLogin(@RequestHeader(value = "Authorization", required = false) String authorization) {
        try {
            // 验证Authorization头
            if (authorization == null || authorization.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("isLogin", false);
                errorResponse.put("message", "未提供登录凭证");
                return ResponseEntity.ok(errorResponse);  // 返回200但表示未登录，而不是401
            }
            
            // 从Authorization头中提取token
            String token = authorization.replace("Bearer ", "");
            
            // 检查token是否有效
            Map<String, Object> checkResult = authService.checkLogin(token);
            return ResponseEntity.ok(checkResult);
        } catch (Exception e) {
            // 打印详细错误信息到控制台
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("isLogin", false);
            errorResponse.put("message", "校验登录状态失败: " + e.getMessage());
            return ResponseEntity.ok(errorResponse);  // 返回200而不是错误状态码
        }
    }
}
