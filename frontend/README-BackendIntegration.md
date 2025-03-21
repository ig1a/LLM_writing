# 前端连接后端集成指南

本文档介绍如何将微信小程序前端与Spring Boot后端进行集成。

## 准备工作

1. 确保后端服务已成功启动
   ```bash
   cd D:\LLM_novel\LLM_writing\backend
   java -jar target\book-chat-backend-0.0.1-SNAPSHOT.jar
   ```

2. 记录后端服务的URL
   - 本地开发环境：`http://localhost:8080`
   - 生产环境：根据您的实际部署情况设置

## 前端集成步骤

### 1. 修改请求基础URL

在 `utils/request.js` 文件中，更新 `BASE_URL` 为后端服务的URL：

```javascript
// 服务器基础URL
var BASE_URL = 'http://localhost:8080';  // 本地开发环境
// var BASE_URL = 'https://your-production-api.com';  // 生产环境
```

### 2. 关闭测试模式

当后端服务准备就绪后，将测试模式关闭：

```javascript
// 测试模式开关 - 设置为true使用本地测试数据，设置为false使用真实API
var TEST_MODE = false;  // 改为false使用真实后端API
```

### 3. 确认API路径一致

确保前端请求的API路径与后端控制器提供的路径一致：

| 功能 | 前端请求路径 | HTTP方法 | 参数 |
|------|------------|---------|------|
| 获取书籍信息 | `/api/books/isbn` | GET | `isbn`: 书籍ISBN编码 |
| 发送聊天消息 | `/api/chat/messages` | POST | `bookId`: 书籍ID，`content`: 消息内容，`history`: 历史消息 |
| 用户登录 | `/api/auth/login` | POST | `username`: 用户名，`password`: 密码 |
| 用户注册 | `/api/auth/register` | POST | `username`: 用户名，`password`: 密码，`nickname`: 昵称 |

### 4. 处理认证

前端需要保存后端返回的JWT令牌，并在后续请求中携带：

```javascript
// 登录成功后保存令牌
wx.setStorageSync('token', response.token);

// 后续请求自动附加令牌
var token = wx.getStorageSync('token');
if (token) {
  options.header.Authorization = 'Bearer ' + token;
}
```

### 5. 处理跨域问题

如果遇到跨域问题，请确保：

1. 后端已正确配置CORS：
   ```java
   @Configuration
   public class CorsConfig {
       @Bean
       public WebMvcConfigurer corsConfigurer() {
           return new WebMvcConfigurer() {
               @Override
               public void addCorsMappings(CorsRegistry registry) {
                   registry.addMapping("/**")
                           .allowedOrigins("*")
                           .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                           .allowedHeaders("*")
                           .maxAge(3600);
               }
           };
       }
   }
   ```

2. 微信小程序项目需要在 `app.json` 中配置：
   ```json
   {
     "networkTimeout": {
       "request": 10000,
       "connectSocket": 10000,
       "uploadFile": 10000,
       "downloadFile": 10000
     }
   }
   ```

## 测试集成

1. 启动后端服务
2. 修改前端配置
3. 编译并运行微信小程序
4. 尝试扫描书籍ISBN，验证是否能从后端获取书籍信息
5. 测试聊天功能，确认消息能正确发送到后端并获得回复

## 常见问题

1. **微信小程序合法域名校验错误**:
   
   错误信息: `xxx 不在以下 request 合法域名列表中`
   
   解决方法:
   - **方法一**: 在微信开发者工具中关闭域名校验
     - 点击开发者工具右上角的"详情"
     - 勾选"不校验合法域名、web-view（业务域名）、TLS 版本以及 HTTPS 证书"
     - 重启工具或重新编译项目
   
   - **方法二**: 使用内网穿透工具
     - 使用ngrok或natapp等工具将本地服务器映射到公网
     - 获取临时域名（如`https://xxxx.ngrok.io`）
     - 在微信公众平台添加此域名为合法域名
     - 修改前端BASE_URL为此域名
   
   - **方法三**: 保持测试模式开启
     - 在`request.js`中设置`TEST_MODE = true`
     - 这样前端将使用模拟数据而不是真实API

有任何问题，在群里直接问，或者微信私聊！千万不要一声不吭
