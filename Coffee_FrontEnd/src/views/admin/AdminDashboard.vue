<template>
  <div class="admin-dashboard">
    <v-container fluid>
      <!-- Admin Header -->
      <v-row>
        <v-col cols="12">
          <v-card class="pa-4 elevation-1">
            <div class="d-flex align-center">
              <v-avatar size="60" color="primary" class="mr-4">
                <v-icon size="40" color="white">mdi-shield-account</v-icon>
              </v-avatar>
              <div>
                <h1 class="text-h4 font-weight-bold primary--text mb-1">Admin Dashboard</h1>
                <div class="text-subtitle-1 grey--text">
                  Manage content, coffee farmers, and platform data
                </div>
              </div>
              <v-spacer></v-spacer>
              <v-btn
                color="primary"
                class="ml-2"
                @click="refreshData"
                :loading="loading"
              >
                <v-icon left>mdi-refresh</v-icon>
                Refresh Data
              </v-btn>
            </div>
          </v-card>
        </v-col>
      </v-row>

      <!-- Admin Functions -->
      <v-row class="mt-6">
        <v-col cols="12">
          <h2 class="admin-subtitle">Admin Functions</h2>
          <v-divider class="mt-2 mb-4"></v-divider>
        </v-col>

        <!-- Create Article Card -->
        <v-col cols="12" md="4">
          <v-card
            class="admin-card h-100"
            hover
            @click="$router.push('/article/editor')"
          >
            <v-img
              height="150"
              src="@/assets/admin/article.jpg"
              gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
              class="white--text align-end"
            >
              <v-card-title>Create Article</v-card-title>
            </v-img>
            <v-card-text class="text-center pt-4">
              <v-icon size="64" color="green">mdi-newspaper-plus</v-icon>
              <div class="mt-3 subtitle-1">
                Create new coffee-related articles and sustainability content
              </div>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="primary"
                text
                @click.stop="$router.push('/article/editor')"
              >
                Create
                <v-icon right>mdi-arrow-right</v-icon>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>

        <!-- Add Farmer Card -->
        <v-col cols="12" md="4">
          <v-card
            class="admin-card h-100"
            hover
            @click="showCreateFarmerDialog"
          >
            <v-img
              height="150"
              src="@/assets/admin/farmer.jpg"
              gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
              class="white--text align-end"
            >
              <v-card-title>Add Farmer</v-card-title>
            </v-img>
            <v-card-text class="text-center pt-4">
              <v-icon size="64" color="brown">mdi-account-plus</v-icon>
              <div class="mt-3 subtitle-1">
                Create accounts for coffee farmers to join the platform
              </div>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="primary"
                text
                @click.stop="showCreateFarmerDialog"
              >
                Create
                <v-icon right>mdi-arrow-right</v-icon>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>

        <!-- Farm Certification Card -->
        <v-col cols="12" md="3">
          <v-card
            class="admin-card h-100"
            hover
            @click="$router.push('/admin/certifications')"
          >
            <v-img
              height="150"
              src="@/assets/admin/certification.jpg"
              gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
              class="white--text align-end"
            >
              <v-card-title>Farm Certification</v-card-title>
            </v-img>
            <v-card-text class="text-center pt-4">
              <v-icon size="64" color="indigo">mdi-certificate</v-icon>
              <div class="mt-3 subtitle-1">
                Review and approve certification applications from farms
              </div>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="primary"
                text
                @click.stop="$router.push('/admin/certifications')"
              >
                Review
                <v-icon right>mdi-arrow-right</v-icon>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>

        <!-- Financial Support Card -->
        <v-col cols="12" md="3">
          <v-card
            class="admin-card h-100"
            hover
            @click="$router.push('/admin/financial')"
          >
            <v-img
              height="150"
              src="@/assets/admin/financial.jpg"
              gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
              class="white--text align-end"
            >
              <v-card-title>Financial Support</v-card-title>
            </v-img>
            <v-card-text class="text-center pt-4">
              <v-icon size="64" color="success">mdi-cash</v-icon>
              <div class="mt-3 subtitle-1">
                Review and manage financial support applications
              </div>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="success"
                text
                @click.stop="$router.push('/admin/financial')"
              >
                Review
                <v-icon right>mdi-arrow-right</v-icon>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>

        <!-- Technical Training Card -->
        <v-col cols="12" md="4">
          <v-card
            class="admin-card h-100"
            hover
            @click="$router.push('/admin/trainings')"
          >
            <v-img
              height="150"
              src="@/assets/admin/training.jpg"
              gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
              class="white--text align-end"
            >
              <v-card-title>Technical Training</v-card-title>
            </v-img>
            <v-card-text class="text-center pt-4">
              <v-icon size="64" color="blue">mdi-school</v-icon>
              <div class="mt-3 subtitle-1">
                Manage and publish on-farm technical training programs
              </div>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="primary"
                text
                @click.stop="$router.push('/admin/trainings')"
              >
                Review
                <v-icon right>mdi-arrow-right</v-icon>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>

        <!-- Farm Audit Card -->
        <!--<v-col cols="12" md="4">
          <v-card
            class="admin-card h-100"
            hover
            @click="$router.push('/admin/farm-review')"
          >
            <v-img
              height="150"
              src="@/assets/admin/farm.jpg"
              gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
              class="white--text align-end"
            >
              <v-card-title>Farm Audit</v-card-title>
            </v-img>
            <v-card-text class="text-center pt-4">
              <v-icon size="64" color="teal">mdi-clipboard-check</v-icon>
              <div class="mt-3 subtitle-1">
                Review and verify sustainable practices of coffee farms
              </div>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="primary"
                text
                @click.stop="$router.push('/admin/farm-review')"
              >
                Review
                <v-icon right>mdi-arrow-right</v-icon>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col> -->
      </v-row>

      <!-- Platform Statistics -->
      <v-row class="mt-8">
        <v-col cols="12">
          <h2 class="admin-subtitle">Platform Statistics</h2>
          <v-divider class="mt-2 mb-4"></v-divider>
        </v-col>

        <v-col cols="12" md="3">
          <v-card class="stat-card">
            <v-card-text>
              <div class="d-flex align-center">
                <v-avatar
                  color="primary"
                  size="50"
                  class="mr-3"
                >
                  <v-icon color="white">mdi-account-multiple</v-icon>
                </v-avatar>
                <div>
                  <div class="text-h5 font-weight-bold">{{ formatNumber(statistics.usersCount) }}</div>
                  <div class="text-body-2 grey--text">Users</div>
                </div>
              </div>
            </v-card-text>
          </v-card>
        </v-col>

        <v-col cols="12" md="3">
          <v-card class="stat-card">
            <v-card-text>
              <div class="d-flex align-center">
                <v-avatar
                  color="green"
                  size="50"
                  class="mr-3"
                >
                  <v-icon color="white">mdi-newspaper</v-icon>
                </v-avatar>
                <div>
                  <div class="text-h5 font-weight-bold">{{ formatNumber(statistics.articlesCount) }}</div>
                  <div class="text-body-2 grey--text">Articles</div>
                </div>
              </div>
            </v-card-text>
          </v-card>
        </v-col>

        <v-col cols="12" md="3">
          <v-card class="stat-card">
            <v-card-text>
              <div class="d-flex align-center">
                <v-avatar
                  color="brown"
                  size="50"
                  class="mr-3"
                >
                  <v-icon color="white">mdi-account-hard-hat</v-icon>
                </v-avatar>
                <div>
                  <div class="text-h5 font-weight-bold">{{ formatNumber(statistics.farmersCount) }}</div>
                  <div class="text-body-2 grey--text">Farmers</div>
                </div>
              </div>
            </v-card-text>
          </v-card>
        </v-col>

        <v-col cols="12" md="3">
          <v-card class="stat-card">
            <v-card-text>
              <div class="d-flex align-center">
                <v-avatar
                  color="teal"
                  size="50"
                  class="mr-3"
                >
                  <v-icon color="white">mdi-home</v-icon>
                </v-avatar>
                <div>
                  <div class="text-h5 font-weight-bold">{{ formatNumber(statistics.farmsCount) }}</div>
                  <div class="text-body-2 grey--text">Farms</div>
                </div>
              </div>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>

      <!-- Recent Articles -->
      <v-row class="mt-8">
        <v-col cols="12">
          <h2 class="admin-subtitle">Recent Articles</h2>
          <v-divider class="mt-2 mb-4"></v-divider>

          <v-data-table
            :headers="articleHeaders"
            :items="recentArticles"
            :items-per-page="5"
            :loading="loadingArticles"
            class="elevation-1 rounded"
          >
            <template v-slot:item.createdAt="{ item }">
              {{ formatDate(item.createdAt) }}
            </template>

            <template v-slot:item.actions="{ item }">
              <v-btn
                x-small
                icon
                color="primary"
                @click="viewArticle(item)"
              >
                <v-icon>mdi-eye</v-icon>
              </v-btn>
              <v-btn
                x-small
                icon
                color="green"
                @click="editArticle(item)"
              >
                <v-icon>mdi-pencil</v-icon>
              </v-btn>
              <v-btn
                x-small
                icon
                color="red"
                @click="deleteArticle(item)"
              >
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </template>
          </v-data-table>
        </v-col>
      </v-row>

      <!-- Farmers List -->
      <v-row class="mt-8">
        <v-col cols="12">
          <h2 class="admin-subtitle">Registered Farmers</h2>
          <v-divider class="mt-2 mb-4"></v-divider>

          <v-data-table
            :headers="farmerHeaders"
            :items="farmers"
            :items-per-page="5"
            :loading="loadingFarmers"
            class="elevation-1 rounded"
          >
            <template v-slot:item.createdAt="{ item }">
              {{ formatDate(item.createdAt) }}
            </template>

            <template v-slot:item.actions="{ item }">
              <v-btn
                x-small
                icon
                color="primary"
                @click="viewFarmer(item)"
              >
                <v-icon>mdi-eye</v-icon>
              </v-btn>
            </template>
          </v-data-table>
        </v-col>
      </v-row>

      <!-- Farms List -->
      <v-row class="mt-8 mb-6">
        <v-col cols="12">
          <h2 class="admin-subtitle">Registered Farms</h2>
          <v-divider class="mt-2 mb-4"></v-divider>

          <v-data-table
            :headers="farmHeaders"
            :items="farms"
            :items-per-page="5"
            :loading="loadingFarms"
            class="elevation-1 rounded"
          >
            <template v-slot:item.createdAt="{ item }">
              {{ formatDate(item.createdAt) }}
            </template>

            <template v-slot:item.status="{ item }">
              <v-chip
                :color="getFarmStatusColor(item.status)"
                small
              >
                {{ item.status }}
              </v-chip>
            </template>

            <template v-slot:item.actions="{ item }">
              <v-btn
                x-small
                icon
                color="primary"
                @click="viewFarm(item)"
              >
                <v-icon>mdi-eye</v-icon>
              </v-btn>
            </template>
          </v-data-table>
        </v-col>
      </v-row>

      <!-- Create Farmer Dialog -->
      <v-dialog v-model="createFarmerDialog" max-width="500px">
        <v-card>
          <v-card-title class="primary white--text">
            <v-icon left color="white">mdi-account-plus</v-icon>
            Create Farmer Account
          </v-card-title>

          <v-card-text class="pt-4">
            <v-form ref="farmerForm" v-model="farmerFormValid">
              <v-text-field
                v-model="newFarmer.username"
                :rules="usernameRules"
                label="Username"
                required
                outlined
                dense
              ></v-text-field>

              <v-text-field
                v-model="newFarmer.email"
                :rules="emailRules"
                label="Email"
                type="email"
                required
                outlined
                dense
              ></v-text-field>

              <v-text-field
                v-model="newFarmer.password"
                :rules="passwordRules"
                label="Password"
                :type="showPassword ? 'text' : 'password'"
                required
                outlined
                dense
                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                @click:append="showPassword = !showPassword"
              ></v-text-field>

              <v-text-field
                v-model="newFarmer.confirmPassword"
                :rules="[(v) => !!v || 'Please confirm password', (v) => v === newFarmer.password || 'Passwords do not match']"
                label="Confirm Password"
                :type="showConfirmPassword ? 'text' : 'password'"
                required
                outlined
                dense
                :append-icon="showConfirmPassword ? 'mdi-eye' : 'mdi-eye-off'"
                @click:append="showConfirmPassword = !showConfirmPassword"
              ></v-text-field>
            </v-form>
          </v-card-text>

          <v-card-actions class="pb-4 px-4">
            <v-spacer></v-spacer>
            <v-btn
              color="grey"
              text
              @click="closeCreateFarmerDialog"
            >
              Cancel
            </v-btn>
            <v-btn
              color="primary"
              :loading="creatingFarmer"
              :disabled="!farmerFormValid"
              @click="createFarmer"
            >
              Create Account
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-container>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'AdminDashboard',
  data() {
    return {
      articleHeaders: [
        { text: 'ID', value: 'id', width: '80px' },
        { text: 'Title', value: 'title', width: '30%' },
        { text: 'Publish Date', value: 'publishDate' },
      ],
      farmerHeaders: [
        { text: 'ID', value: 'id', width: '80px' },
        { text: 'Username', value: 'username' },
        { text: 'Email', value: 'email' },
        { text: 'Registration Date', value: 'createdAt' },
        { text: 'Actions', value: 'actions', sortable: false, width: '100px' }
      ],
      farmHeaders: [
        { text: 'ID', value: 'id', width: '80px' },
        { text: 'Farm Name', value: 'farmName' },
        { text: 'Location', value: 'location' },
        { text: 'Established Year', value: 'establishedYear' },
        { text: 'Actions', value: 'actions', sortable: false, width: '100px' }
      ],
      recentArticles: [],
      farmers: [],
      farms: [],
      statistics: {
        usersCount: 0,
        articlesCount: 0,
        farmersCount: 0,
        farmsCount: 0
      },
      loading: false,
      loadingArticles: false,
      loadingFarmers: false,
      loadingFarms: false,
      createFarmerDialog: false,
      farmerFormValid: false,
      creatingFarmer: false,
      showPassword: false,
      showConfirmPassword: false,
      newFarmer: {
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
      },
      usernameRules: [
        v => !!v || 'Username is required',
        v => (v && v.length >= 3) || 'Username must be at least 3 characters'
      ],
      emailRules: [
        v => !!v || 'Email is required',
        v => /.+@.+\..+/.test(v) || 'Email must be valid'
      ],
      passwordRules: [
        v => !!v || 'Password is required',
        v => (v && v.length >= 6) || 'Password must be at least 6 characters'
      ],
    }
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      this.loading = true;
      try {
        await Promise.all([
          this.fetchArticles(),
          this.fetchFarmers(),
          this.fetchFarms(),
          this.fetchStatistics()
        ]);
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        this.loading = false;
      }
    },

    async fetchArticles() {
      this.loadingArticles = true;
      try {
        const response = await axios.get('/api/articles');
        if (response.data.code === 200) {
          this.recentArticles = response.data.data;
        } else {
          console.error('Error fetching articles:', response.data.message);
        }
      } catch (error) {
        console.error('Error fetching articles:', (error.response && error.response.data && error.response.data.message) || error.message);
      } finally {
        this.loadingArticles = false;
      }
    },

    async fetchFarmers() {
      this.loadingFarmers = true;
      try {
        const response = await axios.get('/api/admin/farmers');
        if (response.data.code === 200) {
          this.farmers = response.data.data;
        } else {
          console.error('Error fetching farmers:', response.data.message);
        }
      } catch (error) {
        console.error('Error fetching farmers:', (error.response && error.response.data && error.response.data.message) || error.message);
      } finally {
        this.loadingFarmers = false;
      }
    },

    async fetchFarms() {
      this.loadingFarms = true;
      try {
        const response = await axios.get('/api/farms');
        if (response.data.code === 200) {
          this.farms = response.data.data.content;
          // Get blog information for each farm
          for (const farm of this.farms) {
            await this.fetchFarmBlogs(farm.id);
          }
        } else {
          console.error('Error fetching farms:', response.data.message);
        }
      } catch (error) {
        console.error('Error fetching farms:', (error.response && error.response.data && error.response.data.message) || error.message);
      } finally {
        this.loadingFarms = false;
      }
    },

    async fetchFarmBlogs(farmId) {
      try {
        const response = await axios.get(`/api/farms/${farmId}/blogs`);
        if (response.data.code === 200) {
          // Add blog information to the corresponding farm object
          const farmIndex = this.farms.findIndex(farm => farm.id === farmId);
          if (farmIndex !== -1) {
            Vue.set(this.farms[farmIndex], 'blogs', response.data.data);
          }
        }
      } catch (error) {
        console.error(`Error fetching blogs for farm ${farmId}:`, error);
      }
    },

    async fetchStatistics() {
      try {
        // Get user statistics
        const usersResponse = await axios.get('/api/admin/farmers');
        const farmersCount = usersResponse.data.code === 200 ? usersResponse.data.data.length : 0;

        // Get article statistics
        const articlesResponse = await axios.get('/api/articles');
        const articlesCount = articlesResponse.data.code === 200 ? articlesResponse.data.data.length : 0;

        // Get farm statistics
        const farmsResponse = await axios.get('/api/farms');
        const farmsCount = farmsResponse.data.code === 200 ? farmsResponse.data.data.content.length : 0;

        this.statistics = {
          usersCount: farmersCount + 1, // +1 represents admin account
          articlesCount: articlesCount,
          farmersCount: farmersCount,
          farmsCount: farmsCount
        };
      } catch (error) {
        console.error('Error fetching statistics:', (error.response && error.response.data && error.response.data.message) || error.message);
      }
    },

    viewArticle(item) {
      this.$router.push(`/article/${item.id}`);
    },

    editArticle(item) {
      this.$router.push(`/article/editor/${item.id}`);
    },

    async deleteArticle(item) {
      if (confirm('Are you sure you want to delete this article?')) {
        try {
          const response = await axios.delete(`/api/articles/${item.id}`);
          if (response.data.code === 200) {
            this.fetchArticles();
            this.fetchStatistics();
          }
        } catch (error) {
          console.error('Error deleting article:', error);
        }
      }
    },

    viewFarmer(item) {
      // Here can navigate to farmer detail page
      console.log('View farmer:', item);
    },

    viewFarm(item) {
      // Here can navigate to farm detail page
      this.$router.push(`/farms/${item.id}`);
    },

    getFarmStatusColor(status) {
      const statusColors = {
        PENDING: 'warning',
        APPROVED: 'success',
        REJECTED: 'error',
        SUSPENDED: 'grey'
      };
      return statusColors[status] || 'primary';
    },

    refreshData() {
      this.fetchData();
    },

    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleDateString();
    },

    formatNumber(number) {
      return number.toLocaleString();
    },

    showCreateFarmerDialog() {
      this.createFarmerDialog = true;
    },

    closeCreateFarmerDialog() {
      this.createFarmerDialog = false;
      this.$refs.farmerForm.reset();
      this.newFarmer = {
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
      };
    },

    async createFarmer() {
      if (this.$refs.farmerForm.validate()) {
        this.creatingFarmer = true;
        try {
          const response = await axios.post('/api/admin/createFarmer', {
            username: this.newFarmer.username,
            email: this.newFarmer.email,
            password: this.newFarmer.password
          });

          if (response.data.code === 200) {
            // 显示成功消息
            this.$emit('show-snackbar', {
              color: 'success',
              text: 'Farmer account created successfully'
            });

            // 关闭对话框并刷新数据
            this.closeCreateFarmerDialog();
            this.fetchFarmers();
            this.fetchStatistics();
          } else {
            throw new Error(response.data.message);
          }
        } catch (error) {
          this.$emit('show-snackbar', {
            color: 'error',
            text: (error.response && error.response.data && error.response.data.message) || error.message
          });
        } finally {
          this.creatingFarmer = false;
        }
      }
    },
  }
}
</script>

<style scoped>
.admin-dashboard {
  padding: 20px 0;
}

.admin-title {
  font-size: 2rem;
  font-weight: 700;
}

.admin-subtitle {
  font-size: 1.5rem;
  font-weight: 600;
  color: #424242;
}

.admin-card {
  transition: all 0.3s ease;
  border-radius: 8px;
  text-align: center;
}

.admin-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.stat-card {
  border-radius: 8px;
}

.h-100 {
  height: 100%;
}
</style>
