package com.ruoyi.crems.mapper;

import java.util.List;
import com.ruoyi.crems.domain.CremsApplication;

/**
 * 简历投递Mapper接口
 *
 * @author crems
 */
public interface CremsApplicationMapper
{
    public CremsApplication selectApplicationById(Long applicationId);

    public List<CremsApplication> selectApplicationList(CremsApplication application);

    public int insertApplication(CremsApplication application);

    public int updateApplication(CremsApplication application);

    public int deleteApplicationById(Long applicationId);

    public int deleteApplicationByIds(Long[] applicationIds);
}
