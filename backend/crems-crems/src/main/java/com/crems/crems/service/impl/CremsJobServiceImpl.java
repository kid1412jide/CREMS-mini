package com.crems.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.crems.crems.domain.CremsJob;
import com.crems.crems.mapper.CremsJobMapper;
import com.crems.crems.service.ICremsJobService;

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
    @Transactional
    public int insertJob(CremsJob job)
    {
        return jobMapper.insertJob(job);
    }

    @Override
    @Transactional
    public int updateJob(CremsJob job)
    {
        return jobMapper.updateJob(job);
    }

    @Override
    @Transactional
    public int incrementViewCount(Long jobId)
    {
        return jobMapper.incrementViewCount(jobId);
    }

    @Override
    @Transactional
    public int incrementApplyCount(Long jobId)
    {
        return jobMapper.incrementApplyCount(jobId);
    }

    @Override
    @Transactional
    public int deleteJobByIds(Long[] jobIds)
    {
        return jobMapper.deleteJobByIds(jobIds);
    }

    @Override
    @Transactional
    public int deleteJobById(Long jobId)
    {
        return jobMapper.deleteJobById(jobId);
    }
}
