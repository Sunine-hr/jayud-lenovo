package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.auth.mapper.SysDictItemMapper;
import com.jayud.auth.service.ISysDictItemService;
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
 * 字典项 服务实现类
 *
 * @author jayud
 * @since 2022-02-23
 */
@Slf4j
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {


    @Autowired
    private SysDictItemMapper sysDictItemMapper;

    @Override
    public IPage<SysDictItem> selectPage(SysDictItem sysDictItem,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysDictItem> page=new Page<SysDictItem>(currentPage,pageSize);
        IPage<SysDictItem> pageList= sysDictItemMapper.pageList(page, sysDictItem);
        return pageList;
    }

    @Override
    public List<SysDictItem> selectList(SysDictItem sysDictItem){
        return sysDictItemMapper.list(sysDictItem);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysDictItemMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysDictItemMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public List<SysDictItem> selectItemByDictCode(String dictCode) {
        return sysDictItemMapper.selectItemByDictCode(dictCode);
    }

}
