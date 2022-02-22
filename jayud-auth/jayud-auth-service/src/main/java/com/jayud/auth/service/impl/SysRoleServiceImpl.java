package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Override
    public IPage<SysRole> selectPage(SysRole sysRole,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysRole> page=new Page<SysRole>(currentPage,pageSize);
        IPage<SysRole> pageList= sysRoleMapper.pageList(page, sysRole);
        return pageList;
    }

    @Override
    public List<SysRole> selectList(SysRole sysRole){
        return sysRoleMapper.list(sysRole);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
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

}
