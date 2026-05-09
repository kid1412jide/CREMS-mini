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
        return success(jobService.selectJobById(jobId));
    }

    @PostMapping("/job")
    public AjaxResult addJob(@Validated @RequestBody CremsJob job)
    {
        job.setCreateBy(getUsername());
        return toAjax(jobService.insertJob(job));
    }

    @PutMapping("/job")
    public AjaxResult updateJob(@Validated @RequestBody CremsJob job)
    {
        job.setUpdateBy(getUsername());
        return toAjax(jobService.updateJob(job));
    }

    @DeleteMapping("/job/{jobId}")
    public AjaxResult deleteJob(@PathVariable Long jobId)
    {
        return toAjax(jobService.deleteJobById(jobId));
    }

    // ==================== 投递 ====================

    @GetMapping("/application/list")
    public TableDataInfo applicationList(CremsApplication application)
    {
        startPage();
        List<CremsApplication> list = applicationService.selectApplicationList(application);
        return getDataTable(list);
    }

    @PostMapping("/application")
    public AjaxResult addApplication(@RequestBody CremsApplication application)
    {
        application.setCreateBy(getUsername());
        return toAjax(applicationService.insertApplication(application));
    }

    @PutMapping("/application")
    public AjaxResult updateApplication(@RequestBody CremsApplication application)
    {
        application.setUpdateBy(getUsername());
        return toAjax(applicationService.updateApplication(application));
    }

    // ==================== 面试 ====================

    @GetMapping("/interview/list")
    public TableDataInfo interviewList(CremsInterview interview)
    {
        startPage();
        List<CremsInterview> list = interviewService.selectInterviewList(interview);
        return getDataTable(list);
    }

    @PostMapping("/interview")
    public AjaxResult addInterview(@RequestBody CremsInterview interview)
    {
        interview.setCreateBy(getUsername());
        return toAjax(interviewService.insertInterview(interview));
    }

    @PutMapping("/interview")
    public AjaxResult updateInterview(@RequestBody CremsInterview interview)
    {
        interview.setUpdateBy(getUsername());
        return toAjax(interviewService.updateInterview(interview));
    }

    // ==================== 收藏 ====================

    @GetMapping("/favorite/list")
    public TableDataInfo favoriteList(CremsFavorite favorite)
    {
        startPage();
        List<CremsFavorite> list = favoriteService.selectFavoriteList(favorite);
        return getDataTable(list);
    }

    @PostMapping("/favorite")
    public AjaxResult addFavorite(@Validated @RequestBody CremsFavorite favorite)
    {
        return toAjax(favoriteService.insertFavorite(favorite));
    }

    @DeleteMapping("/favorite/{favoriteId}")
    public AjaxResult deleteFavorite(@PathVariable Long favoriteId)
    {
        return toAjax(favoriteService.deleteFavoriteById(favoriteId));
    }

    @DeleteMapping("/favorite/job/{jobId}/student/{studentId}")
    public AjaxResult deleteFavoriteByJobAndStudent(@PathVariable Long jobId, @PathVariable Long studentId)
    {
        return toAjax(favoriteService.deleteFavoriteByJobAndStudent(jobId, studentId));
    }

    // ==================== 学生 ====================

    @GetMapping("/student/list")
    public TableDataInfo studentList(CremsStudent student)
    {
        startPage();
        List<CremsStudent> list = studentService.selectStudentList(student);
        return getDataTable(list);
    }

    @GetMapping("/student/{studentId}")
    public AjaxResult getStudent(@PathVariable("studentId") Long studentId)
    {
        return success(studentService.selectStudentById(studentId));
    }

    @PutMapping("/student")
    public AjaxResult updateStudent(@Validated @RequestBody CremsStudent student)
    {
        student.setUpdateBy(getUsername());
        return toAjax(studentService.updateStudent(student));
    }

    // ==================== 企业 ====================

    @GetMapping("/company/list")
    public TableDataInfo companyList(CremsCompany company)
    {
        startPage();
        List<CremsCompany> list = companyService.selectCompanyList(company);
        return getDataTable(list);
    }

    @GetMapping("/company/{companyId}")
    public AjaxResult getCompany(@PathVariable("companyId") Long companyId)
    {
        return success(companyService.selectCompanyById(companyId));
    }

    @PutMapping("/company")
    public AjaxResult updateCompany(@Validated @RequestBody CremsCompany company)
    {
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
