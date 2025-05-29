<template>
  <div class="home-container" @scroll="handleScroll" ref="homeContainer">
    <!-- Hero Section -->
    <section class="hero-fullscreen">
      <v-img
        :src="require('../assets/pic/Homepic.jpg')"
        height="100vh"
        gradient="to bottom, rgba(0,0,0,.4), rgba(0,0,0,.7)"
        class="hero-image"
      >
        <v-container class="fill-height">
          <v-row align="center" justify="center">
            <v-col cols="12" class="text-center">
              <div class="hero-content">
                <h1 class="main-title mb-6">
                  Sustainable Coffee Development
                  <span class="highlight-text">Global Initiative</span>
                </h1>
                <p class="main-subtitle mb-12">
                  Connecting coffee growers and consumers through sustainable development
                </p>
                <v-btn
                  x-large
                  color="primary"
                  rounded
                  elevation="2"
                  class="explore-btn px-8"
                  :to="{ path: '/plantation' }"
                >
                  Learn More
                  <v-icon right>mdi-arrow-right</v-icon>
                </v-btn>
              </div>
            </v-col>
          </v-row>
          <!-- 滚动提示 -->
          <div class="scroll-hint">
            <span class="scroll-text">SCROLL DOWN</span>
            <v-icon color="white" class="scroll-arrow">mdi-chevron-down</v-icon>
          </div>
        </v-container>
      </v-img>
    </section>

    <!-- 视频对话框 -->
    <v-dialog v-model="showVideo" max-width="900">
      <v-card>
        <v-card-title class="headline">
          Starbucks Sustainability Story
          <v-spacer></v-spacer>
          <v-btn icon @click="showVideo = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text class="pa-0">
          <div class="video-container">
            <iframe
              width="100%"
              height="500"
              src="https://www.youtube.com/embed/7MULDTn897k"
              frameborder="0"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowfullscreen
            ></iframe>
          </div>
        </v-card-text>
      </v-card>
    </v-dialog>

    <!-- Statistics Section -->
    <section class="stats-section py-12">
      <v-container>
        <v-row justify="center">
          <v-col cols="12" md="10" lg="8" class="text-center">
            <h2 class="section-title primary--text">Our Global Impact</h2>
            <v-divider class="mx-auto my-6" style="width: 80px; height: 4px;"></v-divider>
            <p class="text-h6 mb-12">
              Starbucks is committed to sustainable coffee development
            </p>
          </v-col>
        </v-row>
        <v-row justify="center">
          <v-col cols="6" md="3" v-for="(stat, index) in stats" :key="index">
            <div class="stat-item text-center">
              <div class="stat-number primary--text">{{ stat.number }}</div>
              <div class="stat-label">{{ stat.text }}</div>
            </div>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <!-- Features Section -->
    <section class="features-section py-12 green-bg">
      <v-container>
        <v-row justify="center">
          <v-col cols="12" class="text-center mb-8">
            <h2 class="section-title white--text">Our Coffee Responsibility</h2>
            <v-divider class="mx-auto my-6 white" style="width: 80px; height: 4px;"></v-divider>
            <p class="features-subtitle white--text">
              Behind every cup of Starbucks coffee lies a story of sustainable development
            </p>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" md="4" v-for="(article, index) in articles" :key="article.id">
            <v-card class="feature-card mx-auto mb-6" max-width="370">
              <v-img
                :src="article.imageUrl"
                height="220"
                class="white--text"
                gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.7)"
              >
                <v-card-title class="text-h5 font-weight-bold">
                  {{ article.title }}
                </v-card-title>
              </v-img>
              <v-card-text class="text-body-1 px-4 pt-4 pb-2">
                {{ article.summary }}
              </v-card-text>
              <v-card-actions class="px-4 pb-4">
                <v-spacer></v-spacer>
                <v-btn
                  text
                  color="primary"
                  class="px-0"
                  :to="'/articles/' + article.id"
                >
                  Read More
                  <v-icon small class="ml-1">mdi-arrow-right</v-icon>
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <!-- 咖啡地图滚动视差部分 -->
    <!--<section class="map-parallax">
      <v-parallax
        :src="require('../assets/pic/worldmap.png')"
        height="500"
      >
        <div class="map-overlay"></div>
        <v-container class="fill-height">
          <v-row align="center" justify="center">
            <v-col cols="12" md="8" class="text-center">
              <h2 class="text-h2 font-weight-bold white--text mb-6">Explore Coffee World</h2>
              <p class="text-h6 white--text mb-8">
                Discover global coffee origins and sustainable farming stories through our interactive map
              </p>
              <v-btn
                x-large
                rounded
                outlined
                dark
                :to="{ path: '/explore' }"
                class="explore-map-btn"
              >
                Open Global Coffee Map
                <v-icon right>mdi-map</v-icon>
              </v-btn>
              <v-btn
                x-large
                color="primary"
                rounded
                elevation="2"
                class="explore-btn px-8"
                :to="{ path: '/plantation' }"
              >
                Learn More
                <v-icon right>mdi-arrow-right</v-icon>
              </v-btn>
            </v-col>
          </v-row>
        </v-container>
      </v-parallax>
    </section>
    -->

    <!-- Signup Section -->
    <section class="signup-section py-12 cream-bg">
      <v-container>
        <v-row justify="center" align="center">
          <v-col cols="12" md="6" class="text-center text-md-left">
            <h2 class="text-h3 font-weight-bold mb-4 primary--text">
              Join Starbucks Sustainable Coffee Membership
            </h2>
            <p class="text-h6 mb-8">
              Become a member to receive exclusive coffee culture information and participate in sustainable development projects
            </p>
          </v-col>
          <v-col cols="12" md="6">
            <v-card class="pa-6 signup-card" elevation="10">
              <v-form>
                <v-text-field
                  label="Your Name"
                  outlined
                  dense
                ></v-text-field>
                <v-text-field
                  label="Email"
                  outlined
                  dense
                ></v-text-field>
                <v-checkbox
                  label="I agree to receive Starbucks latest news and offers"
                ></v-checkbox>
                <v-btn
                  block
                  color="primary"
                  large
                  class="white--text"
                >
                  Register Now
                </v-btn>
              </v-form>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <!-- Write Article Button -->
    <v-btn
      color="primary"
      fab
      fixed
      bottom
      right
      class="mb-4 mr-4"
      @click="$router.push('/article/editor')"
    >
      <v-tooltip top>
        <template v-slot:activator="{ on, attrs }">
          <v-icon v-bind="attrs" v-on="on">mdi-pencil</v-icon>
        </template>
        <span>Write Article</span>
      </v-tooltip>
    </v-btn>

    <!-- Snackbar -->
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
          Close
        </v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import axios from 'axios'

export default {
  name: 'Home',
  data: () => ({
    showVideo: false,
    isScrolled: false,
    stats: [
      { number: '75+', text: 'Origin Countries' },
      { number: '400,000+', text: 'Coffee Farmers' },
      { number: '99%', text: 'Ethically Sourced Coffee' },
      { number: '30 Years', text: 'Sustainable Development History' }
    ],
    articles: [],
    snackbar: {
      show: false,
      text: '',
      color: 'success'
    }
  }),
  computed: {
    ...mapGetters('auth', ['isAuthenticated', 'currentUser']),
    isAdmin() {
      return this.currentUser && this.currentUser.role === 'ADMIN'
    }
  },
  mounted() {
    window.addEventListener('scroll', this.handleScroll);
    // 初始化时设置导航栏为透明
    this.$root.$emit('update-nav-transparency', true);
    this.handleScroll();
    // 获取文章列表
    this.fetchArticles();
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll);
  },
  // 添加路由监听
  watch: {
    '$route'(to, from) {
      // 当路由变化时，如果是回到主页，就重新获取文章
      if (to.path === '/') {
        this.fetchArticles();
      }
    }
  },
  methods: {
    async fetchArticles() {
      try {
        const response = await axios.get('/api/articles');
        if (response.data.code === 200) {
          // 获取所有文章并按创建时间排序
          this.articles = response.data.data.sort((a, b) => {
            const dateA = new Date(a.createTime || a.createDate);
            const dateB = new Date(b.createTime || b.createDate);
            return dateB - dateA;
          });
        } else {
          this.showMessage('获取文章失败: ' + response.data.message, 'error');
        }
      } catch (error) {
        console.error('获取文章错误:', error);
        this.showMessage('获取文章失败', 'error');
      }
    },
    handleScroll() {
      const scrollPosition = window.scrollY;
      this.isScrolled = scrollPosition > 50;
      // 根据滚动位置更新导航栏透明度
      this.$root.$emit('update-nav-transparency', !this.isScrolled);
    },
    scrollToContent() {
      const contentSection = document.querySelector('.stats-section');
      if (contentSection) {
        contentSection.scrollIntoView({ behavior: 'smooth' });
      }
    },
    showMessage(text, color = 'success') {
      this.snackbar = {
        show: true,
        text,
        color
      }
    }
  }
}
</script>

<style scoped>
/* 全局样式 */
.home-container {
  overflow-x: hidden;
  color: #333;
  height: 100vh;
  overflow-y: auto;
}

.section-title {
  font-size: 2.5rem;
  font-weight: 700;
  position: relative;
}

/* Hero区域样式 */
.hero-fullscreen {
  position: relative;
  height: 100vh;
  overflow: hidden;
}

.hero-image {
  height: 100vh !important;
  position: relative;
}

.hero-content {
  animation: fadeIn 1.5s ease-in-out;
  max-width: 800px;
  margin: 0 auto;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.main-title {
  font-size: 3.5rem;
  font-weight: 800;
  line-height: 1.2;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  margin-bottom: 1.5rem;
}

.highlight-text {
  color: #00d676;
  position: relative;
  display: inline-block;
}

.highlight-text:after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 8px;
  background-color: rgba(0, 214, 118, 0.3);
  z-index: -1;
}

.main-subtitle {
  font-size: 1.5rem;
  font-weight: 400;
  color: rgba(255, 255, 255, 0.9);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  max-width: 600px;
  margin: 0 auto 2rem;
  line-height: 1.5;
}

.explore-btn {
  font-size: 1.1rem !important;
  font-weight: 600 !important;
  letter-spacing: 0.5px !important;
  padding: 0 2rem !important;
  height: 54px !important;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1) !important;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.25) !important;
}

.explore-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 7px 30px rgba(0, 0, 0, 0.4) !important;
}

.scroll-hint {
  position: absolute;
  bottom: 40px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% { transform: translateY(0) translateX(-50%); }
  40% { transform: translateY(-20px) translateX(-50%); }
  60% { transform: translateY(-10px) translateX(-50%); }
}

.scroll-text {
  color: white;
  font-size: 0.8rem;
  letter-spacing: 2px;
  margin-bottom: 8px;
  opacity: 0.8;
}

.scroll-arrow {
  animation: arrowBounce 2s infinite;
}

@keyframes arrowBounce {
  0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-10px); }
  60% { transform: translateY(-5px); }
}

/* 数据统计区域 */
.stats-section {
  background-color: #fafafa;
  position: relative;
}

.stats-section:before {
  content: '';
  position: absolute;
  top: -80px;
  left: 0;
  right: 0;
  height: 80px;
  background: linear-gradient(to top, rgba(250,250,250,1) 0%, rgba(250,250,250,0) 100%);
  z-index: 5;
}

.stat-item {
  transition: all 0.3s ease;
  padding: 2rem !important;
  border-radius: 12px;
  position: relative;
  overflow: hidden;
}

.stat-item:hover {
  transform: translateY(-5px);
  background-color: rgba(0, 112, 74, 0.05);
}

.stat-item:before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 0;
  background-color: var(--primary-color);
  transition: height 0.3s ease;
}

.stat-item:hover:before {
  height: 100%;
}

.stat-number {
  font-size: 3rem;
  font-weight: 800;
  color: var(--primary-color);
  margin-bottom: 0.5rem;
}

.stat-label {
  font-size: 1.2rem;
  font-weight: 500;
  color: var(--text-color);
}

/* 特色区域 */
.green-bg {
  background: linear-gradient(135deg, #054733 0%, #00513a 100%);
  position: relative;
}

.green-bg:before, .green-bg:after {
  content: '';
  position: absolute;
  width: 200px;
  height: 200px;
  background-color: rgba(255, 255, 255, 0.05);
  border-radius: 50%;
}

.green-bg:before {
  top: -100px;
  left: -100px;
}

.green-bg:after {
  bottom: -100px;
  right: -100px;
}

.section-title:after {
  content: "";
  position: absolute;
  bottom: -16px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 4px;
  background-color: currentColor;
}

.features-subtitle {
  font-size: 1.2rem;
  opacity: 0.9;
  margin-top: 2rem;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
}

.feature-card {
  border-radius: 12px;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1) !important;
  overflow: hidden;
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2) !important;
}

/* 地图视差区域 */
.map-parallax {
  position: relative;
}

.map-overlay {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background: radial-gradient(circle, rgba(0,0,0,0.4) 0%, rgba(0,0,0,0.7) 100%);
  z-index: 1;
}

.explore-map-btn {
  border-width: 2px !important;
  font-weight: 600 !important;
  letter-spacing: 0.5px !important;
  transition: all 0.3s ease !important;
}

.explore-map-btn:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
  transform: translateY(-3px);
  box-shadow: 0 7px 20px rgba(0, 0, 0, 0.3) !important;
}

/* 注册区域 */
.cream-bg {
  background-color: #f8f5f0;
  position: relative;
  overflow: hidden;
}

.cream-bg:before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(196, 164, 132, 0.2) 0%, rgba(196, 164, 132, 0) 70%);
  z-index: 0;
}

.signup-card {
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05) !important;
  transition: all 0.3s ease;
  position: relative;
  z-index: 1;
}

.signup-card:hover {
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1) !important;
}

/* 响应式调整 */
@media (max-width: 960px) {
  .main-title {
    font-size: 2.8rem;
  }

  .main-subtitle {
    font-size: 1.3rem;
  }

  .section-title {
    font-size: 2rem;
  }
}

@media (max-width: 600px) {
  .main-title {
    font-size: 2.2rem;
  }

  .main-subtitle {
    font-size: 1.1rem;
  }

  .stat-number {
    font-size: 2.5rem;
  }

  .stat-label {
    font-size: 1rem;
  }

  .section-title {
    font-size: 1.8rem;
  }
}
</style>
