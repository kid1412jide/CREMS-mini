package com.crems.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.crems.crems.domain.CremsApplication;
import com.crems.crems.domain.CremsInterview;
import com.crems.crems.mapper.CremsApplicationMapper;
import com.crems.crems.mapper.CremsInterviewMapper;
import com.crems.crems.mapper.CremsJobMapper;
import com.crems.crems.service.ICremsApplicationService;

/**
 * 简历投递Service业务层处理
 *
 * @author crems
 */
@Service
public class CremsApplicationServiceImpl implements ICremsApplicationService
{
    @Autowired
    private CremsApplicationMapper applicationMapper;
    @Autowired
    private CremsJobMapper jobMapper;
    @Autowired
    private CremsInterviewMapper interviewMapper;

    @Override
    public CremsApplication selectApplicationById(Long applicationId)
    {
        return applicationMapper.selectApplicationById(applicationId);
    }

    @Override
    public List<CremsApplication> selectApplicationList(CremsApplication application)
    {
        return applicationMapper.selectApplicationList(application);
    }

    @Override
    @Transactional
    public int insertApplication(CremsApplication application)
    {
        return applicationMapper.insertApplication(application);
    }

    @Override
    @Transactional
    public int applyForJob(CremsApplication application)
    {
        int rows = applicationMapper.insertApplication(application);
        if (rows > 0 && application.getJobId() != null) {
            jobMapper.syncApplyCount(application.getJobId());
        }
        return rows;
    }

    @Override
    @Transactional
    public int updateApplication(CremsApplication application)
    {
        return applicationMapper.updateApplication(application);
    }

    @Override
    @Transactional
    public int updateApplicationStatusIfCurrent(Long applicationId, String currentStatus, String newStatus, String updateBy)
    {
        return applicationMapper.updateApplicationStatusIfCurrent(applicationId, currentStatus, newStatus, updateBy);
    }

    @Override
    @Transactional
    public int deleteApplicationByIds(Long[] applicationIds)
    {
        java.util.Set<Long> jobIds = new java.util.HashSet<>();
        // 先删除关联的面试记录
        for (Long applicationId : applicationIds) {
            CremsApplication application = applicationMapper.selectApplicationById(applicationId);
            if (application != null && application.getJobId() != null) {
                jobIds.add(application.getJobId());
            }
            CremsInterview query = new CremsInterview();
            query.setApplicationId(applicationId);
            java.util.List<CremsInterview> interviews = interviewMapper.selectInterviewList(query);
            for (CremsInterview interview : interviews) {
                interviewMapper.deleteInterviewById(interview.getInterviewId());
            }
        }
        int rows = applicationMapper.deleteApplicationByIds(applicationIds);
        for (Long jobId : jobIds) {
            jobMapper.syncApplyCount(jobId);
        }
        return rows;
    }

    @Override
    @Transactional
    public int deleteApplicationById(Long applicationId)
    {
        CremsApplication application = applicationMapper.selectApplicationById(applicationId);
        // 先删除关联的面试记录
        CremsInterview query = new CremsInterview();
        query.setApplicationId(applicationId);
        java.util.List<CremsInterview> interviews = interviewMapper.selectInterviewList(query);
        for (CremsInterview interview : interviews) {
            interviewMapper.deleteInterviewById(interview.getInterviewId());
        }
        int rows = applicationMapper.deleteApplicationById(applicationId);
        if (application != null && application.getJobId() != null) {
            jobMapper.syncApplyCount(application.getJobId());
        }
        return rows;
    }
}
