package com.crems.crems.controller;

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
import com.crems.common.annotation.Log;
import com.crems.common.core.controller.BaseController;
import com.crems.common.core.domain.AjaxResult;
import com.crems.common.core.page.TableDataInfo;
import com.crems.common.enums.BusinessType;
import com.crems.crems.domain.CremsFavorite;
import com.crems.crems.service.ICremsFavoriteService;

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
        // 批量删除所有收藏记录
        int count = 0;
        for (Long favoriteId : favoriteIds) {
            count += favoriteService.deleteFavoriteById(favoriteId);
        }
        return count > 0 ? success() : error("删除失败");
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