package com.crems.crems.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.crems.crems.domain.CremsStatisticsOverview;
import com.crems.crems.domain.CremsJobStatistics;
import com.crems.crems.domain.CremsApplicationStatistics;
import com.crems.crems.domain.CremsInterviewStatistics;
import com.crems.crems.mapper.CremsStatisticsMapper;
import com.crems.crems.service.ICremsStatisticsService;

/**
 * 统计分析Service业务层处理
 *
 * @author crems
 */
@Service
public class CremsStatisticsServiceImpl implements ICremsStatisticsService
{
    @Autowired
    private CremsStatisticsMapper statisticsMapper;

    @Override
    public CremsStatisticsOverview selectOverview()
    {
        CremsStatisticsOverview overview = new CremsStatisticsOverview();
        // 优化：使用单条SQL查询所有统计数据，减少数据库往返
        Map<String, Long> counts = statisticsMapper.selectOverviewCounts();
        if (counts != null) {
            overview.setCompanyCount(counts.getOrDefault("companyCount", 0L));
            overview.setCertifiedCompanyCount(counts.getOrDefault("certifiedCompanyCount", 0L));
            overview.setStudentCount(counts.getOrDefault("studentCount", 0L));
            overview.setJobCount(counts.getOrDefault("jobCount", 0L));
            overview.setPublishedJobCount(counts.getOrDefault("publishedJobCount", 0L));
            overview.setApplicationCount(counts.getOrDefault("applicationCount", 0L));
            overview.setInterviewCount(counts.getOrDefault("interviewCount", 0L));
        }
        return overview;
    }

    @Override
    public List<CremsJobStatistics> selectJobStatisticsByType()
    {
        return statisticsMapper.selectJobCountByType();
    }

    @Override
    public List<CremsJobStatistics> selectJobStatisticsByCity()
    {
        return statisticsMapper.selectJobCountByCity();
    }

    @Override
    public List<CremsJobStatistics> selectJobStatisticsByIndustry()
    {
        return statisticsMapper.selectJobCountByIndustry();
    }

    @Override
    public List<CremsApplicationStatistics> selectApplicationStatisticsByStatus()
    {
        return statisticsMapper.selectApplicationCountByStatus();
    }

    @Override
    public List<CremsInterviewStatistics> selectInterviewStatisticsByType()
    {
        return statisticsMapper.selectInterviewCountByType();
    }

    @Override
    public List<CremsInterviewStatistics> selectInterviewStatisticsByResult()
    {
        return statisticsMapper.selectInterviewCountByResult();
    }
}