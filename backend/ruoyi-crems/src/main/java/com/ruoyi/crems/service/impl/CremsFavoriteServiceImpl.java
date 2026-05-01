package com.ruoyi.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.crems.domain.CremsFavorite;
import com.ruoyi.crems.mapper.CremsFavoriteMapper;
import com.ruoyi.crems.service.ICremsFavoriteService;

/**
 * 职位收藏Service业务层处理
 *
 * @author crems
 */
@Service
public class CremsFavoriteServiceImpl implements ICremsFavoriteService
{
    @Autowired
    private CremsFavoriteMapper favoriteMapper;

    @Override
    public List<CremsFavorite> selectFavoriteList(CremsFavorite favorite)
    {
        return favoriteMapper.selectFavoriteList(favorite);
    }

    @Override
    public int insertFavorite(CremsFavorite favorite)
    {
        return favoriteMapper.insertFavorite(favorite);
    }

    @Override
    public int deleteFavoriteById(Long favoriteId)
    {
        return favoriteMapper.deleteFavoriteById(favoriteId);
    }

    @Override
    public int deleteFavoriteByJobAndStudent(Long jobId, Long studentId)
    {
        return favoriteMapper.deleteFavoriteByJobAndStudent(jobId, studentId);
    }
}
