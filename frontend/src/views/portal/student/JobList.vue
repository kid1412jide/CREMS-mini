<template>
  <div class="job-list-page">
    <h1 class="portal-page-title">找职位</h1>

    <!-- Search Bar -->
    <div class="search-bar">
      <el-input
        v-model="quecremsParams.jobTitle"
        placeholder="搜索职位名称..."
        size="large"
        clearable
        @keyup.enter="handleQuery"
      >
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
      <el-input
        v-model="quecremsParams.workCity"
        placeholder="城市"
        size="large"
        clearable
        style="width: 140px"
        @keyup.enter="handleQuery"
      />
      <el-select v-model="quecremsParams.jobType" placeholder="职位类型" size="large" clearable style="width: 130px">
        <el-option label="全职" value="full_time" />
        <el-option label="实习" value="internship" />
        <el-option label="兼职" value="part_time" />
      </el-select>
      <el-select v-model="quecremsParams.educationRequired" placeholder="学历要求" size="large" clearable style="width: 130px">
        <el-option label="专科" value="junior_college" />
        <el-option label="本科" value="bachelor" />
        <el-option label="硕士" value="master" />
        <el-option label="博士" value="doctor" />
      </el-select>
      <el-button v-anime-button type="primary" size="large" @click="handleQuery" :icon="Search">搜索</el-button>
    </div>

    <!-- Results -->
    <div class="results-header" v-if="!loading">
      <span class="results-count">共找到 <strong>{{ total }}</strong> 个职位</span>
    </div>

    <div class="portal-grid portal-grid--3" v-loading="loading">
      <JobCard
        v-for="job in jobList"
        :key="job.jobId"
        :job="job"
        :favorited="favoritedSet.has(job.jobId)"
        @click="goDetail(job)"
        @favorite="toggleFavorite(job)"
      />
    </div>

    <div class="portal-empty" v-if="!loading && jobList.length === 0">
      <div class="portal-empty__text">没有找到匹配的职位，试试调整搜索条件</div>
    </div>

    <div style="margin-top: 24px; display: flex; justify-content: center" v-if="total > 0">
      <pagination
        :total="total"
        v-model:page="quecremsParams.pageNum"
        v-model:limit="quecremsParams.pageSize"
        @pagination="getList"
      />
    </div>
  </div>
</template>

<script setup>
import { Search } from '@element-plus/icons-vue'
import { listJob, listFavorite, addFavorite, delFavoriteByJobAndStudent } from '@/api/portal'
import JobCard from '@/components/portal/JobCard.vue'
import useUserStore from '@/store/modules/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const { proxy } = getCurrentInstance()

const loading = ref(false)
const jobList = ref([])
const total = ref(0)
const favoritedSet = ref(new Set())

const quecremsParams = reactive({
  pageNum: 1,
  pageSize: 12,
  jobTitle: route.query.jobTitle || undefined,
  workCity: route.query.workCity || undefined,
  jobType: undefined,
  educationRequired: undefined,
  status: '1'
})

function getList() {
  loading.value = true
  listJob(quecremsParams).then(res => {
    jobList.value = res.rows || []
    total.value = res.total || 0
  }).finally(() => { loading.value = false })
}

function handleQuery() {
  quecremsParams.pageNum = 1
  getList()
}

function goDetail(job) {
  router.push(`/portal/student/job/${job.jobId}`)
}

async function loadFavorites() {
  try {
    const res = await listFavorite({ pageSize: 500, pageNum: 1 })
    favoritedSet.value = new Set((res.rows || []).map(f => f.jobId))
  } catch (e) { /* ignore */ }
}

async function toggleFavorite(job) {
  try {
    if (favoritedSet.value.has(job.jobId)) {
      await delFavoriteByJobAndStudent(job.jobId)
      favoritedSet.value.delete(job.jobId)
      proxy.$modal.msgSuccess('已取消收藏')
    } else {
      await addFavorite({ jobId: job.jobId })
      favoritedSet.value.add(job.jobId)
      proxy.$modal.msgSuccess('收藏成功')
    }
    favoritedSet.value = new Set(favoritedSet.value)
  } catch (e) {
    proxy.$modal.msgError('操作失败')
  }
}

onMounted(() => {
  getList()
  loadFavorites()
})
</script>

<style lang="scss" scoped>
.search-bar {
  display: flex;
  gap: 8px;
  background: #fff;
  padding: 16px;
  border-radius: 12px;
  border: 1px solid #f0f0f0;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.results-header {
  margin-bottom: 16px;

  .results-count {
    font-size: 13px;
    color: #909399;

    strong {
      color: #409eff;
      font-weight: 700;
    }
  }
}
</style>
