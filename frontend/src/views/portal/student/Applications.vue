<template>
  <div class="applications-page">
    <h1 class="portal-page-title">我的投递</h1>

    <!-- Status filter -->
    <div class="status-filter">
      <el-radio-group v-model="quecremsParams.status" @change="handleQuery">
        <el-radio-button value="">全部</el-radio-button>
        <el-radio-button value="0">待查看</el-radio-button>
        <el-radio-button value="1">已查看</el-radio-button>
        <el-radio-button value="2">初筛通过</el-radio-button>
        <el-radio-button value="3">面试邀请</el-radio-button>
        <el-radio-button value="4">已拒绝</el-radio-button>
        <el-radio-button value="5">已录用</el-radio-button>
      </el-radio-group>
    </div>

    <!-- Application list -->
    <div class="app-list" v-loading="loading">
      <div v-for="(app, index) in appList" :key="app.applicationId" class="app-card" v-anime-stagger="index * 60">
        <div class="app-card__header">
          <div class="app-card__job">
            <div class="job-title">{{ app.jobTitle }}</div>
            <div class="company-name">{{ app.companyName }}</div>
          </div>
          <el-tag :type="_getStatusType(app.status)" class="portal-tag">
            {{ _getStatusLabel(app.status) }}
          </el-tag>
        </div>
        <div class="app-card__body">
          <div class="meta-item">
            <span class="meta-label">投递时间</span>
            <span>{{ parseTime(app.applyTime) }}</span>
          </div>
          <div class="meta-item" v-if="app.viewTime">
            <span class="meta-label">查看时间</span>
            <span>{{ parseTime(app.viewTime) }}</span>
          </div>
          <div class="meta-item" v-if="app.feedback">
            <span class="meta-label">企业反馈</span>
            <span class="feedback-text">{{ app.feedback }}</span>
          </div>
        </div>
        <div class="app-card__footer">
          <span></span>
          <el-button type="primary" link @click="handleView(app)">
            {{ expandedId === app.applicationId ? '收起详情' : '查看详情' }}
          </el-button>
        </div>
        <transition name="detail-expand">
          <div v-if="expandedId === app.applicationId" class="app-detail">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="投递ID">{{ viewData.applicationId }}</el-descriptions-item>
              <el-descriptions-item label="职位名称">{{ viewData.jobTitle }}</el-descriptions-item>
              <el-descriptions-item label="企业名称">{{ viewData.companyName }}</el-descriptions-item>
              <el-descriptions-item label="投递时间">{{ parseTime(viewData.applyTime) }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="_getStatusType(viewData.status)">{{ _getStatusLabel(viewData.status) }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="查看时间">{{ parseTime(viewData.viewTime) || '-' }}</el-descriptions-item>
              <el-descriptions-item label="求职信" :span="2">{{ viewData.coverLetter || '未填写' }}</el-descriptions-item>
              <el-descriptions-item label="企业反馈" :span="2">{{ viewData.feedback || '-' }}</el-descriptions-item>
              <el-descriptions-item label="投递简历" :span="2">
                <el-button v-if="viewData.resumeUrl" type="primary" link @click="downloadResume(viewData.resumeUrl)">
                  下载简历
                </el-button>
                <span v-else>未上传</span>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </transition>
      </div>
    </div>

    <div class="portal-empty" v-if="!loading && appList.length === 0">
      <div class="portal-empty__text">暂无投递记录</div>
      <router-link to="/portal/student/jobs">
        <el-button type="primary" size="small">去投递</el-button>
      </router-link>
    </div>

    <div style="margin-top: 24px; display: flex; justify-content: center" v-if="total > 0">
      <pagination :total="total" v-model:page="quecremsParams.pageNum" v-model:limit="quecremsParams.pageSize" @pagination="getList" />
    </div>
  </div>
</template>

<script setup>
import { listApplication } from '@/api/portal'
import { parseTime } from '@/utils/crems'
import { APPLICATION_STATUS_MAP, getStatusLabel, getStatusType } from '@/constants/crems'

const loading = ref(false)
const appList = ref([])
const total = ref(0)
const expandedId = ref(null)
const viewData = ref({})

const quecremsParams = reactive({
  pageNum: 1,
  pageSize: 10,
  status: ''
})

const _getStatusLabel = (s) => getStatusLabel(APPLICATION_STATUS_MAP, s)
const _getStatusType = (s) => getStatusType(APPLICATION_STATUS_MAP, s)

function getList() {
  loading.value = true
  const params = { ...quecremsParams }
  if (params.status === '') delete params.status
  listApplication(params).then(res => {
    appList.value = res.rows || []
    total.value = res.total || 0
    expandedId.value = null
    viewData.value = {}
  }).finally(() => { loading.value = false })
}

function handleQuery() {
  quecremsParams.pageNum = 1
  getList()
}

function handleView(app) {
  if (expandedId.value === app.applicationId) {
    expandedId.value = null
    viewData.value = {}
    return
  }
  viewData.value = app
  expandedId.value = app.applicationId
}

function downloadResume(url) {
  if (url) {
    window.open(url)
  }
}

onMounted(() => getList())
</script>

<style lang="scss" scoped>
.status-filter {
  margin-bottom: 20px;
}

.app-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.app-card {
  background: #fff;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
  padding: 20px;
  transition: box-shadow 0.2s;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  }

  &__header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 14px;
  }

  &__job {
    .job-title {
      font-size: 16px;
      font-weight: 700;
      color: #1a1a2e;
    }

    .company-name {
      font-size: 13px;
      color: #909399;
      margin-top: 4px;
    }
  }

  &__body {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;

    .meta-item {
      font-size: 13px;
      color: #606266;

      .meta-label {
        color: #909399;
        margin-right: 6px;
      }

      .feedback-text {
        color: #409eff;
        font-weight: 500;
      }
    }
  }

  &__footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-top: 12px;
    border-top: 1px solid #f5f5f5;
    margin-top: 10px;
  }
}

.app-detail {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px dashed rgba(37, 99, 235, 0.2);
  overflow: hidden;
}

.detail-expand-enter-active,
.detail-expand-leave-active {
  transition: opacity 0.22s ease, transform 0.22s ease;
}

.detail-expand-enter-from,
.detail-expand-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}

@media (max-width: 768px) {
  .app-detail :deep(.el-descriptions__body .el-descriptions__table) {
    display: block;
  }

  .app-detail :deep(.el-descriptions__body tbody),
  .app-detail :deep(.el-descriptions__body tr),
  .app-detail :deep(.el-descriptions__body th),
  .app-detail :deep(.el-descriptions__body td) {
    display: block;
    width: 100% !important;
  }
}
</style>
