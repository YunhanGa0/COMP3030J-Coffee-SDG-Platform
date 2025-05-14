<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title class="d-flex justify-space-between align-center">
            我的咖啡豆
            <v-btn color="primary" @click="openCreateDialog">
              添加咖啡豆
            </v-btn>
          </v-card-title>
          
          <v-card-text>
            <v-data-table
              :headers="headers"
              :items="coffeeBeans"
              :loading="loading"
              class="elevation-1"
            >
              <template v-slot:item.imageUrl="{ item }">
                <v-img
                  :src="item.imageUrl"
                  max-width="100"
                  max-height="100"
                  contain
                ></v-img>
              </template>

              <template v-slot:item.actions="{ item }">
                <v-btn
                  small
                  text
                  color="primary"
                  @click="viewItemDetails(item)"
                >
                  查看详情
                </v-btn>
                <v-btn
                  small
                  text
                  color="warning"
                  @click="editItem(item)"
                >
                  编辑
                </v-btn>
                <v-btn
                  small
                  text
                  color="error"
                  @click="confirmDelete(item)"
                >
                  删除
                </v-btn>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- 创建/编辑对话框 -->
    <v-dialog v-model="dialog" max-width="800px">
      <v-card>
        <v-card-title>
          <span class="text-h5">{{ formTitle }}</span>
        </v-card-title>

        <v-card-text>
          <v-form ref="form" v-model="valid">
            <v-row>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="editedItem.name"
                  label="咖啡豆名称"
                  :rules="[v => !!v || '名称不能为空']"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="editedItem.variety"
                  label="品种"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="editedItem.processMethod"
                  label="处理方法"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="editedItem.roastLevel"
                  label="烘焙度"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-textarea
                  v-model="editedItem.flavorNotes"
                  label="风味描述"
                  rows="3"
                ></v-textarea>
              </v-col>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model.number="editedItem.weightPerBagKg"
                  label="每包重量(kg)"
                  type="number"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model.number="editedItem.bagStock"
                  label="库存数量"
                  type="number"
                  :rules="[v => !!v || '库存不能为空']"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model.number="editedItem.pricePerBag"
                  label="每包价格"
                  type="number"
                  :rules="[v => !!v || '价格不能为空']"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-switch
                  v-model="editedItem.available"
                  label="是否上架"
                ></v-switch>
              </v-col>
              <v-col cols="12" md="6">
                <v-switch
                  v-model="editedItem.limitedEdition"
                  label="限量版"
                ></v-switch>
              </v-col>
              <v-col cols="12">
                <div class="image-upload-container">
                  <div v-if="!editedItem.imageUrl && !imageFile" class="upload-placeholder">
                    <v-btn
                      color="primary"
                      outlined
                      @click="$refs.imageInput.click()"
                    >
                      <v-icon left>mdi-image-plus</v-icon>
                      选择咖啡豆图片
                    </v-btn>
                    <input
                      ref="imageInput"
                      type="file"
                      accept="image/*"
                      class="d-none"
                      @change="onImageSelected"
                    >
                  </div>
                  
                  <div v-else class="image-preview-container">
                    <v-img
                      :src="imagePreview || editedItem.imageUrl"
                      max-height="250"
                      contain
                      class="coffee-bean-image rounded"
                    >
                      <template v-slot:placeholder>
                        <v-row class="fill-height ma-0" align="center" justify="center">
                          <v-progress-circular indeterminate color="primary"></v-progress-circular>
                        </v-row>
                      </template>
                    </v-img>
                    
                    <div class="image-actions">
                      <v-btn
                        icon
                        color="primary"
                        @click="$refs.imageInput.click()"
                        class="mx-1"
                      >
                        <v-icon>mdi-pencil</v-icon>
                      </v-btn>
                      <v-btn
                        icon
                        color="error"
                        @click="clearImage"
                        class="mx-1"
                      >
                        <v-icon>mdi-delete</v-icon>
                      </v-btn>
                    </div>
                    
                    <input
                      ref="imageInput"
                      type="file"
                      accept="image/*"
                      class="d-none"
                      @change="onImageSelected"
                    >
                  </div>
                  
                  <v-progress-linear
                    v-if="uploadingImage"
                    indeterminate
                    color="primary"
                    class="mt-2"
                  ></v-progress-linear>
                </div>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey darken-1" text @click="closeDialog" :disabled="loading">取消</v-btn>
          <v-btn 
            color="primary" 
            text 
            @click="save" 
            :disabled="!valid || loading" 
            :loading="loading"
          >
            保存
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 删除确认对话框 -->
    <v-dialog v-model="deleteDialog" max-width="400px">
      <v-card>
        <v-card-title class="text-h5">确认删除</v-card-title>
        <v-card-text>
          确定要删除这个咖啡豆商品吗？此操作不可撤销。
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey darken-1" text @click="deleteDialog = false">取消</v-btn>
          <v-btn color="error" text @click="deleteItem">确认删除</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 详情对话框 -->
    <v-dialog v-model="detailDialog" max-width="800px">
      <v-card>
        <v-card-title class="d-flex justify-space-between align-center">
          <span>咖啡豆详情</span>
          <div>
            <v-btn
              color="warning"
              class="mr-2"
              @click="editItem(viewedItem)"
            >
              编辑
            </v-btn>
            <v-btn
              color="error"
              @click="confirmDelete(viewedItem)"
            >
              删除
            </v-btn>
          </div>
        </v-card-title>

        <v-card-text>
          <v-row>
            <v-col cols="12" md="4">
              <v-img
                :src="viewedItem.imageUrl"
                max-height="300"
                contain
                class="grey lighten-2"
              ></v-img>
            </v-col>
            
            <v-col cols="12" md="8">
              <v-list>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>名称</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.name }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>品种</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.variety }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>处理方法</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.processMethod }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>烘焙度</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.roastLevel }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>风味描述</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.flavorNotes }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>每包重量</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.weightPerBagKg }} kg</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>库存数量</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.bagStock }} 包</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>每包价格</v-list-item-subtitle>
                    <v-list-item-title>¥{{ viewedItem.pricePerBag }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>状态</v-list-item-subtitle>
                    <v-list-item-title>
                      <v-chip
                        :color="viewedItem.available ? 'success' : 'error'"
                        small
                      >
                        {{ viewedItem.available ? '已上架' : '已下架' }}
                      </v-chip>
                    </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>限量版</v-list-item-subtitle>
                    <v-list-item-title>
                      <v-chip
                        :color="viewedItem.limitedEdition ? 'warning' : 'grey'"
                        small
                      >
                        {{ viewedItem.limitedEdition ? '是' : '否' }}
                      </v-chip>
                    </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
              </v-list>
            </v-col>
          </v-row>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="detailDialog = false">关闭</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 添加到组件模板底部 -->
    <v-snackbar
      v-model="snackbar.show"
      :color="snackbar.color"
      :timeout="snackbar.timeout"
      top
    >
      {{ snackbar.text }}
      <template v-slot:action="{ attrs }">
        <v-btn
          text
          v-bind="attrs"
          @click="snackbar.show = false"
        >
          关闭
        </v-btn>
      </template>
    </v-snackbar>
  </v-container>
</template>

<script>
import { coffeeBeanApi } from '@/api/coffeeBean'
import axios from 'axios'

export default {
  name: 'CoffeeBeanList',
  
  data: () => ({
    loading: false,
    dialog: false,
    deleteDialog: false,
    valid: false,
    headers: [
      { text: '图片', value: 'imageUrl', sortable: false },
      { text: '名称', value: 'name' },
      { text: '品种', value: 'variety' },
      { text: '处理方法', value: 'processMethod' },
      { text: '库存', value: 'bagStock' },
      { text: '价格', value: 'pricePerBag' },
      { text: '状态', value: 'available' },
      { text: '操作', value: 'actions', sortable: false }
    ],
    coffeeBeans: [],
    editedIndex: -1,
    editedItem: {
      name: '',
      variety: '',
      processMethod: '',
      roastLevel: '',
      flavorNotes: '',
      weightPerBagKg: 0,
      bagStock: 0,
      pricePerBag: 0,
      available: true,
      limitedEdition: false,
      imageUrl: ''
    },
    defaultItem: {
      name: '',
      variety: '',
      processMethod: '',
      roastLevel: '',
      flavorNotes: '',
      weightPerBagKg: 0,
      bagStock: 0,
      pricePerBag: 0,
      available: true,
      limitedEdition: false,
      imageUrl: ''
    },
    detailDialog: false,
    viewedItem: {},
    imageFile: null,
    imagePreview: null,
    uploadingImage: false,
    snackbar: {
      show: false,
      text: '',
      color: 'success',
      timeout: 3000
    }
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? '添加咖啡豆' : '编辑咖啡豆'
    }
  },

  created() {
    this.initialize()
  },

  methods: {
    async initialize() {
      this.loading = true
      try {
        const response = await coffeeBeanApi.getCoffeeBeansList()
        this.coffeeBeans = response.data.data
      } catch (error) {
        this.snackbar = {
          show: true,
          text: '获取咖啡豆列表失败',
          color: 'error',
          timeout: 3000
        }
      } finally {
        this.loading = false
      }
    },

    openCreateDialog() {
      this.editedIndex = -1
      this.editedItem = Object.assign({}, this.defaultItem)
      this.dialog = true
    },

    editItem(item) {
      this.editedIndex = this.coffeeBeans.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.imagePreview = null; // 清空预览，使用现有图片
      this.dialog = true
    },

    onImageSelected(event) {
      const file = event.target.files[0];
      if (!file) return;
      
      if (file.size > 5000000) { // 5MB 限制
        this.snackbar = {
          show: true,
          text: '图片太大，请选择小于 5MB 的图片',
          color: 'error',
          timeout: 3000
        }
        return;
      }
      
      this.imageFile = file;
      
      // 立即创建本地预览
      const reader = new FileReader();
      reader.onload = e => {
        this.imagePreview = e.target.result;
      };
      reader.readAsDataURL(file);
      
      // 自动上传
      this.uploadImage();
    },
    
    clearImage() {
      this.imageFile = null;
      this.imagePreview = null;
      this.editedItem.imageUrl = '';
    },
    
    async uploadImage() {
      if (!this.imageFile) return;
      
      try {
        this.uploadingImage = true;
        
        // 压缩图片
        const compressedFile = await this.compressImage(this.imageFile);
        
        const formData = new FormData();
        formData.append('image', compressedFile);
        formData.append('type', 'COFFEE_BEAN');
        
        const response = await axios.post('/api/articles/images/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        
        if (response.data.code === 200) {
          this.editedItem.imageUrl = response.data.data.url;
          this.snackbar = {
            show: true,
            text: '图片上传成功，请点击保存完成操作',
            color: 'success',
            timeout: 3000
          }
        } else {
          throw new Error(response.data.message || '上传失败');
        }
      } catch (error) {
        console.error('图片上传错误:', error);
        
        // 清除预览，因为上传失败
        this.imagePreview = null;
        
        if (error.response && error.response.status === 401) {
          this.snackbar = {
            show: true,
            text: '登录会话已过期，请重新登录',
            color: 'error',
            timeout: 3000
          }
        } else {
          this.snackbar = {
            show: true,
            text: '图片上传失败: ' + ((error.response && error.response.data && error.response.data.message) || error.message || '未知错误'),
            color: 'error',
            timeout: 3000
          }
        }
      } finally {
        this.uploadingImage = false;
      }
    },
    
    async compressImage(file) {
      return new Promise((resolve) => {
        const reader = new FileReader();
        reader.onload = (event) => {
          const img = new Image();
          img.onload = () => {
            const canvas = document.createElement('canvas');
            let width = img.width;
            let height = img.height;
            
            // 如果图片尺寸太大，进行缩放
            if (width > 1200) {
              height = Math.round(height * 1200 / width);
              width = 1200;
            }
            
            canvas.width = width;
            canvas.height = height;
            
            const ctx = canvas.getContext('2d');
            ctx.drawImage(img, 0, 0, width, height);
            
            // 转换为Blob
            canvas.toBlob((blob) => {
              // 创建一个新的文件对象
              const compressedFile = new File([blob], file.name, {
                type: 'image/jpeg',
                lastModified: Date.now()
              });
              resolve(compressedFile);
            }, 'image/jpeg', 0.7); // 压缩质量为0.7
          };
          img.src = event.target.result;
        };
        reader.readAsDataURL(file);
      });
    },

    async save() {
      if (!this.$refs.form.validate()) return;
      
      this.loading = true;
      
      try {
        let response;
        if (this.editedIndex > -1) {
          // 更新现有咖啡豆
          response = await coffeeBeanApi.updateCoffeeBean(this.editedItem.id, this.editedItem);
          
          // 更新成功，直接关闭对话框
          this.dialog = false;
          
          // 然后更新本地数据和显示成功消息
          const index = this.coffeeBeans.findIndex(item => item.id === this.editedItem.id);
          if (index !== -1) {
            Object.assign(this.coffeeBeans[index], response.data.data || this.editedItem);
          }
          
          this.snackbar = {
            show: true,
            text: '咖啡豆更新成功',
            color: 'success',
            timeout: 3000
          }
        } else {
          // 创建新咖啡豆
          response = await coffeeBeanApi.createCoffeeBean(this.editedItem);
          
          // 创建成功，直接关闭对话框
          this.dialog = false;
          
          // 然后更新列表和显示成功消息
          if (response.data && response.data.data) {
            this.coffeeBeans.unshift(response.data.data);
          }
          
          this.snackbar = {
            show: true,
            text: '咖啡豆创建成功',
            color: 'success',
            timeout: 3000
          }
        }
        
        // 重要：在对话框关闭后重置表单
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem);
          this.editedIndex = -1;
          this.imagePreview = null;
          this.imageFile = null;
          if (this.$refs.form) {
            this.$refs.form.reset();
          }
          
          // 刷新列表
          this.initialize();
        });
      } catch (error) {
        console.error('保存失败:', error);
        let errorMessage = '操作失败';
        
        if (error.response && error.response.data && error.response.data.message) {
          errorMessage = error.response.data.message;
        }
        
        this.snackbar = {
          show: true,
          text: this.editedIndex > -1 ? `更新失败: ${errorMessage}` : `创建失败: ${errorMessage}`,
          color: 'error',
          timeout: 3000
        }
      } finally {
        this.loading = false;
      }
    },

    confirmDelete(item) {
      this.editedItem = Object.assign({}, item)
      this.deleteDialog = true
    },

    async deleteItem() {
      try {
        await coffeeBeanApi.deleteCoffeeBean(this.editedItem.id);
        const index = this.coffeeBeans.indexOf(this.editedItem);
        this.coffeeBeans.splice(index, 1);
        
        this.snackbar = {
          show: true,
          text: '咖啡豆删除成功',
          color: 'success',
          timeout: 3000
        };
        
      } catch (error) {
        this.snackbar = {
          show: true,
          text: '删除失败',
          color: 'error',
          timeout: 3000
        };
      } finally {
        this.deleteDialog = false;
      }
    },

    closeDialog() {
      this.dialog = false;
      this.imagePreview = null;
      this.imageFile = null;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
        this.$refs.form.reset();
      });
    },

    viewItemDetails(item) {
      this.viewedItem = Object.assign({}, item)
      this.detailDialog = true
    },
  }
}
</script>

<style scoped>
.v-data-table {
  width: 100%;
}

.image-upload-container {
  border: 2px dashed #e0e0e0;
  border-radius: 8px;
  padding: 16px;
  position: relative;
}

.upload-placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 150px;
}

.image-preview-container {
  position: relative;
}

.coffee-bean-image {
  border-radius: 8px;
}

.image-actions {
  display: flex;
  justify-content: center;
  margin-top: 8px;
}
</style> 