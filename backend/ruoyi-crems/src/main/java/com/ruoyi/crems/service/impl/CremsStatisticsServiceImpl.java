package com.ruoyi.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.crems.domain.CremsStatisticsOverview;
import com.ruoyi.crems.domain.CremsJobStatistics;
import com.ruoyi.crems.domain.CremsApplicationStatistics;
import com.ruoyi.crems.domain.CremsInterviewStatistics;
import com.ruoyi.crems.mapper.CremsStatisticsMapper;
import com.ruoyi.crems.service.ICremsStatisticsService;

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
        overview.setCompanyCount(statisticsMapper.selectCompanyCount());
        overview.setCertifiedCompanyCount(statisticsMapper.selectCertifiedCompanyCount());
        overview.setStudentCount(statisticsMapper.selectStudentCount());
        overview.setJobCount(statisticsMapper.selectJobCount());
        overview.setPublishedJobCount(statisticsMapper.selectPublishedJobCount());
        overview.setApplicationCount(statisticsMapper.selectApplicationCount());
        overview.setInterviewCount(statisticsMapper.selectInterviewCount());
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