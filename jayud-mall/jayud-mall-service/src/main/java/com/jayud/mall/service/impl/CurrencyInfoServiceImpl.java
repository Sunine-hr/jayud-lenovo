package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.CurrencyInfoMapper;
import com.jayud.mall.model.bo.QueryCurrencyInfoForm;
import com.jayud.mall.model.po.CurrencyInfo;
import com.jayud.mall.model.vo.CurrencyInfoVO;
import com.jayud.mall.service.ICurrencyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 币种 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
@Service
public class CurrencyInfoServiceImpl extends ServiceImpl<CurrencyInfoMapper, CurrencyInfo> implements ICurrencyInfoService {

    @Autowired
    CurrencyInfoMapper currencyInfoMapper;

    @Override
    public List<CurrencyInfoVO> findCurrencyInfo(QueryCurrencyInfoForm form) {
        QueryWrapper<CurrencyInfo> queryWrapper = new QueryWrapper<>();
        String currencyCode = form.getCurrencyCode();
        if(currencyCode != null && currencyCode != ""){
            queryWrapper.like("currency_code", currencyCode);
        }
        String currencyName = form.getCurrencyName();
        if(currencyName != null && currencyName != ""){
            queryWrapper.like("currency_name", currencyName);
        }
        String countryCode = form.getCountryCode();
        if(countryCode != null && countryCode != ""){
            queryWrapper.like("country_code", countryCode);
        }
        String status = form.getStatus();
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }
        List<CurrencyInfo> currencyInfos = currencyInfoMapper.selectList(queryWrapper);
        List<CurrencyInfoVO> currencyInfoVOS = ConvertUtil.convertList(currencyInfos, CurrencyInfoVO.class);
        return currencyInfoVOS;
    }
}
