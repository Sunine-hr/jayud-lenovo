package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.bo.CheckForm;
import com.jayud.auth.model.bo.QueryForm;
import com.jayud.auth.model.bo.SysRoleActionCheckForm;
import com.jayud.auth.model.po.SysMenu;
import com.jayud.auth.model.vo.SysRoleActionCheckVO;
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
    public IPage<SysRoleActionCheckVO> selectPage(QueryForm form,
                                                  Integer currentPage,
                                                  Integer pageSize,
                                                  HttpServletRequest req){

        Page<SysRoleActionCheckVO> page=new Page<SysRoleActionCheckVO>(currentPage,pageSize);
        IPage<SysRoleActionCheckVO> pageList= sysRoleActionCheckMapper.pageList(page, form);
        return pageList;
    }

    @Override
    public List<SysRoleActionCheck> selectList(QueryForm form){
        return sysRoleActionCheckMapper.list(form);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysRoleActionCheckMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(List<Long> ids){
        sysRoleActionCheckMapper.logicDel(ids,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> querySysRoleActionCheckForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.querySysRoleActionCheckForExcel(paramMap);
    }

    @Override
    public void saveSysRoleActionCheck(SysRoleActionCheckForm sysRoleActionCheck) {
        SysUserVO systemUserByName = sysUserService.getSystemUserByName(CurrentUserUtil.getUsername());

        //查询同角色同级别是否存在数据
        List<Integer> integers = new ArrayList<>();
        for (Integer integer : sysRoleActionCheck.getActionId()) {
            QueryWrapper<SysRoleActionCheck> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysRoleActionCheck::getActionId,integer);
            queryWrapper.lambda().eq(SysRoleActionCheck::getRoleId,sysRoleActionCheck.getRoleId());
            queryWrapper.lambda().eq(SysRoleActionCheck::getCheckLevel,sysRoleActionCheck.getCheckLevel());
            queryWrapper.lambda().eq(SysRoleActionCheck::getIsDeleted,0);
            List<SysRoleActionCheck> list = this.list(queryWrapper);
            if(CollectionUtil.isEmpty(list)){
                integers.add(integer);
            }
        }


        List<SysMenu> sysMenus = sysMenuService.getByIds(integers);
        List<SysRoleActionCheck> systemRoleActionChecks = new ArrayList<>();
        for (SysMenu sysMenu : sysMenus) {
            SysRoleActionCheck roleActionCheck = new SysRoleActionCheck();
            roleActionCheck.setCreateBy(systemUserByName.getUserName());
            roleActionCheck.setCreateTime(new Date());
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

    @Override
    public List<SysRoleActionCheckVO> getList(CheckForm checkForm) {
        return this.baseMapper.getList(checkForm.getMenuCode());
    }

    @Override
    public List<SysRoleActionCheckVO> getListByCheckLevelAndMenuCode(Integer checkLevel, String menuCode) {
        return this.baseMapper.getListByCheckLevelAndMenuCode(checkLevel,menuCode);
    }

}
