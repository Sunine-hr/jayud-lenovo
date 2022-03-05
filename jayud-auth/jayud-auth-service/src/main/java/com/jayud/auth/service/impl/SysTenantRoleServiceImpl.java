package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysTenantRole;
import com.jayud.auth.mapper.SysTenantRoleMapper;
import com.jayud.auth.service.ISysTenantRoleService;
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
 * 租户规则配置 服务实现类
 *
 * @author jayud
 * @since 2022-03-05
 */
@Slf4j
@Service
public class SysTenantRoleServiceImpl extends ServiceImpl<SysTenantRoleMapper, SysTenantRole> implements ISysTenantRoleService {


    @Autowired
    private SysTenantRoleMapper sysTenantRoleMapper;

    @Override
    public IPage<SysTenantRole> selectPage(SysTenantRole sysTenantRole,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysTenantRole> page=new Page<SysTenantRole>(currentPage,pageSize);
        IPage<SysTenantRole> pageList= sysTenantRoleMapper.pageList(page, sysTenantRole);
        return pageList;
    }

    @Override
    public List<SysTenantRole> selectList(SysTenantRole sysTenantRole){
        return sysTenantRoleMapper.list(sysTenantRole);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysTenantRoleMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysTenantRoleMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> querySysTenantRoleForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.querySysTenantRoleForExcel(paramMap);
    }

    @Override
    public BaseResult<SysTenantRole> selectByTenatCode(String tenantCode) {
        LambdaQueryWrapper<SysTenantRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysTenantRole::getTenantCode,tenantCode);
        SysTenantRole sysTenantRole = this.getOne(lambdaQueryWrapper);
        if (sysTenantRole == null){
            return BaseResult.error();
        }
        return BaseResult.ok(sysTenantRole);
    }

}
