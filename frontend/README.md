# 书籍RAG聊天小程序

这是一个基于微信小程序的应用，允许用户通过扫描书籍的条形码，与由书中内容驱动的大语言模型(LLM)进行对话交流。

## 功能特点

- **扫码识别**：支持扫描书籍条形码，自动识别书籍信息
- **智能对话**：基于书籍内容的RAG技术，提供智能问答服务
- **Material Design**：采用现代化的Material Design设计风格
- **Vue3技术栈**：使用Vue3框架开发，提供流畅的用户体验

## 项目结构

```
frontend/
├── README.md                 # 项目说明文档
├── project.config.json       # 项目配置文件
├── app.js                    # 小程序入口文件
├── app.json                  # 小程序全局配置
├── app.wxss                  # 小程序全局样式
├── utils/                    # 工具函数目录
│   └── request.js            # 网络请求封装
├── components/               # 组件目录
│   ├── chat-bubble/          # 聊天气泡组件
│   └── loading/              # 加载组件
└── pages/                    # 页面目录
    ├── index/                # 首页(初始界面)
    │   ├── index.js          # 首页逻辑
    │   ├── index.wxml        # 首页结构
    │   ├── index.wxss        # 首页样式
    │   └── index.json        # 首页配置
    ├── scanner/              # 扫码页面
    │   ├── scanner.js
    │   ├── scanner.wxml
    │   ├── scanner.wxss
    │   └── scanner.json
    └── chat/                 # 聊天页面
        ├── chat.js
        ├── chat.wxml
        ├── chat.wxss
        └── chat.json
```

## 如何运行

1. 克隆本仓库到本地
2. 使用微信开发者工具打开本项目
3. 在模拟器中预览或真机调试

## 技术栈

- 微信小程序
- Vue3
- Material Design

## 开发与构建

本项目使用微信小程序原生开发，结合Vue3的开发理念。您需要安装微信开发者工具来进行开发和调试。

## 后续开发计划

- 完善RAG技术与LLM集成
- 添加用户历史记录功能
- 优化UI/UX设计
- 添加更多书籍资源支持
