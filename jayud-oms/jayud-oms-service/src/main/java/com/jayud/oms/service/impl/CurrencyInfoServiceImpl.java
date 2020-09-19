package com.jayud.oms.service.impl;

import com.jayud.oms.model.po.CurrencyInfo;
import com.jayud.oms.mapper.CurrencyInfoMapper;
import com.jayud.oms.service.ICurrencyInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
