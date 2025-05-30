<template>
  <v-dialog v-model="show" persistent max-width="800px">
    <v-card>
      <v-card-title class="headline">
        Welcome to Farm Management System
      </v-card-title>
      <v-card-text>
        <p class="mb-4">It looks like this is your first login. Please fill in the following basic information to complete your farm setup:</p>
        <v-form ref="form" v-model="valid">
          <v-text-field
            v-model="farmData.farmName"
            :rules="[v => !!v || 'Farm name is required']"
            label="Farm Name"
            required
          ></v-text-field>
          
          <v-text-field
            v-model="farmData.country"
            :rules="[v => !!v || 'Country is required']"
            label="Country"
            required
          ></v-text-field>
          
          <v-text-field
            v-model="farmData.location"
            :rules="[v => !!v || 'Location is required']"
            label="Location"
            required
          ></v-text-field>
          
          <v-text-field
            v-model="farmData.size"
            type="number"
            :rules="[v => !!v || 'Farm area is required']"
            label="Farm Area (hectares)"
            required
          ></v-text-field>
          
          <v-text-field
            v-model="farmData.establishedYear"
            type="number"
            :rules="[
              v => !!v || 'Establishment year is required',
              v => (v && v <= new Date().getFullYear()) || 'Year cannot exceed current year'
            ]"
            label="Establishment Year"
            required
          ></v-text-field>
          
          <v-textarea
            v-model="farmData.description"
            :rules="[v => !!v || 'Farm description is required']"
            label="Farm Description"
            required
          ></v-textarea>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
          color="primary"
          :disabled="!valid"
          @click="saveProfile"
        >
          Complete Setup
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from 'axios'

export default {
  name: 'InitialSetupDialog',
  
  props: {
    show: {
      type: Boolean,
      required: true
    }
  },
  
  data: () => ({
    valid: false,
    farmData: {
      farmName: '',
      country: '',
      location: '',
      size: null,
      establishedYear: null,
      description: ''
    }
  }),
  
  methods: {
    async saveProfile() {
      if (!this.$refs.form.validate()) return
      
      try {
        const response = await axios.post('/api/farms/profile', this.farmData)
        
        if (response.data.code === 200) {
          this.$emit('setup-completed')
          this.$emit('update:show', false)
          this.showMessage('Farm information set successfully!', 'success')
        } else {
          this.showMessage(response.data.message || 'Save failed', 'error')
        }
      } catch (error) {
        console.error('Failed to save farm information:', error)
        this.showMessage('Failed to save farm information', 'error')
      }
    },
    
    showMessage(message, type = 'info') {
      this.$emit('show-message', { message, type })
    }
  }
}
</script>