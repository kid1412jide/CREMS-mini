package com.ruoyi.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.crems.domain.CremsCompany;
import com.ruoyi.crems.mapper.CremsCompanyMapper;
import com.ruoyi.crems.service.ICremsCompanyService;

/**
 * 企业信息Service业务层处理
 *
 * @author crems
 */
@Service
public class CremsCompanyServiceImpl implements ICremsCompanyService
{
    @Autowired
    private CremsCompanyMapper companyMapper;

    @Override
    public CremsCompany selectCompanyById(Long companyId)
    {
        return companyMapper.selectCompanyById(companyId);
    }

    @Override
    public CremsCompany selectCompanyByUserId(Long userId)
    {
        return companyMapper.selectCompanyByUserId(userId);
    }

    @Override
    public List<CremsCompany> selectCompanyList(CremsCompany company)
    {
        return companyMapper.selectCompanyList(company);
    }

    @Override
    public int insertCompany(CremsCompany company)
    {
        return companyMapper.insertCompany(company);
    }

    @Override
    public int updateCompany(CremsCompany company)
    {
        return companyMapper.updateCompany(company);
    }

    @Override
    public int deleteCompanyByIds(Long[] companyIds)
    {
        return companyMapper.deleteCompanyByIds(companyIds);
    }

    @Override
    public int deleteCompanyById(Long companyId)
    {
        return companyMapper.deleteCompanyById(companyId);
    }
}
