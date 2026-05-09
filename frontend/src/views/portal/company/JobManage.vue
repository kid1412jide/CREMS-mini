<template>
  <div class="job-manage-page">
    <div class="page-header">
      <h1 class="portal-page-title">职位管理</h1>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>发布新职位
      </el-button>
    </div>

    <!-- Search -->
    <div class="search-bar">
      <el-input v-model="queryParams.jobTitle" placeholder="职位名称" clearable @keyup.enter="handleQuery" style="width: 200px" />
      <el-select v-model="queryParams.status" placeholder="状态" clearable style="width: 120px" @change="handleQuery">
        <el-option label="待审核" value="0" />
        <el-option label="已发布" value="1" />
        <el-option label="已下架" value="2" />
      </el-select>
      <el-button type="primary" @click="handleQuery">搜索</el-button>
      <el-button @click="resetQuery">重置</el-button>
    </div>

    <!-- Job list -->
    <div class="job-table" v-loading="loading">
      <div v-for="job in jobList" :key="job.jobId" class="job-row">
        <div class="job-row__main">
          <div class="job-row__title">{{ job.jobTitle }}</div>
          <div class="job-row__meta">
            <span>{{ job.workCity }}</span>
            <span>{{ formatJobType(job.jobType) }}</span>
            <span>{{ job.salaryMin }}-{{ job.salaryMax }}元/月</span>
            <span>投递 {{ job.applyCount || 0 }} · 浏览 {{ job.viewCount || 0 }}</span>
          </div>
        </div>
        <div class="job-row__actions">
          <el-tag :type="getStatusType(job.status)" size="small" class="portal-tag">
            {{ getStatusLabel(job.status) }}
          </el-tag>
          <el-button type="primary" link size="small" @click="handleEdit(job)">编辑</el-button>
          <el-button v-if="job.status === '0' || job.status === '2'" type="success" link size="small" @click="handlePublish(job)">发布</el-button>
          <el-button v-if="job.status === '1'" type="warning" link size="small" @click="handleDown(job)">下架</el-button>
          <el-button type="danger" link size="small" @click="handleDelete(job)">删除</el-button>
        </div>
      </div>
    </div>

    <div class="portal-empty" v-if="!loading && jobList.length === 0">
      <div class="portal-empty__text">还没有发布职位</div>
      <el-button type="primary" size="small" @click="handleAdd">发布职位</el-button>
    </div>

    <div style="margin-top: 24px; display: flex; justify-content: center" v-if="total > 0">
      <pagination :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </div>

    <!-- Add/Edit dialog -->
    <el-dialog :title="dialogTitle" v-model="dialogOpen" width="680px" append-to-body>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职位名称" prop="jobTitle">
              <el-input v-model="form.jobTitle" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位类型" prop="jobType">
              <el-select v-model="form.jobType" placeholder="请选择" style="width: 100%">
                <el-option label="全职" value="full_time" />
                <el-option label="实习" value="internship" />
                <el-option label="兼职" value="part_time" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工作城市" prop="workCity">
              <el-input v-model="form.workCity" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位类别">
              <el-input v-model="form.category" placeholder="如：软件开发" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最低薪资">
              <el-input-number v-model="form.salaryMin" :min="0" :step="500" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最高薪资">
              <el-input-number v-model="form.salaryMax" :min="0" :step="500" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学历要求">
              <el-select v-model="form.educationRequired" placeholder="请选择" style="width: 100%">
                <el-option label="不限" value="" />
                <el-option label="专科" value="junior_college" />
                <el-option label="本科" value="bachelor" />
                <el-option label="硕士" value="master" />
                <el-option label="博士" value="doctor" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="招聘人数">
              <el-input-number v-model="form.recruitNum" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="职位描述">
              <el-input v-model="form.jobDescription" type="textarea" :rows="4" placeholder="详细描述职位的工作内容" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="任职要求">
              <el-input v-model="form.jobRequirements" type="textarea" :rows="4" placeholder="列出任职所需的技能和条件" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="福利待遇">
              <el-input v-model="form.welfare" placeholder="如：五险一金,带薪年假,弹性工作" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogOpen = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { Plus } from '@element-plus/icons-vue'
import { listJob, getJob, addJob, updateJob, delJob } from '@/api/portal'

const { proxy } = getCurrentInstance()

const loading = ref(false)
const submitting = ref(false)
const jobList = ref([])
const total = ref(0)
const dialogOpen = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

const queryParams = reactive({ pageNum: 1, pageSize: 10, jobTitle: undefined, status: undefined })

const form = reactive({
  jobId: undefined,
  jobTitle: '',
  jobType: 'full_time',
  workCity: '',
  category: '',
  salaryMin: 0,
  salaryMax: 0,
  educationRequired: '',
  recruitNum: 1,
  jobDescription: '',
  jobRequirements: '',
  welfare: ''
})

const rules = {
  jobTitle: [{ required: true, message: '请输入职位名称', trigger: 'blur' }],
  jobType: [{ required: true, message: '请选择职位类型', trigger: 'change' }],
  workCity: [{ required: true, message: '请输入工作城市', trigger: 'blur' }]
}

const jobTypeMap = { full_time: '全职', internship: '实习', part_time: '兼职' }
const statusMap = {
  '0': { label: '待审核', type: 'warning' },
  '1': { label: '已发布', type: 'success' },
  '2': { label: '已下架', type: 'info' }
}
const formatJobType = (t) => jobTypeMap[t] || t
const getStatusLabel = (s) => statusMap[s]?.label || s
const getStatusType = (s) => statusMap[s]?.type || 'info'

function getList() {
  loading.value = true
  listJob(queryParams).then(res => {
    jobList.value = res.rows || []
    total.value = res.total || 0
  }).finally(() => { loading.value = false })
}

function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

function resetQuery() {
  queryParams.jobTitle = undefined
  queryParams.status = undefined
  handleQuery()
}

function resetForm() {
  Object.assign(form, {
    jobId: undefined, jobTitle: '', jobType: 'full_time', workCity: '',
    category: '', salaryMin: 0, salaryMax: 0, educationRequired: '',
    recruitNum: 1, jobDescription: '', jobRequirements: '', welfare: ''
  })
}

function handleAdd() {
  resetForm()
  dialogTitle.value = '发布新职位'
  dialogOpen.value = true
}

async function handleEdit(row) {
  resetForm()
  const res = await getJob(row.jobId)
  const data = res.data || res
  Object.keys(form).forEach(key => {
    if (data[key] !== undefined && data[key] !== null) form[key] = data[key]
  })
  dialogTitle.value = '编辑职位'
  dialogOpen.value = true
}

async function submitForm() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    if (form.jobId) {
      await updateJob(form)
      proxy.$modal.msgSuccess('修改成功')
    } else {
      await addJob(form)
      proxy.$modal.msgSuccess('发布成功')
    }
    dialogOpen.value = false
    getList()
  } catch (e) {
    proxy.$modal.msgError('操作失败')
  } finally {
    submitting.value = false
  }
}

async function handlePublish(job) {
  await updateJob({ jobId: job.jobId, status: '1' })
  proxy.$modal.msgSuccess('已发布')
  getList()
}

async function handleDown(job) {
  await updateJob({ jobId: job.jobId, status: '2' })
  proxy.$modal.msgSuccess('已下架')
  getList()
}

async function handleDelete(job) {
  try {
    await proxy.$modal.confirm('确认删除该职位？')
    await delJob(job.jobId)
    proxy.$modal.msgSuccess('删除成功')
    getList()
  } catch (e) { /* cancelled */ }
}

onMounted(() => getList())
</script>

<style lang="scss" scoped>
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;

  .portal-page-title { margin: 0; }
}

.search-bar {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.job-table {
  background: #fff;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
  overflow: hidden;
}

.job-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid #fafafa;
  gap: 16px;
  transition: background 0.15s;

  &:hover { background: #fafbfc; }
  &:last-child { border-bottom: none; }

  &__main { flex: 1; min-width: 0; }

  &__title {
    font-size: 15px;
    font-weight: 600;
    color: #303133;
  }

  &__meta {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    font-size: 12px;
    color: #909399;
    margin-top: 6px;
  }

  &__actions {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-shrink: 0;
  }
}
</style>
