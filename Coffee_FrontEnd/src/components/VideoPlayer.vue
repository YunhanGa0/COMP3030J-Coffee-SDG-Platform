<template>
  <div class="video-player">
    <v-container>
      <v-row>
        <v-col cols="12">
          <h1 class="text-h4 mb-6 text-center">咖啡种植技术培训视频</h1>
        </v-col>
      </v-row>
      
      <v-row>
        <v-col v-for="video in videoList" :key="video.url" cols="12" md="6" lg="4">
          <v-card class="mx-auto h-100" elevation="3" hover>
            <v-card-title class="text-h6 font-weight-bold">
              {{ video.title }}
            </v-card-title>
            
            <v-card-text>
              <div class="description mb-4">{{ video.description }}</div>
              
              <!-- 视频播放区域 -->
              <div class="video-container">
                <iframe
                  v-if="getProcessedVideoUrl(video.url)"
                  :src="getProcessedVideoUrl(video.url)"
                  frameborder="0"
                  allowfullscreen
                  class="video-iframe"
                ></iframe>
                <div v-else class="error-message">
                  无法加载视频，请检查URL是否正确
                </div>
              </div>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="primary"
                text
                :href="video.url"
                target="_blank"
              >
                在B站观看
                <v-icon right>mdi-open-in-new</v-icon>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>

      <v-row class="mt-6">
        <v-col cols="12" class="text-center">
          <v-btn
            color="primary"
            text
            @click="goBack"
            large
          >
            返回
            <v-icon right>mdi-arrow-left</v-icon>
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {
  name: 'VideoPlayer',
  data() {
    return {
      videoList: [
        {
          url: 'https://www.bilibili.com/video/BV1y9VmzZEGu/',
          title: '咖啡种植基础技术',
          description: '本视频详细介绍了咖啡种植的关键技术要点，包括土壤管理、灌溉技术、以及病虫害防治等内容。'
        },
        {
          url: 'https://www.bilibili.com/video/BV1Ys411K7Rf/',
          title: '有机肥料制作指南',
          description: '学习如何制作和使用有机肥料，提高咖啡种植的可持续性和产品品质。'
        },
        {
          url: 'https://www.bilibili.com/video/BV1zp4y1D7B1/',
          title: '咖啡树修剪技术',
          description: '专业的咖啡树修剪技术教学，帮助您提高咖啡产量和品质。'
        },
        {
          url: 'https://www.bilibili.com/video/BV1Wh411x7K2/',
          title: '病虫害防治方法',
          description: '介绍常见的咖啡树病虫害以及科学的防治方法。'
        },
        {
          url: 'https://www.bilibili.com/video/BV1su411V7dE/',
          title: '咖啡采摘与处理',
          description: '详解咖啡豆的采摘时机和后处理工艺，确保咖啡品质。'
        },
        {
          url: 'https://www.bilibili.com/video/BV1RN411C7VE/',
          title: '可持续种植实践',
          description: '探讨咖啡种植中的可持续发展实践，包括遮阴树种植和生物多样性保护。'
        }
      ]
    }
  },
  methods: {
    getProcessedVideoUrl(url) {
      if (url.includes('bilibili.com')) {
        const bvMatch = url.match(/BV\w+/);
        if (bvMatch) {
          return `//player.bilibili.com/player.html?bvid=${bvMatch[0]}&page=1`;
        }
      }
      return null;
    },
    goBack() {
      this.$router.go(-1);
    }
  }
}
</script>

<style scoped>
.video-player {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.video-container {
  position: relative;
  width: 100%;
  padding-top: 56.25%; /* 16:9 宽高比 */
  background: #f5f5f5;
  margin-bottom: 16px;
}

.video-iframe {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.error-message {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #666;
}

.description {
  color: #666;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.h-100 {
  height: 100%;
}

/* 添加响应式布局样式 */
@media (max-width: 960px) {
  .video-container {
    margin-bottom: 12px;
  }
}

@media (max-width: 600px) {
  .video-player {
    padding: 12px;
  }
}
</style> 