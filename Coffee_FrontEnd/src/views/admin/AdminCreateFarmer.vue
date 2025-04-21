<template>
  <div class="admin-create-farmer">
    <v-container>
      <v-row justify="center">
        <v-col cols="12" md="8" lg="6">
          <!-- Page Header -->
          <div class="d-flex align-center mb-6">
            <v-btn 
              icon 
              class="mr-3"
              @click="$router.push('/admin/dashboard')"
            >
              <v-icon>mdi-arrow-left</v-icon>
            </v-btn>
            <h1 class="text-h4 font-weight-bold primary--text">Create Farmer Account</h1>
          </div>
          
          <!-- Farmer Info Card -->
          <v-card class="mb-6 rounded-lg">
            <v-card-title class="primary darken-1 white--text py-3">
              <v-icon left color="white">mdi-account-plus</v-icon>
              Farmer Registration
            </v-card-title>
            
            <v-card-text class="pa-4">
              <div class="subtitle-1 grey--text text-darken-1 mb-4">
                Create a new account for a coffee farmer to join the sustainable coffee platform.
              </div>
              
              <v-form
                ref="form"
                v-model="valid"
                lazy-validation
                @submit.prevent="submitForm"
              >
                <!-- Username Field -->
                <v-text-field
                  v-model="farmer.username"
                  :rules="usernameRules"
                  label="Username"
                  prepend-icon="mdi-account"
                  required
                  outlined
                  dense
                ></v-text-field>
                
                <!-- Email Field -->
                <v-text-field
                  v-model="farmer.email"
                  :rules="emailRules"
                  label="Email"
                  prepend-icon="mdi-email"
                  required
                  outlined
                  dense
                ></v-text-field>
                
                <!-- Password Field -->
                <v-text-field
                  v-model="farmer.password"
                  :rules="passwordRules"
                  :type="showPassword ? 'text' : 'password'"
                  label="Password"
                  prepend-icon="mdi-lock"
                  required
                  outlined
                  dense
                  :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                  @click:append="showPassword = !showPassword"
                ></v-text-field>
                
                <!-- Confirm Password Field -->
                <v-text-field
                  v-model="farmer.confirmPassword"
                  :rules="confirmPasswordRules"
                  :type="showConfirmPassword ? 'text' : 'password'"
                  label="Confirm Password"
                  prepend-icon="mdi-lock-check"
                  required
                  outlined
                  dense
                  :append-icon="showConfirmPassword ? 'mdi-eye' : 'mdi-eye-off'"
                  @click:append="showConfirmPassword = !showConfirmPassword"
                ></v-text-field>
                
                <!-- Farm Name Field -->
                <v-text-field
                  v-model="farmer.farmName"
                  :rules="farmNameRules"
                  label="Farm Name"
                  prepend-icon="mdi-home"
                  required
                  outlined
                  dense
                ></v-text-field>
                
                <!-- Location Field -->
                <v-text-field
                  v-model="farmer.location"
                  :rules="locationRules"
                  label="Farm Location"
                  prepend-icon="mdi-map-marker"
                  required
                  outlined
                  dense
                  hint="Country or region where the farm is located"
                  persistent-hint
                ></v-text-field>
                
                <!-- Description Field -->
                <v-textarea
                  v-model="farmer.description"
                  :rules="descriptionRules"
                  label="Farm Description"
                  prepend-icon="mdi-text-box"
                  required
                  outlined
                  auto-grow
                  rows="3"
                ></v-textarea>
                
                <!-- Submit Button -->
                <v-btn
                  :disabled="!valid || loading"
                  :loading="loading"
                  color="primary"
                  class="mt-4 px-6"
                  x-large
                  type="submit"
                  block
                >
                  <v-icon left>mdi-account-plus</v-icon>
                  Create Farmer Account
                </v-btn>
              </v-form>
            </v-card-text>
          </v-card>
          
          <!-- Information Card -->
          <v-card class="info-card mb-6 rounded-lg">
            <v-card-title class="subtitle-1">
              <v-icon left color="primary">mdi-information</v-icon>
              Important Information
            </v-card-title>
            <v-card-text>
              <ul class="pl-4">
                <li class="mb-2">The farmer will receive an email with login credentials</li>
                <li class="mb-2">Farmers can add farm details and sustainability practices</li>
                <li class="mb-2">Farm information will be reviewed by administrators</li>
                <li>Make sure the farmer understands our sustainability standards</li>
              </ul>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
    
    <!-- Success Snackbar -->
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
import axios from 'axios';

export default {
  name: 'AdminCreateFarmer',
  data() {
    return {
      valid: false,
      loading: false,
      showPassword: false,
      showConfirmPassword: false,
      farmer: {
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        farmName: '',
        location: '',
        description: ''
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
      confirmPasswordRules: [
        v => !!v || 'Please confirm your password',
        v => v === this.farmer.password || 'Passwords do not match'
      ],
      farmNameRules: [
        v => !!v || 'Farm name is required'
      ],
      locationRules: [
        v => !!v || 'Location is required'
      ],
      descriptionRules: [
        v => !!v || 'Description is required',
        v => (v && v.length >= 20) || 'Description must be at least 20 characters'
      ],
      snackbar: {
        show: false,
        text: '',
        color: 'success'
      }
    };
  },
  methods: {
    async submitForm() {
      if (this.$refs.form.validate()) {
        this.loading = true;
        
        try {
          const formData = {
            username: this.farmer.username,
            email: this.farmer.email,
            password: this.farmer.password,
            role: 'FARMER',
            farmInfo: {
              name: this.farmer.farmName,
              location: this.farmer.location,
              description: this.farmer.description
            }
          };
          
          const response = await axios.post('/api/admin/create-farmer', formData);
          
          if (response.data.code === 200) {
            this.snackbar.color = 'success';
            this.snackbar.text = 'Farmer account created successfully!';
            this.snackbar.show = true;
            this.$refs.form.reset();
            this.resetForm();
          } else {
            throw new Error(response.data.message || 'Failed to create farmer');
          }
        } catch (error) {
          this.snackbar.color = 'error';
          this.snackbar.text = `Error: ${(error.response && error.response.data && error.response.data.message) || error.message || 'Unknown error'}`;
          this.snackbar.show = true;
          console.error('Error creating farmer:', error);
        } finally {
          this.loading = false;
        }
      }
    },
    
    resetForm() {
      this.farmer = {
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        farmName: '',
        location: '',
        description: ''
      };
    }
  }
};
</script>

<style scoped>
.admin-create-farmer {
  padding: 20px 0;
}

.info-card {
  border-left: 4px solid #4CAF50;
  background-color: #F9FFF9;
}
</style> 