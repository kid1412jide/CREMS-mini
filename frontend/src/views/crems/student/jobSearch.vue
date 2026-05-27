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
      <el-col v-for="job in jobList" :key="job.jobId" :xs="24" :sm="12" :md="8" :lg="8" style="margin-bottom: 20px;">
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

    <el-empty v-if="!loading && jobList.length === 0" description="暂无符合条件的职位" />

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
          <el-upload
            ref="uploadRef"
            class="upload-demo"
            :limit="1"
            accept=".pdf,.doc,.docx"
            :headers="uploadHeaders"
            :action="uploadUrl"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :on-exceed="handleUploadExceed"
            :before-upload="beforeUpload"
            :auto-upload="true"
            :show-file-list="true"
            drag
          >
            <el-icon class="el-icon--upload"><Upload /></el-icon>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <template #tip>
              <div class="el-upload__tip">支持 PDF、Word 格式，文件大小不超过 10MB</div>
            </template>
          </el-upload>
          <div v-if="applyForm.resumeUrl" style="margin-top: 8px;">
            <el-tag type="success" closable @close="clearResume">
              {{ uploadedFileName || '已上传简历' }}
            </el-tag>
          </div>
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
import { getToken } from "@/utils/auth"
import useUserStore from "@/store/modules/user"

const { proxy } = getCurrentInstance()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(true)
const showSearch = ref(true)
const jobList = ref([])
const total = ref(0)
const viewOpen = ref(false)
const applyOpen = ref(false)
const viewData = ref({})
const applyForm = ref({})
const uploadRef = ref(null)
const uploadedFileName = ref('')
const currentStudentId = ref(null)

// 上传相关
const uploadUrl = import.meta.env.VITE_APP_BASE_API + '/common/upload'
const uploadHeaders = { Authorization: 'Bearer ' + getToken() }

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
  applyRules: {
    resumeUrl: [{ required: true, message: "请先上传简历", trigger: "change" }]
  }
})

const { queryParams } = toRefs(data)
const applyRef = ref(null)

const { queryParams } = toRefs(data)

function getList() {
  loading.value = true
  listJob(queryParams.value).then(res => {
    jobList.value = res.rows || []
    total.value = res.total
  }).finally(() => {
    loading.value = false
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
  uploadedFileName.value = ''
  applyOpen.value = true
}

// 上传前校验
function beforeUpload(file) {
  const allowedTypes = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document']
  if (!allowedTypes.includes(file.type)) {
    proxy.$modal.msgError('只支持 PDF 和 Word 格式的文件')
    return false
  }
  if (file.size / 1024 / 1024 > 10) {
    proxy.$modal.msgError('文件大小不能超过 10MB')
    return false
  }
  return true
}

// 上传成功回调
function handleUploadSuccess(response, file) {
  if (response.code === 200) {
    applyForm.value.resumeUrl = response.url
    uploadedFileName.value = response.originalFilename || file.name
    proxy.$modal.msgSuccess('上传成功')
  } else {
    proxy.$modal.msgError(response.msg || '上传失败')
  }
}

// 上传失败回调
function handleUploadError() {
  proxy.$modal.msgError('上传失败，请重试')
}

// 超出限制回调
function handleUploadExceed() {
  proxy.$modal.msgWarning('只能上传一个简历文件，请先删除已上传的文件')
}

// 清除已上传简历
function clearResume() {
  applyForm.value.resumeUrl = ''
  uploadedFileName.value = ''
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
}

// 下载简历
function downloadResume() {
  if (applyForm.value.resumeUrl) {
    window.open(applyForm.value.resumeUrl)
  }
}

function submitApply() {
  if (applyRef.value) {
    applyRef.value.validate(valid => {
      if (valid) {
        addApplication(applyForm.value).then(() => {
          proxy.$modal.msgSuccess("投递成功")
          applyOpen.value = false
          getList()
        })
      }
    })
  }
}

function handleFavorite(job) {
  if (!currentStudentId.value) {
    proxy.$modal.msgWarning("请先完善学生信息")
    return
  }
  if (job.isFavorited) {
    delFavoriteByJobAndStudent(job.jobId).then(() => {
      proxy.$modal.msgSuccess("已取消收藏")
      getList()
    })
  } else {
    addFavorite({ jobId: job.jobId, studentId: currentStudentId.value }).then(() => {
      proxy.$modal.msgSuccess("收藏成功")
      getList()
    })
  }
}

// 获取当前登录学生的studentId
async function loadCurrentStudentId() {
  try {
    const res = await fetch('/portal/api/student/list?pageSize=1&pageNum=1', {
      headers: { Authorization: 'Bearer ' + getToken() }
    })
    const data = await res.json()
    if (data.rows && data.rows.length > 0) {
      currentStudentId.value = data.rows[0].studentId
    }
  } catch (e) {
    console.error('获取学生信息失败', e)
  }
}

onMounted(() => {
  getList()
  loadCurrentStudentId()
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