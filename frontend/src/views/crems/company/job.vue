<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="职位名称" prop="jobTitle">
        <el-input v-model="queryParams.jobTitle" placeholder="请输入职位名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="职位类型" prop="jobType">
        <el-select v-model="queryParams.jobType" placeholder="请选择" clearable style="width: 150px">
          <el-option v-for="dict in crems_job_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="工作城市" prop="workCity">
        <el-input v-model="queryParams.workCity" placeholder="请输入工作城市" clearable style="width: 150px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 150px">
          <el-option v-for="dict in crems_job_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['crems:job:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['crems:job:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="Upload" @click="handleImport" v-hasPermi="['crems:job:import']">导入</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns" />
    </el-row>

    <el-table v-loading="loading" :data="jobList" stripe empty-text="暂无职位数据">
      <el-table-column label="职位ID" align="center" key="jobId" prop="jobId" width="80" />
      <el-table-column label="职位名称" align="center" key="jobTitle" prop="jobTitle" :show-overflow-tooltip="true" />
      <el-table-column label="所属企业" align="center" key="companyName" prop="companyName" width="180" />
      <el-table-column label="职位类型" align="center" key="jobType" prop="jobType" width="80">
        <template #default="scope">
          <dict-tag :options="crems_job_type" :value="scope.row.jobType" />
        </template>
      </el-table-column>
      <el-table-column label="工作城市" align="center" key="workCity" prop="workCity" width="100" />
      <el-table-column label="薪资范围" align="center" width="140">
        <template #default="scope">
          {{ scope.row.salaryMin }} - {{ scope.row.salaryMax }}
        </template>
      </el-table-column>
      <el-table-column label="招聘人数" align="center" key="recruitNum" prop="recruitNum" width="80" />
      <el-table-column label="学历要求" align="center" key="educationRequired" prop="educationRequired" width="100" />
      <el-table-column label="状态" align="center" key="status" prop="status" width="80">
        <template #default="scope">
          <dict-tag :options="crems_job_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="发布时间" align="center" prop="publishDate" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.publishDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">详情</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['crems:job:edit']">编辑</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['crems:job:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改职位对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" append-to-body>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="职位名称" prop="jobTitle">
              <el-input v-model="form.jobTitle" placeholder="请输入职位名称" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位类型" prop="jobType">
              <el-select v-model="form.jobType" placeholder="请选择" style="width: 100%">
                <el-option v-for="dict in crems_job_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="工作城市" prop="workCity">
              <el-input v-model="form.workCity" placeholder="请输入工作城市" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工作地址" prop="workAddress">
              <el-input v-model="form.workAddress" placeholder="请输入工作地址" maxlength="200" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="最低薪资" prop="salaryMin">
              <el-input-number v-model="form.salaryMin" :min="0" placeholder="最低薪资" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最高薪资" prop="salaryMax">
              <el-input-number v-model="form.salaryMax" :min="0" placeholder="最高薪资" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="招聘人数" prop="recruitNum">
              <el-input-number v-model="form.recruitNum" :min="1" placeholder="招聘人数" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="学历要求" prop="educationRequired">
              <el-select v-model="form.educationRequired" placeholder="请选择" style="width: 100%">
                <el-option v-for="dict in crems_education" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经验要求" prop="experienceRequired">
              <el-input v-model="form.experienceRequired" placeholder="请输入经验要求" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="职位描述" prop="jobDescription">
          <el-input v-model="form.jobDescription" type="textarea" placeholder="请输入职位描述" :rows="3" maxlength="1000" show-word-limit />
        </el-form-item>
        <el-form-item label="任职要求" prop="jobRequirements">
          <el-input v-model="form.jobRequirements" type="textarea" placeholder="请输入任职要求" :rows="3" maxlength="1000" show-word-limit />
        </el-form-item>
        <el-form-item label="福利待遇" prop="welfare">
          <el-input v-model="form.welfare" placeholder="请输入福利待遇，多个用逗号分隔" maxlength="500" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 职位详情弹窗 -->
    <el-dialog title="职位详情" v-model="viewOpen" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="职位名称">{{ viewData.jobTitle }}</el-descriptions-item>
        <el-descriptions-item label="所属企业">{{ viewData.companyName }}</el-descriptions-item>
        <el-descriptions-item label="职位类型">
          <dict-tag :options="crems_job_type" :value="viewData.jobType" />
        </el-descriptions-item>
        <el-descriptions-item label="工作城市">{{ viewData.workCity }}</el-descriptions-item>
        <el-descriptions-item label="薪资范围">{{ viewData.salaryMin }} - {{ viewData.salaryMax }}</el-descriptions-item>
        <el-descriptions-item label="招聘人数">{{ viewData.recruitNum }}</el-descriptions-item>
        <el-descriptions-item label="学历要求">
          <dict-tag :options="crems_education" :value="viewData.educationRequired" />
        </el-descriptions-item>
        <el-descriptions-item label="经验要求">{{ viewData.experienceRequired }}</el-descriptions-item>
        <el-descriptions-item label="职位描述" :span="2">{{ viewData.jobDescription }}</el-descriptions-item>
        <el-descriptions-item label="任职要求" :span="2">{{ viewData.jobRequirements }}</el-descriptions-item>
        <el-descriptions-item label="福利待遇" :span="2">{{ viewData.welfare }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <dict-tag :options="crems_job_status" :value="viewData.status" />
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" v-model="upload.open" width="400px" append-to-body>
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的职位数据
            </div>
            <span>仅允许导入 xls、xlsx 格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Job">
import { listJob, addJob, updateJob, delJob } from "@/api/crems/job"
import { getToken } from "@/utils/auth"

const { proxy } = getCurrentInstance()
const { crems_job_type, crems_job_status, crems_education } = toRefs(reactive(proxy.useDict('crems_job_type', 'crems_job_status', 'crems_education')))
const loading = ref(true)
const showSearch = ref(true)
const jobList = ref([])
const total = ref(0)
const title = ref("")
const open = ref(false)
const viewOpen = ref(false)
const viewData = ref({})

const columns = ref({
  jobId: { visible: true, label: '职位ID' },
  jobTitle: { visible: true, label: '职位名称' },
  companyName: { visible: true, label: '所属企业' },
  jobType: { visible: true, label: '职位类型' },
  workCity: { visible: true, label: '工作城市' },
  salaryMin: { visible: true, label: '最低薪资' },
  salaryMax: { visible: true, label: '最高薪资' },
  recruitNum: { visible: true, label: '招聘人数' },
  educationRequired: { visible: true, label: '学历要求' },
  status: { visible: true, label: '状态' },
  publishDate: { visible: true, label: '发布时间' }
})

const upload = reactive({
  open: false,
  title: '',
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: 'Bearer ' + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + '/crems/job/importData'
})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    jobTitle: undefined,
    jobType: undefined,
    workCity: undefined,
    status: undefined
  },
  rules: {
    jobTitle: [{ required: true, message: "职位名称不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listJob(queryParams.value).then(res => {
    jobList.value = res.rows
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

function handleAdd() {
  reset()
  open.value = true
  title.value = "添加职位"
}

function handleUpdate(row) {
  reset()
  open.value = true
  title.value = "修改职位"
  form.value = JSON.parse(JSON.stringify(row))
}

function handleView(row) {
  viewData.value = row
  viewOpen.value = true
}

function submitForm() {
  proxy.$refs["formRef"].validate(valid => {
    if (valid) {
      if (form.value.jobId !== undefined) {
        updateJob(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addJob(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

function handleDelete(row) {
  proxy.$modal.confirm('是否确认删除职位ID为"' + row.jobId + '"的数据项？').then(() => {
    return delJob(row.jobId)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function handleExport() {
  proxy.download("crems/job/export", { ...queryParams.value }, `job_${new Date().getTime()}.xlsx`)
}

/** 导入按钮操作 */
function handleImport() {
  upload.title = "职位管理 - 导入数据"
  upload.open = true
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download("crems/job/importTemplate", {}, `job_template_${new Date().getTime()}.xlsx`)
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit()
}

/** 文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true
}

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false
  upload.isUploading = false
  proxy.$refs["uploadRef"].clearFiles()
  // 对 response.msg 进行 HTML 转义，防止 XSS 攻击
  const escapedMsg = response.msg ? response.msg.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#x27;') : ''
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + escapedMsg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
  getList()
}

function reset() {
  form.value = {
    jobId: undefined,
    jobTitle: undefined,
    jobType: undefined,
    workCity: undefined,
    workAddress: undefined,
    salaryMin: 0,
    salaryMax: 0,
    recruitNum: 1,
    educationRequired: undefined,
    experienceRequired: undefined,
    jobDescription: undefined,
    jobRequirements: undefined,
    welfare: undefined,
    status: '0'
  }
  proxy.resetForm("formRef")
}

function cancel() {
  open.value = false
  reset()
}

onMounted(() => {
  getList()
})
</script>