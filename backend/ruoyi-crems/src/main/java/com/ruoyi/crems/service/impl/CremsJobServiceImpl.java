package com.ruoyi.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.crems.domain.CremsJob;
import com.ruoyi.crems.mapper.CremsJobMapper;
import com.ruoyi.crems.service.ICremsJobService;

/**
 * 职位信息Service业务层处理
 *
 * @author crems
 */
@Service
public class CremsJobServiceImpl implements ICremsJobService
{
    @Autowired
    private CremsJobMapper jobMapper;

    @Override
    public CremsJob selectJobById(Long jobId)
    {
        return jobMapper.selectJobById(jobId);
    }

    @Override
    public List<CremsJob> selectJobList(CremsJob job)
    {
        return jobMapper.selectJobList(job);
    }

    @Override
    public int insertJob(CremsJob job)
    {
        return jobMapper.insertJob(job);
    }

    @Override
    public int updateJob(CremsJob job)
    {
        return jobMapper.updateJob(job);
    }

    @Override
    public int incrementViewCount(Long jobId)
    {
        return jobMapper.incrementViewCount(jobId);
    }

    @Override
    public int incrementApplyCount(Long jobId)
    {
        return jobMapper.incrementApplyCount(jobId);
    }

    @Override
    public int deleteJobByIds(Long[] jobIds)
    {
        return jobMapper.deleteJobByIds(jobIds);
    }

    @Override
    public int deleteJobById(Long jobId)
    {
        return jobMapper.deleteJobById(jobId);
    }
}
