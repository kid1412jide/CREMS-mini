package com.crems.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.crems.crems.domain.CremsCompany;
import com.crems.crems.domain.CremsInterview;
import com.crems.crems.domain.CremsApplication;
import com.crems.crems.domain.CremsJob;
import com.crems.crems.mapper.CremsCompanyMapper;
import com.crems.crems.mapper.CremsInterviewMapper;
import com.crems.crems.mapper.CremsApplicationMapper;
import com.crems.crems.mapper.CremsJobMapper;
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
    @Autowired
    private CremsJobMapper jobMapper;
    @Autowired
    private CremsApplicationMapper applicationMapper;
    @Autowired
    private CremsInterviewMapper interviewMapper;

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
        for (Long companyId : companyIds) {
            deleteCompanyCascade(companyId);
        }
        return companyMapper.deleteCompanyByIds(companyIds);
    }

    @Override
    @Transactional
    public int deleteCompanyById(Long companyId)
    {
        deleteCompanyCascade(companyId);
        return companyMapper.deleteCompanyById(companyId);
    }

    /**
     * 级联删除企业关联数据：interview -> application -> job
     */
    private void deleteCompanyCascade(Long companyId) {
        // 查询企业下的所有职位
        CremsJob jobQuery = new CremsJob();
        jobQuery.setCompanyId(companyId);
        java.util.List<CremsJob> jobs = jobMapper.selectJobList(jobQuery);

        for (CremsJob job : jobs) {
            // 删除职位关联的面试
            CremsInterview interviewQuery = new CremsInterview();
            interviewQuery.setJobId(job.getJobId());
            java.util.List<CremsInterview> interviews = interviewMapper.selectInterviewList(interviewQuery);
            for (CremsInterview interview : interviews) {
                interviewMapper.deleteInterviewById(interview.getInterviewId());
            }

            // 删除职位关联的投递
            CremsApplication appQuery = new CremsApplication();
            appQuery.setJobId(job.getJobId());
            java.util.List<CremsApplication> applications = applicationMapper.selectApplicationList(appQuery);
            for (CremsApplication app : applications) {
                applicationMapper.deleteApplicationById(app.getApplicationId());
            }
        }

        // 删除所有职位
        Long[] jobIds = jobs.stream().map(CremsJob::getJobId).toArray(Long[]::new);
        if (jobIds.length > 0) {
            jobMapper.deleteJobByIds(jobIds);
        }

        // 删除企业关联的面试（通过companyId）
        CremsInterview companyInterviewQuery = new CremsInterview();
        companyInterviewQuery.setCompanyId(companyId);
        java.util.List<CremsInterview> companyInterviews = interviewMapper.selectInterviewList(companyInterviewQuery);
        for (CremsInterview interview : companyInterviews) {
            interviewMapper.deleteInterviewById(interview.getInterviewId());
        }

        // 删除企业关联的投递（通过companyId）
        CremsApplication companyAppQuery = new CremsApplication();
        companyAppQuery.setCompanyId(companyId);
        java.util.List<CremsApplication> companyApps = applicationMapper.selectApplicationList(companyAppQuery);
        for (CremsApplication app : companyApps) {
            applicationMapper.deleteApplicationById(app.getApplicationId());
        }
    }
}
