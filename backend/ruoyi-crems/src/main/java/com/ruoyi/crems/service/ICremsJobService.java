package com.ruoyi.crems.service;

import java.util.List;
import com.ruoyi.crems.domain.CremsJob;

/**
 * 职位信息Service接口
 *
 * @author crems
 */
public interface ICremsJobService
{
    public CremsJob selectJobById(Long jobId);

    public List<CremsJob> selectJobList(CremsJob job);

    public int insertJob(CremsJob job);

    public int updateJob(CremsJob job);

    /** 浏览量+1 */
    public int incrementViewCount(Long jobId);

    /** 投递数+1 */
    public int incrementApplyCount(Long jobId);

    public int deleteJobByIds(Long[] jobIds);

    public int deleteJobById(Long jobId);
}
