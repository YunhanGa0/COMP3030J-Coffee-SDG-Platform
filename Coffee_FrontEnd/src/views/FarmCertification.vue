<template>
  <div class="certification-page">
    <!-- 顶部横幅 -->
    <section class="hero">
      <div class="hero-overlay"></div> <!-- ✅ 遮罩层 -->
      <div class="hero-inner">
        <h1>Farm Certification Application</h1>
        <div class="hero-meta">
          <span>Enhance your farm's value</span>
          <span>Gain more support</span>
          <span>Join the sustainability program</span>
        </div>
      </div>
    </section>


    <!-- 主要内容 -->
    <main class="container">
      <v-card class="form-card">
        <v-card-text>
          <v-stepper v-model="currentStep" class="certification-stepper">
            <v-stepper-header>
              <v-stepper-step step="1">
                  Certification
              </v-stepper-step>
            </v-stepper-header>

            <v-stepper-items>
              <!-- 认证申请表单 -->
              <v-stepper-content step="1">
                <v-form ref="certForm" v-model="formValid">
                  <v-row>
                    <v-col cols="12">
                      <v-textarea
                        v-model="description"
                        label="Application Description"
                        :rules="rules.required"
                        outlined
                        counter="1000"
                        hint="Please describe your farm in detail, including cultivation methods, quality control measures, and sustainability practices."
                        persistent-hint
                      ></v-textarea>
                    </v-col>
                  </v-row>
                </v-form>
                <div class="step-actions">
                  <v-spacer></v-spacer>
                  <v-btn
                    color="success"
                    :loading="submitting"
                    @click="submitApplication"
                  >
                    Submit Application
                    <v-icon right>mdi-check</v-icon>
                  </v-btn>
                </div>
              </v-stepper-content>
            </v-stepper-items>
          </v-stepper>
        </v-card-text>
      </v-card>
    </main>

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
  name: 'FarmCertification',

  data: () => ({
    currentStep: 1,
    submitting: false,
    formValid: false,
    description: '',

    // 验证规则
    rules: {
      required: [v => !!v || '此项为必填']
    },

    // 提示消息
    snackbar: {
      show: false,
      text: '',
      color: 'success'
    }
  }),

  methods: {
    // 提交申请
    async submitApplication() {
      if (!this.$refs.certForm.validate()) return

      this.submitting = true
      try {
        const response = await axios.post('/api/farmer/certification/applications', {
          description: this.description
        })

        if (response.data.code === 200) {
          this.showMessage('认证申请提交成功！我们将尽快处理您的申请', 'success')
          // 延迟跳转
          setTimeout(() => {
            this.$router.push('/farmer-dashboard')
          }, 2000)
        } else {
          throw new Error(response.data.message || '提交失败')
        }
      } catch (error) {
        console.error('提交申请失败:', error)
        this.showMessage(error.message || '提交申请失败，请稍后重试', 'error')
      } finally {
        this.submitting = false
      }
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
.certification-page {
  background-color: var(--sand-50);
  min-height: 100vh;
}

/* 顶部横幅 */
.hero {
  position: relative;
  background: #00704A;
  color: #fff;
  padding: 3rem 1rem 2.5rem;
  overflow: hidden;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4); /* ✅ 黑色半透明遮罩 */
  z-index: 0;
}

.hero-inner {
  position: relative; /* ✅ 确保文字在遮罩之上 */
  z-index: 1;
  max-width: 1200px;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.hero h1{
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

/* 主要内容 */
.container {
  max-width: 1000px;
  margin: auto;
  padding: 2rem 1rem;
}

.form-card {
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm) !important;
}

/* 步骤条样式 */
.certification-stepper {
  box-shadow: none !important;
}

/* 步骤操作按钮 */
.step-actions {
  display: flex;
  margin-top: 2rem;
}

/* 确认信息样式 */
.confirm-item {
  margin-bottom: 1rem;
}

.confirm-item strong {
  color: var(--green-700);
  margin-right: 0.5rem;
}

@media (max-width: 600px) {
  .hero h1 {
    font-size: 2rem;
  }

  .hero-meta {
    font-size: 0.85rem;
  }

  .container {
    padding: 1rem;
  }
}
</style>
