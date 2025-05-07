<template>
  <div class="farmer-dashboard">
    <!-- 顶部横幅 -->
    <section class="hero">
      <div class="hero-inner">
        <h1>Farm Management Center</h1>
        <div class="hero-meta">
          <span>Farmer: {{ farm.user ? farm.user.username : 'Loading...' }}</span>
          <span>Farm Name: {{ farm.farmName || 'Loading...' }}</span>
          <span v-if="farm.isCertificated" class="badge-cert">
            <v-icon small class="mr-1">mdi-check-circle</v-icon>
            Certified Farm
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
            Farm Info
          </div>
          <div
            class="nav-item"
            :class="{ active: activeTab === 'blogs' }"
            @click="activeTab = 'blogs'"
          >
            <v-icon size="20" class="mr-2">mdi-post</v-icon>
            Blog Management
          </div>
          <div
            class="nav-item"
            :class="{ active: activeTab === 'orders' }"
            @click="activeTab = 'orders'"
          >
            <v-icon size="20" class="mr-2">mdi-package-variant-closed</v-icon>
            Order Management
          </div>
        </div>
      </aside>

      <!-- 内容区域 -->
      <section class="content-area">
        <!-- 农场信息编辑 -->
        <div v-if="activeTab === 'profile'" class="profile-editor">
          <header class="section-header">
            <h2>Farm profile</h2>
            <div class="actions">
              <v-btn
                color="var(--green-700)"
                outlined
                small
                :loading="saveLoading"
                @click="saveProfile"
              >
                Save Changes
              </v-btn>
            </div>
          </header>

          <v-card class="form-card">
            <v-card-text>
              <v-form ref="profileForm">
                <div class="form-layout">
                  <div class="image-section">
                    <div class="image-upload-section">
                      <label class="image-label">Farm Image</label>
                      <div class="image-preview">
                        <v-img
                          v-if="farmProfile.imageUrl"
                          :src="farmProfile.imageUrl"
                          :aspect-ratio="16/9"
                          cover
                          class="preview-image"
                          height="200"
                        >
                          <template v-slot:placeholder>
                            <v-row
                              class="fill-height ma-0"
                              align="center"
                              justify="center"
                            >
                              <v-progress-circular
                                indeterminate
                                color="grey lighten-2"
                              ></v-progress-circular>
                            </v-row>
                          </template>
                        </v-img>
                        <v-file-input
                          v-model="farmImageFile"
                          accept="image/*"
                          outlined
                          dense
                          hide-details
                          placeholder="Select image"
                          prepend-icon="mdi-camera"
                          @change="handleImageUpload"
                          class="mt-4"
                          :rules="[v => !!v || 'Please upload farm image']"
                        ></v-file-input>
                      </div>
                    </div>
                  </div>
                  <div class="form-fields">
                    <v-row>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model="farmProfile.farmName"
                          label="Farm Name"
                          outlined
                          dense
                          :rules="[v => !!v || 'Farm name is required']"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model="farmProfile.country"
                          label="Country"
                          outlined
                          dense
                          :rules="[v => !!v || 'Country is required']"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model="farmProfile.location"
                          label="Address"
                          outlined
                          dense
                          :rules="[v => !!v || 'Address is required']"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model.number="farmProfile.size"
                          label="Area (hectares)"
                          outlined
                          dense
                          type="number"
                          :rules="[v => !!v || 'Area is required']"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model.number="farmProfile.establishedYear"
                          label="Established Year"
                          outlined
                          dense
                          type="number"
                          :rules="[v => !!v || 'Established year is required']"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model.number="farmProfile.elevation"
                          label="Elevation (meters"
                          outlined
                          dense
                          type="number"
                          :rules="[v => !!v || 'Elevation is required']"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-select
                          v-model="farmProfile.soilType"
                          :items="soilTypes"
                          label="Soil Type"
                          outlined
                          dense
                          :rules="[v => !!v || 'Soil type is required']"
                        ></v-select>
                      </v-col>
                      <v-col cols="12">
                        <v-textarea
                          v-model="farmProfile.description"
                          label="Farm Description"
                          outlined
                          :rules="[v => !!v || 'Description is required']"
                        ></v-textarea>
                      </v-col>
                    </v-row>
                  </div>
                </div>
              </v-form>
            </v-card-text>
          </v-card>
        </div>

        <!-- 博客管理 -->
        <div v-if="activeTab === 'blogs'" class="blogs-manager">
          <header class="section-header">
            <h2>Blog Management</h2>
            <div class="actions">
              <v-btn
                color="var(--green-700)"
                small
                dark
                @click="createNewBlog"
              >
                <v-icon size="18" class="mr-1">mdi-plus</v-icon>
                New Blog
              </v-btn>
            </div>
          </header>

          <!-- 博客列表 -->
          <div v-if="blogs.length === 0" class="text-center py-8 empty-state">
            <v-icon size="48" color="grey">mdi-text-box-outline</v-icon>
            <p class="mt-4 grey--text">No blogs yet. Create your first one now!</p>
          </div>

          <div v-else class="blog-list">
            <v-data-table
              :headers="blogHeaders"
              :items="blogs"
              :items-per-page="5"
              class="blog-table"
            >
              <template v-slot:item.info="{ item }">
                <div class="blog-info-cell">
                  <div class="blog-cover" v-if="item.coverImageUrl">
                    <v-img
                      :src="item.coverImageUrl"
                      height="80"
                      width="120"
                      cover
                      class="rounded"
                    >
                      <template v-slot:placeholder>
                        <v-row
                          class="fill-height ma-0"
                          align="center"
                          justify="center"
                        >
                          <v-progress-circular
                            indeterminate
                            color="grey lighten-2"
                          ></v-progress-circular>
                        </v-row>
                      </template>
                    </v-img>
                  </div>
                  <div class="blog-text">
                    <div
                      class="title-cell clickable"
                      @click="viewBlogDetail(item)"
                    >
                      {{ item.title }}
                    </div>
                    <div class="blog-meta">
                      <span class="blog-date">{{ formatDate(item.createdAt) }}</span>
                      <p class="blog-summary">{{ item.summary }}</p>
                    </div>
                  </div>
                </div>
              </template>

              <template v-slot:item.published="{ item }">
                <v-chip
                  small
                  :color="item.published ? 'success' : 'grey'"
                  text-color="white"
                >
                  {{ item.published ? 'Published' : 'Draft' }}
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
                    <span>Edit</span>
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
                    <span>{{ item.published ? 'Unpublish' : 'Publish' }}</span>
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
                    <span>Delete</span>
                  </v-tooltip>
                </div>
              </template>
            </v-data-table>
          </div>
        </div>

        <!-- 订单管理 -->
        <div v-if="activeTab === 'orders'" class="orders-manager">
          <header class="section-header">
            <h2>Order Management</h2>
          </header>

          <!-- 订单筛选 -->
          <v-btn-toggle
            v-model="orderStatusFilter"
            class="mb-4"
            mandatory
          >
            <v-btn value="ALL">All Orders</v-btn>
            <v-btn value="PAID">Pending</v-btn>
            <v-btn value="SHIPPED">Shipped</v-btn>
          </v-btn-toggle>


          <!-- 订单列表 -->
          <v-data-table
            :headers="orderHeaders"
            :items="filteredOrders"
            :items-per-page="5"
            class="order-table"
            dense
          >
            <template v-slot:item.status="{ item }">
              <v-chip
                small
                :color="item.status === 'SHIPPED' ? 'success' : 'orange'"
                text-color="white"
              >
                {{ item.status === 'SHIPPED' ? 'Shipped' : 'Pending' }}
              </v-chip>
            </template>

            <template v-slot:item.actions="{ item }">
              <v-btn
                small
                color="primary"
                :disabled="item.status === 'SHIPPED'"
                @click="markAsShipped(item.id)"
              >
                Mark as Shipped
              </v-btn>
            </template>
          </v-data-table>
        </div>

      </section>
    </main>

    <!-- 博客编辑对话框 -->
    <v-dialog v-model="blogDialog" max-width="900px" persistent>
      <v-card>
        <v-card-title class="headline">
          {{ editMode ? 'Edit Blog' : 'Create New Blog' }}
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
              label="Title"
              outlined
              :rules="[v => !!v || 'Title is required']"
            ></v-text-field>

            <v-text-field
              v-model="currentBlog.coverImageUrl"
              label="Cover Image URL"
              outlined
              hint="Valid image URL required"
            ></v-text-field>

            <v-textarea
              v-model="currentBlog.summary"
              label="Summary"
              outlined
              rows="2"
              auto-grow
              counter="150"
              :rules="[v => !!v || 'Summary is required']"
            ></v-textarea>

            <v-textarea
              v-model="currentBlog.content"
              label="Content"
              outlined
              rows="15"
              auto-grow
              :rules="[v => !!v || 'Content is required']"
            ></v-textarea>
          </v-form>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions class="pa-4">
          <v-btn
            text
            @click="closeBlogDialog"
          >
            Cancel
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn
            color="grey darken-1"
            text
            :loading="saveLoading"
            @click="saveBlog(false)"
          >
            Save as Draft
          </v-btn>
          <v-btn
            color="success"
            :loading="saveLoading"
            @click="saveBlog(true)"
          >
            {{ editMode ? 'Update & Publish' : 'Save & Publish' }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 确认删除对话框 -->
    <v-dialog v-model="deleteDialog" max-width="400">
      <v-card>
        <v-card-title class="headline">确认删除</v-card-title>
        <v-card-text>
          Are you sure you want to delete blog "{{ blogToDelete ? blogToDelete.title : '' }}"? This action cannot be undone.
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
            Delete
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

    <!-- 强制填写农场信息对话框 -->
    <v-dialog
      v-model="showProfileDialog"
      persistent
      max-width="800px"
      :overlay="true"
    >
      <v-card>
        <v-card-title class="headline primary white--text">
          Complete Farm Profile
        </v-card-title>
        <v-card-text class="pt-4">
          <p class="mb-4">Please complete your farm profile first. This is required to use platform features.</p>
          <v-form ref="profileForm">
            <v-row>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="farmProfile.farmName"
                  label="Farm Name"
                  outlined
                  dense
                  :rules="[v => !!v || 'Please enter farm name']"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="farmProfile.country"
                  label="Country"
                  outlined
                  dense
                  :rules="[v => !!v || 'Please enter country']"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="farmProfile.location"
                  label="地址"
                  outlined
                  dense
                  :rules="[v => !!v || '请输入地址']"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model.number="farmProfile.size"
                  label="面积 (公顷)"
                  outlined
                  dense
                  type="number"
                  :rules="[v => !!v || '请输入面积']"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model.number="farmProfile.establishedYear"
                  label="成立年份"
                  outlined
                  dense
                  type="number"
                  :rules="[v => !!v || '请输入成立年份']"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model.number="farmProfile.elevation"
                  label="海拔 (米)"
                  outlined
                  dense
                  type="number"
                  :rules="[v => !!v || '请输入海拔']"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-select
                  v-model="farmProfile.soilType"
                  :items="soilTypes"
                  label="土壤类型"
                  outlined
                  dense
                  :rules="[v => !!v || '请选择土壤类型']"
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
                      :rules="[v => !!v || '请上传农场图片']"
                    ></v-file-input>
                  </div>
                </div>
              </v-col>
              <v-col cols="12">
                <v-textarea
                  v-model="farmProfile.description"
                  label="农场描述"
                  outlined
                  :rules="[v => !!v || '请输入农场描述']"
                ></v-textarea>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions class="pb-4 px-4">
          <v-spacer></v-spacer>
          <v-btn
            color="primary"
            :loading="saveLoading"
            @click="saveProfile"
          >
            保存信息
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'FarmerDashboard',

  components: {
  },

  data: () => ({
    // 设置初始数据
    activeTab: 'profile',
    farm: {},
    blogs: [],
    hasCompletedProfile: false,  // 添加状态标记
    showProfileDialog: false,    // 添加对话框控制

    // 博客表格列
    blogHeaders: [
      { text: '博客信息', value: 'info', sortable: false },
      { text: '状态', value: 'published', sortable: true, width: 100 },
      { text: '操作', value: 'actions', sortable: false, align: 'end', width: 120 }
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

    orders: [],
    orderStatusFilter: 'ALL',
    orderHeaders: [
      { text: 'Order ID', value: 'id', sortable: false },
      { text: 'Recipient', value: 'recipientName', sortable: false },
      { text: 'Contact', value: 'contactNumber', sortable: false },
      { text: 'Address', value: 'shippingAddress', sortable: false },
      { text: 'Product', value: 'coffeeBeanName', sortable: false },
      { text: 'Quantity', value: 'quantity', sortable: false },
      { text: 'Total', value: 'totalAmount', sortable: false },
      { text: 'Status', value: 'status', sortable: false },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  async created() {
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

    // 先检查农场状态
    await this.checkFarmStatus()

  },

  methods: {
    // 检查农场状态
    async checkFarmStatus() {
      try {
        const response = await axios.get('/api/farms/status')
        if (response.data.code === 200) {
          this.hasCompletedProfile = response.data.data.hasCompletedProfile
          if (!this.hasCompletedProfile) {
            this.activeTab = 'profile'
            this.showProfileDialog = true
          } else {
            // 只有在完成农场信息填写后才获取其他信息
            await this.fetchFarmProfile()
            await this.fetchBlogs()
            await this.fetchOrders()
          }
        }
      } catch (error) {
        console.error('检查农场状态失败:', error)
        this.showMessage('检查农场状态失败', 'error')
      }
    },

    // 获取农场信息
    async fetchFarmProfile() {
      try {
        const response = await axios.get('/api/farms/profile')
        console.log('Farm profile response:', response.data)

        if (response.data.code === 200 && response.data.data) {
          // 更新农场基本信息
          this.farm = response.data.data

          // 填充编辑表单数据
          this.farmProfile = {
            farmName: response.data.data.farmName || '',
            country: response.data.data.country || '',
            location: response.data.data.location || '',
            size: response.data.data.size || null,
            establishedYear: response.data.data.establishedYear || null,
            description: response.data.data.description || '',
            imageUrl: response.data.data.imageUrl || '',
            elevation: response.data.data.elevation || null,
            soilType: response.data.data.soilType || ''
          }

          // 更新页面标题
          if (this.farm.farmName) {
            document.title = `${this.farm.farmName} - 农场管理中心`
          }
        }
      } catch (error) {
        console.error('获取农场信息失败:', error)
        this.showMessage('暂无农场信息', 'info')
      }
    },

    // 保存农场资料
    async saveProfile() {
      if (!this.$refs.profileForm.validate()) return

      this.saveLoading = true
      try {
        const profileData = {
          ...this.farmProfile,
          id: this.farm.id
        };

        let response;
        if (!this.hasCompletedProfile) {
          response = await axios.post('/api/farms/profile', profileData);
        } else {
          response = await axios.put('/api/farms/update', profileData);
        }

        if (response.data.code === 200) {
          this.showMessage(
            !this.hasCompletedProfile ? '农场信息创建成功' : '农场信息更新成功',
            'success'
          );
          this.hasCompletedProfile = true;
          this.showProfileDialog = false;
          await this.fetchFarmProfile();
          await this.fetchBlogs();
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
        const response0 = await axios.get('/api/farms/profile')
        this.farm = response0.data.data
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

    // 查看博客详情
    viewBlogDetail(blog) {
      this.$router.push({
        path: `/farms/${this.farm.id}/blogs/${blog.id}`
      });
    },

    // 获取订单列表
    async fetchOrders() {
      try {
        const response = await axios.get(`/api/farmers/orders`);
        if (response.data.code === 200) {
          this.orders = response.data.data;
        } else {
          this.showMessage(response.data.message || 'Failed to load orders', 'error');
        }
      } catch (error) {
        console.error('Failed to fetch orders:', error);
        this.showMessage('Failed to load orders', 'error');
      }
    },
    // 标记订单为已发货
    async markAsShipped (orderId) {
      try {
        // 等待请求完成
        const { data } = await axios.put(`/api/farmers/orders/${orderId}/ship`);

        if (data.code === 200) {
          this.showMessage('Order marked as shipped', 'success');
          this.fetchOrders();   // 重新加载订单列表
        } else {
          this.showMessage(data.message || 'Failed to update order', 'error');
        }
      } catch (err) {
        console.error('Failed to mark order as shipped:', err);
        this.showMessage('Failed to update order', 'error');
      }
    }
  },

  watch: {
    // 监听标签页变化
    activeTab(newTab) {
      if (!this.hasCompletedProfile) {
        this.activeTab = 'profile'
        this.showProfileDialog = true
      }
    }
  },
  computed: {
    filteredOrders () {
      // 确保有数据
      if (!this.orders || this.orders.length === 0) return []

      // 直接用 v‑model 绑定的字段
      if (this.orderStatusFilter === 'ALL') {
        return this.orders
      }
      // 这里最好统一大小写比较
      return this.orders.filter(
        o => (o.status || '').toUpperCase() === this.orderStatusFilter
      )
    }
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

.blog-info-cell {
  display: flex;
  gap: 1rem;
  padding: 0.5rem 0;
  align-items: center;
}

.blog-cover {
  flex-shrink: 0;
}

.blog-text {
  flex-grow: 1;
  min-width: 0;
}

.blog-meta {
  margin-top: 0.25rem;
}

.blog-date {
  font-size: 0.8rem;
  color: #666;
}

.blog-summary {
  font-size: 0.85rem;
  color: #555;
  margin-top: 0.25rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.title-cell {
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 500px;
}

.title-cell.clickable {
  cursor: pointer;
  color: var(--green-700);
}

.title-cell.clickable:hover {
  text-decoration: underline;
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

/* 表单布局 */
.form-layout {
  display: flex;
  gap: 2rem;
  align-items: flex-start;
}

.image-section {
  width: 300px;
  flex-shrink: 0;
}

.form-fields {
  flex: 1;
  min-width: 0;
}

.image-preview {
  background-color: var(--sand-50);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.preview-image {
  width: 100%;
  border-radius: var(--radius-md);
}

@media (max-width: 768px) {
  .form-layout {
    flex-direction: column;
  }

  .image-section {
    width: 100%;
    margin-bottom: 1.5rem;
  }


}

.orders-manager {
  margin-top: 2rem;
}

.order-tabs {
  margin-bottom: 1.5rem;
}

.order-table {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04) !important;
}

</style>
