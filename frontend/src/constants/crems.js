/**
 * CREMS 业务常量
 * 集中管理投递状态、面试类型/方式/结果、职位类型等映射，消除前端重复定义
 */

/** 投递状态映射 */
export const APPLICATION_STATUS_MAP = {
  '0': { label: '待查看', type: 'warning' },
  '1': { label: '已查看', type: '' },
  '2': { label: '初筛通过', type: 'success' },
  '3': { label: '面试邀请', type: 'success' },
  '4': { label: '已拒绝', type: 'danger' },
  '5': { label: '已录用', type: 'success' }
}

/** 面试状态映射 */
export const INTERVIEW_STATUS_MAP = {
  '0': { label: '待确认', type: 'warning' },
  '1': { label: '已确认', type: 'success' },
  '2': { label: '已完成', type: '' },
  '3': { label: '已取消', type: 'info' }
}

/** 面试类型映射 */
export const INTERVIEW_TYPE_MAP = {
  first: '初试',
  second: '复试',
  final: '终试'
}

/** 面试方式映射（完整标签） */
export const INTERVIEW_METHOD_MAP = {
  onsite: '现场面试',
  video: '视频面试',
  phone: '电话面试'
}

/** 面试方式映射（短标签） */
export const INTERVIEW_METHOD_SHORT_MAP = {
  onsite: '现场',
  video: '视频',
  phone: '电话'
}

/** 面试结果映射 */
export const INTERVIEW_RESULT_MAP = {
  pass: { label: '通过', type: 'success' },
  fail: { label: '未通过', type: 'danger' },
  pending: { label: '待定', type: '' }
}

/** 职位类型映射 */
export const JOB_TYPE_MAP = {
  full_time: '全职',
  internship: '实习',
  part_time: '兼职'
}

/**
 * 获取状态标签
 * @param {Object} map - 状态映射对象
 * @param {string} status - 状态值
 * @returns {string} 标签文本
 */
export function getStatusLabel(map, status) {
  return map[status]?.label || status
}

/**
 * 获取状态标签类型
 * @param {Object} map - 状态映射对象
 * @param {string} status - 状态值
 * @returns {string} Element Plus tag type
 */
export function getStatusType(map, status) {
  return map[status]?.type || 'info'
}
