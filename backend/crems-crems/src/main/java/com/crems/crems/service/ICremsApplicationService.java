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

    public int updateApplication(CremsApplication application);

    public int deleteApplicationByIds(Long[] applicationIds);

    public int deleteApplicationById(Long applicationId);
}
