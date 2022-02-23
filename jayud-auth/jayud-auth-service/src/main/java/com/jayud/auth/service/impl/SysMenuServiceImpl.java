package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jayud.auth.model.po.SysMenu;
import com.jayud.auth.mapper.SysMenuMapper;
import com.jayud.auth.model.po.SysRole;
import com.jayud.auth.model.po.SysUser;
import com.jayud.auth.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.auth.service.ISysRoleService;
import com.jayud.auth.service.ISysUserService;
import com.jayud.common.dto.AuthUserDetail;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author jayud.dev
 * @since 2022-02-21
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService sysRoleService;

    @Override
    public JSONObject getUserMenuByToken() {
        AuthUserDetail userDetail = CurrentUserUtil.getUserDetail();
        Long userId = userDetail.getId();
        SysUser sysUser = sysUserService.getById(userId);
        //租户编码
        String tenantCode = sysUser.getTenantCode();

        List<SysRole> roles = sysRoleService.selectSysRoleByUserId(userId);
        List<Long> roleIds = new ArrayList<>();
        if(CollUtil.isNotEmpty(roles)){
            roles.forEach(role -> {
                roleIds.add(role.getId());
            });
        }else{
            roleIds.add(0L);
        }

        // 原始的数据一条一条的
        List<SysMenu> menus = this.selectSysMenuByRoleIds(roleIds);
        // 构建好的菜单树，第一层菜单的pid是0
        List<SysMenu> menuTree = buildMenuTree(menus, "0");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roleIds", roleIds);
        jsonObject.put("roles", roles);
        jsonObject.put("menuNodeList", menuTree);
        return jsonObject;
    }

    @Override
    public List<SysMenu> selectSysMenuByRoleIds(List<Long> roleIds) {
        return baseMapper.selectSysMenuByRoleIds(roleIds);
    }

    @Override
    public List<SysMenu> selectSysMenuByMenuCodes(List<String> menuCodeList) {
        return baseMapper.selectSysMenuByMenuCodes(menuCodeList);
    }

    /**
     * 构建菜单树
     *
     * @param menuList
     * @param pid
     * @return
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> menuList, String pid) {
        List<SysMenu> treeList = new ArrayList<>();
        menuList.forEach(menu -> {
            if (StrUtil.equals(pid, menu.getParentId().toString())) {
                menu.setChildren(buildMenuTree(menuList, menu.getId().toString()));
                treeList.add(menu);
            }
        });
        return treeList;
    }

}
