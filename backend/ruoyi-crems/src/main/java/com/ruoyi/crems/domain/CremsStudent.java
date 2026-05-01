package com.ruoyi.crems.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学生信息对象 crems_student
 *
 * @author crems
 */
public class CremsStudent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学生ID */
    private Long studentId;

    /** 学号 */
    @Excel(name = "学号")
    private String studentNo;

    /** 姓名 */
    @Excel(name = "姓名")
    private String studentName;

    /** 性别（0男 1女） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String gender;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 身份证号 */
    private String idCard;

    /** 学校 */
    @Excel(name = "学校")
    private String school;

    /** 专业 */
    @Excel(name = "专业")
    private String major;

    /** 学历 */
    @Excel(name = "学历")
    private String education;

    /** 年级 */
    @Excel(name = "年级")
    private String grade;

    /** 预计毕业时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计毕业时间", dateFormat = "yyyy-MM-dd")
    private Date graduationDate;

    /** 头像 */
    private String avatarUrl;

    /** 简历附件 */
    private String resumeUrl;

    /** 自我介绍 */
    private String selfIntroduction;

    /** 技能标签 */
    private String skills;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getStudentId() { return studentId; }

    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public String getStudentNo() { return studentNo; }

    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentName() { return studentName; }

    public void setGender(String gender) { this.gender = gender; }
    public String getGender() { return gender; }

    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
    public Date getBirthDate() { return birthDate; }

    public void setPhone(String phone) { this.phone = phone; }
    public String getPhone() { return phone; }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }

    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getIdCard() { return idCard; }

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

    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getAvatarUrl() { return avatarUrl; }

    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
    public String getResumeUrl() { return resumeUrl; }

    public void setSelfIntroduction(String selfIntroduction) { this.selfIntroduction = selfIntroduction; }
    public String getSelfIntroduction() { return selfIntroduction; }

    public void setSkills(String skills) { this.skills = skills; }
    public String getSkills() { return skills; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("studentId", getStudentId())
            .append("studentNo", getStudentNo())
            .append("studentName", getStudentName())
            .append("school", getSchool())
            .append("major", getMajor())
            .append("education", getEducation())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
