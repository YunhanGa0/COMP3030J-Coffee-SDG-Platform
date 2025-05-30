<template>
  <div class="blog-editor">
    <v-container>
      <v-row>
        <v-col cols="12">
          <v-card class="pa-6">
            <div class="d-flex align-center mb-6">
              <h2 class="text-h5 font-weight-bold primary--text">
                {{ isEditing ? 'Edit Blog' : 'Create New Blog' }}
              </h2>
              <div class="ml-auto">
                <v-chip v-if="isEditing" small :color="blogPublished ? 'success' : 'grey'" class="mr-2">
                  {{ blogPublished ? 'Published' : 'Draft' }}
                </v-chip>
                <v-chip small color="primary" class="ml-2">Farm: {{ farmName }}</v-chip>
              </div>
            </div>

            <v-text-field
              v-model="blogTitle"
              label="Blog Title"
              outlined
              dense
              class="mb-4"
            ></v-text-field>

            <v-text-field
              v-model="blogSummary"
              label="Blog Summary"
              outlined
              dense
              class="mb-4"
            ></v-text-field>

            <v-text-field
              v-model="imageUrl"
              label="Cover Image URL"
              outlined
              dense
              class="mb-4"
            ></v-text-field>

            <!-- 图片上传 -->
            <v-file-input
              v-model="imageFile"
              accept="image/*"
              label="Upload Cover Image"
              prepend-icon="mdi-camera"
              @change="handleImageUpload"
              class="mb-4"
            ></v-file-input>
            
            <!-- 编辑器容器 -->
            <div ref="editor" class="editor-container"></div>

            <!-- 操作按钮 -->
            <div class="d-flex justify-end mt-6">
              <v-btn
                text
                color="grey"
                class="mr-4"
                @click="handleCancel"
              >
                Cancel
              </v-btn>
              <v-btn
                outlined
                color="grey darken-1"
                class="mr-4"
                :loading="loading"
                @click="handleSave(false)"
              >
                Save as Draft
              </v-btn>
              <v-btn
                color="success"
                :loading="loading"
                @click="handleSave(true)"
              >
                {{ isEditing ? 'Update and Publish' : 'Save and Publish' }}
              </v-btn>
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

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
  </div>
</template>

<script>
import E from 'wangeditor'
import axios from 'axios'

export default {
  name: 'BlogEditor',
  data() {
    return {
      editor: null,
      blogTitle: '',
      blogSummary: '',
      imageUrl: '',
      imageFile: null,
      blogContent: '<p>Please enter blog content...</p>',
      loading: false,
      farmId: null,
      blogId: null,
      isEditing: false,
      blogPublished: false,
      farmName: 'Loading...',
      snackbar: {
        show: false,
        text: '',
        color: 'success'
      }
    }
  },
  created() {
    // 从URL参数获取农场ID和博客ID
    this.farmId = this.$route.query.farmId
    this.blogId = this.$route.query.blogId
    
    // 如果有博客ID，则表示编辑模式
    this.isEditing = !!this.blogId
    
    // 初始化数据
    this.fetchFarmInfo()
    
    if (this.isEditing) {
      this.fetchBlogData()
    }
  },
  mounted() {
    this.initEditor()
  },
  beforeDestroy() {
    if (this.editor) {
      this.editor.destroy()
    }
  },
  methods: {
    // 获取农场信息
    async fetchFarmInfo() {
      if (!this.farmId) {
        this.showMessage('Missing farm ID parameter', 'error')
        setTimeout(() => this.$router.push('/farmer-dashboard'), 2000)
        return
      }

      try {
        const response = await axios.get(`/api/farms/${this.farmId}`)
        
        if (response.data.code === 200) {
          const farmData = response.data.data
          this.farmName = farmData.farmName
          document.title = `${this.isEditing ? 'Edit' : 'Create'} Blog - ${farmData.farmName}`
        } else {
          this.showMessage('Failed to get farm information', 'error')
        }
      } catch (error) {
        console.error('Failed to get farm information:', error)
        this.showMessage('Failed to get farm information', 'error')
      }
    },
    
    // 获取博客详情
    async fetchBlogData() {
      try {
        const response = await axios.get(`/api/farms/${this.farmId}/blogs/${this.blogId}`)
        
        if (response.data.code === 200) {
          const blog = response.data.data
          this.blogTitle = blog.title
          this.blogSummary = blog.summary
          this.imageUrl = blog.coverImageUrl
          this.blogContent = blog.content
          this.blogPublished = blog.published
          
          // 如果编辑器已初始化，设置内容
          if (this.editor) {
            this.editor.txt.html(this.blogContent)
          }
        } else {
          this.showMessage('Failed to get blog details', 'error')
        }
      } catch (error) {
        console.error('Failed to get blog details:', error)
        this.showMessage('Failed to get blog details', 'error')
      }
    },
    
    initEditor() {
      this.editor = new E(this.$refs.editor)

      this.editor.config.height = 500
      this.editor.config.placeholder = 'Please enter blog content...'
      
      this.editor.config.menus = [
        'head',
        'bold',
        'fontSize',
        'fontName',
        'italic',
        'underline',
        'strikeThrough',
        'indent',
        'lineHeight',
        'foreColor',
        'backColor',
        'link',
        'list',
        'justify',
        'quote',
        'image',
        'table',
        'code',
        'undo',
        'redo'
      ]

      // 使用axios处理图片上传
      this.editor.config.customUploadImg = async (resultFiles, insertImgFn) => {
        try {
          const formData = new FormData()
          formData.append('image', resultFiles[0])
          formData.append('type', 'CONTENT')
          
          const response = await axios.post('/api/articles/images/upload', formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          
          if (response.data.code === 200 && response.data.data.url) {
            insertImgFn(response.data.data.url)
          } else {
            this.showMessage('Image upload failed', 'error')
          }
        } catch (error) {
          console.error('Image upload failed:', error)
          this.showMessage('Image upload failed', 'error')
        }
      }

      this.editor.config.onchange = (html) => {
        this.blogContent = html
      }

      this.editor.create()
      
      // 如果已有内容，设置编辑器内容
      if (this.blogContent) {
        this.editor.txt.html(this.blogContent)
      }
    },

    async compressImage(file) {
      return new Promise((resolve) => {
        const reader = new FileReader();
        reader.onload = (e) => {
          const img = new Image();
          img.onload = () => {
            const canvas = document.createElement('canvas');
            let width = img.width;
            let height = img.height;
            
            // 如果图片大于1200px，按比例缩小
            if (width > 1200) {
              height = Math.floor(height * (1200 / width));
              width = 1200;
            }

            canvas.width = width;
            canvas.height = height;
            
            const ctx = canvas.getContext('2d');
            ctx.drawImage(img, 0, 0, width, height);
            
            // 转换为blob
            canvas.toBlob((blob) => {
              resolve(new File([blob], file.name, {
                type: 'image/jpeg',
                lastModified: Date.now()
              }));
            }, 'image/jpeg', 0.8); // 压缩质量0.8
          };
          img.src = e.target.result;
        };
        reader.readAsDataURL(file);
      });
    },

    async handleImageUpload(file) {
      if (!file) return;
      
      if (file.size > 2000000) {
        this.showMessage('Image size cannot exceed 2MB', 'error');
        return;
      }

      try {
        // 压缩图片
        const compressedFile = await this.compressImage(file);
        
        const formData = new FormData();
        formData.append('image', compressedFile);
        formData.append('type', 'COVER');

        const response = await axios.post('/api/articles/images/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });

        if (response.data.code === 200) {
          this.imageUrl = response.data.data.url;
          this.showMessage('Image uploaded successfully', 'success');
        } else {
          this.showMessage('Image upload failed: ' + response.data.message, 'error');
        }
      } catch (error) {
        console.error('Image upload failed:', error);
        let errorMessage = 'File may be too large';
        if (error.response && error.response.data && error.response.data.message) {
          errorMessage = error.response.data.message;
        }
        this.showMessage('Image upload failed: ' + errorMessage, 'error');
      }
    },

    async handleSave(published) {
      if (!this.validateForm()) {
        return
      }

      this.loading = true
      try {
        const blogData = {
          title: this.blogTitle,
          summary: this.blogSummary,
          content: this.editor.txt.html(),
          coverImageUrl: this.imageUrl,
          published: published
        }

        let response
        
        if (this.isEditing) {
          // 更新现有博客
          response = await axios.put(`/api/farms/${this.farmId}/blogs/${this.blogId}`, blogData)
        } else {
          // 创建新博客
          response = await axios.post(`/api/farms/${this.farmId}/blogs`, blogData)
        }
        
        if (response.data.code === 200) {
          this.showMessage(`Blog ${this.isEditing ? 'updated' : 'created'} successfully${published ? ' and published' : ''}`, 'success')
          setTimeout(() => {
            this.$router.push('/farmer-dashboard')
          }, 1500)
        } else {
          this.showMessage(response.data.message || `${this.isEditing ? 'Update' : 'Creation'} failed`, 'error')
        }
      } catch (error) {
        console.error(`${this.isEditing ? 'Update' : 'Creation'} blog failed:`, error)
        const errorMessage = error.response && error.response.data && error.response.data.message 
          ? error.response.data.message 
          : `${this.isEditing ? 'Update' : 'Creation'} failed`
        this.showMessage(errorMessage, 'error')
      } finally {
        this.loading = false
      }
    },

    validateForm() {
      if (!this.blogTitle.trim()) {
        this.showMessage('Please enter a blog title', 'error')
        return false
      }
      if (!this.blogSummary.trim()) {
        this.showMessage('Please enter a blog summary', 'error')
        return false
      }
      if (!this.imageUrl.trim()) {
        this.showMessage('Please upload a cover image', 'error')
        return false
      }
      if (!this.editor.txt.html().trim() || this.editor.txt.html() === '<p>Please enter blog content...</p>') {
        this.showMessage('Please enter blog content', 'error')
        return false
      }
      return true
    },

    handleCancel() {
      this.$router.push('/farmer-dashboard')
    },

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

<style lang="scss" scoped>
.blog-editor {
  padding: 20px 0;
}

.editor-container {
  border: 1px solid #ccc;
  border-radius: 4px;
  margin: 20px 0;
}

:deep(.w-e-text-container) {
  min-height: 500px !important;
  background-color: #fff;
}

:deep(.w-e-toolbar) {
  border-bottom: 1px solid #eee !important;
  background-color: #fafafa !important;
}

:deep(.w-e-text) {
  padding: 20px !important;
}

:deep(.w-e-menu:hover) {
  background-color: #f1f1f1 !important;
  color: #00704A !important;
}

:deep(.w-e-text p) {
  margin: 10px 0;
}
</style>