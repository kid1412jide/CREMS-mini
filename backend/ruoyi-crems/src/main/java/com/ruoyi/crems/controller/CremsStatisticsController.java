package com.ruoyi.crems.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.crems.domain.CremsStatisticsOverview;
import com.ruoyi.crems.domain.CremsJobStatistics;
import com.ruoyi.crems.domain.CremsApplicationStatistics;
import com.ruoyi.crems.domain.CremsInterviewStatistics;
import com.ruoyi.crems.service.ICremsStatisticsService;

/**
 * 统计分析Controller
 *
 * @author crems
 */
@RestController
@RequestMapping("/crems/statistics")
public class CremsStatisticsController extends BaseController
{
    @Autowired
    private ICremsStatisticsService statisticsService;

    /**
     * 获取数据概览
     */
    @PreAuthorize("@ss.hasPermi('crems:statistics:overview')")
    @GetMapping("/overview")
    public AjaxResult overview()
    {
        return success(statisticsService.selectOverview());
    }

    /**
     * 按职位类型统计
     */
    @PreAuthorize("@ss.hasPermi('crems:statistics:job')")
    @GetMapping("/job/type")
    public AjaxResult jobByType()
    {
        return success(statisticsService.selectJobStatisticsByType());
    }

    /**
     * 按城市统计职位
     */
    @PreAuthorize("@ss.hasPermi('crems:statistics:job')")
    @GetMapping("/job/city")
    public AjaxResult jobByCity()
    {
        return success(statisticsService.selectJobStatisticsByCity());
    }

    /**
     * 按行业统计职位
     */
    @PreAuthorize("@ss.hasPermi('crems:statistics:job')")
    @GetMapping("/job/industry")
    public AjaxResult jobByIndustry()
    {
        return success(statisticsService.selectJobStatisticsByIndustry());
    }

    /**
     * 按投递状态统计
     */
    @PreAuthorize("@ss.hasPermi('crems:statistics:application')")
    @GetMapping("/application/status")
    public AjaxResult applicationByStatus()
    {
        return success(statisticsService.selectApplicationStatisticsByStatus());
    }

    /**
     * 按面试类型统计
     */
    @PreAuthorize("@ss.hasPermi('crems:statistics:interview')")
    @GetMapping("/interview/type")
    public AjaxResult interviewByType()
    {
        return success(statisticsService.selectInterviewStatisticsByType());
    }

    /**
     * 按面试结果统计
     */
    @PreAuthorize("@ss.hasPermi('crems:statistics:interview')")
    @GetMapping("/interview/result")
    public AjaxResult interviewByResult()
    {
        return success(statisticsService.selectInterviewStatisticsByResult());
    }
}