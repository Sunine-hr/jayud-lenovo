package com.jayud.finance.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.vo.EditBillDateilVO;
import com.jayud.finance.vo.OrderBillCostTotalVO;
import com.jayud.finance.vo.ViewBilToOrderVO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
public interface IOrderBillCostTotalService extends IService<OrderBillCostTotal> {

    /**
     * 根据costIds查询对账金额信息,原始费用表的
     *
     * @return
     */
    List<OrderBillCostTotalVO> findOrderFBillCostTotal(List<Long> costIds, String settlementCurrency, String accountTermStr);

    /**
     * 根据costIds查询对账金额信息,原始费用表的
     *
     * @return
     */
    List<OrderBillCostTotalVO> findOrderSBillCostTotal(List<Long> costIds, String settlementCurrency, String accountTermStr);

    /**
     * 导出对账单:计算结算币种
     *
     * @param headMap
     * @param dynamicHead
     * @param datas
     * @param moneyType   1-应付 2-应收
     */
    public void exportSettlementCurrency(LinkedHashMap<String, String> headMap,
                                         LinkedHashMap<String, String> dynamicHead,
                                         JSONArray datas, String moneyType);

    /**
     * 计算结算币种
     */
    public void calculateSettlementCurrency(String key, JSONArray datas, String moneyType);

    /**
     * 根据账单编号和类型查询出账单详细信息
     */
    public List<OrderBillCostTotal> getByBillNo(List<String> billNo,String moneyType);

    /**
     * 根据账单编号查询编辑对账账单详情
     * @param type   类型(0:应收,1:应付)
     */
    public EditBillDateilVO getEditBillByBillNo(String billNo, Integer type);
}
