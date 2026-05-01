package com.ruoyi.crems.domain;

import java.math.BigDecimal;

/**
 * 统计数据概览VO
 *
 * @author crems
 */
public class CremsStatisticsOverview
{
    /** 企业总数 */
    private Long companyCount;

    /** 已认证企业数 */
    private Long certifiedCompanyCount;

    /** 学生总数 */
    private Long studentCount;

    /** 职位总数 */
    private Long jobCount;

    /** 已发布职位数 */
    private Long publishedJobCount;

    /** 投递总数 */
    private Long applicationCount;

    /** 面试总数 */
    private Long interviewCount;

    public Long getCompanyCount() { return companyCount; }
    public void setCompanyCount(Long companyCount) { this.companyCount = companyCount; }

    public Long getCertifiedCompanyCount() { return certifiedCompanyCount; }
    public void setCertifiedCompanyCount(Long certifiedCompanyCount) { this.certifiedCompanyCount = certifiedCompanyCount; }

    public Long getStudentCount() { return studentCount; }
    public void setStudentCount(Long studentCount) { this.studentCount = studentCount; }

    public Long getJobCount() { return jobCount; }
    public void setJobCount(Long jobCount) { this.jobCount = jobCount; }

    public Long getPublishedJobCount() { return publishedJobCount; }
    public void setPublishedJobCount(Long publishedJobCount) { this.publishedJobCount = publishedJobCount; }

    public Long getApplicationCount() { return applicationCount; }
    public void setApplicationCount(Long applicationCount) { this.applicationCount = applicationCount; }

    public Long getInterviewCount() { return interviewCount; }
    public void setInterviewCount(Long interviewCount) { this.interviewCount = interviewCount; }
}