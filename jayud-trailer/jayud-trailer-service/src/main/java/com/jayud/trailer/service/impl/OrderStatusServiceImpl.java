package com.jayud.trailer.service.impl;

import com.jayud.trailer.po.OrderStatus;
import com.jayud.trailer.mapper.OrderStatusMapper;
import com.jayud.trailer.service.IOrderStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务流程节点，例如报关有(报关接单，报关打单，报关复核 报关申请 报关放行) 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Service
public class OrderStatusServiceImpl extends ServiceImpl<OrderStatusMapper, OrderStatus> implements IOrderStatusService {

}
