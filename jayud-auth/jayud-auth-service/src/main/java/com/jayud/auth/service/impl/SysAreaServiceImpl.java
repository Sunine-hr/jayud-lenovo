package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysArea;
import com.jayud.auth.mapper.SysAreaMapper;
import com.jayud.auth.service.ISysAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 系统-中国行政地区表 服务实现类
 *
 * @author jayud
 * @since 2022-02-26
 */
@Slf4j
@Service
public class SysAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysArea> implements ISysAreaService {


    @Autowired
    private SysAreaMapper sysAreaMapper;

    @Override
    public IPage<SysArea> selectPage(SysArea sysArea,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysArea> page=new Page<SysArea>(currentPage,pageSize);
        IPage<SysArea> pageList= sysAreaMapper.pageList(page, sysArea);
        return pageList;
    }

    @Override
    public List<SysArea> selectList(SysArea sysArea){
        return sysAreaMapper.list(sysArea);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysAreaMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysAreaMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

}
