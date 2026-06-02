package com.crems.crems.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.crems.common.core.domain.BaseEntity;

/**
 * 职位收藏对象 crems_favorite
 *
 * @author crems
 */
public class CremsFavorite
{
    private static final long serialVersionUID = 1L;

    /** 收藏ID */
    private Long favoriteId;

    /** 职位ID */
    private Long jobId;

    /** 学生ID */
    private Long studentId;

    /** 收藏时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 职位名称（关联查询） */
    private String jobTitle;

    /** 企业名称（关联查询） */
    private String companyName;

    public void setFavoriteId(Long favoriteId) { this.favoriteId = favoriteId; }
    public Long getFavoriteId() { return favoriteId; }

    public void setJobId(Long jobId) { this.jobId = jobId; }
    public Long getJobId() { return jobId; }

    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getStudentId() { return studentId; }

    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getCreateTime() { return createTime; }

    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getJobTitle() { return jobTitle; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCompanyName() { return companyName; }
}
