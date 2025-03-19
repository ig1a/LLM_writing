// components/loading/loading.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    // 是否显示加载状态
    show: {
      type: Boolean,
      value: false
    },
    // 加载文本提示
    text: {
      type: String,
      value: '加载中...'
    },
    // 加载类型：circular（圆形）, dots（点）
    type: {
      type: String,
      value: 'circular'
    },
    // 是否全屏显示
    fullscreen: {
      type: Boolean,
      value: false
    },
    // 加载图标颜色
    color: {
      type: String,
      value: '#6200EE'
    }
  },

  /**
   * 组件的初始数据
   */
  data: {
    // dot动画计时器
    timer: null,
    // 点的数量
    dotCount: 3,
    // 动画帧
    animationFrame: 0
  },

  /**
   * 组件的方法列表
   */
  methods: {
    // 开始点动画
    startDotAnimation() {
      if (this.data.timer) {
        clearInterval(this.data.timer);
      }
      
      this.data.timer = setInterval(() => {
        this.setData({
          animationFrame: (this.data.animationFrame + 1) % (this.data.dotCount + 1)
        });
      }, 300);
    },
    
    // 停止点动画
    stopDotAnimation() {
      if (this.data.timer) {
        clearInterval(this.data.timer);
        this.data.timer = null;
      }
    }
  },
  
  /**
   * 组件生命周期函数
   */
  lifetimes: {
    attached() {
      // 如果是dots类型，开始动画
      if (this.data.type === 'dots' && this.data.show) {
        this.startDotAnimation();
      }
    },
    detached() {
      // 组件销毁时清除定时器
      this.stopDotAnimation();
    }
  },
  
  /**
   * 组件的监听属性
   */
  observers: {
    'show, type': function(show, type) {
      // 监听show和type变化，控制动画
      if (show && type === 'dots') {
        this.startDotAnimation();
      } else if (!show) {
        this.stopDotAnimation();
      }
    }
  }
});
