package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 后台用户表 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-21
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-02-21
     * @param: page
     * @param: sysUser
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysUser>
     **/
    IPage<SysUser> pageList(@Param("page") Page<SysUser> page, @Param("sysUser") SysUser sysUser);

    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-02-21
     * @param: sysUser
     * @return: java.util.List<com.jayud.auth.model.po.SysUser>
     **/
    List<SysUser> list(@Param("sysUser") SysUser sysUser);


    /**
     * @description 根据id物理删除
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id, @Param("username") String username);
}
