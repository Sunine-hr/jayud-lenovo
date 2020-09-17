package com.jayud.oms.service;

import com.jayud.model.po.OrderStatus;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 业务流程节点，例如报关有(报关接单，报关打单，报关复核 报关申请 报关放行) 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderStatusService extends IService<OrderStatus> {

}
