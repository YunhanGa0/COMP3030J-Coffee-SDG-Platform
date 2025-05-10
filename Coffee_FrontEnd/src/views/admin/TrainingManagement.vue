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
                <h1 class="text-h4 font-weight-bold primary--text mb-1">Technical Training Management</h1>
                <div class="text-subtitle-1 grey--text">
                  Manage and publish on-farm technical training programs
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
              Create a training program
            </v-card-title>
            <v-card-text>
              <v-form ref="form" v-model="valid">
                <v-text-field
                  v-model="newTraining.title"
                  label="Training Title"
                  :rules="[v => !!v || 'Please enter a title']"
                  required
                ></v-text-field>

                <v-textarea
                  v-model="newTraining.description"
                  label="Training Description"
                  :rules="[v => !!v || 'Please enter a description']"
                  required
                ></v-textarea>

                <v-select
                  v-model="newTraining.type"
                  :items="trainingTypes"
                  label="Type of training"
                  :rules="[v => !!v || 'Please select a type']"
                  required
                ></v-select>

                <v-text-field
                  v-model="newTraining.location"
                  label="Training location"
                  :rules="[v => !!v || 'Please enter a location']"
                  required
                ></v-text-field>

                <v-text-field
                  v-model.number="newTraining.maxParticipants"
                  label="Maximum number of participants"
                  type="number"
                  :rules="[
                    v => !!v || 'Please enter the number of participants',
                    v => v > 0 || 'The number of people must be greater than 0'
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
                      label="Start date"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                      :rules="[v => !!v || 'Please select a start date']"
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
                      label="End date"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                      :rules="[v => !!v || 'Please select an end date']"
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
                  label="Training pictures"
                  accept="image/*"
                  :rules="[v => !!v || 'Please upload an image']"
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
                Create a training
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>

        <!-- 右侧：培训列表 -->
        <v-col cols="12" md="8">
          <v-card>
            <v-card-title>
              List of training programs
              <v-spacer></v-spacer>
              <v-text-field
                v-model="search"
                append-icon="mdi-magnify"
                label="Search"
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
                  Details
                </v-btn>
                <v-btn
                  small
                  color="warning"
                  @click="openStatusDialog(item)"
                  class="mr-2"
                  outlined
                >
                  Status
                </v-btn>
                <v-btn
                  small
                  color="error"
                  @click="confirmDelete(item)"
                  outlined
                >
                  Delete
                </v-btn>
              </template>
            </v-data-table>
          </v-card>

          <!-- 所有申请信息列表 -->
          <v-card class="mt-4">
            <v-card-title class="primary white--text">
              <span class="text-h6">Training application records</span>
              <v-spacer></v-spacer>
              <v-text-field
                v-model="applicationSearch"
                append-icon="mdi-magnify"
                label="Search"
                single-line
                hide-details
                dark
                class="mt-1"
              ></v-text-field>
            </v-card-title>

            <v-card-text class="pa-4">
              <v-data-table
                :headers="allApplicationHeaders"
                :items="allApplications"
                :loading="loadingApplications"
                :search="applicationSearch"
                class="elevation-1"
                :footer-props="{
                  'items-per-page-options': [10, 20, 50, -1],
                  'items-per-page-text': 'Item per page',
                  'items-per-page-all-text': 'All'
                }"
                :items-per-page="10"
              >
                <template v-slot:item.applicationTime="{ item }">
                  {{ formatDate(item.applicationTime) }}
                </template>

                <template v-slot:item.trainingTitle="{ item }">
                  <v-chip
                    small
                    label
                    class="mr-2"
                  >
                    {{ item.trainingTitle }}
                  </v-chip>
                </template>

                <template v-slot:footer.prepend>
                  <div class="text-body-2 grey--text">
                    Total number of applications: {{ allApplications.length }}
                  </div>
                </template>

                <template v-slot:no-data>
                  There is no registration record yet
                </template>
              </v-data-table>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <!-- 状态更新对话框 -->
    <v-dialog v-model="statusDialog" max-width="400">
      <v-card v-if="selectedTraining">
        <v-card-title class="primary white--text">
          Update the training status
        </v-card-title>
        <v-card-text class="pa-4">
          <v-select
            v-model="newStatus"
            :items="statusOptions"
            label="Select a new status"
          ></v-select>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="statusDialog = false">Cancel</v-btn>
          <v-btn
            color="primary"
            :loading="updating"
            @click="updateStatus"
          >
            Confirm
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 删除确认对话框 -->
    <v-dialog v-model="deleteDialog" max-width="400">
      <v-card>
        <v-card-title class="error white--text">
          Confirm the deletion
        </v-card-title>
        <v-card-text class="pa-4">
          Decide that you want to delete the training"{{ selectedTraining ? selectedTraining.title : '' }}"? This operation is not recoverable.
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="deleteDialog = false">Cancel</v-btn>
          <v-btn
            color="error"
            :loading="deleting"
            @click="deleteTraining"
          >
            Delete
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
                  <v-list-item-title>Type of training</v-list-item-title>
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
                  <v-list-item-title>Training location</v-list-item-title>
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
                  <v-list-item-title>Start time</v-list-item-title>
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
                  <v-list-item-title>End time</v-list-item-title>
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
                  <v-list-item-title>Number of participants</v-list-item-title>
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
                  <v-list-item-title>Current status</v-list-item-title>
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
          <div class="text-subtitle-1 font-weight-bold mb-2">Training Description</div>
          <div class="text-body-2">{{ selectedTraining.description }}</div>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="detailsDialog = false">Close</v-btn>
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
          Close
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
      { text: 'Training', value: 'title' },
      //{ text: '培训类型', value: 'type' },
      { text: 'Location', value: 'location' },
      { text: 'Startdate', value: 'startDate' },
      //{ text: '结束日期', value: 'endDate' },
      { text: 'Status', value: 'status' },
      //{ text: '当前/最大人数', value: 'participants', sortable: false },
      { text: 'Action', value: 'actions', sortable: false }
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
    },

    // 申请搜索
    applicationSearch: '',

    // 所有申请列表
    allApplications: [],

    // 申请列表表头
    allApplicationHeaders: [
      { text: 'Training Title', value: 'trainingTitle' },
      { text: 'FarmerID', value: 'farmerId' },
      { text: 'FarmerName', value: 'farmerName' },
      { text: 'ApplicationTime', value: 'applicationTime' }
    ],

    // 加载状态
    loadingApplications: false
  }),

  mounted() {
    this.fetchTrainings()
    this.fetchAllApplications()
  },

  methods: {
    // 获取培训列表
    async fetchTrainings() {
      this.loading = true
      try {
        const response = await axios.get('/api/trainings')
        if (response.data.code === 200) {
          this.trainings = response.data.data.content
          // 获取完培训列表后，获取所有申请信息
          await this.fetchAllApplications()
        }
      } catch (error) {
        this.showMessage('Failed to get training list', 'error')
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
          this.showMessage('The training was created successfully', 'success')
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
        console.error('Failed to create training:', error)
        const errorMessage = error.response && error.response.data && error.response.data.message
          ? error.response.data.message
          : 'Failed to create training'
        this.showMessage(errorMessage, 'error')
      } finally {
        this.saving = false
      }
    },

    // 加载申请信息
    async loadApplications(training) {
      if (this.selectedTraining && this.selectedTraining.id === training.id) {
        // 如果点击的是当前选中的培训，则关闭申请信息
        this.selectedTraining = null
        this.applications = []
        return
      }

      this.selectedTraining = training
      this.loadingApplications = true
      try {
        const response = await axios.get(`/api/admin/trainings/${training.id}/applications`)
        if (response.data.code === 200) {
          this.applications = response.data.data
        }
      } catch (error) {
        this.showMessage('Failed to get enrollment', 'error')
        this.selectedTraining = null
        this.applications = []
      } finally {
        this.loadingApplications = false
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
          this.showMessage('The status update was successful', 'success')
          this.statusDialog = false
          this.fetchTrainings()
        }
      } catch (error) {
        this.showMessage('Update status failed', 'error')
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
          this.showMessage('The training deletion is successful', 'success')
          this.deleteDialog = false
          this.fetchTrainings()
        }
      } catch (error) {
        this.showMessage('Failed to delete training', 'error')
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
          this.showMessage('The image has been uploaded successfully', 'success')
        }
      } catch (error) {
        console.error('Image upload failed:', error)
        this.showMessage('Image upload failed', 'error')
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
        this.showMessage('Failed to get training details', 'error')
      }
    },

    // 获取所有申请信息
    async fetchAllApplications() {
      this.loadingApplications = true
      this.allApplications = []
      try {
        // 遍历所有培训项目，获取每个培训的申请信息
        for (const training of this.trainings) {
          try {
            const response = await axios.get(`/api/admin/trainings/${training.id}/applications`)
            if (response.data.code === 200) {
              // 为每个申请添加培训信息
              const applications = response.data.data.map(app => ({
                ...app,
                trainingId: training.id,
                trainingTitle: training.title,
                trainingType: training.type,
                trainingLocation: training.location,
                trainingStartDate: training.startDate,
                trainingEndDate: training.endDate
              }))
              this.allApplications.push(...applications)
            }
          } catch (error) {
            console.error(`Get training ${training.id} 's request record failed:`, error)
          }
        }
      } catch (error) {
        this.showMessage('Failed to obtain application records', 'error')
      } finally {
        this.loadingApplications = false
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
