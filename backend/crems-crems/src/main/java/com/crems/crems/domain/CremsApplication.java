package com.crems.crems.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.crems.common.annotation.Excel;
import com.crems.common.core.domain.BaseEntity;

/**
 * 简历投递对象 crems_application
 *
 * @author crems
 */
public class CremsApplication extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 投递ID */
    private Long applicationId;

    /** 职位ID */
    @Excel(name = "职位ID")
    private Long jobId;

    /** 学生ID */
    @Excel(name = "学生ID")
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

    /** 学号（关联查询） */
    private String studentNo;

    /** 性别（关联查询） */
    private String gender;

    /** 手机号（关联查询） */
    private String phone;

    /** 邮箱（关联查询） */
    private String email;

    /** 学校（关联查询） */
    private String school;

    /** 专业（关联查询） */
    private String major;

    /** 学历（关联查询） */
    private String education;

    /** 年级（关联查询） */
    private String grade;

    /** 毕业时间（关联查询） */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date graduationDate;

    /** 技能标签（关联查询） */
    private String skills;

    /** 自我介绍（关联查询） */
    private String selfIntroduction;

    /** 学生简历附件（关联查询） */
    private String studentResumeUrl;

    /** 简历附件 */
    private String resumeUrl;

    /** 求职信 */
    private String coverLetter;

    /** 投递时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "投递时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /** 状态（0待查看 1已查看 2初筛通过 3面试邀请 4已拒绝 5已录用） */
    @Excel(name = "状态", readConverterExp = "0=待查看,1=已查看,2=初筛通过,3=面试邀请,4=已拒绝,5=已录用")
    private String status;

    /** 查看时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date viewTime;

    /** 企业反馈 */
    private String feedback;

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

    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public String getStudentNo() { return studentNo; }

    public void setGender(String gender) { this.gender = gender; }
    public String getGender() { return gender; }

    public void setPhone(String phone) { this.phone = phone; }
    public String getPhone() { return phone; }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }

    public void setSchool(String school) { this.school = school; }
    public String getSchool() { return school; }

    public void setMajor(String major) { this.major = major; }
    public String getMajor() { return major; }

    public void setEducation(String education) { this.education = education; }
    public String getEducation() { return education; }

    public void setGrade(String grade) { this.grade = grade; }
    public String getGrade() { return grade; }

    public void setGraduationDate(Date graduationDate) { this.graduationDate = graduationDate; }
    public Date getGraduationDate() { return graduationDate; }

    public void setSkills(String skills) { this.skills = skills; }
    public String getSkills() { return skills; }

    public void setSelfIntroduction(String selfIntroduction) { this.selfIntroduction = selfIntroduction; }
    public String getSelfIntroduction() { return selfIntroduction; }

    public void setStudentResumeUrl(String studentResumeUrl) { this.studentResumeUrl = studentResumeUrl; }
    public String getStudentResumeUrl() { return studentResumeUrl; }

    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
    public String getResumeUrl() { return resumeUrl; }

    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }
    public String getCoverLetter() { return coverLetter; }

    public void setApplyTime(Date applyTime) { this.applyTime = applyTime; }
    public Date getApplyTime() { return applyTime; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    public void setViewTime(Date viewTime) { this.viewTime = viewTime; }
    public Date getViewTime() { return viewTime; }

    public void setFeedback(String feedback) { this.feedback = feedback; }
    public String getFeedback() { return feedback; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("applicationId", getApplicationId())
            .append("jobId", getJobId())
            .append("studentId", getStudentId())
            .append("companyId", getCompanyId())
            .append("status", getStatus())
            .append("applyTime", getApplyTime())
            .toString();
    }
}
