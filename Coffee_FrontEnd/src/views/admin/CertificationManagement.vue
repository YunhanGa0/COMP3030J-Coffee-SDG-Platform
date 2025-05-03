<template>
  <div class="certification-management">
    <v-container fluid>
      <!-- 页面标题 -->
      <v-row>
        <v-col cols="12">
          <v-card class="pa-4 elevation-1">
            <div class="d-flex align-center">
              <v-avatar size="60" color="indigo" class="mr-4">
                <v-icon size="40" color="white">mdi-certificate</v-icon>
              </v-avatar>
              <div>
                <h1 class="text-h4 font-weight-bold primary--text mb-1">Farm certification management</h1>
                <div class="text-subtitle-1 grey--text">
                  Review and manage the farm's certification applications to ensure compliance with sustainability standards
                </div>
              </div>
              <v-spacer></v-spacer>
              <v-btn
                color="primary"
                class="ml-2"
                @click="fetchCertifications"
                :loading="loading"
              >
                <v-icon left>mdi-refresh</v-icon>
                Refresh data
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
              Certification application status filtering
            </v-card-title>
            <v-card-text class="px-0 pb-0">
              <v-chip-group
                v-model="selectedStatus"
                mandatory
                active-class="primary--text"
              >
                <v-chip
                  v-for="status in statusOptions"
                  :key="status.value"
                  :value="status.value"
                  filter
                  outlined
                >
                  {{ status.text }}
                  <v-avatar right color="primary" v-if="getCountByStatus(status.value) > 0">
                    {{ getCountByStatus(status.value) }}
                  </v-avatar>
                </v-chip>
              </v-chip-group>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>

      <!-- 认证申请列表 -->
      <v-row class="mt-4">
        <v-col cols="12">
          <v-card>
            <v-data-table
              :headers="headers"
              :items="filteredCertifications"
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

              <template v-slot:item.createdAt="{ item }">
                {{ formatDate(item.createdAt) }}
              </template>

              <template v-slot:item.actions="{ item }">
                <v-btn
                  small
                  color="primary"
                  class="mr-1"
                  @click="viewDetails(item)"
                  :disabled="loading"
                >
                  查看
                </v-btn>
                <v-btn
                  small
                  color="success"
                  class="mr-1"
                  @click="approveCertification(item)"
                  :disabled="loading || item.status !== 'PENDING'"
                >
                  批准
                </v-btn>
                <v-btn
                  small
                  color="error"
                  @click="rejectCertification(item)"
                  :disabled="loading || item.status !== 'PENDING'"
                >
                  拒绝
                </v-btn>
              </template>

              <template v-slot:no-data>
                <div class="text-center pa-5">
                  <v-icon large color="grey lighten-1">mdi-certificate-outline</v-icon>
                  <p class="mt-3 grey--text">There is no certification application data yet</p>
                </div>
              </template>
            </v-data-table>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <!-- 认证详情对话框 -->
    <v-dialog v-model="detailDialog" max-width="800">
      <v-card v-if="selectedCertification">
        <v-card-title class="primary white--text">
          <v-icon color="white" left>mdi-certificate</v-icon>
          Details of the certification application
          <v-spacer></v-spacer>
          <v-btn icon color="white" @click="detailDialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>

        <v-card-text class="pt-4">
          <v-row>
            <v-col cols="12" sm="6">
              <div class="caption grey--text">Farm name</div>
              <div class="subtitle-1 font-weight-medium">{{ selectedCertification.farmName }}</div>
            </v-col>
            <v-col cols="12" sm="6">
              <div class="caption grey--text">Status</div>
              <v-chip
                :color="getStatusColor(selectedCertification.status)"
                small
              >
                {{ getStatusText(selectedCertification.status) }}
              </v-chip>
            </v-col>
            <v-col cols="12" sm="6">
              <div class="caption grey--text">Application Date</div>
              <div class="subtitle-1">{{ formatDate(selectedCertification.createdAt) }}</div>
            </v-col>
            <v-col cols="12" sm="6">
              <div class="caption grey--text">Farm owner</div>
              <div class="subtitle-1">{{ selectedCertification.ownerName }}</div>
            </v-col>

            <v-col cols="12">
              <v-divider class="my-3"></v-divider>
              <div class="caption grey--text">Certification Type</div>
              <div class="subtitle-1 mb-2">{{ selectedCertification.certificationType }}</div>

              <div class="caption grey--text">Certification Description</div>
              <div class="body-1 mb-4">{{ selectedCertification.description }}</div>

              <div class="caption grey--text mb-2">Supporting documents submitted</div>
              <v-card outlined class="pa-3">
                <v-row>
                  <v-col v-for="(file, index) in selectedCertification.documents" :key="index" cols="6" md="4">
                    <v-card outlined class="pa-2">
                      <v-img
                        v-if="isImageFile(file.url)"
                        :src="file.url"
                        height="120"
                        contain
                        class="grey lighten-2"
                      ></v-img>
                      <div v-else class="d-flex align-center justify-center grey lighten-3" style="height: 120px">
                        <v-icon large color="grey">mdi-file-document-outline</v-icon>
                      </div>
                      <div class="caption mt-1 text-center text-truncate">{{ file.name }}</div>
                      <div class="d-flex justify-center mt-1">
                        <v-btn x-small text color="primary" @click="viewDocument(file)">
                          <v-icon x-small left>mdi-eye</v-icon> Check
                        </v-btn>
                      </div>
                    </v-card>
                  </v-col>
                </v-row>
              </v-card>
            </v-col>
          </v-row>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions class="pa-4">
          <v-spacer></v-spacer>
          <v-btn
            color="error"
            :disabled="selectedCertification.status !== 'PENDING'"
            class="mr-2"
            @click="rejectDialog = true"
          >
            <v-icon left>mdi-close</v-icon>
            Reject
          </v-btn>
          <v-btn
            color="success"
            :disabled="selectedCertification.status !== 'PENDING'"
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
        <v-card-title class="headline">Approve the certification</v-card-title>
        <v-card-text>
          Are you sure you want to approve the farm's application for certification? Upon approval, the farm will receive a certification mark.

          <v-textarea
            v-model="approveComment"
            outlined
            label="批准备注（可选）"
            rows="3"
            class="mt-4"
          ></v-textarea>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey darken-1" text @click="approveDialog = false">取消</v-btn>
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
        <v-card-title class="headline">拒绝认证</v-card-title>
        <v-card-text>
          Are you sure you want to reject the farm's application for certification?

          <v-textarea
            v-model="rejectReason"
            outlined
            label="拒绝原因（必填）"
            rows="3"
            class="mt-4"
            :rules="[v => !!v || '请填写拒绝原因']"
          ></v-textarea>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey darken-1" text @click="rejectDialog = false">取消</v-btn>
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
  name: 'CertificationManagement',

  data() {
    return {
      // 表格列配置
      headers: [
        { text: 'ID', value: 'id', width: '80px' },
        { text: '农场ID', value: 'farmId' },
        { text: '认证描述', value: 'description' },
        { text: '申请时间', value: 'submitTime' },
        { text: '状态', value: 'status', width: '120px' },
        { text: '管理员反馈', value: 'adminFeedback' },
        { text: '操作', value: 'actions', sortable: false, width: '200px' }
      ],

      // 数据和加载状态
      certifications: [],
      loading: false,
      processingAction: false,

      // 状态过滤
      selectedStatus: 'PENDING',
      statusOptions: [
        { text: '待审核', value: 'PENDING' },
        { text: '已批准', value: 'APPROVED' },
        { text: '已拒绝', value: 'REJECTED' }
      ],

      // 对话框控制
      detailDialog: false,
      approveDialog: false,
      rejectDialog: false,
      selectedCertification: null,

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
    // 根据状态过滤认证申请
    filteredCertifications() {
      if (this.selectedStatus === 'ALL') {
        return this.certifications;
      }
      return this.certifications.filter(cert => cert.status === this.selectedStatus);
    }
  },

  mounted() {
    this.fetchCertifications();
  },

  methods: {
    // 获取认证申请列表
    async fetchCertifications() {
      this.loading = true;
      try {
        const response = await axios.get('/api/admin/certification/applications');
        if (response.data.code === 200) {
          this.certifications = response.data.data;
        } else {
          this.showMessage(response.data.message || 'Failed to get the certification application list', 'error');
        }
      } catch (error) {
        console.error('Failed to get the certification application list:', error);
        this.showMessage('Failed to get the certification application list', 'error');
      } finally {
        this.loading = false;
      }
    },

    // 查看认证详情
    viewDetails(item) {
      this.selectedCertification = item;
      this.detailDialog = true;
    },

    // 批准认证
    approveCertification(item) {
      this.selectedCertification = item;
      this.approveDialog = true;
    },

    // 拒绝认证
    rejectCertification(item) {
      this.selectedCertification = item;
      this.rejectDialog = true;
    },

    // 确认批准认证
    async confirmApprove() {
      if (!this.selectedCertification) return;

      this.processingAction = true;
      try {
        const response = await axios.put(`/api/admin/certification/applications/review/${this.selectedCertification.id}`, {
          status: 'APPROVED',
          comment: this.approveComment
        });

        if (response.data.code === 200) {
          this.showMessage('The certification has been successfully approved', 'success');
          this.fetchCertifications();
          this.approveDialog = false;
          this.detailDialog = false;
          this.approveComment = '';
        } else {
          throw new Error(response.data.message || 'Approval certification failed');
        }
      } catch (error) {
        console.error('Approval certification failed:', error);
        this.showMessage('Approval certification failed: ' + (error.message || 'Unknown error'), 'error');
      } finally {
        this.processingAction = false;
      }
    },

    // 确认拒绝认证
    async confirmReject() {
      if (!this.selectedCertification || !this.rejectReason) return;

      this.processingAction = true;
      try {
        const response = await axios.put(`/api/admin/certification/applications/review/${this.selectedCertification.id}`, {
          status: 'REJECTED',
          comment: this.rejectReason
        });

        if (response.data.code === 200) {
          this.showMessage('The certification was successfully rejected', 'success');
          this.fetchCertifications();
          this.rejectDialog = false;
          this.detailDialog = false;
          this.rejectReason = '';
        } else {
          throw new Error(response.data.message || 'Failed to reject the authentication');
        }
      } catch (error) {
        console.error('Failed to reject the authentication:', error);
        this.showMessage('Failed to reject the authentication: ' + (error.message || 'Unknown error'), 'error');
      } finally {
        this.processingAction = false;
      }
    },

    // 查看文档
    viewDocument(file) {
      window.open(file.url, '_blank');
    },

    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待审核',
        'APPROVED': '已批准',
        'REJECTED': '已拒绝'
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
      if (status === 'ALL') {
        return this.certifications.length;
      }
      return this.certifications.filter(cert => cert.status === status).length;
    },

    // 判断是否为图片文件
    isImageFile(url) {
      const imageExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.webp'];
      const lowerUrl = url.toLowerCase();
      return imageExtensions.some(ext => lowerUrl.endsWith(ext));
    },

    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
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
.certification-management {
  padding-bottom: 2rem;
}

.status-badge {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 1rem;
  font-size: 0.75rem;
  font-weight: 500;
  color: white;
}
</style>
