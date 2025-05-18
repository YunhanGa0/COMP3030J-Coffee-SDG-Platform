import Vue from 'vue'
import 'vuetify/dist/vuetify.min.css' // 使用预编译的 CSS
import Vuetify from 'vuetify'

Vue.use(Vuetify)

export default new Vuetify({
  theme: {
    themes: {
      light: {
        primary: '#00704A',     // 星巴克主要品牌绿色
        secondary: '#27251F',   // 深灰色/黑色
        accent: '#C8102E',      // 星巴克红色（用于强调和节日主题）
        background: '#F9F9F9',  // 浅灰色背景
        info: '#4C9EEB',        // 信息蓝
        success: '#006241',     // 深绿色
        warning: '#FFB81C',     // 警告黄
        error: '#D62B1F',       // 错误红
        anchor: '#00704A',      // 链接颜色
        // 自定义颜色
        'starbucks-green-light': '#00A862', // 浅一点的品牌绿
        'starbucks-gold': '#C3A86B',        // 星巴克金色（用于会员相关）
        'starbucks-cream': '#FFF4D4',       // 奶油色（用于背景）
        'starbucks-brown': '#3D3935',       // 咖啡棕色
      }
    },
    options: {
      customProperties: true    // 启用CSS变量
    }
  }
}) 