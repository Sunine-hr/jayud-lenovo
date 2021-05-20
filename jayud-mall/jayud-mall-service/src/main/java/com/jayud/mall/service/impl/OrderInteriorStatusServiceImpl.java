package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.OrderInteriorStatus;
import com.jayud.mall.mapper.OrderInteriorStatusMapper;
import com.jayud.mall.service.IOrderInteriorStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单内部状态表(非流程状态) 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-20
 */
@Service
public class OrderInteriorStatusServiceImpl extends ServiceImpl<OrderInteriorStatusMapper, OrderInteriorStatus> implements IOrderInteriorStatusService {

}
