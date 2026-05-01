import request from '@/utils/request'

// 查询面试列表
export function listInterview(query) {
  return request({
    url: '/crems/interview/list',
    method: 'get',
    params: query
  })
}

// 查询面试详情
export function getInterview(interviewId) {
  return request({
    url: '/crems/interview/' + interviewId,
    method: 'get'
  })
}

// 新增面试
export function addInterview(data) {
  return request({
    url: '/crems/interview',
    method: 'post',
    data: data
  })
}

// 修改面试
export function updateInterview(data) {
  return request({
    url: '/crems/interview',
    method: 'put',
    data: data
  })
}

// 删除面试
export function delInterview(interviewId) {
  return request({
    url: '/crems/interview/' + interviewId,
    method: 'delete'
  })
}

// 导出面试列表
export function exportInterview(query) {
  return request({
    url: '/crems/interview/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}