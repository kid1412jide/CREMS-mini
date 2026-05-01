<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>校园招聘与就业信息管理系统</h2>
        <p>
          CREMS (Campus Recruitment and Employment Management System) 是一款面向高校的校园招聘与就业信息管理系统，为企业、学生和学校提供一站式招聘就业服务平台。
        </p>
        <p>
          系统支持企业管理、学生管理、职位发布、简历投递、面试安排等核心功能，帮助高校实现就业信息的数字化管理。
        </p>
        <p>
          <b>当前版本:</b> <span>v{{ version }}</span>
        </p>
      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>技术选型</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>后端技术</h4>
            <ul>
              <li>SpringBoot</li>
              <li>Spring Security</li>
              <li>JWT</li>
              <li>MyBatis</li>
              <li>Druid</li>
              <li>Fastjson</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>前端技术</h4>
            <ul>
              <li>Vue3</li>
              <li>Pinia</li>
              <li>Element Plus</li>
              <li>Axios</li>
              <li>Vite</li>
              <li>ECharts</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template v-slot:header>
            <div class="clearfix">
              <span>核心功能</span>
            </div>
          </template>
          <div class="body">
            <ul>
              <li>企业管理 - 企业注册、认证、信息管理</li>
              <li>学生管理 - 学生注册、简历管理</li>
              <li>职位管理 - 职位发布、审核、搜索</li>
              <li>简历投递 - 在线投递、状态跟踪</li>
              <li>面试管理 - 面试安排、结果记录</li>
              <li>数据统计 - 招聘数据可视化分析</li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template v-slot:header>
            <div class="clearfix">
              <span>系统角色</span>
            </div>
          </template>
          <div class="body">
            <ul>
              <li>管理员 - 系统管理、数据统计、审核管理</li>
              <li>企业用户 - 职位发布、简历筛选、面试安排</li>
              <li>学生用户 - 职位搜索、简历投递、面试查看</li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template v-slot:header>
            <div class="clearfix">
              <span>项目信息</span>
            </div>
          </template>
          <div class="body">
            <p><b>项目名称:</b> CREMS</p>
            <p><b>启动日期:</b> 2026-04-21</p>
            <p><b>技术框架:</b> SpringBoot + Vue3</p>
            <p><b>数据库:</b> MySQL 8.0+</p>
            <p><b>缓存:</b> Redis</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
const version = ref('1.0.0')
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>
