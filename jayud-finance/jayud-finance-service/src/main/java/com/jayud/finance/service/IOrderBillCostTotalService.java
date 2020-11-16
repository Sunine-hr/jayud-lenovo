package com.jayud.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.vo.OrderBillCostTotalVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
public interface IOrderBillCostTotalService extends IService<OrderBillCostTotal> {

    /**
     * 根据costIds查询对账金额信息,原始费用表的
     * @return
     */
    List<OrderBillCostTotalVO> findOrderFBillCostTotal(List<Long> costIds,String settlementCurrency);

    /**
     * 根据costIds查询对账金额信息,原始费用表的
     * @return
     */
    List<OrderBillCostTotalVO> findOrderSBillCostTotal(List<Long> costIds,String settlementCurrency);
}
