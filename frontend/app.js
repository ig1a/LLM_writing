// app.js
App({
  // 全局数据
  globalData: {
    userInfo: null,
    currentBook: null,
    theme: {
      primary: '#6200EE',    // 主色调
      secondary: '#03DAC6',  // 次要色调
      background: '#FFFFFF', // 背景色
      surface: '#FFFFFF',    // 表面色
      error: '#B00020',      // 错误色
      textPrimary: '#000000DE', // 主要文本颜色
      textSecondary: '#0000008A' // 次要文本颜色
    }
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
  }
});
