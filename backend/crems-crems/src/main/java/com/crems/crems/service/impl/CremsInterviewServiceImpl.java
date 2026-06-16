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
        // 面试邀请必须基于一条已有投递，后续职位/学生/企业信息都从投递记录派生。
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
        // 只有初筛通过后才能进入面试邀请，保持投递状态机单向推进。
        if (!"2".equals(application.getStatus())) {
            throw new ServiceException("只有初筛通过的投递才能发送面试邀请");
        }

        // CAS 条件更新避免多人同时操作时重复发送邀请或覆盖最新状态。
        int updated = applicationMapper.updateApplicationStatusIfCurrent(
                application.getApplicationId(), "2", "3", username);
        if (updated != 1) {
            throw new ServiceException("投递状态已变化，请刷新后重试");
        }

        // 敏感归属字段以后端查询结果为准，不接收前端传入的 jobId/studentId/companyId。
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
        for (Long interviewId : interviewIds) {
            revertApplicationStatus(interviewId);
        }
        return interviewMapper.deleteInterviewByIds(interviewIds);
    }

    @Override
    @Transactional
    public int deleteInterviewById(Long interviewId)
    {
        revertApplicationStatus(interviewId);
        return interviewMapper.deleteInterviewById(interviewId);
    }

    /**
     * 删除面试时，将关联的投递状态从"面试邀请"(3)回退到"初筛通过"(2)
     */
    private void revertApplicationStatus(Long interviewId) {
        CremsInterview interview = interviewMapper.selectInterviewById(interviewId);
        if (interview != null && interview.getApplicationId() != null) {
            CremsApplication application = applicationMapper.selectApplicationById(interview.getApplicationId());
            if (application != null && "3".equals(application.getStatus())) {
                // 仅回退仍停留在“面试邀请”的投递，避免覆盖已录用或已拒绝的后续处理。
                applicationMapper.updateApplicationStatusIfCurrent(
                        application.getApplicationId(), "3", "2", interview.getUpdateBy());
            }
        }
    }
}
