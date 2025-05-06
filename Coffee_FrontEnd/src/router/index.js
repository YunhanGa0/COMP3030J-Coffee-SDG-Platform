import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import WorldMap from '../views/WorldMap.vue'
import About from '../views/About.vue'
import Plantation from '../views/Plantation.vue'
import Explore from '../views/Explore.vue'
import FarmerSupport from '../views/FarmerSupport.vue'
import Login from '../views/auth/Login.vue'
import Register from '../views/auth/Register.vue'
import ArticleEditor from '../views/ArticleEditor.vue'
import ArticleList from '../views/ArticleList.vue'
import ArticleDetail from '../views/ArticleDetail.vue'
import AdminDashboard from '../views/admin/AdminDashboard.vue'
import AdminCreateFarmer from '../views/admin/AdminCreateFarmer.vue'
import FarmDetail from '../views/FarmDetail.vue'
import FarmBlogDetail from '../views/farmer/FarmBlogDetail.vue'
import FarmerDashboard from '../views/farmer/FarmerDashboard.vue'
import BlogEditor from '../views/BlogEditor.vue'
import store from '../store'
import CoffeeBeanDetail from "@/views/CoffeeBeanDetail.vue";
import CertificationManagement from '../views/admin/CertificationManagement.vue'
import FarmCertification from '../views/farmer/FarmCertification.vue'
import FinancialSupport from "@/views/farmer/FinancialSupport.vue";
import FinancialManagement from '../views/admin/FinancialManagement.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    component: About
  },
  {
    path: '/world-map',
    name: 'WorldMap',
    component: WorldMap
  },
  {
    path: '/plantation',
    name: 'Plantation',
    component: Plantation
  },
  {
    path: '/explore',
    name: 'Explore',
    component: Explore
  },
  {
    path: '/farmer-support',
    name: 'FarmerSupport',
    component: FarmerSupport
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { guest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { guest: true }
  },
  {
    path: '/article/editor',
    name: 'ArticleEditor',
    component: ArticleEditor,
    meta: { requiresAuth: true, adminOnly: true }
  },
  {
    path: '/articles',
    name: 'ArticleList',
    component: ArticleList
  },
  {
    path: '/articles/:id',
    name: 'ArticleDetail',
    component: ArticleDetail
  },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: AdminDashboard,
    meta: { requiresAuth: true, adminOnly: true }
  },
  {
    path: '/admin/create-farmer',
    name: 'AdminCreateFarmer',
    component: AdminCreateFarmer,
    meta: { requiresAuth: true, adminOnly: true }
  },
  {
    path: '/farmer-dashboard',
    name: 'FarmerDashboard',
    component: FarmerDashboard,
    meta: { requiresAuth: true, farmerOnly: true }
  },
  {
    path: '/blog-editor',
    name: 'BlogEditor',
    component: BlogEditor,
    meta: { requiresAuth: true, farmerOnly: true }
  },
  {
    path: '/farms/:id',
    name: 'FarmDetail',
    component: FarmDetail
  },
  {
    path: '/farms/:farmId/blogs/:blogId',
    name: 'FarmBlogDetail',
    component: FarmBlogDetail
  },
  {
    path: '/coffee-beans/:id',
    name: 'CoffeeBeanDetail',
    component: CoffeeBeanDetail
  },
  {
    path: '/admin/certifications',
    name: 'CertificationManagement',
    component: CertificationManagement,
    meta: { requiresAuth: true, adminOnly: true }
  },
  {
    path: '/admin/financial',
    name: 'FinancialManagement',
    component: FinancialManagement,
    meta: { requiresAuth: true, roles: ['ADMIN'] }
  },
  {
    path: '/farm-certification',
    name: 'FarmCertification',
    component: FarmCertification,
    meta: {
      requiresAuth: true,
      farmerOnly: true
    }
  },
  {
    path: '/financial-support',
    name: 'FinancialSupport',
    component: FinancialSupport,
    meta: {
      requiresAuth: true,
      farmerOnly: true
    }
  },
  {
    path: '/admin/trainings',
    name: 'TrainingManagement',
    component: () => import('@/views/admin/TrainingManagement.vue'),
    meta: {
      requiresAuth: true,
      requiresAdmin: true
    }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const adminOnly = to.matched.some(record => record.meta.adminOnly)
  const farmerOnly = to.matched.some(record => record.meta.farmerOnly)
  const isAuthenticated = store.getters['auth/isAuthenticated']
  const currentUser = store.getters['auth/currentUser']
  const isAdmin = currentUser && currentUser.role === 'ADMIN'
  const isFarmer = currentUser && currentUser.role === 'FARMER'

  if (requiresAuth && !isAuthenticated) {
    next('/login')
  } else if (adminOnly && !isAdmin) {
    next('/')  // 如果需要管理员权限但用户不是管理员，重定向到首页
  } else if (farmerOnly && !isFarmer) {
    next('/')  // 如果需要农民权限但用户不是农民，重定向到首页
  } else {
    next()
  }
})

export default router
