package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysUrl;
import com.jayud.auth.mapper.SysUrlMapper;
import com.jayud.auth.service.ISysUrlService;
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
 * 系统链接表 服务实现类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Slf4j
@Service
public class SysUrlServiceImpl extends ServiceImpl<SysUrlMapper, SysUrl> implements ISysUrlService {


    @Autowired
    private SysUrlMapper sysUrlMapper;

    @Override
    public IPage<SysUrl> selectPage(SysUrl sysUrl,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysUrl> page=new Page<SysUrl>(currentPage,pageSize);
        IPage<SysUrl> pageList= sysUrlMapper.pageList(page, sysUrl);
        return pageList;
    }

    @Override
    public List<SysUrl> selectList(SysUrl sysUrl){
        return sysUrlMapper.list(sysUrl);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysUrlMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysUrlMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

}
