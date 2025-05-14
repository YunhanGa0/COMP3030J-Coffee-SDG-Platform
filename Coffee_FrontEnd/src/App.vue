<template>
  <v-app>
    <top-nav-bar @show-message="showMessage"></top-nav-bar>

    <v-main>
      <router-view></router-view>
    </v-main>

    <!-- 提示信息 -->
    <v-snackbar
      v-model="snackbar.show"
      :color="snackbar.color"
      :timeout="3000"
      top
    >
      {{ snackbar.text }}
      <template v-slot:action="{ attrs }">
        <v-btn
          text
          v-bind="attrs"
          @click="snackbar.show = false"
        >
          关闭
        </v-btn>
      </template>
    </v-snackbar>

    <floating-ai-button @toggle="showAi = !showAi" />

    <ai-chat-modal
      v-if="$route.meta.ai"
      :visible.sync="showAi"
      v-bind="aiProps"
    />
  </v-app>
</template>

<script>
import TopNavBar from '@/components/TopNavBar.vue'
import FloatingAiButton from "./components/FloatingAiButton.vue";
import AiChatModal from "./components/AiChatModal.vue";
import {mapGetters} from "vuex";

export default {
  name: 'App',
  components: {
    AiChatModal,
    FloatingAiButton,
    TopNavBar
  },

  data: () => ({
    snackbar: {
      show: false,
      text: '',
      color: ''
    },
    showAi: false
  }),

  computed: {
    ...mapGetters('articles', ['allArticles']),
    ...mapGetters('farms',    ['allFarms']),

    aiProps() {
      const { name, params } = this.$route

      if (name === 'ArticleDetail') {
        return {
          resource: 'article',
          mode:     'single',
          targetId: params.id
        }
      }
      if (name === 'ArticleList') {
        return {
          resource: 'article',
          mode:     'list',
          dataList: this.allArticles
        }
      }
      if (name === 'FarmDetail') {
        return {
          resource: 'farm',
          mode:     'single',
          targetId: params.id
        }
      }
      if (name === 'FarmList') {
        return {
          resource: 'farm',
          mode:     'list',
          dataList: this.allFarms
        }
      }

      return {}
    }
  },


  methods: {
    showMessage(message) {
      this.snackbar = {
        show: true,
        text: message.text,
        color: message.color
      }
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #333333;
  background-color: #fafafa;
}

.pointer {
  cursor: pointer;
}

/* 全局动画 */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.5s, opacity 0.5s;
}
.slide-enter,
.slide-leave-to {
  transform: translateY(20px);
  opacity: 0;
}

/* 全局颜色变量 */
:root {
  --primary-color: #00704A;
  --secondary-color: #27251F;
  --light-color: #f5f5f5;
  --accent-color: #C4A484;
  --text-color: #333333;
  --text-light: #666666;
}

/* 通用样式类 */
.section-title {
  font-size: 2.25rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  color: var(--primary-color);
  position: relative;
}

.section-title::after {
  content: "";
  display: block;
  width: 60px;
  height: 4px;
  background-color: var(--primary-color);
  margin-top: 12px;
}

.text-primary {
  color: var(--primary-color) !important;
}

.text-secondary {
  color: var(--secondary-color) !important;
}

.bg-primary-light {
  background-color: rgba(0, 112, 74, 0.1) !important;
}

.card-hover {
  transition: all 0.3s ease;
}

.card-hover:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1) !important;
}

/* 自定义滚动条 */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

::-webkit-scrollbar-thumb {
  background-color: #888;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background-color: #666;
}
</style>
