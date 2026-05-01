import request from '@/utils/request'

// 查询职位列表
export function listJob(query) {
  return request({
    url: '/crems/job/list',
    method: 'get',
    params: query
  })
}

// 查询职位详情
export function getJob(jobId) {
  return request({
    url: '/crems/job/' + jobId,
    method: 'get'
  })
}

// 新增职位
export function addJob(data) {
  return request({
    url: '/crems/job',
    method: 'post',
    data: data
  })
}

// 修改职位
export function updateJob(data) {
  return request({
    url: '/crems/job',
    method: 'put',
    data: data
  })
}

// 删除职位
export function delJob(jobId) {
  return request({
    url: '/crems/job/' + jobId,
    method: 'delete'
  })
}

// 职位审核
export function auditJob(data) {
  return request({
    url: '/crems/job/audit',
    method: 'put',
    data: data
  })
}

// 导出职位列表
export function exportJob(query) {
  return request({
    url: '/crems/job/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 导入职位数据
export function importJob(data) {
  return request({
    url: '/crems/job/importData',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data
  })
}

// 下载导入模板
export function importJobTemplate() {
  return request({
    url: '/crems/job/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}