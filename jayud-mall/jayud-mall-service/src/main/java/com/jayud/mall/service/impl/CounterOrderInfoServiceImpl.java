package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.CounterOrderInfo;
import com.jayud.mall.mapper.CounterOrderInfoMapper;
import com.jayud.mall.service.ICounterOrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 柜子订单信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-15
 */
@Service
public class CounterOrderInfoServiceImpl extends ServiceImpl<CounterOrderInfoMapper, CounterOrderInfo> implements ICounterOrderInfoService {

}
