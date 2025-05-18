<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title class="d-flex justify-space-between align-center">
            My Coffee Beans
            <v-btn color="primary" @click="openCreateDialog">
              Add Coffee Bean
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
                  Details
                </v-btn>
                <v-btn
                  small
                  text
                  color="warning"
                  @click="editItem(item)"
                >
                  Edit
                </v-btn>
                <v-btn
                  small
                  text
                  color="error"
                  @click="confirmDelete(item)"
                >
                  Delete
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
                  label="Coffee Name"
                  :rules="[v => !!v || 'The name cannot be empty']"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="editedItem.variety"
                  label="Varieties"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="editedItem.processMethod"
                  label="Solution"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="editedItem.roastLevel"
                  label="Roast degree"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-textarea
                  v-model="editedItem.flavorNotes"
                  label="Flavor description"
                  rows="3"
                ></v-textarea>
              </v-col>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model.number="editedItem.weightPerBagKg"
                  label="Weight per pack(kg)"
                  type="number"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model.number="editedItem.bagStock"
                  label="Inventory quantity"
                  type="number"
                  :rules="[v => !!v || 'Inventory cannot be empty']"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model.number="editedItem.pricePerBag"
                  label="Price per pack"
                  type="number"
                  :rules="[v => !!v || 'The price cannot be empty']"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-switch
                  v-model="editedItem.available"
                  label="Whether it is on the shelf or not"
                ></v-switch>
              </v-col>
              <v-col cols="12" md="6">
                <v-switch
                  v-model="editedItem.limitedEdition"
                  label="Limited edition"
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
                      Select a picture of coffee beans
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
          <v-btn color="grey darken-1" text @click="closeDialog" :disabled="loading">Cancel</v-btn>
          <v-btn
            color="primary"
            text
            @click="save"
            :disabled="!valid || loading"
            :loading="loading"
          >
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 删除确认对话框 -->
    <v-dialog v-model="deleteDialog" max-width="400px">
      <v-card>
        <v-card-title class="text-h5">Confirm the deletion</v-card-title>
        <v-card-text>
          Are you sure you want to delete this coffee bean product? This action is irreversible.
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey darken-1" text @click="deleteDialog = false">Cancel</v-btn>
          <v-btn color="error" text @click="deleteItem">Confirm the deletion</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 详情对话框 -->
    <v-dialog v-model="detailDialog" max-width="800px">
      <v-card>
        <v-card-title class="d-flex justify-space-between align-center">
          <span>Coffee details</span>
          <div>
            <v-btn
              color="warning"
              class="mr-2"
              @click="editItem(viewedItem)"
            >
              Edit
            </v-btn>
            <v-btn
              color="error"
              @click="confirmDelete(viewedItem)"
            >
              Delete
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
                    <v-list-item-subtitle>Name</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.name }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Varieties</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.variety }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Solution</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.processMethod }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Roast degree</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.roastLevel }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Flavor description</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.flavorNotes }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Weight per pack</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.weightPerBagKg }} kg</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Inventory quantity</v-list-item-subtitle>
                    <v-list-item-title>{{ viewedItem.bagStock }} 包</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Price per pack</v-list-item-subtitle>
                    <v-list-item-title>¥{{ viewedItem.pricePerBag }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Status</v-list-item-subtitle>
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
                    <v-list-item-subtitle>Limited edition</v-list-item-subtitle>
                    <v-list-item-title>
                      <v-chip
                        :color="viewedItem.limitedEdition ? 'warning' : 'grey'"
                        small
                      >
                        {{ viewedItem.limitedEdition ? 'Yes' : 'No' }}
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
          <v-btn color="primary" text @click="detailDialog = false">Close</v-btn>
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
          Close
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
      { text: 'Image', value: 'imageUrl', sortable: false },
      { text: 'Name', value: 'name' },
      { text: 'Varieties', value: 'variety' },
      { text: 'Solution', value: 'processMethod' },
      { text: 'Stock', value: 'bagStock' },
      { text: 'Price', value: 'pricePerBag' },
      { text: 'Status', value: 'available' },
      { text: 'Actions', value: 'actions', sortable: false }
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
      return this.editedIndex === -1 ? 'Add Coffee Beans' : 'Edit Coffee Beans'
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
          text: 'Failed to get coffee bean list',
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
          text: 'If the image is too large, please choose an image that is less than 5MB',
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
            text: 'If the image is uploaded, click Save to complete the operation',
            color: 'success',
            timeout: 3000
          }
        } else {
          throw new Error(response.data.message || 'Upload failed');
        }
      } catch (error) {
        console.error('The image was uploaded incorrectly:', error);

        // 清除预览，因为上传失败
        this.imagePreview = null;

        if (error.response && error.response.status === 401) {
          this.snackbar = {
            show: true,
            text: 'The login session has expired, please log in again',
            color: 'error',
            timeout: 3000
          }
        } else {
          this.snackbar = {
            show: true,
            text: 'Image upload failed: ' + ((error.response && error.response.data && error.response.data.message) || error.message || 'Unknown error'),
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
            text: 'The coffee beans were renewed successfully',
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
            text: 'Coffee beans are created successfully',
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
        console.error('Save failed:', error);
        let errorMessage = 'The operation failed';

        if (error.response && error.response.data && error.response.data.message) {
          errorMessage = error.response.data.message;
        }

        this.snackbar = {
          show: true,
          text: this.editedIndex > -1 ? `The update failed: ${errorMessage}` : `Creation failed: ${errorMessage}`,
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
          text: 'The coffee beans were deleted successfully',
          color: 'success',
          timeout: 3000
        };

      } catch (error) {
        this.snackbar = {
          show: true,
          text: 'Deletion failed',
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
