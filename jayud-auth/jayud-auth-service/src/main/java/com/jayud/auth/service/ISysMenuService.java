package com.jayud.auth.service;

import com.alibaba.fastjson.JSONObject;
import com.jayud.auth.model.po.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author jayud.dev
 * @since 2022-02-21
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 用户菜单
     * @return
     */
    JSONObject getUserMenuByToken();

    /**
     * 根据角色ids，获取菜单
     */
    List<SysMenu> selectSysMenuByRoleIds(List<Long> roleIds);

    /**
     * @description 根据菜单编码集合查询菜单
     * @author  ciro
     * @date   2022/2/23 15:41
     * @param: menuCodeList
     * @return: java.util.List<com.jayud.auth.model.po.SysMenu>
     **/
    List<SysMenu> selectSysMenuByMenuCodes(List<String> menuCodeList);

}
