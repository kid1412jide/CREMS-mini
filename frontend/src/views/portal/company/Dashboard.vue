<template>
  <div class="company-dashboard">
    <h1 class="portal-page-title">企业工作台</h1>

    <!-- Stats -->
    <div class="portal-grid portal-grid--4" style="margin-bottom: 28px">
      <StatCard :value="stats.jobCount" label="发布职位" :icon="Briefcase" color="blue" />
      <StatCard :value="stats.applyCount" label="收到投递" :icon="Document" color="green" />
      <StatCard :value="stats.pendingCount" label="待处理" :icon="Bell" color="orange" />
      <StatCard :value="stats.interviewCount" label="面试安排" :icon="Calendar" color="purple" />
    </div>

    <!-- Quick actions -->
    <div class="quick-actions" style="margin-bottom: 28px">
      <el-button type="primary" @click="router.push('/portal/company/jobs')">
        <el-icon><Plus /></el-icon>发布新职位
      </el-button>
      <el-button @click="router.push('/portal/company/applications')">
        <el-icon><Document /></el-icon>查看投递
      </el-button>
      <el-button @click="router.push('/portal/company/interviews')">
        <el-icon><Calendar /></el-icon>面试管理
      </el-button>
    </div>

    <!-- Recent applications -->
    <div class="portal-section">
      <div class="portal-section__header">
        <h2>最新投递</h2>
        <router-link to="/portal/company/applications" class="section-link">查看全部</router-link>
      </div>
      <div class="recent-table" v-loading="loading">
        <div v-for="app in recentApps" :key="app.applicationId" class="recent-row">
          <div class="recent-row__left">
            <div class="student-name">{{ app.studentName }}</div>
            <div class="job-title">投递：{{ app.jobTitle }}</div>
          </div>
          <div class="recent-row__right">
            <el-tag :type="_getStatusType(app.status)" size="small" class="portal-tag">
              {{ _getStatusLabel(app.status) }}
            </el-tag>
            <span class="apply-time">{{ parseTime(app.applyTime, '{m}-{d} {h}:{i}') }}</span>
          </div>
        </div>
        <div class="portal-empty" v-if="!loading && recentApps.length === 0">
          <div class="portal-empty__text">暂无投递记录</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Briefcase, Document, Bell, Calendar, Plus } from '@element-plus/icons-vue'
import { listJob, listApplication, listInterview } from '@/api/portal'
import { parseTime } from '@/utils/crems'
import { APPLICATION_STATUS_MAP, getStatusLabel, getStatusType } from '@/constants/crems'
import StatCard from '@/components/portal/StatCard.vue'

const router = useRouter()

const loading = ref(false)
const recentApps = ref([])
const stats = reactive({ jobCount: 0, applyCount: 0, pendingCount: 0, interviewCount: 0 })

const _getStatusLabel = (s) => getStatusLabel(APPLICATION_STATUS_MAP, s)
const _getStatusType = (s) => getStatusType(APPLICATION_STATUS_MAP, s)

onMounted(async () => {
  loading.value = true

  listJob({ pageSize: 1, pageNum: 1 }).then(res => {
    stats.jobCount = res.total || 0
  }).catch(() => {})

  listApplication({ pageSize: 5, pageNum: 1 }).then(res => {
    recentApps.value = res.rows || []
    stats.applyCount = res.total || 0
  }).catch(() => {}).finally(() => { loading.value = false })

  listApplication({ status: '0', pageSize: 1, pageNum: 1 }).then(res => {
    stats.pendingCount = res.total || 0
  }).catch(() => {})

  listInterview({ pageSize: 1, pageNum: 1 }).then(res => {
    stats.interviewCount = res.total || 0
  }).catch(() => {})
})
</script>

<style lang="scss" scoped>
.quick-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.recent-table {
  background: #fff;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
  overflow: hidden;
}

.recent-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 20px;
  border-bottom: 1px solid #fafafa;
  transition: background 0.15s;

  &:hover { background: #fafbfc; }
  &:last-child { border-bottom: none; }

  &__left {
    .student-name {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
    }

    .job-title {
      font-size: 12px;
      color: #909399;
      margin-top: 2px;
    }
  }

  &__right {
    display: flex;
    align-items: center;
    gap: 12px;

    .apply-time {
      font-size: 12px;
      color: #c0c4cc;
    }
  }
}
</style>
