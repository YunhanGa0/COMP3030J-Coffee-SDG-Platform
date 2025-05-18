<template>
  <div class="explore-page">
    <!-- 页面标题区域 -->
    <section class="page-header">
      <v-parallax
        :src="require('@/assets/pic/worldmap.png')"
        height="350"
      >
        <div class="overlay"></div>
        <v-container class="fill-height">
          <v-row align="center" justify="center">
            <v-col cols="12" class="text-center">
              <h1 class="text-h2 font-weight-bold white--text mb-4">Explore Coffee Origins</h1>
              <div class="divider-line mb-4"></div>
              <p class="text-h6 white--text">
                Discover global premium coffee origins and learn about their heritage and flavors
              </p>
            </v-col>
          </v-row>
        </v-container>
      </v-parallax>
    </section>

    <!-- 主要内容区域 -->
    <section class="map-section py-8">
      <v-container>
        <v-card class="map-card" elevation="4">
          <v-card-text class="pa-0">
            <div class='wrapper'>
              <div class='chart' id='coffeeChart'></div>
            </div>
          </v-card-text>
        </v-card>

        <v-row class="mt-8">
          <v-col cols="12" md="6">
            <v-card class="region-card h-100" elevation="2">
              <v-card-title class="primary--text font-weight-bold">
                <v-icon color="primary" class="mr-2">mdi-map-marker</v-icon>
                Region Information
              </v-card-title>
              <v-divider></v-divider>
              <v-card-text class="pt-4">
                <div v-if="selectedRegion" class="region-info">
                  <h2 class="text-h4 font-weight-bold mb-4 region-name">
                    {{ selectedRegion.name }}
                  </h2>
                  <div class="region-details">
                    <div class="detail-item">
                      <div class="detail-icon">
                        <v-icon color="primary">mdi-factory</v-icon>
                      </div>
                      <div class="detail-content">
                        <div class="detail-label">Annual output</div>
                        <div class="detail-value">{{ selectedRegion.value }} tons</div>
                      </div>
                    </div>
                    <div class="detail-item">
                      <div class="detail-icon">
                        <v-icon color="primary">mdi-seed</v-icon>
                      </div>
                      <div class="detail-content">
                        <div class="detail-label">The main variety</div>
                        <div class="detail-value">{{ selectedRegion.varieties || 'Arabica' }}</div>
                      </div>
                    </div>
                    <div class="detail-item">
                      <div class="detail-icon">
                        <v-icon color="primary">mdi-water</v-icon>
                      </div>
                      <div class="detail-content">
                        <div class="detail-label">Flavor characteristics</div>
                        <div class="detail-value">{{ selectedRegion.flavor || 'Mellow, slightly fruity' }}</div>
                      </div>
                    </div>
                    <div class="detail-item">
                      <div class="detail-icon">
                        <v-icon color="primary">mdi-mountains</v-icon>
                      </div>
                      <div class="detail-content">
                        <div class="detail-label">Altitude</div>
                        <div class="detail-value">{{ selectedRegion.altitude || '1200-1800m' }}</div>
                      </div>
                    </div>
                  </div>
                  <v-btn
                    color="primary"
                    class="mt-4"
                    outlined
                  >
                    Learn more
                    <v-icon right>mdi-arrow-right</v-icon>
                  </v-btn>
                </div>
                <div v-else class="empty-state">
                  <v-img
                    :src="require('@/assets/pic/咖啡近摄1.jpg')"
                    max-height="200"
                    contain
                    class="mb-4"
                  ></v-img>
                  <p class="text-center grey--text text--darken-1">
                    Click on a coffee region on the map to view detailed information
                  </p>
                </div>
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" md="6">
            <v-card class="info-card h-100" elevation="2">
              <v-card-title class="primary--text font-weight-bold">
                <v-icon color="primary" class="mr-2">mdi-coffee</v-icon>
                Coffee Knowledge
              </v-card-title>
              <v-divider></v-divider>
              <v-card-text class="pt-4">
                <v-tabs
                  v-model="infoTab"
                  background-color="transparent"
                  color="primary"
                  class="mb-4"
                  grow
                >
                  <v-tab class="tab-button">
                    <v-icon left>mdi-sprout</v-icon>
                    Growing
                  </v-tab>
                  <v-tab class="tab-button">
                    <v-icon left>mdi-fire</v-icon>
                    Roasting
                  </v-tab>
                  <v-tab class="tab-button">
                    <v-icon left>mdi-coffee-maker</v-icon>
                    Brewing
                  </v-tab>
                </v-tabs>

                <v-tabs-items v-model="infoTab" class="tab-content">
                  <v-tab-item>
                    <div class="pa-3">
                      <v-img
                        :src="require('@/assets/pic/咖啡园区1.jpg')"
                        height="200"
                        class="rounded-lg mb-4"
                      ></v-img>
                      <p class="text-body-1 mb-3">Coffee cultivation is mainly distributed in the "coffee belt" region between the Tropic of Cancer and the Tropic of Capricorn, where suitable climate conditions and altitude exist.</p>
                      <p class="text-body-1 mb-3">High-quality coffee typically grows in high-altitude areas (1,200-2,000m), with moderate temperatures (15-24°C) and adequate rainfall.</p>
                      <p class="text-body-1">Coffee trees take 3-4 years from planting to first harvest, and can continue producing for 20-30 years.</p>
                    </div>
                  </v-tab-item>
                  <v-tab-item>
                    <div class="pa-3">
                      <v-img
                        :src="require('@/assets/pic/咖啡近摄2.jpg')"
                        height="200"
                        class="rounded-lg mb-4"
                      ></v-img>
                      <p class="text-body-1 mb-3">Coffee roasting is the key step in transforming green beans into drinkable coffee. Different roasting levels, from light to dark, produce distinct flavor characteristics.</p>
                      <p class="text-body-1 mb-3">Light roasts preserve more original flavors and acidity, highlighting fruity and floral notes; medium roasts enhance sweetness and balance; dark roasts increase bitterness and roasted aromas.</p>
                      <p class="text-body-1">The "first crack" and "second crack" during roasting are important indicators of roasting level, representing physical and chemical changes within the coffee beans.</p>
                    </div>
                  </v-tab-item>
                  <v-tab-item>
                    <div class="pa-3">
                      <v-img
                        :src="require('@/assets/pic/pexels-abhinavcoca-291539.jpg')"
                        height="200"
                        class="rounded-lg mb-4"
                      ></v-img>
                      <p class="text-body-1 mb-3">Coffee brewing methods are diverse, including drip, siphon, French press, and espresso. Different brewing methods emphasize different flavor characteristics of the coffee.</p>
                      <p class="text-body-1 mb-3">Water temperature (88-96°C), grind size, coffee-to-water ratio (1:15-1:18), and extraction time are key factors affecting coffee flavor.</p>
                      <p class="text-body-1">Pour-over coffee requires precise water flow control. Good pouring technique can evenly extract soluble substances from the coffee grounds, resulting in a more balanced flavor.</p>
                    </div>
                  </v-tab-item>
                </v-tabs-items>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <!-- 搜索和筛选区域 -->
    <!--<v-row>
      <v-col cols="12" md="6">
        <v-text-field
          v-model="searchQuery"
          label="Search Origins"
          prepend-icon="mdi-magnify"
          outlined
          dense
          clearable
          @input="handleSearch"
        ></v-text-field>
      </v-col>
      <v-col cols="12" md="6">
        <v-select
          v-model="selectedRegion"
          :items="regions"
          label="Filter by Region"
          outlined
          dense
          clearable
          @change="handleFilter"
        ></v-select>
      </v-col>
    </v-row>-->

    <!-- 产地列表 -->
<!--
    <v-row>
      <v-col
        v-for="origin in filteredOrigins"
        :key="origin.id"
        cols="12"
        sm="6"
        md="4"
      >
        <v-card
          class="origin-card"
          :to="'/origins/' + origin.id"
          hover
          elevation="2"
        >
          <v-img
            :src="origin.image"
            height="200"
            class="origin-image"
          ></v-img>

          <v-card-title class="origin-title">
            {{ origin.name }}
          </v-card-title>

          <v-card-text>
            <p class="origin-description">{{ origin.description }}</p>
            <div class="d-flex align-center mt-4">
              <v-icon small color="grey" class="mr-1">mdi-map-marker</v-icon>
              <span class="text-caption grey&#45;&#45;text">
                {{ origin.region }}
              </span>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    &lt;!&ndash; 无数据提示 &ndash;&gt;
    <v-row v-if="filteredOrigins.length === 0">
      <v-col cols="12" class="text-center">
        <v-icon size="64" color="grey lighten-1" class="mb-4">mdi-map-search-outline</v-icon>
        <h3 class="text-h5 grey&#45;&#45;text">No origins found</h3>
        <p class="body-2 grey&#45;&#45;text mt-2">Try adjusting your search criteria</p>
      </v-col>
    </v-row>
-->

    <!-- 产区比较部分 -->
    <section class="compare-section py-12 grey-bg">
      <v-container>
        <v-row justify="center">
          <v-col cols="12" class="text-center mb-8">
            <h2 class="section-title">Major Region Comparison</h2>
            <p class="section-subtitle">
              Coffee from different regions exhibits unique flavor characteristics due to variations in growing environment and processing methods
            </p>
          </v-col>
        </v-row>

        <v-row>
          <v-col
            cols="12"
            sm="6"
            md="4"
            v-for="(region, index) in coffeeRegions.slice(0, 6)"
            :key="index"
          >
            <v-card class="compare-card card-hover h-100" @click="selectedRegion = region">
              <v-img
                :src="getRegionImage(region.name)"
                height="180"
                class="region-image"
              >
                <div class="region-overlay">
                  <div class="region-title text-h5 font-weight-bold white--text">
                    {{ region.name }}
                  </div>
                </div>
              </v-img>
              <v-card-text>
                <div class="region-flavor mb-2">
                  <v-icon small color="amber darken-2" class="mr-1">mdi-star</v-icon>
                  <span class="font-weight-medium">Flavor: </span>
                  <span>{{ region.flavor }}</span>
                </div>
                <div class="region-altitude">
                  <v-icon small color="green darken-1" class="mr-1">mdi-altitude</v-icon>
                  <span class="font-weight-medium">Altitude: </span>
                  <span>{{ region.altitude }}</span>
                </div>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </section>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Explore',
  data() {
    return {
      chart: null,
      infoTab: 0,
      selectedRegion: null,
      coffeeRegions: [
        { name: "Colombia", value: 8500, varieties: "Arabica (Typica, Bourbon)", flavor: "Balanced, citrus acidity, nutty", altitude: "1200-2000m", image: "colombia.jpg" },
        { name: "Ethiopia", value: 7200, varieties: "Heirloom Arabica (Yirgacheffe, Sidamo)", flavor: "Floral, citrus, berry notes", altitude: "1500-2200m", image: "ethiopia.jpg" },
        { name: "Kenya", value: 5000, varieties: "SL28, SL34, Kenya AA", flavor: "Bright acidity, blackcurrant, tropical fruit", altitude: "1700-2100m", image: "kenya.jpg" },
        { name: "Guatemala", value: 4800, varieties: "Bourbon, Caturra, Pacamara", flavor: "Chocolate, caramel, citrus", altitude: "1300-1800m", image: "guatemala.jpg" },
        { name: "Brazil", value: 9800, varieties: "Bourbon, Catuaí, Mundo Novo", flavor: "Nutty, chocolatey, low acidity", altitude: "800-1300m", image: "brazil.jpg" },
        { name: "Costa Rica", value: 4200, varieties: "Caturra, Catuaí, Villa Sarchi", flavor: "Balanced, citrus, caramel", altitude: "1200-1800m", image: "costa_rica.jpg" },
        { name: "Indonesia", value: 6300, varieties: "Java, Sumatra", flavor: "Earthy, spicy, low acidity", altitude: "1000-1500m", image: "indonesia.jpg" },
        { name: "Vietnam", value: 10200, varieties: "Robusta, Catimor", flavor: "Bold, nutty, cocoa", altitude: "600-800m", image: "vietnam.jpg" }
      ],
      regions: [
        'Africa',
        'Asia',
        'Central America',
        'South America',
        'Oceania'
      ],
      origins: [
        {
          id: 1,
          name: 'Ethiopia',
          description: 'The birthplace of coffee, known for its distinctive floral and citrus notes.',
          region: 'Africa',
          image: require('@/assets/pic/coffee_pick.jpg')
        },
        {
          id: 2,
          name: 'Colombia',
          description: 'Famous for its balanced, medium-bodied coffee with caramel sweetness.',
          region: 'South America',
          image: require('@/assets/pic/coffee_pick.jpg')
        }
      ],
      searchQuery: '',
      filteredOrigins: []
    }
  },
  mounted() {
    this.initChart()
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
    window.removeEventListener('resize', this.resizeHandler)
  },
  methods: {
    initChart() {
      this.chart = echarts.init(document.getElementById('coffeeChart'))
      window.addEventListener('resize', this.resizeHandler)

      const chartData = this.coffeeRegions.map(region => {
        return {
          name: this.translateCountryName(region.name),
          value: region.value,
          originalData: region
        }
      })

      const option = {
        backgroundColor: '#fff',
        tooltip: {
          trigger: 'item',
          formatter: function(params) {
            if (params.data && params.data.value) {
              return `<div style="font-weight:bold;">${params.name}</div>
                     <div>Annual output: ${params.value} 吨</div>`;
            }
            return params.name;
          }
        },
        visualMap: {
          min: 0,
          max: 10000,
          text: ['High yield', 'Low yield'],
          realtime: false,
          calculable: true,
          inRange: {
            color: ['#f5f5f5', '#c8e6c9', '#66bb6a', '#2e7d32', '#00704A']
          },
          textStyle: {
            color: '#333'
          }
        },
        series: [
          {
            name: 'Global coffee producers',
            type: 'map',
            mapType: 'world',
            roam: true,
            zoom: 1.2,
            label: {
              show: false
            },
            itemStyle: {
              borderWidth: 0.5,
              borderColor: '#404a59',
              areaColor: '#e8f5e9'
            },
            emphasis: {
              label: {
                show: true,
                color: '#fff'
              },
              itemStyle: {
                areaColor: '#00704A'
              }
            },
            data: chartData
          }
        ]
      }

      this.chart.setOption(option)

      // 添加点击事件
      this.chart.on('click', (params) => {
        if (params.data && params.data.originalData) {
          this.selectedRegion = params.data.originalData
        }
      })
    },
    resizeHandler() {
      if (this.chart) {
        this.chart.resize()
      }
    },
    translateCountryName(chineseName) {
      // 将中文国家名称转换为英文（ECharts地图使用英文名称）
      const nameMap = {
        '哥伦比亚': 'Colombia',
        '埃塞俄比亚': 'Ethiopia',
        '肯尼亚': 'Kenya',
        '危地马拉': 'Guatemala',
        '巴西': 'Brazil',
        '哥斯达黎加': 'Costa Rica',
        '印度尼西亚': 'Indonesia',
        '越南': 'Vietnam'
      }
      return nameMap[chineseName] || chineseName
    },
    getRegionImage(regionName) {
      // 根据地区名称返回对应的图片
      const imageMap = {
        'Colombia': require('@/assets/pic/咖啡园区1.jpg'),
        'Ethiopia': require('@/assets/pic/咖啡采集女1.jpg'),
        'Kenya': require('@/assets/pic/咖啡采集男1.jpg'),
        'Guatemala': require('@/assets/pic/咖啡近摄1.jpg'),
        'Brazil': require('@/assets/pic/咖啡园区2.jpg'),
        'Costa Rica': require('@/assets/pic/咖啡近摄2.jpg'),
        'Indonesia': require('@/assets/pic/咖啡采集女2.jpg'),
        'Vietnam': require('@/assets/pic/咖啡采集男2.jpg')
      }

      return imageMap[regionName] || require('@/assets/pic/咖啡园区3.jpg')
    },
    handleSearch() {
      // 实现搜索逻辑
      console.log('Searching for:', this.searchQuery)
    },
    handleFilter() {
      // 实现筛选逻辑
      console.log('Filtering by region:', this.selectedRegion)
    }
  }
}
</script>

<style scoped>
.explore-page {
  overflow-x: hidden;
}

/* 页面标题区域 */
.page-header {
  position: relative;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1;
}

.divider-line {
  width: 80px;
  height: 4px;
  background-color: white;
  margin: 0 auto;
  animation: expandWidth 1.5s ease-out;
}

@keyframes expandWidth {
  from { width: 0; }
  to { width: 80px; }
}

/* 地图区域 */
.wrapper {
  width: 100%;
  height: 60vh;
  min-height: 400px;
  background: #fff;
}

.chart {
  width: 100%;
  height: 100%;
  padding: 10px;
}

.map-card {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

/* 信息卡片 */
.region-card, .info-card {
  border-radius: 12px;
  transition: all 0.3s ease;
  height: 100%;
}

.h-100 {
  height: 100%;
}

.region-name {
  position: relative;
  display: inline-block;
  padding-bottom: 8px;
  color: var(--primary-color);
}

.region-name:after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 60px;
  height: 3px;
  background-color: var(--primary-color);
}

.region-details {
  margin-top: 20px;
}

.detail-item {
  display: flex;
  margin-bottom: 16px;
  align-items: flex-start;
}

.detail-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background-color: rgba(0, 112, 74, 0.1);
  margin-right: 16px;
}

.detail-label {
  font-size: 0.9rem;
  color: var(--text-light);
  margin-bottom: 4px;
}

.detail-value {
  font-size: 1.1rem;
  font-weight: 500;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  text-align: center;
}

/* 标签页样式 */
.tab-button {
  font-weight: 500;
  letter-spacing: 0.5px;
}

.tab-content {
  background-color: #fafafa;
  border-radius: 8px;
}

/* 比较区域 */
.grey-bg {
  background-color: #f9f9f9;
}

.section-title {
  position: relative;
  font-size: 2.25rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  color: var(--primary-color);
}

.section-title::after {
  content: "";
  display: block;
  width: 60px;
  height: 4px;
  background-color: var(--primary-color);
  margin: 0.75rem auto 0;
}

.section-subtitle {
  font-size: 1.2rem;
  max-width: 700px;
  margin: 0 auto 1.5rem;
  color: var(--text-light);
}

.compare-card {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
}

.card-hover:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12) !important;
}

.region-image {
  position: relative;
}

.region-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0));
}

.region-flavor, .region-altitude {
  display: flex;
  align-items: center;
}

/* 响应式调整 */
@media (max-width: 960px) {
  .wrapper {
    height: 50vh;
  }

  .detail-value {
    font-size: 1rem;
  }
}

@media (max-width: 600px) {
  .wrapper {
    height: 40vh;
  }

  .region-name {
    font-size: 1.8rem !important;
  }

  .detail-item {
    margin-bottom: 12px;
  }

  .detail-icon {
    width: 32px;
    height: 32px;
  }
}
</style>
