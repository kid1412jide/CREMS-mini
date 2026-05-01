<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="学号" prop="studentNo">
        <el-input v-model="queryParams.studentNo" placeholder="请输入学号" clearable style="width: 150px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="姓名" prop="studentName">
        <el-input v-model="queryParams.studentName" placeholder="请输入姓名" clearable style="width: 150px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="学校" prop="school">
        <el-input v-model="queryParams.school" placeholder="请输入学校" clearable style="width: 150px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="专业" prop="major">
        <el-input v-model="queryParams.major" placeholder="请输入专业" clearable style="width: 150px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 120px">
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['crems:student:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['crems:student:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['crems:student:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['crems:student:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="Upload" @click="handleImport" v-hasPermi="['crems:student:import']">导入</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns" />
    </el-row>

    <el-table v-loading="loading" :data="studentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="学生ID" align="center" key="studentId" prop="studentId" width="80" />
      <el-table-column label="学号" align="center" key="studentNo" prop="studentNo" width="120" />
      <el-table-column label="姓名" align="center" key="studentName" prop="studentName" width="100" />
      <el-table-column label="性别" align="center" key="gender" prop="gender" width="60">
        <template #default="scope">
          <span>{{ scope.row.gender === '0' ? '男' : '女' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="学校" align="center" key="school" prop="school" :show-overflow-tooltip="true" />
      <el-table-column label="专业" align="center" key="major" prop="major" :show-overflow-tooltip="true" />
      <el-table-column label="学历" align="center" key="education" prop="education" width="80">
        <template #default="scope">
          <dict-tag :options="crems_education" :value="scope.row.education" />
        </template>
      </el-table-column>
      <el-table-column label="手机号" align="center" key="phone" prop="phone" width="130" />
      <el-table-column label="状态" align="center" key="status" prop="status" width="80">
        <template #default="scope">
          <el-switch v-model="scope.row.status" active-value="0" inactive-value="1" @change="handleStatusChange(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">详情</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['crems:student:edit']">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改学生对话框 -->
    <el-dialog :title="title" v-model="open" width="700px" append-to-body>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="学号" prop="studentNo">
              <el-input v-model="form.studentNo" placeholder="请输入学号" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="studentName">
              <el-input v-model="form.studentName" placeholder="请输入姓名" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio label="0">男</el-radio>
                <el-radio label="1">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker v-model="form.birthDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="100" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="学校" prop="school">
              <el-input v-model="form.school" placeholder="请输入学校" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-input v-model="form.major" placeholder="请输入专业" maxlength="100" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="学历" prop="education">
              <el-select v-model="form.education" placeholder="请选择" style="width: 100%">
                <el-option v-for="dict in crems_education" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年级" prop="grade">
              <el-input v-model="form.grade" placeholder="如：2022级" maxlength="20" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="技能" prop="skills">
          <el-input v-model="form.skills" placeholder="请输入技能标签，多个用逗号分隔" maxlength="500" />
        </el-form-item>
        <el-form-item label="自我介绍" prop="selfIntroduction">
          <el-input v-model="form.selfIntroduction" type="textarea" placeholder="请输入自我介绍" :rows="3" maxlength="500" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 学生详情弹窗 -->
    <el-dialog title="学生详情" v-model="viewOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="学号">{{ viewData.studentNo }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ viewData.studentName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ viewData.gender === '0' ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ viewData.birthDate }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ viewData.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ viewData.email }}</el-descriptions-item>
        <el-descriptions-item label="学校">{{ viewData.school }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ viewData.major }}</el-descriptions-item>
        <el-descriptions-item label="学历">
          <dict-tag :options="crems_education" :value="viewData.education" />
        </el-descriptions-item>
        <el-descriptions-item label="年级">{{ viewData.grade }}</el-descriptions-item>
        <el-descriptions-item label="技能" :span="2">{{ viewData.skills }}</el-descriptions-item>
        <el-descriptions-item label="自我介绍" :span="2">{{ viewData.selfIntroduction }}</el-descriptions-item>
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
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的学生数据
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

<script setup name="Student">
import { listStudent, addStudent, updateStudent, delStudent } from "@/api/crems/student"
import { getToken } from "@/utils/auth"

const { proxy } = getCurrentInstance()
const { crems_education } = toRefs(reactive(proxy.useDict('crems_education')))
const loading = ref(true)
const showSearch = ref(true)
const studentList = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const open = ref(false)
const viewOpen = ref(false)
const viewData = ref({})
const ids = ref([])

const columns = ref({
  studentId: { visible: true, label: '学生ID' },
  studentNo: { visible: true, label: '学号' },
  studentName: { visible: true, label: '姓名' },
  gender: { visible: true, label: '性别' },
  school: { visible: true, label: '学校' },
  major: { visible: true, label: '专业' },
  education: { visible: true, label: '学历' },
  phone: { visible: true, label: '手机号' },
  status: { visible: true, label: '状态' }
})

const upload = reactive({
  open: false,
  title: '',
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: 'Bearer ' + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + '/crems/student/importData'
})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    studentNo: undefined,
    studentName: undefined,
    school: undefined,
    major: undefined,
    status: undefined
  },
  rules: {
    studentNo: [{ required: true, message: "学号不能为空", trigger: "blur" }],
    studentName: [{ required: true, message: "姓名不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listStudent(queryParams.value).then(res => {
    loading.value = false
    studentList.value = res.rows
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

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.studentId)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = "添加学生"
}

function handleUpdate(row) {
  reset()
  open.value = true
  title.value = "修改学生"
  form.value = JSON.parse(JSON.stringify(row))
}

function handleView(row) {
  viewData.value = row
  viewOpen.value = true
}

function submitForm() {
  proxy.$refs["formRef"].validate(valid => {
    if (valid) {
      if (form.value.studentId !== undefined) {
        updateStudent(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addStudent(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

function handleDelete(row) {
  const studentIds = row.studentId || ids.value
  proxy.$modal.confirm('是否确认删除学生ID为"' + studentIds + '"的数据项？').then(() => {
    return delStudent(studentIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function handleExport() {
  proxy.download("crems/student/export", { ...queryParams.value }, `student_${new Date().getTime()}.xlsx`)
}

/** 导入按钮操作 */
function handleImport() {
  upload.title = "学生管理 - 导入数据"
  upload.open = true
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download("crems/student/importTemplate", {}, `student_template_${new Date().getTime()}.xlsx`)
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
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
  getList()
}

function handleStatusChange(row) {
  const text = row.status === "0" ? "启用" : "停用"
  proxy.$modal.confirm('确认要"' + text + '"该学生吗？').then(() => {
    return updateStudent(row)
  }).then(() => {
    proxy.$modal.msgSuccess(text + "成功")
  }).catch(() => {
    row.status = row.status === "0" ? "1" : "0"
  })
}

function reset() {
  form.value = {
    studentId: undefined,
    studentNo: undefined,
    studentName: undefined,
    gender: '0',
    birthDate: undefined,
    phone: undefined,
    email: undefined,
    school: undefined,
    major: undefined,
    education: undefined,
    grade: undefined,
    skills: undefined,
    selfIntroduction: undefined,
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