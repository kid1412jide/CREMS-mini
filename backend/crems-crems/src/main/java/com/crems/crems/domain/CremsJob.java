package com.crems.crems.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.crems.common.annotation.Excel;
import com.crems.common.core.domain.BaseEntity;

/**
 * 职位信息对象 crems_job
 *
 * @author crems
 */
public class CremsJob extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 职位ID */
    private Long jobId;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long companyId;

    /** 企业名称（非数据库字段，关联查询） */
    @Excel(name = "企业名称")
    private String companyName;

    /** 职位名称 */
    @Excel(name = "职位名称")
    private String jobTitle;

    /** 职位类型 */
    @Excel(name = "职位类型")
    private String jobType;

    /** 职位分类 */
    @Excel(name = "职位分类")
    private String category;

    /** 所属部门 */
    private String department;

    /** 招聘人数 */
    @Excel(name = "招聘人数")
    private Integer recruitNum;

    /** 工作城市 */
    @Excel(name = "工作城市")
    private String workCity;

    /** 工作地址 */
    private String workAddress;

    /** 最低薪资 */
    @Excel(name = "最低薪资")
    private BigDecimal salaryMin;

    /** 最高薪资 */
    @Excel(name = "最高薪资")
    private BigDecimal salaryMax;

    /** 学历要求 */
    @Excel(name = "学历要求")
    private String educationRequired;

    /** 经验要求 */
    private String experienceRequired;

    /** 职位描述 */
    private String jobDescription;

    /** 任职要求 */
    private String jobRequirements;

    /** 福利待遇 */
    private String welfare;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    /** 截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deadline;

    /** 浏览次数 */
    private Integer viewCount;

    /** 投递次数 */
    private Integer applyCount;

    /** 状态（0待审核 1已发布 2已下架） */
    @Excel(name = "状态", readConverterExp = "0=待审核,1=已发布,2=已下架")
    private String status;

    public void setJobId(Long jobId) { this.jobId = jobId; }
    public Long getJobId() { return jobId; }

    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getCompanyId() { return companyId; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCompanyName() { return companyName; }

    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getJobTitle() { return jobTitle; }

    public void setJobType(String jobType) { this.jobType = jobType; }
    public String getJobType() { return jobType; }

    public void setCategory(String category) { this.category = category; }
    public String getCategory() { return category; }

    public void setDepartment(String department) { this.department = department; }
    public String getDepartment() { return department; }

    public void setRecruitNum(Integer recruitNum) { this.recruitNum = recruitNum; }
    public Integer getRecruitNum() { return recruitNum; }

    public void setWorkCity(String workCity) { this.workCity = workCity; }
    public String getWorkCity() { return workCity; }

    public void setWorkAddress(String workAddress) { this.workAddress = workAddress; }
    public String getWorkAddress() { return workAddress; }

    public void setSalaryMin(BigDecimal salaryMin) { this.salaryMin = salaryMin; }
    public BigDecimal getSalaryMin() { return salaryMin; }

    public void setSalaryMax(BigDecimal salaryMax) { this.salaryMax = salaryMax; }
    public BigDecimal getSalaryMax() { return salaryMax; }

    public void setEducationRequired(String educationRequired) { this.educationRequired = educationRequired; }
    public String getEducationRequired() { return educationRequired; }

    public void setExperienceRequired(String experienceRequired) { this.experienceRequired = experienceRequired; }
    public String getExperienceRequired() { return experienceRequired; }

    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
    public String getJobDescription() { return jobDescription; }

    public void setJobRequirements(String jobRequirements) { this.jobRequirements = jobRequirements; }
    public String getJobRequirements() { return jobRequirements; }

    public void setWelfare(String welfare) { this.welfare = welfare; }
    public String getWelfare() { return welfare; }

    public void setPublishDate(Date publishDate) { this.publishDate = publishDate; }
    public Date getPublishDate() { return publishDate; }

    public void setDeadline(Date deadline) { this.deadline = deadline; }
    public Date getDeadline() { return deadline; }

    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public Integer getViewCount() { return viewCount; }

    public void setApplyCount(Integer applyCount) { this.applyCount = applyCount; }
    public Integer getApplyCount() { return applyCount; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("jobId", getJobId())
            .append("companyId", getCompanyId())
            .append("jobTitle", getJobTitle())
            .append("jobType", getJobType())
            .append("workCity", getWorkCity())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
