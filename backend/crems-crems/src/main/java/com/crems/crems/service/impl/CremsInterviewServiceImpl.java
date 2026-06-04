package com.crems.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.crems.crems.domain.CremsInterview;
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
