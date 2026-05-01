<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="职位名称" prop="jobTitle">
        <el-input v-model="queryParams.jobTitle" placeholder="请输入职位名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="工作城市" prop="workCity">
        <el-input v-model="queryParams.workCity" placeholder="请输入工作城市" clearable style="width: 150px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="职位类型" prop="jobType">
        <el-select v-model="queryParams.jobType" placeholder="请选择" clearable style="width: 120px">
          <el-option label="全职" value="full_time" />
          <el-option label="实习" value="internship" />
          <el-option label="兼职" value="part_time" />
        </el-select>
      </el-form-item>
      <el-form-item label="学历要求" prop="educationRequired">
        <el-select v-model="queryParams.educationRequired" placeholder="请选择" clearable style="width: 120px">
          <el-option label="专科" value="junior_college" />
          <el-option label="本科" value="bachelor" />
          <el-option label="硕士" value="master" />
          <el-option label="博士" value="doctor" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="20" v-loading="loading">
      <el-col v-for="job in jobList" :key="job.jobId" :span="8" style="margin-bottom: 20px;">
        <el-card shadow="hover" class="job-card">
          <template #header>
            <div class="job-header">
              <span class="job-title">{{ job.jobTitle }}</span>
              <el-tag type="success" v-if="job.status === '1'">已发布</el-tag>
              <el-tag type="warning" v-else>待审核</el-tag>
            </div>
          </template>
          <div class="job-content">
            <div class="job-info">
              <el-icon><Location /></el-icon>
              <span>{{ job.workCity || '未填写' }}</span>
            </div>
            <div class="job-info">
              <el-icon><Money /></el-icon>
              <span>{{ job.salaryMin }} - {{ job.salaryMax }} 元/月</span>
            </div>
            <div class="job-info">
              <el-icon><User /></el-icon>
              <span>{{ job.companyName }}</span>
            </div>
            <div class="job-info">
              <el-icon><Reading /></el-icon>
              <span>{{ job.educationRequired || '不限' }}</span>
            </div>
            <div class="job-info">
              <el-icon><Calendar /></el-icon>
              <span>{{ parseTime(job.publishDate) }}</span>
            </div>
          </div>
          <div class="job-actions">
            <el-button type="primary" size="small" @click="handleView(job)">查看详情</el-button>
            <el-button type="success" size="small" @click="handleApply(job)" v-if="job.status === '1'">投递简历</el-button>
            <el-button type="warning" size="small" @click="handleFavorite(job)">{{ job.isFavorited ? '已收藏' : '收藏' }}</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 职位详情弹窗 -->
    <el-dialog title="职位详情" v-model="viewOpen" width="650px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="职位名称">{{ viewData.jobTitle }}</el-descriptions-item>
        <el-descriptions-item label="所属企业">{{ viewData.companyName }}</el-descriptions-item>
        <el-descriptions-item label="职位类型">
          <span v-if="viewData.jobType === 'full_time'">全职</span>
          <span v-else-if="viewData.jobType === 'internship'">实习</span>
          <span v-else-if="viewData.jobType === 'part_time'">兼职</span>
        </el-descriptions-item>
        <el-descriptions-item label="工作城市">{{ viewData.workCity }}</el-descriptions-item>
        <el-descriptions-item label="薪资范围">{{ viewData.salaryMin }} - {{ viewData.salaryMax }} 元/月</el-descriptions-item>
        <el-descriptions-item label="招聘人数">{{ viewData.recruitNum }} 人</el-descriptions-item>
        <el-descriptions-item label="学历要求">{{ viewData.educationRequired || '不限' }}</el-descriptions-item>
        <el-descriptions-item label="经验要求">{{ viewData.experienceRequired || '不限' }}</el-descriptions-item>
        <el-descriptions-item label="职位描述" :span="2">{{ viewData.jobDescription }}</el-descriptions-item>
        <el-descriptions-item label="任职要求" :span="2">{{ viewData.jobRequirements }}</el-descriptions-item>
        <el-descriptions-item label="福利待遇" :span="2">{{ viewData.welfare }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="success" @click="handleApply(viewData)">投递简历</el-button>
          <el-button @click="viewOpen = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 投递对话框 -->
    <el-dialog title="投递简历" v-model="applyOpen" width="500px" append-to-body>
      <el-form :model="applyForm" :rules="applyRules" ref="applyRef" label-width="100px">
        <el-form-item label="上传简历" prop="resumeUrl">
          <el-input v-model="applyForm.resumeUrl" placeholder="请输入简历链接或上传简历" />
        </el-form-item>
        <el-form-item label="求职信" prop="coverLetter">
          <el-input v-model="applyForm.coverLetter" type="textarea" placeholder="请输入求职信（选填）" :rows="4" maxlength="500" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitApply">确认投递</el-button>
          <el-button @click="applyOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="JobSearch">
import { listJob, getJob } from "@/api/crems/job"
import { addApplication } from "@/api/crems/application"
import { addFavorite, delFavoriteByJobAndStudent } from "@/api/crems/favorite"

const { proxy } = getCurrentInstance()
const router = useRouter()
const loading = ref(true)
const showSearch = ref(true)
const jobList = ref([])
const total = ref(0)
const viewOpen = ref(false)
const applyOpen = ref(false)
const viewData = ref({})
const applyForm = ref({})

const columns = ref({
  jobTitle: { visible: true, label: '职位名称' },
  companyName: { visible: true, label: '企业名称' },
  workCity: { visible: true, label: '工作城市' },
  jobType: { visible: true, label: '职位类型' },
  salaryMin: { visible: true, label: '最低薪资' },
  salaryMax: { visible: true, label: '最高薪资' }
})

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 12,
    jobTitle: undefined,
    workCity: undefined,
    jobType: undefined,
    educationRequired: undefined,
    status: '1'
  },
  applyRules: {}
})

const { queryParams } = toRefs(data)

function getList() {
  loading.value = true
  listJob(queryParams.value).then(res => {
    loading.value = false
    jobList.value = res.rows || []
    total.value = res.total
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

function handleView(row) {
  viewData.value = row
  viewOpen.value = true
}

function handleApply(job) {
  viewOpen.value = false
  applyForm.value = {
    jobId: job.jobId,
    companyId: job.companyId,
    resumeUrl: '',
    coverLetter: ''
  }
  applyOpen.value = true
}

function submitApply() {
  addApplication(applyForm.value).then(() => {
    proxy.$modal.msgSuccess("投递成功")
    applyOpen.value = false
    getList()
  })
}

function handleFavorite(job) {
  if (job.isFavorited) {
    delFavoriteByJobAndStudent(job.jobId, job.studentId || 1).then(() => {
      proxy.$modal.msgSuccess("已取消收藏")
      getList()
    })
  } else {
    addFavorite({ jobId: job.jobId, studentId: job.studentId || 1 }).then(() => {
      proxy.$modal.msgSuccess("收藏成功")
      getList()
    })
  }
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.job-card {
  height: 100%;
}
.job-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.job-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}
.job-content {
  margin-bottom: 15px;
}
.job-info {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  color: #606266;
  font-size: 14px;
}
.job-info .el-icon {
  margin-right: 8px;
}
.job-actions {
  display: flex;
  justify-content: space-between;
}
</style>