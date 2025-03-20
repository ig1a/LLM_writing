# 书籍RAG聊天小程序 - 后端服务

这是书籍RAG聊天小程序的后端服务，基于Spring Boot框架开发，提供与微信小程序前端进行交互的API接口。

## 技术栈

- **Spring Boot 3.x**: 提供REST API接口
- **Spring Security**: 用户认证和授权
- **MySQL**: 数据持久化
- **MyBatis Plus**: ORM框架
- **JWT**: 基于Token的认证
- **OpenAPI兼容接口**: 支持LLM API调用

## 功能模块

- **用户管理**: 用户注册、登录、认证
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
```

## API接口说明

### 用户认证接口
- POST `/api/auth/register` - 用户注册
- POST `/api/auth/login` - 用户登录
- GET `/api/auth/userinfo` - 获取用户信息

### 书籍信息接口
- GET `/api/book/info?barcode={isbn}` - 获取书籍信息

### 聊天消息接口
- POST `/api/chat/message` - 发送聊天消息
- GET `/api/chat/history?bookId={id}&page={page}` - 获取聊天历史

## 数据库设计

### 用户表 (user)
- id: 主键
- username: 用户名
- password: 密码(加密存储)
- nickname: 昵称
- avatar: 头像URL
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
3. 运行数据库脚本(script/db/)
4. 使用Maven编译项目: `mvn clean package`
5. 运行应用: `java -jar target/book-chat-backend.jar`

## 部署指南

### 服务器要求
- JRE 17+
- MySQL 8.0+
- 2GB+ RAM

### 部署步骤
1. 编译打包: `mvn clean package -P prod`
2. 将JAR文件及配置上传到服务器
3. 修改配置文件中的数据库连接和API密钥
4. 运行应用: `java -jar book-chat-backend.jar --spring.profiles.active=prod`
