package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.model.po.OrderStatus;
import com.jayud.oms.mapper.OrderStatusMapper;
import com.jayud.oms.service.IOrderStatusService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务流程节点，例如报关有(报关接单，报关打单，报关复核 报关申请 报关放行) 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderStatusServiceImpl extends ServiceImpl<OrderStatusMapper, OrderStatus> implements IOrderStatusService {

}
