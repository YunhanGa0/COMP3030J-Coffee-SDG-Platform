<template>
  <v-container>
    <h1>Your Orders</h1>

    <!-- 订单状态筛选 -->
    <v-btn-toggle v-model="selectedStatus" class="mb-6">
      <v-btn value="ALL">All</v-btn>
      <v-btn value="PAID">Pending Shipment</v-btn>
      <v-btn value="SHIPPED">Awaiting Delivery</v-btn>
    </v-btn-toggle>

    <!-- 无订单提示 -->
    <v-alert
      v-if="filteredOrders.length === 0 && !loading"
      type="info"
      class="my-6"
    >
      No orders found.
    </v-alert>

    <!-- 订单列表 -->
    <v-row v-else>
      <v-col
        v-for="order in filteredOrders"
        :key="order.id"
        cols="12"
        sm="6"
        md="4"
      >
        <v-card
          class="order-card"
          outlined
          @click="viewOrderDetail(order.id)"
        >
          <!-- 商品图片 -->
          <v-img
            :src="order.coffeeBeanImageUrl || fallbackImg"
            height="150"
            class="order-img"
          />

          <v-card-title>
            Order #{{ order.id }}
          </v-card-title>

          <v-card-subtitle>
            {{ formatDate(order.orderTime) }}
          </v-card-subtitle>

          <v-card-text>
            <div><strong>Product:</strong> {{ order.coffeeBeanName }}</div>
            <div><strong>Quantity:</strong> {{ order.quantity }}</div>
            <div><strong>Total:</strong> ${{ order.totalAmount }}</div>
            <div :class="statusClass(order.status)">
              <strong>Status:</strong> {{ order.status }}
            </div>
          </v-card-text>

          <v-card-actions>
            <v-btn
              color="green darken-2"
              @click.stop="viewOrderDetail(order.id)"
            >
              View Details
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>

    <!-- 加载中 -->
    <v-skeleton-loader
      v-if="loading"
      type="card"
      class="my-6"
      :width="300"
      :height="200"
      count="3"
    />
  </v-container>
</template>

<script>
import axios from 'axios'

export default {
  name: 'OrderList',
  data() {
    return {
      orders: [],
      selectedStatus: 'ALL',
      loading: true,
      fallbackImg: '/img/default-bean.jpg'
    }
  },
  computed: {
    filteredOrders() {
      if (this.selectedStatus === 'ALL') {
        return this.orders;
      }
      return this.orders.filter(order => order.status === this.selectedStatus);
    }
  },
  async created() {
    try {
      const response = await axios.get('/api/orders/my');
      if (response.data.code === 200) {
        this.orders = response.data.data;
      }
    } catch (e) {
      console.error('Failed to load orders:', e);
    } finally {
      this.loading = false;
    }
  },
  methods: {
    formatDate(date) {
      const d = new Date(date);
      return d.toLocaleDateString() + ' ' + d.toLocaleTimeString();
    },
    viewOrderDetail(orderId) {
      this.$router.push(`/orders/${orderId}`);
    },
    statusClass(status) {
      return status === 'PAID' ? 'order-status-paid' : 'order-status-shipped';
    }
  }
}
</script>

<style scoped>
h1 {
  color: #19624c;
  margin-bottom: 2rem;
}

.order-card {
  margin-bottom: 1.5rem;
  cursor: pointer;
  transition: box-shadow 0.2s, transform 0.2s;
}

.order-card:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.order-card v-card-title {
  font-weight: 700;
}

.order-card v-card-subtitle {
  color: #666;
}

.order-card v-card-text {
  font-size: 0.95rem;
  line-height: 1.4;
  margin-bottom: 1rem;
}

.order-img {
  border-radius: 8px;
  margin-bottom: 1rem;
}

.order-status-paid {
  color: #f39c12; /* 橙色 */
}

.order-status-shipped {
  color: #27ae60; /* 绿色 */
}
</style>
