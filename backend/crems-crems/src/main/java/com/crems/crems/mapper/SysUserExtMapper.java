package com.crems.crems.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 系统用户扩展Mapper（用于业务模块同步更新用户信息）
 */
public interface SysUserExtMapper
{
    /**
     * 更新用户昵称
     */
    public int updateNickName(@Param("userId") Long userId, @Param("nickName") String nickName);
}
