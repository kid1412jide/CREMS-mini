package com.ruoyi.crems.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.crems.domain.CremsFavorite;
import com.ruoyi.crems.service.ICremsFavoriteService;

/**
 * 职位收藏Controller
 *
 * @author crems
 */
@RestController
@RequestMapping("/crems/favorite")
public class CremsFavoriteController extends BaseController
{
    @Autowired
    private ICremsFavoriteService favoriteService;

    /**
     * 查询职位收藏列表
     */
    @PreAuthorize("@ss.hasPermi('crems:favorite:list')")
    @GetMapping("/list")
    public TableDataInfo list(CremsFavorite favorite)
    {
        startPage();
        List<CremsFavorite> list = favoriteService.selectFavoriteList(favorite);
        return getDataTable(list);
    }

    /**
     * 获取收藏详细信息
     */
    @PreAuthorize("@ss.hasPermi('crems:favorite:query')")
    @GetMapping(value = "/{favoriteId}")
    public AjaxResult getInfo(@PathVariable("favoriteId") Long favoriteId)
    {
        return success(favoriteService.selectFavoriteList(new CremsFavorite() {{ setFavoriteId(favoriteId); }}));
    }

    /**
     * 添加职位收藏
     */
    @PreAuthorize("@ss.hasPermi('crems:favorite:add')")
    @Log(title = "职位收藏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CremsFavorite favorite)
    {
        return toAjax(favoriteService.insertFavorite(favorite));
    }

    /**
     * 删除职位收藏
     */
    @PreAuthorize("@ss.hasPermi('crems:favorite:remove')")
    @Log(title = "职位收藏", businessType = BusinessType.DELETE)
    @DeleteMapping("/{favoriteIds}")
    public AjaxResult remove(@PathVariable Long[] favoriteIds)
    {
        return toAjax(favoriteService.deleteFavoriteById(favoriteIds[0]));
    }

    /**
     * 根据职位ID和学生ID取消收藏
     */
    @PreAuthorize("@ss.hasPermi('crems:favorite:remove')")
    @Log(title = "职位收藏", businessType = BusinessType.DELETE)
    @DeleteMapping("/job/{jobId}/student/{studentId}")
    public AjaxResult removeByJobAndStudent(@PathVariable Long jobId, @PathVariable Long studentId)
    {
        return toAjax(favoriteService.deleteFavoriteByJobAndStudent(jobId, studentId));
    }
}