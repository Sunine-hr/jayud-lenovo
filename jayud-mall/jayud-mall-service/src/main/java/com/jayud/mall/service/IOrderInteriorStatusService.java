package com.jayud.mall.service;

import com.jayud.mall.model.bo.AuditOrderInteriorStatusForm;
import com.jayud.mall.model.bo.OrderInteriorStatusQueryForm;
import com.jayud.mall.model.po.OrderInteriorStatus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OrderInteriorStatusVO;

/**
 * <p>
 * 订单内部状态表(非流程状态) 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-20
 */
public interface IOrderInteriorStatusService extends IService<OrderInteriorStatus> {

    /**
     * 查询订单内部状态
     * @param form
     * @return
     */
    OrderInteriorStatusVO findOrderInteriorStatusByOrderIdAndCode(OrderInteriorStatusQueryForm form);

    /**
     * 订单内部状态审核
     * @param form
     */
    void auditOrderInteriorStatus(AuditOrderInteriorStatusForm form);
}
