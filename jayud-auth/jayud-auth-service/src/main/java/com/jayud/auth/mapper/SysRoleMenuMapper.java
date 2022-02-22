package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色-菜单关联表 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-21
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-21
     * @param: page
     * @param: sysRoleMenu
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysRoleMenu>
     **/
    IPage<SysRoleMenu> pageList(@Param("page") Page<SysRoleMenu> page, @Param("sysRoleMenu") SysRoleMenu sysRoleMenu);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-21
     * @param: sysRoleMenu
     * @return: java.util.List<com.jayud.auth.model.po.SysRoleMenu>
     **/
    List<SysRoleMenu> list(@Param("sysRoleMenu") SysRoleMenu sysRoleMenu);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-02-21
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-02-21
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);
}
