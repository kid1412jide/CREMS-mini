package com.ruoyi.crems.service;

import java.util.List;
import com.ruoyi.crems.domain.CremsFavorite;

/**
 * 职位收藏Service接口
 *
 * @author crems
 */
public interface ICremsFavoriteService
{
    public List<CremsFavorite> selectFavoriteList(CremsFavorite favorite);

    public int insertFavorite(CremsFavorite favorite);

    public int deleteFavoriteById(Long favoriteId);

    public int deleteFavoriteByJobAndStudent(Long jobId, Long studentId);
}
