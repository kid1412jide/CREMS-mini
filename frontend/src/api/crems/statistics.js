import request from '@/utils/request'

// 获取数据概览
export function getStatisticsOverview() {
  return request({
    url: '/crems/statistics/overview',
    method: 'get'
  })
}

// 按职位类型统计
export function getJobStatisticsByType() {
  return request({
    url: '/crems/statistics/job/type',
    method: 'get'
  })
}

// 按城市统计职位
export function getJobStatisticsByCity() {
  return request({
    url: '/crems/statistics/job/city',
    method: 'get'
  })
}

// 按行业统计职位
export function getJobStatisticsByIndustry() {
  return request({
    url: '/crems/statistics/job/industry',
    method: 'get'
  })
}

// 按投递状态统计
export function getApplicationStatisticsByStatus() {
  return request({
    url: '/crems/statistics/application/status',
    method: 'get'
  })
}

// 按面试类型统计
export function getInterviewStatisticsByType() {
  return request({
    url: '/crems/statistics/interview/type',
    method: 'get'
  })
}

// 按面试结果统计
export function getInterviewStatisticsByResult() {
  return request({
    url: '/crems/statistics/interview/result',
    method: 'get'
  })
}