<template>
  <div class="job-detail-page" v-loading="loading">
    <div class="detail-layout">
      <!-- Main content -->
      <div class="detail-main">
        <div class="detail-card">
          <div class="detail-header">
            <div>
              <h1 class="detail-title">{{ job.jobTitle }}</h1>
              <div class="detail-company">{{ job.companyName }}</div>
            </div>
            <div class="detail-salary">
              {{ job.salaryMin }}-{{ job.salaryMax }}<span class="unit">元/月</span>
            </div>
          </div>
          <div class="detail-tags">
            <span class="dt-tag">{{ job.workCity || '未填写' }}</span>
            <span class="dt-tag" v-if="job.jobType">{{ formatJobType(job.jobType) }}</span>
            <span class="dt-tag" v-if="job.educationRequired">{{ formatEdu(job.educationRequired) }}</span>
            <span class="dt-tag" v-if="job.experienceRequired">{{ job.experienceRequired }}</span>
            <span class="dt-tag" v-if="job.recruitNum">招{{ job.recruitNum }}人</span>
          </div>
          <div class="detail-meta">
            <span>发布时间：{{ parseTime(job.publishDate) }}</span>
            <span v-if="job.deadline">截止时间：{{ parseTime(job.deadline) }}</span>
            <span>浏览 {{ job.viewCount || 0 }} · 投递 {{ job.applyCount || 0 }}</span>
          </div>
        </div>

        <div class="detail-card">
          <h3 class="section-title">职位描述</h3>
          <div class="section-content">{{ job.jobDescription || '暂无描述' }}</div>
        </div>

        <div class="detail-card" v-if="job.jobRequirements">
          <h3 class="section-title">任职要求</h3>
          <div class="section-content">{{ job.jobRequirements }}</div>
        </div>

        <div class="detail-card" v-if="job.welfare">
          <h3 class="section-title">福利待遇</h3>
          <div class="welfare-tags">
            <span class="welfare-tag" v-for="w in welfareList" :key="w">{{ w }}</span>
          </div>
        </div>
      </div>

      <!-- Sidebar -->
      <div class="detail-sidebar">
        <!-- Apply button -->
        <div class="detail-card apply-card">
          <el-button
            v-if="!hasApplied"
            type="primary"
            size="large"
            class="apply-btn"
            @click="handleApply"
          >
            投递简历
          </el-button>
          <el-button
            v-else
            type="success"
            size="large"
            class="apply-btn"
            disabled
          >
            已投递
          </el-button>
          <el-button
            :type="isFavorited ? 'warning' : 'default'"
            size="large"
            class="fav-btn"
            @click="toggleFavorite"
          >
            {{ isFavorited ? '已收藏' : '收藏职位' }}
          </el-button>
        </div>

        <!-- Company card -->
        <div class="detail-card" v-if="company.companyId">
          <h3 class="section-title">企业信息</h3>
          <div class="company-card">
            <div class="company-card__icon">{{ (company.companyName || '企').charAt(0) }}</div>
            <div class="company-card__name">{{ company.companyName }}</div>
          </div>
          <div class="company-meta">
            <div class="meta-row" v-if="company.industry">
              <span class="meta-label">行业</span>
              <span>{{ company.industry }}</span>
            </div>
            <div class="meta-row" v-if="company.scale">
              <span class="meta-label">规模</span>
              <span>{{ formatScale(company.scale) }}</span>
            </div>
            <div class="meta-row" v-if="company.address">
              <span class="meta-label">地址</span>
              <span>{{ company.address }}</span>
            </div>
          </div>
        </div>

        <!-- Similar jobs -->
        <div class="detail-card" v-if="similarJobs.length > 0">
          <h3 class="section-title">相似职位</h3>
          <div class="similar-list">
            <div
              v-for="s in similarJobs"
              :key="s.jobId"
              class="similar-item"
              @click="router.push(`/portal/student/job/${s.jobId}`)"
            >
              <div class="similar-item__title">{{ s.jobTitle }}</div>
              <div class="similar-item__info">{{ s.salaryMin }}-{{ s.salaryMax }}元/月</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Apply dialog -->
    <el-dialog title="投递简历" v-model="applyOpen" width="500px" append-to-body>
      <el-form :model="applyForm" ref="applyRef" label-width="80px">
        <el-form-item label="简历链接">
          <el-input v-model="applyForm.resumeUrl" placeholder="请输入简历链接或附件地址" />
        </el-form-item>
        <el-form-item label="求职信">
          <el-input v-model="applyForm.coverLetter" type="textarea" :rows="4" placeholder="简单介绍自己，表达对该职位的兴趣（选填）" maxlength="500" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="applyOpen = false">取消</el-button>
        <el-button type="primary" @click="submitApply" :loading="applyLoading">确认投递</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { getJob, getCompany, addApplication, listApplication, addFavorite, delFavoriteByJobAndStudent, listFavorite } from '@/api/portal'
import { parseTime } from '@/utils/ruoyi'

const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()

const jobId = computed(() => route.params.jobId)
const loading = ref(false)
const job = ref({})
const company = ref({})
const similarJobs = ref([])
const hasApplied = ref(false)
const isFavorited = ref(false)
const applyOpen = ref(false)
const applyLoading = ref(false)
const applyForm = ref({ resumeUrl: '', coverLetter: '' })

const jobTypeMap = { full_time: '全职', internship: '实习', part_time: '兼职' }
const eduMap = { junior_college: '专科', bachelor: '本科', master: '硕士', doctor: '博士' }
const scaleMap = { '0-50': '0-50人', '50-150': '50-150人', '150-500': '150-500人', '500-1000': '500-1000人', '1000+': '1000人以上' }
const formatJobType = (t) => jobTypeMap[t] || t
const formatEdu = (e) => eduMap[e] || e || '不限'
const formatScale = (s) => scaleMap[s] || s

const welfareList = computed(() => {
  if (!job.value.welfare) return []
  return job.value.welfare.split(/[,，、;；\s]+/).filter(Boolean)
})

async function loadData() {
  loading.value = true
  try {
    const res = await getJob(jobId.value)
    job.value = res.data || res

    if (job.value.companyId) {
      getCompany(job.value.companyId).then(cr => {
        company.value = cr.data || cr
      }).catch(() => {})
    }

    listApplication({ jobId: jobId.value, pageSize: 1 }).then(ar => {
      hasApplied.value = (ar.total || 0) > 0
    }).catch(() => {})

    listFavorite({ jobId: jobId.value, pageSize: 1 }).then(fr => {
      isFavorited.value = (fr.total || 0) > 0
    }).catch(() => {})
  } finally {
    loading.value = false
  }
}

function handleApply() {
  applyForm.value = { resumeUrl: '', coverLetter: '' }
  applyOpen.value = true
}

async function submitApply() {
  applyLoading.value = true
  try {
    await addApplication({
      jobId: Number(jobId.value),
      companyId: job.value.companyId,
      ...applyForm.value
    })
    proxy.$modal.msgSuccess('投递成功')
    hasApplied.value = true
    applyOpen.value = false
  } catch (e) {
    proxy.$modal.msgError('投递失败，可能已经投递过该职位')
  } finally {
    applyLoading.value = false
  }
}

async function toggleFavorite() {
  try {
    if (isFavorited.value) {
      await delFavoriteByJobAndStudent(jobId.value, 0)
      isFavorited.value = false
      proxy.$modal.msgSuccess('已取消收藏')
    } else {
      await addFavorite({ jobId: Number(jobId.value) })
      isFavorited.value = true
      proxy.$modal.msgSuccess('收藏成功')
    }
  } catch (e) {
    proxy.$modal.msgError('操作失败')
  }
}

watch(jobId, () => { if (jobId.value) loadData() }, { immediate: true })
</script>

<style lang="scss" scoped>
.detail-layout {
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: 20px;
  align-items: start;
}

.detail-card {
  background: #fff;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
  padding: 24px;
  margin-bottom: 16px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.detail-title {
  font-size: 24px;
  font-weight: 800;
  color: #1a1a2e;
  margin: 0;
  letter-spacing: -0.3px;
}

.detail-company {
  font-size: 14px;
  color: #909399;
  margin-top: 6px;
}

.detail-salary {
  font-size: 24px;
  font-weight: 800;
  color: #f56c6c;
  white-space: nowrap;

  .unit {
    font-size: 13px;
    font-weight: 500;
  }
}

.detail-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 14px;

  .dt-tag {
    padding: 4px 12px;
    border-radius: 4px;
    font-size: 13px;
    font-weight: 500;
    color: #606266;
    background: #f5f7fa;
  }
}

.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  font-size: 13px;
  color: #909399;
}

.section-title {
  font-size: 16px;
  font-weight: 700;
  color: #1a1a2e;
  margin: 0 0 14px;
}

.section-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
  white-space: pre-wrap;
}

.welfare-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;

  .welfare-tag {
    padding: 4px 14px;
    border-radius: 20px;
    font-size: 13px;
    color: #409eff;
    background: #ecf5ff;
    font-weight: 500;
  }
}

.apply-card {
  display: flex;
  flex-direction: column;
  gap: 10px;

  .apply-btn {
    width: 100%;
    height: 44px;
    font-size: 16px;
    font-weight: 600;
    border-radius: 8px;
  }

  .fav-btn {
    width: 100%;
    height: 40px;
    border-radius: 8px;
  }
}

.company-card {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 14px;

  &__icon {
    width: 40px;
    height: 40px;
    border-radius: 8px;
    background: linear-gradient(135deg, #e8f4fd, #d1e9ff);
    color: #409eff;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 700;
    font-size: 18px;
  }

  &__name {
    font-size: 15px;
    font-weight: 600;
    color: #303133;
  }
}

.company-meta {
  .meta-row {
    display: flex;
    padding: 6px 0;
    font-size: 13px;
    color: #606266;

    .meta-label {
      width: 48px;
      color: #909399;
      flex-shrink: 0;
    }
  }
}

.similar-list {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.similar-item {
  padding: 10px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.15s;

  &:hover { background: #f5f7fa; }

  &__title {
    font-size: 14px;
    font-weight: 600;
    color: #303133;
  }

  &__info {
    font-size: 12px;
    color: #f56c6c;
    margin-top: 2px;
  }
}

@media (max-width: 1024px) {
  .detail-layout {
    grid-template-columns: 1fr;
  }
}
</style>
