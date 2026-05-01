-- ----------------------------
-- 校园招聘系统 - 菜单数据
-- ----------------------------

-- 一级菜单：招聘管理
insert into sys_menu values('2000', '招聘管理', '0', '4', 'crems', null, '', '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', sysdate(), '', null, '招聘管理目录');

-- 二级菜单：企业管理
insert into sys_menu values('2001', '企业管理', '2000', '1', 'company', 'crems/company/index', '', '', 1, 0, 'C', '0', '0', 'crems:company:list', 'international', 'admin', sysdate(), '', null, '企业管理菜单');
-- 企业管理按钮
insert into sys_menu values('2010', '企业查询', '2001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:company:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2011', '企业新增', '2001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:company:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2012', '企业修改', '2001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:company:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2013', '企业删除', '2001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:company:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2014', '企业导出', '2001', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:company:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2015', '企业审核', '2001', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:company:audit', '#', 'admin', sysdate(), '', null, '');

-- 二级菜单：学生管理
insert into sys_menu values('2002', '学生管理', '2000', '2', 'student', 'crems/student/index', '', '', 1, 0, 'C', '0', '0', 'crems:student:list', 'user', 'admin', sysdate(), '', null, '学生管理菜单');
-- 学生管理按钮
insert into sys_menu values('2020', '学生查询', '2002', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:student:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2021', '学生新增', '2002', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:student:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2022', '学生修改', '2002', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:student:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2023', '学生删除', '2002', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:student:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2024', '学生导出', '2002', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:student:export', '#', 'admin', sysdate(), '', null, '');

-- 二级菜单：职位管理
insert into sys_menu values('2003', '职位管理', '2000', '3', 'job', 'crems/company/job', '', '', 1, 0, 'C', '0', '0', 'crems:job:list', 'documentation', 'admin', sysdate(), '', null, '职位管理菜单');
-- 职位管理按钮
insert into sys_menu values('2030', '职位查询', '2003', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:job:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2031', '职位新增', '2003', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:job:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2032', '职位修改', '2003', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:job:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2033', '职位删除', '2003', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:job:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2034', '职位导出', '2003', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:job:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2035', '职位审核', '2003', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:job:audit', '#', 'admin', sysdate(), '', null, '');

-- 二级菜单：投递管理
insert into sys_menu values('2004', '投递管理', '2000', '4', 'application', 'crems/company/application', '', '', 1, 0, 'C', '0', '0', 'crems:application:list', 'email', 'admin', sysdate(), '', null, '投递管理菜单');
-- 投递管理按钮
insert into sys_menu values('2040', '投递查询', '2004', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:application:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2041', '投递修改', '2004', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:application:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2042', '投递删除', '2004', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:application:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2043', '投递导出', '2004', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:application:export', '#', 'admin', sysdate(), '', null, '');

-- 二级菜单：面试管理
insert into sys_menu values('2005', '面试管理', '2000', '5', 'interview', 'crems/company/interview', '', '', 1, 0, 'C', '0', '0', 'crems:interview:list', 'date', 'admin', sysdate(), '', null, '面试管理菜单');
-- 面试管理按钮
insert into sys_menu values('2050', '面试查询', '2005', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:interview:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2051', '面试新增', '2005', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:interview:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2052', '面试修改', '2005', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:interview:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2053', '面试删除', '2005', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:interview:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2054', '面试导出', '2005', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'crems:interview:export', '#', 'admin', sysdate(), '', null, '');
