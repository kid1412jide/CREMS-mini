package com.ruoyi.crems.service;

import java.util.List;
import com.ruoyi.crems.domain.CremsInterview;

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

    public int updateInterview(CremsInterview interview);

    public int deleteInterviewByIds(Long[] interviewIds);

    public int deleteInterviewById(Long interviewId);
}
