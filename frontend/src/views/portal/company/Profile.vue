<template>
  <div class="company-profile-page">
    <h1 class="portal-page-title">企业资料</h1>

    <div class="profile-layout">
      <div class="profile-main">
        <div class="profile-card">
          <h3 class="card-title">显示昵称</h3>
          <el-form label-width="100px">
            <el-form-item label="昵称">
              <el-input v-model="nickname" placeholder="设置您的显示昵称" maxlength="30" />
              <div class="form-tip">此昵称将显示在右上角和对外信息中</div>
            </el-form-item>
          </el-form>
        </div>

        <div class="profile-card">
          <h3 class="card-title">基本信息</h3>
          <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="企业名称" prop="companyName">
                  <el-input v-model="form.companyName" placeholder="请输入" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="统一信用代码" prop="companyCode">
                  <el-input v-model="form.companyCode" placeholder="请输入" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="企业类型">
                  <el-select v-model="form.companyType" placeholder="请选择" style="width: 100%">
                    <el-option label="国有企业" value="state_owned" />
                    <el-option label="民营企业" value="private" />
                    <el-option label="外资企业" value="foreign" />
                    <el-option label="合资企业" value="joint" />
                    <el-option label="上市公司" value="listed" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="所属行业">
                  <el-input v-model="form.industry" placeholder="如：互联网/IT" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="企业规模">
                  <el-select v-model="form.scale" placeholder="请选择" style="width: 100%">
                    <el-option label="0-50人" value="0-50" />
                    <el-option label="50-150人" value="50-150" />
                    <el-option label="150-500人" value="150-500" />
                    <el-option label="500-1000人" value="500-1000" />
                    <el-option label="1000人以上" value="1000+" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="企业网站">
                  <el-input v-model="form.website" placeholder="https://" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="企业地址">
                  <el-input v-model="form.address" placeholder="请输入详细地址" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="企业简介">
                  <el-input v-model="form.description" type="textarea" :rows="5" placeholder="介绍企业的业务、文化和发展历程..." maxlength="2000" show-word-limit />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>

        <div class="profile-card">
          <h3 class="card-title">联系方式</h3>
          <el-form :model="form" label-width="100px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="联系人">
                  <el-input v-model="form.contactPerson" placeholder="请输入" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话">
                  <el-input v-model="form.contactPhone" placeholder="请输入" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系邮箱">
                  <el-input v-model="form.contactEmail" placeholder="请输入" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>

        <div class="form-actions">
          <el-button type="primary" size="large" @click="submitForm" :loading="submitting">保存资料</el-button>
        </div>
      </div>

      <div class="profile-sidebar">
        <div class="profile-card preview-card">
          <div class="preview-logo">{{ (form.companyName || '企').charAt(0) }}</div>
          <div class="preview-name">{{ form.companyName || '企业名称' }}</div>
          <div class="preview-type" v-if="form.companyType">{{ formatType(form.companyType) }}</div>
          <div class="preview-info" v-if="form.industry">{{ form.industry }}</div>
          <div class="preview-info" v-if="form.scale">{{ formatScale(form.scale) }}</div>
          <el-tag v-if="form.status === '1'" type="success" size="small" style="margin-top: 10px">已认证</el-tag>
          <el-tag v-else-if="form.status === '0'" type="warning" size="small" style="margin-top: 10px">待审核</el-tag>
          <el-tag v-else-if="form.status === '2'" type="danger" size="small" style="margin-top: 10px">已拒绝</el-tag>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { getCurrentCompany, addCompany, updateCompany, getNickname, updateNickname } from '@/api/portal'
import useUserStore from '@/store/modules/user'

const { proxy } = getCurrentInstance()
const userStore = useUserStore()

const formRef = ref(null)
const nickname = ref('')
const submitting = ref(false)
const companyId = ref(null)
const form = reactive({
  companyName: '',
  companyCode: '',
  companyType: '',
  industry: '',
  scale: '',
  address: '',
  website: '',
  description: '',
  contactPerson: '',
  contactPhone: '',
  contactEmail: '',
  status: ''
})

const rules = {
  companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  companyCode: [{ required: true, message: '请输入统一信用代码', trigger: 'blur' }]
}

const typeMap = { state_owned: '国有企业', private: '民营企业', foreign: '外资企业', joint: '合资企业', listed: '上市公司' }
const scaleMap = { '0-50': '0-50人', '50-150': '50-150人', '150-500': '150-500人', '500-1000': '500-1000人', '1000+': '1000人以上' }
const formatType = (t) => typeMap[t] || t
const formatScale = (s) => scaleMap[s] || s

async function loadProfile() {
  try {
    const res = await getCurrentCompany()
    if (res.data) {
      const company = res.data
      companyId.value = company.companyId
      Object.keys(form).forEach(key => {
        if (company[key] !== undefined && company[key] !== null) {
          form[key] = company[key]
        }
      })
    }
  } catch (e) {
    // 保持空表单，避免展示其他企业资料。
  }
  // 加载当前昵称 - 从 API 获取最新值
  try {
    const nickRes = await getNickname()
    // 兼容不同的响应格式
    if (typeof nickRes === 'string') {
      nickname.value = nickRes
    } else if (nickRes && nickRes.data) {
      nickname.value = nickRes.data
    } else {
      // 从 store 读取作为兜底
      nickname.value = userStore.nickName || ''
    }
  } catch (e) {
    // 从 store 读取作为兜底
    nickname.value = userStore.nickName || ''
  }
}

async function submitForm() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    if (companyId.value) {
      await updateCompany({ companyId: companyId.value, ...form })
    } else {
      const res = await addCompany({ ...form })
      companyId.value = res.data?.companyId || companyId.value
    }
    // 保存昵称
    if (nickname.value) {
      await updateNickname(nickname.value)
      // 直接更新 store，立即生效
      userStore.nickName = nickname.value
    }
    proxy.$modal.msgSuccess('保存成功')
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

.form-actions {
  text-align: center;
  padding: 8px 0;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.preview-card {
  text-align: center;
  position: sticky;
  top: 80px;
}

.preview-logo {
  width: 72px;
  height: 72px;
  border-radius: 12px;
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

.preview-type {
  font-size: 13px;
  color: #409eff;
  font-weight: 500;
  margin-bottom: 4px;
}

.preview-info {
  font-size: 13px;
  color: #909399;
}

@media (max-width: 1024px) {
  .profile-layout { grid-template-columns: 1fr; }
  .preview-card { position: static; }
}
</style>
