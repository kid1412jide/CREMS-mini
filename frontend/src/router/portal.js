import PortalLayout from '@/layout/portal'

export const portalRoutes = [
  {
    path: '/portal',
    component: PortalLayout,
    redirect: '/portal/home',
    hidden: true,
    children: [
      {
        path: 'home',
        component: () => import('@/views/portal/Home.vue'),
        name: 'PortalHome',
        meta: { title: '首页', portal: true }
      },
      // ========== 学生端 ==========
      {
        path: 'student',
        redirect: '/portal/student/dashboard',
        meta: { portal: true, roles: ['student'] },
        children: [
          {
            path: 'dashboard',
            component: () => import('@/views/portal/student/Dashboard.vue'),
            name: 'PortalStudentDashboard',
            meta: { title: '工作台', portal: true, roles: ['student'] }
          },
          {
            path: 'jobs',
            component: () => import('@/views/portal/student/JobList.vue'),
            name: 'PortalStudentJobList',
            meta: { title: '找职位', portal: true, roles: ['student'] }
          },
          {
            path: 'job/:jobId',
            component: () => import('@/views/portal/student/JobDetail.vue'),
            name: 'PortalStudentJobDetail',
            meta: { title: '职位详情', portal: true, roles: ['student'] }
          },
          {
            path: 'applications',
            component: () => import('@/views/portal/student/Applications.vue'),
            name: 'PortalStudentApplications',
            meta: { title: '我的投递', portal: true, roles: ['student'] }
          },
          {
            path: 'interviews',
            component: () => import('@/views/portal/student/Interviews.vue'),
            name: 'PortalStudentInterviews',
            meta: { title: '我的面试', portal: true, roles: ['student'] }
          },
          {
            path: 'favorites',
            component: () => import('@/views/portal/student/Favorites.vue'),
            name: 'PortalStudentFavorites',
            meta: { title: '我的收藏', portal: true, roles: ['student'] }
          },
          {
            path: 'profile',
            component: () => import('@/views/portal/student/Profile.vue'),
            name: 'PortalStudentProfile',
            meta: { title: '个人简历', portal: true, roles: ['student'] }
          }
        ]
      },
      // ========== 企业端 ==========
      {
        path: 'company',
        redirect: '/portal/company/dashboard',
        meta: { portal: true, roles: ['company'] },
        children: [
          {
            path: 'dashboard',
            component: () => import('@/views/portal/company/Dashboard.vue'),
            name: 'PortalCompanyDashboard',
            meta: { title: '工作台', portal: true, roles: ['company'] }
          },
          {
            path: 'jobs',
            component: () => import('@/views/portal/company/JobManage.vue'),
            name: 'PortalCompanyJobManage',
            meta: { title: '职位管理', portal: true, roles: ['company'] }
          },
          {
            path: 'applications',
            component: () => import('@/views/portal/company/ApplicationReview.vue'),
            name: 'PortalCompanyApplications',
            meta: { title: '简历处理', portal: true, roles: ['company'] }
          },
          {
            path: 'interviews',
            component: () => import('@/views/portal/company/InterviewManage.vue'),
            name: 'PortalCompanyInterviews',
            meta: { title: '面试管理', portal: true, roles: ['company'] }
          },
          {
            path: 'profile',
            component: () => import('@/views/portal/company/Profile.vue'),
            name: 'PortalCompanyProfile',
            meta: { title: '企业资料', portal: true, roles: ['company'] }
          }
        ]
      }
    ]
  }
]
