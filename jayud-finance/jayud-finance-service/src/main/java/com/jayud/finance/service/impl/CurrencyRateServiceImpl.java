package com.jayud.finance.service.impl;

import com.jayud.finance.po.CurrencyRate;
import com.jayud.finance.mapper.CurrencyRateMapper;
import com.jayud.finance.service.ICurrencyRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 币种汇率 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-12
 */
@Service
public class CurrencyRateServiceImpl extends ServiceImpl<CurrencyRateMapper, CurrencyRate> implements ICurrencyRateService {

}
