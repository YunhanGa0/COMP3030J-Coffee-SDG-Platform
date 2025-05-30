<template>
  <div class="financial-management">
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
                <h1 class="text-h4 font-weight-bold success--text mb-1">FinancialManagement</h1>
                <div class="text-subtitle-1 grey--text">
                  Review and manage applications for financial support from farms
                </div>
              </div>
              <v-spacer></v-spacer>
              <v-btn
                color="success"
                class="ml-2"
                @click="fetchApplications"
                :loading="loading"
              >
                <v-icon left>mdi-refresh</v-icon>
                REFRESH DATA
              </v-btn>
            </div>
          </v-card>
        </v-col>
      </v-row>

      <!-- 状态过滤 -->
      <v-row class="mt-4">
        <v-col cols="12">
          <v-card class="pa-4">
            <v-card-title class="px-0 pt-0">
              Application Status Screening
            </v-card-title>
            <v-card-text class="px-0 pb-0">
              <v-chip-group
                v-model="selectedStatus"
                mandatory
                active-class="success--text"
              >
                <v-chip
                  v-for="status in statusOptions"
                  :key="status.value"
                  :value="status.value"
                  filter
                  outlined
                >
                  {{ status.text }}
                  <v-avatar right color="success" v-if="getCountByStatus(status.value) > 0">
                    {{ getCountByStatus(status.value) }}
                  </v-avatar>
                </v-chip>
              </v-chip-group>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>

      <!-- 申请列表 -->
      <v-row class="mt-4">
        <v-col cols="12">
          <v-card>
            <v-data-table
              :headers="headers"
              :items="filteredApplications"
              :items-per-page="10"
              :loading="loading"
              class="elevation-0"
            >
              <template v-slot:item.status="{ item }">
                <v-chip
                  :color="getStatusColor(item.status)"
                  small
                  label
                >
                  {{ getStatusText(item.status) }}
                </v-chip>
              </template>

              <template v-slot:item.applyTime="{ item }">
                {{ item.applyTime }}
              </template>

              <template v-slot:item.financialSupport.budget="{ item }">
                ¥{{ item.financialSupport.budget }}
              </template>

              <template v-slot:item.actions="{ item }">
                <v-btn
                  small
                  color="primary"
                  class="mr-1"
                  @click="viewDetails(item)"
                  :disabled="loading"
                >
                  View
                </v-btn>
                <template v-if="item.status === 'PENDING'">
                  <v-btn
                    small
                    color="success"
                    class="mr-1"
                    @click="approveApplication(item)"
                    :disabled="loading"
                  >
                    Approve
                  </v-btn>
                  <v-btn
                    small
                    color="error"
                    @click="rejectApplication(item)"
                    :disabled="loading"
                  >
                    Reject
                  </v-btn>
                </template>
              </template>

              <template v-slot:no-data>
                <div class="text-center pa-5">
                  <v-icon large color="grey lighten-1">mdi-cash-remove</v-icon>
                  <p class="mt-3 grey--text">There is no data on financial support applications</p>
                </div>
              </template>
            </v-data-table>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <!-- 详情对话框 -->
    <v-dialog v-model="detailDialog" max-width="800">
      <v-card v-if="selectedApplication">
        <v-card-title class="success white--text">
          <v-icon color="white" left>mdi-cash</v-icon>
          Details of the financial support application
          <v-spacer></v-spacer>
          <v-btn icon color="white" @click="detailDialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>

        <v-card-text class="pt-4">
          <v-row>
            <v-col cols="12" sm="6">
              <div class="caption grey--text">Apply for a project</div>
              <div class="subtitle-1 font-weight-medium">{{ selectedApplication.financialSupport.title }}</div>
            </v-col>
            <v-col cols="12" sm="6">
              <div class="caption grey--text">Application Status</div>
              <v-chip
                :color="getStatusColor(selectedApplication.status)"
                small
              >
                {{ getStatusText(selectedApplication.status) }}
              </v-chip>
            </v-col>
            <v-col cols="12" sm="6">
              <div class="caption grey--text">Application Time</div>
              <div class="subtitle-1">{{ selectedApplication.applyTime }}</div>
            </v-col>
            <v-col cols="12" sm="6">
              <div class="caption grey--text">Budget</div>
              <div class="subtitle-1">¥{{ selectedApplication.financialSupport.budget }}</div>
            </v-col>

            <v-col cols="12">
              <v-divider class="my-3"></v-divider>
              <div class="caption grey--text">Project Description</div>
              <div class="body-1 mb-4">{{ selectedApplication.financialSupport.description }}</div>

              <div class="caption grey--text">Application Purpose</div>
              <div class="body-1 mb-4">{{ selectedApplication.purpose }}</div>

              <div v-if="selectedApplication.adminFeedback" class="mt-4">
                <div class="caption grey--text">Admin Feedback</div>
                <div class="body-1">{{ selectedApplication.adminFeedback }}</div>
              </div>
            </v-col>
          </v-row>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions class="pa-4">
          <v-spacer></v-spacer>
          <v-btn
            color="error"
            :disabled="selectedApplication.status !== 'PENDING'"
            class="mr-2"
            @click="rejectDialog = true"
          >
            <v-icon left>mdi-close</v-icon>
            Reject
          </v-btn>
          <v-btn
            color="success"
            :disabled="selectedApplication.status !== 'PENDING'"
            @click="approveDialog = true"
          >
            <v-icon left>mdi-check</v-icon>
            Approve
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 批准确认对话框 -->
    <v-dialog v-model="approveDialog" max-width="500">
      <v-card>
        <v-card-title class="headline">Approve financial support</v-card-title>
        <v-card-text>
          Are you sure you want to approve the farm's application for financial support? After approval, the farm will be provided with corresponding financial support.

          <v-textarea
            v-model="approveComment"
            outlined
            label="Approval Notes (Optional)"
            rows="3"
            class="mt-4"
          ></v-textarea>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey darken-1" text @click="approveDialog = false">Cancel</v-btn>
          <v-btn
            color="success"
            @click="confirmApprove"
            :loading="processingAction"
          >Confirm the approval</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 拒绝确认对话框 -->
    <v-dialog v-model="rejectDialog" max-width="500">
      <v-card>
        <v-card-title class="headline">Reject Application</v-card-title>
        <v-card-text>
          Are you sure you want to deny an application for financial support for the farm?

          <v-textarea
            v-model="rejectReason"
            outlined
            label="Rejection Reason (Required)"
            rows="3"
            class="mt-4"
            :rules="[v => !!v || 'Please enter rejection reason']"
          ></v-textarea>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey darken-1" text @click="rejectDialog = false">Cancel</v-btn>
          <v-btn
            color="error"
            @click="confirmReject"
            :loading="processingAction"
            :disabled="!rejectReason"
          >Confirm the rejection</v-btn>
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
  name: 'FinancialManagement',

  data() {
    return {
      // 表格列配置
      headers: [
        { text: 'ID', value: 'id', width: '80px' },
        { text: 'FarmerID', value: 'farmerId' },
        { text: 'Project Title', value: 'financialSupport.title' },
        { text: 'Application Budget', value: 'financialSupport.budget' },
        { text: 'Apply Time', value: 'applyTime' },
        { text: 'Status', value: 'status', width: '120px' },
        { text: 'Action', value: 'actions', sortable: false, width: '120px' }
      ],

      // 数据和加载状态
      applications: [],
      loading: false,
      processingAction: false,

      // 状态过滤
      selectedStatus: 'PENDING',
      statusOptions: [
        { text: 'Pending', value: 'PENDING' },
        { text: 'Approve', value: 'APPROVED' },
        { text: 'Rejected', value: 'REJECTED' }
      ],

      // 对话框控制
      detailDialog: false,
      approveDialog: false,
      rejectDialog: false,
      selectedApplication: null,

      // 表单数据
      approveComment: '',
      rejectReason: '',

      // 提示消息
      snackbar: {
        show: false,
        text: '',
        color: 'success'
      }
    }
  },

  computed: {
    // 根据状态过滤申请
    filteredApplications() {
      if (!this.selectedStatus) {
        return this.applications;
      }
      return this.applications.filter(app => app.status === this.selectedStatus);
    }
  },

  mounted() {
    this.fetchApplications();
  },

  methods: {
    // 获取申请列表
    async fetchApplications() {
      this.loading = true;
      try {
        const response = await axios.get('/api/admin/financial-applications');
        if (response.data.code === 200) {
          this.applications = response.data.data;
        } else {
          this.showMessage(response.data.message || 'Failed to get financial support application list', 'error');
        }
      } catch (error) {
        console.error('Failed to get financial support application list:', error);
        this.showMessage('Failed to get financial support application list', 'error');
      } finally {
        this.loading = false;
      }
    },

    // 查看详情
    viewDetails(item) {
      this.selectedApplication = item;
      this.detailDialog = true;
    },

    // 批准申请
    approveApplication(item) {
      this.selectedApplication = item;
      this.approveDialog = true;
    },

    // 拒绝申请
    rejectApplication(item) {
      this.selectedApplication = item;
      this.rejectDialog = true;
    },

    // 确认批准
    async confirmApprove() {
      if (!this.selectedApplication) return;

      this.processingAction = true;
      try {
        const response = await axios.put(`/api/admin/financial-applications/review/${this.selectedApplication.id}`, {
          status: 'APPROVED',
          adminFeedback: this.approveComment
        });

        if (response.data.code === 200) {
          this.showMessage('Financial support application approved successfully', 'success');
          this.fetchApplications();
          this.approveDialog = false;
          this.detailDialog = false;
          this.approveComment = '';
        } else {
          throw new Error(response.data.message || 'Failed to approve financial support application');
        }
      } catch (error) {
        console.error('Failed to approve financial support application:', error);
        this.showMessage('Failed to approve financial support application: ' + (error.message || 'Unknown error'), 'error');
      } finally {
        this.processingAction = false;
      }
    },

    // 确认拒绝
    async confirmReject() {
      if (!this.selectedApplication || !this.rejectReason) return;

      this.processingAction = true;
      try {
        const response = await axios.put(`/api/admin/financial-applications/review/${this.selectedApplication.id}`, {
          status: 'REJECTED',
          adminFeedback: this.rejectReason
        });

        if (response.data.code === 200) {
          this.showMessage('Financial support application rejected successfully', 'success');
          this.fetchApplications();
          this.rejectDialog = false;
          this.detailDialog = false;
          this.rejectReason = '';
        } else {
          throw new Error(response.data.message || 'Failed to reject financial support application');
        }
      } catch (error) {
        console.error('Failed to reject financial support application:', error);
        this.showMessage('Failed to reject financial support application: ' + (error.message || 'Unknown error'), 'error');
      } finally {
        this.processingAction = false;
      }
    },

    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        'PENDING': 'Pending',
        'APPROVED': 'Approved',
        'REJECTED': 'Rejected'
      };
      return statusMap[status] || status;
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'error'
      };
      return colorMap[status] || 'grey';
    },

    // 获取每种状态的数量
    getCountByStatus(status) {
      return this.applications.filter(app => app.status === status).length;
    },

    // 显示提示消息
    showMessage(text, color = 'success') {
      this.snackbar = {
        show: true,
        text,
        color
      };
    }
  }
}
</script>

<style scoped>
.financial-management {
  padding-bottom: 2rem;
}
</style>
