package com.ruoyi.crems.service;

import java.util.List;
import com.ruoyi.crems.domain.CremsStudent;

/**
 * 学生信息Service接口
 *
 * @author crems
 */
public interface ICremsStudentService
{
    public CremsStudent selectStudentById(Long studentId);

    public List<CremsStudent> selectStudentList(CremsStudent student);

    public int insertStudent(CremsStudent student);

    public int updateStudent(CremsStudent student);

    public int deleteStudentByIds(Long[] studentIds);

    public int deleteStudentById(Long studentId);
}
