package com.crems.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.crems.crems.domain.CremsCompany;
import com.crems.crems.mapper.CremsCompanyMapper;
import com.crems.crems.service.ICremsCompanyService;

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
    @Transactional
    public int insertCompany(CremsCompany company)
    {
        return companyMapper.insertCompany(company);
    }

    @Override
    @Transactional
    public int updateCompany(CremsCompany company)
    {
        return companyMapper.updateCompany(company);
    }

    @Override
    @Transactional
    public int deleteCompanyByIds(Long[] companyIds)
    {
        return companyMapper.deleteCompanyByIds(companyIds);
    }

    @Override
    @Transactional
    public int deleteCompanyById(Long companyId)
    {
        return companyMapper.deleteCompanyById(companyId);
    }
}
