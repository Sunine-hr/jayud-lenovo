package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysUserRole;
import com.jayud.auth.mapper.SysUserRoleMapper;
import com.jayud.auth.service.ISysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户-角色关联表 服务实现类
 *
 * @author jayud
 * @since 2022-02-22
 */
@Slf4j
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {


    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public IPage<SysUserRole> selectPage(SysUserRole sysUserRole,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysUserRole> page=new Page<SysUserRole>(currentPage,pageSize);
        IPage<SysUserRole> pageList= sysUserRoleMapper.pageList(page, sysUserRole);
        return pageList;
    }

    @Override
    public List<SysUserRole> selectList(SysUserRole sysUserRole){
        return sysUserRoleMapper.list(sysUserRole);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysUserRoleMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id) {
        sysUserRoleMapper.logicDel(id, CurrentUserUtil.getUsername());
    }

    @Override
    public boolean exitByRolesIds(List<Long> rolesIds) {
        QueryWrapper<SysUserRole> condition = new QueryWrapper<>();
        condition.lambda().in(SysUserRole::getRoleId, rolesIds).eq(SysUserRole::getIsDeleted, false);
        return this.count(condition) > 0;
    }

    @Override
    public void associatedEmployees(Long rolesId, List<Long> userIds) {
        QueryWrapper<SysUserRole> condition = new QueryWrapper<>();
        condition.lambda().eq(SysUserRole::getRoleId, rolesId).eq(SysUserRole::getIsDeleted, false);
        this.update(new SysUserRole().setIsDeleted(true), condition);

        List<SysUserRole> list = new ArrayList<>();
        for (Long userId : userIds) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(rolesId).setUserId(userId);
            list.add(sysUserRole);
        }
        this.saveBatch(list);
    }

}
