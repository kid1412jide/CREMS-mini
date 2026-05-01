import request from '@/utils/request'

// 查询学生列表
export function listStudent(query) {
  return request({
    url: '/crems/student/list',
    method: 'get',
    params: query
  })
}

// 查询学生详情
export function getStudent(studentId) {
  return request({
    url: '/crems/student/' + studentId,
    method: 'get'
  })
}

// 新增学生
export function addStudent(data) {
  return request({
    url: '/crems/student',
    method: 'post',
    data: data
  })
}

// 修改学生
export function updateStudent(data) {
  return request({
    url: '/crems/student',
    method: 'put',
    data: data
  })
}

// 删除学生
export function delStudent(studentId) {
  return request({
    url: '/crems/student/' + studentId,
    method: 'delete'
  })
}

// 导出学生列表
export function exportStudent(query) {
  return request({
    url: '/crems/student/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 导入学生数据
export function importStudent(data) {
  return request({
    url: '/crems/student/importData',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data
  })
}

// 下载导入模板
export function importStudentTemplate() {
  return request({
    url: '/crems/student/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}