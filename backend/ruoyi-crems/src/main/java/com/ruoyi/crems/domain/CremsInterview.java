package com.ruoyi.crems.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 面试安排对象 crems_interview
 *
 * @author crems
 */
public class CremsInterview extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 面试ID */
    private Long interviewId;

    /** 投递ID */
    private Long applicationId;

    /** 职位ID */
    private Long jobId;

    /** 学生ID */
    private Long studentId;

    /** 企业ID */
    private Long companyId;

    /** 职位名称（关联查询） */
    @Excel(name = "职位名称")
    private String jobTitle;

    /** 企业名称（关联查询） */
    @Excel(name = "企业名称")
    private String companyName;

    /** 学生姓名（关联查询） */
    @Excel(name = "学生姓名")
    private String studentName;

    /** 面试类型（初试/复试/终试） */
    @Excel(name = "面试类型")
    private String interviewType;

    /** 面试方式（现场/视频/电话） */
    @Excel(name = "面试方式")
    private String interviewMethod;

    /** 面试时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "面试时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date interviewTime;

    /** 面试地址 */
    @Excel(name = "面试地址")
    private String interviewAddress;

    /** 面试官 */
    @Excel(name = "面试官")
    private String interviewer;

    /** 联系电话 */
    private String contactPhone;

    /** 面试通知 */
    private String interviewNotice;

    /** 面试结果（通过/未通过/待定） */
    @Excel(name = "面试结果")
    private String interviewResult;

    /** 面试评价 */
    private String interviewFeedback;

    /** 面试评分 */
    @Excel(name = "面试评分")
    private BigDecimal score;

    /** 状态（0待确认 1已确认 2已完成 3已取消） */
    @Excel(name = "状态", readConverterExp = "0=待确认,1=已确认,2=已完成,3=已取消")
    private String status;

    public void setInterviewId(Long interviewId) { this.interviewId = interviewId; }
    public Long getInterviewId() { return interviewId; }

    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public Long getApplicationId() { return applicationId; }

    public void setJobId(Long jobId) { this.jobId = jobId; }
    public Long getJobId() { return jobId; }

    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getStudentId() { return studentId; }

    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getCompanyId() { return companyId; }

    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getJobTitle() { return jobTitle; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCompanyName() { return companyName; }

    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentName() { return studentName; }

    public void setInterviewType(String interviewType) { this.interviewType = interviewType; }
    public String getInterviewType() { return interviewType; }

    public void setInterviewMethod(String interviewMethod) { this.interviewMethod = interviewMethod; }
    public String getInterviewMethod() { return interviewMethod; }

    public void setInterviewTime(Date interviewTime) { this.interviewTime = interviewTime; }
    public Date getInterviewTime() { return interviewTime; }

    public void setInterviewAddress(String interviewAddress) { this.interviewAddress = interviewAddress; }
    public String getInterviewAddress() { return interviewAddress; }

    public void setInterviewer(String interviewer) { this.interviewer = interviewer; }
    public String getInterviewer() { return interviewer; }

    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactPhone() { return contactPhone; }

    public void setInterviewNotice(String interviewNotice) { this.interviewNotice = interviewNotice; }
    public String getInterviewNotice() { return interviewNotice; }

    public void setInterviewResult(String interviewResult) { this.interviewResult = interviewResult; }
    public String getInterviewResult() { return interviewResult; }

    public void setInterviewFeedback(String interviewFeedback) { this.interviewFeedback = interviewFeedback; }
    public String getInterviewFeedback() { return interviewFeedback; }

    public void setScore(BigDecimal score) { this.score = score; }
    public BigDecimal getScore() { return score; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("interviewId", getInterviewId())
            .append("applicationId", getApplicationId())
            .append("interviewType", getInterviewType())
            .append("interviewTime", getInterviewTime())
            .append("status", getStatus())
            .toString();
    }
}
