package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.model.po.OrderInfo;
import com.jayud.oms.mapper.OrderInfoMapper;
import com.jayud.oms.service.IOrderInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 主订单基础数据表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

}
