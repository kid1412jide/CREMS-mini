-- ----------------------------
-- 校园招聘与就业信息管理系统 - 数据库表结构
-- ----------------------------

-- ----------------------------
-- 1. 企业信息表
-- ----------------------------
drop table if exists crems_company;
create table crems_company (
  company_id        bigint(20)      not null auto_increment    comment '企业ID',
  user_id           bigint(20)      default null               comment '关联用户ID',
  company_name      varchar(100)    not null                   comment '企业名称',
  company_code      varchar(50)     not null                   comment '统一社会信用代码',
  company_type      varchar(20)     default null               comment '企业类型',
  industry          varchar(50)     default null               comment '所属行业',
  scale             varchar(20)     default null               comment '企业规模',
  address           varchar(200)    default null               comment '企业地址',
  website           varchar(200)    default null               comment '企业网站',
  description       text            default null               comment '企业简介',
  logo_url          varchar(200)    default null               comment '企业Logo',
  contact_person    varchar(50)     default null               comment '联系人',
  contact_phone     varchar(20)     default null               comment '联系电话',
  contact_email     varchar(100)    default null               comment '联系邮箱',
  status            char(1)         not null default '0'       comment '状态（0待审核 1已认证 2已拒绝）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (company_id)
) engine=innodb auto_increment=100 comment = '企业信息表';

create unique index uk_company_code on crems_company(company_code);
create index idx_company_name on crems_company(company_name);
create index idx_company_status on crems_company(status);

-- ----------------------------
-- 2. 学生信息表
-- ----------------------------
drop table if exists crems_student;
create table crems_student (
  student_id        bigint(20)      not null auto_increment    comment '学生ID',
  user_id           bigint(20)      default null               comment '关联用户ID',
  student_no        varchar(50)     not null                   comment '学号',
  student_name      varchar(50)     not null                   comment '姓名',
  gender            char(1)         default null               comment '性别（0男 1女）',
  birth_date        date            default null               comment '出生日期',
  phone             varchar(20)     default null               comment '手机号',
  email             varchar(100)    default null               comment '邮箱',
  id_card           varchar(18)     default null               comment '身份证号',
  school            varchar(100)    default null               comment '学校',
  major             varchar(100)    default null               comment '专业',
  education         varchar(20)     default null               comment '学历',
  grade             varchar(20)     default null               comment '年级',
  graduation_date   date            default null               comment '预计毕业时间',
  avatar_url        varchar(200)    default null               comment '头像',
  resume_url        varchar(200)    default null               comment '简历附件',
  self_introduction text            default null               comment '自我介绍',
  skills            varchar(500)    default null               comment '技能标签',
  status            char(1)         not null default '0'       comment '状态（0正常 1停用）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (student_id)
) engine=innodb auto_increment=100 comment = '学生信息表';

create unique index uk_student_no on crems_student(student_no);
create index idx_student_name on crems_student(student_name);
create index idx_student_major on crems_student(major);

-- ----------------------------
-- 3. 职位信息表
-- ----------------------------
drop table if exists crems_job;
create table crems_job (
  job_id            bigint(20)      not null auto_increment    comment '职位ID',
  company_id        bigint(20)      not null                   comment '企业ID',
  job_title         varchar(100)    not null                   comment '职位名称',
  job_type          varchar(20)     default null               comment '职位类型（全职/实习/兼职）',
  category          varchar(50)     default null               comment '职位分类',
  department        varchar(100)    default null               comment '所属部门',
  recruit_num       int(11)         default 1                  comment '招聘人数',
  work_city         varchar(100)    default null               comment '工作城市',
  work_address      varchar(200)    default null               comment '工作地址',
  salary_min        decimal(10,2)   default null               comment '最低薪资',
  salary_max        decimal(10,2)   default null               comment '最高薪资',
  education_required varchar(20)    default null               comment '学历要求',
  experience_required varchar(50)   default null               comment '经验要求',
  job_description   text            default null               comment '职位描述',
  job_requirements  text            default null               comment '任职要求',
  welfare           varchar(500)    default null               comment '福利待遇',
  publish_date      datetime        default null               comment '发布时间',
  deadline          datetime        default null               comment '截止时间',
  view_count        int(11)         default 0                  comment '浏览次数',
  apply_count       int(11)         default 0                  comment '投递次数',
  status            char(1)         not null default '0'       comment '状态（0待审核 1已发布 2已下架）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (job_id)
) engine=innodb auto_increment=100 comment = '职位信息表';

create index idx_job_company on crems_job(company_id);
create index idx_job_title on crems_job(job_title);
create index idx_job_city on crems_job(work_city);
create index idx_job_status on crems_job(status);
create index idx_job_publish on crems_job(publish_date);

-- ----------------------------
-- 4. 简历投递表
-- ----------------------------
drop table if exists crems_application;
create table crems_application (
  application_id    bigint(20)      not null auto_increment    comment '投递ID',
  job_id            bigint(20)      not null                   comment '职位ID',
  student_id        bigint(20)      not null                   comment '学生ID',
  company_id        bigint(20)      not null                   comment '企业ID',
  resume_url        varchar(200)    default null               comment '简历附件',
  cover_letter      text            default null               comment '求职信',
  apply_time        datetime        default null               comment '投递时间',
  status            char(1)         not null default '0'       comment '状态（0待查看 1已查看 2初筛通过 3面试邀请 4已拒绝 5已录用）',
  view_time         datetime        default null               comment '查看时间',
  feedback          text            default null               comment '企业反馈',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (application_id)
) engine=innodb auto_increment=100 comment = '简历投递表';

create unique index uk_app_job_student on crems_application(job_id, student_id);
create index idx_app_job on crems_application(job_id);
create index idx_app_student on crems_application(student_id);
create index idx_app_company on crems_application(company_id);
create index idx_app_status on crems_application(status);

-- ----------------------------
-- 5. 面试安排表
-- ----------------------------
drop table if exists crems_interview;
create table crems_interview (
  interview_id      bigint(20)      not null auto_increment    comment '面试ID',
  application_id    bigint(20)      not null                   comment '投递ID',
  job_id            bigint(20)      not null                   comment '职位ID',
  student_id        bigint(20)      not null                   comment '学生ID',
  company_id        bigint(20)      not null                   comment '企业ID',
  interview_type    varchar(20)     default null               comment '面试类型（初试/复试/终试）',
  interview_method  varchar(20)     default null               comment '面试方式（现场/视频/电话）',
  interview_time    datetime        default null               comment '面试时间',
  interview_address varchar(200)    default null               comment '面试地址',
  interviewer       varchar(100)    default null               comment '面试官',
  contact_phone     varchar(20)     default null               comment '联系电话',
  interview_notice  text            default null               comment '面试通知',
  interview_result  varchar(20)     default null               comment '面试结果（通过/未通过/待定）',
  interview_feedback text           default null               comment '面试评价',
  score             decimal(5,2)    default null               comment '面试评分',
  status            char(1)         not null default '0'       comment '状态（0待确认 1已确认 2已完成 3已取消）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (interview_id)
) engine=innodb auto_increment=100 comment = '面试安排表';

create index idx_itv_application on crems_interview(application_id);
create index idx_itv_student on crems_interview(student_id);
create index idx_itv_company on crems_interview(company_id);
create index idx_itv_time on crems_interview(interview_time);
create index idx_itv_status on crems_interview(status);

-- ----------------------------
-- 6. 职位收藏表
-- ----------------------------
drop table if exists crems_favorite;
create table crems_favorite (
  favorite_id       bigint(20)      not null auto_increment    comment '收藏ID',
  job_id            bigint(20)      not null                   comment '职位ID',
  student_id        bigint(20)      not null                   comment '学生ID',
  create_time       datetime                                   comment '收藏时间',
  primary key (favorite_id)
) engine=innodb auto_increment=100 comment = '职位收藏表';

create unique index uk_fav_job_student on crems_favorite(job_id, student_id);
create index idx_fav_student on crems_favorite(student_id);
