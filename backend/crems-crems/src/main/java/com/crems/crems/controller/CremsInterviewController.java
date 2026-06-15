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
import com.crems.crems.domain.CremsInterview;
import com.crems.crems.service.ICremsInterviewService;

/**
 * 面试管理Controller
 *
 * @author crems
 */
@RestController
@RequestMapping("/crems/interview")
public class CremsInterviewController extends BaseController
{
    @Autowired
    private ICremsInterviewService interviewService;

    @PreAuthorize("@ss.hasPermi('crems:interview:list')")
    @GetMapping("/list")
    public TableDataInfo list(CremsInterview interview)
    {
        startPage();
        List<CremsInterview> list = interviewService.selectInterviewList(interview);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('crems:interview:export')")
    @Log(title = "面试管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CremsInterview interview, jakarta.servlet.http.HttpServletResponse response)
    {
        List<CremsInterview> list = interviewService.selectInterviewList(interview);
        ExcelUtil<CremsInterview> util = new ExcelUtil<CremsInterview>(CremsInterview.class);
        util.exportExcel(response, list, "面试数据");
    }

    @PreAuthorize("@ss.hasPermi('crems:interview:query')")
    @GetMapping(value = "/{interviewId}")
    public AjaxResult getInfo(@PathVariable("interviewId") Long interviewId)
    {
        return success(interviewService.selectInterviewById(interviewId));
    }

    @PreAuthorize("@ss.hasPermi('crems:interview:add')")
    @Log(title = "面试管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CremsInterview interview)
    {
        interview.setCreateBy(getUsername());
        return toAjax(interviewService.insertInterview(interview));
    }

    @PreAuthorize("@ss.hasPermi('crems:interview:edit')")
    @Log(title = "面试管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CremsInterview interview)
    {
        interview.setUpdateBy(getUsername());

        // 校验面试状态转换
        if (interview.getStatus() != null && interview.getInterviewId() != null) {
            CremsInterview existing = interviewService.selectInterviewById(interview.getInterviewId());
            if (existing != null && !interview.getStatus().equals(existing.getStatus())) {
                if (!isValidInterviewStatusTransition(existing.getStatus(), interview.getStatus())) {
                    return error("不允许从状态 " + existing.getStatus() + " 转换到 " + interview.getStatus());
                }
            }
        }

        return toAjax(interviewService.updateInterview(interview));
    }

    /**
     * 校验面试状态转换是否合法
     * 0:待确认 -> 1:已确认, 3:已取消
     * 1:已确认 -> 2:已完成, 3:已取消
     */
    private boolean isValidInterviewStatusTransition(String current, String target) {
        return switch (current) {
            case "0" -> "1".equals(target) || "3".equals(target);
            case "1" -> "2".equals(target) || "3".equals(target);
            default -> false;
        };
    }

    @PreAuthorize("@ss.hasPermi('crems:interview:remove')")
    @Log(title = "面试管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{interviewIds}")
    public AjaxResult remove(@PathVariable Long[] interviewIds)
    {
        return toAjax(interviewService.deleteInterviewByIds(interviewIds));
    }
}
