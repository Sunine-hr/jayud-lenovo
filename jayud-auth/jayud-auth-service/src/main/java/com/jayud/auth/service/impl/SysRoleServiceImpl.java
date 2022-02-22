package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.dto.AddSysRole;
import com.jayud.auth.model.po.SysUserRole;
import com.jayud.auth.service.ISysUserRoleService;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysRole;
import com.jayud.auth.mapper.SysRoleMapper;
import com.jayud.auth.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public IPage<SysRole> selectPage(SysRole sysRole,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){
        Page<SysRole> page = new Page<SysRole>(currentPage, pageSize);
        IPage<SysRole> pageList = sysRoleMapper.pageList(page, sysRole);
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
    public void logicDel(Long id){
        sysRoleMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public List<SysRole> selectSysRoleByUserId(Long userId) {
        return sysRoleMapper.selectSysRoleByUserId(userId);
    }

    @Override
    public boolean checkUnique(Long id, String roleName, String roleCode) {
        SysRole sysRole = this.getOne(new QueryWrapper<>(new SysRole().setIsDeleted(false).setRoleName(roleName)));
        if (sysRole != null && !sysRole.getId().equals(id)) {
            throw new JayudBizException("角色名称必须唯一");
        }
        sysRole = this.getOne(new QueryWrapper<>(new SysRole().setIsDeleted(false).setRoleCode(roleCode)));
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
    public List<SysRole> getRoleByUserId(Long userId) {

        return null;
    }

}
