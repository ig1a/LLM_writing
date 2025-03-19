// pages/chat/chat.js
// 导入请求API
const { sendChatMessage } = require('../../utils/request.js');

// 生成唯一ID的函数
const generateId = () => {
  return 'msg_' + Date.now() + '_' + Math.floor(Math.random() * 1000);
};

// 格式化时间
const formatTime = (date) => {
  const hour = date.getHours();
  const minute = date.getMinutes();
  return [hour, minute].map(n => n < 10 ? '0' + n : n).join(':');
};

Page({
  /**
   * 页面的初始数据
   */
  data: {
    bookId: '',            // 书籍ID
    bookInfo: {},          // 书籍信息
    messageList: [],       // 消息列表
    inputMessage: '',      // 输入框内容
    isThinking: false,     // AI是否正在思考
    scrollToMessage: '',   // 要滚动到的消息ID
    keyboardHeight: 0,     // 键盘高度
    showError: false,      // 是否显示错误提示
    errorMessage: '',      // 错误信息
    loadingHistory: false, // 是否正在加载历史消息
    hasMoreHistory: true,  // 是否还有更多历史消息
    currentPage: 1,        // 当前历史消息页数
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 获取页面参数
    const { bookId, title } = options;
    
    if (!bookId) {
      wx.showToast({
        title: '缺少书籍信息',
        icon: 'none'
      });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
      return;
    }
    
    // 设置书籍基本信息
    this.setData({
      bookId,
      bookInfo: {
        title: title || '未知书籍',
        isbn: bookId
      }
    });
    
    // 从全局获取完整书籍信息
    const app = getApp();
    if (app.globalData.currentBook) {
      this.setData({
        bookInfo: app.globalData.currentBook
      });
    }
    
    // 加载本地存储的聊天记录
    this.loadLocalChatHistory();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    // 滚动到最新消息
    this.scrollToBottom();
  },

  /**
   * 监听键盘高度变化
   */
  onInputFocus: function(e) {
    // 键盘弹出时滚动到底部
    this.scrollToBottom();
    
    // 记录键盘高度
    this.setData({
      keyboardHeight: e.detail.height || 0
    });
  },
  
  /**
   * 输入框失焦事件
   */
  onInputBlur: function() {
    this.setData({
      keyboardHeight: 0
    });
  },

  /**
   * 输入框内容变化
   */
  onInputChange: function(e) {
    this.setData({
      inputMessage: e.detail.value
    });
  },

  /**
   * 发送消息
   */
  sendMessage: function() {
    // 检查是否有输入内容
    const message = this.data.inputMessage.trim();
    if (!message || this.data.isThinking) {
      return;
    }
    
    // 生成用户消息对象
    const userMessage = {
      id: generateId(),
      type: 'user',
      content: message,
      time: formatTime(new Date())
    };
    
    // 添加到消息列表
    this.setData({
      messageList: [...this.data.messageList, userMessage],
      inputMessage: '',
      isThinking: true
    });
    
    // 滚动到最新消息
    this.scrollToBottom();
    
    // 调用API发送消息
    const { bookId } = this.data;
    const chatHistory = this.formatChatHistoryForAPI();
    
    sendChatMessage(bookId, message, chatHistory).then(res => {
      console.log('AI回复：', res);
      
      // 生成AI回复消息对象
      const aiMessage = {
        id: generateId(),
        type: 'ai',
        content: res.data.response,
        time: formatTime(new Date())
      };
      
      // 添加到消息列表
      this.setData({
        messageList: [...this.data.messageList, aiMessage],
        isThinking: false
      });
      
      // 保存聊天记录到本地存储
      this.saveChatHistory();
      
      // 滚动到最新消息
      this.scrollToBottom();
      
    }).catch(err => {
      console.error('发送消息失败：', err);
      
      // 结束加载状态
      this.setData({
        isThinking: false,
        showError: true,
        errorMessage: '网络异常，请重试'
      });
      
      // 自动隐藏错误提示
      setTimeout(() => {
        this.setData({
          showError: false
        });
      }, 3000);
    });
  },

  /**
   * 格式化聊天记录为API所需格式
   */
  formatChatHistoryForAPI: function() {
    return this.data.messageList.map(msg => ({
      role: msg.type === 'user' ? 'user' : 'assistant',
      content: msg.content
    }));
  },

  /**
   * 保存聊天记录到本地存储
   */
  saveChatHistory: function() {
    const { bookId, messageList } = this.data;
    
    try {
      // 存储当前书籍的聊天记录
      wx.setStorageSync(`chat_history_${bookId}`, JSON.stringify(messageList));
      
      // 更新聊天记录索引
      let chatIndex = wx.getStorageSync('chat_history_index') || [];
      if (!chatIndex.includes(bookId)) {
        chatIndex.push(bookId);
        wx.setStorageSync('chat_history_index', chatIndex);
      }
      
    } catch (e) {
      console.error('保存聊天记录失败：', e);
    }
  },

  /**
   * 加载本地存储的聊天记录
   */
  loadLocalChatHistory: function() {
    const { bookId } = this.data;
    
    try {
      const chatHistory = wx.getStorageSync(`chat_history_${bookId}`);
      if (chatHistory) {
        this.setData({
          messageList: JSON.parse(chatHistory)
        });
      }
    } catch (e) {
      console.error('加载聊天记录失败：', e);
    }
  },

  /**
   * 滚动到底部
   */
  scrollToBottom: function() {
    const { messageList } = this.data;
    if (messageList.length > 0) {
      const lastMessage = messageList[messageList.length - 1];
      this.setData({
        scrollToMessage: `msg-${lastMessage.id}`
      });
    }
  },

  /**
   * 分享消息
   */
  shareMessage: function(e) {
    const messageId = e.currentTarget.dataset.messageId;
    const message = this.data.messageList.find(msg => msg.id === messageId);
    
    if (message) {
      wx.setClipboardData({
        data: message.content,
        success: function() {
          wx.showToast({
            title: '已复制到剪贴板',
            icon: 'success'
          });
        }
      });
    }
  },

  /**
   * 滚动到顶部加载更多历史消息
   */
  onScrollToUpper: function() {
    // 目前使用本地存储，这个功能仅为示例
    // 实际项目中可以调用分页API获取更多历史消息
    
    if (!this.data.hasMoreHistory || this.data.loadingHistory) {
      return;
    }
    
    this.setData({
      loadingHistory: true
    });
    
    // 模拟加载更多历史消息
    setTimeout(() => {
      this.setData({
        loadingHistory: false,
        hasMoreHistory: false
      });
      
      wx.showToast({
        title: '没有更多历史消息',
        icon: 'none'
      });
    }, 1000);
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    const { bookInfo } = this.data;
    
    return {
      title: `与《${bookInfo.title || '书籍'}》进行AI对话`,
      path: `/pages/index/index`,
      imageUrl: bookInfo.coverUrl || '/images/share-image.png'
    };
  }
});
