# 书籍RAG聊天小程序 - 后端服务

这是书籍RAG聊天小程序的后端服务，基于Spring Boot框架开发，提供与微信小程序前端进行交互的API接口。

## 技术栈

- **Spring Boot 3.x**: 提供REST API接口
- **Spring Security**: 用户认证和授权
- **MySQL**: 数据持久化
- **MyBatis Plus**: ORM框架
- **JWT**: 基于Token的认证
- **微信小程序登录**: 用户通过微信授权登录
- **OpenAPI兼容接口**: 支持LLM API调用

## 功能模块

- **用户管理**: 用户注册、登录、认证
- **微信登录**: 通过微信小程序授权快速登录
- **书籍管理**: 书籍信息检索与存储
- **聊天管理**: 对话历史记录存储与检索
- **LLM集成**: 兼容OpenAI API进行聊天生成

## 项目结构

```
backend/
├── README.md                    # 项目文档
├── pom.xml                      # Maven配置文件
├── src/                         # 源代码目录
│   ├── main/
│   │   ├── java/com/book/chat/  # Java代码
│   │   │   ├── config/          # 配置类
│   │   │   ├── controller/      # 控制器
│   │   │   ├── dto/             # 数据传输对象
│   │   │   ├── entity/          # 实体类
│   │   │   ├── mapper/          # MyBatis接口
│   │   │   ├── service/         # 服务接口及实现
│   │   │   ├── util/            # 工具类
│   │   │   └── BookChatApplication.java  # 应用入口
│   │   └── resources/           # 资源文件
│   │       ├── application.yml  # 应用配置
│   │       ├── application-dev.yml  # 开发环境配置
│   │       ├── application-prod.yml # 生产环境配置
│   │       └── mapper/          # MyBatis XML映射
│   └── test/                    # 测试代码
├── script/                      # 脚本文件
│   └── db/                      # 数据库脚本
└── doc/                         # 文档目录
    └── QA.md                    # 常见问题解答
```

## API接口说明

### 用户认证接口
- POST `/api/auth/register` - 用户注册
- POST `/api/auth/login` - 用户登录
- GET `/api/auth/userinfo` - 获取用户信息
- POST `/api/auth/wx-login` - 微信小程序登录
- GET `/api/auth/check-login` - 检查登录状态

### 书籍信息接口
- GET `/api/book/info?barcode={isbn}` - 获取书籍信息

### 聊天消息接口
- POST `/api/chat/message` - 发送聊天消息
- GET `/api/chat/history?bookId={id}&page={page}` - 获取聊天历史

## 微信登录实现

### 前端流程
1. 小程序调用 `wx.login()` 获取临时登录凭证 code
2. 将 code 发送到后端的 `/api/auth/wx-login` 接口
3. 接收并存储后端返回的 token
4. 后续请求在 header 中添加 Authorization: Bearer {token}

### 后端流程
1. 接收前端发送的 code
2. 使用 code 、appid 和 secret 请求微信接口获取 openid
3. 根据 openid 创建或更新用户信息
4. 生成 JWT token 返回给前端
5. 维护用户登录状态

### 配置说明
微信小程序配置位于 `application.yml` 文件中：
```yaml
wx:
  appid: your_appid  # 微信小程序appid
  secret: your_secret  # 微信小程序secret
```

## 数据库设计

### 用户表 (user)
- id: 主键
- username: 用户名
- password: 密码(加密存储)
- nickname: 昵称
- avatar: 头像URL
- openid: 微信用户唯一标识
- create_time: 创建时间
- update_time: 更新时间

### 书籍表 (book)
- id: 主键
- title: 书名
- author: 作者
- publisher: 出版社
- isbn: ISBN编码
- cover: 封面图片URL
- description: 描述
- create_time: 创建时间
- update_time: 更新时间

### 聊天记录表 (chat_message)
- id: 主键
- user_id: 用户ID
- book_id: 书籍ID
- content: 消息内容
- type: 消息类型(user/ai)
- create_time: 创建时间

## 开发环境设置

### 前置条件
- JDK 17+
- Maven 3.8+
- MySQL 8.0+

### 本地开发步骤
1. 克隆仓库
2. 配置数据库连接(application-dev.yml)
3. 配置微信小程序appid和secret(application.yml)
4. 运行数据库脚本(script/db/)
5. 使用Maven编译项目: `mvn clean package`
6. 运行应用: `java -jar target/book-chat-backend.jar`

## 部署指南

### 服务器要求
- JRE 17+
- MySQL 8.0+
- 2GB+ RAM

### 部署步骤
1. 编译打包: `mvn clean package -P prod`
2. 将JAR文件及配置上传到服务器
3. 修改配置文件中的数据库连接和API密钥
4. 修改微信小程序的正式环境appid和secret
5. 运行应用: `java -jar book-chat-backend.jar --spring.profiles.active=prod`
