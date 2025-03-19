// pages/scanner/scanner.js
// 导入请求API
const { getBookInfo } = require('../../utils/request.js');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    cameraAuth: false,         // 相机授权状态
    isLoading: false,          // 加载状态
    scanLineAnimation: null,   // 扫描线动画
    showManualInputModal: false, // 是否显示手动输入模态框
    manualIsbn: '',            // 手动输入的ISBN
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 检查相机权限
    this.checkCameraAuth();
    
    // 创建扫描线动画
    this.createScanAnimation();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    // 如果已授权，启动动画
    if (this.data.cameraAuth) {
      this.startScanAnimation();
    }
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    // 停止动画
    this.stopScanAnimation();
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    // 清除动画
    this.stopScanAnimation();
  },

  /**
   * 检查相机权限
   */
  checkCameraAuth: function() {
    const that = this;
    
    // 获取相机权限状态
    wx.getSetting({
      success: (res) => {
        if (res.authSetting['scope.camera']) {
          // 已授权
          that.setData({ cameraAuth: true });
          
          // 启动扫描动画
          that.startScanAnimation();
        } else {
          // 未授权或未确定
          wx.authorize({
            scope: 'scope.camera',
            success: () => {
              that.setData({ cameraAuth: true });
              
              // 启动扫描动画
              that.startScanAnimation();
            },
            fail: () => {
              that.setData({ cameraAuth: false });
            }
          });
        }
      }
    });
  },

  /**
   * 请求相机权限
   */
  requestCameraAuth: function() {
    const that = this;
    
    wx.openSetting({
      success: (res) => {
        if (res.authSetting['scope.camera']) {
          that.setData({ cameraAuth: true });
          
          // 启动扫描动画
          that.startScanAnimation();
        }
      }
    });
  },

  /**
   * 相机错误处理
   */
  onCameraError: function(e) {
    console.error('相机错误：', e.detail);
    wx.showToast({
      title: '相机启动失败，请检查权限',
      icon: 'none'
    });
    this.setData({ cameraAuth: false });
  },

  /**
   * 创建扫描线动画
   */
  createScanAnimation: function() {
    const animation = wx.createAnimation({
      duration: 1500,
      timingFunction: 'linear',
      delay: 0
    });
    
    this.animation = animation;
  },

  /**
   * 开始扫描动画
   */
  startScanAnimation: function() {
    // 清除可能存在的定时器
    if (this.scanTimer) {
      clearInterval(this.scanTimer);
    }
    
    const that = this;
    let moveUp = true;
    
    // 创建循环动画
    this.scanTimer = setInterval(() => {
      if (moveUp) {
        that.animation.translateY(200).step();
      } else {
        that.animation.translateY(0).step();
      }
      
      that.setData({
        scanLineAnimation: that.animation.export()
      });
      
      moveUp = !moveUp;
    }, 1500);
  },

  /**
   * 停止扫描动画
   */
  stopScanAnimation: function() {
    if (this.scanTimer) {
      clearInterval(this.scanTimer);
      this.scanTimer = null;
    }
  },

  /**
   * 扫码按钮点击
   */
  onScanTap: function() {
    if (!this.data.cameraAuth) {
      this.requestCameraAuth();
      return;
    }
    
    const that = this;
    
    // 设置加载状态
    this.setData({ isLoading: true });
    
    // 调用微信扫码API
    wx.scanCode({
      scanType: ['barCode'],
      success: (res) => {
        console.log('扫码结果：', res);
        
        // 处理扫码结果
        that.processBarcode(res.result);
      },
      fail: (err) => {
        console.error('扫码失败：', err);
        
        // 取消扫码或失败
        that.setData({ isLoading: false });
        
        if (err.errMsg !== "scanCode:fail cancel") {
          wx.showToast({
            title: '扫码失败，请重试',
            icon: 'none'
          });
        }
      }
    });
  },

  /**
   * 处理条形码结果
   */
  processBarcode: function(barcode) {
    const that = this;
    
    // 调用API获取书籍信息
    getBookInfo(barcode).then(res => {
      console.log('书籍信息：', res);
      
      // 结束加载状态
      that.setData({ isLoading: false });
      
      // 保存当前书籍信息到全局
      getApp().globalData.currentBook = res;
      
      // 跳转到聊天页面
      wx.navigateTo({
        url: `/pages/chat/chat?bookId=${res.id}&title=${encodeURIComponent(res.title)}`
      });
      
    }).catch(err => {
      console.error('获取书籍信息失败：', err);
      
      // 结束加载状态
      that.setData({ isLoading: false });
      
      // 显示错误提示
      wx.showModal({
        title: '未找到书籍',
        content: '无法识别该书籍信息，请确保条形码清晰或尝试手动输入ISBN',
        showCancel: false
      });
    });
  },

  /**
   * 取消按钮点击
   */
  onCancelTap: function() {
    wx.navigateBack();
  },

  /**
   * 显示手动输入模态框
   */
  showManualInput: function() {
    this.setData({
      showManualInputModal: true,
      manualIsbn: ''
    });
  },

  /**
   * 隐藏手动输入模态框
   */
  hideManualInput: function() {
    this.setData({
      showManualInputModal: false
    });
  },

  /**
   * 手动输入变化
   */
  onManualInput: function(e) {
    this.setData({
      manualIsbn: e.detail.value
    });
  },

  /**
   * 提交手动输入
   */
  submitManualInput: function() {
    const isbn = this.data.manualIsbn.trim();
    
    if (!isbn) {
      wx.showToast({
        title: '请输入有效的ISBN',
        icon: 'none'
      });
      return;
    }
    
    // 隐藏模态框
    this.setData({
      showManualInputModal: false,
      isLoading: true
    });
    
    // 处理ISBN
    this.processBarcode(isbn);
  }
});
