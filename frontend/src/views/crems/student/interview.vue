<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="职位名称" prop="jobTitle">
        <el-input v-model="queryParams.jobTitle" placeholder="请输入职位名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="面试状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 120px">
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
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns" />
    </el-row>

    <el-table v-loading="loading" :data="interviewList">
      <el-table-column label="面试ID" align="center" key="interviewId" prop="interviewId" width="80" />
      <el-table-column label="职位名称" align="center" key="jobTitle" prop="jobTitle" :show-overflow-tooltip="true" />
      <el-table-column label="企业名称" align="center" key="companyName" prop="companyName" :show-overflow-tooltip="true" />
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
      <el-table-column label="操作" align="center" width="100" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 面试详情弹窗 -->
    <el-dialog title="面试详情" v-model="viewOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="职位名称">{{ viewData.jobTitle }}</el-descriptions-item>
        <el-descriptions-item label="企业名称">{{ viewData.companyName }}</el-descriptions-item>
        <el-descriptions-item label="面试类型">
          <span v-if="viewData.interviewType === 'first'">初试</span>
          <span v-else-if="viewData.interviewType === 'second'">复试</span>
          <span v-else-if="viewData.interviewType === 'final'">终试</span>
        </el-descriptions-item>
        <el-descriptions-item label="面试方式">
          <span v-if="viewData.interviewMethod === 'onsite'">现场面试</span>
          <span v-else-if="viewData.interviewMethod === 'video'">视频面试</span>
          <span v-else-if="viewData.interviewMethod === 'phone'">电话面试</span>
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
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="面试评分">{{ viewData.score }}</el-descriptions-item>
        <el-descriptions-item label="面试通知" :span="2">{{ viewData.interviewNotice }}</el-descriptions-item>
        <el-descriptions-item label="面试评价" :span="2">{{ viewData.interviewFeedback }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup name="StudentInterview">
import { listInterview } from "@/api/crems/interview"

const { proxy } = getCurrentInstance()
const loading = ref(true)
const showSearch = ref(true)
const interviewList = ref([])
const total = ref(0)
const viewOpen = ref(false)
const viewData = ref({})

const columns = ref({
  interviewId: { visible: true, label: '面试ID' },
  jobTitle: { visible: true, label: '职位名称' },
  companyName: { visible: true, label: '企业名称' },
  interviewType: { visible: true, label: '面试类型' },
  interviewMethod: { visible: true, label: '面试方式' },
  interviewTime: { visible: true, label: '面试时间' },
  interviewer: { visible: true, label: '面试官' },
  status: { visible: true, label: '状态' }
})

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    jobTitle: undefined,
    status: undefined
  }
})

const { queryParams } = toRefs(data)

function getList() {
  loading.value = true
  listInterview(queryParams.value).then(res => {
    loading.value = false
    interviewList.value = res.rows
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

onMounted(() => {
  getList()
})
</script>