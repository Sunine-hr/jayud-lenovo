package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.scm.model.po.RateRmbFromBoc;
import com.jayud.scm.mapper.RateRmbFromBocMapper;
import com.jayud.scm.service.IRateRmbFromBocService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 中行汇率表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
@Service
public class RateRmbFromBocServiceImpl extends ServiceImpl<RateRmbFromBocMapper, RateRmbFromBoc> implements IRateRmbFromBocService {

    @Override
    public List<RateRmbFromBoc> getRateRmbFromBocByTimeAndCurrencyName(LocalDateTime publishTime, String currencyName) {
        QueryWrapper<RateRmbFromBoc> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(RateRmbFromBoc::getPublishTime,publishTime);
        queryWrapper.lambda().eq(RateRmbFromBoc::getCurrencyName,currencyName);
        queryWrapper.lambda().eq(RateRmbFromBoc::getVoided,0);
        return this.list(queryWrapper);
    }
}
