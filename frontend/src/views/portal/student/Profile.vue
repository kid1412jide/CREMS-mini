<template>
  <div class="profile-page">
    <h1 class="portal-page-title">个人简历</h1>

    <div class="profile-layout">
      <div class="profile-main">
        <!-- Basic Info -->
        <div class="profile-card">
          <h3 class="card-title">基本信息</h3>
          <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="姓名" prop="studentName">
                  <el-input v-model="form.studentName" placeholder="请输入姓名" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="学号" prop="studentNo">
                  <el-input v-model="form.studentNo" placeholder="请输入学号" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="性别">
                  <el-radio-group v-model="form.gender">
                    <el-radio value="0">男</el-radio>
                    <el-radio value="1">女</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="出生日期">
                  <el-date-picker v-model="form.birthDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机号" prop="phone">
                  <el-input v-model="form.phone" placeholder="请输入手机号" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="form.email" placeholder="请输入邮箱" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>

        <!-- Education -->
        <div class="profile-card">
          <h3 class="card-title">教育经历</h3>
          <el-form :model="form" label-width="80px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="学校">
                  <el-input v-model="form.school" placeholder="请输入学校名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="专业">
                  <el-input v-model="form.major" placeholder="请输入专业" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="学历">
                  <el-select v-model="form.education" placeholder="请选择" style="width: 100%">
                    <el-option label="专科" value="junior_college" />
                    <el-option label="本科" value="bachelor" />
                    <el-option label="硕士" value="master" />
                    <el-option label="博士" value="doctor" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="年级">
                  <el-input v-model="form.grade" placeholder="如：大三、研二" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="毕业时间">
                  <el-date-picker v-model="form.graduationDate" type="date" placeholder="预计毕业时间" value-format="YYYY-MM-DD" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>

        <!-- Skills -->
        <div class="profile-card">
          <h3 class="card-title">技能标签</h3>
          <div class="skills-area">
            <el-tag
              v-for="skill in skillsList"
              :key="skill"
              closable
              @close="removeSkill(skill)"
              class="skill-tag"
            >
              {{ skill }}
            </el-tag>
            <el-input
              v-if="skillInputVisible"
              ref="skillInputRef"
              v-model="skillInputValue"
              size="small"
              style="width: 120px"
              @keyup.enter="addSkill"
              @blur="addSkill"
            />
            <el-button v-else size="small" @click="showSkillInput">+ 添加技能</el-button>
          </div>
        </div>

        <!-- Self Introduction -->
        <div class="profile-card">
          <h3 class="card-title">自我介绍</h3>
          <el-input
            v-model="form.selfIntroduction"
            type="textarea"
            :rows="5"
            placeholder="介绍一下自己的经历、特长和求职意向..."
            maxlength="1000"
            show-word-limit
          />
        </div>

        <!-- Resume -->
        <div class="profile-card">
          <h3 class="card-title">简历附件</h3>
          <el-input v-model="form.resumeUrl" placeholder="请输入简历链接或附件地址" />
        </div>

        <div class="form-actions">
          <el-button type="primary" size="large" @click="submitForm" :loading="submitting">保存简历</el-button>
        </div>
      </div>

      <!-- Sidebar preview -->
      <div class="profile-sidebar">
        <div class="profile-card preview-card">
          <div class="preview-avatar">
            {{ (form.studentName || '学').charAt(0) }}
          </div>
          <div class="preview-name">{{ form.studentName || '未填写姓名' }}</div>
          <div class="preview-info">{{ form.school || '未填写学校' }} · {{ form.major || '' }}</div>
          <div class="preview-tags" v-if="skillsList.length > 0">
            <span class="preview-tag" v-for="s in skillsList" :key="s">{{ s }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { getStudent, updateStudent, listStudent } from '@/api/crems/student'
import useUserStore from '@/store/modules/user'

const userStore = useUserStore()
const { proxy } = getCurrentInstance()

const formRef = ref(null)
const submitting = ref(false)
const studentId = ref(null)
const form = reactive({
  studentName: '',
  studentNo: '',
  gender: '0',
  birthDate: '',
  phone: '',
  email: '',
  school: '',
  major: '',
  education: '',
  grade: '',
  graduationDate: '',
  selfIntroduction: '',
  resumeUrl: '',
  skills: ''
})

const rules = {
  studentName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  studentNo: [{ required: true, message: '请输入学号', trigger: 'blur' }]
}

const skillsList = computed(() => {
  if (!form.skills) return []
  return form.skills.split(/[,，、;；\s]+/).filter(Boolean)
})

const skillInputVisible = ref(false)
const skillInputValue = ref('')
const skillInputRef = ref(null)

function showSkillInput() {
  skillInputVisible.value = true
  nextTick(() => { skillInputRef.value?.focus() })
}

function addSkill() {
  if (skillInputValue.value) {
    const current = form.skills ? form.skills + ',' + skillInputValue.value : skillInputValue.value
    form.skills = current
  }
  skillInputVisible.value = false
  skillInputValue.value = ''
}

function removeSkill(skill) {
  form.skills = skillsList.value.filter(s => s !== skill).join(',')
}

async function loadProfile() {
  try {
    // Try to find student by current user
    const res = await listStudent({ pageSize: 1, pageNum: 1 })
    if (res.rows && res.rows.length > 0) {
      const student = res.rows[0]
      studentId.value = student.studentId
      Object.keys(form).forEach(key => {
        if (student[key] !== undefined && student[key] !== null) {
          form[key] = student[key]
        }
      })
    }
  } catch (e) { /* ignore */ }
}

async function submitForm() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    if (studentId.value) {
      await updateStudent({ studentId: studentId.value, ...form })
      proxy.$modal.msgSuccess('保存成功')
    } else {
      proxy.$modal.msgWarning('未找到学生信息')
    }
  } catch (e) {
    proxy.$modal.msgError('保存失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => loadProfile())
</script>

<style lang="scss" scoped>
.profile-layout {
  display: grid;
  grid-template-columns: 1fr 280px;
  gap: 20px;
  align-items: start;
}

.profile-card {
  background: #fff;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
  padding: 24px;
  margin-bottom: 16px;
}

.card-title {
  font-size: 16px;
  font-weight: 700;
  color: #1a1a2e;
  margin: 0 0 18px;
}

.skills-area {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

.skill-tag {
  border-radius: 4px;
}

.form-actions {
  text-align: center;
  padding: 8px 0;
}

.preview-card {
  text-align: center;
  position: sticky;
  top: 80px;
}

.preview-avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: linear-gradient(135deg, #409eff, #2563eb);
  color: #fff;
  font-size: 28px;
  font-weight: 800;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12px;
}

.preview-name {
  font-size: 18px;
  font-weight: 700;
  color: #1a1a2e;
  margin-bottom: 4px;
}

.preview-info {
  font-size: 13px;
  color: #909399;
  margin-bottom: 14px;
}

.preview-tags {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 6px;

  .preview-tag {
    padding: 2px 10px;
    border-radius: 4px;
    font-size: 12px;
    color: #409eff;
    background: #ecf5ff;
    font-weight: 500;
  }
}

@media (max-width: 1024px) {
  .profile-layout { grid-template-columns: 1fr; }
  .preview-card { position: static; }
}
</style>
