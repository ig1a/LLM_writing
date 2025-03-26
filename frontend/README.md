# 书籍RAG聊天小程序

这是一个基于微信小程序的应用，允许用户通过扫描书籍的条形码，与由书中内容驱动的大语言模型(LLM)进行对话交流。

## 功能特点

- **扫码识别**：支持扫描书籍条形码，自动识别书籍信息
- **智能对话**：基于书籍内容的RAG技术，提供智能问答服务
- **微信登录**：通过微信授权快速登录，无需额外注册
- **Material Design**：采用现代化的Material Design设计风格
- **跨平台兼容**：适配不同尺寸设备，提供流畅的用户体验

## 项目结构

```
frontend/
├── README.md                     # 项目说明文档
├── README-BackendIntegration.md  # 前后端对接指南
├── project.config.json           # 项目配置文件
├── app.js                        # 小程序入口文件(含登录逻辑)
├── app.json                      # 小程序全局配置
├── app.wxss                      # 小程序全局样式
├── doc/                          # 文档目录
│   ├── 微信登录实现.md            # 微信登录实现文档
│   └── 微信扫一扫.md              # 扫码功能实现文档
├── images/                       # 图片资源
│   ├── ai_avatar.png             # AI头像
│   ├── user_avatar.png           # 用户头像
│   ├── default_book.png          # 默认书籍封面
│   ├── logo.png                  # 应用logo
│   └── share-image.png           # 分享图片
├── utils/                        # 工具函数目录
│   └── request.js                # 网络请求封装
├── components/                   # 组件目录
│   ├── chat-bubble/              # 聊天气泡组件
│   │   ├── chat-bubble.js
│   │   ├── chat-bubble.wxml
│   │   ├── chat-bubble.wxss
│   │   └── chat-bubble.json
│   └── loading/                  # 加载组件
│       ├── loading.js
│       ├── loading.wxml
│       ├── loading.wxss
│       └── loading.json
└── pages/                        # 页面目录
    ├── login/                    # 登录页面
    │   ├── login.js              # 登录逻辑
    │   ├── login.wxml            # 登录页面结构
    │   ├── login.wxss            # 登录页面样式
    │   └── login.json            # 登录页面配置
    ├── index/                    # 首页(初始界面)
    │   ├── index.js              # 首页逻辑
    │   ├── index.wxml            # 首页结构
    │   ├── index.wxss            # 首页样式
    │   └── index.json            # 首页配置
    ├── scanner/                  # 扫码页面
    │   ├── scanner.js
    │   ├── scanner.wxml
    │   ├── scanner.wxss
    │   └── scanner.json
    ├── user/                     # 用户页面
    │   ├── user.js
    │   ├── user.wxml
    │   ├── user.wxss
    │   └── user.json
    └── chat/                     # 聊天页面
        ├── chat.js
        ├── chat.wxml
        ├── chat.wxss
        └── chat.json
```

## 测试指南

### 测试用ISBN码

以下是一些常见书籍的ISBN码，可用于测试扫码和聊天功能：

| 书名                       | ISBN              | 备注                     |
|----------------------------|-------------------|--------------------------|
| 三体                       | 9787536692930     | 刘慈欣科幻小说           |
| 活着                       | 9787506365437     | 余华著名小说             |
| 人类简史                   | 9787508647357     | 尤瓦尔·赫拉利历史著作    |
| 百年孤独                   | 9787544253994     | 加西亚·马尔克斯经典      |
| 解忧杂货店                 | 9787544270878     | 东野圭吾推理小说         |
| 小王子                     | 9787540478698     | 安东尼·德·圣-埃克苏佩里  |
| 围城                       | 9787020090006     | 钱钟书经典小说           |
| 追风筝的人                 | 9787208061644     | 卡勒德·胡赛尼小说        |

### 功能测试途径

#### 登录测试
- 打开小程序，自动跳转到登录页面
- 点击"微信登录"按钮，授权登录
- 验证登录成功后是否能正确跳转到首页

#### 首页测试
- 打开小程序，检查首页UI显示是否正常
- 点击"开始扫码"按钮，应跳转到扫码页面
- 点击个人中心图标，验证是否能显示用户信息

#### 扫码页面测试
- 手动输入ISBN：在输入框中输入上述任一ISBN码，点击"确认"按钮
- 扫码功能：使用相机扫描实体书籍条形码
- 取消功能：点击"取消"按钮，应返回首页

#### 聊天页面测试
- 书籍信息卡片：验证书籍标题、作者、出版社等信息显示
- 消息发送：在输入框中输入问题并发送
- AI回复：验证AI是否根据书籍内容提供相关回复
- 长按消息：测试复制文本功能
- 返回功能：点击左上角返回按钮，应返回首页

### 开发者测试数据
如需在无API环境下测试，可在utils/request.js中添加mock数据进行功能验证。

前后端集成指南请参考README-BackendIntegration.md，其中包含详细的集成步骤和配置说明。

## 开发环境设置

### 前置条件
- 微信开发者工具：[下载地址](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html)
- 微信小程序账号（可使用测试号进行开发）
- 后端服务（参考backend目录下的README.md）

### 本地开发步骤

1. **安装微信开发者工具**
   - 下载并安装最新版本的微信开发者工具
   - 使用微信扫码登录

2. **导入小程序项目**
   - 打开微信开发者工具
   - 点击左上角的"+"号，选择"导入项目"
   - 项目目录选择`frontend`文件夹
   - AppID填写您的小程序AppID（如果没有，可以选择"测试号"）

3. **配置后端连接**
   - 打开`utils/request.js`
   - 设置`BASE_URL`为后端服务地址
   - 调整`TEST_MODE`的值（开发阶段可设为true，生产环境设为false）

4. **配置微信登录**
   - 确保`app.js`中的登录逻辑已正确实现
   - 在后端的`application.yml`中配置正确的微信小程序appid和secret

5. **运行项目**
   - 点击开发者工具中的"编译"按钮
   - 项目将启动并显示在模拟器中

6. **调试技巧**
   - 使用控制台调试：开发者工具右侧"调试器"面板的"Console"标签页可查看日志
   - 使用断点调试：在代码编辑器中点击行号，设置断点进行调试
   - 使用网络监控：在"Network"标签页监控API请求

## 打包部署

### 上传代码
1. 点击开发者工具右上角的"上传"按钮
2. 填写版本号和项目备注
3. 确认上传

### 提交审核
1. 登录[微信公众平台](https://mp.weixin.qq.com/)
2. 进入"版本管理"页面
3. 选择已上传的版本，点击"提交审核"
4. 填写相关信息，完成审核提交

## 贡献指南

欢迎对本项目做出贡献！请遵循以下步骤：

1. Fork本仓库
2. 创建新分支 (`git checkout -b feature/your-feature`)
3. 提交更改 (`git commit -m 'Add some feature'`)
4. 推送到分支 (`git push origin feature/your-feature`)
5. 创建Pull Request

## 版权和许可
本项目采用MIT许可证。详情请参见[LICENSE](LICENSE)文件。

## 联系方式
如有任何问题或建议，请联系项目维护者：
- 邮箱：contact@example.com
- 微信：wxid_example