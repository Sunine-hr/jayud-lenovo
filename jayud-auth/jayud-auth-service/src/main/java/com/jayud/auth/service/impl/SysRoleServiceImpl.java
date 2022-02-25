package com.jayud.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.dto.AddSysRole;
import com.jayud.auth.model.po.SysRoleMenu;
import com.jayud.auth.model.po.SysUserRole;
import com.jayud.auth.model.vo.SysRoleVO;
import com.jayud.auth.service.ISysRoleMenuService;
import com.jayud.auth.service.ISysUserRoleService;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysRole;
import com.jayud.auth.mapper.SysRoleMapper;
import com.jayud.auth.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色表 服务实现类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {


    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    @Override
    public IPage<SysRoleVO> selectPage(SysRole sysRole,
                                       Integer currentPage,
                                       Integer pageSize,
                                       HttpServletRequest req) {
        Page<SysRole> page = new Page<SysRole>(currentPage, pageSize);
        IPage<SysRoleVO> pageList = sysRoleMapper.pageList(page, sysRole);
        return pageList;
    }

    @Override
    public List<SysRole> selectList(SysRole sysRole) {
        return sysRoleMapper.list(sysRole);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id) {
        sysRoleMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id) {
        sysRoleMapper.logicDel(id, CurrentUserUtil.getUsername());
    }

    @Override
    public List<SysRole> selectSysRoleByUserId(Long userId) {
        return sysRoleMapper.selectSysRoleByUserId(userId);
    }

    @Override
    public boolean checkUnique(Long id, String roleName, String roleCode) {
        SysRole sysRole = this.getOne(new QueryWrapper<>(new SysRole().setIsDeleted(false).setRoleName(roleName).setTenantCode(CurrentUserUtil.getUserTenantCode())));
        if (sysRole != null && !sysRole.getId().equals(id)) {
            throw new JayudBizException("角色名称必须唯一");
        }
        sysRole = this.getOne(new QueryWrapper<>(new SysRole().setIsDeleted(false).setRoleCode(roleCode).setTenantCode(CurrentUserUtil.getUserTenantCode())));
        if (sysRole != null && !sysRole.getId().equals(id)) {
            throw new JayudBizException("角色编码必须唯一");
        }
        return true;
    }

    @Override
    public void addOrUpdate(AddSysRole form) {
        SysRole sysRole = ConvertUtil.convert(form, SysRole.class);
        if (sysRole.getId() == null) {
            sysRole.setCreateTime(new Date()).setCreateBy(CurrentUserUtil.getUsername());
            this.save(sysRole);
        } else {
            sysRole.setUpdateTime(new Date()).setUpdateBy(CurrentUserUtil.getUsername());
            this.updateById(sysRole);
        }
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        List<SysUserRole> tmps = this.sysUserRoleService.getByCondition(new SysUserRole().setUserId(userId).setIsDeleted(false));
        List<Long> roleIds = tmps.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
        return roleIds;
    }

    @Override
    public void setRoles(Long userId, List<Long> roleIds) {
        this.sysUserRoleService.deleteByUserId(userId);
        List<SysUserRole> list = new ArrayList<>();
        for (Long roleId : roleIds) {
            SysUserRole tmp = new SysUserRole();
            tmp.setRoleId(roleId).setUserId(userId);
            list.add(tmp);
        }
        this.sysUserRoleService.saveBatch(list);
    }

    @Override
    public void setRolePermissions(Long roleId, List<Long> menuIds) {
        this.sysRoleMenuService.deleteByRoleId(roleId);

        List<SysRoleMenu> list = new ArrayList<>();
        for (Long menuId : menuIds) {
            SysRoleMenu tmp = new SysRoleMenu();
            tmp.setMenuId(menuId).setRoleId(roleId);
            list.add(tmp);
        }
        this.sysRoleMenuService.saveBatch(list);
    }

    @Override
    public List<SysRole> selectRoleByUsername(String username) {
        return sysRoleMapper.selectRoleByUsername(username);
    }

}
