import request from '@/utils/request'

// 查询收藏列表
export function listFavorite(query) {
  return request({
    url: '/crems/favorite/list',
    method: 'get',
    params: query
  })
}

// 新增收藏
export function addFavorite(data) {
  return request({
    url: '/crems/favorite',
    method: 'post',
    data: data
  })
}

// 删除收藏
export function delFavorite(favoriteId) {
  return request({
    url: '/crems/favorite/' + favoriteId,
    method: 'delete'
  })
}

// 按职位和学生删除收藏
export function delFavoriteByJobAndStudent(jobId, studentId) {
  return request({
    url: '/crems/favorite/job/' + jobId + '/student/' + studentId,
    method: 'delete'
  })
}