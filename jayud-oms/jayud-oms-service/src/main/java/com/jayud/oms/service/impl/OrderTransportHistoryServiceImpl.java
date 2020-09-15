package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.model.po.OrderTransportHistory;
import com.jayud.oms.mapper.OrderTransportHistoryMapper;
import com.jayud.oms.service.IOrderTransportHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 子订单变量历史记录表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderTransportHistoryServiceImpl extends ServiceImpl<OrderTransportHistoryMapper, OrderTransportHistory> implements IOrderTransportHistoryService {

}
