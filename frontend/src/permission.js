import router from './router'
import { ElMessage } from 'element-plus'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isHttp, isPathMatch } from '@/utils/validate'
import { isRelogin } from '@/utils/request'
import useUserStore from '@/store/modules/user'
import useLockStore from '@/store/modules/lock'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/register', '/portal', '/portal/home']

const isWhiteList = (path) => {
  return whiteList.some(pattern => isPathMatch(pattern, path))
}

router.beforeEach(async (to, from) => {
  NProgress.start()
  if (getToken()) {
    to.meta.title && useSettingsStore().setTitle(to.meta.title)
    const isLock = useLockStore().isLock
    if (to.path === '/login') {
      NProgress.done()
      return { path: '/' }
    }
    if (isWhiteList(to.path)) {
      return true
    }
    if (isLock && to.path !== '/lock') {
      NProgress.done()
      return { path: '/lock' }
    }
    if (!isLock && to.path === '/lock') {
      NProgress.done()
      return { path: '/' }
    }
    if (useUserStore().roles.length === 0) {
      isRelogin.show = true
      try {
        // 拉取user_info信息
        await useUserStore().getInfo()
        // 从数据库刷新昵称（解决 Redis 缓存不同步问题）
        await useUserStore().refreshNickname()
        isRelogin.show = false
        const roles = useUserStore().roles
        // 根据角色重定向：学生和企业用户进入门户，管理员进入后台
        if (to.path === '/' || to.path === '/index') {
          if (roles.includes('student') || roles.includes('ROLE_DEFAULT')) {
            return { path: '/portal/student/dashboard', replace: true }
          } else if (roles.includes('company')) {
            return { path: '/portal/company/dashboard', replace: true }
          }
        }
        // 角色路由隔离：学生不能进企业页面，企业不能进学生页面
        if (to.path.startsWith('/portal/student/') && roles.includes('company') && !roles.includes('admin')) {
          return { path: '/portal/company/dashboard', replace: true }
        }
        if (to.path.startsWith('/portal/company/') && (roles.includes('student') || roles.includes('ROLE_DEFAULT')) && !roles.includes('admin')) {
          return { path: '/portal/student/dashboard', replace: true }
        }
        // 门户路由跳过动态路由生成
        if (to.meta && to.meta.portal) {
          return { ...to, replace: true }
        }
        // 根据roles权限生成可访问的路由
        const accessRoutes = await usePermissionStore().generateRoutes()
        accessRoutes.forEach(route => {
          if (!isHttp(route.path)) {
            router.addRoute(route)
          }
        })
        // 重新导航到目标路由，确保动态路由已注册
        return { ...to, replace: true }
      } catch (err) {
        await useUserStore().logOut()
        ElMessage.error(err)
        return { path: '/' }
      }
    }
    // 角色路由隔离（roles已加载的情况）
    const roles = useUserStore().roles
    if (to.path.startsWith('/portal/student/') && roles.includes('company') && !roles.includes('admin')) {
      return { path: '/portal/company/dashboard', replace: true }
    }
    if (to.path.startsWith('/portal/company/') && (roles.includes('student') || roles.includes('ROLE_DEFAULT')) && !roles.includes('admin')) {
      return { path: '/portal/student/dashboard', replace: true }
    }
    return true
  } else {
    // 没有token
    if (isWhiteList(to.path)) {
      // 在免登录白名单，直接进入
      return true
    }
    NProgress.done()
    return `/login?redirect=${to.fullPath}` // 否则全部重定向到登录页
  }
})

router.afterEach(() => {
  NProgress.done()
})
