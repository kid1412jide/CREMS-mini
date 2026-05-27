<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="企业名称" prop="companyName">
        <el-input v-model="queryParams.companyName" placeholder="请输入企业名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="企业类型" prop="companyType">
        <el-select v-model="queryParams.companyType" placeholder="请选择" clearable style="width: 150px">
          <el-option v-for="dict in crems_company_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 150px">
          <el-option v-for="dict in crems_company_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['crems:company:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['crems:company:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['crems:company:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['crems:company:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="Upload" @click="handleImport" v-hasPermi="['crems:company:import']">导入</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns" />
    </el-row>

    <el-table v-loading="loading" :data="companyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="企业ID" align="center" key="companyId" prop="companyId" v-if="columns.companyId.visible" width="80" />
      <el-table-column label="企业名称" align="center" key="companyName" prop="companyName" :show-overflow-tooltip="true" />
      <el-table-column label="统一社会信用代码" align="center" key="companyCode" prop="companyCode" width="180" />
      <el-table-column label="企业类型" align="center" key="companyType" prop="companyType" width="100">
        <template #default="scope">
          <dict-tag :options="crems_company_type" :value="scope.row.companyType" />
        </template>
      </el-table-column>
      <el-table-column label="所属行业" align="center" key="industry" prop="industry" width="120" />
      <el-table-column label="企业规模" align="center" key="scale" prop="scale" width="100" />
      <el-table-column label="联系人" align="center" key="contactPerson" prop="contactPerson" width="100" />
      <el-table-column label="联系电话" align="center" key="contactPhone" prop="contactPhone" width="130" />
      <el-table-column label="状态" align="center" key="status" prop="status" width="100">
        <template #default="scope">
          <dict-tag :options="crems_company_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['crems:company:query']">详情</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['crems:company:edit']">编辑</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['crems:company:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改企业对话框 -->
    <el-dialog :title="title" v-model="open" width="700px" append-to-body>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="企业名称" prop="companyName">
              <el-input v-model="form.companyName" placeholder="请输入企业名称" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="统一社会信用代码" prop="companyCode">
              <el-input v-model="form.companyCode" placeholder="请输入统一社会信用代码" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="企业类型" prop="companyType">
              <el-select v-model="form.companyType" placeholder="请选择" style="width: 100%">
                <el-option v-for="dict in crems_company_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属行业" prop="industry">
              <el-input v-model="form.industry" placeholder="请输入所属行业" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="企业规模" prop="scale">
              <el-select v-model="form.scale" placeholder="请选择" style="width: 100%">
                <el-option v-for="dict in crems_company_scale" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="企业地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入企业地址" maxlength="200" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="form.contactPerson" placeholder="请输入联系人" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系电话" maxlength="20" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系邮箱" prop="contactEmail">
              <el-input v-model="form.contactEmail" placeholder="请输入联系邮箱" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="企业网站" prop="website">
              <el-input v-model="form.website" placeholder="请输入企业网站" maxlength="200" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="企业简介" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入企业简介" :rows="3" maxlength="500" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 企业详情弹窗 -->
    <el-dialog title="企业详情" v-model="viewOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="企业名称">{{ viewData.companyName }}</el-descriptions-item>
        <el-descriptions-item label="统一社会信用代码">{{ viewData.companyCode }}</el-descriptions-item>
        <el-descriptions-item label="企业类型"><dict-tag :options="crems_company_type" :value="viewData.companyType" /></el-descriptions-item>
        <el-descriptions-item label="所属行业">{{ viewData.industry }}</el-descriptions-item>
        <el-descriptions-item label="企业规模"><dict-tag :options="crems_company_scale" :value="viewData.scale" /></el-descriptions-item>
        <el-descriptions-item label="企业地址">{{ viewData.address }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ viewData.contactPerson }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ viewData.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="联系邮箱">{{ viewData.contactEmail }}</el-descriptions-item>
        <el-descriptions-item label="企业网站">{{ viewData.website }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <dict-tag :options="crems_company_status" :value="viewData.status" />
        </el-descriptions-item>
        <el-descriptions-item label="企业简介" :span="2">{{ viewData.description }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" v-model="upload.open" width="400px" append-to-body>
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的企业数据
            </div>
            <span>仅允许导入 xls、xlsx 格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Company">
import { listCompany, addCompany, updateCompany, delCompany } from "@/api/crems/company"
import { getToken } from "@/utils/auth"

const { proxy } = getCurrentInstance()
const { crems_company_type, crems_company_scale, crems_company_status } = toRefs(reactive(proxy.useDict('crems_company_type', 'crems_company_scale', 'crems_company_status')))
const loading = ref(true)
const showSearch = ref(true)
const companyList = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const open = ref(false)
const viewOpen = ref(false)
const viewData = ref({})
const ids = ref([])

const columns = ref({
  companyId: { visible: true, label: '企业ID' },
  companyName: { visible: true, label: '企业名称' },
  companyCode: { visible: true, label: '统一社会信用代码' },
  companyType: { visible: true, label: '企业类型' },
  industry: { visible: true, label: '所属行业' },
  scale: { visible: true, label: '企业规模' },
  contactPerson: { visible: true, label: '联系人' },
  contactPhone: { visible: true, label: '联系电话' },
  status: { visible: true, label: '状态' },
  createTime: { visible: true, label: '创建时间' }
})

const upload = reactive({
  open: false,
  title: '',
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: 'Bearer ' + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + '/crems/company/importData'
})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    companyName: undefined,
    companyType: undefined,
    status: undefined
  },
  rules: {
    companyName: [{ required: true, message: "企业名称不能为空", trigger: "blur" }],
    companyCode: [{ required: true, message: "统一社会信用代码不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询企业列表 */
function getList() {
  loading.value = true
  listCompany(queryParams.value).then(res => {
    loading.value = false
    companyList.value = res.rows
    total.value = res.total
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 选择条数 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.companyId)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加企业"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  open.value = true
  title.value = "修改企业"
  form.value = JSON.parse(JSON.stringify(row))
}

/** 详情按钮操作 */
function handleView(row) {
  viewData.value = row
  viewOpen.value = true
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["formRef"].validate(valid => {
    if (valid) {
      if (form.value.companyId !== undefined) {
        updateCompany(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addCompany(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const companyIds = row.companyId || ids.value
  proxy.$modal.confirm('是否确认删除企业ID为"' + companyIds + '"的数据项？').then(() => {
    return delCompany(companyIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("crems/company/export", { ...queryParams.value }, `company_${new Date().getTime()}.xlsx`)
}

/** 导入按钮操作 */
function handleImport() {
  upload.title = "企业管理 - 导入数据"
  upload.open = true
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download("crems/company/importTemplate", {}, `company_template_${new Date().getTime()}.xlsx`)
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit()
}

/** 文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true
}

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false
  upload.isUploading = false
  proxy.$refs["uploadRef"].clearFiles()
  // 对 response.msg 进行 HTML 转义，防止 XSS 攻击
  const escapedMsg = response.msg ? response.msg.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#x27;') : ''
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + escapedMsg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
  getList()
}

/** 重置表单 */
function reset() {
  form.value = {
    companyId: undefined,
    companyName: undefined,
    companyCode: undefined,
    companyType: undefined,
    industry: undefined,
    scale: undefined,
    address: undefined,
    website: undefined,
    description: undefined,
    contactPerson: undefined,
    contactPhone: undefined,
    contactEmail: undefined,
    status: '0'
  }
  proxy.resetForm("formRef")
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

onMounted(() => {
  getList()
})
</script>