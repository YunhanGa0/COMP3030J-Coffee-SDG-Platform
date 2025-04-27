<template>
  <div class="farm-detail">
    <!-- 加载状态 -->
    <v-skeleton-loader
      v-if="loading"
      type="article"
      class="mx-auto"
    ></v-skeleton-loader>

    <!-- 农庄内容 -->
    <template v-else>
      <!-- 农庄头部 - 英雄区域 -->
      <section class="hero">
        <div class="hero-inner">
          <h1>{{ farm.farmName }}</h1>
          <div class="hero-meta">
            <span>Country: {{ farm.country }}</span>
            <span>Altitude: {{ farm.elevation }}m</span>
            <span>Established: {{ farm.establishedYear }}</span>
            <span v-if="farm.isCertificated" class="badge-cert">
              <v-icon small class="mr-1">mdi-check-circle</v-icon>
              Certified Farm
            </span>
          </div>
        </div>
      </section>

      <!-- 主要内容 -->
      <main class="container farm-layout">
        <!-- 左侧 - 农庄详情卡片 -->
        <aside class="farm-card">
          <h3>Farm Details</h3>
          <div class="farm-photo">
            <v-img :src="farm.imageUrl || require('@/assets/pic/plantation.jpg')" alt="Farm Image" height=200px></v-img>
          </div>
          <div class="detail-grid">
            <div><span>Size</span>{{ farm.size }} hectares</div>
            <div><span>Location</span>{{ farm.location }}</div>
            <div><span>Soil Type</span>{{ formatSoilType(farm.soilType) }}</div>
            <div><span>Country</span>{{ farm.country }}</div>
            <div><span>Owner</span>{{ farm.user ? farm.user.username : 'Unknown' }}</div>
            <div><span>Established</span>{{ farm.establishedYear }}</div>
          </div>
          <p class="about"><strong>About this Farm:</strong> {{ farm.description }}</p>
        </aside>

        <!-- 右侧内容区域 -->
        <section class="right-section">
          <!-- 农庄咖啡豆展示区域 -->
          <section class="coffee-beans-section">
            <h2 >Featured Coffee</h2>
            <v-row dense>
              <v-col
                v-for="bean in coffeeBeans"
                :key="bean.id"
                cols="4"
                sm="6"
                md="4">
                <v-card class="coffee-bean-card" outlined>
                  <v-img :src="bean.imageUrl || require('@/assets/pic/farm.jpg')" height="150px"></v-img>

                  <div class="bean-content">
                    <div class="bean-title">{{ bean.name }}</div>
                    <div class="bean-subtitle">{{ bean.variety }} | {{ bean.processMethod }}</div>
                    <div class="bean-info">
                      <p class="bean-flavor">Flavor: {{ bean.flavorNotes }}</p>
                      <p class="bean-stock-price">
                        <span>Stock: {{ bean.bagStock }} bags</span>
                        <span class="bean-price">${{ bean.pricePerBag }}</span>
                      </p>
                    </div>
                  </div>
                  <v-card-actions class="bean-actions">

                    <v-btn block size="large"
                           class="bean-buy-btn"
                           color="green-500"
                    elevated
                    @click="$router.push(`/coffee-beans/${bean.id}`)">
                    Buy&nbsp;Now
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-col>
            </v-row>
          </section>

          <v-divider class="my-8"></v-divider> <!-- 👈 分隔线 -->


          <!-- 👇 农庄博客区域 -->
          <section class="blogs-section">
            <h2 >Farm Blogs</h2>

            <div v-if="blogs.length === 0" class="text-center py-8 empty-state">
              <v-icon size="48" color="grey">mdi-text-box-outline</v-icon>
              <p class="mt-4 grey--text">No blogs available for this farm yet.</p>
            </div>

            <div class="blog-list" v-else>
              <article
                v-for="blog in blogs.filter(b => b.published)"
                :key="blog.id"
                class="blog-card"
                @click="$router.push(`/farms/${farmId}/blogs/${blog.id}`)"
              >
                <h3 class="blog-title">{{ blog.title }}</h3>
                <p class="blog-excerpt">{{ blog.summary }}</p>
                <div class="blog-footer">
                  <time :datetime="formatDate(blog.createdAt)">{{ formatDate(blog.createdAt) }}</time>
                  <v-btn
                    color="green-700"
                    class="read-more-btn"
                    @click="$router.push(`/farms/${farmId}/blogs/${blog.id}`)"
                  >
                    Read&nbsp;More
                    <v-icon end>mdi-arrow-right</v-icon>
                  </v-btn>
                </div>
              </article>
            </div>

            <div class="text-center mt-8" v-if="pagination.totalPages > 1">
              <v-pagination
                v-model="currentPage"
                :length="pagination.totalPages"
                :total-visible="7"
                @input="handlePageChange"
                color="var(--green-700)"
              ></v-pagination>
            </div>
          </section>
        </section>
      </main>

      <!-- 返回按钮 -->
      <div class="back-link">
        <a @click="$router.push('/plantation')">BACK TO PLANTATIONS</a>
      </div>
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
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'FarmDetail',
  data: () => ({
    farm: {},
    blogs: [],
    coffeeBeans: [],
    loading: true,
    farmId: null,
    currentPage: 1,
    pageSize: 5,
    publishedFilter: null,
    publishedOptions: [
      { text: 'All Blogs', value: null },
      { text: 'Published Only', value: true },
      { text: 'Drafts Only', value: false }
    ],
    pagination: {
      totalPages: 0,
      totalElements: 0,
      first: true,
      last: false,
      empty: true
    },
    snackbar: {
      show: false,
      text: '',
      color: 'error'
    }
  }),
  created() {
    this.farmId = this.$route.params.id
    this.fetchFarmDetail()
    this.fetchCoffeeBeans()
    // 添加设计变量到文档根
    document.documentElement.style.setProperty('--green-900', '#114232')
    document.documentElement.style.setProperty('--green-700', '#19624c')
    document.documentElement.style.setProperty('--green-500', '#2f8f5b')
    document.documentElement.style.setProperty('--sand-50', '#faf9f6')
    document.documentElement.style.setProperty('--sand-100', '#efece6')
    document.documentElement.style.setProperty('--sand-300', '#d7d2c8')
    document.documentElement.style.setProperty('--text-main', '#1f1f1f')
    document.documentElement.style.setProperty('--radius-lg', '18px')
    document.documentElement.style.setProperty('--radius-md', '12px')
    document.documentElement.style.setProperty('--shadow-sm', '0 4px 12px rgba(0, 0, 0, .06)')
  },
  methods: {
    async fetchFarmDetail() {
      try {
        const response = await axios.get(`/api/farms/${this.farmId}`)

        if (response.data.code === 200) {
          this.farm = response.data.data
          document.title = `${this.farm.farmName} - Coffee SDG Platform`
          this.fetchFarmBlogs()
        } else {
          this.showMessage(response.data.message || 'Failed to get farm details', 'error')
        }
      } catch (error) {
        console.error('Failed to get farm details:', error)
        this.showMessage('Failed to get farm details', 'error')
      } finally {
        this.loading = false
      }
    },
    async fetchCoffeeBeans() {
      try {
        const response = await axios.get(`/api/${this.farmId}/coffee-beans`)
        if (response.data.code === 200) {
          this.coffeeBeans = response.data.data
        }
      } catch (error) {
        console.error('Failed to fetch coffee beans:', error)
      }
    },

    async fetchFarmBlogs() {
      try {
        let url = `/api/farms/${this.farmId}/blogs?page=${this.currentPage - 1}&size=${this.pageSize}`

        if (this.publishedFilter !== null) {
          url += `&published=${this.publishedFilter}`
        }

        const response = await axios.get(url)

        if (response.data.code === 200) {
          const data = response.data.data
          this.blogs = data.content
          this.pagination = {
            totalPages: data.totalPages,
            totalElements: data.totalElements,
            first: data.first,
            last: data.last,
            empty: data.empty
          }
        } else {
          this.showMessage(response.data.message || 'Failed to get farm blogs', 'error')
        }
      } catch (error) {
        console.error('Failed to get farm blogs:', error)
        this.showMessage('Failed to get farm blogs', 'error')
      }
    },

    handlePageChange(page) {
      this.currentPage = page
      this.fetchFarmBlogs()
    },

    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
    },

    formatSoilType(soilType) {
      if (!soilType) return 'Unknown'
      return soilType.replace('_', ' ').toLowerCase()
        .split(' ')
        .map(word => word.charAt(0).toUpperCase() + word.slice(1))
        .join(' ')
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
/* ----------  Reset  ---------- */
*{box-sizing:border-box;}

/* ----------  Hero Section (Farm Name)  ---------- */
.hero{
  background:linear-gradient(135deg, var(--green-700) 0%, var(--green-500) 100%);
  color:#F9FFF9;
  padding:2rem 1rem 1rem;
}
.hero-inner{max-width:800px;margin:auto;display:flex;flex-direction:column;gap:1.25rem}
.hero h1{font-size:2.75rem;font-weight:700;line-height:1.2}
.hero-meta{display:flex;flex-wrap:wrap;gap:1rem;font-size:.95rem;font-weight:300;opacity:.95}
.badge-cert{background:#fff;color:var(--green-700);padding:.2rem .8rem;border-radius:9999px;font-size:.75rem;font-weight:600;display:inline-flex;align-items:center;}

/* ----------  Layout ---------- */
.container{max-width:1200px;margin:auto;padding:2rem 1rem;display:grid;grid-template-columns:280px 1fr;gap:2rem}
.container.farm-layout {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1rem;
}
@media(max-width:900px){.container{grid-template-columns:1fr}}

/* ----------  Farm Details Card ---------- */
/* 左边农庄卡片 */
.farm-card {
  background: #fff;
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: var(--shadow-sm);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}
/* 农庄照片 */
.farm-photo {
  width: 100%;
  background: var(--sand-100);
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 1rem;
}
.card{background:#fff;border-radius:var(--radius-lg);padding:1rem;box-shadow:var(--shadow-sm);text-align: left}
.card h3{font-size:1.15rem;margin-bottom:1rem;color:var(--green-700)}
.detail-grid{display:grid;grid-template-columns:1fr 1fr;gap:0.75rem;font-size:.9rem}
.detail-grid div span{display:block;font-size:.7rem;color:#666;text-transform:uppercase;letter-spacing:.4px;margin-bottom:.15rem}
.about{margin-top:1.25rem;font-size:.9rem;line-height:1.6;color:#555}

/* ----------  Right Section (Coffee Beans) ---------- */
/* 右侧整体区域 */
.right-section {
  display: flex;
  flex-direction: column;
}

/* ----------  Blog Section ---------- */
.blogs-header{display:flex;justify-content:space-between;align-items:center;margin-bottom:1rem}
.blogs-header h2{font-size:1.5rem;color:var(--green-700)}
.blog-list{display:flex;flex-direction:column;gap:1.25rem}
.blog-card{background:#fff;border-radius:var(--radius-md);padding:1.5rem;box-shadow:var(--shadow-sm);transition:transform .2s; cursor:pointer;}
.blog-card:hover{transform:translateY(-4px)}
.blog-title{font-size:1.15rem;font-weight:600;margin-bottom:.4rem}
.blog-meta{display:flex;align-items:center;gap:.5rem;font-size:.7rem;color:#777;margin-bottom:.6rem}
.blog-meta .badge{background:var(--sand-100);padding:.15rem .55rem;border-radius:9999px;font-weight:600}
.blog-meta .badge.published{background:#e8f5e9;color:#2e7d32;}
.blog-meta .badge.draft{background:#ececec;color:#666;}
.blog-excerpt{font-size:.9rem;color:#555;line-height:1.55;
  max-height:4.5em;overflow:hidden;text-overflow:ellipsis;display:-webkit-box;
  -webkit-line-clamp:3;-webkit-box-orient:vertical;}
.blog-footer a{color:var(--green-700);font-weight:600}
.blog-footer a:hover{color:var(--green-900)}
.read-more-btn{
  margin-left:auto;              /* 靠右 */
  text-transform:none;           /* 保持大小写 */
  font-weight:600;
  padding:0 16px;
}
.blog-footer{
  display:flex;
  justify-content:space-between;
  align-items:center;
  margin-top:1rem;
}
.blog-title{
  font-size:1.15rem;
  font-weight:600;
  margin-bottom:.5rem;
}

.blog-excerpt{
  font-size:.9rem;
  color:#555;
  line-height:1.55;
  max-height:4.5em;
  overflow:hidden;
  text-overflow:ellipsis;
  display:-webkit-box;
  -webkit-line-clamp:3;
  -webkit-box-orient:vertical;
}

/* ----------  Back Link ---------- */
.back-link{text-align:center;margin:3rem 0}
.back-link a{display:inline-block;border:1.5px solid var(--green-700);padding:.6rem 2rem;border-radius:9999px;font-size:.85rem;font-weight:600;color:var(--green-700);transition:.2s;cursor:pointer;}
.back-link a:hover{background:var(--green-700);color:#fff}

/* Empty state */
.empty-state {
  background-color: var(--sand-50);
  border-radius: var(--radius-md);
  padding: 3rem 1rem;
}

.farm-detail {
  background-color: var(--sand-50);
  font-family: "Poppins", sans-serif;
  color: var(--text-main);
  min-height: 100vh;
}


.coffee-bean-card {
  height: 320px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: box-shadow 0.3s ease, transform 0.3s ease;
}

.coffee-bean-card:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
  transform: translateY(-4px);
}

.bean-content {
  padding: 0.5rem 1rem;
  flex-grow: 1;
}

.bean-title {
  font-size: 1rem;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.bean-subtitle {
  font-size: 0.8rem;
  color: #666;
  margin-top: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.bean-info {
  font-size: 0.85rem;
  color: #555;
  margin-top: 6px;
}

.bean-flavor {
  margin: 4px 0;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2; /* flavor 最多两行 */
  -webkit-box-orient: vertical;
}

.bean-stock-price {
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
  margin-top: 4px;
}

.bean-price {
  font-weight: bold;
  color: var(--green-700);
}

.bean-actions {
  padding: 0 12px 20px;
}

.bean-buy-btn {
  /* 文字颜色换深色，提高对比度 */
  --v-theme-on-surface: #114232;     /* green-900 */
  font-weight: 600;
}

/* 如果想让 hover 时更明显 */
.bean-buy-btn:hover {
  background-color: var(--green-700) !important;
  --v-theme-on-surface: #FFFFFF;     /* hover 时文字改白色 */
}


/* Coffee Beans区 */
.coffee-beans-section {
  background: #f9f9f9;
  padding: 1rem 1.5rem;
  border-radius: 12px;
}

/* Blogs区 */
.blogs-section {
  background: #ffffff;
  padding: 1rem 1.5rem;
  border-radius: 12px;
}


</style>
