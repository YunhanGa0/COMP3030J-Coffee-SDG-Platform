<template>
  <div class="article-editor">
    <v-container>
      <v-row>
        <v-col cols="12">
          <v-card class="pa-6">
            <v-text-field
              v-model="articleTitle"
              label="Article Title"
              outlined
              dense
              class="mb-4"
            ></v-text-field>

            <v-text-field
              v-model="articleSummary"
              label="Article Summary"
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
                color="primary"
                :loading="loading"
                @click="handleSave"
              >
                Save Article
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
  name: 'ArticleEditor',
  data() {
    return {
      editor: null,
      articleTitle: '',
      articleSummary: '',
      imageUrl: '',
      imageFile: null,
      articleContent: '<p>Please enter article content...</p>',
      loading: false,
      snackbar: {
        show: false,
        text: '',
        color: 'success'
      }
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
    initEditor() {
      this.editor = new E(this.$refs.editor)

      this.editor.config.height = 500
      this.editor.config.placeholder = 'Please enter content...'

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
          console.error('Image upload error:', error)
          this.showMessage('Image upload failed', 'error')
        }
      }

      this.editor.config.onchange = (html) => {
        this.articleContent = html
      }

      this.editor.create()
      this.editor.txt.html(this.articleContent)
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
        console.error('Image upload error:', error);
        let errorMessage = 'File might be too large';
        if (error.response && error.response.data && error.response.data.message) {
          errorMessage = error.response.data.message;
        }
        this.showMessage('Image upload failed: ' + errorMessage, 'error');
      }
    },

    async handleSave() {
      if (!this.validateForm()) {
        return
      }

      this.loading = true
      try {
        const articleData = {
          title: this.articleTitle,
          summary: this.articleSummary,
          content: this.editor.txt.html(),
          imageUrl: this.imageUrl
        }

        const response = await axios.post('/api/articles', articleData)

        if (response.data.code === 200) {
          this.showMessage('Article saved successfully', 'success')
          setTimeout(() => {
            this.$router.push('/')
          }, 1500)
        } else {
          this.showMessage(response.data.message || 'Save failed', 'error')
        }
      } catch (error) {
        console.error('Save article error:', error)
        const errorMessage = error.response && error.response.data && error.response.data.message
          ? error.response.data.message
          : 'Save failed'
        this.showMessage(errorMessage, 'error')
      } finally {
        this.loading = false
      }
    },

    validateForm() {
      if (!this.articleTitle.trim()) {
        this.showMessage('Please enter article title', 'error')
        return false
      }
      if (!this.articleSummary.trim()) {
        this.showMessage('Please enter article summary', 'error')
        return false
      }
      if (!this.imageUrl.trim()) {
        this.showMessage('Please upload cover image', 'error')
        return false
      }
      if (!this.editor.txt.html().trim()) {
        this.showMessage('Please enter article content', 'error')
        return false
      }
      return true
    },

    handleCancel() {
      this.$router.go(-1)
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
.article-editor {
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
