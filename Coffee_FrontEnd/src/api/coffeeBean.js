import axios from 'axios'

const BASE_URL = '/api/farmers/coffee-beans'

export const coffeeBeanApi = {
  // 获取农庄的咖啡豆列表
  getCoffeeBeansList() {
    return axios.get(BASE_URL)
  },

  // 获取单个咖啡豆详情
  getCoffeeBeanDetail(id) {
    return axios.get(`/api/coffee-beans/${id}`)
  },

  // 创建新的咖啡豆
  createCoffeeBean(data) {
    return axios.post(BASE_URL, data)
  },

  // 更新咖啡豆信息
  updateCoffeeBean(id, data) {
    return axios.put(`${BASE_URL}/${id}`, data)
  },

  // 删除咖啡豆
  deleteCoffeeBean(id) {
    return axios.delete(`${BASE_URL}/${id}`)
  }
}