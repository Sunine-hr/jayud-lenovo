package com.jayud.oms.service;

import com.jayud.oms.model.po.OrderFlowSheet;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单完成情况表 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-03-03
 */
public interface IOrderFlowSheetService extends IService<OrderFlowSheet> {

    /**
     * 根据条件搜索
     */
    public List<OrderFlowSheet> getByCondition(OrderFlowSheet condition);
}
