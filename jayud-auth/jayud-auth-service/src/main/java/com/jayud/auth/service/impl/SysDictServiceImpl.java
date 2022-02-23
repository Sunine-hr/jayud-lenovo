package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.exception.JayudBizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysDict;
import com.jayud.auth.mapper.SysDictMapper;
import com.jayud.auth.service.ISysDictService;
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
 * 字典 服务实现类
 *
 * @author jayud
 * @since 2022-02-23
 */
@Slf4j
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {


    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public IPage<SysDict> selectPage(SysDict sysDict,
                                     Integer currentPage,
                                     Integer pageSize,
                                     HttpServletRequest req) {

        Page<SysDict> page = new Page<SysDict>(currentPage, pageSize);
        IPage<SysDict> pageList = sysDictMapper.pageList(page, sysDict);
        return pageList;
    }

    @Override
    public List<SysDict> selectList(SysDict sysDict) {
        return sysDictMapper.list(sysDict);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id) {
        sysDictMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id) {
        sysDictMapper.logicDel(id, CurrentUserUtil.getUsername());
    }


    @Override
    public void checkUnique(SysDict sysDict) {
        SysDict tmp = this.baseMapper.selectOne(new QueryWrapper<>(new SysDict().setDictCode(sysDict.getDictCode()).setIsDeleted(false)));
        if (tmp != null && !tmp.getId().equals(sysDict.getId())) {
            throw new JayudBizException("字典编码已存在");
        }

        tmp = this.baseMapper.selectOne(new QueryWrapper<>(new SysDict().setDictCode(sysDict.getDictName()).setIsDeleted(false)));
        if (tmp != null && !tmp.getId().equals(sysDict.getId())) {
            throw new JayudBizException("字典名称已存在");
        }
    }

}
