package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysPost;
import com.jayud.auth.mapper.SysPostMapper;
import com.jayud.auth.service.ISysPostService;
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
 * 岗位表 服务实现类
 *
 * @author jayud
 * @since 2022-02-22
 */
@Slf4j
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements ISysPostService {


    @Autowired
    private SysPostMapper sysPostMapper;

    @Override
    public IPage<SysPost> selectPage(SysPost sysPost,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysPost> page=new Page<SysPost>(currentPage,pageSize);
        IPage<SysPost> pageList= sysPostMapper.pageList(page, sysPost);
        return pageList;
    }

    @Override
    public List<SysPost> selectList(SysPost sysPost){
        return sysPostMapper.list(sysPost);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysPostMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysPostMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

}
