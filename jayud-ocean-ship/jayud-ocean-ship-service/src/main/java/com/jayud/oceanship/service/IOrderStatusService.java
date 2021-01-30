package com.jayud.oceanship.service;

import com.jayud.oceanship.po.OrderStatus;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 业务流程节点，例如报关有(报关接单，报关打单，报关复核 报关申请 报关放行) 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-30
 */
public interface IOrderStatusService extends IService<OrderStatus> {

}
