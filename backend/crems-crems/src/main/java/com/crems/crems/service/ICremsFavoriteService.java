package com.crems.crems.service;

import java.util.List;
import com.crems.crems.domain.CremsFavorite;

/**
 * 职位收藏Service接口
 *
 * @author crems
 */
public interface ICremsFavoriteService
{
    public List<CremsFavorite> selectFavoriteList(CremsFavorite favorite);

    public CremsFavorite selectFavoriteById(Long favoriteId);

    public int insertFavorite(CremsFavorite favorite);

    public int deleteFavoriteById(Long favoriteId);

    public int deleteFavoriteByJobAndStudent(Long jobId, Long studentId);
}
