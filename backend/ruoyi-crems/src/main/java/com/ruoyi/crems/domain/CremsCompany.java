package com.ruoyi.crems.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 企业信息对象 crems_company
 *
 * @author crems
 */
public class CremsCompany extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 企业ID */
    private Long companyId;

    /** 企业名称 */
    @Excel(name = "企业名称")
    private String companyName;

    /** 统一社会信用代码 */
    @Excel(name = "统一社会信用代码")
    private String companyCode;

    /** 企业类型 */
    @Excel(name = "企业类型")
    private String companyType;

    /** 所属行业 */
    @Excel(name = "所属行业")
    private String industry;

    /** 企业规模 */
    @Excel(name = "企业规模")
    private String scale;

    /** 企业地址 */
    @Excel(name = "企业地址")
    private String address;

    /** 企业网站 */
    private String website;

    /** 企业简介 */
    private String description;

    /** 企业Logo */
    private String logoUrl;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contactPerson;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String contactPhone;

    /** 联系邮箱 */
    @Excel(name = "联系邮箱")
    private String contactEmail;

    /** 状态（0待审核 1已认证 2已拒绝） */
    @Excel(name = "状态", readConverterExp = "0=待审核,1=已认证,2=已拒绝")
    private String status;

    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getCompanyId() { return companyId; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCompanyName() { return companyName; }

    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
    public String getCompanyCode() { return companyCode; }

    public void setCompanyType(String companyType) { this.companyType = companyType; }
    public String getCompanyType() { return companyType; }

    public void setIndustry(String industry) { this.industry = industry; }
    public String getIndustry() { return industry; }

    public void setScale(String scale) { this.scale = scale; }
    public String getScale() { return scale; }

    public void setAddress(String address) { this.address = address; }
    public String getAddress() { return address; }

    public void setWebsite(String website) { this.website = website; }
    public String getWebsite() { return website; }

    public void setDescription(String description) { this.description = description; }
    public String getDescription() { return description; }

    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getLogoUrl() { return logoUrl; }

    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getContactPerson() { return contactPerson; }

    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactPhone() { return contactPhone; }

    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getContactEmail() { return contactEmail; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("companyId", getCompanyId())
            .append("companyName", getCompanyName())
            .append("companyCode", getCompanyCode())
            .append("companyType", getCompanyType())
            .append("industry", getIndustry())
            .append("scale", getScale())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
