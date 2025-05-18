<template>
  <div>
    <v-container fluid class="pa-0">
      <v-row no-gutters>
        <v-col cols="12">
          <v-parallax
            :src="require('@/assets/pic/plantation.jpg')"
            height="500"
            dark
          >
            <v-row
              align="center"
              justify="center"
              class="overlay-gradient fill-height"
            >
              <v-col
                class="text-center"
                cols="12"
              >
                <v-slide-y-transition>
                  <h1 class="text-h2 font-weight-bold white--text mb-4">Coffee Plantations</h1>
                </v-slide-y-transition>
                <v-slide-y-transition>
                  <h4 class="text-h5 white--text">Sustainable Coffee Cultivation and Production</h4>
                </v-slide-y-transition>
                <v-slide-y-transition>
                  <v-btn
                    class="mt-8"
                    x-large
                    outlined
                    color="white"
                    @click="scrollToContent"
                    :ripple="{ class: 'white--text' }"
                  >
                    Explore More
                    <v-icon right>mdi-chevron-down</v-icon>
                  </v-btn>
                </v-slide-y-transition>
              </v-col>
            </v-row>
          </v-parallax>
        </v-col>
      </v-row>
    </v-container>

    <v-container class="my-12" id="content">
      <v-row>
        <v-col cols="12" md="6">
          <v-slide-x-transition>
            <h2 class="text-h4 font-weight-bold mb-6 primary--text">Our Plantation Philosophy</h2>
          </v-slide-x-transition>
          <v-hover v-slot="{ hover }">
            <v-card
              class="content-block"
              :elevation="hover ? 4 : 0"
              :class="{ 'on-hover': hover }"
            >
              <v-card-text>
                <p class="text-body-1 mb-4">
                  Starbucks is committed to sourcing ethical coffee beans to protect biodiversity and promote the prosperity of coffee-growing communities.
                </p>
                <p class="text-body-1 mb-4">
                  We collaborate with over 400,000 coffee farmers worldwide, providing expertise in agricultural practices and actively supporting coffee farmers to improve their livelihoods.
                </p>
                <p class="text-body-1 mb-8">
                  Through our C.A.F.E. Practices (Coffee and Farmer Equity) program, we ensure coffee is produced in a way that meets environmental, social, and economic standards.
                </p>
                <v-chip-group
                  column
                  show-arrows
                >
                  <v-chip color="primary" outlined pill>Sustainable Development</v-chip>
                  <v-chip color="primary" outlined pill>Fair Trade</v-chip>
                  <v-chip color="primary" outlined pill>Ecological Protection</v-chip>
                  <v-chip color="primary" outlined pill>Community Support</v-chip>
                </v-chip-group>
              </v-card-text>
            </v-card>
          </v-hover>
        </v-col>
        <v-col cols="12" md="6">
          <v-hover v-slot="{ hover }">
            <v-card
              class="video-container-wrapper"
              :elevation="hover ? 8 : 2"
              :class="{ 'on-hover': hover }"
            >
              <div class="video-container">
                <iframe
                  v-if="getProcessedVideoUrl(plantationVideo.url)"
                  :src="getProcessedVideoUrl(plantationVideo.url)"
                  frameborder="0"
                  allowfullscreen
                  class="video-iframe"
                ></iframe>
                <div v-else class="error-message">
                  The video cannot be loaded, please check if the URL is correct
                </div>
              </div>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                  color="primary"
                  text
                  :href="plantationVideo.url"
                  target="_blank"
                >
                  Watch on Bilibili
                  <v-icon right>mdi-open-in-new</v-icon>
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-hover>
        </v-col>
      </v-row>

      <v-row class="my-12">
        <v-col
          v-for="(stat, index) in statistics"
          :key="index"
          cols="12"
          md="3"
        >
          <v-slide-y-transition>
            <v-hover v-slot="{ hover }">
              <v-card
                class="pa-4 text-center"
                :elevation="hover ? 8 : 2"
                :class="{ 'on-hover': hover }"
              >
                <h3 class="text-h3 font-weight-bold primary--text">{{ stat.value }}</h3>
                <p class="text-subtitle-1">{{ stat.label }}</p>
              </v-card>
            </v-hover>
          </v-slide-y-transition>
        </v-col>
      </v-row>

      <v-divider class="my-12"></v-divider>

      <v-row>
        <v-col cols="12" class="text-center mb-8">
          <v-slide-y-transition>
            <h2 class="text-h4 font-weight-bold primary--text">Global Selected Estates</h2>
          </v-slide-y-transition>
          <v-slide-y-transition>
            <p class="text-subtitle-1 grey--text">Explore our premium coffee growing bases around the world</p>
          </v-slide-y-transition>
        </v-col>
      </v-row>

      <v-row>
        <v-col
          v-for="(farm, index) in farms"
          :key="farm.id"
          cols="12"
          md="4"
        >
          <v-hover v-slot="{ hover }">
            <v-card
              class="mb-6 farm-card"
              :elevation="hover ? 8 : 2"
            >
              <v-img
                :src="farm.imageUrl || require('@/assets/pic/plantation.jpg')"
                height="250"
                class="grey lighten-2"
                cover
              >
                <template v-slot:placeholder>
                  <v-row
                    class="fill-height ma-0"
                    align="center"
                    justify="center"
                  >
                    <v-progress-circular
                      indeterminate
                      color="grey lighten-2"
                    ></v-progress-circular>
                  </v-row>
                </template>
                <v-expand-transition>
                  <div
                    v-if="hover"
                    class="d-flex transition-fast-in-fast-out primary v-card--reveal text-h6 white--text"
                    style="height: 100%;"
                  >
                    <v-btn
                      text
                      large
                      color="white"
                      @click="exploreEstate(farm.id)"
                    >
                      View Details
                      <v-icon right>mdi-arrow-right</v-icon>
                    </v-btn>
                  </div>
                </v-expand-transition>
              </v-img>
              <v-card-title class="primary--text">{{ farm.farmName }}</v-card-title>
              <v-card-subtitle>
                {{ farm.country }} | Altitude: {{ farm.elevation }}m
                <v-chip
                  v-if="farm.isCertificated"
                  color="success"
                  x-small
                  class="ml-2"
                >
                  Certified
                </v-chip>
              </v-card-subtitle>
              <v-card-text>
                <p class="farm-description">{{ truncateDescription(farm.description) }}</p>
                <v-chip-group>
                  <v-chip
                    x-small
                    class="mr-2 mt-2"
                    color="primary"
                    label
                  >
                    {{ farm.soilType ? formatSoilType(farm.soilType) : 'Coffee' }}
                  </v-chip>
                  <v-chip
                    x-small
                    class="mr-2 mt-2"
                    color="primary"
                    label
                  >
                    Est. {{ farm.establishedYear }}
                  </v-chip>
                </v-chip-group>
              </v-card-text>
            </v-card>
          </v-hover>
        </v-col>
        <v-col cols="12" v-if="farms.length === 0" class="text-center">
          <v-skeleton-loader
            v-if="loading"
            type="card, card, card"
            class="mx-auto"
          ></v-skeleton-loader>
          <v-alert
            v-else
            type="info"
            outlined
          >
            No farms available at the moment.
          </v-alert>
        </v-col>
      </v-row>

      <!-- Pagination -->
      <v-row v-if="pagination.totalPages > 1">
        <v-col cols="12" class="text-center">
          <v-pagination
            v-model="currentPage"
            :length="pagination.totalPages"
            :total-visible="7"
            @input="fetchFarms"
          ></v-pagination>
        </v-col>
      </v-row>

      <v-row class="mt-16">
        <v-col cols="12">
          <v-hover v-slot="{ hover }">
            <v-card
              color="primary"
              dark
              class="commitment-card"
              :elevation="hover ? 16 : 2"
            >
              <v-card-text class="text-center py-12">
                <v-slide-y-transition>
                  <h3 class="text-h4 font-weight-bold mb-6">Our Commitment</h3>
                </v-slide-y-transition>
                <v-slide-y-transition>
                  <p class="text-h6 font-weight-regular">
                    By 2025, we commit to achieving 100% traceable coffee bean sources and ensuring all coffee farmers receive fair income.
                  </p>
                </v-slide-y-transition>
                <!--<v-slide-y-transition>
                  <v-btn
                    class="mt-6"
                    outlined
                    large
                    @click="learnMore"
                  >
                    Learn More
                    <v-icon right>mdi-arrow-right</v-icon>
                  </v-btn>
                </v-slide-y-transition>-->
              </v-card-text>
            </v-card>
          </v-hover>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Plantation',
  data: () => ({
    statistics: [
      { value: '400K+', label: 'Partner Coffee Farmers' },
      { value: '30+', label: 'Growing Countries' },
      { value: '99%', label: 'Ethically Sourced Coffee' },
      { value: '50M+', label: 'Annual Investment (USD)' }
    ],
    farms: [],
    loading: true,
    currentPage: 1,
    pageSize: 6,
    pagination: {
      totalPages: 0,
      totalElements: 0,
      first: true,
      last: false,
      empty: true
    },
    error: null,
    plantationVideo: {
      url: 'https://www.bilibili.com/video/BV1tmbVeJEnV/',
      title: ''
    }
  }),
  created() {
    this.fetchFarms();
  },
  methods: {
    scrollToContent() {
      const element = document.getElementById('content')
      element.scrollIntoView({ behavior: 'smooth' })
    },
    exploreEstate(farmId) {
      this.$router.push(`/farms/${farmId}`)
    },
    learnMore() {
      console.log('Learning more about our commitment')
      // 这里可以添加跳转到可持续发展承诺页面的逻辑
    },
    async fetchFarms() {
      this.loading = true;
      try {
        const response = await axios.get(`/api/farms`, {
          params: {
            page: this.currentPage - 1,
            size: this.pageSize,
            certified: null
          }
        });

        if (response.data.code === 200) {
          const data = response.data.data;
          this.farms = data.content;
          this.pagination = {
            totalPages: data.totalPages,
            totalElements: data.totalElements,
            first: data.first,
            last: data.last,
            empty: data.empty
          };
        } else {
          this.error = response.data.message || 'Failed to fetch farms';
          console.error(this.error);
        }
      } catch (error) {
        this.error = 'Error connecting to server';
        console.error('Failed to fetch farms:', error);
      } finally {
        this.loading = false;
      }
    },
    truncateDescription(description) {
      if (!description) return 'No description available';
      return description.length > 120 ? description.substring(0, 120) + '...' : description;
    },
    formatSoilType(soilType) {
      if (!soilType) return 'Unknown';
      return soilType.replace('_', ' ').toLowerCase()
        .split(' ')
        .map(word => word.charAt(0).toUpperCase() + word.slice(1))
        .join(' ');
    },
    getProcessedVideoUrl(url) {
      if (url.includes('bilibili.com')) {
        const bvMatch = url.match(/BV\w+/);
        if (bvMatch) {
          return `//player.bilibili.com/player.html?bvid=${bvMatch[0]}&page=1`;
        }
      }
      return null;
    }
  }
}
</script>

<style scoped>
.overlay-gradient {
  background: linear-gradient(to bottom, rgba(0,0,0,0.3), rgba(0,0,0,0.6));
}

.content-block {
  background: #f8f8f8;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.farm-card {
  transition: all 0.3s ease;
  height: 100%;
  overflow: hidden;
}

.farm-card .v-image {
  transition: transform 0.3s ease;
}

.farm-card:hover .v-image {
  transform: scale(1.05);
}

.v-card--reveal {
  align-items: center;
  justify-content: center;
  opacity: 0.9;
  backdrop-filter: blur(3px);
}

.commitment-card {
  background: linear-gradient(135deg, #00704A 0%, #27251F 100%) !important;
  border-radius: 16px;
  transition: all 0.3s ease;
}

.on-hover {
  transform: translateY(-4px);
}

.v-card__text {
  line-height: 1.8;
}

.primary--text {
  color: #00704A !important;
}

.v-chip {
  font-weight: 500;
}

.farm-description {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  height: 4.5em;
}

.video-container-wrapper {
  width: 100%;
}

.video-container {
  position: relative;
  width: 100%;
  padding-top: 56.25%; /* 16:9 宽高比 */
  background: #f5f5f5;
  margin-bottom: 16px;
}

.video-iframe {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.error-message {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #666;
}
</style>
