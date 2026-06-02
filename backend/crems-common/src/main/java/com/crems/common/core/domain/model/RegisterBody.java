package com.crems.common.core.domain.model;

/**
 * 用户注册对象
 *
 * @author crems
 */
public class RegisterBody extends LoginBody
{
    /**
     * 用户身份类型：student-学生 company-企业
     */
    private String roleType;

    public String getRoleType()
    {
        return roleType;
    }

    public void setRoleType(String roleType)
    {
        this.roleType = roleType;
    }
}
