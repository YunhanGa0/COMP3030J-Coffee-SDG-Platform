<template>
  <v-container class="profile-page py-8">
    <v-row justify="center">
      <v-col cols="12" md="8" lg="6">
        <v-card class="elevation-2 rounded-lg">
          <v-card-title class="headline primary--text py-4 px-6">
            User Profile
          </v-card-title>
          
          <v-card-text class="pa-6">
            <div class="text-center mb-6">
              <v-avatar size="120" class="mb-4">
                <v-img :src="getUserAvatar()"></v-img>
              </v-avatar>
              <h2 class="text-h4 font-weight-bold">{{ currentUser ? currentUser.username : 'User' }}</h2>
              <v-chip class="mt-2" :color="getRoleColor()" small>
                {{ userRoleText }}
              </v-chip>
            </div>
            
            <v-divider class="my-4"></v-divider>
            
            <v-list>
              <v-list-item>
                <v-list-item-icon>
                  <v-icon color="primary">mdi-account</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>Username</v-list-item-title>
                  <v-list-item-subtitle>{{ currentUser ? currentUser.username : 'Not available' }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              
              <v-list-item>
                <v-list-item-icon>
                  <v-icon color="primary">mdi-email</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>Email</v-list-item-title>
                  <v-list-item-subtitle>{{ currentUser && currentUser.email ? currentUser.email : 'Not available' }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              
              <v-list-item>
                <v-list-item-icon>
                  <v-icon color="primary">mdi-shield-account</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>Role</v-list-item-title>
                  <v-list-item-subtitle>{{ userRoleText }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              
              <v-list-item v-if="isFarmer">
                <v-list-item-icon>
                  <v-icon color="primary">mdi-barn</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>Farm Management</v-list-item-title>
                  <v-list-item-subtitle>
                    <v-btn text color="primary" to="/farmer-dashboard" small>
                      Go to Farm Dashboard
                      <v-icon right small>mdi-arrow-right</v-icon>
                    </v-btn>
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              
              <v-list-item v-if="isAdmin">
                <v-list-item-icon>
                  <v-icon color="primary">mdi-view-dashboard</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>Admin Panel</v-list-item-title>
                  <v-list-item-subtitle>
                    <v-btn text color="primary" to="/admin" small>
                      Go to Admin Dashboard
                      <v-icon right small>mdi-arrow-right</v-icon>
                    </v-btn>
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              
              <v-list-item v-if="isCustomer">
                <v-list-item-icon>
                  <v-icon color="primary">mdi-package-variant-closed</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>Orders</v-list-item-title>
                  <v-list-item-subtitle>
                    <v-btn text color="primary" to="/orders" small>
                      View My Orders
                      <v-icon right small>mdi-arrow-right</v-icon>
                    </v-btn>
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-card-text>
          
          <v-card-actions class="pa-6 pt-0">
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="handleLogout">
              <v-icon left>mdi-logout</v-icon>
              Logout
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'Profile',
  computed: {
    ...mapGetters('auth', ['isAuthenticated', 'currentUser']),
    isAdmin() {
      return this.currentUser && this.currentUser.role === 'ADMIN'
    },
    isFarmer() {
      return this.currentUser && this.currentUser.role === 'FARMER'
    },
    isCustomer() {
      return this.currentUser && this.currentUser.role === 'CUSTOMER'
    },
    userRoleText() {
      if (!this.currentUser) return '';

      if (this.currentUser.role === 'ADMIN') {
        return 'Administrator';
      } else if (this.currentUser.role === 'FARMER') {
        return 'Coffee Farmer';
      } else {
        return 'Regular User';
      }
    }
  },
  methods: {
    ...mapActions('auth', ['logout']),
    getUserAvatar() {
      if (!this.currentUser) return require('@/assets/pic/user.jpg');

      if (this.currentUser.role === 'ADMIN') {
        return require('@/assets/pic/admin.jpg');
      } else if (this.currentUser.role === 'FARMER') {
        return require('@/assets/pic/farmer.jpg');
      } else {
        return require('@/assets/pic/user.jpg');
      }
    },
    getRoleColor() {
      if (!this.currentUser) return 'grey';

      if (this.currentUser.role === 'ADMIN') {
        return 'red';
      } else if (this.currentUser.role === 'FARMER') {
        return 'green';
      } else {
        return 'blue';
      }
    },
    async handleLogout() {
      try {
        await this.logout()
        this.$router.push('/login')
      } catch (error) {
        console.error('Logout failed:', error)
      }
    }
  },
  created() {
    // Redirect to login if not authenticated
    if (!this.isAuthenticated) {
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.profile-page {
  background-color: #f5f5f5;
  min-height: calc(100vh - 64px);
}

.v-avatar {
  border: 3px solid var(--primary-color);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
</style>