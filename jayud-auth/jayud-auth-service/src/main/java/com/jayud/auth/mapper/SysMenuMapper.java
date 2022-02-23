package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统菜单表 Mapper 接口
 * </p>
 *
 * @author jayud.dev
 * @since 2022-02-21
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据角色Ids，查询菜单
     * @param roleIds
     * @return
     */
    List<SysMenu> selectSysMenuByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * @description 根据菜单编码集合查询菜单
     * @author  ciro
     * @date   2022/2/23 15:40
     * @param: menuCodeList
     * @return: java.util.List<com.jayud.auth.model.po.SysMenu>
     **/
    List<SysMenu> selectSysMenuByMenuCodes(@Param("menuCodeList") List<String> menuCodeList);
}
