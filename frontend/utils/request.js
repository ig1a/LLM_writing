/**
 * 网络请求工具类
 */

// 服务器基础URL
const BASE_URL = 'https://api.example.com';

/**
 * 封装微信请求API
 * @param {Object} options - 请求选项
 * @param {string} options.url - 请求地址
 * @param {string} [options.method='GET'] - 请求方法
 * @param {Object} [options.data={}] - 请求数据
 * @param {Object} [options.header={}] - 请求头
 * @param {boolean} [options.loading=true] - 是否显示加载提示
 * @returns {Promise} - Promise对象
 */
const request = (options) => {
  // 默认值
  options = Object.assign({
    method: 'GET',
    data: {},
    header: {
      'content-type': 'application/json'
    },
    loading: true
  }, options);

  // 显示加载提示
  if (options.loading) {
    wx.showLoading({
      title: '加载中...',
      mask: true
    });
  }

  // 添加认证信息（如果有）
  const token = wx.getStorageSync('token');
  if (token) {
    options.header.Authorization = `Bearer ${token}`;
  }

  // 请求完整URL
  const url = /^(http|https):\/\//.test(options.url) 
    ? options.url 
    : BASE_URL + options.url;

  // 返回Promise
  return new Promise((resolve, reject) => {
    wx.request({
      url,
      method: options.method,
      data: options.data,
      header: options.header,
      success: (res) => {
        // 隐藏加载提示
        if (options.loading) {
          wx.hideLoading();
        }

        // 处理请求结果
        if (res.statusCode >= 200 && res.statusCode < 300) {
          resolve(res.data);
        } else if (res.statusCode === 401) {
          // 未授权，清除本地token并跳转到登录页
          wx.removeStorageSync('token');
          wx.showToast({
            title: '请重新登录',
            icon: 'none',
            duration: 2000
          });
          // 跳转到首页
          setTimeout(() => {
            wx.reLaunch({
              url: '/pages/index/index'
            });
          }, 2000);
          reject(new Error('未授权，请重新登录'));
        } else {
          // 其他错误
          wx.showToast({
            title: res.data.message || '请求失败',
            icon: 'none',
            duration: 2000
          });
          reject(new Error(res.data.message || '请求失败'));
        }
      },
      fail: (err) => {
        // 隐藏加载提示
        if (options.loading) {
          wx.hideLoading();
        }
        // 网络错误
        wx.showToast({
          title: '网络异常，请稍后重试',
          icon: 'none',
          duration: 2000
        });
        reject(new Error('网络异常，请稍后重试'));
      }
    });
  });
};

/**
 * 发送书籍条形码到后端
 * @param {string} barcode - 书籍条形码
 * @returns {Promise} - Promise对象
 */
const getBookInfo = (barcode) => {
  return request({
    url: '/api/book/info',
    method: 'POST',
    data: { barcode }
  });
};

/**
 * 发送聊天消息到LLM
 * @param {string} bookId - 书籍ID
 * @param {string} message - 用户消息
 * @param {Array} history - 历史消息记录
 * @returns {Promise} - Promise对象
 */
const sendChatMessage = (bookId, message, history = []) => {
  return request({
    url: '/api/chat/message',
    method: 'POST',
    data: {
      bookId,
      message,
      history
    }
  });
};

// 导出工具方法
module.exports = {
  request,
  getBookInfo,
  sendChatMessage
};
