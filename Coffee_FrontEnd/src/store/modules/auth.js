import axios from 'axios'

const state = {
  user: JSON.parse(localStorage.getItem('user')) || null,
  token: localStorage.getItem('token') || null
}

const mutations = {
  SET_USER(state, user) {
    state.user = user
    if (user) {
      localStorage.setItem('user', JSON.stringify(user))
    } else {
      localStorage.removeItem('user')
    }
  },
  SET_TOKEN(state, token) {
    state.token = token
    if (token) {
      localStorage.setItem('token', token)
    } else {
      localStorage.removeItem('token')
    }
  }
}

const actions = {
  async login({ commit }, credentials) {
    try {
      console.log('开始登录请求:', credentials)
      const response = await axios.post('/api/auth/login', credentials)

      console.log('登录响应状态:', response.status)
      console.log('登录响应数据:', response.data)

      const data = response.data

      if (data.code !== 200 || !data.data || !data.data.token) {
        throw new Error(data.message || 'Login failed')
      }

      commit('SET_USER', data.data)
      commit('SET_TOKEN', data.data.token)
      return data
    } catch (error) {
      console.error('登录错误:', error)
      throw error
    }
  },

  async register({ commit }, userData) {
    try {
      const response = await axios.post('/api/auth/register', userData)
      const data = response.data

      if (!response.status === 200) {
        throw new Error(data.message || '注册失败')
      }

      return data
    } catch (error) {
      throw error
    }
  },

  async getAdminForTest({ commit }) {
    try {
      const response = await axios.get('/api/auth/admin/test')
      const data = response.data

      if (data.code === 200) {
        commit('SET_USER', data.data)
        commit('SET_TOKEN', data.data.token)
        return data
      } else {
        throw new Error(data.message || '获取管理员账户失败')
      }
    } catch (error) {
      console.error('获取管理员账户错误:', error)
      throw error
    }
  },

  logout({ commit }) {
    commit('SET_USER', null)
    commit('SET_TOKEN', null)
  }
}

const getters = {
  isAuthenticated: state => !!state.token,
  currentUser: state => state.user,
  token: state => state.token
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}
