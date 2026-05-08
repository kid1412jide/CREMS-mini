<template>
  <div class="job-card" @click="$emit('click', job)">
    <div class="job-card__header">
      <div class="job-card__title">{{ job.jobTitle }}</div>
      <div class="job-card__salary">
        {{ job.salaryMin }}-{{ job.salaryMax }}元/月
      </div>
    </div>
    <div class="job-card__tags">
      <span class="tag">{{ job.workCity || '未填写' }}</span>
      <span class="tag" v-if="job.jobType">{{ formatJobType(job.jobType) }}</span>
      <span class="tag" v-if="job.educationRequired">{{ formatEducation(job.educationRequired) }}</span>
      <span class="tag" v-if="job.recruitNum">招{{ job.recruitNum }}人</span>
    </div>
    <div class="job-card__company">
      <div class="company-icon">{{ (job.companyName || '企').charAt(0) }}</div>
      <div class="company-info">
        <span class="company-name">{{ job.companyName }}</span>
      </div>
    </div>
    <div class="job-card__footer">
      <span class="publish-time">{{ formatTime(job.publishDate) }}</span>
      <div class="actions" @click.stop>
        <el-button
          v-if="showFavorite !== false"
          :type="favorited ? 'warning' : 'default'"
          :icon="favorited ? Star : StarFilled"
          size="small"
          circle
          @click.stop="$emit('favorite', job)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { Star, StarFilled } from '@element-plus/icons-vue'

const props = defineProps({
  job: { type: Object, required: true },
  favorited: { type: Boolean, default: false },
  showFavorite: { type: Boolean, default: true }
})

defineEmits(['click', 'favorite'])

const jobTypeMap = { full_time: '全职', internship: '实习', part_time: '兼职' }
const eduMap = { junior_college: '专科', bachelor: '本科', master: '硕士', doctor: '博士' }

function formatJobType(type) { return jobTypeMap[type] || type }
function formatEducation(edu) { return eduMap[edu] || edu || '不限' }

function formatTime(date) {
  if (!date) return ''
  const d = new Date(date)
  const now = new Date()
  const diff = Math.floor((now - d) / 86400000)
  if (diff === 0) return '今天'
  if (diff === 1) return '昨天'
  if (diff < 7) return `${diff}天前`
  if (diff < 30) return `${Math.floor(diff / 7)}周前`
  return `${d.getMonth() + 1}月${d.getDate()}日`
}
</script>

<style lang="scss" scoped>
.job-card {
  background: #fff;
  border-radius: 10px;
  padding: 20px;
  border: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.25s ease;

  &:hover {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
    transform: translateY(-2px);
    border-color: transparent;
  }

  &__header {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    margin-bottom: 12px;
    gap: 12px;
  }

  &__title {
    font-size: 16px;
    font-weight: 700;
    color: #1a1a2e;
    line-height: 1.4;
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  &__salary {
    font-size: 15px;
    font-weight: 700;
    color: #f56c6c;
    white-space: nowrap;
    flex-shrink: 0;
  }

  &__tags {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
    margin-bottom: 14px;

    .tag {
      padding: 2px 10px;
      border-radius: 4px;
      font-size: 12px;
      font-weight: 500;
      color: #606266;
      background: #f5f7fa;
    }
  }

  &__company {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 14px;
    padding-bottom: 14px;
    border-bottom: 1px solid #f5f5f5;

    .company-icon {
      width: 32px;
      height: 32px;
      border-radius: 6px;
      background: linear-gradient(135deg, #e8f4fd, #d1e9ff);
      color: #409eff;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 700;
      font-size: 14px;
      flex-shrink: 0;
    }

    .company-info {
      flex: 1;
      min-width: 0;
    }

    .company-name {
      font-size: 13px;
      color: #606266;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      display: block;
    }
  }

  &__footer {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .publish-time {
      font-size: 12px;
      color: #c0c4cc;
    }
  }
}
</style>
