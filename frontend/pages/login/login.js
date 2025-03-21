// login.js
Page({
  data: {
    userInfo: {},
    hasUserInfo: false,
    isLoading: false
  },

  onLoad: function() {
    // 检查用户是否已登录
    // 已经登录则跳转到首页
    const app = getApp();
    
    // 添加日志输出
    console.log("登录页面加载，检查用户登录状态");
    
    app.checkLogin(res => {
      console.log("检查登录结果:", res);
      if (res.isLogin) {
        console.log("用户已登录，跳转到首页");
        wx.reLaunch({
          url: '/pages/index/index'
        });
      } else {
        console.log("用户未登录");
      }
    });
  },

  // 直接执行登录
  login: function() {
    console.log("用户点击登录按钮");
    
    // 显示加载中
    this.setData({
      isLoading: true
    });
    
    const app = getApp();
    
    // 检查网络状态
    wx.getNetworkType({
      success: (res) => {
        console.log("当前网络状态:", res.networkType);
        
        if (res.networkType === 'none') {
          wx.showToast({
            title: '网络连接失败，请检查网络设置',
            icon: 'none'
          });
          
          this.setData({
            isLoading: false
          });
          return;
        }
        
        // 执行微信登录
        app.login(success => {
          console.log("登录回调结果:", success);
          
          if (success) {
            // 登录成功后跳转到首页
            console.log("登录成功，跳转到首页");
            wx.reLaunch({
              url: '/pages/index/index'
            });
          } else {
            // 登录失败
            console.log("登录失败");
            
            this.setData({
              isLoading: false
            });
          }
        });
      },
      fail: (err) => {
        console.error("获取网络状态失败:", err);
        this.setData({
          isLoading: false
        });
      }
    });
  }
});
