package com.crems.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.crems.crems.domain.CremsJob;
import com.crems.crems.domain.CremsInterview;
import com.crems.crems.domain.CremsApplication;
import com.crems.crems.mapper.CremsJobMapper;
import com.crems.crems.mapper.CremsInterviewMapper;
import com.crems.crems.mapper.CremsApplicationMapper;
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
    @Autowired
    private CremsInterviewMapper interviewMapper;
    @Autowired
    private CremsApplicationMapper applicationMapper;

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
    public int syncApplyCount(Long jobId)
    {
        return jobMapper.syncApplyCount(jobId);
    }

    @Override
    @Transactional
    public int deleteJobByIds(Long[] jobIds)
    {
        for (Long jobId : jobIds) {
            deleteJobCascade(jobId);
        }
        return jobMapper.deleteJobByIds(jobIds);
    }

    @Override
    @Transactional
    public int deleteJobById(Long jobId)
    {
        deleteJobCascade(jobId);
        return jobMapper.deleteJobById(jobId);
    }

    /**
     * 级联删除职位关联数据：interview -> application
     */
    private void deleteJobCascade(Long jobId) {
        // 删除面试
        CremsInterview interviewQuery = new CremsInterview();
        interviewQuery.setJobId(jobId);
        java.util.List<CremsInterview> interviews = interviewMapper.selectInterviewList(interviewQuery);
        for (CremsInterview interview : interviews) {
            interviewMapper.deleteInterviewById(interview.getInterviewId());
        }

        // 删除投递
        CremsApplication appQuery = new CremsApplication();
        appQuery.setJobId(jobId);
        java.util.List<CremsApplication> applications = applicationMapper.selectApplicationList(appQuery);
        for (CremsApplication app : applications) {
            applicationMapper.deleteApplicationById(app.getApplicationId());
        }
    }
}
