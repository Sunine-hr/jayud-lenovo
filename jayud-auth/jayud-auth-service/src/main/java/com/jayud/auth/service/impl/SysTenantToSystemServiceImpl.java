package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysTenantToSystem;
import com.jayud.auth.mapper.SysTenantToSystemMapper;
import com.jayud.auth.service.ISysTenantToSystemService;
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
 * 租户-系统关联表 服务实现类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Slf4j
@Service
public class SysTenantToSystemServiceImpl extends ServiceImpl<SysTenantToSystemMapper, SysTenantToSystem> implements ISysTenantToSystemService {


    @Autowired
    private SysTenantToSystemMapper sysTenantToSystemMapper;

    @Override
    public IPage<SysTenantToSystem> selectPage(SysTenantToSystem sysTenantToSystem,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysTenantToSystem> page=new Page<SysTenantToSystem>(currentPage,pageSize);
        IPage<SysTenantToSystem> pageList= sysTenantToSystemMapper.pageList(page, sysTenantToSystem);
        return pageList;
    }

    @Override
    public List<SysTenantToSystem> selectList(SysTenantToSystem sysTenantToSystem){
        return sysTenantToSystemMapper.list(sysTenantToSystem);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysTenantToSystemMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysTenantToSystemMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

}
