<template>
  <div class="farmer-support-page">
    <!-- 顶部横幅区域 -->
    <section class="hero-section">
      <v-parallax
        :src="require('@/assets/pic/coffee_pick.jpg')"
        height="450"
      >
        <div class="overlay"></div>
        <v-container class="fill-height">
          <v-row align="center" justify="center">
            <v-col cols="12" class="text-center">
              <div class="hero-content">
                <h1 class="text-h2 font-weight-bold white--text mb-4 hero-title">
                  Farmer Support
                </h1>
                <div class="divider-line mb-4"></div>
                <p class="text-h5 white--text hero-subtitle">
                  Support for coffee farmers, building a sustainable future together
                </p>
                <v-btn
                  color="primary"
                  large
                  class="mt-6 hero-btn"
                  depressed
                >
                  Learn More
                  <v-icon right>mdi-arrow-right</v-icon>
                </v-btn>
              </div>
            </v-col>
          </v-row>
        </v-container>
      </v-parallax>
    </section>

    <!-- 引言部分 -->
    <section class="intro-section py-12">
      <v-container>
        <v-row justify="center">
          <v-col cols="12" md="8" lg="7" class="text-center">
            <h2 class="section-title text-center">Our Support Plan</h2>
            <p class="intro-text">
              OB coffee farmer support plan aims to provide technical, financial, and educational support to global coffee farmers,
              helping them improve coffee quality, increase income, and promote environmental sustainability.
              We believe that by empowering farmers, we can create a more beautiful coffee industry ecosystem together.
            </p>
            <div class="stats-row mt-8">
              <v-row>
                <v-col cols="4">
                  <div class="stat-item">
                    <div class="stat-number primary--text">18,000+</div>
                    <div class="stat-label">Benefited Farmers</div>
                  </div>
                </v-col>
                <v-col cols="4">
                  <div class="stat-item">
                    <div class="stat-number primary--text">12</div>
                    <div class="stat-label">Supporting Countries</div>
                  </div>
                </v-col>
                <v-col cols="4">
                  <div class="stat-item">
                    <div class="stat-number primary--text">$8.5M</div>
                    <div class="stat-label">Investment Amount</div>
                  </div>
                </v-col>
              </v-row>
            </div>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <!-- 主要内容区域 -->
    <section class="support-programs-section py-10 grey-bg">
      <v-container>
        <v-row justify="center">
          <v-col cols="12" md="10" lg="9">
            <h2 class="section-title text-center mb-10">Support Projects</h2>
            
            <!-- 可持续种植技术 -->
            <transition-group name="fade-up-stagger" tag="div">
              <v-card 
                class="mb-10 support-card" 
                elevation="4" 
                key="tech"
              >
                <v-row no-gutters>
                  <v-col cols="12" md="5">
                    <v-img
                      :src="require('@/assets/pic/sustainable.jpg')"
                      gradient="to right, rgba(0,0,0,0.4), rgba(0,0,0,0.1)"
                      height="100%"
                      class="fill-height card-image"
                    >
                      <div class="d-flex align-end justify-start fill-height pa-6">
                        <div class="card-badge">Technology</div>
                      </div>
                    </v-img>
                  </v-col>
                  <v-col cols="12" md="7">
                    <v-card-text class="pa-6">
                      <h2 class="text-h4 mb-4 card-title">Sustainable Farming Technology</h2>
                      <p class="text-body-1 mb-4">
                         We provide innovative sustainable farming technology and practices to help farmers increase yields and quality,
                         while reducing environmental impact and addressing climate change challenges.
                      </p>
                      <div class="mb-6">
                        <div class="feature-item">
                          <v-icon color="primary" class="mr-2">mdi-sprout</v-icon>
                          <span>Organic Fertilizer and Compost Production Guide</span>
                        </div>
                        <div class="feature-item">
                          <v-icon color="primary" class="mr-2">mdi-weather-sunny</v-icon>
                          <span>Shade Planting and Soil Conservation Technology</span>
                        </div>
                        <div class="feature-item">
                          <v-icon color="primary" class="mr-2">mdi-bug-outline</v-icon>
                          <span>Biological Diversity Pest Control Manual</span>
                        </div>
                      </div>
                      <v-btn
                        color="primary"
                        outlined
                        elevation="0"
                        class="card-btn"
                        @click="navigateToDetail('sustainable')"
                      >
                        View Details
                        <v-icon right>mdi-arrow-right</v-icon>
                      </v-btn>
                    </v-card-text>
                  </v-col>
                </v-row>
              </v-card>

              <!-- 咖啡种植基金 -->
              <v-card 
                class="mb-10 support-card" 
                elevation="4" 
                key="fund"
              >
                <v-row no-gutters>
                  <v-col cols="12" md="5">
                    <v-img
                      :src="require('@/assets/pic/fund.png')"
                      gradient="to right, rgba(0,0,0,0.4), rgba(0,0,0,0.1)"
                      height="100%"
                      class="fill-height card-image"
                    >
                      <div class="d-flex align-end justify-start fill-height pa-6">
                        <div class="card-badge">Funding</div>
                      </div>
                    </v-img>
                  </v-col>
                  <v-col cols="12" md="7">
                    <v-card-text class="pa-6">
                      <h2 class="text-h4 mb-4 card-title">Coffee Farming Fund</h2>
                      <p class="text-body-1 mb-4">
                         We provide interest-free or low-interest loans and funding support to help farmers obtain the funds needed for farm renovation, equipment update, and project improvement.
                      </p>
                      <div class="mb-6">
                        <div class="feature-item">
                          <v-icon color="primary" class="mr-2">mdi-cash</v-icon>
                          <span>Fund for Farm Infrastructure Renovation</span>
                        </div>
                        <div class="feature-item">
                          <v-icon color="primary" class="mr-2">mdi-seed</v-icon>
                          <span>High-Quality Seedling and Planting Subsidy Plan</span>
                        </div>
                        <div class="feature-item">
                          <v-icon color="primary" class="mr-2">mdi-tools</v-icon>
                          <span>Modernization Update Assistance for Processing Equipment</span>
                        </div>
                      </div>
                      <v-btn
                        color="primary"
                        outlined
                        elevation="0"
                        class="card-btn"
                        @click="navigateToDetail('fund')"
                      >
                        View Details
                        <v-icon right>mdi-arrow-right</v-icon>
                      </v-btn>
                    </v-card-text>
                  </v-col>
                </v-row>
              </v-card>

              <!-- 申请甄选农场 -->
              <v-card 
                class="mb-10 support-card" 
                elevation="4" 
                key="selection"
              >
                <v-row no-gutters>
                  <v-col cols="12" md="5">
                    <v-img
                      :src="require('@/assets/pic/farm.jpg')"
                      gradient="to right, rgba(0,0,0,0.4), rgba(0,0,0,0.1)"
                      height="100%"
                      class="fill-height card-image"
                    >
                      <div class="d-flex align-end justify-start fill-height pa-6">
                        <div class="card-badge">Certification</div>
                      </div>
                    </v-img>
                  </v-col>
                  <v-col cols="12" md="7">
                    <v-card-text class="pa-6">
                      <h2 class="text-h4 mb-4 card-title">Application for Certified Farm</h2>
                      <p class="text-body-1 mb-4">
                         Through our farm certification program, farms that meet sustainable standards can obtain higher purchase prices,
                         stable purchase commitments, and global market promotion opportunities.
                      </p>
                      <div class="mb-6">
                        <div class="feature-item">
                          <v-icon color="primary" class="mr-2">mdi-clipboard-check</v-icon>
                          <span>Detailed Explanation of Farm Sustainable Certification Standards</span>
                        </div>
                        <div class="feature-item">
                          <v-icon color="primary" class="mr-2">mdi-file-document</v-icon>
                          <span>Application Process and Required File Guide</span>
                        </div>
                        <div class="feature-item">
                          <v-icon color="primary" class="mr-2">mdi-handshake</v-icon>
                          <span>OB Farm Cooperation Rights and Responsibility Description</span>
                        </div>
                      </div>
                      <v-btn
                        color="primary"
                        outlined
                        elevation="0"
                        class="card-btn"
                        @click="$router.push('/farm-certification')"
                      >
                        立即申请
                        <v-icon right>mdi-arrow-right</v-icon>
                      </v-btn>
                    </v-card-text>
                  </v-col>
                </v-row>
              </v-card>

              <!-- 在线学习视频 -->
              <v-card 
                class="mb-10 support-card" 
                elevation="4" 
                key="learning"
              >
                <v-row no-gutters>
                  <v-col cols="12" md="5">
                    <v-img
                      :src="require('@/assets/pic/learning.jpg')"
                      gradient="to right, rgba(0,0,0,0.4), rgba(0,0,0,0.1)"
                      height="100%"
                      class="fill-height card-image"
                    >
                      <div class="d-flex align-end justify-start fill-height pa-6">
                        <div class="card-badge">Education</div>
                      </div>
                    </v-img>
                  </v-col>
                  <v-col cols="12" md="7">
                    <v-card-text class="pa-6">
                      <h2 class="text-h4 mb-4 card-title">Online Learning Resources</h2>
                      <p class="text-body-1 mb-4">
                         We provide rich online learning resources and training courses, including video tutorials, online lectures, and expert guidance,
                         to help farmers master the latest farming technology and management knowledge.
                      </p>
                      <div class="mb-6">
                        <div class="feature-item">
                          <v-icon color="primary" class="mr-2">mdi-play-circle</v-icon>
                          <span>Coffee Planting and Processing Technology Course</span>
                        </div>
                        <div class="feature-item">
                          <v-icon color="primary" class="mr-2">mdi-school</v-icon>
                          <span>Global Expert Online Guidance and Q&A</span>
                        </div>
                        <div class="feature-item">
                          <v-icon color="primary" class="mr-2">mdi-account-group</v-icon>
                          <span>Excellent Farmer Experience Sharing Community</span>
                        </div>
                      </div>
                      <v-btn
                        color="primary"
                        outlined
                        elevation="0"
                        class="card-btn"
                        @click="navigateToDetail('learning')"
                      >
                        View Details
                        <v-icon right>mdi-arrow-right</v-icon>
                      </v-btn>
                    </v-card-text>
                  </v-col>
                </v-row>
              </v-card>
            </transition-group>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <!-- 申请流程区域 -->
    <section class="process-section py-12">
      <v-container>
        <v-row justify="center">
          <v-col cols="12" md="10" lg="8" class="text-center">
            <h2 class="section-title text-center mb-10">Application Process</h2>
            <v-stepper 
              alt-labels 
              class="process-stepper transparent-bg elevation-0"
              value="1"
            >
              <v-stepper-header class="elevation-0 transparent-bg">
                <v-stepper-step 
                  step="1" 
                  color="primary"
                  complete
                >
                  Fill in Application
                </v-stepper-step>

                <v-divider></v-divider>

                <v-stepper-step 
                  step="2" 
                  color="primary"
                >
                  Initial Review
                </v-stepper-step>

                <v-divider></v-divider>

                <v-stepper-step 
                  step="3" 
                  color="primary"
                >
                  Farm Visit
                </v-stepper-step>

                <v-divider></v-divider>

                <v-stepper-step 
                  step="4" 
                  color="primary"
                >
                  Plan Development
                </v-stepper-step>

                <v-divider></v-divider>

                <v-stepper-step 
                  step="5" 
                  color="primary"
                >
                  Cooperation Begins
                </v-stepper-step>
              </v-stepper-header>
            </v-stepper>
            
            <v-btn
              color="primary"
              large
              class="mt-10"
              depressed
            >
              Apply Now
              <v-icon right>mdi-send</v-icon>
            </v-btn>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <!-- 成功案例部分 -->
    <section class="success-stories-section py-12 cream-bg">
      <v-container>
        <v-row justify="center">
          <v-col cols="12" class="text-center mb-8">
            <h2 class="section-title text-center mb-2">Success Stories</h2>
            <p class="section-subtitle mb-8">
              Farmers around the world have improved productivity and quality of life through our support plan
            </p>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" md="4" v-for="(story, index) in successStories" :key="index">
            <v-card class="story-card">
              <v-img
                :src="story.image"
                height="200"
                gradient="to bottom, rgba(0,0,0,0), rgba(0,0,0,0.7)"
              >
                <div class="d-flex align-end fill-height pa-6">
                  <div>
                    <div class="white--text text-h6 font-weight-bold">{{ story.name }}</div>
                    <div class="white--text text-subtitle-2">{{ story.location }}</div>
                  </div>
                </div>
              </v-img>
              <v-card-text class="pa-4">
                <p class="text-body-2">{{ story.description }}</p>
              </v-card-text>
              <v-card-actions class="px-4 pb-4">
                <v-spacer></v-spacer>
                <v-btn
                  text
                  color="primary"
                  small
                >
                  Read More
                  <v-icon small right>mdi-chevron-right</v-icon>
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </section>
  </div>
</template>

<script>
export default {
  name: 'FarmerSupport',
  data: () => ({
    successStories: [
      {
        name: 'Carlos Mendez Family Farm',
        location: 'Colombia · Antioquia',
        description: 'Mendez family increased their coffee yield by 30% through OB\'s technical support and organic planting subsidy, and also obtained organic certification, raising the price by 40%.',
        image: 'https://dailycoffeenews.com/wp-content/uploads/2018/12/colombia-farmer-eibar-coffee.jpg'
      },
      {
        name: 'Abeba Cooperative Agricultural Cooperative',
        location: 'Ethiopia · Yirgacheffe',
        description: 'This 48-member small farmer cooperative established a modern water washing processing station through OB\'s funding support, greatly improving coffee quality and farmer income.',
        image: 'https://dailycoffeenews.com/wp-content/uploads/2019/10/Ethiopia-coffee-farmer.jpg'
      },
      {
        name: 'Nguyen Family Farm',
        location: 'Vietnam · Central Highlands',
        description: 'Nguyen family participated in OB\'s sustainable transformation plan, shifting from traditional planting to shade planting, not only improving coffee quality but also restoring local ecosystem.',
        image: 'https://www.perfectdailygrind.com/wp-content/uploads/2019/11/Vietnam.jpg'
      }
    ],
    services: [
      {
        id: 1,
        title: 'Sustainable Farming Resources',
        description: 'Access guides and resources for sustainable coffee farming practices.',
        image: require('@/assets/pic/coffee_pick.jpg'),
        route: '/resources'
      },
      {
        id: 2,
        title: 'Funding Applications',
        description: 'Apply for funding to support your coffee farming initiatives.',
        image: require('@/assets/pic/coffee_pick.jpg'),
        route: '/funding'
      },
      {
        id: 3,
        title: 'Certification Process',
        description: 'Learn about the certification process for sustainable coffee farming.',
        image: require('@/assets/pic/coffee_pick.jpg'),
        route: '/certification'
      }
    ]
  }),
  methods: {
    navigateToDetail(section) {
      this.$router.push({
        name: 'ArticleList',
        query: { category: section }
      })
    },
    contactSupport() {
      // 实现联系支持的逻辑
      console.log('Contacting support')
    }
  }
}
</script>

<style scoped>
.farmer-support-page {
  overflow-x: hidden;
}

/* 顶部横幅区域 */
.hero-section {
  position: relative;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.6));
  z-index: 1;
}

.hero-content {
  animation: fadeIn 1.5s ease-in-out;
}

.hero-title {
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
  letter-spacing: 1px;
}

.hero-subtitle {
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
  font-weight: 400;
  letter-spacing: 0.5px;
}

.divider-line {
  width: 80px;
  height: 4px;
  background-color: white;
  margin: 0 auto;
  animation: expandWidth 1.5s ease-out;
}

.hero-btn {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.25) !important;
  transition: all 0.3s ease;
}

.hero-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 25px rgba(0, 0, 0, 0.4) !important;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes expandWidth {
  from { width: 0; }
  to { width: 80px; }
}

/* 引言部分 */
.intro-section {
  background-color: white;
}

.intro-text {
  font-size: 1.2rem;
  line-height: 1.8;
  color: var(--text-light);
  margin-top: 1.5rem;
}

.stat-item {
  text-align: center;
  padding: 1rem;
}

.stat-number {
  font-size: 2.5rem;
  font-weight: 800;
  margin-bottom: 0.5rem;
}

.stat-label {
  font-size: 1rem;
  color: var(--text-light);
  font-weight: 500;
}

/* 主要内容区域 */
.grey-bg {
  background-color: #f9f9f9;
}

.cream-bg {
  background-color: #f8f5f0;
}

.transparent-bg {
  background-color: transparent !important;
}

.section-title {
  position: relative;
  font-size: 2.25rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  color: var(--primary-color);
}

.section-title::after {
  content: "";
  display: block;
  width: 60px;
  height: 4px;
  background-color: var(--primary-color);
  margin: 0.75rem auto 0;
}

.section-subtitle {
  font-size: 1.2rem;
  max-width: 700px;
  margin: 0 auto;
  color: var(--text-light);
}

.support-card {
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
  overflow: hidden;
  border-radius: 12px !important;
}

.support-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1) !important;
}

.card-image {
  transition: all 0.5s ease;
}

.card-image:hover {
  transform: scale(1.05);
}

.card-badge {
  background-color: var(--primary-color);
  color: white;
  padding: 4px 16px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 1px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.card-title {
  position: relative;
  display: inline-block;
  padding-bottom: 8px;
}

.card-title::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 60px;
  height: 3px;
  background-color: var(--primary-color);
}

.feature-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 1.05rem;
  font-weight: 500;
}

.card-btn {
  transition: all 0.3s ease;
}

.card-btn:hover {
  transform: translateX(5px);
}

/* 申请流程 */
.process-stepper {
  margin-top: 2rem;
}

/* 故事卡片 */
.story-card {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  height: 100%;
}

.story-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1) !important;
}

/* 动画效果 */
.fade-up-stagger-enter-active {
  transition: all 0.8s ease;
  transition-delay: calc(0.2s * var(--index));
}

.fade-up-stagger-enter {
  opacity: 0;
  transform: translateY(30px);
}

/* 响应式调整 */
@media (max-width: 960px) {
  .hero-title {
    font-size: 2.5rem !important;
  }
  
  .hero-subtitle {
    font-size: 1.2rem !important;
  }
  
  .intro-text {
    font-size: 1.1rem;
  }
  
  .stat-number {
    font-size: 2rem;
  }
  
  .card-title {
    font-size: 1.8rem !important;
  }
}

@media (max-width: 600px) {
  .hero-title {
    font-size: 2rem !important;
  }
  
  .hero-subtitle {
    font-size: 1rem !important;
  }
  
  .section-title {
    font-size: 1.8rem;
  }
  
  .stat-number {
    font-size: 1.8rem;
  }
  
  .stat-label {
    font-size: 0.9rem;
  }
  
  .card-title {
    font-size: 1.5rem !important;
  }
  
  .feature-item {
    font-size: 0.95rem;
  }
}
</style>
