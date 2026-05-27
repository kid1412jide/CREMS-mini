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
          <div ref="jobTypeChart" v-loading="jobTypeLoading" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>投递状态分布</span>
          </template>
          <div ref="appStatusChart" v-loading="appStatusLoading" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>城市职位分布（TOP 10）</span>
          </template>
          <div ref="jobCityChart" v-loading="jobCityLoading" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>面试结果分布</span>
          </template>
          <div ref="interviewResultChart" v-loading="interviewResultLoading" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Statistics">
import { getStatisticsOverview, getJobStatisticsByType, getJobStatisticsByCity, getApplicationStatisticsByStatus, getInterviewStatisticsByResult } from "@/api/crems/statistics"
import * as echarts from 'echarts/core'
import { PieChart, BarChart } from 'echarts/charts'
import { TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { GraphicComponent } from 'echarts/components'

echarts.use([PieChart, BarChart, TooltipComponent, LegendComponent, GridComponent, CanvasRenderer, GraphicComponent])

const overview = ref({})
const jobTypeData = ref([])
const jobCityData = ref([])
const appStatusData = ref([])
const interviewResultData = ref([])
const jobTypeLoading = ref(true)
const jobCityLoading = ref(true)
const appStatusLoading = ref(true)
const interviewResultLoading = ref(true)

// 图表引用
const jobTypeChart = ref(null)
const appStatusChart = ref(null)
const jobCityChart = ref(null)
const interviewResultChart = ref(null)

// 图表实例
let jobTypeChartInstance = null
let appStatusChartInstance = null
let jobCityChartInstance = null
let interviewResultChartInstance = null

const overviewLoading = ref(true)

function loadOverview() {
  overviewLoading.value = true
  getStatisticsOverview().then(res => {
    overview.value = res.data || {}
  }).finally(() => {
    overviewLoading.value = false
  })
}

function initJobTypeChart(data) {
  if (!jobTypeChart.value) return
  if (!jobTypeChartInstance) {
    jobTypeChartInstance = echarts.init(jobTypeChart.value)
  }
  const option = {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{
      name: '职位类型',
      type: 'pie',
      radius: '50%',
      data: data.map(item => ({ name: item.groupName || '未知', value: item.jobCount })),
      emphasis: {
        itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' }
      }
    }]
  }
  jobTypeChartInstance.setOption(option)
}

function initAppStatusChart(data) {
  if (!appStatusChart.value) return
  if (!appStatusChartInstance) {
    appStatusChartInstance = echarts.init(appStatusChart.value)
  }
  const statusMap = { '0': '待查看', '1': '已查看', '2': '初筛通过', '3': '面试邀请', '4': '已拒绝', '5': '已录用' }
  const colors = ['#909399', '#409eff', '#e6a23c', '#67c23a', '#f56c6c', '#00ced1']
  const option = {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{
      name: '投递状态',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
      label: { show: false, position: 'center' },
      emphasis: { label: { show: true, fontSize: 20, fontWeight: 'bold' } },
      labelLine: { show: false },
      data: data.map((item, index) => ({
        name: statusMap[item.groupName] || item.groupName,
        value: item.applicationCount,
        itemStyle: { color: colors[index % colors.length] }
      }))
    }]
  }
  appStatusChartInstance.setOption(option)
}

function initJobCityChart(data) {
  if (!jobCityChart.value) return
  if (!jobCityChartInstance) {
    jobCityChartInstance = echarts.init(jobCityChart.value)
  }
  const option = {
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: data.map(item => item.groupName), axisLabel: { rotate: 30 } },
    yAxis: { type: 'value' },
    series: [{
      name: '职位数量',
      type: 'bar',
      data: data.map(item => item.jobCount),
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#409eff' },
          { offset: 1, color: '#79bbff' }
        ])
      },
      barWidth: '40%'
    }]
  }
  jobCityChartInstance.setOption(option)
}

function initInterviewResultChart(data) {
  if (!interviewResultChart.value) return
  if (!interviewResultChartInstance) {
    interviewResultChartInstance = echarts.init(interviewResultChart.value)
  }
  const resultMap = { 'pass': '通过', 'fail': '未通过', 'pending': '待定' }
  const option = {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{
      name: '面试结果',
      type: 'pie',
      radius: '50%',
      data: data.map(item => ({ name: resultMap[item.groupName] || item.groupName, value: item.interviewCount })),
      emphasis: {
        itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' }
      }
    }]
  }
  interviewResultChartInstance.setOption(option)
}

function loadJobType() {
  jobTypeLoading.value = true
  getJobStatisticsByType().then(res => {
    jobTypeData.value = res.data || []
    initJobTypeChart(jobTypeData.value)
  }).finally(() => {
    jobTypeLoading.value = false
  })
}

function loadJobCity() {
  jobCityLoading.value = true
  getJobStatisticsByCity().then(res => {
    jobCityData.value = res.data || []
    initJobCityChart(jobCityData.value)
  }).finally(() => {
    jobCityLoading.value = false
  })
}

function loadAppStatus() {
  appStatusLoading.value = true
  getApplicationStatisticsByStatus().then(res => {
    appStatusData.value = res.data || []
    initAppStatusChart(appStatusData.value)
  }).finally(() => {
    appStatusLoading.value = false
  })
}

function loadInterviewResult() {
  interviewResultLoading.value = true
  getInterviewStatisticsByResult().then(res => {
    interviewResultData.value = res.data || []
    initInterviewResultChart(interviewResultData.value)
  }).finally(() => {
    interviewResultLoading.value = false
  })
}

// 窗口大小变化时重绘图表
function handleResize() {
  jobTypeChartInstance?.resize()
  appStatusChartInstance?.resize()
  jobCityChartInstance?.resize()
  interviewResultChartInstance?.resize()
}

onMounted(() => {
  loadOverview()
  loadJobType()
  loadJobCity()
  loadAppStatus()
  loadInterviewResult()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  jobTypeChartInstance?.dispose()
  appStatusChartInstance?.dispose()
  jobCityChartInstance?.dispose()
  interviewResultChartInstance?.dispose()
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
