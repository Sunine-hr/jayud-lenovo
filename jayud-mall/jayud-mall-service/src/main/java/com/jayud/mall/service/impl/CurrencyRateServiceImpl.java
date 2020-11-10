package com.jayud.mall.service.impl;

import com.jayud.mall.model.bo.QueryCurrencyRateForm;
import com.jayud.mall.model.po.CurrencyRate;
import com.jayud.mall.mapper.CurrencyRateMapper;
import com.jayud.mall.model.vo.CurrencyRateVO;
import com.jayud.mall.service.ICurrencyRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 币种汇率 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
@Service
public class CurrencyRateServiceImpl extends ServiceImpl<CurrencyRateMapper, CurrencyRate> implements ICurrencyRateService {

    @Autowired
    CurrencyRateMapper currencyRateMapper;

    @Override
    public List<CurrencyRateVO> findCurrencyRate(QueryCurrencyRateForm form) {
        List<CurrencyRateVO> list = currencyRateMapper.findCurrencyRate(form);
        return list;
    }
}
