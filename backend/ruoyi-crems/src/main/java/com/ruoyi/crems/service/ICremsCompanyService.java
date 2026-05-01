package com.ruoyi.crems.service;

import java.util.List;
import com.ruoyi.crems.domain.CremsCompany;

/**
 * 企业信息Service接口
 *
 * @author crems
 */
public interface ICremsCompanyService
{
    public CremsCompany selectCompanyById(Long companyId);

    public List<CremsCompany> selectCompanyList(CremsCompany company);

    public int insertCompany(CremsCompany company);

    public int updateCompany(CremsCompany company);

    public int deleteCompanyByIds(Long[] companyIds);

    public int deleteCompanyById(Long companyId);
}
