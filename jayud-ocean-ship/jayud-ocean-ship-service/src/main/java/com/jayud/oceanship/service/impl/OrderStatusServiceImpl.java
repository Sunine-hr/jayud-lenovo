package com.jayud.oceanship.service.impl;

import com.jayud.oceanship.po.OrderStatus;
import com.jayud.oceanship.mapper.OrderStatusMapper;
import com.jayud.oceanship.service.IOrderStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务流程节点，例如报关有(报关接单，报关打单，报关复核 报关申请 报关放行) 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-30
 */
@Service
public class OrderStatusServiceImpl extends ServiceImpl<OrderStatusMapper, OrderStatus> implements IOrderStatusService {

}
