<template>
  <v-container class="bean-page" fluid>
    <!-- skeleton -->
    <v-skeleton-loader
      v-if="loading"
      type="article, card"
      width="640"
      class="mx-auto my-12"
    ></v-skeleton-loader>

    <!-- 正文 -->
    <div v-else>
      <!-- hero -->
      <section class="hero">
        <v-btn
          fab
          small
        class="back-fab"
        color="grey"
        title="Back to previous"
        aria-label="Back to previous"
        @click="$router.back()"
        >
        <!-- 直接内联 24×24 SVG -->
        <svg viewBox="0 0 24 24" width="24" height="24" fill="currentColor">
          <!-- 细实线 ←  -->
          <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2Z"/>
        </svg>

        <!-- 小屏幕下同时显示文字 -->
        <span class="back-label d-sm-none ml-1">Back</span>
        </v-btn>
        <v-img :src="bean.imageUrl || fallbackImg" class="hero-img" />

        <div class="hero-meta">
          <h1 class="bean-name">{{ bean.name }}</h1>
          <p class="bean-sub">
            {{ bean.variety }} · {{ bean.processMethod }} · {{ bean.roastLevel }}
          </p>
          <p class="bean-price">${{ bean.pricePerBag }} / bag</p>

          <v-btn color="green darken-2" large @click="drawer = true">
            Buy Now
          </v-btn>
        </div>
      </section>

      <!-- details -->
      <v-row class="mt-8">
        <v-col cols="12" md="8">
          <v-card elevation="1" class="pa-4">
            <v-card-title class="headline">Flavor Notes</v-card-title>
            <v-card-text>{{ bean.flavorNotes }}</v-card-text>

            <v-divider class="my-4" />

            <v-card-title class="headline">Specification</v-card-title>
            <v-card-text>
              <ul>
                <li>Weight / bag: {{ bean.weightPerBagKg }} kg</li>
                <li>Stock: {{ bean.bagStock }} bags</li>
                <li v-if="bean.limitedEdition">Limited Edition ✅</li>
              </ul>
            </v-card-text>
          </v-card>
        </v-col>

      </v-row>
    </div>

    <!-- 侧边 / Mobile sheet -->
    <v-navigation-drawer
      v-model="drawer"
      :right="!$vuetify.breakpoint.xsOnly"
      fixed
      temporary
      width="420"
    >
      <v-card flat class="d-flex flex-column fill-height pa-4">
        <v-card-title class="headline">
          Checkout – {{ bean.name }}
        </v-card-title>

        <v-card-text class="flex-grow-1 d-flex flex-column">
          <div class="d-flex justify-space-between mb-2">
            <span>Price / bag</span><strong>${{ bean.pricePerBag }}</strong>
          </div>

          <v-text-field
            v-model.number="qty"
            type="number"
            label="Quantity"
            :min="1"
            :max="bean.bagStock"
            class="mt-2"
          />

          <v-divider class="my-4"></v-divider>

          <div class="d-flex justify-space-between subtitle-1">
            <span>Total</span><strong class="headline">${{ total }}</strong>
          </div>

          <v-spacer></v-spacer>

          <div class="checkout-actions mt-6">
            <v-btn
              :loading="placingOrder"
              color="green-darken-1"
              class="flex-1 mr-2"
              @click="placeOrder"
            >
              Confirm & Pay
            </v-btn>

            <v-btn
              class="flex-1 ml-2"
              variant="outlined"
              color="grey"
              @click="drawer = false"
            >
              Cancel
            </v-btn>
          </div>
        </v-card-text>
      </v-card>
    </v-navigation-drawer>

    <!-- snackbar -->
    <v-snackbar v-model="snackbar.show" :color="snackbar.color">
      {{ snackbar.text }}
    </v-snackbar>
  </v-container>
</template>

<script>
import axios from 'axios'

export default {
  name: 'CoffeeBeanDetail',

  data () {
    return {
      bean: {},
      loading: true,
      drawer: false,
      qty: 1,
      placing: false,
      snackbar: { show: false, text: '', color: 'success' },
      fallbackImg: '/img/default-bean.jpg'
    }
  },

  computed: {
    total () {
      return (this.qty || 1) * (this.bean.pricePerBag || 0)
    }
  },

  created () {
    const id = this.$route.params.id
    axios.get(`/api/coffee-beans/${id}`)
      .then(res => { this.bean = res.data.data })
      .catch(()   => { this.toast('Failed to load', 'error') })
      .finally(() => { this.loading = false })
  },

  methods: {
    async placeOrder () {
      if (this.qty < 1) return
      this.placing = true
      try {
        await axios.post('/api/orders', {
          beanId: this.bean.id,
          quantity: this.qty
        })
        this.toast('Order placed 🎉', 'success')
        this.drawer = false
        this.$router.push('/orders')
      } catch (e) {
        this.toast('Payment failed', 'error')
      } finally {
        this.placing = false
      }
    },
    toast (msg, color) {
      this.snackbar = { show: true, text: msg, color }
    }
  }
}
</script>

<style scoped>
.bean-page { background:#fafafa; min-height:100vh; }

/* hero */
.hero {
  display: flex;
  flex-wrap: wrap;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,.05);
}

.hero-img  {
  flex: 0 0 320px;   /* 宽度锁定 320px */
  height: 220px;     /* 高度 220 */
  object-fit: cover;
}

.hero-meta {
  flex: 1 1 320px;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: .6rem;
}

.bean-name  { font-weight:700; font-size:2rem; margin:0; }
.bean-sub   { color:#666; }
.bean-price { font-size:1.6rem; font-weight:700; color:#19624c; }

@media (max-width:600px){
  /* 让 drawer 变成底部 sheet */
  .v-navigation-drawer--temporary.v-navigation-drawer--right{
    left:0 !important; right:0 !important; width:100% !important;
    border-top-left-radius:24px; border-top-right-radius:24px;
  }
}

.checkout-actions {
  display: flex;
  width: 100%;
}

.checkout-actions .v-btn {
  min-height: 44px;         /* 按钮高度更紧凑（iOS 推荐 44px） */
  font-weight: 600;
  text-transform: none;     /* 取消全大写，视品牌而定 */
}


.back-fab {
  position: absolute;
  top: 16px;
  left: 16px;          /* 更契合返回手势 */
  z-index: 3;          /* 确保在图片之上 */
}

/* 可选：悬停、聚焦时轻微抬升 */
.back-fab:hover, .back-fab:focus-visible {
  transform: translateY(-2px);
}
.back-label {
  font-size: 0.75rem;
  font-weight: 500;
}


</style>
