package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
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
        String tenantCode = sysUser.getTenantCode();//租户编码

        List<SysRole> roles = sysRoleService.selectSysRoleByUserId(userId);
        List<Long> roleIds = new ArrayList<>();
        if(CollUtil.isNotEmpty(roles)){
            roles.forEach(role -> {
                roleIds.add(role.getId());
            });
        }else{
            roleIds.add(0L);
        }

        List<SysMenu> sysMenus = this.selectSysMenuByRoleIds(roleIds);


        return null;
    }

    @Override
    public List<SysMenu> selectSysMenuByRoleIds(List<Long> roleIds) {
        return baseMapper.selectSysMenuByRoleIds(roleIds);
    }
}
