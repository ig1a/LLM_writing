package com.book.chat.service.impl;

import com.book.chat.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    // 存储用户session信息
    private final Map<String, Map<String, Object>> sessions = new ConcurrentHashMap<>();
    
    // 存储用户信息
    private final Map<String, Map<String, Object>> users = new ConcurrentHashMap<>();
    
    @Value("${wx.appid}")
    private String appId;
    
    @Value("${wx.secret}")
    private String appSecret;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 处理微信登录
     */
    @Override
    public Map<String, Object> wxLogin(String code, Map<String, Object> userInfo) {
        try {
            // 打印调试信息
            System.out.println("处理微信登录，appId: " + appId);
            System.out.println("处理微信登录，appSecret前4位: " + (appSecret != null && appSecret.length() > 4 ? appSecret.substring(0, 4) + "..." : "null"));
            System.out.println("处理微信登录，登录code: " + (code != null ? code.substring(0, Math.min(code.length(), 6)) + "..." : "null"));
            
            if (appId == null || appId.isEmpty()) {
                throw new RuntimeException("微信小程序appid未正确配置，请在application.yml中设置wx.appid");
            }
            
            if (appSecret == null || appSecret.isEmpty()) {
                throw new RuntimeException("微信小程序secret未正确配置，请在application.yml中设置wx.secret");
            }
            
            // 构建微信登录URL
            String url = "https://api.weixin.qq.com/sns/jscode2session" +
                    "?appid=" + appId +
                    "&secret=" + appSecret +
                    "&js_code=" + code +
                    "&grant_type=authorization_code";
            
            System.out.println("请求微信接口: " + url.replaceAll(appSecret, "****"));
            
            // ===== 测试环境下模拟登录 =====
            // 当使用测试环境时，微信API可能无法正常工作，我们使用模拟数据
            // 注意：仅用于开发测试，生产环境请移除此代码
            boolean useTestMode = true; // 设置为true启用测试模式，false则使用真实微信API
            if (useTestMode) {
                System.out.println("*** 使用测试模式登录 ***");
                
                // 生成测试用的openid
                String testOpenid = "test_" + UUID.randomUUID().toString().substring(0, 8);
                String testSessionKey = "test_session_" + UUID.randomUUID().toString().substring(0, 8);
                
                // 生成自定义登录凭证token
                String token = "tk_" + UUID.randomUUID().toString().replace("-", "");
                
                // 保存session信息
                Map<String, Object> sessionInfo = new HashMap<>();
                sessionInfo.put("openid", testOpenid);
                sessionInfo.put("session_key", testSessionKey);
                sessionInfo.put("created_at", new Date());
                sessions.put(token, sessionInfo);
                
                // 保存或更新用户信息
                Map<String, Object> testUserInfo = new HashMap<>();
                if (userInfo != null) {
                    testUserInfo.putAll(userInfo);
                }
                testUserInfo.put("openid", testOpenid);
                testUserInfo.put("nickname", "测试用户");
                users.put(testOpenid, testUserInfo);
                
                System.out.println("测试模式登录成功，生成token: " + token);
                
                // 返回登录结果
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("token", token);
                result.put("userInfo", testUserInfo);
                
                return result;
            }
            
            // 请求微信接口
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            System.out.println("微信接口响应状态码: " + response.getStatusCode().value());
            System.out.println("微信接口响应内容: " + response.getBody());
            
            // 解析响应JSON
            @SuppressWarnings("unchecked")
            Map<String, Object> wxResponse = objectMapper.readValue(response.getBody(), Map.class);
            
            // 检查微信接口返回的错误
            if (wxResponse.containsKey("errcode") && !wxResponse.get("errcode").equals(0)) {
                throw new RuntimeException("微信登录失败: " + wxResponse.get("errmsg") + ", rid: " + wxResponse.getOrDefault("rid", ""));
            }
            
            // 提取openid和session_key
            String openid = (String) wxResponse.get("openid");
            String sessionKey = (String) wxResponse.get("session_key");
            
            System.out.println("获取到openid: " + (openid != null ? openid.substring(0, Math.min(openid.length(), 6)) + "..." : "null"));
            
            if (openid == null || openid.isEmpty()) {
                throw new RuntimeException("无法获取用户OpenID");
            }
            
            // 生成自定义登录凭证token
            String token = "tk_" + UUID.randomUUID().toString().replace("-", "");
            
            // 保存session信息
            Map<String, Object> sessionInfo = new HashMap<>();
            sessionInfo.put("openid", openid);
            sessionInfo.put("session_key", sessionKey);
            sessionInfo.put("created_at", new Date());
            sessions.put(token, sessionInfo);
            
            // 保存或更新用户信息
            if (userInfo != null) {
                users.put(openid, userInfo);
            } else if (!users.containsKey(openid)) {
                // 如果没有提供用户信息且系统中没有，创建一个基本用户信息
                Map<String, Object> basicUserInfo = new HashMap<>();
                basicUserInfo.put("openid", openid);
                users.put(openid, basicUserInfo);
            }
            
            System.out.println("登录成功，生成token: " + token);
            
            // 返回登录结果
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("token", token);
            result.put("userInfo", users.get(openid));
            
            return result;
        } catch (Exception e) {
            System.err.println("处理微信登录出错: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("处理微信登录出错: " + e.getMessage(), e);
        }
    }

    /**
     * 检查用户登录状态
     */
    @Override
    public Map<String, Object> checkLogin(String token) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            System.out.println("检查登录状态，token: " + token);
            
            // 检查token是否存在
            if (sessions.containsKey(token)) {
                Map<String, Object> sessionInfo = sessions.get(token);
                String openid = (String) sessionInfo.get("openid");
                
                System.out.println("token有效，对应openid: " + openid);
                
                result.put("success", true);
                result.put("isLogin", true);
                
                // 如果有用户信息，一并返回
                if (users.containsKey(openid)) {
                    result.put("userInfo", users.get(openid));
                }
            } else {
                System.out.println("token无效");
                
                result.put("success", false);
                result.put("isLogin", false);
                result.put("message", "无效的登录凭证");
            }
            
            return result;
        } catch (Exception e) {
            System.err.println("检查登录状态出错: " + e.getMessage());
            e.printStackTrace();
            
            result.put("success", false);
            result.put("isLogin", false);
            result.put("message", "检查登录状态出错: " + e.getMessage());
            return result;
        }
    }

    /**
     * 获取会话信息
     * @param token 登录token
     * @return 会话信息Map，或null如果token无效
     */
    public Map<String, Object> getSessionInfo(String token) {
        // 验证token是否存在
        if (token == null || !sessions.containsKey(token)) {
            return null;
        }
        
        // 返回会话信息
        return sessions.get(token);
    }
}
