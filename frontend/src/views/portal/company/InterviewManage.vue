<template>
  <div class="interview-manage-page">
    <h1 class="portal-page-title">面试管理</h1>

    <!-- Filters -->
    <div class="search-bar">
      <el-input v-model="quecremsParams.studentName" placeholder="学生姓名" clearable @keyup.enter="handleQuery" style="width: 160px" />
      <el-select v-model="quecremsParams.status" placeholder="状态" clearable style="width: 130px" @change="handleQuery">
        <el-option label="待确认" value="0" />
        <el-option label="已确认" value="1" />
        <el-option label="已完成" value="2" />
        <el-option label="已取消" value="3" />
      </el-select>
      <el-button type="primary" @click="handleQuery">搜索</el-button>
    </div>

    <!-- Interview list -->
    <div class="iv-list" v-loading="loading">
      <div v-for="item in interviewList" :key="item.interviewId" class="iv-card">
        <div class="iv-card__left">
          <div class="iv-card__date">
            <div class="date-day">{{ parseTime(item.interviewTime, '{d}') }}</div>
            <div class="date-month">{{ parseTime(item.interviewTime, '{m}月') }}</div>
          </div>
        </div>
        <div class="iv-card__main">
          <div class="iv-card__header">
            <div>
              <div class="iv-card__title">{{ item.studentName }} - {{ item.jobTitle }}</div>
              <div class="iv-card__meta">
                <span>{{ parseTime(item.interviewTime, '{h}:{i}') }}</span>
                <span>{{ formatType(item.interviewType) }}</span>
                <span>{{ formatMethod(item.interviewMethod) }}</span>
                <span v-if="item.interviewer">面试官：{{ item.interviewer }}</span>
              </div>
            </div>
            <el-tag :type="getStatusType(item.status)" size="small" class="portal-tag">
              {{ getStatusLabel(item.status) }}
            </el-tag>
          </div>
          <div class="iv-card__notice" v-if="item.interviewNotice">
            {{ item.interviewNotice }}
          </div>
          <div class="iv-card__result" v-if="item.interviewResult && item.interviewResult !== 'pending'">
            <span class="result-label">面试结果：</span>
            <el-tag :type="item.interviewResult === 'pass' ? 'success' : 'danger'" size="small">
              {{ item.interviewResult === 'pass' ? '通过' : '未通过' }}
            </el-tag>
            <span class="feedback" v-if="item.interviewFeedback">{{ item.interviewFeedback }}</span>
          </div>
        </div>
        <div class="iv-card__actions">
          <el-button v-if="item.status === '0'" type="primary" size="small" @click="handleConfirm(item)">确认</el-button>
          <el-button v-if="item.status === '1'" type="success" size="small" @click="handleComplete(item)">完成</el-button>
          <el-button v-if="item.status !== '2' && item.status !== '3'" type="danger" size="small" plain @click="handleCancel(item)">取消</el-button>
          <el-button type="primary" link size="small" @click="handleEdit(item)">编辑</el-button>
        </div>
      </div>
    </div>

    <div class="portal-empty" v-if="!loading && interviewList.length === 0">
      <div class="portal-empty__text">暂无面试安排</div>
    </div>

    <div style="margin-top: 24px; display: flex; justify-content: center" v-if="total > 0">
      <pagination :total="total" v-model:page="quecremsParams.pageNum" v-model:limit="quecremsParams.pageSize" @pagination="getList" />
    </div>

    <!-- Edit dialog -->
    <el-dialog title="编辑面试" v-model="editOpen" width="500px" append-to-body>
      <el-form :model="editForm" ref="editRef" label-width="100px">
        <el-form-item label="面试类型">
          <el-select v-model="editForm.interviewType" style="width: 100%">
            <el-option label="初试" value="first" />
            <el-option label="复试" value="second" />
            <el-option label="终试" value="final" />
          </el-select>
        </el-form-item>
        <el-form-item label="面试方式">
          <el-select v-model="editForm.interviewMethod" style="width: 100%">
            <el-option label="现场面试" value="onsite" />
            <el-option label="视频面试" value="video" />
            <el-option label="电话面试" value="phone" />
          </el-select>
        </el-form-item>
        <el-form-item label="面试时间">
          <el-date-picker v-model="editForm.interviewTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
        </el-form-item>
        <el-form-item label="面试地点">
          <el-input v-model="editForm.interviewAddress" />
        </el-form-item>
        <el-form-item label="面试官">
          <el-input v-model="editForm.interviewer" />
        </el-form-item>
        <el-form-item label="面试结果">
          <el-select v-model="editForm.interviewResult" style="width: 100%" clearable>
            <el-option label="待定" value="pending" />
            <el-option label="通过" value="pass" />
            <el-option label="未通过" value="fail" />
          </el-select>
        </el-form-item>
        <el-form-item label="面试反馈">
          <el-input v-model="editForm.interviewFeedback" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editOpen = false">取消</el-button>
        <el-button type="primary" @click="submitEdit" :loading="editLoading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { listInterview, updateInterview } from '@/api/portal'
import { parseTime } from '@/utils/crems'

const { proxy } = getCurrentInstance()

const loading = ref(false)
const interviewList = ref([])
const total = ref(0)
const editOpen = ref(false)
const editLoading = ref(false)
const editRef = ref(null)

const quecremsParams = reactive({ pageNum: 1, pageSize: 10, studentName: undefined, status: undefined })

const editForm = reactive({
  interviewId: undefined,
  interviewType: '',
  interviewMethod: '',
  interviewTime: '',
  interviewAddress: '',
  interviewer: '',
  interviewResult: '',
  interviewFeedback: ''
})

const statusMap = {
  '0': { label: '待确认', type: 'warning' },
  '1': { label: '已确认', type: 'success' },
  '2': { label: '已完成', type: '' },
  '3': { label: '已取消', type: 'info' }
}
const typeMap = { first: '初试', second: '复试', final: '终试' }
const methodMap = { onsite: '现场', video: '视频', phone: '电话' }
const getStatusLabel = (s) => statusMap[s]?.label || s
const getStatusType = (s) => statusMap[s]?.type || 'info'
const formatType = (t) => typeMap[t] || t
const formatMethod = (m) => methodMap[m] || m

function getList() {
  loading.value = true
  listInterview(quecremsParams).then(res => {
    interviewList.value = res.rows || []
    total.value = res.total || 0
  }).finally(() => { loading.value = false })
}

function handleQuery() {
  quecremsParams.pageNum = 1
  getList()
}

async function handleConfirm(item) {
  await updateInterview({ interviewId: item.interviewId, status: '1' })
  proxy.$modal.msgSuccess('已确认')
  getList()
}

async function handleComplete(item) {
  await updateInterview({ interviewId: item.interviewId, status: '2' })
  proxy.$modal.msgSuccess('已标记完成')
  getList()
}

async function handleCancel(item) {
  try {
    await proxy.$modal.confirm('确认取消该面试？')
    await updateInterview({ interviewId: item.interviewId, status: '3' })
    proxy.$modal.msgSuccess('已取消')
    getList()
  } catch (e) { /* cancelled */ }
}

function handleEdit(item) {
  Object.assign(editForm, {
    interviewId: item.interviewId,
    interviewType: item.interviewType,
    interviewMethod: item.interviewMethod,
    interviewTime: item.interviewTime,
    interviewAddress: item.interviewAddress,
    interviewer: item.interviewer,
    interviewResult: item.interviewResult || 'pending',
    interviewFeedback: item.interviewFeedback || ''
  })
  editOpen.value = true
}

async function submitEdit() {
  editLoading.value = true
  try {
    await updateInterview(editForm)
    proxy.$modal.msgSuccess('保存成功')
    editOpen.value = false
    getList()
  } catch (e) {
    proxy.$modal.msgError('保存失败')
  } finally {
    editLoading.value = false
  }
}

onMounted(() => getList())
</script>

<style lang="scss" scoped>
.search-bar {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.iv-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.iv-card {
  display: flex;
  gap: 20px;
  background: #fff;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
  padding: 20px;
  transition: box-shadow 0.2s;

  &:hover { box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06); }

  &__left { flex-shrink: 0; }

  &__date {
    text-align: center;
    min-width: 56px;

    .date-day {
      font-size: 28px;
      font-weight: 800;
      color: #409eff;
      line-height: 1;
    }

    .date-month {
      font-size: 13px;
      color: #909399;
      margin-top: 4px;
    }
  }

  &__main {
    flex: 1;
    min-width: 0;
  }

  &__header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 12px;
  }

  &__title {
    font-size: 15px;
    font-weight: 700;
    color: #1a1a2e;
  }

  &__meta {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    font-size: 12px;
    color: #909399;
    margin-top: 6px;
  }

  &__notice {
    margin-top: 10px;
    padding: 10px 12px;
    background: #f5f7fa;
    border-radius: 6px;
    font-size: 13px;
    color: #606266;
    line-height: 1.5;
  }

  &__result {
    margin-top: 10px;
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 13px;

    .result-label { color: #909399; }
    .feedback { color: #606266; }
  }

  &__actions {
    display: flex;
    flex-direction: column;
    gap: 6px;
    flex-shrink: 0;
  }
}
</style>
