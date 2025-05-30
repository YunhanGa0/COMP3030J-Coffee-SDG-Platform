<template>
  <v-container class="fill-height" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card class="elevation-12">
          <v-toolbar color="primary" dark flat>
            <v-toolbar-title>Login</v-toolbar-title>
          </v-toolbar>
          <v-card-text>
            <v-form ref="form" v-model="valid" lazy-validation @submit.prevent="handleLogin">
              <v-text-field
                v-model="formData.username"
                :rules="usernameRules"
                label="Username"
                prepend-icon="mdi-account"
                required
              ></v-text-field>

              <v-text-field
                v-model="formData.password"
                :rules="passwordRules"
                label="Password"
                prepend-icon="mdi-lock"
                :type="showPassword ? 'text' : 'password'"
                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                @click:append="showPassword = !showPassword"
                required
              ></v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              color="primary"
              block
              :loading="loading"
              type="submit"
              class="mt-4"
              @click="handleLogin"
            >
              Login
            </v-btn>
            <div class="text-center mt-4">
              <router-link to="/register" class="text-decoration-none">
                No account? Register here
              </router-link>
            </div>

            <v-divider class="my-4"></v-divider>

            <!-- <div class="text-center">
              <v-btn
                text
                color="grey"
                @click="getAdminAccount"
                :loading="adminLoading"
              >
                <v-icon left small>mdi-shield-account</v-icon>
                Get Admin Account (For Testing)
              </v-btn>
            </div> -->
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>

    <!-- 提示信息 -->
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
  </v-container>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'Login',
  data: () => ({
    valid: true,
    loading: false,
    showPassword: false,
    formData: {
      username: '',
      password: ''
    },
    usernameRules: [
      v => !!v || 'Username is required'
    ],
    passwordRules: [
      v => !!v || 'Password is required'
    ],
    snackbar: {
      show: false,
      text: '',
      color: ''
    },
    adminLoading: false
  }),

  methods: {
    ...mapActions('auth', ['login']),

    async handleLogin() {
      if (this.$refs.form.validate()) {
        this.loading = true
        try {
          await this.login(this.formData)
          this.showMessage('Login successful', 'success')
          this.$router.push('/')
        } catch (error) {
          console.error('Login error:', error)
          this.showMessage(error.message || 'Login failed, please try again', 'error')
        } finally {
          this.loading = false
        }
      }
    },

    showMessage(text, color = 'success') {
      this.snackbar = {
        show: true,
        text,
        color
      }
    },

    async getAdminAccount() {
      this.adminLoading = true
      this.errorMessage = ''

      try {
        const response = await this.$store.dispatch('auth/getAdminForTest')
        console.log('Admin account:', response)

        this.$emit('show-message', { text: 'Logged in as admin', color: 'success' })
        this.$router.push('/admin')
      } catch (error) {
        console.error('Failed to get admin account:', error)
        this.errorMessage = 'Failed to get admin account, please try again'
      } finally {
        this.adminLoading = false
      }
    }
  }
}
</script>
