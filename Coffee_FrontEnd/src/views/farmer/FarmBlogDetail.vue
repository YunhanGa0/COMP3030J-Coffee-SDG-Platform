<template>
  <v-container class="blog-detail py-8">
    <!-- 加载状态 -->
    <v-skeleton-loader
      v-if="loading"
      type="article"
      class="mx-auto"
    ></v-skeleton-loader>

    <!-- 博客内容 -->
    <template v-else>
      <!-- 博客头部 -->
      <v-row justify="center">
        <v-col cols="12" md="10" lg="8">
          <transition name="fade" appear>
            <div class="blog-header mb-8">
              <h1 class="text-h2 font-weight-bold mb-5 blog-title">{{ blog.title }}</h1>
              <div class="d-flex align-center mb-4">
                <v-avatar color="primary" size="36" class="mr-3">
                  <v-icon dark>mdi-coffee</v-icon>
                </v-avatar>
                <div>
                  <div class="blog-info d-flex align-center flex-wrap">
                    <span class="text-body-2 grey--text text--darken-1">
                      Farm: {{ blog.farm ? blog.farm.farmName : 'Unknown' }}
                    </span>
                    <v-divider vertical class="mx-3"></v-divider>
                    <span class="text-body-2 grey--text text--darken-1">
                      Published on {{ formatDate(blog.createdAt) }}
                    </span>
                    <v-divider vertical class="mx-3" v-if="blog.updatedAt && blog.updatedAt !== blog.createdAt"></v-divider>
                    <span class="text-body-2 grey--text" v-if="blog.updatedAt && blog.updatedAt !== blog.createdAt">
                      Updated on {{ formatDate(blog.updatedAt) }}
                    </span>
                  </div>
                </div>
              </div>
              <div v-if="blog.published" class="published-badge mb-2">
                <v-chip color="success" small>
                  <v-icon left small>mdi-check-circle</v-icon>
                  Published
                </v-chip>
              </div>
              <div v-else class="draft-badge mb-2">
                <v-chip color="grey" small>
                  <v-icon left small>mdi-pencil</v-icon>
                  Draft
                </v-chip>
              </div>
            </div>
          </transition>

          <!-- 博客封面图 -->
          <transition name="fade" appear>
            <v-img
              v-if="blog.coverImageUrl"
              :src="blog.coverImageUrl"
              max-height="500"
              class="blog-cover rounded-lg mb-8"
              gradient="to top, rgba(0,0,0,.4), rgba(0,0,0,0) 50%"
            >
              <div class="image-credit pa-3">
                <v-icon small color="white" class="mr-1">mdi-camera</v-icon>
                <span class="text-caption white--text">{{ blog.farm ? blog.farm.farmName : 'Coffee Farm' }}</span>
              </div>
            </v-img>
          </transition>

          <!-- 博客摘要 -->
          <transition name="fade" appear>
            <v-card class="blog-summary mb-8 elevation-1" v-if="blog.summary">
              <v-card-text class="pa-5">
                <v-icon color="primary" class="mr-2">mdi-format-quote-open</v-icon>
                <span class="text-body-1 font-italic">{{ blog.summary }}</span>
                <v-icon color="primary" class="ml-2">mdi-format-quote-close</v-icon>
              </v-card-text>
            </v-card>
          </transition>

          <!-- 博客内容 -->
          <transition name="fade" appear>
            <div class="blog-content mb-8">
              <div v-html="blog.content" class="text-body-1"></div>
            </div>
          </transition>

          <!-- 博客底部信息 -->
          <transition name="fade" appear>
            <div>
              <v-divider class="mb-6"></v-divider>
              <div class="blog-footer d-flex align-center justify-space-between flex-wrap pb-8">
                <v-btn
                  color="primary"
                  outlined
                  class="action-btn"
                  @click="$router.push(`/farms/${farmId}`)"
                >
                  <v-icon left>mdi-arrow-left</v-icon>
                  Back to Farm
                </v-btn>
                
                <div class="d-flex mt-3 mt-sm-0">
                  <v-btn icon color="primary" class="mr-2">
                    <v-icon>mdi-share-variant</v-icon>
                  </v-btn>
                  <v-btn icon color="primary">
                    <v-icon>mdi-bookmark-outline</v-icon>
                  </v-btn>
                </div>
              </div>
            </div>
          </transition>
          
          <!-- 农场信息 -->
          <transition name="fade" appear>
            <v-card class="farm-card mt-8 mb-8" v-if="blog.farm">
              <v-card-title class="primary--text">
                <v-icon color="primary" class="mr-2">mdi-home-variant</v-icon>
                About {{ blog.farm.farmName }}
              </v-card-title>
              <v-divider></v-divider>
              <v-card-text>
                <v-row align="center">
                  <v-col cols="12" md="4" class="text-center">
                    <v-avatar size="120" color="grey lighten-4" class="mb-3">
                      <v-img
                        :src="farmImageUrl"
                        alt="Farm"
                      ></v-img>
                    </v-avatar>
                  </v-col>
                  <v-col cols="12" md="8">
                    <p class="text-body-1 mb-4">
                      {{ getFarmDescription() }}
                    </p>
                    <v-btn
                      color="primary"
                      text
                      :to="`/farms/${farmId}`"
                    >
                      Visit Farm Page
                      <v-icon right>mdi-arrow-right</v-icon>
                    </v-btn>
                  </v-col>
                </v-row>
              </v-card-text>
            </v-card>
          </transition>
          
          <!-- 其他农场博客推荐 -->
          <transition name="fade" appear>
            <div class="related-blogs mt-8 pt-4" v-if="relatedBlogs.length > 0">
              <h3 class="text-h5 font-weight-bold mb-5">More from this Farm</h3>
              <v-row>
                <v-col cols="12" sm="6" v-for="relatedBlog in relatedBlogs" :key="relatedBlog.id">
                  <v-card 
                    class="card-hover" 
                    outlined
                    :to="`/farms/${farmId}/blogs/${relatedBlog.id}`"
                  >
                    <div class="d-flex">
                      <v-img
                        :src="relatedBlog.coverImageUrl || require('@/assets/pic/plantation.jpg')"
                        height="120"
                        width="120"
                        class="flex-shrink-0"
                      ></v-img>
                      <v-card-text class="py-2">
                        <div class="text-subtitle-1 font-weight-bold">{{ relatedBlog.title }}</div>
                        <div class="text-caption grey--text mt-1">{{ formatDate(relatedBlog.createdAt) }}</div>
                      </v-card-text>
                    </div>
                  </v-card>
                </v-col>
              </v-row>
            </div>
          </transition>
        </v-col>
      </v-row>
    </template>

    <!-- 错误提示 -->
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
  </v-container>
</template>

<script>
import axios from 'axios'

export default {
  name: 'FarmBlogDetail',
  data: () => ({
    blog: {},
    relatedBlogs: [],
    loading: true,
    farmId: null,
    blogId: null,
    farmImageUrl: require('@/assets/pic/plantation.jpg'),
    snackbar: {
      show: false,
      text: '',
      color: 'error'
    }
  }),
  created() {
    this.farmId = this.$route.params.farmId
    this.blogId = this.$route.params.blogId
    this.fetchBlogDetail()
  },
  methods: {
    async fetchBlogDetail() {
      try {
        const response = await axios.get(`/api/farms/${this.farmId}/blogs/${this.blogId}`)
        
        if (response.data.code === 200) {
          this.blog = response.data.data
          document.title = `${this.blog.title} - Coffee SDG Platform`
          
          if (this.blog.farm && this.blog.farm.id) {
            this.fetchFarmDetail(this.blog.farm.id)
            this.fetchRelatedBlogs(this.blog.farm.id)
          }
        } else {
          this.showMessage(response.data.message || 'Failed to get blog detail', 'error')
        }
      } catch (error) {
        console.error('Failed to get blog detail:', error)
        this.showMessage('Failed to get blog detail', 'error')
      } finally {
        this.loading = false
      }
    },
    
    async fetchFarmDetail(farmId) {
      try {
        const response = await axios.get(`/api/farms/${farmId}`)
        
        if (response.data.code === 200) {
          const farm = response.data.data
          this.blog.farm = {
            ...this.blog.farm,
            ...farm
          }
          if (farm.imageUrl) {
            this.farmImageUrl = farm.imageUrl
          }
        }
      } catch (error) {
        console.error('Failed to get farm details:', error)
      }
    },
    
    async fetchRelatedBlogs(farmId) {
      try {
        const response = await axios.get(`/api/farms/${farmId}/blogs?page=0&size=4&published=true`)
        
        if (response.data.code === 200) {
          // Filter out current blog and limit to 2 blogs
          this.relatedBlogs = response.data.data.content
            .filter(blog => blog.id !== parseInt(this.blogId))
            .slice(0, 2)
        }
      } catch (error) {
        console.error('Failed to get related blogs:', error)
      }
    },
    
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
    },
    
    getFarmDescription() {
      if (this.blog.farm && this.blog.farm.description) {
        if (this.blog.farm.description.length > 200) {
          return this.blog.farm.description.substring(0, 200) + '...'
        }
        return this.blog.farm.description
      }
      return `Visit our farm page to learn more about ${this.blog.farm ? this.blog.farm.farmName : 'our farm'} and our sustainable coffee growing practices.`
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
.blog-detail {
  min-height: 100vh;
  background-color: #fff;
  padding-bottom: 100px;
}

.blog-title {
  line-height: 1.3;
  background: linear-gradient(to right, var(--primary-color), #408e6c);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  display: inline-block;
}

.blog-info {
  font-size: 0.9rem;
}

.blog-cover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  position: relative;
}

.image-credit {
  position: absolute;
  bottom: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.5);
  border-top-left-radius: 8px;
}

.blog-summary {
  background-color: #f5f8f7;
  border-left: 4px solid var(--primary-color);
  border-radius: 0 8px 8px 0;
}

.blog-content {
  line-height: 1.8;
  font-size: 1.1rem;
  color: #333;
}

.blog-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 28px 0;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.blog-content :deep(h1), 
.blog-content :deep(h2), 
.blog-content :deep(h3), 
.blog-content :deep(h4) {
  margin-top: 1.5em;
  margin-bottom: 0.7em;
  color: var(--primary-color);
}

.blog-content :deep(blockquote) {
  border-left: 4px solid var(--primary-color);
  padding-left: 1em;
  color: #666;
  font-style: italic;
  margin: 1em 0;
}

.blog-content :deep(ul), 
.blog-content :deep(ol) {
  padding-left: 2em;
  margin: 1em 0;
}

.farm-card {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.card-hover {
  transition: all 0.3s ease;
}

.card-hover:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1) !important;
}

/* Fade Animation */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}
</style> 