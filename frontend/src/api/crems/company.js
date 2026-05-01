import request from '@/utils/request'

// 查询企业列表
export function listCompany(query) {
  return request({
    url: '/crems/company/list',
    method: 'get',
    params: query
  })
}

// 查询企业详情
export function getCompany(companyId) {
  return request({
    url: '/crems/company/' + companyId,
    method: 'get'
  })
}

// 新增企业
export function addCompany(data) {
  return request({
    url: '/crems/company',
    method: 'post',
    data: data
  })
}

// 修改企业
export function updateCompany(data) {
  return request({
    url: '/crems/company',
    method: 'put',
    data: data
  })
}

// 删除企业
export function delCompany(companyId) {
  return request({
    url: '/crems/company/' + companyId,
    method: 'delete'
  })
}

// 企业审核
export function auditCompany(data) {
  return request({
    url: '/crems/company/audit',
    method: 'put',
    data: data
  })
}

// 导出企业列表
export function exportCompany(query) {
  return request({
    url: '/crems/company/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 导入企业数据
export function importCompany(data) {
  return request({
    url: '/crems/company/importData',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data
  })
}

// 下载导入模板
export function importTemplate() {
  return request({
    url: '/crems/company/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}