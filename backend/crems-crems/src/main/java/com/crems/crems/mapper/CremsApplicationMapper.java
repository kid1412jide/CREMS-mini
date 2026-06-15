package com.crems.crems.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.crems.crems.domain.CremsApplication;

/**
 * 简历投递Mapper接口
 *
 * @author crems
 */
public interface CremsApplicationMapper
{
    public CremsApplication selectApplicationById(Long applicationId);

    public List<CremsApplication> selectApplicationList(CremsApplication application);

    public int insertApplication(CremsApplication application);

    public int updateApplication(CremsApplication application);

    public int updateApplicationStatusIfCurrent(@Param("applicationId") Long applicationId,
                                                @Param("currentStatus") String currentStatus,
                                                @Param("newStatus") String newStatus,
                                                @Param("updateBy") String updateBy);

    public int deleteApplicationById(Long applicationId);

    public int deleteApplicationByIds(Long[] applicationIds);
}
