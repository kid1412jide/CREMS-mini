package com.ruoyi.crems.mapper;

import java.util.List;
import com.ruoyi.crems.domain.CremsStudent;

/**
 * 学生信息Mapper接口
 *
 * @author crems
 */
public interface CremsStudentMapper
{
    public CremsStudent selectStudentById(Long studentId);

    public List<CremsStudent> selectStudentList(CremsStudent student);

    public int insertStudent(CremsStudent student);

    public int updateStudent(CremsStudent student);

    public int deleteStudentById(Long studentId);

    public int deleteStudentByIds(Long[] studentIds);
}
