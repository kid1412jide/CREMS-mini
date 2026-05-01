package com.ruoyi.crems.domain;

/**
 * 投递统计VO
 *
 * @author crems
 */
public class CremsApplicationStatistics
{
    /** 分组名称（如状态名称、时间周期等） */
    private String groupName;

    /** 投递数量 */
    private Long applicationCount;

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public Long getApplicationCount() { return applicationCount; }
    public void setApplicationCount(Long applicationCount) { this.applicationCount = applicationCount; }
}