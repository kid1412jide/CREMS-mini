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
import com.ruoyi.crems.domain.CremsStudent;
import com.ruoyi.crems.service.ICremsStudentService;

/**
 * 学生管理Controller
 *
 * @author crems
 */
@RestController
@RequestMapping("/crems/student")
public class CremsStudentController extends BaseController
{
    @Autowired
    private ICremsStudentService studentService;

    @PreAuthorize("@ss.hasPermi('crems:student:list')")
    @GetMapping("/list")
    public TableDataInfo list(CremsStudent student)
    {
        startPage();
        List<CremsStudent> list = studentService.selectStudentList(student);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('crems:student:export')")
    @Log(title = "学生管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CremsStudent student, jakarta.servlet.http.HttpServletResponse response)
    {
        List<CremsStudent> list = studentService.selectStudentList(student);
        ExcelUtil<CremsStudent> util = new ExcelUtil<CremsStudent>(CremsStudent.class);
        util.exportExcel(response, list, "学生数据");
    }

    /**
     * 导入学生数据
     */
    @PreAuthorize("@ss.hasPermi('crems:student:import')")
    @Log(title = "学生管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<CremsStudent> util = new ExcelUtil<CremsStudent>(CremsStudent.class);
        List<CremsStudent> list;
        try (java.io.InputStream is = file.getInputStream()) {
            list = util.importExcel(is);
        }
        String operName = getUsername();
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (CremsStudent student : list)
        {
            try
            {
                student.setCreateBy(operName);
                studentService.insertStudent(student);
                successNum++;
                successMsg.append("<br/>" + successNum + "、学生 " + student.getStudentName() + " 导入成功");
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、学生 " + student.getStudentName() + " 导入失败：";
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
    @PreAuthorize("@ss.hasPermi('crems:student:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(jakarta.servlet.http.HttpServletResponse response)
    {
        ExcelUtil<CremsStudent> util = new ExcelUtil<CremsStudent>(CremsStudent.class);
        util.importTemplateExcel(response, "学生数据");
    }

    @PreAuthorize("@ss.hasPermi('crems:student:query')")
    @GetMapping(value = "/{studentId}")
    public AjaxResult getInfo(@PathVariable("studentId") Long studentId)
    {
        return success(studentService.selectStudentById(studentId));
    }

    @PreAuthorize("@ss.hasPermi('crems:student:add')")
    @Log(title = "学生管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CremsStudent student)
    {
        student.setCreateBy(getUsername());
        return toAjax(studentService.insertStudent(student));
    }

    @PreAuthorize("@ss.hasPermi('crems:student:edit')")
    @Log(title = "学生管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CremsStudent student)
    {
        student.setUpdateBy(getUsername());
        return toAjax(studentService.updateStudent(student));
    }

    @PreAuthorize("@ss.hasPermi('crems:student:remove')")
    @Log(title = "学生管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{studentIds}")
    public AjaxResult remove(@PathVariable Long[] studentIds)
    {
        return toAjax(studentService.deleteStudentByIds(studentIds));
    }
}
