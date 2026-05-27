package com.ruoyi.crems.mapper;

import java.util.List;
import com.ruoyi.crems.domain.CremsFavorite;

/**
 * 职位收藏Mapper接口
 *
 * @author crems
 */
public interface CremsFavoriteMapper
{
    public List<CremsFavorite> selectFavoriteList(CremsFavorite favorite);

    public CremsFavorite selectFavoriteById(Long favoriteId);

    public int insertFavorite(CremsFavorite favorite);

    public int deleteFavoriteById(Long favoriteId);

    public int deleteFavoriteByJobAndStudent(Long jobId, Long studentId);
}
