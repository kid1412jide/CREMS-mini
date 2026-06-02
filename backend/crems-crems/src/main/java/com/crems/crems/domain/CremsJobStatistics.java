package com.crems.crems.domain;

import java.math.BigDecimal;

/**
 * 职位统计VO
 *
 * @author crems
 */
public class CremsJobStatistics
{
    /** 分组名称（如城市名、职位类型等） */
    private String groupName;

    /** 职位数量 */
    private Long jobCount;

    /** 平均薪资 */
    private BigDecimal avgSalary;

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public Long getJobCount() { return jobCount; }
    public void setJobCount(Long jobCount) { this.jobCount = jobCount; }

    public BigDecimal getAvgSalary() { return avgSalary; }
    public void setAvgSalary(BigDecimal avgSalary) { this.avgSalary = avgSalary; }
}