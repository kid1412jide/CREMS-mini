package com.crems.crems.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.crems.common.core.controller.BaseController;
import com.crems.common.core.domain.AjaxResult;
import com.crems.common.core.page.TableDataInfo;
import com.crems.common.exception.ServiceException;
import com.crems.common.utils.SecurityUtils;
import com.crems.common.utils.StringUtils;
import com.crems.crems.domain.CremsApplication;
import com.crems.crems.domain.CremsCompany;
import com.crems.crems.domain.CremsFavorite;
import com.crems.crems.domain.CremsInterview;
import com.crems.crems.domain.CremsJob;
import com.crems.crems.domain.CremsStudent;
import com.crems.crems.mapper.SysUserExtMapper;
import com.crems.crems.service.ICremsApplicationService;
import com.crems.crems.service.ICremsCompanyService;
import com.crems.crems.service.ICremsFavoriteService;
import com.crems.crems.service.ICremsInterviewService;
import com.crems.crems.service.ICremsJobService;
import com.crems.crems.service.ICremsStatisticsService;
import com.crems.crems.service.ICremsStudentService;

/**
 * 门户端API - 无需菜单权限，仅需登录认证
 *
 * @author crems
 */
@RestController
@RequestMapping("/portal/api")
public class CremsPortalController extends BaseController
{
    @Autowired
    private ICremsJobService jobService;
    @Autowired
    private ICremsApplicationService applicationService;
    @Autowired
    private ICremsInterviewService interviewService;
    @Autowired
    private ICremsFavoriteService favoriteService;
    @Autowired
    private ICremsStudentService studentService;
    @Autowired
    private ICremsCompanyService companyService;
    @Autowired
    private ICremsStatisticsService statisticsService;
    @Autowired
    private SysUserExtMapper sysUserExtMapper;

    private boolean isStudentRole() {
        try {
            // 门户端按业务角色分流，管理员拥有更高权限时不套用学生端限制。
            return SecurityUtils.hasRole("student") && !SecurityUtils.hasRole("admin");
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isCompanyRole() {
        try {
            // 企业门户只处理纯企业账号，避免管理员被误判后进入企业数据边界。
            return SecurityUtils.hasRole("company") && !SecurityUtils.hasRole("admin");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取当前登录学生的studentId，如果不是学生则抛出异常
     */
    private Long getCurrentStudentId() {
        Long userId = getUserId();
        CremsStudent student = studentService.selectStudentByUserId(userId);
        if (student == null) {
            throw new ServiceException("未找到当前用户的学生信息");
        }
        return student.getStudentId();
    }

    /**
     * 获取当前登录企业的companyId，如果不是企业则抛出异常
     */
    private Long getCurrentCompanyId() {
        Long userId = getUserId();
        CremsCompany company = companyService.selectCompanyByUserId(userId);
        if (company == null) {
            throw new ServiceException("未找到当前用户的企业信息");
        }
        return company.getCompanyId();
    }

    // ==================== 职位 ====================

    @GetMapping("/job/list")
    public TableDataInfo jobList(CremsJob job)
    {
        startPage();
        if (isCompanyRole()) {
            // 企业端列表强制绑定当前企业，防止通过请求参数查看其他企业职位。
            CremsCompany company = companyService.selectCompanyByUserId(getUserId());
            if (company == null) {
                return getDataTable(List.of());
            }
            job.setCompanyId(company.getCompanyId());
        }
        List<CremsJob> list = jobService.selectJobList(job);
        return getDataTable(list);
    }

    @GetMapping("/job/{jobId}")
    public AjaxResult getJob(@PathVariable("jobId") Long jobId)
    {
        CremsJob job = jobService.selectJobById(jobId);
        if (job == null) {
            return error("职位不存在");
        }
        if (isCompanyRole()) {
            // 企业查看职位详情时先做归属校验；学生端只能查看已发布职位。
            CremsCompany company = companyService.selectCompanyByUserId(getUserId());
            if (company == null) {
                return error("请先完善企业资料");
            }
            if (!company.getCompanyId().equals(job.getCompanyId())) {
                return error("无权查看此职位");
            }
            return success(job);
        }
        if (!"1".equals(job.getStatus())) {
            return error("职位未发布");
        }
        // 浏览量+1
        jobService.incrementViewCount(jobId);
        return success(jobService.selectJobById(jobId));
    }

    @PostMapping("/job")
    public AjaxResult addJob(@Validated @RequestBody CremsJob job)
    {
        // 企业只能为自己发布职位
        Long companyId = getCurrentCompanyId();
        job.setCompanyId(companyId);
        job.setCreateBy(getUsername());
        return toAjax(jobService.insertJob(job));
    }

    @PutMapping("/job")
    public AjaxResult updateJob(@Validated @RequestBody CremsJob job)
    {
        // 企业只能修改自己的职位
        Long companyId = getCurrentCompanyId();
        CremsJob existingJob = jobService.selectJobById(job.getJobId());
        if (existingJob == null || !companyId.equals(existingJob.getCompanyId())) {
            return error("无权修改此职位");
        }
        job.setUpdateBy(getUsername());
        return toAjax(jobService.updateJob(job));
    }

    @DeleteMapping("/job/{jobId}")
    public AjaxResult deleteJob(@PathVariable Long jobId)
    {
        // 企业只能删除自己的职位
        Long companyId = getCurrentCompanyId();
        CremsJob existingJob = jobService.selectJobById(jobId);
        if (existingJob == null || !companyId.equals(existingJob.getCompanyId())) {
            return error("无权删除此职位");
        }
        return toAjax(jobService.deleteJobById(jobId));
    }

    // ==================== 投递 ====================

    @GetMapping("/application/list")
    public TableDataInfo applicationList(CremsApplication application)
    {
        startPage();
        // 投递列表是学生端和企业端共用接口，入口处统一收窄数据范围。
        Long userId = getUserId();
        CremsStudent student = studentService.selectStudentByUserId(userId);
        if (student != null) {
            // 学生只能查看自己的投递
            application.setStudentId(student.getStudentId());
        } else {
            // 企业只能查看自己公司职位的投递
            CremsCompany company = companyService.selectCompanyByUserId(userId);
            if (company != null) {
                application.setCompanyId(company.getCompanyId());
            } else {
                // 非学生非企业用户（如admin）不允许通过portal访问
                return getDataTable(List.of());
            }
        }
        List<CremsApplication> list = applicationService.selectApplicationList(application);
        return getDataTable(list);
    }

    @PostMapping("/application")
    public AjaxResult addApplication(@RequestBody CremsApplication application)
    {
        application.setCreateBy(getUsername());
        // 学生身份从登录态获取，不能信任前端传来的 studentId。
        Long studentId = getCurrentStudentId();
        application.setStudentId(studentId);
        // companyId 由职位反查得到，保证投递记录和职位归属一致。
        CremsJob job = jobService.selectJobById(application.getJobId());
        if (job == null) {
            return error("职位不存在");
        }
        application.setCompanyId(job.getCompanyId());

        // 检查是否已投递过该职位
        CremsApplication query = new CremsApplication();
        query.setStudentId(studentId);
        query.setJobId(application.getJobId());
        java.util.List<CremsApplication> existing = applicationService.selectApplicationList(query);
        if (existing != null && !existing.isEmpty()) {
            return error("您已投递过该职位，请勿重复投递");
        }

        // 使用统一事务方法，同时插入投递记录和更新投递计数
        int rows = applicationService.applyForJob(application);
        return rows > 0 ? success() : error("投递失败");
    }

    @PutMapping("/application")
    public AjaxResult updateApplication(@RequestBody CremsApplication application)
    {
        // 企业只能处理自己公司职位下的投递，状态变更也在这个边界内完成。
        Long companyId = getCurrentCompanyId();
        CremsApplication existing = applicationService.selectApplicationById(application.getApplicationId());
        if (existing == null || !companyId.equals(existing.getCompanyId())) {
            return error("无权修改此投递记录");
        }

        String newStatus = application.getStatus();
        String currentStatus = existing.getStatus();

        // 如果状态发生变化，使用 CAS 更新并校验状态机
        if (newStatus != null && !newStatus.equals(currentStatus)) {
            if (!isValidStatusTransition(currentStatus, newStatus)) {
                return error("不允许从状态 " + currentStatus + " 转换到 " + newStatus);
            }
            int updated = applicationService.updateApplicationStatusIfCurrent(
                    application.getApplicationId(), currentStatus, newStatus, getUsername());
            if (updated == 0) {
                return error("投递状态已变化，请刷新后重试");
            }
        }

        // 更新其他字段（反馈、查看时间等）
        if (application.getFeedback() != null || application.getViewTime() != null) {
            CremsApplication updateEntity = new CremsApplication();
            updateEntity.setApplicationId(application.getApplicationId());
            updateEntity.setFeedback(application.getFeedback());
            updateEntity.setViewTime(application.getViewTime());
            updateEntity.setUpdateBy(getUsername());
            applicationService.updateApplication(updateEntity);
        }

        return success();
    }

    /**
     * 校验投递状态转换是否合法
     * 0:待查看 -> 1:已查看, 2:初筛通过, 4:已拒绝
     * 1:已查看 -> 2:初筛通过, 4:已拒绝
     * 2:初筛通过 -> 3:面试邀请, 4:已拒绝
     * 3:面试邀请 -> 5:已录用, 4:已拒绝
     */
    private boolean isValidStatusTransition(String current, String target) {
        return switch (current) {
            case "0" -> "1".equals(target) || "2".equals(target) || "4".equals(target);
            case "1" -> "2".equals(target) || "4".equals(target);
            case "2" -> "3".equals(target) || "4".equals(target);
            case "3" -> "5".equals(target) || "4".equals(target);
            default -> false;
        };
    }

    // ==================== 面试 ====================

    @GetMapping("/interview/list")
    public TableDataInfo interviewList(CremsInterview interview)
    {
        startPage();
        // 面试列表同样按当前账号身份收窄：学生看本人，企业看本公司。
        Long userId = getUserId();
        CremsStudent student = studentService.selectStudentByUserId(userId);
        if (student != null) {
            // 学生只能查看自己的面试
            interview.setStudentId(student.getStudentId());
        } else {
            // 企业只能查看自己公司职位的面试
            CremsCompany company = companyService.selectCompanyByUserId(userId);
            if (company != null) {
                interview.setCompanyId(company.getCompanyId());
            } else {
                // 非学生非企业用户（如admin）不允许通过portal访问
                return getDataTable(List.of());
            }
        }
        List<CremsInterview> list = interviewService.selectInterviewList(interview);
        return getDataTable(list);
    }

    @PostMapping("/interview")
    public AjaxResult addInterview(@RequestBody CremsInterview interview)
    {
        Long companyId = getCurrentCompanyId();
        // 邀请面试交给 Service 做事务处理：校验投递归属、推进状态、创建面试。
        return toAjax(interviewService.inviteInterview(interview, companyId, getUsername()));
    }

    @PutMapping("/interview")
    public AjaxResult updateInterview(@RequestBody CremsInterview interview)
    {
        interview.setUpdateBy(getUsername());
        CremsInterview existing = interviewService.selectInterviewById(interview.getInterviewId());
        if (existing == null) {
            return error("面试记录不存在");
        }

        if (isStudentRole()) {
            Long studentId = getCurrentStudentId();
            if (!studentId.equals(existing.getStudentId())) {
                return error("无权修改此面试记录");
            }
            // 学生端只负责确认邀请，不能替企业修改面试安排或结果。
            if (!"0".equals(existing.getStatus()) || !"1".equals(interview.getStatus())) {
                return error("学生只能确认待确认的面试");
            }
            CremsInterview updateEntity = new CremsInterview();
            updateEntity.setInterviewId(interview.getInterviewId());
            updateEntity.setStatus("1");
            updateEntity.setUpdateBy(getUsername());
            return toAjax(interviewService.updateInterview(updateEntity));
        }

        // 企业只能更新自己公司的面试
        Long companyId = getCurrentCompanyId();
        if (!companyId.equals(existing.getCompanyId())) {
            return error("无权修改此面试记录");
        }

        // 校验面试状态转换
        String newStatus = interview.getStatus();
        if (newStatus != null && !newStatus.equals(existing.getStatus())) {
            if ("1".equals(newStatus)) {
                return error("面试确认需由学生本人操作");
            }
            if (!isValidInterviewStatusTransition(existing.getStatus(), newStatus)) {
                return error("不允许从状态 " + existing.getStatus() + " 转换到 " + newStatus);
            }
        }

        return toAjax(interviewService.updateInterview(interview));
    }

    /**
     * 校验面试状态转换是否合法
     * 0:待确认 -> 1:已确认（学生）, 3:已取消
     * 1:已确认 -> 2:已完成, 3:已取消
     */
    private boolean isValidInterviewStatusTransition(String current, String target) {
        return switch (current) {
            case "0" -> "1".equals(target) || "3".equals(target);
            case "1" -> "2".equals(target) || "3".equals(target);
            default -> false;
        };
    }

    // ==================== 收藏 ====================

    @GetMapping("/favorite/list")
    public TableDataInfo favoriteList(CremsFavorite favorite)
    {
        startPage();
        // 学生只能查看自己的收藏
        CremsStudent student = studentService.selectStudentByUserId(getUserId());
        if (student == null) {
            return getDataTable(List.of());
        }
        favorite.setStudentId(student.getStudentId());
        List<CremsFavorite> list = favoriteService.selectFavoriteList(favorite);
        return getDataTable(list);
    }

    @PostMapping("/favorite")
    public AjaxResult addFavorite(@Validated @RequestBody CremsFavorite favorite)
    {
        // 学生只能以自己的身份收藏
        Long studentId = getCurrentStudentId();
        favorite.setStudentId(studentId);
        return toAjax(favoriteService.insertFavorite(favorite));
    }

    @DeleteMapping("/favorite/{favoriteId}")
    public AjaxResult deleteFavorite(@PathVariable Long favoriteId)
    {
        // 学生只能删除自己的收藏
        Long studentId = getCurrentStudentId();
        CremsFavorite existing = favoriteService.selectFavoriteById(favoriteId);
        if (existing == null || !studentId.equals(existing.getStudentId())) {
            return error("无权删除此收藏");
        }
        return toAjax(favoriteService.deleteFavoriteById(favoriteId));
    }

    @DeleteMapping("/favorite/job/{jobId}")
    public AjaxResult deleteFavoriteByJob(@PathVariable Long jobId)
    {
        // 学生只能取消自己的收藏
        Long studentId = getCurrentStudentId();
        return toAjax(favoriteService.deleteFavoriteByJobAndStudent(jobId, studentId));
    }

    // ==================== 学生 ====================

    @GetMapping("/student/list")
    public TableDataInfo studentList(CremsStudent student)
    {
        startPage();
        // 学生资料列表用于不同门户场景，必须先判断当前账号已有的业务档案。
        Long userId = getUserId();
        CremsStudent currentStudent = studentService.selectStudentByUserId(userId);
        if (currentStudent != null) {
            // 学生用户只能查看自己的信息
            student.setStudentId(currentStudent.getStudentId());
        } else if (isStudentRole()) {
            // 学生角色尚未创建个人资料时，不能回退为全表查询
            return getDataTable(List.of());
        } else if (!isCompanyRole()) {
            return getDataTable(List.of());
        }
        // 企业用户不过滤，可以查看所有学生（用于筛选投递候选人等场景）
        List<CremsStudent> list = studentService.selectStudentList(student);
        return getDataTable(list);
    }

    @GetMapping("/student/current")
    public AjaxResult getCurrentStudent()
    {
        // 获取当前登录学生的信息
        Long userId = getUserId();
        CremsStudent student = studentService.selectStudentByUserId(userId);
        if (student == null) {
            return AjaxResult.success((Object) null);
        }
        return success(student);
    }

    @GetMapping("/student/{studentId}")
    public AjaxResult getStudent(@PathVariable("studentId") Long studentId)
    {
        // 学生只能查看自己的信息
        Long currentStudentId = getCurrentStudentId();
        if (!currentStudentId.equals(studentId)) {
            return error("无权查看其他学生信息");
        }
        return success(studentService.selectStudentById(studentId));
    }

    @PostMapping("/student")
    public AjaxResult addStudent(@Validated @RequestBody CremsStudent student)
    {
        if (!isStudentRole()) {
            return error("当前用户不是学生角色");
        }
        if (StringUtils.isEmpty(student.getStudentName())) {
            return warn("请输入姓名");
        }
        if (StringUtils.isEmpty(student.getStudentNo())) {
            return warn("请输入学号");
        }

        Long userId = getUserId();
        CremsStudent existing = studentService.selectStudentByUserId(userId);
        student.setUserId(userId);
        if (existing != null) {
            // 个人资料采用“有则更新、无则创建”，避免同一账号产生多条学生档案。
            student.setStudentId(existing.getStudentId());
            student.setUpdateBy(getUsername());
            return studentService.updateStudent(student) > 0 ? success(student) : error();
        }

        student.setCreateBy(getUsername());
        if (StringUtils.isEmpty(student.getStatus())) {
            student.setStatus("0");
        }
        return studentService.insertStudent(student) > 0 ? success(student) : error();
    }

    @PutMapping("/student")
    public AjaxResult updateStudent(@Validated @RequestBody CremsStudent student)
    {
        // 学生只能修改自己的信息
        Long currentStudentId = getCurrentStudentId();
        student.setStudentId(currentStudentId);
        student.setUpdateBy(getUsername());
        return toAjax(studentService.updateStudent(student));
    }

    // ==================== 企业 ====================

    @GetMapping("/company/list")
    public TableDataInfo companyList(CremsCompany company)
    {
        startPage();
        // 企业资料列表和学生资料列表互为镜像，防止未建档账号回退成全表查询。
        Long userId = getUserId();
        CremsCompany currentCompany = companyService.selectCompanyByUserId(userId);
        if (currentCompany != null) {
            // 企业用户只能查看自己的信息
            company.setCompanyId(currentCompany.getCompanyId());
        } else if (isCompanyRole()) {
            // 企业角色尚未创建企业资料时，不能回退为全表查询
            return getDataTable(List.of());
        } else if (!isStudentRole()) {
            return getDataTable(List.of());
        }
        // 学生用户不过滤，可以查看所有企业（用于选择投递目标等场景）
        List<CremsCompany> list = companyService.selectCompanyList(company);
        return getDataTable(list);
    }

    @GetMapping("/company/current")
    public AjaxResult getCurrentCompany()
    {
        // 获取当前登录企业的信息
        Long userId = getUserId();
        CremsCompany company = companyService.selectCompanyByUserId(userId);
        if (company == null) {
            return AjaxResult.success((Object) null);
        }
        return success(company);
    }

    @GetMapping("/company/{companyId}")
    public AjaxResult getCompany(@PathVariable("companyId") Long companyId)
    {
        // 公开接口：任何登录用户都可以查看企业基本信息
        return success(companyService.selectCompanyById(companyId));
    }

    @PostMapping("/company")
    public AjaxResult addCompany(@Validated @RequestBody CremsCompany company)
    {
        if (!isCompanyRole()) {
            return error("当前用户不是企业角色");
        }
        if (StringUtils.isEmpty(company.getCompanyName())) {
            return warn("请输入企业名称");
        }
        if (StringUtils.isEmpty(company.getCompanyCode())) {
            return warn("请输入统一社会信用代码");
        }

        Long userId = getUserId();
        CremsCompany existing = companyService.selectCompanyByUserId(userId);
        company.setUserId(userId);
        if (existing != null) {
            // 企业资料也按 userId 维持一对一关系，重复提交时更新原记录。
            company.setCompanyId(existing.getCompanyId());
            company.setUpdateBy(getUsername());
            return companyService.updateCompany(company) > 0 ? success(company) : error();
        }

        company.setCreateBy(getUsername());
        if (StringUtils.isEmpty(company.getStatus())) {
            company.setStatus("0");
        }
        return companyService.insertCompany(company) > 0 ? success(company) : error();
    }

    @PutMapping("/company")
    public AjaxResult updateCompany(@Validated @RequestBody CremsCompany company)
    {
        // 企业只能修改自己的信息
        Long currentCompanyId = getCurrentCompanyId();
        company.setCompanyId(currentCompanyId);
        company.setUpdateBy(getUsername());
        return toAjax(companyService.updateCompany(company));
    }

    // ==================== 用户设置 ====================

    @GetMapping("/user/nickname")
    public AjaxResult getNickname()
    {
        Long userId = getUserId();
        // 通过 userId 查询 sys_user 的 nick_name
        String nickname = sysUserExtMapper.selectNickNameByUserId(userId);
        // 确保返回非 null 值
        return success(nickname != null ? nickname : "");
    }

    @PutMapping("/user/nickname")
    public AjaxResult updateNickname(@RequestBody java.util.Map<String, String> body)
    {
        String nickname = body.get("nickname");
        if (StringUtils.isEmpty(nickname)) {
            return warn("昵称不能为空");
        }
        if (nickname.length() > 30) {
            return warn("昵称不能超过30个字符");
        }
        Long userId = getUserId();
        int rows = sysUserExtMapper.updateNickName(userId, nickname);
        if (rows > 0) {
            return success();
        }
        return error("修改失败");
    }

    // ==================== 统计 ====================

    @GetMapping("/statistics/overview")
    public AjaxResult statisticsOverview()
    {
        return success(statisticsService.selectOverview());
    }
}
