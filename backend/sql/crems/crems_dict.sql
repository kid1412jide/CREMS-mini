-- ----------------------------
-- 校园招聘系统 - 字典数据
-- ----------------------------

-- 字典类型
insert into sys_dict_type values(100, '企业类型',   'crems_company_type',  '0', 'admin', sysdate(), '', null, '企业类型列表');
insert into sys_dict_type values(101, '企业规模',   'crems_company_scale', '0', 'admin', sysdate(), '', null, '企业规模列表');
insert into sys_dict_type values(102, '企业状态',   'crems_company_status','0', 'admin', sysdate(), '', null, '企业审核状态');
insert into sys_dict_type values(103, '学历类型',   'crems_education',     '0', 'admin', sysdate(), '', null, '学历类型列表');
insert into sys_dict_type values(104, '职位类型',   'crems_job_type',      '0', 'admin', sysdate(), '', null, '职位类型列表');
insert into sys_dict_type values(105, '职位状态',   'crems_job_status',    '0', 'admin', sysdate(), '', null, '职位状态列表');
insert into sys_dict_type values(106, '投递状态',   'crems_app_status',    '0', 'admin', sysdate(), '', null, '投递状态列表');
insert into sys_dict_type values(107, '面试类型',   'crems_interview_type','0', 'admin', sysdate(), '', null, '面试类型列表');
insert into sys_dict_type values(108, '面试方式',   'crems_interview_method','0','admin', sysdate(), '', null, '面试方式列表');
insert into sys_dict_type values(109, '面试结果',   'crems_interview_result','0','admin', sysdate(), '', null, '面试结果列表');
insert into sys_dict_type values(110, '面试状态',   'crems_interview_status','0','admin', sysdate(), '', null, '面试状态列表');

-- 企业类型
insert into sys_dict_data values(1000, 1, '国有企业', 'state_owned', 'crems_company_type', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1001, 2, '民营企业', 'private',     'crems_company_type', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1002, 3, '外资企业', 'foreign',     'crems_company_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1003, 4, '合资企业', 'joint',       'crems_company_type', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1004, 5, '上市公司', 'listed',      'crems_company_type', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');

-- 企业规模
insert into sys_dict_data values(1010, 1, '50人以下',    '0-50',      'crems_company_scale', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1011, 2, '50-150人',    '50-150',    'crems_company_scale', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1012, 3, '150-500人',   '150-500',   'crems_company_scale', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1013, 4, '500-1000人',  '500-1000',  'crems_company_scale', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1014, 5, '1000人以上',  '1000+',     'crems_company_scale', '', '', 'N', '0', 'admin', sysdate(), '', null, '');

-- 企业状态
insert into sys_dict_data values(1020, 1, '待审核', '0', 'crems_company_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1021, 2, '已认证', '1', 'crems_company_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1022, 3, '已拒绝', '2', 'crems_company_status', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');

-- 学历类型
insert into sys_dict_data values(1030, 1, '专科', 'junior_college', 'crems_education', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1031, 2, '本科', 'bachelor',       'crems_education', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1032, 3, '硕士', 'master',         'crems_education', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1033, 4, '博士', 'doctor',         'crems_education', '', '', 'N', '0', 'admin', sysdate(), '', null, '');

-- 职位类型
insert into sys_dict_data values(1040, 1, '全职', 'full_time',  'crems_job_type', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1041, 2, '实习', 'internship', 'crems_job_type', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1042, 3, '兼职', 'part_time',  'crems_job_type', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');

-- 职位状态
insert into sys_dict_data values(1050, 1, '待审核', '0', 'crems_job_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1051, 2, '已发布', '1', 'crems_job_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1052, 3, '已下架', '2', 'crems_job_status', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');

-- 投递状态
insert into sys_dict_data values(1060, 1, '待查看',   '0', 'crems_app_status', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1061, 2, '已查看',   '1', 'crems_app_status', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1062, 3, '初筛通过', '2', 'crems_app_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1063, 4, '面试邀请', '3', 'crems_app_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1064, 5, '已拒绝',   '4', 'crems_app_status', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1065, 6, '已录用',   '5', 'crems_app_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');

-- 面试类型
insert into sys_dict_data values(1070, 1, '初试', 'first',  'crems_interview_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1071, 2, '复试', 'second', 'crems_interview_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1072, 3, '终试', 'final',  'crems_interview_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '');

-- 面试方式
insert into sys_dict_data values(1080, 1, '现场面试', 'onsite', 'crems_interview_method', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1081, 2, '视频面试', 'video',  'crems_interview_method', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1082, 3, '电话面试', 'phone',  'crems_interview_method', '', '', 'N', '0', 'admin', sysdate(), '', null, '');

-- 面试结果
insert into sys_dict_data values(1090, 1, '通过',   'pass',    'crems_interview_result', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1091, 2, '未通过', 'fail',    'crems_interview_result', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1092, 3, '待定',   'pending', 'crems_interview_result', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');

-- 面试状态
insert into sys_dict_data values(1100, 1, '待确认', '0', 'crems_interview_status', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1101, 2, '已确认', '1', 'crems_interview_status', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1102, 3, '已完成', '2', 'crems_interview_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1103, 4, '已取消', '3', 'crems_interview_status', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');
