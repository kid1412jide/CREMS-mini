package com.ruoyi.crems.mapper;

import java.util.List;
import com.ruoyi.crems.domain.CremsInterview;

/**
 * 面试安排Mapper接口
 *
 * @author crems
 */
public interface CremsInterviewMapper
{
    public CremsInterview selectInterviewById(Long interviewId);

    public List<CremsInterview> selectInterviewList(CremsInterview interview);

    public int insertInterview(CremsInterview interview);

    public int updateInterview(CremsInterview interview);

    public int deleteInterviewById(Long interviewId);

    public int deleteInterviewByIds(Long[] interviewIds);
}
