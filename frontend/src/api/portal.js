import request from '@/utils/request'

// 门户端所有业务接口统一从这里出口，页面只关心函数语义，不直接拼后端路径。

// ==================== 职位 ====================

export function listJob(query) {
  return request({
    url: '/portal/api/job/list',
    method: 'get',
    params: query
  })
}

export function getJob(jobId) {
  return request({
    url: '/portal/api/job/' + jobId,
    method: 'get'
  })
}

export function addJob(data) {
  return request({
    url: '/portal/api/job',
    method: 'post',
    data: data
  })
}

export function updateJob(data) {
  return request({
    url: '/portal/api/job',
    method: 'put',
    data: data
  })
}

export function delJob(jobId) {
  return request({
    url: '/portal/api/job/' + jobId,
    method: 'delete'
  })
}

// ==================== 投递 ====================

export function listApplication(query) {
  return request({
    url: '/portal/api/application/list',
    method: 'get',
    params: query
  })
}

export function addApplication(data) {
  return request({
    url: '/portal/api/application',
    method: 'post',
    data: data
  })
}

export function updateApplication(data) {
  return request({
    url: '/portal/api/application',
    method: 'put',
    data: data
  })
}

// ==================== 面试 ====================

export function listInterview(query) {
  return request({
    url: '/portal/api/interview/list',
    method: 'get',
    params: query
  })
}

export function addInterview(data) {
  // 邀请面试只传表单和 applicationId，职位/学生/企业归属由后端事务内反查。
  return request({
    url: '/portal/api/interview',
    method: 'post',
    data: data
  })
}

export function updateInterview(data) {
  return request({
    url: '/portal/api/interview',
    method: 'put',
    data: data
  })
}

// ==================== 收藏 ====================

export function listFavorite(query) {
  return request({
    url: '/portal/api/favorite/list',
    method: 'get',
    params: query
  })
}

export function addFavorite(data) {
  return request({
    url: '/portal/api/favorite',
    method: 'post',
    data: data
  })
}

export function delFavorite(favoriteId) {
  return request({
    url: '/portal/api/favorite/' + favoriteId,
    method: 'delete'
  })
}

export function delFavoriteByJobAndStudent(jobId) {
  return request({
    url: '/portal/api/favorite/job/' + jobId,
    method: 'delete'
  })
}

// ==================== 学生 ====================

export function listStudent(query) {
  return request({
    url: '/portal/api/student/list',
    method: 'get',
    params: query
  })
}

export function getStudent(studentId) {
  return request({
    url: '/portal/api/student/' + studentId,
    method: 'get'
  })
}

export function getCurrentStudent() {
  return request({
    url: '/portal/api/student/current',
    method: 'get'
  })
}

export function addStudent(data) {
  return request({
    url: '/portal/api/student',
    method: 'post',
    data: data
  })
}

export function updateStudent(data) {
  return request({
    url: '/portal/api/student',
    method: 'put',
    data: data
  })
}

// ==================== 企业 ====================

export function listCompany(query) {
  return request({
    url: '/portal/api/company/list',
    method: 'get',
    params: query
  })
}

export function getCompany(companyId) {
  return request({
    url: '/portal/api/company/' + companyId,
    method: 'get'
  })
}

export function getCurrentCompany() {
  return request({
    url: '/portal/api/company/current',
    method: 'get'
  })
}

export function addCompany(data) {
  return request({
    url: '/portal/api/company',
    method: 'post',
    data: data
  })
}

export function updateCompany(data) {
  return request({
    url: '/portal/api/company',
    method: 'put',
    data: data
  })
}

// ==================== 用户设置 ====================

export function getNickname() {
  // 昵称属于轻量设置接口，关闭重复提交拦截避免连续刷新时被误判。
  return request({
    url: '/portal/api/user/nickname',
    method: 'get',
    headers: { repeatSubmit: false }
  })
}

export function updateNickname(nickname) {
  // 后端按登录用户更新昵称，前端不传 userId。
  return request({
    url: '/portal/api/user/nickname',
    method: 'put',
    data: { nickname },
    headers: { repeatSubmit: false }
  })
}

// ==================== 统计 ====================

export function getStatisticsOverview() {
  return request({
    url: '/portal/api/statistics/overview',
    method: 'get'
  })
}
