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

3. 准备微信小程序信息
   - AppID: 在微信公众平台获取的小程序AppID
   - AppSecret: 在微信公众平台获取的小程序AppSecret

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
| 微信登录 | `/api/auth/wx-login` | POST | `code`: 微信登录凭证，`userInfo`: 用户信息（可选） |
| 检查登录状态 | `/api/auth/check-login` | GET | 无参数，需要在header中携带token |
| 获取书籍信息 | `/api/books/isbn` | GET | `isbn`: 书籍ISBN编码 |
| 发送聊天消息 | `/api/chat/messages` | POST | `bookId`: 书籍ID，`content`: 消息内容，`history`: 历史消息 |
| 用户登录 | `/api/auth/login` | POST | `username`: 用户名，`password`: 密码 |
| 用户注册 | `/api/auth/register` | POST | `username`: 用户名，`password`: 密码，`nickname`: 昵称 |

### 4. 配置微信登录

1. 在后端配置微信小程序信息:

   打开 `backend/src/main/resources/application.yml`，设置微信小程序的AppID和AppSecret：

   ```yaml
   wx:
     appid: 你的小程序AppID  # 填写真实的AppID
     secret: 你的小程序AppSecret  # 填写真实的AppSecret
   ```

2. 确保前端app.js中的微信登录逻辑正确:

   ```javascript
   // 在app.js中
   login: function(callback) {
     wx.login({
       success: (res) => {
         if (res.code) {
           // 将获取到的code发送到后端
           this.loginWithCode(res.code, null, callback);
         }
       }
     });
   }
   ```

3. 验证登录页面login.js中的登录触发逻辑:

   ```javascript
   // 在pages/login/login.js中
   login: function() {
     const app = getApp();
     app.login(success => {
       if (success) {
         wx.reLaunch({
           url: '/pages/index/index'
         });
       }
     });
   }
   ```

### 5. 处理认证

前端需要保存后端返回的JWT令牌，并在后续请求中携带：

```javascript
// 登录成功后保存令牌 (app.js的loginWithCode函数中)
wx.setStorageSync('token', response.data.token);
this.globalData.token = response.data.token;

// 后续请求自动附加令牌 (utils/request.js中)
var token = wx.getStorageSync('token');
if (token) {
  options.header.Authorization = 'Bearer ' + token;
}
```

### 6. 处理跨域问题

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
4. 测试微信登录功能
5. 尝试扫描书籍ISBN，验证是否能从后端获取书籍信息
6. 测试聊天功能，确认消息能正确发送到后端并获得回复

## 微信登录流程图

```
前端                                     后端                                 微信服务器
  |                                       |                                      |
  |--- wx.login() 获取code -------------->|                                      |
  |                                       |                                      |
  |                                       |--- 请求 jscode2session 接口 -------->|
  |                                       |                                      |
  |                                       |<-- 返回 openid 和 session_key ------|
  |                                       |                                      |
  |                                       |--- 生成自定义登录凭证token ----------|
  |                                       |                                      |
  |<-- 返回token和用户信息 ---------------|                                      |
  |                                       |                                      |
  |--- 发送请求 (带 Authorization) ------>|                                      |
  |                                       |--- 验证token ----------------------->|
  |                                       |                                      |
  |<-- 返回业务数据 ---------------------|                                      |
  |                                       |                                      |
```

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

2. **微信登录失败**:

   错误信息: `登录失败: 无法获取用户OpenID`

   解决方法:
   - 检查后端`application.yml`中的`wx.appid`和`wx.secret`配置是否正确
   - 确保使用的是真实有效的AppID和AppSecret，不是测试账号
   - 检查网络连接，确保后端可以访问微信API
   - 检查后端日志，查看详细错误信息

3. **Authorization头不正确**:

   错误信息: `无效的登录凭证`

   解决方法:
   - 确保token格式正确，应以`Bearer `开头，如`Bearer your_token_here`
   - 检查token是否已过期
   - 确认token是通过正确的微信登录流程获取的

有任何问题，在群里直接问，或者微信私聊！千万不要一声不吭
