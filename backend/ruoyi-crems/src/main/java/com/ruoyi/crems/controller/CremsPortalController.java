package com.ruoyi.crems.controller;

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
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.crems.domain.CremsApplication;
import com.ruoyi.crems.domain.CremsCompany;
import com.ruoyi.crems.domain.CremsFavorite;
import com.ruoyi.crems.domain.CremsInterview;
import com.ruoyi.crems.domain.CremsJob;
import com.ruoyi.crems.domain.CremsStudent;
import com.ruoyi.crems.service.ICremsApplicationService;
import com.ruoyi.crems.service.ICremsCompanyService;
import com.ruoyi.crems.service.ICremsFavoriteService;
import com.ruoyi.crems.service.ICremsInterviewService;
import com.ruoyi.crems.service.ICremsJobService;
import com.ruoyi.crems.service.ICremsStatisticsService;
import com.ruoyi.crems.service.ICremsStudentService;

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
        List<CremsJob> list = jobService.selectJobList(job);
        return getDataTable(list);
    }

    @GetMapping("/job/{jobId}")
    public AjaxResult getJob(@PathVariable("jobId") Long jobId)
    {
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
        // 根据当前用户角色过滤数据
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
            }
        }
        List<CremsApplication> list = applicationService.selectApplicationList(application);
        return getDataTable(list);
    }

    @PostMapping("/application")
    public AjaxResult addApplication(@RequestBody CremsApplication application)
    {
        application.setCreateBy(getUsername());
        // 学生只能以自己的身份投递
        Long studentId = getCurrentStudentId();
        application.setStudentId(studentId);
        // 获取职位信息以设置companyId
        CremsJob job = jobService.selectJobById(application.getJobId());
        if (job == null) {
            return error("职位不存在");
        }
        application.setCompanyId(job.getCompanyId());
        int rows = applicationService.insertApplication(application);
        if (rows > 0) {
            // 投递成功后，职位投递数+1
            jobService.incrementApplyCount(application.getJobId());
            return success();
        }
        return error("投递失败，可能已经投递过该职位");
    }

    @PutMapping("/application")
    public AjaxResult updateApplication(@RequestBody CremsApplication application)
    {
        application.setUpdateBy(getUsername());
        // 企业只能更新自己公司职位的投递状态
        Long companyId = getCurrentCompanyId();
        CremsApplication existing = applicationService.selectApplicationById(application.getApplicationId());
        if (existing == null || !companyId.equals(existing.getCompanyId())) {
            return error("无权修改此投递记录");
        }
        return toAjax(applicationService.updateApplication(application));
    }

    // ==================== 面试 ====================

    @GetMapping("/interview/list")
    public TableDataInfo interviewList(CremsInterview interview)
    {
        startPage();
        // 根据当前用户角色过滤数据
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
            }
        }
        List<CremsInterview> list = interviewService.selectInterviewList(interview);
        return getDataTable(list);
    }

    @PostMapping("/interview")
    public AjaxResult addInterview(@RequestBody CremsInterview interview)
    {
        interview.setCreateBy(getUsername());
        // 企业只能为自己的公司安排面试
        Long companyId = getCurrentCompanyId();
        interview.setCompanyId(companyId);
        return toAjax(interviewService.insertInterview(interview));
    }

    @PutMapping("/interview")
    public AjaxResult updateInterview(@RequestBody CremsInterview interview)
    {
        interview.setUpdateBy(getUsername());
        // 企业只能更新自己公司的面试
        Long companyId = getCurrentCompanyId();
        CremsInterview existing = interviewService.selectInterviewById(interview.getInterviewId());
        if (existing == null || !companyId.equals(existing.getCompanyId())) {
            return error("无权修改此面试记录");
        }
        return toAjax(interviewService.updateInterview(interview));
    }

    // ==================== 收藏 ====================

    @GetMapping("/favorite/list")
    public TableDataInfo favoriteList(CremsFavorite favorite)
    {
        startPage();
        // 学生只能查看自己的收藏
        Long studentId = getCurrentStudentId();
        favorite.setStudentId(studentId);
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
        // 根据当前用户角色过滤数据
        Long userId = getUserId();
        CremsStudent currentStudent = studentService.selectStudentByUserId(userId);
        if (currentStudent != null) {
            // 学生用户只能查看自己的信息
            student.setStudentId(currentStudent.getStudentId());
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
            return error("未找到当前用户的学生信息");
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
        // 根据当前用户角色过滤数据
        Long userId = getUserId();
        CremsCompany currentCompany = companyService.selectCompanyByUserId(userId);
        if (currentCompany != null) {
            // 企业用户只能查看自己的信息
            company.setCompanyId(currentCompany.getCompanyId());
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
            return error("未找到当前用户的企业信息");
        }
        return success(company);
    }

    @GetMapping("/company/{companyId}")
    public AjaxResult getCompany(@PathVariable("companyId") Long companyId)
    {
        // 公开接口：任何登录用户都可以查看企业基本信息
        return success(companyService.selectCompanyById(companyId));
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

    // ==================== 统计 ====================

    @GetMapping("/statistics/overview")
    public AjaxResult statisticsOverview()
    {
        return success(statisticsService.selectOverview());
    }
}
