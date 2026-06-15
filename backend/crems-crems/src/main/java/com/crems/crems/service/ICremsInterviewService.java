package com.crems.crems.service;

import java.util.List;
import com.crems.crems.domain.CremsInterview;

/**
 * 面试安排Service接口
 *
 * @author crems
 */
public interface ICremsInterviewService
{
    public CremsInterview selectInterviewById(Long interviewId);

    public List<CremsInterview> selectInterviewList(CremsInterview interview);

    public int insertInterview(CremsInterview interview);

    public int inviteInterview(CremsInterview interview, Long companyId, String username);

    public int updateInterview(CremsInterview interview);

    public int deleteInterviewByIds(Long[] interviewIds);

    public int deleteInterviewById(Long interviewId);
}
