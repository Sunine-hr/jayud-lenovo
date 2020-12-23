package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.CurrencyInfoMapper;
import com.jayud.oms.model.po.CurrencyInfo;
import com.jayud.oms.model.vo.CurrencyInfoVO;
import com.jayud.oms.service.ICurrencyInfoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 币种 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-19
 */
@Service
public class CurrencyInfoServiceImpl extends ServiceImpl<CurrencyInfoMapper, CurrencyInfo> implements ICurrencyInfoService {

    @Override
    public List<CurrencyInfoVO> findCurrencyInfo(String createdTimeStr) {
        String createdMonthStr = createdTimeStr.substring(0,7);//把时间处理到月
        List<CurrencyInfoVO> currencyInfoVOS = baseMapper.findCurrencyInfo(createdMonthStr);
        //汇率小于0的数据剔除
        List<CurrencyInfoVO> newCurrencyInfos = new ArrayList<>();
        for (CurrencyInfoVO currencyInfoVO : currencyInfoVOS) {
            if(!(currencyInfoVO.getExchangeRate() == null || currencyInfoVO.getExchangeRate().compareTo(new BigDecimal(0)) == 0)){
               newCurrencyInfos.add(currencyInfoVO);
            }
        }
        return newCurrencyInfos;
    }
}
