// app.js
App({
  // 全局数据
  globalData: {
    userInfo: null,
    currentBook: null,
    token: null, // 添加token字段用于存储用户登录凭证
    theme: {
      primary: '#6200EE',    // 主色调
      secondary: '#03DAC6',  // 次要色调
      background: '#FFFFFF', // 背景色
      surface: '#FFFFFF',    // 表面色
      error: '#B00020',      // 错误色
      textPrimary: '#000000DE', // 主要文本颜色
      textSecondary: '#0000008A' // 次要文本颜色
    },
    // API基础URL
    apiBaseUrl: 'http://localhost:8080', // 确保apiBaseUrl设置正确
  },
  
  // 小程序启动时执行
  onLaunch: function() {
    // 初始化云开发环境（如果需要的话）
    // wx.cloud.init({...})
    
    // 检查更新版本
    this.checkUpdate();
    
    // 获取系统信息
    const systemInfo = wx.getSystemInfoSync();
    this.globalData.systemInfo = systemInfo;
    
    // 获取主题色模式（暗黑模式或亮色模式）
    wx.onThemeChange((result) => {
      this.updateTheme(result.theme);
    });
    
    // 默认获取一次当前主题
    wx.getSystemInfo({
      success: (res) => {
        this.updateTheme(res.theme);
      }
    });
    
    // 检查用户是否已登录
    this.checkLogin();
  },
  
  // 更新主题色设置
  updateTheme(theme) {
    if (theme === 'dark') {
      this.globalData.theme = {
        primary: '#BB86FC',    // 暗色模式主色调
        secondary: '#03DAC6',  // 暗色模式次要色调
        background: '#121212', // 暗色模式背景色
        surface: '#121212',    // 暗色模式表面色
        error: '#CF6679',      // 暗色模式错误色
        textPrimary: '#FFFFFFDE', // 暗色模式主要文本颜色
        textSecondary: '#FFFFFF8A' // 暗色模式次要文本颜色
      };
    } else {
      this.globalData.theme = {
        primary: '#6200EE',    // 亮色模式主色调
        secondary: '#03DAC6',  // 亮色模式次要色调
        background: '#FFFFFF', // 亮色模式背景色
        surface: '#FFFFFF',    // 亮色模式表面色
        error: '#B00020',      // 亮色模式错误色
        textPrimary: '#000000DE', // 亮色模式主要文本颜色
        textSecondary: '#0000008A' // 亮色模式次要文本颜色
      };
    }
  },
  
  // 检查小程序版本更新
  checkUpdate() {
    if (wx.canIUse('getUpdateManager')) {
      const updateManager = wx.getUpdateManager();
      updateManager.onCheckForUpdate(function(res) {
        if (res.hasUpdate) {
          updateManager.onUpdateReady(function() {
            wx.showModal({
              title: '更新提示',
              content: '新版本已经准备好，是否重启应用？',
              success: function(res) {
                if (res.confirm) {
                  updateManager.applyUpdate();
                }
              }
            });
          });
          updateManager.onUpdateFailed(function() {
            wx.showModal({
              title: '更新提示',
              content: '新版本下载失败，请检查网络后重试'
            });
          });
        }
      });
    }
  },
  
  // 检查用户是否已登录
  checkLogin: function(callback) {
    // 从全局变量中获取token
    var token = this.globalData.token;
    
    // 如果全局变量中没有token，则从本地存储中获取
    if (!token) {
      token = wx.getStorageSync('token');
      
      // 如果找到了token，更新全局变量
      if (token) {
        this.globalData.token = token;
      } else {
        // 如果没有token，表示未登录
        if (callback) {
          callback({
            isLogin: false
          });
        }
        return;
      }
    }
    
    // 发送请求到后端验证token是否有效
    wx.request({
      url: this.globalData.apiBaseUrl + '/api/auth/check-login',
      method: 'GET',
      header: {
        'Authorization': 'Bearer ' + token
      },
      success: (res) => {
        console.log("检查登录响应:", res); 
        if (res.statusCode === 200 && res.data.success) {
          // token有效，已登录
          // 如果返回了用户信息，则保存
          if (res.data.userInfo) {
            this.globalData.userInfo = res.data.userInfo;
          }
          
          if (callback) {
            callback({
              isLogin: true
            });
          }
        } else {
          // token无效，需要重新登录
          // 清除失效的token
          wx.removeStorageSync('token');
          this.globalData.token = null;
          
          if (callback) {
            callback({
              isLogin: false
            });
          }
        }
      },
      fail: (err) => {
        // 请求失败，可能是网络问题
        console.error("检查登录请求失败:", err); 
        if (callback) {
          callback({
            isLogin: false,
            networkError: true
          });
        }
      }
    });
  },
  
  // 执行微信登录
  login: function(callback) {
    console.log("开始执行微信登录流程");
    
    // 使用wx.login获取登录凭证code
    wx.login({
      success: (res) => {
        if (res.code) {
          console.log("获取到微信登录code成功");
          
          // 请求登录 - 直接使用code登录，不再获取用户信息
          console.log("尝试直接使用code登录");
          this.loginWithCode(res.code, null, callback);
        } else {
          // 获取code失败
          console.error('获取code失败:', res.errMsg);
          wx.showToast({
            title: '获取登录凭证失败',
            icon: 'none'
          });
          
          if (callback) {
            callback(false);
          }
        }
      },
      fail: (err) => {
        // wx.login调用失败
        console.error('wx.login调用失败:', err);
        wx.showToast({
          title: '微信登录失败',
          icon: 'none'
        });
        
        if (callback) {
          callback(false);
        }
      }
    });
  },
  
  // 使用code发起登录请求
  loginWithCode: function(code, userInfo, callback) {
    console.log("调用后端登录接口");
    
    wx.request({
      url: this.globalData.apiBaseUrl + '/api/auth/wx-login',
      method: 'POST',
      data: {
        code: code,
        userInfo: userInfo
      },
      success: (response) => {
        console.log("登录响应状态码:", response.statusCode);
        if (response.data) {
          console.log("登录响应data.success:", response.data.success);
          console.log("登录响应是否有token:", response.data.token ? true : false);
        }
        
        if (response.statusCode === 200 && response.data && response.data.token) {
          // 登录成功，获取到token
          const token = response.data.token;
          
          // 保存token到全局变量和本地存储
          this.globalData.token = token;
          wx.setStorage({
            key: 'token',
            data: token
          });
          
          // 保存用户信息（如果后端返回）
          if (response.data.userInfo) {
            this.globalData.userInfo = response.data.userInfo;
          }
          
          console.log('登录成功，获取到token');
          
          if (callback) {
            callback(true);
          }
        } else {
          // 登录失败
          console.error('登录失败:', response);
          
          let errorMsg = '登录失败';
          if (response.data && response.data.message) {
            errorMsg = response.data.message;
          }
          
          wx.showToast({
            title: errorMsg,
            icon: 'none',
            duration: 3000
          });
          
          if (callback) {
            callback(false);
          }
        }
      },
      fail: (err) => {
        // 请求失败
        console.error('登录请求失败:', err);
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none',
          duration: 3000
        });
        
        if (callback) {
          callback(false);
        }
      }
    });
  }
});
