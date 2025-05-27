<template>
  <div class="financial-support">
    <v-container fluid>
      <!-- 页面标题 -->
      <v-row>
        <v-col cols="12">
          <v-card class="pa-4 elevation-1">
            <div class="d-flex align-center">
              <v-avatar size="60" color="success" class="mr-4">
                <v-icon size="40" color="white">mdi-cash</v-icon>
              </v-avatar>
              <div>
                <h1 class="text-h4 font-weight-bold success--text mb-1">Financial Support</h1>
                <div class="text-subtitle-1 grey--text">
                  Apply for financial support to improve your farm
                </div>
              </div>
            </div>
          </v-card>
        </v-col>
      </v-row>

      <!-- 申请表单 -->
      <v-row class="mt-4">
        <v-col cols="12" md="6">
          <v-card class="pa-4">
            <v-card-title>
              <v-icon left color="success">mdi-file-document-edit</v-icon>
              New Application
            </v-card-title>
            <v-card-text>
              <v-form ref="form" v-model="valid">
                <v-select
                  v-model="application.financialSupportId"
                  :items="supportList"
                  item-text="title"
                  item-value="id"
                  label="Select Support Program"
                  :rules="[v => !!v || 'Please select a support program']"
                  required
                ></v-select>

                <v-textarea
                  v-model="application.purpose"
                  label="Purpose of Application"
                  :rules="[v => !!v || 'Purpose is required']"
                  required
                  rows="4"
                ></v-textarea>
              </v-form>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="success"
                :loading="submitting"
                :disabled="!valid"
                @click="submitApplication"
              >
                Submit Application
                <v-icon right>mdi-send</v-icon>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>

        <!-- 申请历史 -->
        <v-col cols="12" md="6">
          <v-card class="pa-4">
            <v-card-title>
              <v-icon left color="primary">mdi-history</v-icon>
              Application History
            </v-card-title>
            <v-card-text>
              <v-data-table
                :headers="headers"
                :items="applications"
                :loading="loading"
                :items-per-page="5"
              >
                <template v-slot:item.status="{ item }">
                  <v-chip
                    :color="getStatusColor(item.status)"
                    small
                    label
                  >
                    {{ item.status }}
                  </v-chip>
                </template>

                <template v-slot:item.amount="{ item }">
                  ${{ item.amount.toLocaleString() }}
                </template>

                <template v-slot:item.createdAt="{ item }">
                  {{ formatDate(item.createdAt) }}
                </template>

                <template v-slot:item.actions="{ item }">
                  <v-btn
                    x-small
                    color="primary"
                    @click="viewDetails(item)"
                  >
                    <v-icon small>mdi-eye</v-icon>
                  </v-btn>
                </template>
              </v-data-table>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <!-- 详情对话框 -->
    <v-dialog v-model="detailDialog" max-width="600">
      <v-card v-if="selectedApplication">
        <v-card-title class="success white--text">
          Application Details
          <v-spacer></v-spacer>
          <v-btn icon color="white" @click="detailDialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text class="pt-4">
          <v-row>
            <v-col cols="12" sm="6">
              <div class="caption grey--text">Support Program</div>
              <div class="subtitle-1">{{ selectedApplication.financialSupport.title }}</div>
            </v-col>
            <v-col cols="12" sm="6">
              <div class="caption grey--text">Application Status</div>
              <v-chip
                :color="getStatusColor(selectedApplication.status)"
                small
              >
                {{ selectedApplication.status }}
              </v-chip>
            </v-col>
            <v-col cols="12" sm="6">
              <div class="caption grey--text">Requested Amount</div>
              <div class="subtitle-1">${{ selectedApplication.financialSupport.budget.toLocaleString() }}</div>
            </v-col>
            <v-col cols="12" sm="6">
              <div class="caption grey--text">Application Date</div>
              <div class="subtitle-1">{{ selectedApplication.applyTime }}</div>
            </v-col>
            <v-col cols="12">
              <div class="caption grey--text">Program Description</div>
              <div class="body-1">{{ selectedApplication.financialSupport.description }}</div>
            </v-col>
            <v-col cols="12">
              <div class="caption grey--text">Purpose</div>
              <div class="body-1">{{ selectedApplication.purpose }}</div>
            </v-col>
            <v-col cols="12">
              <div class="caption grey--text">Admin Feedback</div>
              <div class="body-1">{{ selectedApplication.adminFeedback || '暂无反馈' }}</div>
            </v-col>
          </v-row>
        </v-card-text>
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
  name: 'FinancialSupport',

  data: () => ({
    valid: false,
    submitting: false,
    loading: false,
    detailDialog: false,
    selectedApplication: null,

    // 申请表单数据
    application: {
      financialSupportId: null,
      purpose: ''
    },

    // 支持项目列表
    supportList: [],

    // 表格配置
    headers: [
      { text: 'Support Program', value: 'financialSupport.title' },
      { text: 'Requested Amount', value: 'financialSupport.budget' },
      { text: 'Application Status', value: 'status' },
      { text: 'Application Date', value: 'applyTime' },
      { text: 'Admin Feedback', value: 'adminFeedback' },
      { text: 'Actions', value: 'actions', sortable: false }
    ],

    // 申请列表
    applications: [],

    // 提示消息
    snackbar: {
      show: false,
      text: '',
      color: 'success'
    }
  }),

  mounted() {
    this.fetchSupportList()
  },

  methods: {
    // 获取支持项目列表
    async fetchSupportList() {
      try {
        const response = await axios.get('/api/financial-supports')
        if (response.data.code === 200) {
          this.supportList = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch support programs')
        }
      } catch (error) {
        console.error('Failed to fetch support programs:', error)
        const errorMessage = error.response && error.response.data && error.response.data.message || 'Failed to fetch support programs'
        this.showMessage(errorMessage, 'error')
      }
    },

    // 提交申请
    async submitApplication() {
      if (!this.$refs.form.validate()) return

      this.submitting = true
      try {
        const response = await axios.post('/api/farmer/financial-applications', {
          financialSupportId: this.application.financialSupportId,
          purpose: this.application.purpose
        })

        if (response.data.code === 200 || 201) {
          this.showMessage('Application submitted successfully', 'success')
          this.$refs.form.reset()
          this.fetchApplications()
          this.application = {
            financialSupportId: null,
            purpose: ''
          }
        } else {
          throw new Error(response.data.message || 'Application submission failed')
        }
      } catch (error) {
        console.error('Application submission failed:', error)
        const errorMessage = error.response && error.response.data && error.response.data.message || '申请提交失败'
        this.showMessage(errorMessage, 'error')
      } finally {
        this.submitting = false
      }
    },

    // 查看详情
    viewDetails(item) {
      this.selectedApplication = item
      this.detailDialog = true
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'error',
        'PROCESSING': 'info'
      }
      return colorMap[status] || 'grey'
    },

    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
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
.financial-support {
  padding-bottom: 2rem;
}
</style>
