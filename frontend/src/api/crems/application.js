import request from '@/utils/request'

// 查询投递列表
export function listApplication(query) {
  return request({
    url: '/crems/application/list',
    method: 'get',
    params: query
  })
}

// 查询投递详情
export function getApplication(applicationId) {
  return request({
    url: '/crems/application/' + applicationId,
    method: 'get'
  })
}

// 新增投递
export function addApplication(data) {
  return request({
    url: '/crems/application',
    method: 'post',
    data: data
  })
}

// 修改投递状态
export function updateApplication(data) {
  return request({
    url: '/crems/application',
    method: 'put',
    data: data
  })
}

// 删除投递
export function delApplication(applicationId) {
  return request({
    url: '/crems/application/' + applicationId,
    method: 'delete'
  })
}

// 导出投递列表
export function exportApplication(query) {
  return request({
    url: '/crems/application/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}