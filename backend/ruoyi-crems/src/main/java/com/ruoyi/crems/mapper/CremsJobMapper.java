package com.ruoyi.crems.mapper;

import java.util.List;
import com.ruoyi.crems.domain.CremsJob;

/**
 * 职位信息Mapper接口
 *
 * @author crems
 */
public interface CremsJobMapper
{
    public CremsJob selectJobById(Long jobId);

    public List<CremsJob> selectJobList(CremsJob job);

    public int insertJob(CremsJob job);

    public int updateJob(CremsJob job);

    /** 浏览量+1 */
    public int incrementViewCount(Long jobId);

    /** 投递数+1 */
    public int incrementApplyCount(Long jobId);

    public int deleteJobById(Long jobId);

    public int deleteJobByIds(Long[] jobIds);
}
