package com.jayud.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author ciro
 * @since 2022-02-15
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
