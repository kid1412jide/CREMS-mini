<template>
  <div class="interviews-page">
    <h1 class="portal-page-title">我的面试</h1>

    <div class="interview-list" v-loading="loading">
      <div v-for="item in interviewList" :key="item.interviewId" class="iv-card">
        <div class="iv-card__left">
          <div class="iv-card__date">
            <div class="date-day">{{ parseTime(item.interviewTime, '{d}') }}</div>
            <div class="date-month">{{ parseTime(item.interviewTime, '{m}月') }}</div>
          </div>
        </div>
        <div class="iv-card__main">
          <div class="iv-card__header">
            <div class="iv-card__title">{{ item.jobTitle }}</div>
            <el-tag :type="_getStatusType(item.status)" size="small" class="portal-tag">
              {{ _getStatusLabel(item.status) }}
            </el-tag>
          </div>
          <div class="iv-card__company">{{ item.companyName }}</div>
          <div class="iv-card__details">
            <span class="detail-item">
              <el-icon><Clock /></el-icon>
              {{ parseTime(item.interviewTime, '{h}:{i}') }}
            </span>
            <span class="detail-item" v-if="item.interviewType">
              <el-icon><User /></el-icon>
              {{ formatType(item.interviewType) }}
            </span>
            <span class="detail-item" v-if="item.interviewMethod">
              <el-icon><Monitor /></el-icon>
              {{ formatMethod(item.interviewMethod) }}
            </span>
            <span class="detail-item" v-if="item.interviewAddress">
              <el-icon><Location /></el-icon>
              {{ item.interviewAddress }}
            </span>
          </div>
          <div class="iv-card__notice" v-if="item.interviewNotice">
            <div class="notice-label">面试须知</div>
            <div class="notice-text">{{ item.interviewNotice }}</div>
          </div>
        </div>
      </div>
    </div>

    <div class="portal-empty" v-if="!loading && interviewList.length === 0">
      <div class="portal-empty__text">暂无面试安排</div>
    </div>

    <div style="margin-top: 24px; display: flex; justify-content: center" v-if="total > 0">
      <pagination :total="total" v-model:page="quecremsParams.pageNum" v-model:limit="quecremsParams.pageSize" @pagination="getList" />
    </div>
  </div>
</template>

<script setup>
import { Clock, User, Monitor, Location } from '@element-plus/icons-vue'
import { listInterview } from '@/api/portal'
import { parseTime } from '@/utils/crems'
import { INTERVIEW_STATUS_MAP, INTERVIEW_TYPE_MAP, INTERVIEW_METHOD_MAP, getStatusLabel, getStatusType } from '@/constants/crems'

const loading = ref(false)
const interviewList = ref([])
const total = ref(0)

const quecremsParams = reactive({ pageNum: 1, pageSize: 10 })

const _getStatusLabel = (s) => getStatusLabel(INTERVIEW_STATUS_MAP, s)
const _getStatusType = (s) => getStatusType(INTERVIEW_STATUS_MAP, s)
const formatType = (t) => INTERVIEW_TYPE_MAP[t] || t
const formatMethod = (m) => INTERVIEW_METHOD_MAP[m] || m

function getList() {
  loading.value = true
  listInterview(quecremsParams).then(res => {
    interviewList.value = res.rows || []
    total.value = res.total || 0
  }).finally(() => { loading.value = false })
}

onMounted(() => getList())
</script>

<style lang="scss" scoped>
.interview-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.iv-card {
  display: flex;
  gap: 20px;
  background: #fff;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
  padding: 20px;
  transition: box-shadow 0.2s;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  }

  &__left {
    flex-shrink: 0;
  }

  &__date {
    text-align: center;
    min-width: 56px;
    padding: 8px 0;

    .date-day {
      font-size: 28px;
      font-weight: 800;
      color: #409eff;
      line-height: 1;
    }

    .date-month {
      font-size: 13px;
      color: #909399;
      margin-top: 4px;
    }
  }

  &__main {
    flex: 1;
    min-width: 0;
  }

  &__header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
  }

  &__title {
    font-size: 16px;
    font-weight: 700;
    color: #1a1a2e;
  }

  &__company {
    font-size: 13px;
    color: #909399;
    margin-top: 4px;
  }

  &__details {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    margin-top: 12px;

    .detail-item {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 13px;
      color: #606266;
    }
  }

  &__notice {
    margin-top: 12px;
    padding: 12px;
    background: #f5f7fa;
    border-radius: 6px;

    .notice-label {
      font-size: 12px;
      font-weight: 600;
      color: #909399;
      margin-bottom: 4px;
    }

    .notice-text {
      font-size: 13px;
      color: #606266;
      line-height: 1.6;
    }
  }
}
</style>
