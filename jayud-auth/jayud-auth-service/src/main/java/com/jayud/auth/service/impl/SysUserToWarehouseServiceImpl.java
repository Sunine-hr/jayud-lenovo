package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysUserToWarehouse;
import com.jayud.auth.mapper.SysUserToWarehouseMapper;
import com.jayud.auth.service.ISysUserToWarehouseService;
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
 * 用户与仓库关联表 服务实现类
 *
 * @author jayud
 * @since 2022-04-08
 */
@Slf4j
@Service
public class SysUserToWarehouseServiceImpl extends ServiceImpl<SysUserToWarehouseMapper, SysUserToWarehouse> implements ISysUserToWarehouseService {


    @Autowired
    private SysUserToWarehouseMapper sysUserToWarehouseMapper;

    @Override
    public IPage<SysUserToWarehouse> selectPage(SysUserToWarehouse sysUserToWarehouse,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysUserToWarehouse> page=new Page<SysUserToWarehouse>(currentPage,pageSize);
        IPage<SysUserToWarehouse> pageList= sysUserToWarehouseMapper.pageList(page, sysUserToWarehouse);
        return pageList;
    }

    @Override
    public List<SysUserToWarehouse> selectList(SysUserToWarehouse sysUserToWarehouse){
        return sysUserToWarehouseMapper.list(sysUserToWarehouse);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysUserToWarehouseMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysUserToWarehouseMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> querySysUserToWarehouseForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.querySysUserToWarehouseForExcel(paramMap);
    }

}
