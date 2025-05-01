<template>
  <div class="about-page">
    <!-- 页面标题区域 -->
    <section class="page-header">
      <v-parallax
        :src="require('@/assets/pic/咖啡园区3.jpg')"
        height="400"
      >
        <div class="overlay"></div>
        <v-container class="fill-height">
          <v-row align="center" justify="center">
            <v-col cols="12" class="text-center">
              <h1 class="text-h2 font-weight-bold white--text mb-4 animated-title">About Us</h1>
              <div class="subtitle-line"></div>
              <p class="text-h6 white--text mt-6 mb-0 subtitle-text">
                Connecting Coffee and People, Creating a Sustainable Future
              </p>
            </v-col>
          </v-row>
        </v-container>
      </v-parallax>
    </section>

    <!-- 品牌使命部分 -->
    <section class="mission-section py-12">
      <v-container>
        <v-row justify="center">
          <v-col cols="12" md="8" class="text-center">
            <h2 class="section-title text-center mb-8">Our Mission</h2>
            <p class="mission-text mb-8">
              We are committed to building a platform that connects coffee growers and consumers, ensuring that every cup of coffee is not just a beverage,
              but a force that can change lives, protect the environment, and promote community development.
            </p>
            <div class="value-chips d-flex flex-wrap justify-center">
              <v-chip
                class="ma-2 value-chip"
                color="primary"
                outlined
                large
              >
                <v-icon left>mdi-handshake</v-icon>
                Fair Trade
              </v-chip>
              <v-chip
                class="ma-2 value-chip"
                color="primary"
                outlined
                large
              >
                <v-icon left>mdi-leaf</v-icon>
                Environmental Sustainability
              </v-chip>
              <v-chip
                class="ma-2 value-chip"
                color="primary"
                outlined
                large
              >
                <v-icon left>mdi-home-group</v-icon>
                Community Support
              </v-chip>
              <v-chip
                class="ma-2 value-chip"
                color="primary"
                outlined
                large
              >
                <v-icon left>mdi-cup</v-icon>
                Quality Assurance
              </v-chip>
            </div>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <!-- 星巴克合作部分 -->
    <section class="starbucks-section py-12">
      <v-container>
        <v-row align="center">
          <v-col cols="12" md="6" class="pr-md-10">
            <div class="content-card">
              <transition name="fade-up-left" appear>
                <div>
                  <v-img
                    :src="require('@/assets/starbucks_logo.png')"
                    alt="Starbucks"
                    contain
                    height="80"
                    class="mb-6"
                  ></v-img>
                  <h2 class="text-h4 font-weight-bold mb-6 primary--text">Starbucks Sustainability Partnership</h2>
                  <p class="text-body-1 mb-4">
                    Starbucks is committed to growing and purchasing high-quality coffee in a responsible way while improving the lives of coffee farmers and their communities.
                  </p>
                  <p class="text-body-1 mb-4">
                    We partner with Starbucks to advance sustainable development in the coffee industry. Through collaboration with coffee farmers, customers, and environmental organizations,
                    we strive to support coffee farmers and their communities while protecting the environment.
                  </p>
                  <p class="text-body-1 mb-6">
                    Over the years, we have established a comprehensive approach to sustainability, including ethical sourcing practices, environmental protection measures, and community investment projects.
                  </p>
                  <v-btn
                    outlined
                    color="primary"
                    class="learn-more-btn"
                    href="https://about.starbucks.com/sustainability/"
                    target="_blank"
                  >
                    Learn More
                    <v-icon right>mdi-arrow-right</v-icon>
                  </v-btn>
                </div>
              </transition>
            </div>
          </v-col>
          <v-col cols="12" md="6">
            <transition name="fade-up-right" appear>
              <v-card class="overflow-hidden card-hover" elevation="8">
                <v-img
                  :src="require('@/assets/pic/starbucks_main.png')"
                  height="400"
                  class="starbucks-image"
                >
                  <div class="image-overlay d-flex flex-column justify-end pa-6">
                    <div class="fact-chip mb-2">
                      <span class="font-weight-bold">99%</span> Ethically Sourced Coffee
                    </div>
                    <div class="fact-chip mb-2">
                      <span class="font-weight-bold">400,000+</span> Partner Coffee Farmers
                    </div>
                    <div class="fact-chip">
                      <span class="font-weight-bold">30 Years</span> of Sustainable Development
                    </div>
                  </div>
                </v-img>
              </v-card>
            </transition>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <!-- OriginBoost介绍部分 -->
    <section class="ob-section py-12">
      <v-container>
        <v-row align="center">
          <v-col cols="12" md="6" order="2" order-md="1">
            <transition name="fade-up-left" appear>
              <v-card class="overflow-hidden card-hover" elevation="8">
                <v-img
                  :src="require('@/assets/pic/team.jpg')"
                  height="400"
                  class="ob-image"
                >
                  <div class="image-overlay d-flex flex-column justify-end pa-6">
                    <div class="fact-chip mb-2">
                      <span class="font-weight-bold">Origin</span> Agricultural Origin
                    </div>
                    <div class="fact-chip mb-2">
                      <span class="font-weight-bold">Boost</span> Enhancement and Promotion
                    </div>
                    <div class="fact-chip">
                      <span class="font-weight-bold">OB</span> Creating Benefits for Origin
                    </div>
                  </div>
                </v-img>
              </v-card>
            </transition>
          </v-col>
          <v-col cols="12" md="6" order="1" order-md="2" class="pl-md-10">
            <div class="content-card">
              <transition name="fade-up-right" appear>
                <div>
                  <div class="ob-logo mb-6 d-flex justify-center">
                    <v-img
                      :src="require('@/assets/pic/OB.png')"
                      alt="OB Logo"
                      contain
                      height="80"
                      class="mb-6"
                    ></v-img>
                    <!--<span class="text-h3 font-weight-black">OB</span> -->
                    <!--<span class="text-subtitle-1 ml-2">OriginBoost</span>-->
                  </div>
                  <h2 class="text-h4 font-weight-bold mb-6 secondary--text">OriginBoost Technology</h2>
                  <p class="text-body-1 mb-4">
                    <span class="font-weight-bold">Origin</span> represents agricultural origin, emphasizing the core position of farmers and farms.
                    <span class="font-weight-bold">Boost</span> represents enhancement and promotion, reflecting the goal of creating benefits and improving treatment for origin.
                  </p>
                  <p class="text-body-1 mb-4">
                    Our mission is to create benefits for origin (Origin Benefits), by directly connecting consumers with coffee growers,
                    eliminating intermediaries, ensuring coffee farmers receive fair compensation, while providing high-quality coffee products.
                  </p>
                  <p class="text-body-1 mb-6">
                    The OB platform provides a comprehensive traceability system, allowing consumers to understand the story behind every cup of coffee they purchase, the cultivation methods, and the impact on local communities.
                    We believe transparency is key to building consumer trust and promoting sustainable development.
                  </p>
                  <v-btn
                    outlined
                    color="secondary"
                    class="learn-more-btn"
                  >
                    Contact Us
                    <v-icon right>mdi-arrow-right</v-icon>
                  </v-btn>
                </div>
              </transition>
            </div>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <!-- 团队介绍部分 -->
    <section class="team-section py-12 cream-bg">
      <v-container>
        <v-row justify="center">
          <v-col cols="12" class="text-center mb-10">
            <h2 class="section-title text-center">Our Team</h2>
            <p class="section-subtitle">
              Professionals from different fields working together for sustainable coffee industry
            </p>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" sm="6" md="4" v-for="(member, i) in teamMembers" :key="i">
            <transition
              name="fade-up"
              appear
              :style="{ transitionDelay: `${i * 0.1}s` }"
            >
              <v-card class="team-card mx-auto" max-width="320">
                <v-img
                  :src="member.photo"
                  height="280"
                  class="team-photo"
                ></v-img>
                <v-card-title class="font-weight-bold">{{ member.name }}</v-card-title>
                <v-card-subtitle>{{ member.role }}</v-card-subtitle>
                <v-card-text class="pb-0">
                  <p>{{ member.description }}</p>
                </v-card-text>
                <v-card-actions class="justify-center pb-4">
                  <v-btn icon small class="mx-1">
                    <v-icon>mdi-email</v-icon>
                  </v-btn>
                  <v-btn icon small class="mx-1">
                    <v-icon>mdi-linkedin</v-icon>
                  </v-btn>
                  <v-btn icon small class="mx-1">
                    <v-icon>mdi-twitter</v-icon>
                  </v-btn>
                </v-card-actions>
              </v-card>
            </transition>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <!-- 联系表单区域 -->
    <section class="contact-section py-12">
      <v-container>
        <v-row justify="center">
          <v-col cols="12" md="8">
            <v-card class="contact-card pa-6">
              <h2 class="text-h4 font-weight-bold mb-6 text-center">Contact Us</h2>
              <v-form>
                <v-row>
                  <v-col cols="12" sm="6">
                    <v-text-field
                      label="Your Name"
                      outlined
                      dense
                      hide-details="auto"
                      class="mb-4"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6">
                    <v-text-field
                      label="Email"
                      outlined
                      dense
                      hide-details="auto"
                      class="mb-4"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12">
                    <v-text-field
                      label="Subject"
                      outlined
                      dense
                      hide-details="auto"
                      class="mb-4"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12">
                    <v-textarea
                      label="Message"
                      outlined
                      hide-details="auto"
                      rows="5"
                      class="mb-6"
                    ></v-textarea>
                  </v-col>
                  <v-col cols="12" class="text-center">
                    <v-btn
                      color="primary"
                      large
                      class="px-8"
                    >
                      Send Message
                    </v-btn>
                  </v-col>
                </v-row>
              </v-form>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </section>
  </div>
</template>

<script>
export default {
  name: 'About',
  data: () => ({
    teamMembers: [
      {
        name: 'Bohan Zhang',
        role: 'Founder and CEO',
        description: 'With 15 years of experience in the coffee industry, dedicated to promoting sustainable coffee cultivation and fair trade.',
        photo: require('@/assets/pic/张博涵.jpg')
      },
      {
        name: 'Yunhan Gao',
        role: 'Chief Operating Officer',
        description: 'Focused on agricultural supply chain management, with extensive experience in connecting farmers with markets.',
        photo: require('@/assets/pic/高云瀚.jpg')
      },
      {
        name: 'Le Liu',
        role: 'Director of Sustainable Development',
        description: 'PhD in Environmental Science, responsible for designing and implementing our sustainable development strategies and projects.',
        photo: require('@/assets/pic/刘乐.jpg')
      },
      {
        name: 'Ziheng Wang',
        role: 'Head of Technology',
        description: 'Expert in blockchain and traceability systems, leading our efforts to create transparent supply chains for coffee production.',
        photo: require('@/assets/pic/王子恒.jpg')
      },
      {
        name: 'Sicheng Yi',
        role: 'Community Manager',
        description: 'Specializes in building relationships with coffee farming communities and implementing education programs for sustainable practices.',
        photo: require('@/assets/pic/ysc.jpg')
      }
    ]
  })
}
</script>

<style scoped>
.about-page {
  overflow-x: hidden;
}

/* 页面标题区域 */
.page-header {
  position: relative;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1;
}

.animated-title {
  position: relative;
  animation: fadeInDown 1.2s ease-out;
}

.subtitle-line {
  width: 80px;
  height: 4px;
  background-color: white;
  margin: 0 auto;
  animation: expandWidth 1.5s ease-out;
}

.subtitle-text {
  animation: fadeInUp 1.2s ease-out;
}

@keyframes fadeInDown {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes expandWidth {
  from { width: 0; }
  to { width: 80px; }
}

/* 使命部分 */
.mission-section {
  background-color: #fff;
}

.mission-text {
  font-size: 1.25rem;
  line-height: 1.8;
  max-width: 800px;
  margin: 0 auto;
}

.value-chip {
  font-weight: 500;
  font-size: 1rem;
  padding: 0 16px;
  height: 40px;
  transition: all 0.3s ease;
}

.value-chip:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 8px rgba(0, 112, 74, 0.2);
}

/* 星巴克部分 */
.starbucks-section {
  background-color: #fafafa;
}

.content-card {
  padding: 20px;
  height: 100%;
}

.starbucks-image {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.5s ease;
}

.starbucks-image:hover {
  transform: scale(1.03);
}

.image-overlay {
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent 70%);
  height: 100%;
}

.fact-chip {
  background: rgba(255, 255, 255, 0.9);
  color: #333;
  padding: 6px 12px;
  border-radius: 20px;
  display: inline-block;
  font-size: 0.9rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-right: 8px;
}

.learn-more-btn {
  transition: all 0.3s ease;
}

.learn-more-btn:hover {
  transform: translateX(5px);
}

/* OB部分 */
.ob-section {
  background-color: #fff;
}

.ob-logo {
  display: inline-flex;
  align-items: center;
  color: var(--secondary-color);
}

.ob-image {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.5s ease;
}

.ob-image:hover {
  transform: scale(1.03);
}

/* 团队部分 */
.cream-bg {
  background-color: #f8f5f0;
  position: relative;
}

.section-subtitle {
  font-size: 1.2rem;
  max-width: 700px;
  margin: 1rem auto 0;
  color: var(--text-light);
}

.team-card {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  height: 100%;
}

.team-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1) !important;
}

.team-photo {
  transition: all 0.5s ease;
}

.team-card:hover .team-photo {
  transform: scale(1.1);
}

/* 联系部分 */
.contact-section {
  background-color: #f5f5f5;
}

.contact-card {
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05) !important;
}

/* 动画效果 */
.fade-up-left-enter-active, .fade-up-right-enter-active, .fade-up-enter-active {
  transition: all 1s ease;
}

.fade-up-left-enter {
  opacity: 0;
  transform: translate(-30px, 30px);
}

.fade-up-right-enter {
  opacity: 0;
  transform: translate(30px, 30px);
}

.fade-up-enter {
  opacity: 0;
  transform: translateY(30px);
}

/* 响应式调整 */
@media (max-width: 960px) {
  .mission-text {
    font-size: 1.1rem;
  }

  .content-card {
    padding: 20px 0;
  }
}

@media (max-width: 600px) {
  .animated-title {
    font-size: 2rem !important;
  }

  .mission-text {
    font-size: 1rem;
  }

  .value-chip {
    font-size: 0.85rem;
    margin: 4px !important;
  }
}
</style>
