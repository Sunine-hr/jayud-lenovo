package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.CurrencyInfo;
import com.jayud.auth.mapper.CurrencyInfoMapper;
import com.jayud.auth.service.ICurrencyInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 币种 服务实现类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Service
public class CurrencyInfoServiceImpl extends ServiceImpl<CurrencyInfoMapper, CurrencyInfo> implements ICurrencyInfoService {


    @Autowired
    private CurrencyInfoMapper currencyInfoMapper;

    @Override
    public IPage<CurrencyInfo> selectPage(CurrencyInfo currencyInfo,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CurrencyInfo> page=new Page<CurrencyInfo>(currentPage,pageSize);
        IPage<CurrencyInfo> pageList= currencyInfoMapper.pageList(page, currencyInfo);
        return pageList;
    }

    @Override
    public List<CurrencyInfo> selectList(CurrencyInfo currencyInfo){
        return currencyInfoMapper.list(currencyInfo);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        currencyInfoMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        currencyInfoMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCurrencyInfoForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCurrencyInfoForExcel(paramMap);
    }

    @Override
    public List<CurrencyInfo> findCurrencyInfo(String createdTimeStr) {
        String createdMonthStr = createdTimeStr.substring(0, 7);//把时间处理到月
        List<CurrencyInfo> currencyInfoVOS = baseMapper.findCurrencyInfo(createdMonthStr);
        //汇率小于0的数据剔除
        List<CurrencyInfo> newCurrencyInfos = new ArrayList<>();
        for (CurrencyInfo currencyInfoVO : currencyInfoVOS) {
            if (!(currencyInfoVO.getExchangeRate() == null || currencyInfoVO.getExchangeRate().compareTo(new BigDecimal(0)) == 0)) {
                newCurrencyInfos.add(currencyInfoVO);
            }
        }
        return newCurrencyInfos;
    }

}
