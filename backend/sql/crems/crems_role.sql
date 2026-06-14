-- ----------------------------
-- 校园招聘系统 - 学生/企业角色
-- ----------------------------

-- 新增学生角色
insert into sys_role values('3', '学生角色', 'student', 3, 2, 1, 1, '0', '0', 'admin', sysdate(), '', null, '学生角色');

-- 新增企业角色：数据范围为“仅本人”，用于企业端数据隔离
insert into sys_role values('4', '企业角色', 'company', 4, 5, 1, 1, '0', '0', 'admin', sysdate(), '', null, '企业角色');
