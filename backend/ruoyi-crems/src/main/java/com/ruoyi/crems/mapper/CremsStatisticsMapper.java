package com.ruoyi.crems.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.crems.domain.CremsJobStatistics;
import com.ruoyi.crems.domain.CremsApplicationStatistics;
import com.ruoyi.crems.domain.CremsInterviewStatistics;

/**
 * 统计分析Mapper接口
 *
 * @author crems
 */
public interface CremsStatisticsMapper
{
    /** 查询企业总数 */
    public Long selectCompanyCount();

    /** 查询已认证企业数 */
    public Long selectCertifiedCompanyCount();

    /** 查询学生总数 */
    public Long selectStudentCount();

    /** 查询职位总数 */
    public Long selectJobCount();

    /** 查询已发布职位数 */
    public Long selectPublishedJobCount();

    /** 查询投递总数 */
    public Long selectApplicationCount();

    /** 查询面试总数 */
    public Long selectInterviewCount();

    /** 一次性查询所有统计数据 */
    public Map<String, Long> selectOverviewCounts();

    /** 按职位类型统计 */
    public List<CremsJobStatistics> selectJobCountByType();

    /** 按城市统计职位 */
    public List<CremsJobStatistics> selectJobCountByCity();

    /** 按行业统计职位 */
    public List<CremsJobStatistics> selectJobCountByIndustry();

    /** 按投递状态统计 */
    public List<CremsApplicationStatistics> selectApplicationCountByStatus();

    /** 按面试类型统计 */
    public List<CremsInterviewStatistics> selectInterviewCountByType();

    /** 按面试结果统计 */
    public List<CremsInterviewStatistics> selectInterviewCountByResult();
}