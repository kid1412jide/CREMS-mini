<template>
  <div class="app-container">
    <el-form :model="quecremsParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="职位名称" prop="jobTitle">
        <el-input v-model="quecremsParams.jobTitle" placeholder="请输入职位名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="学生姓名" prop="studentName">
        <el-input v-model="quecremsParams.studentName" placeholder="请输入学生姓名" clearable style="width: 150px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="投递状态" prop="status">
        <el-select v-model="quecremsParams.status" placeholder="请选择" clearable style="width: 150px">
          <el-option label="待查看" value="0" />
          <el-option label="已查看" value="1" />
          <el-option label="初筛通过" value="2" />
          <el-option label="面试邀请" value="3" />
          <el-option label="已拒绝" value="4" />
          <el-option label="已录用" value="5" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['crems:application:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns" />
    </el-row>

    <el-table v-loading="loading" :data="applicationList" stripe empty-text="暂无投递数据">
      <el-table-column label="投递ID" align="center" key="applicationId" prop="applicationId" width="80" />
      <el-table-column label="职位名称" align="center" key="jobTitle" prop="jobTitle" :show-overflow-tooltip="true" />
      <el-table-column label="学生姓名" align="center" key="studentName" prop="studentName" width="100" />
      <el-table-column label="学校" align="center" key="school" prop="school" :show-overflow-tooltip="true" />
      <el-table-column label="专业" align="center" key="major" prop="major" :show-overflow-tooltip="true" />
      <el-table-column label="投递时间" align="center" prop="applyTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.applyTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" key="status" prop="status" width="100">
        <template #default="scope">
          <el-tag type="info" v-if="scope.row.status === '0'">待查看</el-tag>
          <el-tag v-else-if="scope.row.status === '1'">已查看</el-tag>
          <el-tag type="success" v-else-if="scope.row.status === '2'">初筛通过</el-tag>
          <el-tag type="primary" v-else-if="scope.row.status === '3'">面试邀请</el-tag>
          <el-tag type="danger" v-else-if="scope.row.status === '4'">已拒绝</el-tag>
          <el-tag type="success" v-else-if="scope.row.status === '5'">已录用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">详情</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdateStatus(scope.row)" v-hasPermi="['crems:application:edit']">处理</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="quecremsParams.pageNum" v-model:limit="quecremsParams.pageSize" @pagination="getList" />

    <!-- 处理投递对话框 -->
    <el-dialog title="处理投递" v-model="open" width="500px" append-to-body>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="处理状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择" style="width: 100%">
            <el-option label="已查看" value="1" />
            <el-option label="初筛通过" value="2" />
            <el-option label="面试邀请" value="3" />
            <el-option label="已拒绝" value="4" />
            <el-option label="已录用" value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="企业反馈" prop="feedback">
          <el-input v-model="form.feedback" type="textarea" placeholder="请输入反馈信息" :rows="3" maxlength="500" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" :loading="submitting" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 投递详情弹窗 -->
    <el-dialog title="投递详情" v-model="viewOpen" width="700px" append-to-body>
      <el-divider content-position="left">投递信息</el-divider>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="投递ID">{{ viewData.applicationId }}</el-descriptions-item>
        <el-descriptions-item label="职位名称">{{ viewData.jobTitle }}</el-descriptions-item>
        <el-descriptions-item label="投递时间">{{ parseTime(viewData.applyTime) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag type="info" v-if="viewData.status === '0'">待查看</el-tag>
          <el-tag v-else-if="viewData.status === '1'">已查看</el-tag>
          <el-tag type="success" v-else-if="viewData.status === '2'">初筛通过</el-tag>
          <el-tag type="primary" v-else-if="viewData.status === '3'">面试邀请</el-tag>
          <el-tag type="danger" v-else-if="viewData.status === '4'">已拒绝</el-tag>
          <el-tag type="success" v-else-if="viewData.status === '5'">已录用</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="查看时间">{{ parseTime(viewData.viewTime) }}</el-descriptions-item>
        <el-descriptions-item label="企业反馈" :span="2">{{ viewData.feedback }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">学生简历</el-divider>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">{{ viewData.studentName }}</el-descriptions-item>
        <el-descriptions-item label="学号">{{ viewData.studentNo }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ viewData.gender === '0' ? '男' : viewData.gender === '1' ? '女' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ viewData.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ viewData.email }}</el-descriptions-item>
        <el-descriptions-item label="学校">{{ viewData.school }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ viewData.major }}</el-descriptions-item>
        <el-descriptions-item label="学历">{{ viewData.education }}</el-descriptions-item>
        <el-descriptions-item label="年级">{{ viewData.grade }}</el-descriptions-item>
        <el-descriptions-item label="毕业时间">{{ parseTime(viewData.graduationDate) }}</el-descriptions-item>
        <el-descriptions-item label="技能标签" :span="2">
          <template v-if="viewData.skills">
            <template v-for="skill in viewData.skills.split(',')" :key="skill">
              <el-tag v-if="skill.trim()" style="margin-right: 5px">{{ skill.trim() }}</el-tag>
            </template>
          </template>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="自我介绍" :span="2">{{ viewData.selfIntroduction || '-' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">投递简历</el-divider>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="简历文件">
          <template v-if="viewData.resumeUrl">
            <el-button type="primary" link @click="downloadResume(viewData.resumeUrl)">
              <el-icon><Download /></el-icon> 下载简历
            </el-button>
            <span style="margin-left: 10px; color: #909399; font-size: 12px;">
              (投递时上传)
            </span>
          </template>
          <span v-else>未上传简历</span>
        </el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">求职信</el-divider>
      <div style="padding: 10px; background: #f5f7fa; border-radius: 4px; min-height: 60px;">
        {{ viewData.coverLetter || '未填写求职信' }}
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="Application">
import { listApplication, updateApplication } from "@/api/crems/application"

const { proxy } = getCurrentInstance()
const loading = ref(true)
const submitting = ref(false)
const showSearch = ref(true)
const applicationList = ref([])
const total = ref(0)
const open = ref(false)
const viewOpen = ref(false)
const viewData = ref({})

const columns = ref({
  applicationId: { visible: true, label: '投递ID' },
  jobTitle: { visible: true, label: '职位名称' },
  studentName: { visible: true, label: '学生姓名' },
  school: { visible: true, label: '学校' },
  major: { visible: true, label: '专业' },
  applyTime: { visible: true, label: '投递时间' },
  status: { visible: true, label: '状态' }
})

const data = reactive({
  form: {},
  quecremsParams: {
    pageNum: 1,
    pageSize: 10,
    jobTitle: undefined,
    studentName: undefined,
    status: undefined
  },
  rules: {
    status: [{ required: true, message: "状态不能为空", trigger: "change" }]
  }
})

const { quecremsParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listApplication(quecremsParams.value).then(res => {
    applicationList.value = res.rows
    total.value = res.total
  }).finally(() => {
    loading.value = false
  })
}

function handleQuery() {
  quecremsParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

function handleView(row) {
  viewData.value = { ...row }
  viewOpen.value = true
  // 待查看状态自动标记为已查看
  if (row.status === '0') {
    updateApplication({
      applicationId: row.applicationId,
      status: '1',
      viewTime: new Date()
    }).then(() => {
      getList()
    })
  }
}

// 下载简历
function downloadResume(url) {
  if (url) {
    window.open(url)
  }
}

function handleUpdateStatus(row) {
  form.value = {
    applicationId: row.applicationId,
    status: row.status,
    feedback: row.feedback
  }
  open.value = true
}

function submitForm() {
  proxy.$refs["formRef"].validate(valid => {
    if (valid) {
      submitting.value = true
      updateApplication(form.value).then(() => {
        proxy.$modal.msgSuccess("处理成功")
        open.value = false
        getList()
      }).finally(() => {
        submitting.value = false
      })
    }
  })
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    applicationId: undefined,
    status: undefined,
    feedback: undefined
  }
  proxy.resetForm("formRef")
}

function handleExport() {
  proxy.download("crems/application/export", { ...quecremsParams.value }, `application_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList()
})
</script>