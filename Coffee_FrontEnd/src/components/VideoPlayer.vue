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
                  The video cannot be loaded, please check if the URL is correct
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
                Watch on Bilibili
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
            Return
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
          url: 'https://www.bilibili.com/video/BV1YjmnYzEu3/',
          title: 'Coffee winter planting techniques',
          description: 'This video details how to grow excellent quality coffee in the winter.'
        },
        {
          url: 'https://www.bilibili.com/video/BV16i4y1c7Vq/',
          title: 'Basic coffee knowledge',
          description: 'Some basic coffee primer for farms that are new to coffee farming.'
        },
        {
          url: 'https://www.bilibili.com/video/BV1bZ4y1z73S/',
          title: 'Introduction to coffee bean varieties',
          description: 'Introducing common coffee varieties and their corresponding flavors.'
        },
        {
          url: 'https://www.bilibili.com/video/BV11S4y1Q7ym/',
          title: 'Coffee tree pruning techniques',
          description: 'Professional teaching of coffee tree pruning techniques to help you improve your coffee yield and quality.'
        },
        {
          url: 'https://www.bilibili.com/video/BV1p642137fk/',
          title: 'Coffee picking and processing',
          description: 'Explain the timing of coffee beans and the post-processing process to ensure the quality of the coffee.'
        },
        {
          url: 'https://www.bilibili.com/video/BV14S4y197dD/',
          title: 'Coffee grounds to make fertilizer tutorial',
          description: 'Explore sustainable practices in coffee processing and explain how coffee residues can be used to make fertilizer.'
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
