// components/chat-bubble/chat-bubble.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    // 消息内容
    content: {
      type: String,
      value: ''
    },
    // 消息类型：'user' 或 'ai'
    type: {
      type: String,
      value: 'user' // 默认为用户消息
    },
    // 头像URL
    avatar: {
      type: String,
      value: ''
    },
    // 发送时间
    time: {
      type: String,
      value: ''
    },
    // 是否显示头像
    showAvatar: {
      type: Boolean,
      value: true
    },
    // 是否为最后一条消息
    isLast: {
      type: Boolean,
      value: false
    }
  },

  /**
   * 组件的初始数据
   */
  data: {
    // 默认用户头像
    defaultUserAvatar: '/images/user_avatar.png',
    // 默认AI头像
    defaultAiAvatar: '/images/ai_avatar.png'
  },

  /**
   * 组件的方法列表
   */
  methods: {
    // 检查并预览图片
    checkAndPreviewImage: function() {
      var content = this.data.content;
      // 检查是否是图片链接
      if (content.includes('http') && this.isImageUrl(content)) {
        this.previewImage();
      }
    },
    
    // 判断URL是否为图片
    isImageUrl: function(url) {
      // 检查URL是否以常见图片扩展名结尾
      var imgExtensions = ['.jpeg', '.jpg', '.gif', '.png'];
      var lowerUrl = url.toLowerCase();
      
      for (var i = 0; i < imgExtensions.length; i++) {
        var ext = imgExtensions[i];
        if (lowerUrl.endsWith(ext)) {
          return true;
        }
      }
      
      return false;
    },
    
    // 显示大图
    previewImage: function() {
      wx.previewImage({
        urls: [this.data.content],
        current: this.data.content
      });
    },
    
    // 复制文本内容
    copyText: function() {
      var that = this;
      wx.setClipboardData({
        data: that.data.content,
        success: function() {
          wx.showToast({
            title: '已复制到剪贴板',
            icon: 'success'
          });
        }
      });
    },
    
    // 长按菜单
    onLongPress: function() {
      var that = this;
      wx.showActionSheet({
        itemList: ['复制文本', '分享'],
        success: function(res) {
          if (res.tapIndex === 0) {
            that.copyText();
          } else if (res.tapIndex === 1) {
            that.triggerEvent('share');
          }
        }
      });
    }
  }
});
