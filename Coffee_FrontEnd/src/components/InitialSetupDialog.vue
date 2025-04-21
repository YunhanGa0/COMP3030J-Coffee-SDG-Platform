<template>
  <v-dialog v-model="show" persistent max-width="800px">
    <v-card>
      <v-card-title class="headline">
        欢迎使用农场管理系统
      </v-card-title>
      <v-card-text>
        <p class="mb-4">看起来这是您第一次登录。请填写以下基本信息来完成农场设置：</p>
        <v-form ref="form" v-model="valid">
          <v-text-field
            v-model="farmData.farmName"
            :rules="[v => !!v || '农场名称是必填项']"
            label="农场名称"
            required
          ></v-text-field>
          
          <v-text-field
            v-model="farmData.country"
            :rules="[v => !!v || '国家是必填项']"
            label="国家"
            required
          ></v-text-field>
          
          <v-text-field
            v-model="farmData.location"
            :rules="[v => !!v || '具体位置是必填项']"
            label="具体位置"
            required
          ></v-text-field>
          
          <v-text-field
            v-model="farmData.size"
            type="number"
            :rules="[v => !!v || '农场面积是必填项']"
            label="农场面积（公顷）"
            required
          ></v-text-field>
          
          <v-text-field
            v-model="farmData.establishedYear"
            type="number"
            :rules="[
              v => !!v || '成立年份是必填项',
              v => (v && v <= new Date().getFullYear()) || '年份不能超过当前年份'
            ]"
            label="成立年份"
            required
          ></v-text-field>
          
          <v-textarea
            v-model="farmData.description"
            :rules="[v => !!v || '农场描述是必填项']"
            label="农场描述"
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
          完成设置
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
          this.showMessage('农场信息设置成功！', 'success')
        } else {
          this.showMessage(response.data.message || '保存失败', 'error')
        }
      } catch (error) {
        console.error('保存农场信息失败:', error)
        this.showMessage('保存农场信息失败', 'error')
      }
    },
    
    showMessage(message, type = 'info') {
      this.$emit('show-message', { message, type })
    }
  }
}
</script> 