package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.CurrencyRate;
import com.jayud.auth.mapper.CurrencyRateMapper;
import com.jayud.auth.service.ICurrencyRateService;
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
 * 币种汇率 服务实现类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Service
public class CurrencyRateServiceImpl extends ServiceImpl<CurrencyRateMapper, CurrencyRate> implements ICurrencyRateService {


    @Autowired
    private CurrencyRateMapper currencyRateMapper;

    @Override
    public IPage<CurrencyRate> selectPage(CurrencyRate currencyRate,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CurrencyRate> page=new Page<CurrencyRate>(currentPage,pageSize);
        IPage<CurrencyRate> pageList= currencyRateMapper.pageList(page, currencyRate);
        return pageList;
    }

    @Override
    public List<CurrencyRate> selectList(CurrencyRate currencyRate){
        return currencyRateMapper.list(currencyRate);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        currencyRateMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        currencyRateMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCurrencyRateForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCurrencyRateForExcel(paramMap);
    }

}
