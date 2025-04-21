<template>
  <div class="farmer-dashboard">
    <!-- 顶部横幅 -->
    <section class="hero">
      <div class="hero-inner">
        <h1>农场管理中心</h1>
        <div class="hero-meta">
          <span>农场主: {{ farm.user ? farm.user.username : '加载中...' }}</span>
          <span>农场名称: {{ farm.farmName || '加载中...' }}</span>
          <span v-if="farm.isCertificated" class="badge-cert">
            <v-icon small class="mr-1">mdi-check-circle</v-icon>
            认证农场
          </span>
        </div>
      </div>
    </section>

    <!-- 主体内容 -->
    <main class="container">
      <!-- 侧边栏导航 -->
      <aside class="sidebar">
        <div class="nav-menu">
          <div 
            class="nav-item" 
            :class="{ active: activeTab === 'profile' }"
            @click="activeTab = 'profile'"
          >
            <v-icon size="20" class="mr-2">mdi-account-edit</v-icon>
            农场信息
          </div>
          <div 
            class="nav-item" 
            :class="{ active: activeTab === 'blogs' }"
            @click="activeTab = 'blogs'"
          >
            <v-icon size="20" class="mr-2">mdi-post</v-icon>
            博客管理
          </div>
        </div>
      </aside>

      <!-- 内容区域 -->
      <section class="content-area">
        <!-- 农场信息编辑 -->
        <div v-if="activeTab === 'profile'" class="profile-editor">
          <header class="section-header">
            <h2>农场信息</h2>
            <div class="actions">
              <v-btn 
                color="var(--green-700)" 
                outlined 
                small
                :loading="saveLoading"
                @click="saveProfile"
              >
                保存修改
              </v-btn>
            </div>
          </header>

          <v-card class="form-card">
            <v-card-text>
              <v-form ref="profileForm">
                <v-row>
                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model="farmProfile.farmName"
                      label="农场名称"
                      outlined
                      dense
                      :rules="[v => !!v || '请输入农场名称']"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model="farmProfile.country"
                      label="国家"
                      outlined
                      dense
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model="farmProfile.location"
                      label="地址"
                      outlined
                      dense
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model.number="farmProfile.size"
                      label="面积 (公顷)"
                      outlined
                      dense
                      type="number"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model.number="farmProfile.establishedYear"
                      label="成立年份"
                      outlined
                      dense
                      type="number"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model.number="farmProfile.elevation"
                      label="海拔 (米)"
                      outlined
                      dense
                      type="number"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12">
                    <v-select
                      v-model="farmProfile.soilType"
                      :items="soilTypes"
                      label="土壤类型"
                      outlined
                      dense
                    ></v-select>
                  </v-col>
                  <v-col cols="12">
                    <div class="image-upload-section">
                      <label class="image-label">农场图片</label>
                      <div class="d-flex align-center">
                        <v-img
                          v-if="farmProfile.imageUrl"
                          :src="farmProfile.imageUrl"
                          max-width="200"
                          max-height="120"
                          class="mr-4 rounded"
                        ></v-img>
                        <v-file-input
                          v-model="farmImageFile"
                          accept="image/*"
                          outlined
                          dense
                          hide-details
                          placeholder="选择图片"
                          prepend-icon="mdi-camera"
                          @change="handleImageUpload"
                          class="flex-grow-1"
                        ></v-file-input>
                      </div>
                    </div>
                  </v-col>
                  <v-col cols="12">
                    <v-textarea
                      v-model="farmProfile.description"
                      label="农场描述"
                      outlined
                      auto-grow
                      rows="4"
                    ></v-textarea>
                  </v-col>
                </v-row>
              </v-form>
            </v-card-text>
          </v-card>
        </div>

        <!-- 博客管理 -->
        <div v-if="activeTab === 'blogs'" class="blogs-manager">
          <header class="section-header">
            <h2>博客管理</h2>
            <div class="actions">
              <v-btn 
                color="var(--green-700)" 
                small
                dark
                @click="createNewBlog"
              >
                <v-icon size="18" class="mr-1">mdi-plus</v-icon>
                新建博客
              </v-btn>
            </div>
          </header>

          <!-- 博客列表 -->
          <div v-if="blogs.length === 0" class="text-center py-8 empty-state">
            <v-icon size="48" color="grey">mdi-text-box-outline</v-icon>
            <p class="mt-4 grey--text">暂无农场博客，快来创建第一篇吧！</p>
          </div>

          <div v-else class="blog-list">
            <v-data-table
              :headers="blogHeaders"
              :items="blogs"
              :items-per-page="5"
              class="blog-table"
            >
              <template v-slot:item.title="{ item }">
                <div class="title-cell">{{ item.title }}</div>
              </template>
              
              <template v-slot:item.createdAt="{ item }">
                {{ formatDate(item.createdAt) }}
              </template>
              
              <template v-slot:item.published="{ item }">
                <v-chip
                  small
                  :color="item.published ? 'success' : 'grey'"
                  text-color="white"
                >
                  {{ item.published ? '已发布' : '草稿' }}
                </v-chip>
              </template>
              
              <template v-slot:item.actions="{ item }">
                <div class="actions-cell">
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                        icon
                        x-small
                        color="primary"
                        v-bind="attrs"
                        v-on="on"
                        @click="editBlog(item)"
                      >
                        <v-icon>mdi-pencil</v-icon>
                      </v-btn>
                    </template>
                    <span>编辑</span>
                  </v-tooltip>
                  
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                        icon
                        x-small
                        :color="item.published ? 'orange' : 'success'"
                        v-bind="attrs"
                        v-on="on"
                        @click="togglePublish(item)"
                      >
                        <v-icon>{{ item.published ? 'mdi-eye-off' : 'mdi-eye' }}</v-icon>
                      </v-btn>
                    </template>
                    <span>{{ item.published ? '取消发布' : '发布' }}</span>
                  </v-tooltip>
                  
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                        icon
                        x-small
                        color="error"
                        v-bind="attrs"
                        v-on="on"
                        @click="confirmDelete(item)"
                      >
                        <v-icon>mdi-delete</v-icon>
                      </v-btn>
                    </template>
                    <span>删除</span>
                  </v-tooltip>
                </div>
              </template>
            </v-data-table>
          </div>
        </div>
      </section>
    </main>

    <!-- 博客编辑对话框 -->
    <v-dialog v-model="blogDialog" max-width="900px" persistent>
      <v-card>
        <v-card-title class="headline">
          {{ editMode ? '编辑博客' : '创建新博客' }}
          <v-spacer></v-spacer>
          <v-btn icon @click="closeBlogDialog">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-divider></v-divider>
        <v-card-text class="pt-4">
          <v-form ref="blogForm">
            <v-text-field
              v-model="currentBlog.title"
              label="标题"
              outlined
              :rules="[v => !!v || '请输入博客标题']"
            ></v-text-field>
            
            <v-text-field
              v-model="currentBlog.coverImageUrl"
              label="封面图片链接"
              outlined
              hint="输入有效的图片URL"
            ></v-text-field>
            
            <v-textarea
              v-model="currentBlog.summary"
              label="摘要"
              outlined
              rows="2"
              auto-grow
              counter="150"
              :rules="[v => !!v || '请输入博客摘要']"
            ></v-textarea>
            
            <v-textarea
              v-model="currentBlog.content"
              label="正文内容"
              outlined
              rows="15"
              auto-grow
              :rules="[v => !!v || '请输入博客内容']"
            ></v-textarea>
          </v-form>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions class="pa-4">
          <v-btn 
            text 
            @click="closeBlogDialog"
          >
            取消
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn
            color="grey darken-1"
            text
            :loading="saveLoading"
            @click="saveBlog(false)"
          >
            保存为草稿
          </v-btn>
          <v-btn
            color="success"
            :loading="saveLoading"
            @click="saveBlog(true)"
          >
            {{ editMode ? '更新并发布' : '保存并发布' }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 确认删除对话框 -->
    <v-dialog v-model="deleteDialog" max-width="400">
      <v-card>
        <v-card-title class="headline">确认删除</v-card-title>
        <v-card-text>
          你确定要删除博客 "{{ blogToDelete ? blogToDelete.title : '' }}" 吗？此操作不可撤销。
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="deleteDialog = false">取消</v-btn>
          <v-btn 
            color="error" 
            text 
            :loading="deleteLoading"
            @click="deleteBlog"
          >
            删除
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 提示消息 -->
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

    <initial-setup-dialog
      :show="showInitialSetup"
      @setup-completed="handleSetupCompleted"
      @show-message="showMessage"
    />
  </div>
</template>

<script>
import axios from 'axios'
import InitialSetupDialog from '@/components/InitialSetupDialog.vue'

export default {
  name: 'FarmerDashboard',
  
  components: {
    InitialSetupDialog
  },
  
  data: () => ({
    // 设置初始数据
    activeTab: 'profile',
    farm: {},
    blogs: [],
    showInitialSetup: false,
    
    // 博客表格列
    blogHeaders: [
      { text: '标题', value: 'title', sortable: true },
      { text: '创建时间', value: 'createdAt', sortable: true },
      { text: '状态', value: 'published', sortable: true },
      { text: '操作', value: 'actions', sortable: false, align: 'end' }
    ],
    
    // 编辑对话框
    blogDialog: false,
    editMode: false,
    currentBlog: {
      id: null,
      title: '',
      summary: '',
      content: '',
      coverImageUrl: '',
      published: false
    },
    
    // 删除对话框
    deleteDialog: false,
    blogToDelete: null,
    deleteLoading: false,
    
    // 农场资料编辑
    farmProfile: {
      farmName: '',
      country: '',
      location: '',
      size: null,
      establishedYear: null,
      description: '',
      imageUrl: '',
      elevation: null,
      soilType: ''
    },
    
    // 土壤类型选项
    soilTypes: [
      'LOAMY',
      'CLAY',
      'SANDY',
      'SILTY',
      'CHALKY',
      'PEATY'
    ],
    
    // 加载状态
    saveLoading: false,
    
    // 提示消息
    snackbar: {
      show: false,
      text: '',
      color: 'success'
    },
    farmImageFile: null,
  }),
  
  async created() {
    await this.checkInitialSetup()
    // 设置CSS变量
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
    
    // 获取农场信息
    this.fetchFarmProfile()
    
    // 获取博客列表
    this.fetchBlogs()
  },
  
  methods: {
    async checkInitialSetup() {
      try {
        const response = await axios.get('/api/farms/profile')
        if (!response.data || !response.data.farmName) {
          this.showInitialSetup = true
        }
      } catch (error) {
        console.error('Error checking farm profile:', error)
        this.showInitialSetup = true
      }
    },
    
    // 获取农场信息
    async fetchFarmProfile() {
      try {
        const response = await axios.get('/api/farms/status')
        
        if (response.data.code === 200) {
          const { hasCompletedProfile, ...farmData } = response.data.data;
          this.farm = farmData;
          this.showInitialSetup = !hasCompletedProfile;

          if (hasCompletedProfile) {
            // 如果已完成设置，复制农场数据到编辑表单
            this.farmProfile = {
              farmName: this.farm.farmName,
              country: this.farm.country,
              location: this.farm.location,
              size: this.farm.size,
              establishedYear: this.farm.establishedYear,
              description: this.farm.description,
              imageUrl: this.farm.imageUrl,
              elevation: this.farm.elevation,
              soilType: this.farm.soilType
            }
            
            document.title = `${this.farm.farmName} - 农场管理中心`
          }
        } else {
          this.showMessage(response.data.message || '获取农场信息失败', 'error')
        }
      } catch (error) {
        console.error('获取农场信息失败:', error)
        this.showMessage('获取农场信息失败', 'error')
      }
    },
    
    // 保存农场资料
    async saveProfile() {
      if (!this.$refs.profileForm.validate()) return
      
      this.saveLoading = true
      try {
        let response;
        const profileData = {
          ...this.farmProfile,
          id: this.farm.id
        };

        // 根据是否已有农场信息来决定是创建还是更新
        if (!this.farm.id) {
          response = await axios.post('/api/farms/profile', profileData);
        } else {
          response = await axios.put('/api/farms/update', profileData);
        }
        
        if (response.data.code === 200) {
          this.showMessage(
            !this.farm.id ? '农场信息创建成功' : '农场信息更新成功', 
            'success'
          );
          await this.fetchFarmProfile();
        } else {
          this.showMessage(response.data.message || '保存农场信息失败', 'error');
        }
      } catch (error) {
        console.error('保存农场信息失败:', error);
        this.showMessage('保存农场信息失败', 'error');
      } finally {
        this.saveLoading = false;
      }
    },
    
    // 获取博客列表
    async fetchBlogs() {
      try {
        const response = await axios.get(`/api/farms/${this.farm.id}/blogs`)
        
        if (response.data.code === 200) {
          this.blogs = response.data.data.content || []
        } else {
          this.showMessage(response.data.message || '获取博客列表失败', 'error')
        }
      } catch (error) {
        console.error('获取博客列表失败:', error)
        this.showMessage('获取博客列表失败', 'error')
      }
    },
    
    // 创建新博客
    createNewBlog() {
      this.$router.push({
        path: '/blog-editor',
        query: { farmId: this.farm.id }
      })
    },
    
    // 编辑博客
    editBlog(blog) {
      this.$router.push({
        path: '/blog-editor',
        query: { 
          farmId: this.farm.id,
          blogId: blog.id 
        }
      })
    },
    
    // 关闭博客编辑对话框
    closeBlogDialog() {
      this.blogDialog = false
      setTimeout(() => {
        this.currentBlog = {
          id: null,
          title: '',
          summary: '',
          content: '',
          coverImageUrl: '',
          published: false
        }
      }, 300)
    },
    
    // 保存博客
    async saveBlog(published) {
      if (!this.$refs.blogForm.validate()) return
      
      this.saveLoading = true
      
      try {
        const blogData = {
          title: this.currentBlog.title,
          summary: this.currentBlog.summary,
          content: this.currentBlog.content,
          coverImageUrl: this.currentBlog.coverImageUrl,
          published: published
        }
        
        let response
        
        if (this.editMode) {
          // 更新博客
          response = await axios.put(
            `/api/farms/${this.farm.id}/blogs/${this.currentBlog.id}`, 
            blogData
          )
        } else {
          // 创建博客
          response = await axios.post(
            `/api/farms/${this.farm.id}/blogs`, 
            blogData
          )
        }
        
        if (response.data.code === 200) {
          this.showMessage(
            `博客${this.editMode ? '更新' : '创建'}成功${published ? '并已发布' : ''}`, 
            'success'
          )
          this.blogDialog = false
          this.fetchBlogs()
        } else {
          this.showMessage(response.data.message || `博客${this.editMode ? '更新' : '创建'}失败`, 'error')
        }
      } catch (error) {
        console.error(`博客${this.editMode ? '更新' : '创建'}失败:`, error)
        this.showMessage(`博客${this.editMode ? '更新' : '创建'}失败`, 'error')
      } finally {
        this.saveLoading = false
      }
    },
    
    // 确认删除博客
    confirmDelete(blog) {
      this.blogToDelete = blog
      this.deleteDialog = true
    },
    
    // 删除博客
    async deleteBlog() {
      if (!this.blogToDelete) return
      
      this.deleteLoading = true
      
      try {
        const response = await axios.delete(
          `/api/farms/${this.farm.id}/blogs/${this.blogToDelete.id}`
        )
        
        if (response.data.code === 200) {
          this.showMessage('博客已成功删除', 'success')
          this.deleteDialog = false
          this.fetchBlogs()
        } else {
          this.showMessage(response.data.message || '删除博客失败', 'error')
        }
      } catch (error) {
        console.error('删除博客失败:', error)
        this.showMessage('删除博客失败', 'error')
      } finally {
        this.deleteLoading = false
      }
    },
    
    // 切换博客发布状态
    async togglePublish(blog) {
      try {
        const response = await axios.patch(
          `/api/farms/${this.farm.id}/blogs/${blog.id}/publish`,
          { published: !blog.published }
        )
        
        if (response.data.code === 200) {
          this.showMessage(
            `博客已${blog.published ? '取消发布' : '发布'}`, 
            'success'
          )
          this.fetchBlogs()
        } else {
          this.showMessage(response.data.message || `${blog.published ? '取消发布' : '发布'}失败`, 'error')
        }
      } catch (error) {
        console.error(`${blog.published ? '取消发布' : '发布'}失败:`, error)
        this.showMessage(`${blog.published ? '取消发布' : '发布'}失败`, 'error')
      }
    },
    
    // 显示提示消息
    showMessage(text, color = 'success') {
      this.snackbar = {
        show: true,
        text,
        color
      }
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
    },
    
    // 处理图片上传
    async handleImageUpload(file) {
      if (!file) return;
      
      try {
        const formData = new FormData();
        formData.append('image', file);
        formData.append('type', 'FARM');
        
        const response = await axios.post('/api/articles/images/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        
        if (response.data.code === 200) {
          this.farmProfile.imageUrl = response.data.data.url;
          this.showMessage('图片上传成功', 'success');
        } else {
          throw new Error(response.data.message || '图片上传失败');
        }
      } catch (error) {
        console.error('图片上传失败:', error);
        this.showMessage(error.message || '图片上传失败', 'error');
        this.farmImageFile = null;
      }
    },
    
    handleSetupCompleted() {
      this.showInitialSetup = false
      this.fetchFarmProfile()
    },
  }
}
</script>

<style scoped>
/* 页面基本样式 */
.farmer-dashboard {
  background-color: var(--sand-50);
  font-family: "Poppins", sans-serif;
  color: var(--text-main);
  min-height: 100vh;
}

/* 顶部横幅 */
.hero {
  background: linear-gradient(135deg, var(--green-700) 0%, var(--green-500) 100%);
  color: #fff;
  padding: 3rem 1rem 2.5rem;
}

.hero-inner {
  max-width: 1200px;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.hero h1 {
  font-size: 2.5rem;
  font-weight: 700;
  line-height: 1.2;
}

.hero-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  font-size: 0.95rem;
  font-weight: 300;
  opacity: 0.95;
}

.badge-cert {
  background: #fff;
  color: var(--green-700);
  padding: 0.2rem 0.8rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 600;
  display: inline-flex;
  align-items: center;
}

/* 主要布局 */
.container {
  max-width: 1200px;
  margin: auto;
  padding: 2rem 1rem;
  display: grid;
  grid-template-columns: 220px 1fr;
  gap: 2rem;
}

@media (max-width: 900px) {
  .container {
    grid-template-columns: 1fr;
  }
}

/* 侧边栏 */
.sidebar {
  position: sticky;
  top: 2rem;
  align-self: flex-start;
}

.nav-menu {
  background: #fff;
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.nav-item {
  padding: 1rem 1.5rem;
  font-size: 0.95rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: all 0.2s;
  border-left: 3px solid transparent;
}

.nav-item:hover {
  background-color: var(--sand-50);
}

.nav-item.active {
  background-color: var(--sand-100);
  border-left-color: var(--green-700);
  color: var(--green-700);
}

/* 内容区域 */
.content-area {
  background: #fff;
  border-radius: var(--radius-lg);
  padding: 2rem;
  box-shadow: var(--shadow-sm);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.section-header h2 {
  font-size: 1.5rem;
  color: var(--green-700);
  font-weight: 600;
}

/* 表单卡片 */
.form-card {
  border-radius: var(--radius-md);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04) !important;
}

/* 博客表格 */
.blog-table {
  border-radius: var(--radius-md);
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04) !important;
}

.title-cell {
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 300px;
}

.actions-cell {
  display: flex;
  gap: 8px;
}

/* 空状态 */
.empty-state {
  background-color: var(--sand-100);
  border-radius: var(--radius-md);
  padding: 3rem 1rem;
  margin: 1rem 0;
}

.image-upload-section {
  margin-bottom: 16px;
}

.image-label {
  color: rgba(0, 0, 0, 0.6);
  font-size: 12px;
  margin-bottom: 8px;
}
</style> 