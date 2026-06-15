package com.crems.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.crems.crems.domain.CremsStudent;
import com.crems.crems.domain.CremsInterview;
import com.crems.crems.domain.CremsApplication;
import com.crems.crems.domain.CremsFavorite;
import com.crems.crems.mapper.CremsStudentMapper;
import com.crems.crems.mapper.CremsInterviewMapper;
import com.crems.crems.mapper.CremsApplicationMapper;
import com.crems.crems.mapper.CremsFavoriteMapper;
import com.crems.crems.service.ICremsStudentService;

/**
 * 学生信息Service业务层处理
 *
 * @author crems
 */
@Service
public class CremsStudentServiceImpl implements ICremsStudentService
{
    @Autowired
    private CremsStudentMapper studentMapper;
    @Autowired
    private CremsInterviewMapper interviewMapper;
    @Autowired
    private CremsApplicationMapper applicationMapper;
    @Autowired
    private CremsFavoriteMapper favoriteMapper;

    @Override
    public CremsStudent selectStudentById(Long studentId)
    {
        return studentMapper.selectStudentById(studentId);
    }

    @Override
    public CremsStudent selectStudentByUserId(Long userId)
    {
        return studentMapper.selectStudentByUserId(userId);
    }

    @Override
    public List<CremsStudent> selectStudentList(CremsStudent student)
    {
        return studentMapper.selectStudentList(student);
    }

    @Override
    @Transactional
    public int insertStudent(CremsStudent student)
    {
        return studentMapper.insertStudent(student);
    }

    @Override
    @Transactional
    public int updateStudent(CremsStudent student)
    {
        return studentMapper.updateStudent(student);
    }

    @Override
    @Transactional
    public int deleteStudentByIds(Long[] studentIds)
    {
        for (Long studentId : studentIds) {
            deleteStudentCascade(studentId);
        }
        return studentMapper.deleteStudentByIds(studentIds);
    }

    @Override
    @Transactional
    public int deleteStudentById(Long studentId)
    {
        deleteStudentCascade(studentId);
        return studentMapper.deleteStudentById(studentId);
    }

    /**
     * 级联删除学生关联数据：favorite -> interview -> application
     */
    private void deleteStudentCascade(Long studentId) {
        // 删除收藏
        CremsFavorite favQuery = new CremsFavorite();
        favQuery.setStudentId(studentId);
        java.util.List<CremsFavorite> favorites = favoriteMapper.selectFavoriteList(favQuery);
        for (CremsFavorite fav : favorites) {
            favoriteMapper.deleteFavoriteById(fav.getFavoriteId());
        }

        // 删除面试
        CremsInterview interviewQuery = new CremsInterview();
        interviewQuery.setStudentId(studentId);
        java.util.List<CremsInterview> interviews = interviewMapper.selectInterviewList(interviewQuery);
        for (CremsInterview interview : interviews) {
            interviewMapper.deleteInterviewById(interview.getInterviewId());
        }

        // 删除投递
        CremsApplication appQuery = new CremsApplication();
        appQuery.setStudentId(studentId);
        java.util.List<CremsApplication> applications = applicationMapper.selectApplicationList(appQuery);
        for (CremsApplication app : applications) {
            applicationMapper.deleteApplicationById(app.getApplicationId());
        }
    }
}
