<template>
  <div class="student-dashboard">
    <h1 class="portal-page-title">欢迎回来，{{ userStore.nickName }}</h1>

    <!-- Stats -->
    <div class="portal-grid portal-grid--4" style="margin-bottom: 28px">
      <StatCard :value="stats.applyCount" label="投递数" :icon="Document" color="blue" />
      <StatCard :value="stats.interviewCount" label="面试数" :icon="Calendar" color="green" />
      <StatCard :value="stats.favoriteCount" label="收藏数" :icon="StarFilled" color="orange" />
      <StatCard :value="stats.offerCount" label="Offer" :icon="Trophy" color="purple" />
    </div>

    <div class="dashboard-body">
      <!-- Recent Applications -->
      <div class="portal-section">
        <div class="portal-section__header">
          <h2>最近投递</h2>
          <router-link to="/portal/student/applications" class="section-link">查看全部</router-link>
        </div>
        <div class="app-list" v-loading="appLoading">
          <div v-for="app in recentApps" :key="app.applicationId" class="app-item">
            <div class="app-item__left">
              <div class="app-item__title">{{ app.jobTitle }}</div>
              <div class="app-item__company">{{ app.companyName }}</div>
            </div>
            <div class="app-item__right">
              <el-tag :type="getStatusType(app.status)" size="small" class="portal-tag">
                {{ getStatusLabel(app.status) }}
              </el-tag>
              <span class="app-item__time">{{ parseTime(app.applyTime, '{m}-{d}') }}</span>
            </div>
          </div>
          <div class="portal-empty" v-if="!appLoading && recentApps.length === 0">
            <div class="portal-empty__text">还没有投递记录</div>
            <router-link to="/portal/student/jobs">
              <el-button type="primary" size="small">去投递</el-button>
            </router-link>
          </div>
        </div>
      </div>

      <!-- Recommended Jobs -->
      <div class="portal-section">
        <div class="portal-section__header">
          <h2>推荐职位</h2>
          <router-link to="/portal/student/jobs" class="section-link">更多职位</router-link>
        </div>
        <div class="portal-grid portal-grid--3" v-loading="jobLoading">
          <JobCard
            v-for="job in recommendJobs"
            :key="job.jobId"
            :job="job"
            :show-favorite="false"
            @click="router.push(`/portal/student/job/${job.jobId}`)"
          />
        </div>
      </div>

      <!-- Upcoming Interviews -->
      <div class="portal-section" v-if="upcomingInterviews.length > 0">
        <div class="portal-section__header">
          <h2>即将到来的面试</h2>
          <router-link to="/portal/student/interviews" class="section-link">查看全部</router-link>
        </div>
        <div class="interview-list">
          <div v-for="item in upcomingInterviews" :key="item.interviewId" class="interview-item">
            <div class="interview-item__time">
              <div class="time-day">{{ parseTime(item.interviewTime, '{d}') }}</div>
              <div class="time-month">{{ parseTime(item.interviewTime, '{M}月') }}</div>
            </div>
            <div class="interview-item__info">
              <div class="info-title">{{ item.jobTitle }}</div>
              <div class="info-detail">
                {{ item.companyName }} · {{ formatInterviewType(item.interviewType) }} · {{ formatMethod(item.interviewMethod) }}
              </div>
            </div>
            <el-tag :type="item.status === '1' ? 'success' : 'warning'" size="small" class="portal-tag">
              {{ item.status === '1' ? '已确认' : '待确认' }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Document, Calendar, StarFilled, Trophy } from '@element-plus/icons-vue'
import { listApplication } from '@/api/crems/application'
import { listInterview } from '@/api/crems/interview'
import { listFavorite } from '@/api/crems/favorite'
import { listJob } from '@/api/crems/job'
import { parseTime } from '@/utils/ruoyi'
import useUserStore from '@/store/modules/user'
import StatCard from '@/components/portal/StatCard.vue'
import JobCard from '@/components/portal/JobCard.vue'

const router = useRouter()
const userStore = useUserStore()

const stats = reactive({ applyCount: 0, interviewCount: 0, favoriteCount: 0, offerCount: 0 })
const recentApps = ref([])
const recommendJobs = ref([])
const upcomingInterviews = ref([])
const appLoading = ref(false)
const jobLoading = ref(false)

const statusMap = {
  '0': { label: '待查看', type: 'warning' },
  '1': { label: '已查看', type: '' },
  '2': { label: '初筛通过', type: 'success' },
  '3': { label: '面试邀请', type: 'success' },
  '4': { label: '已拒绝', type: 'danger' },
  '5': { label: '已录用', type: 'success' }
}
const getStatusLabel = (s) => statusMap[s]?.label || s
const getStatusType = (s) => statusMap[s]?.type || 'info'

const interviewTypeMap = { first: '初试', second: '复试', final: '终试' }
const methodMap = { onsite: '现场', video: '视频', phone: '电话' }
const formatInterviewType = (t) => interviewTypeMap[t] || t
const formatMethod = (m) => methodMap[m] || m

onMounted(async () => {
  appLoading.value = true
  jobLoading.value = true

  listApplication({ pageSize: 5, pageNum: 1 }).then(res => {
    recentApps.value = res.rows || []
    stats.applyCount = res.total || 0
  }).finally(() => { appLoading.value = false })

  listJob({ status: '1', pageSize: 6, pageNum: 1 }).then(res => {
    recommendJobs.value = res.rows || []
  }).finally(() => { jobLoading.value = false })

  listInterview({ pageSize: 5, pageNum: 1 }).then(res => {
    upcomingInterviews.value = (res.rows || []).filter(i => i.status === '0' || i.status === '1')
    stats.interviewCount = res.total || 0
  }).catch(() => {})

  listFavorite({ pageSize: 1, pageNum: 1 }).then(res => {
    stats.favoriteCount = res.total || 0
  }).catch(() => {})
})
</script>

<style lang="scss" scoped>
.app-list {
  background: #fff;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
  overflow: hidden;
}

.app-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 20px;
  border-bottom: 1px solid #fafafa;
  transition: background 0.15s;

  &:hover { background: #fafbfc; }
  &:last-child { border-bottom: none; }

  &__left { flex: 1; min-width: 0; }

  &__title {
    font-size: 14px;
    font-weight: 600;
    color: #303133;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  &__company {
    font-size: 12px;
    color: #909399;
    margin-top: 2px;
  }

  &__right {
    display: flex;
    align-items: center;
    gap: 12px;
    flex-shrink: 0;
  }

  &__time {
    font-size: 12px;
    color: #c0c4cc;
  }
}

.interview-list {
  background: #fff;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
  overflow: hidden;
}

.interview-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 20px;
  border-bottom: 1px solid #fafafa;

  &:last-child { border-bottom: none; }

  &__time {
    text-align: center;
    min-width: 48px;

    .time-day {
      font-size: 24px;
      font-weight: 800;
      color: #409eff;
      line-height: 1;
    }

    .time-month {
      font-size: 12px;
      color: #909399;
      margin-top: 2px;
    }
  }

  &__info {
    flex: 1;
    min-width: 0;

    .info-title {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
    }

    .info-detail {
      font-size: 12px;
      color: #909399;
      margin-top: 2px;
    }
  }
}

@media (max-width: 768px) {
  .portal-grid--4 { grid-template-columns: repeat(2, 1fr); }
}
</style>
