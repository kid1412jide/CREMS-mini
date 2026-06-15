package com.crems.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.crems.common.exception.ServiceException;
import com.crems.common.utils.StringUtils;
import com.crems.crems.domain.CremsApplication;
import com.crems.crems.domain.CremsInterview;
import com.crems.crems.mapper.CremsApplicationMapper;
import com.crems.crems.mapper.CremsInterviewMapper;
import com.crems.crems.service.ICremsInterviewService;

/**
 * 面试安排Service业务层处理
 *
 * @author crems
 */
@Service
public class CremsInterviewServiceImpl implements ICremsInterviewService
{
    @Autowired
    private CremsInterviewMapper interviewMapper;
    @Autowired
    private CremsApplicationMapper applicationMapper;

    @Override
    public CremsInterview selectInterviewById(Long interviewId)
    {
        return interviewMapper.selectInterviewById(interviewId);
    }

    @Override
    public List<CremsInterview> selectInterviewList(CremsInterview interview)
    {
        return interviewMapper.selectInterviewList(interview);
    }

    @Override
    @Transactional
    public int insertInterview(CremsInterview interview)
    {
        return interviewMapper.insertInterview(interview);
    }

    @Override
    @Transactional
    public int inviteInterview(CremsInterview interview, Long companyId, String username)
    {
        if (interview.getApplicationId() == null) {
            throw new ServiceException("投递记录不能为空");
        }
        if (StringUtils.isEmpty(interview.getInterviewType())
                || StringUtils.isEmpty(interview.getInterviewMethod())
                || interview.getInterviewTime() == null) {
            throw new ServiceException("请完整填写面试类型、方式和时间");
        }

        CremsApplication application = applicationMapper.selectApplicationById(interview.getApplicationId());
        if (application == null || !companyId.equals(application.getCompanyId())) {
            throw new ServiceException("无权为其他企业的投递安排面试");
        }
        if (!"2".equals(application.getStatus())) {
            throw new ServiceException("只有初筛通过的投递才能发送面试邀请");
        }

        int updated = applicationMapper.updateApplicationStatusIfCurrent(
                application.getApplicationId(), "2", "3", username);
        if (updated != 1) {
            throw new ServiceException("投递状态已变化，请刷新后重试");
        }

        interview.setJobId(application.getJobId());
        interview.setStudentId(application.getStudentId());
        interview.setCompanyId(companyId);
        interview.setStatus("0");
        interview.setCreateBy(username);

        int inserted = interviewMapper.insertInterview(interview);
        if (inserted != 1) {
            throw new ServiceException("面试邀请创建失败");
        }
        return inserted;
    }

    @Override
    @Transactional
    public int updateInterview(CremsInterview interview)
    {
        return interviewMapper.updateInterview(interview);
    }

    @Override
    @Transactional
    public int deleteInterviewByIds(Long[] interviewIds)
    {
        return interviewMapper.deleteInterviewByIds(interviewIds);
    }

    @Override
    @Transactional
    public int deleteInterviewById(Long interviewId)
    {
        return interviewMapper.deleteInterviewById(interviewId);
    }
}
