<template>
  <div class="favorites-page">
    <h1 class="portal-page-title">我的收藏</h1>

    <div class="portal-grid portal-grid--3" v-loading="loading">
      <div v-for="fav in favoriteList" :key="fav.favoriteId" class="fav-card">
        <div class="fav-card__header">
          <div class="fav-card__title" @click="goDetail(fav.jobId)">{{ fav.jobTitle }}</div>
          <el-button
            type="warning"
            :icon="StarFilled"
            size="small"
            circle
            @click="handleRemove(fav)"
          />
        </div>
        <div class="fav-card__company">{{ fav.companyName }}</div>
        <div class="fav-card__footer">
          <span class="fav-time">收藏于 {{ parseTime(fav.createTime, '{y}-{m}-{d}') }}</span>
          <el-button type="primary" link size="small" @click="goDetail(fav.jobId)">查看详情</el-button>
        </div>
      </div>
    </div>

    <div class="portal-empty" v-if="!loading && favoriteList.length === 0">
      <div class="portal-empty__text">还没有收藏职位，去发现好职位吧</div>
      <router-link to="/portal/student/jobs">
        <el-button type="primary" size="small">去找职位</el-button>
      </router-link>
    </div>

    <div style="margin-top: 24px; display: flex; justify-content: center" v-if="total > 0">
      <pagination :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </div>
  </div>
</template>

<script setup>
import { StarFilled } from '@element-plus/icons-vue'
import { listFavorite, delFavorite } from '@/api/crems/favorite'
import { parseTime } from '@/utils/ruoyi'

const router = useRouter()
const { proxy } = getCurrentInstance()

const loading = ref(false)
const favoriteList = ref([])
const total = ref(0)
const queryParams = reactive({ pageNum: 1, pageSize: 12 })

function getList() {
  loading.value = true
  listFavorite(queryParams).then(res => {
    favoriteList.value = res.rows || []
    total.value = res.total || 0
  }).finally(() => { loading.value = false })
}

function goDetail(jobId) {
  router.push(`/portal/student/job/${jobId}`)
}

async function handleRemove(fav) {
  try {
    await delFavorite(fav.favoriteId)
    proxy.$modal.msgSuccess('已取消收藏')
    getList()
  } catch (e) {
    proxy.$modal.msgError('操作失败')
  }
}

onMounted(() => getList())
</script>

<style lang="scss" scoped>
.fav-card {
  background: #fff;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
  padding: 20px;
  transition: all 0.25s;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  }

  &__header {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 12px;
  }

  &__title {
    font-size: 16px;
    font-weight: 700;
    color: #1a1a2e;
    cursor: pointer;
    flex: 1;

    &:hover { color: #409eff; }
  }

  &__company {
    font-size: 13px;
    color: #909399;
    margin-top: 6px;
    margin-bottom: 14px;
  }

  &__footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-top: 12px;
    border-top: 1px solid #f5f5f5;

    .fav-time {
      font-size: 12px;
      color: #c0c4cc;
    }
  }
}
</style>
