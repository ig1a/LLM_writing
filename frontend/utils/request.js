/**
 * 网络请求工具类
 */

// 服务器基础URL
// 修改为您的后端服务器地址，在本地开发时使用localhost
var BASE_URL = 'http://localhost:8080';

// 测试模式开关 - 设置为true使用本地测试数据，设置为false使用真实API
// 当后端服务准备就绪后，将此设置为false以连接真实后端
var TEST_MODE = false;

// 测试数据 - 用于在没有后端API情况下进行前端测试
var TEST_DATA = {
  // 书籍信息测试数据
  books: {
    // 三体
    '9787536692930': {
      id: '1001',
      title: '三体',
      author: '刘慈欣',
      publisher: '重庆出版社',
      isbn: '9787536692930',
      cover: 'https://img3.doubanio.com/view/subject/l/public/s2768378.jpg',
      description: '文化大革命如火如荼进行的同时，军方探寻外星文明的绝秘计划"红岸工程"取得了突破性进展。但在按下发射键的那一刻，历经劫难的叶文洁没有意识到，她彻底改变了人类的命运。地球文明向宇宙发出的第一声啼鸣，以太阳为中心，以光速向宇宙深处飞驰……'
    },
    // 活着
    '9787506365437': {
      id: '1002',
      title: '活着',
      author: '余华',
      publisher: '作家出版社',
      isbn: '9787506365437',
      cover: 'https://img2.doubanio.com/view/subject/l/public/s29053580.jpg',
      description: '《活着》是余华的代表作，讲述了一个人一生的故事，这是一个历经世间沧桑和磨难老人的人生感言，是一幕演绎人生苦难经历的戏剧。小说的叙事者"我"在去乡下收集民间歌谣的途中，遇到了一个爱讲故事的老人富贵，听他讲述了自己坎坷的人生经历。'
    },
    // 人类简史
    '9787508647357': {
      id: '1003',
      title: '人类简史: 从动物到上帝',
      author: '[以色列] 尤瓦尔·赫拉利',
      publisher: '中信出版社',
      isbn: '9787508647357',
      cover: 'https://img9.doubanio.com/view/subject/l/public/s27814883.jpg',
      description: '《人类简史》是以色列新锐历史学家的一部重磅作品。从十万年前有生命迹象开始到21世纪资本、科技交织的人类发展史。挑战长久以来形成的关于人类进化的常识，这本书带领读者穿越过去，重新了解人类近几万年的发展历程。'
    },
    // 默认书籍 - 用于处理未找到的ISBN
    'default': {
      id: '1000',
      title: '未找到该书籍',
      author: '未知',
      publisher: '未知',
      isbn: '未知',
      cover: '/images/default_book.png',
      description: '抱歉，我们无法找到该ISBN对应的书籍信息。请确认ISBN是否正确，或者尝试其他书籍。'
    }
  },
  
  // 预设的AI回复 - 用于测试聊天功能
  chatResponses: {
    // 三体相关回复
    '1001': {
      default: '《三体》是刘慈欣创作的科幻小说，讲述了地球文明与三体文明之间的复杂故事。有什么具体问题想了解的吗？',
      keywords: {
        '三体': '三体是一个围绕三颗恒星运行的行星系统，由于三体问题的存在，这个系统极度不稳定，导致三体文明面临着生存危机。这也是三体文明寻求地球作为新家园的主要原因。',
        '地球叛军': '地球叛军，又称ETO(地球三体组织)，是一群对人类文明失望，转而支持三体文明入侵地球的人类。叶文洁是该组织的创始人，她在文化大革命中的经历使她对人类失去了信心。',
        '水滴': '水滴是三体文明派往地球的超级武器，外形如同一滴水，表面光滑无暇。它由强互作用力材料制成，几乎坚不可摧，可以轻易切开任何地球材料。在小说中，水滴摧毁了人类的太空舰队。',
        '面壁者': '面壁者是人类为对抗三体入侵而选出的四位顶尖科学家，他们被赋予特殊权力，开发秘密计划来拯救人类。罗辑、希恩斯、雷迪亚兹和比尔·希尔斯是四位面壁者。'
      }
    },
    // 活着相关回复
    '1002': {
      default: '《活着》是余华的代表作，讲述了农民福贵一生的悲惨遭遇。有什么想了解的吗？',
      keywords: {
        '福贵': '福贵是小说的主人公，一个经历了赌博破产、抗美援朝、大跃进、文化大革命等时代变迁的普通农民。他的一生充满了苦难，先后失去了自己的父母、妻子、儿女，最终只剩下一头老牛相伴。',
        '家珍': '家珍是福贵的妻子，小说中刻画了她坚韧、善良的形象。尽管家道中落，她仍然默默支持丈夫，抚养孩子。她因病去世，临终前仍惦记着家人。',
        '有庆': '有庆是福贵的儿子，在一次献血事件中因失血过多死亡。他的死亡是小说中最令人心痛的情节之一，反映了那个年代医疗条件的落后。',
        '凤霞': '凤霞是福贵的女儿，因病失去了听力和说话能力。她嫁给了二喜，生下了儿子苦根，但在分娩时不幸去世。'
      }
    },
    // 人类简史相关回复
    '1003': {
      default: '《人类简史》探讨了从认知革命、农业革命到科学革命，人类如何从弱小的物种成为地球的主宰。您想了解书中的哪个观点或时期？',
      keywords: {
        '认知革命': '认知革命发生在约7万年前，是智人大脑和认知能力的突变，让智人获得了虚构故事、形成大规模合作的能力。这一能力让智人能够以前所未有的方式组织起来，最终成为地球上唯一幸存的人类物种。',
        '农业革命': '农业革命大约发生在1.2万年前，人类从狩猎采集转向农业生产。赫拉利认为这不一定是进步，而可能是一场"陷阱"——人口增加了，但普通人的生活质量反而下降，工作时间更长，营养更差，疾病更多。',
        '科学革命': '科学革命始于约500年前，以承认无知、以实证观察和数学工具为基础。这一革命与帝国主义和资本主义紧密结合，促成了现代科技的发展和人类力量的空前增长。',
        '宗教': '书中将宗教视为一种能够让大量陌生人合作的"虚构现实"或"互为主观"的存在。赫拉利认为像宗教、国家、金钱这样的概念都是人类创造的故事，它们之所以有力量，是因为许多人共同相信它们。'
      }
    },
    // 默认回复 - 用于未知书籍
    'default': {
      default: '抱歉，我没有关于这本书的足够信息。请尝试询问一些基本问题，或者换一本书继续对话。',
      keywords: {}
    }
  }
};

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
var request = function(options) {
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
  var token = wx.getStorageSync('token');
  if (token) {
    options.header.Authorization = 'Bearer ' + token;
  }

  // 请求完整URL
  var url = /^(http|https):\/\//.test(options.url) 
    ? options.url 
    : BASE_URL + options.url;

  // 如果在测试模式，使用mock数据
  if (TEST_MODE) {
    return new Promise(function(resolve, reject) {
      // 模拟网络延迟
      setTimeout(function() {
        if (options.loading) {
          wx.hideLoading();
        }
        
        // 根据请求路径返回不同的测试数据
        if (options.url === '/api/books/isbn') {
          var isbn = options.data.isbn;
          var bookInfo = TEST_DATA.books[isbn] || TEST_DATA.books.default;
          resolve(bookInfo);
        } 
        else if (options.url === '/api/chat/messages') {
          var bookId = options.data.bookId;
          var message = options.data.content;
          var bookResponses = TEST_DATA.chatResponses[bookId] || TEST_DATA.chatResponses.default;
          
          // 检查消息中是否包含关键词
          var response = bookResponses.default;
          var keywords = Object.keys(bookResponses.keywords);
          
          for (var i = 0; i < keywords.length; i++) {
            var keyword = keywords[i];
            if (message.includes(keyword)) {
              response = bookResponses.keywords[keyword];
              break;
            }
          }
          
          resolve({
            message: response,
            timestamp: new Date().toISOString()
          });
        } 
        else {
          // 默认返回成功
          resolve({ success: true });
        }
      }, 1000); // 模拟1秒延迟
    });
  }

  // 真实API请求
  return new Promise(function(resolve, reject) {
    wx.request({
      url: url,
      method: options.method,
      data: options.data,
      header: options.header,
      success: function(res) {
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
          setTimeout(function() {
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
      fail: function(err) {
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
var getBookInfo = function(barcode) {
  return request({
    url: '/api/books/isbn',  // 修改为后端实际API路径
    method: 'GET',           // 修改为GET请求
    data: { isbn: barcode }  // 参数名称修改为isbn
  });
};

/**
 * 发送聊天消息到LLM
 * @param {string} bookId - 书籍ID
 * @param {string} message - 用户消息
 * @param {Array} history - 历史消息记录
 * @returns {Promise} - Promise对象
 */
var sendChatMessage = function(bookId, message, history) {
  if (!history) {
    history = [];
  }
  return request({
    url: '/api/chat/messages',  // 修改为后端实际API路径
    method: 'POST',
    data: {
      bookId: bookId,
      content: message,       // 参数名称修改为content，与后端一致
      history: history
    }
  });
};

// 导出工具方法
module.exports = {
  request: request,
  getBookInfo: getBookInfo,
  sendChatMessage: sendChatMessage
};
