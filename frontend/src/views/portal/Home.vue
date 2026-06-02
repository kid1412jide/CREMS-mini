<template>
  <div class="home-page">
    <!-- Hero Section -->
    <section class="hero">
      <div class="hero__content">
        <h1 class="hero__title">
          发现你的<span class="highlight">理想工作</span>
        </h1>
        <p class="hero__subtitle">校园招聘与就业信息管理平台，连接优秀企业与优秀人才</p>
        <div class="hero__search">
          <div class="search-box">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索职位名称、关键词..."
              size="large"
              clearable
              @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-input
              v-model="searchCity"
              placeholder="城市"
              size="large"
              clearable
              style="width: 160px"
              @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><Location /></el-icon>
              </template>
            </el-input>
            <el-button v-anime-button type="primary" size="large" @click="handleSearch" class="search-btn">
              搜索职位
            </el-button>
          </div>
        </div>
      </div>
      <div class="hero__bg"></div>
    </section>

    <!-- Stats Section -->
    <section class="stats-section" v-if="stats">
      <div class="stats-grid">
        <div class="stat-item" v-for="(s, index) in statsItems" :key="s.label" v-anime-stagger="index * 80">
          <div class="stat-item__value">{{ s.value }}</div>
          <div class="stat-item__label">{{ s.label }}</div>
        </div>
      </div>
    </section>

    <!-- Latest Jobs -->
    <section class="portal-section">
      <div class="portal-section__header">
        <h2>最新职位</h2>
        <router-link to="/portal/student/jobs" class="section-link">查看全部</router-link>
      </div>
      <div class="portal-grid portal-grid--3" v-loading="jobsLoading">
        <JobCard
          v-for="job in latestJobs"
          :key="job.jobId"
          :job="job"
          :show-favorite="false"
          @click="goJobDetail(job)"
        />
      </div>
      <div class="portal-empty" v-if="!jobsLoading && latestJobs.length === 0">
        <div class="portal-empty__text">暂无职位信息</div>
      </div>
    </section>

    <!-- Hot Categories -->
    <section class="portal-section">
      <div class="portal-section__header">
        <h2>热门分类</h2>
      </div>
      <div class="categories-grid">
        <div
          v-for="cat in categories"
          :key="cat.label"
          class="category-item"
          @click="searchByCategory(cat.type)"
        >
          <div class="category-item__icon" :style="{ background: cat.color }">
            <span>{{ cat.icon }}</span>
          </div>
          <span class="category-item__label">{{ cat.label }}</span>
        </div>
      </div>
    </section>

    <!-- How it works -->
    <section class="portal-section">
      <div class="portal-section__header">
        <h2>使用流程</h2>
      </div>
      <div class="steps-grid">
        <div class="step-card" v-for="(step, i) in steps" :key="i">
          <div class="step-card__number">{{ i + 1 }}</div>
          <h3 class="step-card__title">{{ step.title }}</h3>
          <p class="step-card__desc">{{ step.desc }}</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { Search, Location } from '@element-plus/icons-vue'
import { listJob, getStatisticsOverview } from '@/api/portal'
import JobCard from '@/components/portal/JobCard.vue'

const router = useRouter()

const searchKeyword = ref('')
const searchCity = ref('')
const latestJobs = ref([])
const jobsLoading = ref(false)
const stats = ref(null)

const statsItems = computed(() => {
  if (!stats.value) return []
  return [
    { label: '入驻企业', value: stats.value.companyCount || 0 },
    { label: '招聘职位', value: stats.value.jobCount || 0 },
    { label: '求职学生', value: stats.value.studentCount || 0 },
    { label: '投递次数', value: stats.value.applicationCount || 0 }
  ]
})

const categories = [
  { label: '技术开发', icon: 'Dev', color: 'linear-gradient(135deg, #2563eb, #14b8a6)', type: '技术' },
  { label: '产品运营', icon: 'Ops', color: 'linear-gradient(135deg, #16a34a, #65a30d)', type: '产品' },
  { label: '设计创意', icon: 'UI', color: 'linear-gradient(135deg, #f59e0b, #ef4444)', type: '设计' },
  { label: '市场营销', icon: 'Mkt', color: 'linear-gradient(135deg, #f43f5e, #ec4899)', type: '市场' },
  { label: '人事行政', icon: 'HR', color: 'linear-gradient(135deg, #7c3aed, #2563eb)', type: '人事' },
  { label: '财务法务', icon: 'Fin', color: 'linear-gradient(135deg, #0891b2, #0f766e)', type: '财务' }
]

const steps = [
  { title: '注册账号', desc: '创建学生或企业账号，完善个人或企业信息' },
  { title: '搜索投递', desc: '浏览职位信息，一键投递心仪的岗位' },
  { title: '面试入职', desc: '收到面试邀请，通过面试拿到offer' }
]

function handleSearch() {
  const query = {}
  if (searchKeyword.value) query.jobTitle = searchKeyword.value
  if (searchCity.value) query.workCity = searchCity.value
  router.push({ path: '/portal/student/jobs', query })
}

function searchByCategory(keyword) {
  router.push({ path: '/portal/student/jobs', query: { jobTitle: keyword } })
}

function goJobDetail(job) {
  router.push({ path: `/portal/student/job/${job.jobId}` })
}

onMounted(async () => {
  jobsLoading.value = true
  try {
    const [jobRes, statsRes] = await Promise.all([
      listJob({ status: '1', pageSize: 6, pageNum: 1 }),
      getStatisticsOverview().catch(() => null)
    ])
    latestJobs.value = jobRes.rows || []
    if (statsRes) stats.value = statsRes
  } finally {
    jobsLoading.value = false
  }
})
</script>

<style lang="scss" scoped>
.hero {
  position: relative;
  border-radius: 8px;
  padding: 64px 52px;
  margin-bottom: 36px;
  overflow: hidden;
  background:
    linear-gradient(120deg, rgba(12, 21, 45, 0.96) 0%, rgba(25, 55, 109, 0.92) 50%, rgba(20, 184, 166, 0.78) 100%),
    url("../../assets/images/login-background.jpg") center / cover;
  color: #fff;
  box-shadow: 0 24px 64px rgba(15, 23, 42, 0.18);

  &__content {
    position: relative;
    z-index: 2;
    max-width: 640px;
  }

  &__title {
    font-size: 42px;
    font-weight: 800;
    line-height: 1.3;
    margin: 0 0 12px;
    letter-spacing: 0;

    .highlight {
      background: linear-gradient(135deg, #93c5fd, #5eead4);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }
  }

  &__subtitle {
    font-size: 16px;
    opacity: 0.86;
    margin: 0 0 28px;
    line-height: 1.6;
  }

  &__search {
    .search-box {
      display: flex;
      gap: 8px;
      background: rgba(255, 255, 255, 0.16);
      padding: 8px;
      border-radius: 8px;
      border: 1px solid rgba(255, 255, 255, 0.16);
      backdrop-filter: blur(12px);
      box-shadow: 0 18px 42px rgba(0, 0, 0, 0.18);
    }

    :deep(.el-input__wrapper) {
      border-radius: 7px;
      background: rgba(255, 255, 255, 0.94);
      box-shadow: none;
    }

    .search-btn {
      padding: 0 28px;
      font-weight: 600;
      border-radius: 7px;
      border: none;
      background: linear-gradient(135deg, #2563eb, #14b8a6);
    }
  }

  &__bg {
    position: absolute;
    inset: 0;
    background:
      linear-gradient(90deg, transparent 0%, rgba(255, 255, 255, 0.08) 55%, rgba(255, 255, 255, 0.16) 100%),
      linear-gradient(rgba(255, 255, 255, 0.12) 1px, transparent 1px),
      linear-gradient(90deg, rgba(255, 255, 255, 0.12) 1px, transparent 1px);
    background-size: auto, 48px 48px, 48px 48px;
    mask-image: linear-gradient(90deg, transparent 30%, #000 100%);
    z-index: 1;
    animation: hero-grid-drift 22s linear infinite;
  }
}

.stats-section {
  margin-bottom: 36px;

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
    background: #fff;
    border-radius: 8px;
    padding: 28px 32px;
    border: 1px solid rgba(31, 41, 55, 0.08);
    box-shadow: var(--portal-shadow-sm);
  }

  .stat-item {
    text-align: center;
    position: relative;

    & + .stat-item::before {
      content: "";
      position: absolute;
      left: -8px;
      top: 12%;
      width: 1px;
      height: 76%;
      background: linear-gradient(180deg, transparent, rgba(31, 41, 55, 0.12), transparent);
    }

    &__value {
      font-size: 28px;
      font-weight: 800;
      color: #172033;
      letter-spacing: 0;
    }

    &__label {
      font-size: 13px;
      color: #909399;
      margin-top: 4px;
    }
  }
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 16px;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 20px 12px;
  background: #fff;
  border-radius: 8px;
  border: 1px solid rgba(31, 41, 55, 0.08);
  cursor: pointer;
  box-shadow: var(--portal-shadow-sm);
  transition: transform 0.25s ease, box-shadow 0.25s ease, border-color 0.25s ease;

  &:hover {
    box-shadow: var(--portal-shadow-md);
    transform: translateY(-4px);
    border-color: rgba(37, 99, 235, 0.16);
  }

  &__icon {
    width: 44px;
    height: 44px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-weight: 700;
    font-size: 13px;
    letter-spacing: 0;
  }

  &__label {
    font-size: 13px;
    font-weight: 600;
    color: #303133;
  }
}

.steps-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.step-card {
  background: #fff;
  border-radius: 8px;
  padding: 28px 24px;
  border: 1px solid rgba(31, 41, 55, 0.08);
  text-align: center;
  box-shadow: var(--portal-shadow-sm);
  transition: transform 0.25s ease, box-shadow 0.25s ease;

  &:hover {
    transform: translateY(-4px);
    box-shadow: var(--portal-shadow-md);
  }

  &__number {
    width: 40px;
    height: 40px;
    border-radius: 8px;
    background: linear-gradient(135deg, #2563eb, #14b8a6);
    color: #fff;
    font-weight: 800;
    font-size: 18px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 16px;
  }

  &__title {
    font-size: 16px;
    font-weight: 700;
    color: #1a1a2e;
    margin: 0 0 8px;
  }

  &__desc {
    font-size: 13px;
    color: #909399;
    margin: 0;
    line-height: 1.6;
  }
}

@keyframes hero-grid-drift {
  from {
    background-position: 0 0, 0 0, 0 0;
  }
  to {
    background-position: 0 0, 48px 48px, 48px 48px;
  }
}

@media (max-width: 1024px) {
  .categories-grid { grid-template-columns: repeat(3, 1fr); }
  .stats-section .stats-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 768px) {
  .hero {
    padding: 36px 20px;

    &__title { font-size: 26px; }

    &__search .search-box {
      flex-direction: column;
    }
  }

  .categories-grid { grid-template-columns: repeat(2, 1fr); }
  .steps-grid { grid-template-columns: 1fr; }
  .stats-section .stats-grid { grid-template-columns: repeat(2, 1fr); padding: 20px; }
}
</style>
