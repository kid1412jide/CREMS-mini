# CREMS - 校园招聘与就业信息管理系统

<p align="center">
  <img alt="logo" src="https://oscimg.oschina.net/oscnet/up-d3d0a9303e11d522a06cd263f3079027715.png" width="120">
</p>
<h1 align="center" style="margin: 20px 0;">Campus Recruitment & Employment Management System</h1>
<h4 align="center">基于 Spring Boot + Vue 3 的校园招聘与就业信息管理系统</h4>

## 项目简介

CREMS（校园招聘与就业信息管理系统）是一款面向高校、企业和学生三方的高校招聘信息管理平台。系统基于 RuoYi-Vue 框架开发，提供企业入驻、职位发布、学生求职、简历投递、面试安排、数据统计等完整功能。

## 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot + Spring Security + JWT | 4.0.3 |
| 数据访问 | MyBatis + PageHelper | 4.0.1 / 2.1.1 |
| 数据库 | MySQL | 8.0+ |
| 缓存 | Redis | 3.0+ |
| 前端框架 | Vue 3 + Element Plus + Vite | 3.x |
| 构建工具 | Maven + npm | 3.6+ / 16+ |
| JDK | OpenJDK | 17+ |

## 目录结构

```
CREMS mini/
├── backend/                    # 后端 (RuoYi-Vue)
│   ├── ruoyi-admin/            # Web服务入口
│   ├── ruoyi-framework/        # 框架配置
│   ├── ruoyi-system/           # 系统模块
│   ├── ruoyi-common/           # 通用工具
│   ├── ruoyi-quartz/           # 定时任务
│   ├── ruoyi-generator/        # 代码生成器
│   └── ruoyi-crems/            # 业务模块 ★ 自定义
│       └── java/com/ruoyi/crems/
│           ├── controller/     # 6个业务控制器 + 统计
│           ├── domain/         # 实体类 + VO
│           ├── mapper/        # 数据访问层
│           └── service/       # 业务服务层
├── frontend/                   # 前端 (RuoYi-Vue3)
│   └── src/
│       ├── api/crems/         # 7个API模块
│       └── views/crems/       # 10个业务页面
├── sql/                        # 数据库脚本
│   └── crems/
│       ├── crems_schema.sql   # 6张业务表
│       ├── crems_menu.sql     # 菜单权限
│       └── crems_dict.sql     # 数据字典
└── docs/                       # 项目文档
    ├── 项目规划.md
    ├── 数据库设计.md
    ├── 开发阶段详细说明.md
    └── 接口文档.md
```

## 业务模块

系统包含六大核心业务模块：

| 模块 | 说明 | 主要功能 |
|------|------|---------|
| **企业管理** | 企业注册、认证、信息管理 | 入驻审核、企业信息维护 |
| **学生管理** | 学生注册、简历管理 | 个人信息、简历上传 |
| **职位管理** | 职位发布、上下架、审核 | 职位发布、搜索、收藏 |
| **投递管理** | 简历投递、状态跟踪 | 投递简历、进度查询 |
| **面试管理** | 面试安排、结果录入 | 面试通知、评价反馈 |
| **统计分析** | 数据概览、多维度统计 | 投递统计、面试统计 |

## 主要接口

| 接口路径 | 说明 |
|---------|------|
| `GET /crems/company/list` | 查询企业列表 |
| `POST /crems/company` | 新增企业 |
| `PUT /crems/company/audit` | 企业审核 |
| `GET /crems/student/list` | 查询学生列表 |
| `POST /crems/student` | 新增学生 |
| `GET /crems/job/list` | 查询职位列表 |
| `POST /crems/job` | 发布职位 |
| `PUT /crems/job/audit` | 职位审核 |
| `POST /crems/application` | 投递简历 |
| `PUT /crems/application` | 更新投递状态 |
| `POST /crems/interview` | 创建面试 |
| `PUT /crems/interview` | 更新面试信息 |
| `GET /crems/statistics/overview` | 数据概览 |

详细接口文档见 [接口文档.md](接口文档.md)。

## 数据库表

| 表名 | 说明 |
|------|------|
| `crems_company` | 企业信息表 |
| `crems_student` | 学生信息表 |
| `crems_job` | 职位信息表 |
| `crems_application` | 简历投递表 |
| `crems_interview` | 面试安排表 |
| `crems_favorite` | 职位收藏表 |

## 环境要求

- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

## 快速启动

### 1. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p123456

# 执行初始化脚本
source sql/ry_20260417.sql;           # 若依系统表
source sql/quartz.sql;                # 定时任务表
source sql/crems/crems_schema.sql;   # 业务表
source sql/crems/crems_menu.sql;     # 菜单权限
source sql/crems/crems_dict.sql;     # 数据字典
```

### 2. 后端启动

```bash
cd backend

# 修改配置文件中的数据库密码
# ruoyi-admin/src/main/resources/application-druid.yml
# ruoyi-admin/src/main/resources/application.yml (Redis密码)

# 编译打包
mvn clean package -DskipTests

# 启动服务
java -jar ruoyi-admin/target/ruoyi-admin.jar
```

服务地址：http://localhost:8080

### 3. 前端启动

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端地址：http://localhost:3000

## 系统账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 企业账号 | 自行注册 | - |
| 学生账号 | 自行注册 | - |

## 项目文档

- [项目规划.md](项目规划.md) — 项目整体规划与阶段划分
- [数据库设计.md](数据库设计.md) — 表结构、字段、索引设计
- [开发阶段详细说明.md](开发阶段详细说明.md) — 各阶段任务清单
- [接口文档.md](接口文档.md) — RESTful API 完整文档
- [已完成工作记录.md](已完成工作记录.md) — 开发进度记录

## 开发规范

### 后端（Java）

- 分层结构：`Controller → Service → Mapper → Domain`
- 权限控制：`@PreAuthorize("@ss.hasPermi('crems:xxx:yyy')")`
- 分页查询：使用 `startPage()` + `getDataTable()`
- 统一返回：`AjaxResult`（code / msg / data）

### 前端（Vue 3）

- 组合式 API：`setup` + `<script setup>`
- 样式规范：Element Plus 组件 + 自定义样式
- API 调用：`/api/crems/` 模块化封装

### 数据库

- 表名小写 + 下划线：`crems_xxx`
- 字段命名清晰，带注释
- 必填字段在前，可选字段在后
- 创建时间、更新时间、备注字段统一

## 许可证

本项目基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 框架扩展，遵循原项目许可证。
