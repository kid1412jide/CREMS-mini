package com.ruoyi.crems.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.crems.domain.CremsCompany;
import com.ruoyi.crems.service.ICremsCompanyService;

/**
 * 企业管理Controller
 *
 * @author crems
 */
@RestController
@RequestMapping("/crems/company")
public class CremsCompanyController extends BaseController
{
    @Autowired
    private ICremsCompanyService companyService;

    /**
     * 查询企业列表
     */
    @PreAuthorize("@ss.hasPermi('crems:company:list')")
    @GetMapping("/list")
    public TableDataInfo list(CremsCompany company)
    {
        startPage();
        List<CremsCompany> list = companyService.selectCompanyList(company);
        return getDataTable(list);
    }

    /**
     * 导出企业列表
     */
    @PreAuthorize("@ss.hasPermi('crems:company:export')")
    @Log(title = "企业管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CremsCompany company, jakarta.servlet.http.HttpServletResponse response)
    {
        List<CremsCompany> list = companyService.selectCompanyList(company);
        ExcelUtil<CremsCompany> util = new ExcelUtil<CremsCompany>(CremsCompany.class);
        util.exportExcel(response, list, "企业数据");
    }

    /**
     * 导入企业数据
     */
    @PreAuthorize("@ss.hasPermi('crems:company:import')")
    @Log(title = "企业管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<CremsCompany> util = new ExcelUtil<CremsCompany>(CremsCompany.class);
        List<CremsCompany> list;
        try (java.io.InputStream is = file.getInputStream()) {
            list = util.importExcel(is);
        }
        String operName = getUsername();
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (CremsCompany company : list)
        {
            try
            {
                company.setCreateBy(operName);
                companyService.insertCompany(company);
                successNum++;
                successMsg.append("<br/>" + successNum + "、企业 " + company.getCompanyName() + " 导入成功");
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、企业 " + company.getCompanyName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            return error(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return success(successMsg.toString());
    }

    /**
     * 导入模板下载
     */
    @PreAuthorize("@ss.hasPermi('crems:company:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(jakarta.servlet.http.HttpServletResponse response)
    {
        ExcelUtil<CremsCompany> util = new ExcelUtil<CremsCompany>(CremsCompany.class);
        util.importTemplateExcel(response, "企业数据");
    }

    /**
     * 获取企业详细信息
     */
    @PreAuthorize("@ss.hasPermi('crems:company:query')")
    @GetMapping(value = "/{companyId}")
    public AjaxResult getInfo(@PathVariable("companyId") Long companyId)
    {
        return success(companyService.selectCompanyById(companyId));
    }

    /**
     * 新增企业
     */
    @PreAuthorize("@ss.hasPermi('crems:company:add')")
    @Log(title = "企业管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CremsCompany company)
    {
        company.setCreateBy(getUsername());
        return toAjax(companyService.insertCompany(company));
    }

    /**
     * 修改企业
     */
    @PreAuthorize("@ss.hasPermi('crems:company:edit')")
    @Log(title = "企业管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CremsCompany company)
    {
        company.setUpdateBy(getUsername());
        return toAjax(companyService.updateCompany(company));
    }

    /**
     * 删除企业
     */
    @PreAuthorize("@ss.hasPermi('crems:company:remove')")
    @Log(title = "企业管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{companyIds}")
    public AjaxResult remove(@PathVariable Long[] companyIds)
    {
        return toAjax(companyService.deleteCompanyByIds(companyIds));
    }

    /**
     * 企业审核
     */
    @PreAuthorize("@ss.hasPermi('crems:company:audit')")
    @Log(title = "企业审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody CremsCompany company)
    {
        company.setUpdateBy(getUsername());
        return toAjax(companyService.updateCompany(company));
    }
}
