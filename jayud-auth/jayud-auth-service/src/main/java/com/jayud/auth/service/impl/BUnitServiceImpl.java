package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.BUnit;
import com.jayud.auth.mapper.BUnitMapper;
import com.jayud.auth.service.IBUnitService;
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
 * 计量单位代码表 服务实现类
 *
 * @author jayud
 * @since 2022-03-25
 */
@Slf4j
@Service
public class BUnitServiceImpl extends ServiceImpl<BUnitMapper, BUnit> implements IBUnitService {


    @Autowired
    private BUnitMapper bUnitMapper;

    @Override
    public IPage<BUnit> selectPage(BUnit bUnit,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<BUnit> page=new Page<BUnit>(currentPage,pageSize);
        IPage<BUnit> pageList= bUnitMapper.pageList(page, bUnit);
        return pageList;
    }

    @Override
    public List<BUnit> selectList(BUnit bUnit){
        return bUnitMapper.list(bUnit);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        bUnitMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        bUnitMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryBUnitForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryBUnitForExcel(paramMap);
    }

    @Override
    public List<String> getUnits() {
        return this.bUnitMapper.getUnits();
    }

}
