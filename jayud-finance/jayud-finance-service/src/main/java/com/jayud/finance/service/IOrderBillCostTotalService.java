package com.jayud.finance.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.vo.OrderBillCostTotalVO;
import com.jayud.finance.vo.ViewBilToOrderVO;

import java.util.LinkedHashMap;
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
    List<OrderBillCostTotalVO> findOrderFBillCostTotal(List<Long> costIds,String settlementCurrency,String accountTermStr);

    /**
     * 根据costIds查询对账金额信息,原始费用表的
     * @return
     */
    List<OrderBillCostTotalVO> findOrderSBillCostTotal(List<Long> costIds,String settlementCurrency,String accountTermStr);

    /**
     * 导出对账单:计算结算币种
     *
     * @param headMap
     * @param dynamicHead
     * @param datas
     * @param moneyType   1-应付 2-应收
     */
    public void calculateSettlementCurrency(LinkedHashMap<String, String> headMap,
                                             LinkedHashMap<String, String> dynamicHead,
                                             JSONArray datas,String moneyType);
}
