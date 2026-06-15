package com.crems.crems.service;

import java.util.List;
import com.crems.crems.domain.CremsApplication;

/**
 * 简历投递Service接口
 *
 * @author crems
 */
public interface ICremsApplicationService
{
    public CremsApplication selectApplicationById(Long applicationId);

    public List<CremsApplication> selectApplicationList(CremsApplication application);

    public int insertApplication(CremsApplication application);

    /**
     * 投递职位（在同一事务中同时更新投递计数）
     */
    public int applyForJob(CremsApplication application);

    public int updateApplication(CremsApplication application);

    /**
     * CAS 更新投递状态（仅当当前状态匹配时才更新）
     */
    public int updateApplicationStatusIfCurrent(Long applicationId, String currentStatus, String newStatus, String updateBy);

    public int deleteApplicationByIds(Long[] applicationIds);

    public int deleteApplicationById(Long applicationId);
}
