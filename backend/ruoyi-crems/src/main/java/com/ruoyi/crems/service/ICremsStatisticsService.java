package com.ruoyi.crems.service;

import java.util.List;
import com.ruoyi.crems.domain.CremsStatisticsOverview;
import com.ruoyi.crems.domain.CremsJobStatistics;
import com.ruoyi.crems.domain.CremsApplicationStatistics;
import com.ruoyi.crems.domain.CremsInterviewStatistics;

/**
 * 统计分析Service接口
 *
 * @author crems
 */
public interface ICremsStatisticsService
{
    /** 获取数据概览 */
    public CremsStatisticsOverview selectOverview();

    /** 按职位类型统计 */
    public List<CremsJobStatistics> selectJobStatisticsByType();

    /** 按城市统计 */
    public List<CremsJobStatistics> selectJobStatisticsByCity();

    /** 按行业统计 */
    public List<CremsJobStatistics> selectJobStatisticsByIndustry();

    /** 按投递状态统计 */
    public List<CremsApplicationStatistics> selectApplicationStatisticsByStatus();

    /** 按面试类型统计 */
    public List<CremsInterviewStatistics> selectInterviewStatisticsByType();

    /** 按面试结果统计 */
    public List<CremsInterviewStatistics> selectInterviewStatisticsByResult();
}