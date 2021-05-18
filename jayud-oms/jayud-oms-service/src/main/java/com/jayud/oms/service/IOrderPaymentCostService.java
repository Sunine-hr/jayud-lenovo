package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.vo.DriverOrderPaymentCostVO;
import com.jayud.oms.model.vo.InputPaymentCostVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单对应应付费用明细 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderPaymentCostService extends IService<OrderPaymentCost> {

    /**
     * 获取费用详情
     *
     * @param form
     * @return
     */
    List<InputPaymentCostVO> findPaymentCost(GetCostDetailForm form);

    /**
     * 根据订单编码查询录用费用明细
     *
     * @param orderNo
     * @return
     */
    List<DriverOrderPaymentCostVO> getDriverOrderPaymentCost(String orderNo);

    /**
     * 判断是否司机已经提交费用
     */
    boolean isCostSubmitted(String orderNo);

    /**
     * 获取该条费用以出账时结算币种的汇率和本币金额
     *
     * @param costId
     * @return
     */
    InputPaymentCostVO getWriteBackFCostData(Long costId);

    /**
     * 根据主订单号/子订单号批量获取提交审核通过费用
     */
    List<OrderPaymentCost> getCostSubmittedByOrderNos(List<String> mainOrders, List<String> subOrderNos);

    /**
     * 是否录用过费用
     *
     * @param orderNo
     * @param type    0.主订单,1子订单
     * @return
     */
    boolean isCost(String orderNo, Integer type);

    /**
     * 根据类型查询费用详情
     *
     * @param subType
     * @return
     */
    List<OrderPaymentCost> getByType(List<String> orderNos, String subType);

    public Map<String, Object> getOrderCostStatus(List<String> mainOrderNos, List<String> subOrderNos, Map<String, Object> callbackParam);
    /**
     * 查询待处理费用审核
     */
    public List<Map<String, Object>> getPendingExpenseApproval(String subType, List<String> orderNos, List<Long> legalIds);

    List<OrderPaymentCost> getBySubType(String subType);
}
