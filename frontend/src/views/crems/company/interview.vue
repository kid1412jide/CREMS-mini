<template>
  <div class="app-container">
    <el-form :model="quecremsParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="学生姓名" prop="studentName">
        <el-input v-model="quecremsParams.studentName" placeholder="请输入学生姓名" clearable style="width: 150px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="面试类型" prop="interviewType">
        <el-select v-model="quecremsParams.interviewType" placeholder="请选择" clearable style="width: 120px">
          <el-option label="初试" value="first" />
          <el-option label="复试" value="second" />
          <el-option label="终试" value="final" />
        </el-select>
      </el-form-item>
      <el-form-item label="面试状态" prop="status">
        <el-select v-model="quecremsParams.status" placeholder="请选择" clearable style="width: 120px">
          <el-option label="待确认" value="0" />
          <el-option label="已确认" value="1" />
          <el-option label="已完成" value="2" />
          <el-option label="已取消" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['crems:interview:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['crems:interview:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns" />
    </el-row>

    <el-table v-loading="loading" :data="interviewList" stripe empty-text="暂无面试数据">
      <el-table-column label="面试ID" align="center" key="interviewId" prop="interviewId" width="80" />
      <el-table-column label="学生姓名" align="center" key="studentName" prop="studentName" width="100" />
      <el-table-column label="职位名称" align="center" key="jobTitle" prop="jobTitle" :show-overflow-tooltip="true" />
      <el-table-column label="面试类型" align="center" key="interviewType" prop="interviewType" width="80">
        <template #default="scope">
          <span v-if="scope.row.interviewType === 'first'">初试</span>
          <span v-else-if="scope.row.interviewType === 'second'">复试</span>
          <span v-else-if="scope.row.interviewType === 'final'">终试</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="面试方式" align="center" key="interviewMethod" prop="interviewMethod" width="80">
        <template #default="scope">
          <span v-if="scope.row.interviewMethod === 'onsite'">现场</span>
          <span v-else-if="scope.row.interviewMethod === 'video'">视频</span>
          <span v-else-if="scope.row.interviewMethod === 'phone'">电话</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="面试时间" align="center" prop="interviewTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.interviewTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="面试官" align="center" key="interviewer" prop="interviewer" width="100" />
      <el-table-column label="状态" align="center" key="status" prop="status" width="80">
        <template #default="scope">
          <el-tag type="warning" v-if="scope.row.status === '0'">待确认</el-tag>
          <el-tag type="success" v-else-if="scope.row.status === '1'">已确认</el-tag>
          <el-tag type="primary" v-else-if="scope.row.status === '2'">已完成</el-tag>
          <el-tag type="info" v-else-if="scope.row.status === '3'">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="面试结果" align="center" key="interviewResult" prop="interviewResult" width="80">
        <template #default="scope">
          <el-tag type="success" v-if="scope.row.interviewResult === 'pass'">通过</el-tag>
          <el-tag type="danger" v-else-if="scope.row.interviewResult === 'fail'">未通过</el-tag>
          <el-tag v-else-if="scope.row.interviewResult === 'pending'">待定</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">详情</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['crems:interview:edit']">编辑</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['crems:interview:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="quecremsParams.pageNum" v-model:limit="quecremsParams.pageSize" @pagination="getList" />

    <!-- 添加或修改面试对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="面试类型" prop="interviewType">
              <el-select v-model="form.interviewType" placeholder="请选择" style="width: 100%">
                <el-option label="初试" value="first" />
                <el-option label="复试" value="second" />
                <el-option label="终试" value="final" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="面试方式" prop="interviewMethod">
              <el-select v-model="form.interviewMethod" placeholder="请选择" style="width: 100%">
                <el-option label="现场面试" value="onsite" />
                <el-option label="视频面试" value="video" />
                <el-option label="电话面试" value="phone" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="面试时间" prop="interviewTime">
              <el-date-picker v-model="form.interviewTime" type="datetime" placeholder="请选择面试时间" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="面试官" prop="interviewer">
              <el-input v-model="form.interviewer" placeholder="请输入面试官" maxlength="100" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="面试地址" prop="interviewAddress">
          <el-input v-model="form.interviewAddress" placeholder="请输入面试地址" maxlength="200" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话" maxlength="20" />
        </el-form-item>
        <el-form-item label="面试通知" prop="interviewNotice">
          <el-input v-model="form.interviewNotice" type="textarea" placeholder="请输入面试通知" :rows="2" maxlength="500" show-word-limit />
        </el-form-item>
        <el-form-item label="面试评价" prop="interviewFeedback">
          <el-input v-model="form.interviewFeedback" type="textarea" placeholder="请输入面试评价" :rows="2" maxlength="500" show-word-limit />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="面试结果" prop="interviewResult">
              <el-select v-model="form.interviewResult" placeholder="请选择" style="width: 100%">
                <el-option label="通过" value="pass" />
                <el-option label="未通过" value="fail" />
                <el-option label="待定" value="pending" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="面试评分" prop="score">
              <el-input-number v-model="form.score" :min="0" :max="100" placeholder="评分" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" :loading="submitting" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 面试详情弹窗 -->
    <el-dialog title="面试详情" v-model="viewOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="学生姓名">{{ viewData.studentName }}</el-descriptions-item>
        <el-descriptions-item label="职位名称">{{ viewData.jobTitle }}</el-descriptions-item>
        <el-descriptions-item label="面试类型">
          <span v-if="viewData.interviewType === 'first'">初试</span>
          <span v-else-if="viewData.interviewType === 'second'">复试</span>
          <span v-else-if="viewData.interviewType === 'final'">终试</span>
        </el-descriptions-item>
        <el-descriptions-item label="面试方式">
          <span v-if="viewData.interviewMethod === 'onsite'">现场</span>
          <span v-else-if="viewData.interviewMethod === 'video'">视频</span>
          <span v-else-if="viewData.interviewMethod === 'phone'">电话</span>
        </el-descriptions-item>
        <el-descriptions-item label="面试时间">{{ parseTime(viewData.interviewTime) }}</el-descriptions-item>
        <el-descriptions-item label="面试官">{{ viewData.interviewer }}</el-descriptions-item>
        <el-descriptions-item label="面试地址">{{ viewData.interviewAddress }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ viewData.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag type="warning" v-if="viewData.status === '0'">待确认</el-tag>
          <el-tag type="success" v-else-if="viewData.status === '1'">已确认</el-tag>
          <el-tag type="primary" v-else-if="viewData.status === '2'">已完成</el-tag>
          <el-tag type="info" v-else-if="viewData.status === '3'">已取消</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="面试结果">
          <el-tag type="success" v-if="viewData.interviewResult === 'pass'">通过</el-tag>
          <el-tag type="danger" v-else-if="viewData.interviewResult === 'fail'">未通过</el-tag>
          <el-tag v-else-if="viewData.interviewResult === 'pending'">待定</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="面试评分">{{ viewData.score }}</el-descriptions-item>
        <el-descriptions-item label="面试通知" :span="2">{{ viewData.interviewNotice }}</el-descriptions-item>
        <el-descriptions-item label="面试评价" :span="2">{{ viewData.interviewFeedback }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup name="Interview">
import { listInterview, addInterview, updateInterview, delInterview } from "@/api/crems/interview"

const { proxy } = getCurrentInstance()
const loading = ref(true)
const submitting = ref(false)
const showSearch = ref(true)
const interviewList = ref([])
const total = ref(0)
const title = ref("")
const open = ref(false)
const viewOpen = ref(false)
const viewData = ref({})

const columns = ref({
  interviewId: { visible: true, label: '面试ID' },
  studentName: { visible: true, label: '学生姓名' },
  jobTitle: { visible: true, label: '职位名称' },
  interviewType: { visible: true, label: '面试类型' },
  interviewMethod: { visible: true, label: '面试方式' },
  interviewTime: { visible: true, label: '面试时间' },
  interviewer: { visible: true, label: '面试官' },
  status: { visible: true, label: '状态' },
  interviewResult: { visible: true, label: '面试结果' }
})

const data = reactive({
  form: {},
  quecremsParams: {
    pageNum: 1,
    pageSize: 10,
    studentName: undefined,
    interviewType: undefined,
    status: undefined
  },
  rules: {
    interviewType: [{ required: true, message: "请选择面试类型", trigger: "change" }],
    interviewMethod: [{ required: true, message: "请选择面试方式", trigger: "change" }],
    interviewTime: [{ required: true, message: "请选择面试时间", trigger: "change" }]
  }
})

const { quecremsParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listInterview(quecremsParams.value).then(res => {
    interviewList.value = res.rows
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

function handleAdd() {
  reset()
  open.value = true
  title.value = "添加面试"
}

function handleUpdate(row) {
  reset()
  open.value = true
  title.value = "修改面试"
  form.value = JSON.parse(JSON.stringify(row))
}

function handleView(row) {
  viewData.value = { ...row }
  viewOpen.value = true
}

function submitForm() {
  proxy.$refs["formRef"].validate(valid => {
    if (valid) {
      submitting.value = true
      const action = form.value.interviewId !== undefined ? updateInterview(form.value) : addInterview(form.value)
      action.then(() => {
        proxy.$modal.msgSuccess(form.value.interviewId !== undefined ? "修改成功" : "新增成功")
        open.value = false
        getList()
      }).finally(() => {
        submitting.value = false
      })
    }
  })
}

function handleDelete(row) {
  const deleteMsg = row.studentName && row.jobTitle
    ? '是否确认删除"' + row.studentName + '"的"' + row.jobTitle + '"面试？'
    : '是否确认删除该面试记录？'
  proxy.$modal.confirm(deleteMsg).then(() => {
    return delInterview(row.interviewId)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function handleExport() {
  proxy.download("crems/interview/export", { ...quecremsParams.value }, `interview_${new Date().getTime()}.xlsx`)
}

function reset() {
  form.value = {
    interviewId: undefined,
    interviewType: undefined,
    interviewMethod: undefined,
    interviewTime: undefined,
    interviewAddress: undefined,
    interviewer: undefined,
    contactPhone: undefined,
    interviewNotice: undefined,
    interviewFeedback: undefined,
    interviewResult: undefined,
    score: undefined,
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