<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #409eff;">
            <el-icon :size="30"><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overview.companyCount || 0 }}</div>
            <div class="stat-label">企业总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #67c23a;">
            <el-icon :size="30"><UserFilled /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overview.studentCount || 0 }}</div>
            <div class="stat-label">学生总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #e6a23c;">
            <el-icon :size="30"><Briefcase /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overview.jobCount || 0 }}</div>
            <div class="stat-label">职位总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #f56c6c;">
            <el-icon :size="30"><Document /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overview.applicationCount || 0 }}</div>
            <div class="stat-label">投递总数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #909399;">
            <el-icon :size="30"><CircleCheck /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overview.certifiedCompanyCount || 0 }}</div>
            <div class="stat-label">已认证企业</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #c71585;">
            <el-icon :size="30"><Position /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overview.publishedJobCount || 0 }}</div>
            <div class="stat-label">已发布职位</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #00ced1;">
            <el-icon :size="30"><Calendar /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overview.interviewCount || 0 }}</div>
            <div class="stat-label">面试总数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>职位类型分布</span>
          </template>
          <div v-loading="jobTypeLoading">
            <el-table :data="jobTypeData" size="small">
              <el-table-column prop="groupName" label="职位类型" />
              <el-table-column prop="jobCount" label="职位数量" />
            </el-table>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>投递状态分布</span>
          </template>
          <div v-loading="appStatusLoading">
            <el-table :data="appStatusData" size="small">
              <el-table-column prop="groupName" label="状态">
                <template #default="scope">
                  <span v-if="scope.row.groupName === '0'">待查看</span>
                  <span v-else-if="scope.row.groupName === '1'">已查看</span>
                  <span v-else-if="scope.row.groupName === '2'">初筛通过</span>
                  <span v-else-if="scope.row.groupName === '3'">面试邀请</span>
                  <span v-else-if="scope.row.groupName === '4'">已拒绝</span>
                  <span v-else-if="scope.row.groupName === '5'">已录用</span>
                  <span v-else>{{ scope.row.groupName }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="applicationCount" label="投递数量" />
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>城市职位分布（TOP 10）</span>
          </template>
          <div v-loading="jobCityLoading">
            <el-table :data="jobCityData" size="small">
              <el-table-column prop="groupName" label="城市" />
              <el-table-column prop="jobCount" label="职位数量" />
            </el-table>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>面试结果分布</span>
          </template>
          <div v-loading="interviewResultLoading">
            <el-table :data="interviewResultData" size="small">
              <el-table-column prop="groupName" label="结果">
                <template #default="scope">
                  <span v-if="scope.row.groupName === 'pass'">通过</span>
                  <span v-else-if="scope.row.groupName === 'fail'">未通过</span>
                  <span v-else-if="scope.row.groupName === 'pending'">待定</span>
                  <span v-else>{{ scope.row.groupName }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="interviewCount" label="面试数量" />
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Statistics">
import { getStatisticsOverview, getJobStatisticsByType, getJobStatisticsByCity, getApplicationStatisticsByStatus, getInterviewStatisticsByResult } from "@/api/crems/statistics"

const overview = ref({})
const jobTypeData = ref([])
const jobCityData = ref([])
const appStatusData = ref([])
const interviewResultData = ref([])
const jobTypeLoading = ref(true)
const jobCityLoading = ref(true)
const appStatusLoading = ref(true)
const interviewResultLoading = ref(true)

function loadOverview() {
  getStatisticsOverview().then(res => {
    overview.value = res.data || {}
  })
}

function loadJobType() {
  jobTypeLoading.value = true
  getJobStatisticsByType().then(res => {
    jobTypeData.value = res.data || []
    jobTypeLoading.value = false
  })
}

function loadJobCity() {
  jobCityLoading.value = true
  getJobStatisticsByCity().then(res => {
    jobCityData.value = res.data || []
    jobCityLoading.value = false
  })
}

function loadAppStatus() {
  appStatusLoading.value = true
  getApplicationStatisticsByStatus().then(res => {
    appStatusData.value = res.data || []
    appStatusLoading.value = false
  })
}

function loadInterviewResult() {
  interviewResultLoading.value = true
  getInterviewStatisticsByResult().then(res => {
    interviewResultData.value = res.data || []
    interviewResultLoading.value = false
  })
}

onMounted(() => {
  loadOverview()
  loadJobType()
  loadJobCity()
  loadAppStatus()
  loadInterviewResult()
})
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
}
.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-right: 20px;
}
.stat-content {
  flex: 1;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}
</style>