package com.crems.crems.controller;

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
import com.crems.common.annotation.Log;
import com.crems.common.core.controller.BaseController;
import com.crems.common.core.domain.AjaxResult;
import com.crems.common.core.page.TableDataInfo;
import com.crems.common.enums.BusinessType;
import com.crems.common.utils.poi.ExcelUtil;
import com.crems.crems.domain.CremsJob;
import com.crems.crems.service.ICremsJobService;

/**
 * 职位管理Controller
 *
 * @author crems
 */
@RestController
@RequestMapping("/crems/job")
public class CremsJobController extends BaseController
{
    @Autowired
    private ICremsJobService jobService;

    @PreAuthorize("@ss.hasPermi('crems:job:list')")
    @GetMapping("/list")
    public TableDataInfo list(CremsJob job)
    {
        startPage();
        List<CremsJob> list = jobService.selectJobList(job);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('crems:job:export')")
    @Log(title = "职位管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CremsJob job, jakarta.servlet.http.HttpServletResponse response)
    {
        List<CremsJob> list = jobService.selectJobList(job);
        ExcelUtil<CremsJob> util = new ExcelUtil<CremsJob>(CremsJob.class);
        util.exportExcel(response, list, "职位数据");
    }

    /**
     * 导入职位数据
     */
    @PreAuthorize("@ss.hasPermi('crems:job:import')")
    @Log(title = "职位管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<CremsJob> util = new ExcelUtil<CremsJob>(CremsJob.class);
        List<CremsJob> list;
        try (java.io.InputStream is = file.getInputStream()) {
            list = util.importExcel(is);
        }
        String operName = getUsername();
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (CremsJob job : list)
        {
            try
            {
                job.setCreateBy(operName);
                jobService.insertJob(job);
                successNum++;
                successMsg.append("<br/>" + successNum + "、职位 " + job.getJobTitle() + " 导入成功");
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、职位 " + job.getJobTitle() + " 导入失败：";
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
    @PreAuthorize("@ss.hasPermi('crems:job:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(jakarta.servlet.http.HttpServletResponse response)
    {
        ExcelUtil<CremsJob> util = new ExcelUtil<CremsJob>(CremsJob.class);
        util.importTemplateExcel(response, "职位数据");
    }

    @PreAuthorize("@ss.hasPermi('crems:job:query')")
    @GetMapping(value = "/{jobId}")
    public AjaxResult getInfo(@PathVariable("jobId") Long jobId)
    {
        return success(jobService.selectJobById(jobId));
    }

    @PreAuthorize("@ss.hasPermi('crems:job:add')")
    @Log(title = "职位管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CremsJob job)
    {
        job.setCreateBy(getUsername());
        return toAjax(jobService.insertJob(job));
    }

    @PreAuthorize("@ss.hasPermi('crems:job:edit')")
    @Log(title = "职位管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CremsJob job)
    {
        job.setUpdateBy(getUsername());
        return toAjax(jobService.updateJob(job));
    }

    @PreAuthorize("@ss.hasPermi('crems:job:remove')")
    @Log(title = "职位管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobIds}")
    public AjaxResult remove(@PathVariable Long[] jobIds)
    {
        return toAjax(jobService.deleteJobByIds(jobIds));
    }

    /**
     * 职位审核
     */
    @PreAuthorize("@ss.hasPermi('crems:job:audit')")
    @Log(title = "职位审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody CremsJob job)
    {
        job.setUpdateBy(getUsername());
        return toAjax(jobService.updateJob(job));
    }
}
