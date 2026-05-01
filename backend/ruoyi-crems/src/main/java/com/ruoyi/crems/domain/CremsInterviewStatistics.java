package com.ruoyi.crems.domain;

/**
 * 面试统计VO
 *
 * @author crems
 */
public class CremsInterviewStatistics
{
    /** 分组名称（如面试类型、面试结果等） */
    private String groupName;

    /** 面试数量 */
    private Long interviewCount;

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public Long getInterviewCount() { return interviewCount; }
    public void setInterviewCount(Long interviewCount) { this.interviewCount = interviewCount; }
}