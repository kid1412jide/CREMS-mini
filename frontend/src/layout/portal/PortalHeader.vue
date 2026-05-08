<template>
  <header class="portal-header">
    <div class="portal-header__inner">
      <!-- Logo -->
      <router-link to="/portal/home" class="portal-header__logo">
        <div class="logo-mark">C</div>
        <span class="logo-text">CREMS</span>
      </router-link>

      <!-- Navigation -->
      <nav class="portal-header__nav" v-if="!isMobile">
        <router-link
          v-for="item in visibleMenus"
          :key="item.path"
          :to="item.path"
          class="nav-link"
          :class="{ active: isActive(item.path) }"
        >
          {{ item.label }}
        </router-link>
      </nav>

      <!-- Right actions -->
      <div class="portal-header__actions">
        <template v-if="isLoggedIn">
          <el-dropdown trigger="click" @command="handleCommand">
            <div class="user-trigger">
              <el-avatar :size="32" :src="userStore.avatar" class="user-avatar">
                {{ userStore.nickName?.charAt(0) }}
              </el-avatar>
              <span class="user-name" v-if="!isMobile">{{ userStore.nickName }}</span>
              <el-icon class="user-arrow"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>个人中心
                </el-dropdown-item>
                <el-dropdown-item command="admin" v-if="hasAdminRole" divided>
                  <el-icon><Setting /></el-icon>管理后台
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <router-link to="/login" class="btn-login">登录</router-link>
          <router-link to="/register" class="btn-register">注册</router-link>
        </template>
      </div>

      <!-- Mobile menu toggle -->
      <button class="mobile-toggle" v-if="isMobile" @click="mobileMenuOpen = !mobileMenuOpen">
        <el-icon :size="24"><Menu /></el-icon>
      </button>
    </div>

    <!-- Mobile menu -->
    <transition name="slide-down">
      <div class="mobile-menu" v-if="isMobile && mobileMenuOpen">
        <router-link
          v-for="item in visibleMenus"
          :key="item.path"
          :to="item.path"
          class="mobile-menu__item"
          :class="{ active: isActive(item.path) }"
          @click="mobileMenuOpen = false"
        >
          {{ item.label }}
        </router-link>
      </div>
    </transition>
  </header>
</template>

<script setup>
import { ArrowDown, User, Setting, SwitchButton, Menu } from '@element-plus/icons-vue'
import useUserStore from '@/store/modules/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const mobileMenuOpen = ref(false)
const windowWidth = ref(window.innerWidth)
const isMobile = computed(() => windowWidth.value < 768)

const isLoggedIn = computed(() => !!userStore.token)
const hasAdminRole = computed(() => userStore.roles.includes('admin'))
const isStudent = computed(() => userStore.roles.includes('student') || userStore.roles.includes('ROLE_DEFAULT'))
const isCompany = computed(() => userStore.roles.includes('company'))

const studentMenus = [
  { label: '找职位', path: '/portal/student/jobs' },
  { label: '工作台', path: '/portal/student/dashboard' },
  { label: '我的投递', path: '/portal/student/applications' },
  { label: '我的面试', path: '/portal/student/interviews' },
  { label: '我的收藏', path: '/portal/student/favorites' }
]

const companyMenus = [
  { label: '工作台', path: '/portal/company/dashboard' },
  { label: '职位管理', path: '/portal/company/jobs' },
  { label: '简历处理', path: '/portal/company/applications' },
  { label: '面试管理', path: '/portal/company/interviews' }
]

const guestMenus = [
  { label: '首页', path: '/portal/home' },
  { label: '找职位', path: '/portal/student/jobs' }
]

const visibleMenus = computed(() => {
  if (!isLoggedIn.value) return guestMenus
  if (isCompany.value) return companyMenus
  return studentMenus
})

function isActive(path) {
  return route.path.startsWith(path)
}

function handleCommand(cmd) {
  if (cmd === 'profile') {
    const target = isCompany.value ? '/portal/company/profile' : '/portal/student/profile'
    router.push(target)
  } else if (cmd === 'admin') {
    router.push('/index')
  } else if (cmd === 'logout') {
    userStore.logOut().then(() => {
      router.push('/portal/home')
    })
  }
}

onMounted(() => {
  const handler = () => { windowWidth.value = window.innerWidth }
  window.addEventListener('resize', handler)
  onUnmounted(() => window.removeEventListener('resize', handler))
})
</script>

<style lang="scss" scoped>
.portal-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.88);
  backdrop-filter: blur(12px) saturate(180%);
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);

  &__inner {
    display: flex;
    align-items: center;
    justify-content: space-between;
    max-width: 1280px;
    margin: 0 auto;
    padding: 0 24px;
    height: 60px;
  }

  &__logo {
    display: flex;
    align-items: center;
    gap: 10px;
    text-decoration: none;
    flex-shrink: 0;

    .logo-mark {
      width: 34px;
      height: 34px;
      border-radius: 8px;
      background: linear-gradient(135deg, #409eff, #2563eb);
      color: #fff;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 700;
      font-size: 18px;
      letter-spacing: -0.5px;
    }

    .logo-text {
      font-size: 20px;
      font-weight: 700;
      color: #1a1a2e;
      letter-spacing: -0.5px;
    }
  }

  &__nav {
    display: flex;
    align-items: center;
    gap: 4px;

    .nav-link {
      padding: 6px 16px;
      border-radius: 6px;
      font-size: 14px;
      font-weight: 500;
      color: #606266;
      text-decoration: none;
      transition: all 0.2s;

      &:hover {
        color: #409eff;
        background: rgba(64, 158, 255, 0.06);
      }

      &.active {
        color: #409eff;
        background: rgba(64, 158, 255, 0.1);
        font-weight: 600;
      }
    }
  }

  &__actions {
    display: flex;
    align-items: center;
    gap: 12px;
    flex-shrink: 0;
  }
}

.user-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background 0.2s;

  &:hover {
    background: #f5f7fa;
  }

  .user-name {
    font-size: 14px;
    font-weight: 500;
    color: #303133;
    max-width: 100px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .user-arrow {
    font-size: 12px;
    color: #909399;
  }
}

.user-avatar {
  background: linear-gradient(135deg, #409eff, #2563eb);
  color: #fff;
  font-weight: 600;
  font-size: 14px;
}

.btn-login {
  padding: 7px 20px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  color: #409eff;
  text-decoration: none;
  transition: all 0.2s;

  &:hover {
    background: rgba(64, 158, 255, 0.06);
  }
}

.btn-register {
  padding: 7px 20px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 600;
  color: #fff;
  background: #409eff;
  text-decoration: none;
  transition: all 0.2s;

  &:hover {
    background: #337ecc;
  }
}

.mobile-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border: none;
  background: none;
  cursor: pointer;
  color: #303133;
  border-radius: 8px;

  &:hover {
    background: #f5f7fa;
  }
}

.mobile-menu {
  padding: 8px 24px 16px;
  border-top: 1px solid #f0f0f0;

  &__item {
    display: block;
    padding: 10px 16px;
    border-radius: 8px;
    font-size: 15px;
    font-weight: 500;
    color: #606266;
    text-decoration: none;
    transition: all 0.2s;

    &:hover, &.active {
      color: #409eff;
      background: rgba(64, 158, 255, 0.06);
    }
  }
}

.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.25s ease;
}

.slide-down-enter-from,
.slide-down-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}

@media (max-width: 768px) {
  .portal-header__inner {
    padding: 0 16px;
  }
}
</style>
