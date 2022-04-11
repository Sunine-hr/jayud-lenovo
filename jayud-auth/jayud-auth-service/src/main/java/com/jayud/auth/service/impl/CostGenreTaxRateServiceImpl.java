package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.CostGenreTaxRate;
import com.jayud.auth.mapper.CostGenreTaxRateMapper;
import com.jayud.auth.service.ICostGenreTaxRateService;
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
 * 费用类型税率表 服务实现类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Service
public class CostGenreTaxRateServiceImpl extends ServiceImpl<CostGenreTaxRateMapper, CostGenreTaxRate> implements ICostGenreTaxRateService {


    @Autowired
    private CostGenreTaxRateMapper costGenreTaxRateMapper;

    @Override
    public IPage<CostGenreTaxRate> selectPage(CostGenreTaxRate costGenreTaxRate,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CostGenreTaxRate> page=new Page<CostGenreTaxRate>(currentPage,pageSize);
        IPage<CostGenreTaxRate> pageList= costGenreTaxRateMapper.pageList(page, costGenreTaxRate);
        return pageList;
    }

    @Override
    public List<CostGenreTaxRate> selectList(CostGenreTaxRate costGenreTaxRate){
        return costGenreTaxRateMapper.list(costGenreTaxRate);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        costGenreTaxRateMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        costGenreTaxRateMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCostGenreTaxRateForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCostGenreTaxRateForExcel(paramMap);
    }

}
