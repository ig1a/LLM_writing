// pages/index/index.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    isFirstTimeUser: true, // 是否首次使用
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 检查是否首次使用
    const hasUsed = wx.getStorageSync('hasUsed');
    if (hasUsed) {
      this.setData({
        isFirstTimeUser: false
      });
    }
  },

  /**
   * 扫描按钮点击事件
   */
  onScanTap: function() {
    // 记录已使用标记
    wx.setStorageSync('hasUsed', true);
    
    // 跳转到扫码页面
    wx.navigateTo({
      url: '/pages/scanner/scanner'
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    // 展示欢迎动画
    if (this.data.isFirstTimeUser) {
      // 可以添加一些首次使用的动画效果
    }
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    // 如果有未完成的聊天会话，可以提示用户继续
    const currentBook = getApp().globalData.currentBook;
    if (currentBook) {
      wx.showModal({
        title: '继续聊天',
        content: `您有一个关于《${currentBook.title}》的聊天未完成，是否继续？`,
        confirmText: '继续聊天',
        cancelText: '重新开始',
        success: (res) => {
          if (res.confirm) {
            // 继续之前的聊天
            wx.navigateTo({
              url: `/pages/chat/chat?bookId=${currentBook.id}&title=${currentBook.title}`
            });
          } else {
            // 清除当前书籍信息
            getApp().globalData.currentBook = null;
          }
        }
      });
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    return {
      title: '书籍RAG聊天 - 与书籍AI对话的新方式',
      path: '/pages/index/index',
      imageUrl: '/images/share-image.png' // 分享显示的图片
    };
  }
});
