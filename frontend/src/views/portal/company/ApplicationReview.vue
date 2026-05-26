<template>
  <div class="app-review-page">
    <h1 class="portal-page-title">简历处理</h1>

    <!-- Filters -->
    <div class="search-bar">
      <el-input v-model="queryParams.studentName" placeholder="学生姓名" clearable @keyup.enter="handleQuery" style="width: 160px" />
      <el-select v-model="queryParams.status" placeholder="状态" clearable style="width: 130px" @change="handleQuery">
        <el-option label="待查看" value="0" />
        <el-option label="已查看" value="1" />
        <el-option label="初筛通过" value="2" />
        <el-option label="面试邀请" value="3" />
        <el-option label="已拒绝" value="4" />
        <el-option label="已录用" value="5" />
      </el-select>
      <el-button type="primary" @click="handleQuery">搜索</el-button>
    </div>

    <!-- Application list -->
    <div class="app-list" v-loading="loading">
      <div v-for="(app, index) in appList" :key="app.applicationId" class="app-card" v-anime-stagger="index * 60">
        <div class="app-card__header">
          <div class="app-card__info">
            <div class="student-name">{{ app.studentName }}</div>
            <div class="job-info">投递：{{ app.jobTitle }}</div>
          </div>
          <el-tag :type="getStatusType(app.status)" class="portal-tag">
            {{ getStatusLabel(app.status) }}
          </el-tag>
        </div>
        <div class="app-card__body" v-if="app.coverLetter">
          <div class="cover-letter">
            <span class="label">求职信：</span>{{ app.coverLetter }}
          </div>
        </div>
        <div class="app-card__footer">
          <span class="apply-time">投递于 {{ parseTime(app.applyTime) }}</span>
          <div class="action-btns">
            <el-button v-anime-button type="primary" size="small" plain @click="handleView(app)">查看详情</el-button>
            <el-button v-anime-button v-if="app.status === '0'" type="primary" size="small" @click="handleStatus(app, '1')">标记已查看</el-button>
            <el-button v-anime-button v-if="app.status === '0' || app.status === '1'" type="success" size="small" @click="handleStatus(app, '2')">初筛通过</el-button>
            <el-button v-anime-button v-if="app.status === '2'" type="primary" size="small" @click="handleInvite(app)">邀请面试</el-button>
            <el-button v-anime-button v-if="app.status === '3'" type="success" size="small" @click="handleStatus(app, '5')">录用</el-button>
            <el-button v-anime-button v-if="app.status !== '4' && app.status !== '5'" type="danger" size="small" plain @click="handleStatus(app, '4')">拒绝</el-button>
          </div>
        </div>
      </div>
    </div>

    <div class="portal-empty" v-if="!loading && appList.length === 0">
      <div class="portal-empty__text">暂无投递记录</div>
    </div>

    <div style="margin-top: 24px; display: flex; justify-content: center" v-if="total > 0">
      <pagination :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </div>

    <!-- Detail dialog -->
    <el-dialog title="投递详情" v-model="viewOpen" width="700px" destroy-on-close>
      <div style="max-height: 65vh; overflow-y: auto; padding-right: 8px;">
      <el-divider content-position="left">投递信息</el-divider>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="投递ID">{{ viewData.applicationId }}</el-descriptions-item>
        <el-descriptions-item label="职位名称">{{ viewData.jobTitle }}</el-descriptions-item>
        <el-descriptions-item label="投递时间">{{ parseTime(viewData.applyTime) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(viewData.status)">{{ getStatusLabel(viewData.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="查看时间">{{ parseTime(viewData.viewTime) || '-' }}</el-descriptions-item>
        <el-descriptions-item label="企业反馈" :span="2">{{ viewData.feedback || '-' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">学生简历</el-divider>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">{{ viewData.studentName }}</el-descriptions-item>
        <el-descriptions-item label="学号">{{ viewData.studentNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ viewData.gender === '0' ? '男' : viewData.gender === '1' ? '女' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ viewData.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ viewData.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学校">{{ viewData.school || '-' }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ viewData.major || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学历">{{ viewData.education || '-' }}</el-descriptions-item>
        <el-descriptions-item label="年级">{{ viewData.grade || '-' }}</el-descriptions-item>
        <el-descriptions-item label="毕业时间">{{ parseTime(viewData.graduationDate) || '-' }}</el-descriptions-item>
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
              下载简历
            </el-button>
          </template>
          <span v-else>未上传简历</span>
        </el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">求职信</el-divider>
      <div style="padding: 10px; background: #f5f7fa; border-radius: 4px; min-height: 60px;">
        {{ viewData.coverLetter || '未填写求职信' }}
      </div>
      </div>
    </el-dialog>

    <!-- Invite interview dialog -->
    <el-dialog title="邀请面试" v-model="inviteOpen" width="500px" append-to-body>
      <el-form :model="inviteForm" :rules="inviteRules" ref="inviteRef" label-width="100px">
        <el-form-item label="面试类型" prop="interviewType">
          <el-select v-model="inviteForm.interviewType" placeholder="请选择" style="width: 100%">
            <el-option label="初试" value="first" />
            <el-option label="复试" value="second" />
            <el-option label="终试" value="final" />
          </el-select>
        </el-form-item>
        <el-form-item label="面试方式" prop="interviewMethod">
          <el-select v-model="inviteForm.interviewMethod" placeholder="请选择" style="width: 100%">
            <el-option label="现场面试" value="onsite" />
            <el-option label="视频面试" value="video" />
            <el-option label="电话面试" value="phone" />
          </el-select>
        </el-form-item>
        <el-form-item label="面试时间" prop="interviewTime">
          <el-date-picker v-model="inviteForm.interviewTime" type="datetime" placeholder="选择时间" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
        </el-form-item>
        <el-form-item label="面试地点">
          <el-input v-model="inviteForm.interviewAddress" placeholder="面试地址或视频链接" />
        </el-form-item>
        <el-form-item label="面试官">
          <el-input v-model="inviteForm.interviewer" placeholder="面试官姓名" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="inviteForm.contactPhone" placeholder="联系电话" />
        </el-form-item>
        <el-form-item label="面试须知">
          <el-input v-model="inviteForm.interviewNotice" type="textarea" :rows="3" placeholder="需要候选人注意的事项" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="inviteOpen = false">取消</el-button>
        <el-button type="primary" @click="submitInvite" :loading="inviteLoading">发送邀请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { listApplication, updateApplication, addInterview } from '@/api/portal'
import { parseTime } from '@/utils/ruoyi'

const { proxy } = getCurrentInstance()

const loading = ref(false)
const appList = ref([])
const total = ref(0)
const viewOpen = ref(false)
const viewData = ref({})
const inviteOpen = ref(false)
const inviteLoading = ref(false)
const inviteRef = ref(null)
const currentApp = ref(null)

const queryParams = reactive({ pageNum: 1, pageSize: 10, studentName: undefined, status: undefined })

const inviteForm = reactive({
  interviewType: 'first',
  interviewMethod: 'onsite',
  interviewTime: '',
  interviewAddress: '',
  interviewer: '',
  contactPhone: '',
  interviewNotice: ''
})

const inviteRules = {
  interviewType: [{ required: true, message: '请选择面试类型', trigger: 'change' }],
  interviewMethod: [{ required: true, message: '请选择面试方式', trigger: 'change' }],
  interviewTime: [{ required: true, message: '请选择面试时间', trigger: 'change' }]
}

const statusMap = {
  '0': { label: '待查看', type: 'warning' },
  '1': { label: '已查看', type: '' },
  '2': { label: '初筛通过', type: 'success' },
  '3': { label: '面试邀请', type: 'success' },
  '4': { label: '已拒绝', type: 'danger' },
  '5': { label: '已录用', type: 'success' }
}
const getStatusLabel = (s) => statusMap[s]?.label || s
const getStatusType = (s) => statusMap[s]?.type || 'info'

function getList() {
  loading.value = true
  listApplication(queryParams).then(res => {
    appList.value = res.rows || []
    total.value = res.total || 0
  }).finally(() => { loading.value = false })
}

function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

async function handleStatus(app, status) {
  try {
    await updateApplication({ applicationId: app.applicationId, status })
    proxy.$modal.msgSuccess('操作成功')
    getList()
  } catch (e) {
    proxy.$modal.msgError('操作失败')
  }
}

function handleView(app) {
  viewData.value = { ...app }
  viewOpen.value = true
  // 待查看状态自动标记为已查看
  if (app.status === '0') {
    updateApplication({ applicationId: app.applicationId, status: '1' }).then(() => {
      getList()
    }).catch(() => {})
  }
}

function downloadResume(url) {
  if (url) {
    window.open(url)
  }
}

function handleInvite(app) {
  currentApp.value = app
  Object.assign(inviteForm, {
    interviewType: 'first', interviewMethod: 'onsite', interviewTime: '',
    interviewAddress: '', interviewer: '', contactPhone: '', interviewNotice: ''
  })
  inviteOpen.value = true
}

async function submitInvite() {
  const valid = await inviteRef.value?.validate().catch(() => false)
  if (!valid) return

  inviteLoading.value = true
  try {
    const app = currentApp.value
    await addInterview({
      applicationId: app.applicationId,
      jobId: app.jobId,
      studentId: app.studentId,
      companyId: app.companyId,
      ...inviteForm
    })
    await updateApplication({ applicationId: app.applicationId, status: '3' })
    proxy.$modal.msgSuccess('面试邀请已发送')
    inviteOpen.value = false
    getList()
  } catch (e) {
    proxy.$modal.msgError('操作失败')
  } finally {
    inviteLoading.value = false
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

.app-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.app-card {
  background: #fff;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
  padding: 20px;
  transition: box-shadow 0.2s;

  &:hover { box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06); }

  &__header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 10px;
  }

  &__info {
    .student-name {
      font-size: 16px;
      font-weight: 700;
      color: #1a1a2e;
    }

    .job-info {
      font-size: 13px;
      color: #909399;
      margin-top: 4px;
    }
  }

  &__body {
    margin-bottom: 12px;

    .cover-letter {
      font-size: 13px;
      color: #606266;
      line-height: 1.6;

      .label {
        color: #909399;
        font-weight: 500;
      }
    }
  }

  &__footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-top: 12px;
    border-top: 1px solid #f5f5f5;

    .apply-time {
      font-size: 12px;
      color: #c0c4cc;
    }

    .action-btns {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
    }
  }
}
</style>
