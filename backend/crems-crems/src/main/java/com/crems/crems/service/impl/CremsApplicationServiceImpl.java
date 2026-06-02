package com.crems.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.crems.crems.domain.CremsApplication;
import com.crems.crems.mapper.CremsApplicationMapper;
import com.crems.crems.service.ICremsApplicationService;

/**
 * 简历投递Service业务层处理
 *
 * @author crems
 */
@Service
public class CremsApplicationServiceImpl implements ICremsApplicationService
{
    @Autowired
    private CremsApplicationMapper applicationMapper;

    @Override
    public CremsApplication selectApplicationById(Long applicationId)
    {
        return applicationMapper.selectApplicationById(applicationId);
    }

    @Override
    public List<CremsApplication> selectApplicationList(CremsApplication application)
    {
        return applicationMapper.selectApplicationList(application);
    }

    @Override
    public int insertApplication(CremsApplication application)
    {
        return applicationMapper.insertApplication(application);
    }

    @Override
    public int updateApplication(CremsApplication application)
    {
        return applicationMapper.updateApplication(application);
    }

    @Override
    public int deleteApplicationByIds(Long[] applicationIds)
    {
        return applicationMapper.deleteApplicationByIds(applicationIds);
    }

    @Override
    public int deleteApplicationById(Long applicationId)
    {
        return applicationMapper.deleteApplicationById(applicationId);
    }
}
