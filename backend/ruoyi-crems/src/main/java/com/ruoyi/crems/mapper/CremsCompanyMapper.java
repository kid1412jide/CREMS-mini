package com.ruoyi.crems.mapper;

import java.util.List;
import com.ruoyi.crems.domain.CremsCompany;

/**
 * 企业信息Mapper接口
 *
 * @author crems
 */
public interface CremsCompanyMapper
{
    public CremsCompany selectCompanyById(Long companyId);

    public List<CremsCompany> selectCompanyList(CremsCompany company);

    public int insertCompany(CremsCompany company);

    public int updateCompany(CremsCompany company);

    public int deleteCompanyById(Long companyId);

    public int deleteCompanyByIds(Long[] companyIds);
}
