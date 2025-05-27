<template>
  <div class="training-application">
    <v-container>
      <!-- 页面标题 -->
      <v-row>
        <v-col cols="12">
          <v-card class="pa-4 elevation-1">
            <div class="d-flex align-center">
              <v-avatar size="60" color="primary" class="mr-4">
                <v-icon size="40" color="white">mdi-school</v-icon>
              </v-avatar>
              <div>
                <h1 class="text-h4 font-weight-bold primary--text mb-1">Technical Training Application</h1>
                <div class="text-subtitle-1 grey--text">
                  Browse and apply for our technical training programs
                </div>
              </div>
            </div>
          </v-card>
        </v-col>
      </v-row>

      <!-- 筛选器 -->
      <v-row class="mt-4">
        <v-col cols="12">
          <v-card class="pa-4">
            <div class="d-flex flex-wrap align-center">
              <v-select
                v-model="selectedType"
                :items="trainingTypes"
                label="Training Type"
                clearable
                class="mr-4 filter-select"
                style="max-width: 200px"
                @change="filterTrainings"
              ></v-select>
              <v-select
                v-model="selectedStatus"
                :items="statusTypes"
                label="Training Status"
                clearable
                class="mr-4 filter-select"
                style="max-width: 200px"
                @change="filterTrainings"
              ></v-select>
            </div>
          </v-card>
        </v-col>
      </v-row>

      <!-- 培训列表 -->
      <v-row class="mt-4">
        <v-col cols="12" md="6" lg="4" v-for="training in filteredTrainings" :key="training.id">
          <v-card class="training-card">
            <v-img
              :src="training.imageUrl || require('@/assets/pic/learning.jpg')"
              height="200"
              class="white--text align-end"
              gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
            >
              <v-card-title>{{ training.title }}</v-card-title>
            </v-img>

            <v-card-text>
              <v-chip
                small
                :color="getStatusColor(training.status)"
                text-color="white"
                class="mb-2"
              >
                {{ getStatusName(training.status) }}
              </v-chip>
              <v-chip
                small
                color="primary"
                outlined
                class="mb-2 ml-2"
              >
                {{ getTrainingTypeName(training.type) }}
              </v-chip>

              <div class="mt-2 text-subtitle-2 grey--text">
                <v-icon small class="mr-1">mdi-calendar</v-icon>
                {{ formatDate(training.startDate) }} - {{ formatDate(training.endDate) }}
              </div>

              <div class="mt-1 text-subtitle-2 grey--text">
                <v-icon small class="mr-1">mdi-map-marker</v-icon>
                {{ training.location }}
              </div>

              <div class="mt-1 text-subtitle-2 grey--text">
                <v-icon small class="mr-1">mdi-account-group</v-icon>
                {{ training.currentParticipants }}/{{ training.maxParticipants }} participants
              </div>

              <div class="mt-3 text-body-2">{{ training.description }}</div>
            </v-card-text>

            <v-divider></v-divider>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="primary"
                text
                @click="showDetails(training)"
              >
                Details
              </v-btn>
              <v-btn
                color="primary"
                :disabled="!canApply(training)"
                @click="applyTraining(training)"
              >
                Apply
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <!-- 详情对话框 -->
    <v-dialog v-model="detailDialog" max-width="600">
      <v-card v-if="selectedTraining">
        <v-img
          :src="selectedTraining.imageUrl || require('@/assets/pic/learning.jpg')"
          height="200"
          class="white--text align-end"
          gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
        >
          <v-card-title>{{ selectedTraining.title }}</v-card-title>
        </v-img>

        <v-card-text class="pt-4">
          <div class="mb-4">
            <div class="text-h6 mb-2">Details</div>
            <p>{{ selectedTraining.description }}</p>
          </div>

          <v-list dense>
            <v-list-item>
              <v-list-item-icon>
                <v-icon color="primary">mdi-calendar</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>Training Time</v-list-item-title>
                <v-list-item-subtitle>
                  {{ formatDate(selectedTraining.startDate) }} - {{ formatDate(selectedTraining.endDate) }}
                </v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>

            <v-list-item>
              <v-list-item-icon>
                <v-icon color="primary">mdi-map-marker</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>Location</v-list-item-title>
                <v-list-item-subtitle>{{ selectedTraining.location }}</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>

            <v-list-item>
              <v-list-item-icon>
                <v-icon color="primary">mdi-account-group</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>Participants</v-list-item-title>
                <v-list-item-subtitle>
                  {{ selectedTraining.currentParticipants }}/{{ selectedTraining.maxParticipants }} 人
                </v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>

            <v-list-item>
              <v-list-item-icon>
                <v-icon color="primary">mdi-tag</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>Training Type</v-list-item-title>
                <v-list-item-subtitle>
                  {{ getTrainingTypeName(selectedTraining.type) }}
                </v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            text
            @click="detailDialog = false"
          >
            Close
          </v-btn>
          <v-btn
            color="primary"
            :disabled="!canApply(selectedTraining)"
            @click="applyTraining(selectedTraining)"
          >
            申请参加
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
          Close
        </v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TrainingApplication',

  data: () => ({
    // 培训列表
    trainings: [],
    filteredTrainings: [],

    // 筛选条件
    selectedType: null,
    selectedStatus: null,

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
    statusTypes: [
      { text: 'Upcoming', value: 'UPCOMING' },
      { text: 'Ongoing', value: 'ONGOING' },
      { text: 'Completed', value: 'COMPLETED' },
      { text: 'Cancelled', value: 'CANCELLED' }
    ],

    // 详情对话框
    detailDialog: false,
    selectedTraining: null,

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
      try {
        const response = await axios.get('/api/trainings')
        if (response.data.code === 200) {
          this.trainings = response.data.data.content
          this.filterTrainings()
        }
      } catch (error) {
        this.showMessage('Failed to fetch training list', 'error')
      }
    },

    // 筛选培训
    filterTrainings() {
      this.filteredTrainings = this.trainings.filter(training => {
        const typeMatch = !this.selectedType || training.type === this.selectedType
        const statusMatch = !this.selectedStatus || training.status === this.selectedStatus
        return typeMatch && statusMatch
      })
    },

    // 申请培训
    async applyTraining(training) {
      try {
        const response = await axios.post(`/api/trainings/${training.id}/apply`)
        if (response.data.code === 200) {
          this.showMessage('Application successful', 'success')
          this.detailDialog = false
          await this.fetchTrainings()
        }
      } catch (error) {
        const errorMessage = error.response && error.response.data && error.response.data.message
        this.showMessage(errorMessage || 'Application failed', 'error')
      }
    },

    // 显示详情
    showDetails(training) {
      this.selectedTraining = training
      this.detailDialog = true
    },

    // 判断是否可以申请
    canApply(training) {
      console.log('Training status:', training.status);

      const statusOk = training.status === 'UPCOMING' || training.status === 'ONGOING';
      //const hasSpace = training.currentParticipants < training.maxParticipants;

      console.log('Status OK:', statusOk);
      //console.log('Has space:', hasSpace);

      return statusOk;
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

    // 获取状态名称
    getStatusName(status) {
      const statusMap = {
        'UPCOMING': '即将开始',
        'ONGOING': '进行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },

    // 获取培训类型名称
    getTrainingTypeName(type) {
      const typeMap = {
        'COFFEE_PLANTING': 'Coffee Planting',
        'PROCESSING_TECHNIQUES': 'Processing Techniques',
        'QUALITY_CONTROL': 'Quality Control',
        'SUSTAINABLE_FARMING': 'Sustainable Farming',
        'ORGANIC_CERTIFICATION': 'Organic Certification',
        'PEST_MANAGEMENT': 'Pest Management',
        'SOIL_MANAGEMENT': 'Soil Management'
      }
      return typeMap[type] || type
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
    }
  }
}
</script>

<style scoped>
.training-application {
  padding-bottom: 2rem;
}

.training-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.filter-select {
  flex: 0 0 auto;
}

@media (max-width: 600px) {
  .filter-select {
    width: 100%;
    margin-bottom: 1rem;
  }
}
</style>
