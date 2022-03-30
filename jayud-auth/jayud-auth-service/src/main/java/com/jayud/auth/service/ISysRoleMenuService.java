package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色-菜单关联表 服务类
 *
 * @author jayud
 * @since 2022-02-21
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-21
     * @param: sysRoleMenu
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysRoleMenu>
     **/
    IPage<SysRoleMenu> selectPage(SysRoleMenu sysRoleMenu,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-21
     * @param: sysRoleMenu
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SysRoleMenu>
     **/
    List<SysRoleMenu> selectList(SysRoleMenu sysRoleMenu);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-21
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-02-21
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);


    void deleteByRoleId(Long roleId);

    List<Long> getMenuIdsByRoleId(Long roleId);

    /**
     * 获取按钮与角色关系
     * @param longs
     * @param  menuId
     * @return
     */
    List<SysRoleMenu> getSystemRoleMenuByRoleIdsAndActionCode(List<Long> longs, Long menuId);
}
