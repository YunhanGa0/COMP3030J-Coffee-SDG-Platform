<template>
  <div class="training-management">
    <v-container fluid>
      <!-- 页面标题 -->
      <v-row>
        <v-col cols="12">
          <v-card class="pa-4 elevation-1">
            <div class="d-flex align-center">
              <v-avatar size="60" color="primary" class="mr-4">
                <v-icon size="40" color="white">mdi-school</v-icon>
              </v-avatar>
              <div>
                <h1 class="text-h4 font-weight-bold primary--text mb-1">技术培训管理</h1>
                <div class="text-subtitle-1 grey--text">
                  管理和发布农场技术培训项目
                </div>
              </div>
            </div>
          </v-card>
        </v-col>
      </v-row>

      <!-- 功能区域 -->
      <v-row class="mt-4">
        <!-- 左侧：创建培训表单 -->
        <v-col cols="12" md="4">
          <v-card class="pa-4">
            <v-card-title class="d-flex align-center">
              <v-icon left color="primary">mdi-plus-circle</v-icon>
              创建培训项目
            </v-card-title>
            <v-card-text>
              <v-form ref="form" v-model="valid">
                <v-text-field
                  v-model="newTraining.title"
                  label="培训标题"
                  :rules="[v => !!v || '请输入标题']"
                  required
                ></v-text-field>

                <v-textarea
                  v-model="newTraining.description"
                  label="培训描述"
                  :rules="[v => !!v || '请输入描述']"
                  required
                ></v-textarea>

                <v-select
                  v-model="newTraining.type"
                  :items="trainingTypes"
                  label="培训类型"
                  :rules="[v => !!v || '请选择类型']"
                  required
                ></v-select>

                <v-text-field
                  v-model="newTraining.location"
                  label="培训地点"
                  :rules="[v => !!v || '请输入地点']"
                  required
                ></v-text-field>

                <v-text-field
                  v-model.number="newTraining.maxParticipants"
                  label="最大参与人数"
                  type="number"
                  :rules="[
                    v => !!v || '请输入人数',
                    v => v > 0 || '人数必须大于0'
                  ]"
                  required
                ></v-text-field>

                <v-menu
                  v-model="startDateMenu"
                  :close-on-content-click="false"
                  transition="scale-transition"
                  offset-y
                  min-width="290px"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                      v-model="newTraining.startDate"
                      label="开始日期"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                      :rules="[v => !!v || '请选择开始日期']"
                    ></v-text-field>
                  </template>
                  <v-date-picker
                    v-model="newTraining.startDate"
                    no-title
                    @input="startDateMenu = false"
                  ></v-date-picker>
                </v-menu>

                <v-menu
                  v-model="endDateMenu"
                  :close-on-content-click="false"
                  transition="scale-transition"
                  offset-y
                  min-width="290px"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                      v-model="newTraining.endDate"
                      label="结束日期"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                      :rules="[v => !!v || '请选择结束日期']"
                    ></v-text-field>
                  </template>
                  <v-date-picker
                    v-model="newTraining.endDate"
                    no-title
                    @input="endDateMenu = false"
                  ></v-date-picker>
                </v-menu>

                <v-file-input
                  v-model="trainingImage"
                  label="培训图片"
                  accept="image/*"
                  :rules="[v => !!v || '请上传图片']"
                  @change="handleImageUpload"
                ></v-file-input>
              </v-form>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="primary"
                :loading="saving"
                :disabled="!valid"
                @click="createTraining"
              >
                创建培训
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>

        <!-- 右侧：培训列表 -->
        <v-col cols="12" md="8">
          <v-card>
            <v-card-title>
              培训项目列表
              <v-spacer></v-spacer>
              <v-text-field
                v-model="search"
                append-icon="mdi-magnify"
                label="搜索"
                single-line
                hide-details
              ></v-text-field>
            </v-card-title>

            <v-data-table
              :headers="headers"
              :items="trainings"
              :search="search"
              :loading="loading"
            >
              <template v-slot:item.type="{ item }">
                {{ getTrainingTypeName(item.type) }}
              </template>

              <template v-slot:item.status="{ item }">
                <v-chip
                  :color="getStatusColor(item.status)"
                  small
                  label
                >
                  {{ getStatusName(item.status) }}
                </v-chip>
              </template>

              <template v-slot:item.actions="{ item }">
                <v-btn
                  small
                  color="info"
                  @click="viewDetails(item)"
                  class="mr-2"
                  outlined
                >

                  详情
                </v-btn>
                <v-btn
                  small
                  color="primary"
                  @click="viewApplications(item)"
                  class="mr-2"
                  outlined
                >
                  报名
                </v-btn>
                <v-btn
                  small
                  color="warning"
                  @click="openStatusDialog(item)"
                  class="mr-2"
                  outlined
                >
                  状态
                </v-btn>
                <v-btn
                  small
                  color="error"
                  @click="confirmDelete(item)"
                  outlined
                >
                  删除
                </v-btn>
              </template>
            </v-data-table>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <!-- 报名情况对话框 -->
    <v-dialog v-model="applicationsDialog" max-width="600">
      <v-card v-if="selectedTraining">
        <v-card-title class="primary white--text">
          报名情况 - {{ selectedTraining.title }}
          <v-spacer></v-spacer>
          <v-btn icon color="white" @click="applicationsDialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text class="pa-4">
          <v-list v-if="applications.length">
            <v-list-item v-for="app in applications" :key="app.applicationId">
              <v-list-item-content>
                <v-list-item-title>{{ app.farmerName }}</v-list-item-title>
                <v-list-item-subtitle>申请时间：{{ formatDate(app.applicationTime) }}</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
          </v-list>
          <div v-else class="text-center pa-4">
            暂无报名记录
          </div>
        </v-card-text>
      </v-card>
    </v-dialog>

    <!-- 状态更新对话框 -->
    <v-dialog v-model="statusDialog" max-width="400">
      <v-card v-if="selectedTraining">
        <v-card-title class="primary white--text">
          更新培训状态
        </v-card-title>
        <v-card-text class="pa-4">
          <v-select
            v-model="newStatus"
            :items="statusOptions"
            label="选择新状态"
          ></v-select>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="statusDialog = false">取消</v-btn>
          <v-btn
            color="primary"
            :loading="updating"
            @click="updateStatus"
          >
            确认
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 删除确认对话框 -->
    <v-dialog v-model="deleteDialog" max-width="400">
      <v-card>
        <v-card-title class="error white--text">
          确认删除
        </v-card-title>
        <v-card-text class="pa-4">
          确定要删除培训"{{ selectedTraining ? selectedTraining.title : '' }}"吗？此操作不可恢复。
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="deleteDialog = false">取消</v-btn>
          <v-btn
            color="error"
            :loading="deleting"
            @click="deleteTraining"
          >
            删除
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 培训详情对话框 -->
    <v-dialog v-model="detailsDialog" max-width="700">
      <v-card v-if="selectedTraining">
        <v-img
          :src="selectedTraining.imageUrl"
          height="200"
          class="white--text align-end"
          gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
        >
          <v-card-title class="text-h5">{{ selectedTraining.title }}</v-card-title>
        </v-img>
        <v-card-text class="pa-4">
          <v-row>
            <v-col cols="12" sm="6">
              <v-list-item>
                <v-list-item-icon>
                  <v-icon color="primary">mdi-tag</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>培训类型</v-list-item-title>
                  <v-list-item-subtitle>{{ getTrainingTypeName(selectedTraining.type) }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-col>
            <v-col cols="12" sm="6">
              <v-list-item>
                <v-list-item-icon>
                  <v-icon color="primary">mdi-map-marker</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>培训地点</v-list-item-title>
                  <v-list-item-subtitle>{{ selectedTraining.location }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-col>
            <v-col cols="12" sm="6">
              <v-list-item>
                <v-list-item-icon>
                  <v-icon color="primary">mdi-calendar-range</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>开始时间</v-list-item-title>
                  <v-list-item-subtitle>{{ formatDate(selectedTraining.startDate) }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-col>
            <v-col cols="12" sm="6">
              <v-list-item>
                <v-list-item-icon>
                  <v-icon color="primary">mdi-calendar-check</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>结束时间</v-list-item-title>
                  <v-list-item-subtitle>{{ formatDate(selectedTraining.endDate) }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-col>
            <v-col cols="12" sm="6">
              <v-list-item>
                <v-list-item-icon>
                  <v-icon color="primary">mdi-account-group</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>参与人数</v-list-item-title>
                  <v-list-item-subtitle>{{ selectedTraining.currentParticipants }}/{{ selectedTraining.maxParticipants }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-col>
            <v-col cols="12" sm="6">
              <v-list-item>
                <v-list-item-icon>
                  <v-icon :color="getStatusColor(selectedTraining.status)">mdi-clock-outline</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>当前状态</v-list-item-title>
                  <v-list-item-subtitle>
                    <v-chip
                      :color="getStatusColor(selectedTraining.status)"
                      small
                      label
                    >
                      {{ getStatusName(selectedTraining.status) }}
                    </v-chip>
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-col>
          </v-row>
          <v-divider class="my-4"></v-divider>
          <div class="text-subtitle-1 font-weight-bold mb-2">培训描述</div>
          <div class="text-body-2">{{ selectedTraining.description }}</div>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="detailsDialog = false">关闭</v-btn>
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
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TrainingManagement',

  data: () => ({
    valid: false,
    saving: false,
    loading: false,
    updating: false,
    deleting: false,
    search: '',

    // 新培训表单
    newTraining: {
      title: '',
      description: '',
      type: '',
      location: '',
      maxParticipants: null,
      startDate: null,
      endDate: null,
      imageUrl: ''
    },

    // 日期选择器
    startDateMenu: false,
    endDateMenu: false,

    // 培训类型选项
    trainingTypes: [
      { text: 'Coffee Planting', value: 'COFFEE_PLANTING' },
      { text: 'Processing Techniques', value: 'PROCESSING_TECHNIQUES' },
      { text: 'Quality Control', value: 'QUALITY_CONTROL' },
      { text: 'Sustainable Farming', value: 'SUSTAINABLE_FARMING' },
      { text: 'Organic Certification', value: 'ORGANIC_CERTIFICATION' },
      { text: 'Pest Management', value: 'PEST_MANAGEMENT' },
      { text: 'Soil Management', value: 'SOIL_MANAGEMENT' }
    ],

    // 状态选项
    statusOptions: [
      { text: 'Upcoming', value: 'UPCOMING' },
      { text: 'Ongoing', value: 'ONGOING' },
      { text: 'Completed', value: 'COMPLETED' },
      { text: 'Cancelled', value: 'CANCELLED' }
    ],

    // 表格配置
    headers: [
      { text: '培训标题', value: 'title' },
      //{ text: '培训类型', value: 'type' },
      { text: '地点', value: 'location' },
      { text: '开始日期', value: 'startDate' },
      //{ text: '结束日期', value: 'endDate' },
      { text: '状态', value: 'status' },
      //{ text: '当前/最大人数', value: 'participants', sortable: false },
      { text: '操作', value: 'actions', sortable: false }
    ],

    // 培训列表
    trainings: [],

    // 对话框控制
    applicationsDialog: false,
    statusDialog: false,
    deleteDialog: false,
    detailsDialog: false,

    // 选中的培训
    selectedTraining: null,
    newStatus: null,

    // 报名列表
    applications: [],

    // 图片上传
    trainingImage: null,

    // 提示消息
    snackbar: {
      show: false,
      text: '',
      color: 'success'
    }
  }),

  mounted() {
    this.fetchTrainings()
  },

  methods: {
    // 获取培训列表
    async fetchTrainings() {
      this.loading = true
      try {
        const response = await axios.get('/api/trainings')
        if (response.data.code === 200) {
          this.trainings = response.data.data.content
        }
      } catch (error) {
        this.showMessage('获取培训列表失败', 'error')
      } finally {
        this.loading = false
      }
    },

    // 创建培训
    async createTraining() {
      if (!this.$refs.form.validate()) return

      this.saving = true
      try {
        // 转换日期格式为后端所需的LocalDateTime格式
        const trainingData = {
          ...this.newTraining,
          startDate: `${this.newTraining.startDate}T00:00:00`,
          endDate: `${this.newTraining.endDate}T23:59:59`
        }

        const response = await axios.post('/api/admin/trainings', trainingData)
        if (response.data.code === 200) {
          this.showMessage('培训创建成功', 'success')
          this.$refs.form.reset()
          this.newTraining = {
            title: '',
            description: '',
            type: '',
            location: '',
            maxParticipants: null,
            startDate: null,
            endDate: null,
            imageUrl: ''
          }
          this.trainingImage = null
          await this.fetchTrainings()
        }
      } catch (error) {
        console.error('创建培训失败:', error)
        const errorMessage = error.response && error.response.data && error.response.data.message
          ? error.response.data.message
          : '创建培训失败'
        this.showMessage(errorMessage, 'error')
      } finally {
        this.saving = false
      }
    },

    // 查看报名情况
    async viewApplications(training) {
      this.selectedTraining = training
      this.applicationsDialog = true
      try {
        const response = await axios.get(`/api/admin/trainings/${training.id}/applications`)
        if (response.data.code === 200) {
          this.applications = response.data.data
        }
      } catch (error) {
        this.showMessage('获取报名情况失败', 'error')
      }
    },

    // 打开状态更新对话框
    openStatusDialog(training) {
      this.selectedTraining = training
      this.newStatus = training.status
      this.statusDialog = true
    },

    // 更新培训状态
    async updateStatus() {
      if (!this.selectedTraining || !this.newStatus) return

      this.updating = true
      try {
        const response = await axios.put(
          `/api/admin/trainings/update/${this.selectedTraining.id}`,
          { status: this.newStatus }
        )
        if (response.data.code === 200) {
          this.showMessage('状态更新成功', 'success')
          this.statusDialog = false
          this.fetchTrainings()
        }
      } catch (error) {
        this.showMessage('更新状态失败', 'error')
      } finally {
        this.updating = false
      }
    },

    // 确认删除对话框
    confirmDelete(training) {
      this.selectedTraining = training
      this.deleteDialog = true
    },

    // 删除培训
    async deleteTraining() {
      if (!this.selectedTraining) return

      this.deleting = true
      try {
        const response = await axios.delete(`/api/admin/trainings/${this.selectedTraining.id}`)
        if (response.data.code === 200) {
          this.showMessage('培训删除成功', 'success')
          this.deleteDialog = false
          this.fetchTrainings()
        }
      } catch (error) {
        this.showMessage('删除培训失败', 'error')
      } finally {
        this.deleting = false
      }
    },

    // 处理图片上传
    async handleImageUpload(file) {
      if (!file) {
        this.newTraining.imageUrl = ''
        return
      }

      try {
        const formData = new FormData()
        formData.append('image', file)
        formData.append('type', 'TRAINING')

        const response = await axios.post('/api/articles/images/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })

        if (response.data.code === 200) {
          this.newTraining.imageUrl = response.data.data.url
          this.showMessage('图片上传成功', 'success')
        }
      } catch (error) {
        console.error('图片上传失败:', error)
        this.showMessage('图片上传失败', 'error')
        this.trainingImage = null
        this.newTraining.imageUrl = ''
      }
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'UPCOMING': 'warning',
        'ONGOING': 'success',
        'COMPLETED': 'grey',
        'CANCELLED': 'error'
      }
      return colorMap[status] || 'grey'
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    },

    // 显示提示消息
    showMessage(text, color = 'success') {
      this.snackbar = {
        show: true,
        text,
        color
      }
    },

    // 工具方法
    getTrainingTypeName(type) {
      const typeMap = {
        'PLANTING': '咖啡种植',
        'PROCESSING': '咖啡加工',
        'QUALITY_CONTROL': '质量控制',
        'SUSTAINABILITY': '可持续发展'
      }
      return typeMap[type] || type
    },

    getStatusName(status) {
      const statusMap = {
        'UPCOMING': '即将开始',
        'ONGOING': '进行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },

    // 查看培训详情
    async viewDetails(training) {
      try {
        const response = await axios.get(`/api/trainings/${training.id}`)
        if (response.data.code === 200) {
          this.selectedTraining = response.data.data
          this.detailsDialog = true
        }
      } catch (error) {
        this.showMessage('获取培训详情失败', 'error')
      }
    }
  }
}
</script>

<style scoped>
.training-management {
  padding-bottom: 2rem;
}
</style>
