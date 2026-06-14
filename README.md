# CREMS-mini 校园招聘与就业信息管理系统

CREMS-mini 是一个面向高校校园招聘场景的前后端分离管理系统。项目基于 CREMS/RuoYi-Vue 风格脚手架扩展，保留用户、角色、菜单、字典、日志、定时任务、代码生成等后台基础能力，并新增学生、企业、职位、投递、面试、收藏和统计分析等招聘业务模块。

当前仓库包含完整后端、前端、数据库脚本和实训文档，可用于课程设计、毕业设计原型、校园招聘业务演示或二次开发。

## 功能概览

| 角色/端  | 功能                                                 |
| ----- | -------------------------------------------------- |
| 管理员后台 | 用户角色、菜单权限、部门岗位、字典参数、公告、日志、在线用户、服务监控、缓存监控、定时任务、代码生成 |
| 招聘管理  | 企业管理、学生管理、职位管理、投递管理、面试管理、招聘数据统计                    |
| 学生门户  | 职位浏览、职位详情、职位收藏、简历投递、投递进度、面试安排、个人简历                 |
| 企业门户  | 企业资料、职位发布与维护、简历处理、面试安排、企业端数据概览                     |

## 技术栈

| 层级  | 技术                                                                                              |
| --- | ----------------------------------------------------------------------------------------------- |
| 后端  | Java 17、Spring Boot 4.0.3、Spring Security、JWT、MyBatis、PageHelper、Druid、Quartz、Springdoc OpenAPI |
| 数据  | MySQL、Redis                                                                                     |
| 前端  | Vue 3.5、Vue Router 4、Pinia、Element Plus、Axios、ECharts、Vite 6                                    |
| 构建  | Maven、npm                                                                                       |

## 项目结构

```text
CREMS-mini/
├── backend/
│   ├── crems-admin/            # 后端启动入口与 Web 控制器
│   ├── crems-common/           # 通用常量、工具、注解、基础实体
│   ├── crems-framework/        # 安全、配置、拦截器、Redis、数据源等框架能力
│   ├── crems-system/           # 用户、角色、菜单、部门、字典、公告、日志等系统模块
│   ├── crems-quartz/           # 定时任务模块
│   ├── crems-generator/        # 代码生成模块
│   ├── crems-crems/            # 校园招聘业务模块
│   ├── sql/                    # 系统表、业务表、菜单、角色、测试数据脚本
│   └── doc/                    # 后端环境使用手册
├── frontend/
│   ├── src/api/                # 接口封装
│   ├── src/views/crems/        # 后台招聘管理页面
│   ├── src/views/portal/       # 学生端与企业端门户页面
│   ├── src/router/portal.js    # 门户路由
│   └── vite.config.js          # Vite 构建与代理配置
└── README.md
```

## 环境要求

- JDK 17+
- Maven 3.6+
- Node.js 16+，建议使用 18+
- MySQL 8.0+
- Redis 6.0+

## 本地启动

### 1. 初始化数据库

先创建数据库：

```sql
CREATE DATABASE crems DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

按顺序执行脚本：

```bash
mysql -u root -p crems < backend/sql/crems_20260417.sql
mysql -u root -p crems < backend/sql/quartz.sql
mysql -u root -p crems < backend/sql/crems/crems_schema.sql
mysql -u root -p crems < backend/sql/crems/crems_menu.sql
mysql -u root -p crems < backend/sql/crems/crems_role.sql
mysql -u root -p crems < backend/sql/crems/crems_role_menu.sql
mysql -u root -p crems < backend/sql/crems/crems_dict.sql
mysql -u root -p crems < backend/sql/crems/crems_test_data.sql
```

`crems_test_data.sql` 是演示数据，不需要示例账号和业务数据时可以跳过。

### 2. 配置后端

数据库与 Redis 配置位于：

- `backend/crems-admin/src/main/resources/application-druid.yml`
- `backend/crems-admin/src/main/resources/application.yml`

默认配置：

| 配置项       | 默认值                                                   |
| --------- | ----------------------------------------------------- |
| 后端端口      | `8080`                                                |
| MySQL 地址  | `localhost:3306/crems`                                |
| MySQL 用户名 | `root`                                                |
| MySQL 密码  | `123456`                                              |
| Redis 地址  | `localhost:6379`                                      |
| Redis 密码  | 空                                                     |
| 文件上传目录    | `${CREMS_UPLOAD_PATH:${user.home}/.crems/uploadPath}` |

上传文件默认保存到当前用户主目录下的 `.crems/uploadPath`，例如 macOS 上通常是 `/Users/你的用户名/.crems/uploadPath`。生产环境建议使用环境变量指定独立数据目录：

```bash
export CREMS_UPLOAD_PATH=/data/crems/uploadPath
```

### 3. 启动后端

```bash
cd backend
mvn clean package -DskipTests
java -jar crems-admin/target/crems-admin.jar
```

后端服务地址：

- API：`http://localhost:8080`
- Swagger UI：`http://localhost:8080/swagger-ui.html`
- Druid 控制台：`http://localhost:8080/druid`，账号 `crems`，密码 `123456`

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端开发服务默认配置在 `frontend/vite.config.js`：

- 访问地址：`http://localhost`
- 端口：`80`
- 开发代理：`/dev-api` 转发到 `http://localhost:8080`

如果 80 端口被占用或当前系统不允许普通用户监听低端口，请把 `frontend/vite.config.js` 中的 `server.port` 改为 `3000`、`5173` 等普通端口。

## 默认账号

所有初始化账号密码均为 `admin123`。

| 角色    | 用户名示例                     | 来源                                      |
| ----- | ------------------------- | --------------------------------------- |
| 超级管理员 | `admin`                   | `backend/sql/crems_20260417.sql`        |
| 普通用户  | `crems`                   | `backend/sql/crems_20260417.sql`        |
| 学生用户  | `student01` 至 `student05` | `backend/sql/crems/crems_test_data.sql` |
| 企业用户  | `company01` 至 `company05` | `backend/sql/crems/crems_test_data.sql` |

## 业务数据模型

招聘业务模块主要包含 6 张表：

| 表名                  | 说明                 |
| ------------------- | ------------------ |
| `crems_company`     | 企业信息、认证状态、联系人信息    |
| `crems_student`     | 学生档案、学校专业、技能、简历信息  |
| `crems_job`         | 职位信息、薪资、城市、要求、发布状态 |
| `crems_application` | 简历投递记录、处理状态        |
| `crems_interview`   | 面试安排、面试方式、结果状态     |
| `crems_favorite`    | 学生收藏职位             |

相关脚本位于 `backend/sql/crems/`。

## 主要接口

系统接口分为后台招聘管理接口和门户接口。

| 模块      | 接口前缀                                          | 说明                           |
| ------- | --------------------------------------------- | ---------------------------- |
| 登录认证    | `/login`、`/register`、`/getInfo`、`/getRouters` | 登录、注册、用户信息、路由权限              |
| 企业管理    | `/crems/company`                              | 企业列表、详情、新增、修改、删除、导入导出、审核     |
| 学生管理    | `/crems/student`                              | 学生列表、详情、新增、修改、删除、导入导出        |
| 职位管理    | `/crems/job`                                  | 职位列表、详情、新增、修改、删除、导入导出、审核     |
| 投递管理    | `/crems/application`                          | 投递列表、详情、新增、状态更新、删除、导出        |
| 面试管理    | `/crems/interview`                            | 面试列表、详情、新增、修改、删除、导出          |
| 收藏管理    | `/crems/favorite`                             | 收藏列表、新增、取消收藏                 |
| 统计分析    | `/crems/statistics`                           | 总览、职位分布、投递状态、面试类型与结果统计       |
| 学生/企业门户 | `/portal/api`                                 | 门户职位、投递、面试、收藏、当前学生/企业资料、统计概览 |

## 常用命令

后端：

```bash
cd backend
mvn clean package -DskipTests
mvn test
java -jar crems-admin/target/crems-admin.jar
```

前端：

```bash
cd frontend
npm run dev
npm run build:prod
npm run preview
```

## 开发说明

- 后端包名统一为 `com.crems`。
- 业务模块代码集中在 `backend/crems-crems/src/main/java/com/crems/crems`。
- 后台招聘管理页面集中在 `frontend/src/views/crems`。
- 学生端与企业端门户页面集中在 `frontend/src/views/portal`。
- 权限控制沿用菜单权限标识，例如 `crems:job:list`、`crems:company:audit`。
- 统一返回结构沿用 `AjaxResult`，分页列表沿用 `TableDataInfo`。
- 前端请求统一走 `frontend/src/utils/request.js` 和 `frontend/src/api` 下的模块封装。

## 文档

- `docs/实训报告.md`
- `docs/校园招聘与就业信息管理系统实训报告.docx`
- `docs/security-fixes-review.md`
- `backend/doc/CREMS环境使用手册.docx`

## 许可证

前端声明为 MIT License。项目基于 CREMS/RuoYi-Vue 风格后台框架扩展，二次开发时请同时遵守原框架及本仓库中各模块的许可证声明。
