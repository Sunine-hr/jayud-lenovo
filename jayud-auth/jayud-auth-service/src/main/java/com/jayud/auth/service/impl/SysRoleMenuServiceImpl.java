package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysRoleMenu;
import com.jayud.auth.mapper.SysRoleMenuMapper;
import com.jayud.auth.service.ISysRoleMenuService;
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
 * 角色-菜单关联表 服务实现类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Slf4j
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {


    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public IPage<SysRoleMenu> selectPage(SysRoleMenu sysRoleMenu,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysRoleMenu> page=new Page<SysRoleMenu>(currentPage,pageSize);
        IPage<SysRoleMenu> pageList= sysRoleMenuMapper.pageList(page, sysRoleMenu);
        return pageList;
    }

    @Override
    public List<SysRoleMenu> selectList(SysRoleMenu sysRoleMenu){
        return sysRoleMenuMapper.list(sysRoleMenu);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysRoleMenuMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysRoleMenuMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

}
