package com.crems.crems.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.crems.crems.domain.CremsStudent;
import com.crems.crems.mapper.CremsStudentMapper;
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
        return studentMapper.deleteStudentByIds(studentIds);
    }

    @Override
    @Transactional
    public int deleteStudentById(Long studentId)
    {
        return studentMapper.deleteStudentById(studentId);
    }
}
