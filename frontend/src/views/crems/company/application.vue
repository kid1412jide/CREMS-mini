<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="职位名称" prop="jobTitle">
        <el-input v-model="queryParams.jobTitle" placeholder="请输入职位名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="学生姓名" prop="studentName">
        <el-input v-model="queryParams.studentName" placeholder="请输入学生姓名" clearable style="width: 150px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="投递状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 150px">
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

    <el-table v-loading="loading" :data="applicationList">
      <el-table-column label="投递ID" align="center" key="applicationId" prop="applicationId" width="80" />
      <el-table-column label="职位名称" align="center" key="jobTitle" prop="jobTitle" :show-overflow-tooltip="true" />
      <el-table-column label="学生姓名" align="center" key="studentName" prop="studentName" width="100" />
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
      <el-table-column label="查看时间" align="center" prop="viewTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.viewTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="企业反馈" align="center" key="feedback" prop="feedback" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">详情</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdateStatus(scope.row)" v-hasPermi="['crems:application:edit']">处理</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 处理投递对话框 -->
    <el-dialog title="处理投递" v-model="open" width="500px" append-to-body>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="处理状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择" style="width: 100%">
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
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 投递详情弹窗 -->
    <el-dialog title="投递详情" v-model="viewOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="投递ID">{{ viewData.applicationId }}</el-descriptions-item>
        <el-descriptions-item label="职位名称">{{ viewData.jobTitle }}</el-descriptions-item>
        <el-descriptions-item label="学生姓名">{{ viewData.studentName }}</el-descriptions-item>
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
        <el-descriptions-item label="简历">{{ viewData.resumeUrl }}</el-descriptions-item>
        <el-descriptions-item label="求职信" :span="2">{{ viewData.coverLetter }}</el-descriptions-item>
        <el-descriptions-item label="企业反馈" :span="2">{{ viewData.feedback }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup name="Application">
import { listApplication, updateApplication } from "@/api/crems/application"

const { proxy } = getCurrentInstance()
const loading = ref(true)
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
  applyTime: { visible: true, label: '投递时间' },
  status: { visible: true, label: '状态' },
  viewTime: { visible: true, label: '查看时间' },
  feedback: { visible: true, label: '企业反馈' }
})

const data = reactive({
  form: {},
  queryParams: {
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

const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listApplication(queryParams.value).then(res => {
    loading.value = false
    applicationList.value = res.rows
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
      updateApplication(form.value).then(() => {
        proxy.$modal.msgSuccess("处理成功")
        open.value = false
        getList()
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
  proxy.download("crems/application/export", { ...queryParams.value }, `application_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList()
})
</script>