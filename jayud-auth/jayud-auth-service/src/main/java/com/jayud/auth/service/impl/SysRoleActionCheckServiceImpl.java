package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.bo.SysRoleActionCheckForm;
import com.jayud.auth.model.po.SysMenu;
import com.jayud.auth.model.vo.SysUserVO;
import com.jayud.auth.service.ISysMenuService;
import com.jayud.auth.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysRoleActionCheck;
import com.jayud.auth.mapper.SysRoleActionCheckMapper;
import com.jayud.auth.service.ISysRoleActionCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 角色审核级别权限 服务实现类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Slf4j
@Service
public class SysRoleActionCheckServiceImpl extends ServiceImpl<SysRoleActionCheckMapper, SysRoleActionCheck> implements ISysRoleActionCheckService {


    @Autowired
    private SysRoleActionCheckMapper sysRoleActionCheckMapper;

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public IPage<SysRoleActionCheck> selectPage(SysRoleActionCheck sysRoleActionCheck,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysRoleActionCheck> page=new Page<SysRoleActionCheck>(currentPage,pageSize);
        IPage<SysRoleActionCheck> pageList= sysRoleActionCheckMapper.pageList(page, sysRoleActionCheck);
        return pageList;
    }

    @Override
    public List<SysRoleActionCheck> selectList(SysRoleActionCheck sysRoleActionCheck){
        return sysRoleActionCheckMapper.list(sysRoleActionCheck);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysRoleActionCheckMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysRoleActionCheckMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> querySysRoleActionCheckForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.querySysRoleActionCheckForExcel(paramMap);
    }

    @Override
    public void saveSysRoleActionCheck(SysRoleActionCheckForm sysRoleActionCheck) {
        SysUserVO systemUserByName = sysUserService.getSystemUserByName(CurrentUserUtil.getUsername());

        List<SysMenu> sysMenus = sysMenuService.getByIds(sysRoleActionCheck.getActionIds());
        List<SysRoleActionCheck> systemRoleActionChecks = new ArrayList<>();
        for (SysMenu sysMenu : sysMenus) {
            SysRoleActionCheck roleActionCheck = new SysRoleActionCheck();
            roleActionCheck.setCrtBy(systemUserByName.getId());
            roleActionCheck.setCrtByDtm(LocalDateTime.now());
            roleActionCheck.setCrtByName(systemUserByName.getUserName());
            roleActionCheck.setRoleId(sysRoleActionCheck.getRoleId());
            roleActionCheck.setActionCode(sysMenu.getCode());
            roleActionCheck.setActionId(sysMenu.getId());
            roleActionCheck.setCheckLevel(sysRoleActionCheck.getCheckLevel() != null ?sysRoleActionCheck.getCheckLevel():null);
            roleActionCheck.setCheckMoney(sysRoleActionCheck.getCheckMoney() != null ?sysRoleActionCheck.getCheckMoney():null);
            systemRoleActionChecks.add(roleActionCheck);
        }
        boolean result = this.saveBatch(systemRoleActionChecks);
        if(result){
            log.warn("添加成功");
        }
    }

}
