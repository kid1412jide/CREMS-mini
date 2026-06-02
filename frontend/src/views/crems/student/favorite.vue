<template>
  <div class="app-container">
    <el-form :model="quecremsParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="职位名称" prop="jobTitle">
        <el-input v-model="quecremsParams.jobTitle" placeholder="请输入职位名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns" />
    </el-row>

    <el-table v-loading="loading" :data="favoriteList" stripe empty-text="暂无收藏数据">
      <el-table-column label="收藏ID" align="center" key="favoriteId" prop="favoriteId" width="80" />
      <el-table-column label="职位名称" align="center" key="jobTitle" prop="jobTitle" :show-overflow-tooltip="true" />
      <el-table-column label="企业名称" align="center" key="companyName" prop="companyName" :show-overflow-tooltip="true" />
      <el-table-column label="收藏时间" align="center" prop="createTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="100" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="danger" icon="Delete" @click="handleCancel(scope.row)">取消收藏</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="quecremsParams.pageNum" v-model:limit="quecremsParams.pageSize" @pagination="getList" />
  </div>
</template>

<script setup name="Favorite">
import { listFavorite, delFavorite } from "@/api/crems/favorite"

const { proxy } = getCurrentInstance()
const loading = ref(true)
const showSearch = ref(true)
const favoriteList = ref([])
const total = ref(0)

const columns = ref({
  favoriteId: { visible: true, label: '收藏ID' },
  jobTitle: { visible: true, label: '职位名称' },
  companyName: { visible: true, label: '企业名称' },
  createTime: { visible: true, label: '收藏时间' }
})

const data = reactive({
  form: {},
  quecremsParams: {
    pageNum: 1,
    pageSize: 10,
    jobTitle: undefined
  }
})

const { quecremsParams, form } = toRefs(data)

function getList() {
  loading.value = true
  listFavorite(quecremsParams.value).then(res => {
    favoriteList.value = res.rows
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

function handleCancel(row) {
  proxy.$modal.confirm('是否确认取消该收藏？').then(() => {
    return delFavorite(row.favoriteId)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("取消收藏成功")
  }).catch(() => {})
}

onMounted(() => {
  getList()
})
</script>