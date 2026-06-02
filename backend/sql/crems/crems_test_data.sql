-- ----------------------------
-- 校园招聘系统 - 测试数据
-- 执行顺序：在 crems_schema.sql、crems_menu.sql、crems_role.sql、crems_dict.sql 之后执行
-- ----------------------------

-- ----------------------------
-- 1. 创建测试用户（学生用户）
-- ----------------------------
-- 密码为 admin123 的 RSA 加密后存储，这里使用CREMS默认密码
INSERT INTO sys_user (user_id, user_name, nick_name, email, phonenumber, sex, avatar, password, status, del_flag, create_by, create_time)
VALUES
(100, 'student01', '张三', 'zhangsan@example.com', '13800138001', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', sysdate()),
(101, 'student02', '李四', 'lisi@example.com', '13800138002', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', sysdate()),
(102, 'student03', '王五', 'wangwu@example.com', '13800138003', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', sysdate()),
(103, 'student04', '赵六', 'zhaoliu@example.com', '13800138004', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', sysdate()),
(104, 'student05', '钱七', 'qianqi@example.com', '13800138005', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', sysdate());

-- 创建测试用户（企业用户）
INSERT INTO sys_user (user_id, user_name, nick_name, email, phonenumber, sex, avatar, password, status, del_flag, create_by, create_time)
VALUES
(200, 'company01', '华为HR', 'huawei@example.com', '13900139001', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', sysdate()),
(201, 'company02', '腾讯HR', 'tencent@example.com', '13900139002', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', sysdate()),
(202, 'company03', '阿里HR', 'alibaba@example.com', '13900139003', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', sysdate()),
(203, 'company04', '字节HR', 'bytedance@example.com', '13900139004', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', sysdate()),
(204, 'company05', '美团HR', 'meituan@example.com', '13900139005', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', sysdate());

-- 分配角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (100, 3), (101, 3), (102, 3), (103, 3), (104, 3);
INSERT INTO sys_user_role (user_id, role_id) VALUES (200, 4), (201, 4), (202, 4), (203, 4), (204, 4);

-- ----------------------------
-- 2. 企业测试数据
-- ----------------------------
INSERT INTO crems_company (company_id, user_id, company_name, company_code, company_type, industry, scale, address, website, description, contact_person, contact_phone, contact_email, status, create_by, create_time)
VALUES
(1, 200, '华为技术有限公司', '914403001922038216', 'listed', 'IT/互联网', '1000+', '深圳市龙岗区坂田华为基地', 'https://www.huawei.com', '华为是全球领先的 ICT 基础设施和智能终端提供商，致力于把数字世界带入每个人、每个家庭、每个组织，构建万物互联的智能世界。', '张经理', '0755-28780808', 'hr@huawei.com', '1', 'admin', sysdate()),
(2, 201, '腾讯科技（深圳）有限公司', '91440300708461136T', 'listed', 'IT/互联网', '1000+', '深圳市南山区高新科技园中区一路', 'https://www.tencent.com', '腾讯是一家世界领先的互联网科技公司，用创新的产品和服务提升全球各地人们的生活品质。', '李经理', '0755-86013388', 'hr@tencent.com', '1', 'admin', sysdate()),
(3, 202, '阿里巴巴（中国）有限公司', '91330100799655058B', 'listed', '电子商务', '1000+', '杭州市余杭区文一西路969号', 'https://www.alibaba.com', '阿里巴巴集团由曾担任英语教师的马云与其他来自不同背景的17个人，于1999年在中国杭州创立。', '王经理', '0571-85022088', 'hr@alibaba.com', '1', 'admin', sysdate()),
(4, 203, '北京字节跳动科技有限公司', '91110108551385082Q', 'private', 'IT/互联网', '1000+', '北京市海淀区北三环西路甲18号', 'https://www.bytedance.com', '字节跳动是一家全球性的科技公司，致力于用技术赋能全球创作者和用户。', '赵经理', '010-57655999', 'hr@bytedance.com', '1', 'admin', sysdate()),
(5, 204, '北京三快在线科技有限公司（美团）', '91110108551385082Q', 'listed', '生活服务', '1000+', '北京市朝阳区望京东路4号', 'https://www.meituan.com', '美团的使命是「帮大家吃得更好，生活更好」。公司聚焦「Food + Platform」战略。', '钱经理', '010-57655999', 'hr@meituan.com', '1', 'admin', sysdate()),
(6, NULL, '杭州网易云音乐科技有限公司', '913301003112073726', 'private', '文娱传媒', '500-1000', '杭州市滨江区网商路399号', 'https://music.163.com', '网易云音乐是一款专注于发现与分享的音乐产品，依托专业音乐人、DJ、好友推荐及社交功能。', '孙经理', '0571-89853999', 'hr@music.163.com', '0', 'admin', sysdate());

-- ----------------------------
-- 3. 学生测试数据
-- ----------------------------
INSERT INTO crems_student (student_id, user_id, student_no, student_name, gender, birth_date, phone, email, school, major, education, grade, graduation_date, skills, status, create_by, create_time)
VALUES
(1, 100, '2022001001', '张三', '0', '2000-03-15', '13800138001', 'zhangsan@example.com', '北京大学', '计算机科学与技术', 'bachelor', '大四', '2026-06-30', 'Java,Python,Spring Boot,MySQL', '0', 'admin', sysdate()),
(2, 101, '2022001002', '李四', '1', '2001-07-22', '13800138002', 'lisi@example.com', '清华大学', '软件工程', 'bachelor', '大三', '2027-06-30', 'Vue.js,React,Node.js,TypeScript', '0', 'admin', sysdate()),
(3, 102, '2022001003', '王五', '0', '1999-11-08', '13800138003', 'wangwu@example.com', '浙江大学', '人工智能', 'master', '研二', '2026-06-30', 'TensorFlow,PyTorch,机器学习,深度学习', '0', 'admin', sysdate()),
(4, 103, '2022001004', '赵六', '1', '2000-05-30', '13800138004', 'zhaoliu@example.com', '复旦大学', '数据科学与大数据技术', 'bachelor', '大四', '2026-06-30', 'Spark,Hadoop,Python,SQL', '0', 'admin', sysdate()),
(5, 104, '2022001005', '钱七', '0', '2001-01-12', '13800138005', 'qianqi@example.com', '上海交通大学', '电子信息工程', 'bachelor', '大三', '2027-06-30', 'C/C++,嵌入式开发,FPGA', '0', 'admin', sysdate()),
(6, NULL, '2022001006', '孙八', '0', '2000-09-18', '13800138006', 'sunba@example.com', '南京大学', '信息安全', 'bachelor', '大四', '2026-06-30', '网络安全,渗透测试,Linux,Docker', '0', 'admin', sysdate()),
(7, NULL, '2022001007', '周九', '1', '2001-12-25', '13800138007', 'zhoujiu@example.com', '武汉大学', '数学与应用数学', 'master', '研一', '2028-06-30', '数学建模,统计分析,R语言,Python', '0', 'admin', sysdate());

-- ----------------------------
-- 4. 职位测试数据
-- ----------------------------
INSERT INTO crems_job (job_id, company_id, job_title, job_type, category, department, recruit_num, work_city, salary_min, salary_max, education_required, experience_required, job_description, job_requirements, welfare, publish_date, deadline, status, create_by, create_time)
VALUES
(1, 1, 'Java后端开发工程师', 'full_time', '技术类', '2012实验室', 10, '深圳', 15000.00, 30000.00, 'bachelor', '应届生', '负责华为云平台后端服务的设计与开发，参与系统架构优化。', '1. 计算机相关专业本科及以上学历；2. 熟悉 Java/Spring Boot；3. 了解微服务架构；4. 良好的编码习惯。', '五险一金,年终奖,股票期权,免费班车,餐补', sysdate(), '2026-08-31', '1', 'company01', sysdate()),
(2, 1, '前端开发工程师', 'full_time', '技术类', '消费者BG', 5, '深圳', 12000.00, 25000.00, 'bachelor', '应届生', '负责华为商城、花粉俱乐部等前端页面开发与维护。', '1. 熟悉 Vue.js/React；2. 了解 HTML5/CSS3/JavaScript；3. 有移动端开发经验优先。', '五险一金,年终奖,免费班车,餐补', sysdate(), '2026-08-31', '1', 'company01', sysdate()),
(3, 2, '后端开发工程师', 'full_time', '技术类', '微信事业群', 15, '广州', 18000.00, 35000.00, 'bachelor', '应届生', '参与微信核心系统的后端开发，支撑海量用户服务。', '1. 计算机相关专业；2. 扎实的数据结构与算法基础；3. 熟悉 Go/C++/Java 任一。', '六险一金,年终奖,股票期权,免费三餐', sysdate(), '2026-09-30', '1', 'company02', sysdate()),
(4, 2, '游戏开发工程师', 'full_time', '技术类', '天美工作室', 8, '深圳', 20000.00, 40000.00, 'bachelor', '应届生', '参与王者荣耀等顶级游戏项目开发。', '1. 热爱游戏；2. 熟悉 Unity/Unreal Engine；3. 有游戏项目经验优先。', '六险一金,年终奖,游戏体验,免费三餐', sysdate(), '2026-09-30', '1', 'company02', sysdate()),
(5, 3, '算法工程师', 'full_time', '技术类', '达摩院', 10, '杭州', 20000.00, 45000.00, 'master', '应届生', '从事推荐系统、NLP、CV 等方向的算法研究与落地。', '1. 硕士及以上学历；2. 有顶会论文优先；3. 扎实的机器学习基础。', '六险一金,年终奖,股票期权,免费三餐,健身房', sysdate(), '2026-10-31', '1', 'company03', sysdate()),
(6, 3, '产品经理', 'full_time', '产品类', '淘天集团', 5, '杭州', 15000.00, 30000.00, 'bachelor', '应届生', '负责淘宝核心产品功能的设计与迭代。', '1. 对电商有浓厚兴趣；2. 优秀的逻辑思维能力；3. 良好的沟通表达能力。', '六险一金,年终奖,免费三餐', sysdate(), '2026-10-31', '1', 'company03', sysdate()),
(7, 4, 'Go后端开发工程师', 'full_time', '技术类', '抖音', 20, '北京', 18000.00, 35000.00, 'bachelor', '应届生', '参与抖音、今日头条等产品的后端服务开发。', '1. 熟悉 Go/Java/C++ 任一；2. 了解分布式系统；3. 有高并发经验优先。', '六险一金,年终奖,免费三餐,健身房', sysdate(), '2026-09-30', '1', 'company04', sysdate()),
(8, 4, '数据分析师', 'internship', '数据类', '商业化', 3, '北京', 300.00, 500.00, 'bachelor', '实习', '协助分析广告投放数据，产出分析报告。', '1. 熟练使用 SQL/Excel；2. 了解 Python 数据分析；3. 每周至少 4 天。', '实习补贴,免费三餐,转正机会', sysdate(), '2026-07-31', '1', 'company04', sysdate()),
(9, 5, 'Java开发工程师', 'full_time', '技术类', '到店事业群', 12, '北京', 15000.00, 28000.00, 'bachelor', '应届生', '参与美团外卖、到店等核心业务系统开发。', '1. 计算机相关专业；2. 熟悉 Java 技术栈；3. 了解 Spring Cloud 优先。', '六险一金,年终奖,免费三餐,打车补贴', sysdate(), '2026-09-30', '1', 'company05', sysdate()),
(10, 5, '测试开发工程师', 'full_time', '技术类', '基础研发', 6, '北京', 12000.00, 22000.00, 'bachelor', '应届生', '负责自动化测试框架搭建与测试工具开发。', '1. 熟悉 Python/Java；2. 了解自动化测试框架；3. 细心负责。', '六险一金,年终奖,免费三餐', sysdate(), '2026-09-30', '1', 'company05', sysdate());

-- ----------------------------
-- 5. 投递测试数据
-- ----------------------------
INSERT INTO crems_application (application_id, job_id, student_id, company_id, apply_time, status, create_by, create_time)
VALUES
(1, 1, 1, 1, '2026-05-10 10:30:00', '3', 'student01', sysdate()),
(2, 3, 1, 2, '2026-05-11 14:20:00', '1', 'student01', sysdate()),
(3, 5, 3, 3, '2026-05-12 09:15:00', '2', 'student03', sysdate()),
(4, 7, 2, 4, '2026-05-13 16:45:00', '0', 'student02', sysdate()),
(5, 9, 4, 5, '2026-05-14 11:00:00', '1', 'student04', sysdate()),
(6, 2, 2, 1, '2026-05-15 08:30:00', '4', 'student02', sysdate()),
(7, 4, 5, 2, '2026-05-16 15:20:00', '0', 'student05', sysdate()),
(8, 6, 1, 3, '2026-05-17 10:10:00', '5', 'student01', sysdate());

-- ----------------------------
-- 6. 面试测试数据
-- ----------------------------
INSERT INTO crems_interview (interview_id, application_id, job_id, student_id, company_id, interview_type, interview_method, interview_time, interview_address, interviewer, contact_phone, interview_notice, status, create_by, create_time)
VALUES
(1, 1, 1, 1, 1, 'first', 'onsite', '2026-05-25 14:00:00', '深圳市龙岗区坂田华为基地A区', '李工', '0755-28780808', '请携带身份证和简历，提前15分钟到达。', '1', 'company01', sysdate()),
(2, 1, 1, 1, 1, 'second', 'video', '2026-05-28 10:00:00', '腾讯会议（会议号另行通知）', '王经理', '0755-28780808', '请确保网络畅通，提前测试设备。', '0', 'company01', sysdate()),
(3, 3, 5, 3, 3, 'first', 'video', '2026-05-26 15:30:00', '钉钉视频面试', '赵博士', '0571-85022088', '请准备算法题目讲解。', '1', 'company03', sysdate()),
(4, 8, 6, 1, 3, 'first', 'onsite', '2026-05-20 09:00:00', '杭州市余杭区文一西路969号', '孙经理', '0571-85022088', '请携带作品集。', '2', 'company03', sysdate()),
(5, 8, 6, 1, 3, 'final', 'onsite', '2026-05-27 14:00:00', '杭州市余杭区文一西路969号', 'HR总监', '0571-85022088', '终面，请着正装。', '0', 'company03', sysdate());

-- ----------------------------
-- 7. 收藏测试数据
-- ----------------------------
INSERT INTO crems_favorite (favorite_id, job_id, student_id, create_time)
VALUES
(1, 1, 1, sysdate()),
(2, 3, 1, sysdate()),
(3, 5, 1, sysdate()),
(4, 7, 2, sysdate()),
(5, 9, 2, sysdate()),
(6, 2, 3, sysdate()),
(7, 4, 3, sysdate()),
(8, 6, 4, sysdate()),
(9, 8, 4, sysdate()),
(10, 10, 5, sysdate());

-- ----------------------------
-- 完成！测试数据已全部插入
-- ----------------------------
