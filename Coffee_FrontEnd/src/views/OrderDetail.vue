<template>
  <v-container>
    <v-card class="order-card my-8">
      <v-img
        :src="order.coffeeBeanImageUrl || fallbackImg"
        height="200"
        class="order-img"
      />

      <v-card-title>
        Order #{{ order.id }}
      </v-card-title>

      <v-card-subtitle>
        {{ formatDate(order.orderTime) }}
      </v-card-subtitle>

      <v-card-text>
        <ul class="order-details">
          <li><strong>Recipient:</strong> {{ order.recipientName }}</li>
          <li><strong>Contact:</strong> {{ order.contactNumber }}</li>
          <li><strong>Shipping Address:</strong> {{ order.shippingAddress }}</li>
          <li><strong>Product:</strong> {{ order.coffeeBeanName }}</li>
          <li><strong>Quantity:</strong> {{ order.quantity }}</li>
          <li><strong>Total:</strong> ${{ order.totalAmount }}</li>
          <li :class="statusClass(order.status)">
            <strong>Status:</strong> {{ order.status }}
          </li>
        </ul>
      </v-card-text>

      <v-card-actions>
        <v-btn color="grey darken-1" @click="$router.back()">Back</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
import axios from 'axios'

export default {
  name: 'OrderDetail',
  data() {
    return {
      order: {},
      fallbackImg: '/img/default-bean.jpg'
    }
  },
  async created() {
    const orderId = this.$route.params.id;
    try {
      const response = await axios.get(`/api/orders/${orderId}`);
      if (response.data.code === 200) {
        this.order = response.data.data;
      }
    } catch (e) {
      console.error('Failed to load order details:', e);
    }
  },
  methods: {
    formatDate(date) {
      const d = new Date(date);
      return d.toLocaleDateString() + ' ' + d.toLocaleTimeString();
    },
    statusClass(status) {
      return status === 'PAID' ? 'status-paid' : 'status-shipped';
    }
  }
}
</script>

<style scoped>
.order-card {
  padding: 1.5rem;
  border-radius: 12px;
  background-color: #ffffff;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.v-card-title {
  font-weight: 700;
  color: #19624c;
}

.order-details {
  list-style: none;
  padding: 0;
  margin: 1rem 0;
}

.order-details li {
  margin-bottom: 0.5rem;
}

.status-paid {
  color: #f39c12; /* 橙色 */
}

.status-shipped {
  color: #27ae60; /* 绿色 */
}

.order-img {
  border-radius: 12px;
  margin-bottom: 1rem;
  object-fit: cover;
}
</style>
