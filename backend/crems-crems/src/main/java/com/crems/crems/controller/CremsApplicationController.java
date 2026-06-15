package com.crems.crems.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.crems.common.annotation.Log;
import com.crems.common.core.controller.BaseController;
import com.crems.common.core.domain.AjaxResult;
import com.crems.common.core.page.TableDataInfo;
import com.crems.common.enums.BusinessType;
import com.crems.common.utils.poi.ExcelUtil;
import com.crems.crems.domain.CremsApplication;
import com.crems.crems.service.ICremsApplicationService;

/**
 * 投递管理Controller
 *
 * @author crems
 */
@RestController
@RequestMapping("/crems/application")
public class CremsApplicationController extends BaseController
{
    @Autowired
    private ICremsApplicationService applicationService;

    @PreAuthorize("@ss.hasPermi('crems:application:list')")
    @GetMapping("/list")
    public TableDataInfo list(CremsApplication application)
    {
        startPage();
        List<CremsApplication> list = applicationService.selectApplicationList(application);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('crems:application:export')")
    @Log(title = "投递管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CremsApplication application, jakarta.servlet.http.HttpServletResponse response)
    {
        List<CremsApplication> list = applicationService.selectApplicationList(application);
        ExcelUtil<CremsApplication> util = new ExcelUtil<CremsApplication>(CremsApplication.class);
        util.exportExcel(response, list, "投递数据");
    }

    @PreAuthorize("@ss.hasPermi('crems:application:query')")
    @GetMapping(value = "/{applicationId}")
    public AjaxResult getInfo(@PathVariable("applicationId") Long applicationId)
    {
        return success(applicationService.selectApplicationById(applicationId));
    }

    @PreAuthorize("@ss.hasPermi('crems:application:add')")
    @Log(title = "简历投递", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CremsApplication application)
    {
        application.setCreateBy(getUsername());
        return toAjax(applicationService.insertApplication(application));
    }

    @PreAuthorize("@ss.hasPermi('crems:application:edit')")
    @Log(title = "投递管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CremsApplication application)
    {
        application.setUpdateBy(getUsername());

        // 如果状态发生变化，使用 CAS 更新并校验状态机
        if (application.getStatus() != null && application.getApplicationId() != null) {
            CremsApplication existing = applicationService.selectApplicationById(application.getApplicationId());
            if (existing != null && !application.getStatus().equals(existing.getStatus())) {
                if (!isValidStatusTransition(existing.getStatus(), application.getStatus())) {
                    return error("不允许从状态 " + existing.getStatus() + " 转换到 " + application.getStatus());
                }
                int updated = applicationService.updateApplicationStatusIfCurrent(
                        application.getApplicationId(), existing.getStatus(), application.getStatus(), getUsername());
                if (updated == 0) {
                    return error("投递状态已变化，请刷新后重试");
                }
                // 更新其他字段
                application.setStatus(null); // 状态已更新，不再重复更新
            }
        }

        return toAjax(applicationService.updateApplication(application));
    }

    /**
     * 校验投递状态转换是否合法
     */
    private boolean isValidStatusTransition(String current, String target) {
        return switch (current) {
            case "0" -> "1".equals(target) || "2".equals(target) || "4".equals(target);
            case "1" -> "2".equals(target) || "4".equals(target);
            case "2" -> "3".equals(target) || "4".equals(target);
            case "3" -> "5".equals(target) || "4".equals(target);
            default -> false;
        };
    }

    @PreAuthorize("@ss.hasPermi('crems:application:remove')")
    @Log(title = "投递管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{applicationIds}")
    public AjaxResult remove(@PathVariable Long[] applicationIds)
    {
        return toAjax(applicationService.deleteApplicationByIds(applicationIds));
    }
}
