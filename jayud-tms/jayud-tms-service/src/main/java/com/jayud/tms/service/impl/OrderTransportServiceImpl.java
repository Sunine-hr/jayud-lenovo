package com.jayud.tms.service.impl;

import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.mapper.OrderTransportMapper;
import com.jayud.tms.service.IOrderTransportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 中港运输订单 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderTransportServiceImpl extends ServiceImpl<OrderTransportMapper, OrderTransport> implements IOrderTransportService {

}
