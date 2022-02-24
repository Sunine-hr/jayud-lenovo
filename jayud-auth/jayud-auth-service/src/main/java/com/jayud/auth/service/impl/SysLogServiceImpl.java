package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysLog;
import com.jayud.auth.mapper.SysLogMapper;
import com.jayud.auth.service.ISysLogService;
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
 * 系统日志表 服务实现类
 *
 * @author jayud
 * @since 2022-02-24
 */
@Slf4j
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {


    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public IPage<SysLog> selectPage(SysLog sysLog,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysLog> page=new Page<SysLog>(currentPage,pageSize);
        IPage<SysLog> pageList= sysLogMapper.pageList(page, sysLog);
        return pageList;
    }

    @Override
    public List<SysLog> selectList(SysLog sysLog){
        return sysLogMapper.list(sysLog);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysLogMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysLogMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

}
