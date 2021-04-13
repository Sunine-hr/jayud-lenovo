package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.po.OrderReceivableCost;
import com.jayud.oms.model.vo.InputReceivableCostVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单对应应收费用明细 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderReceivableCostService extends IService<OrderReceivableCost> {

    /**
     * 获取费用详情
     *
     * @param form
     * @return
     */
    List<InputReceivableCostVO> findReceivableCost(GetCostDetailForm form);

    /**
     * 获取该条费用以出账时结算币种的汇率和本币金额
     *
     * @param costId
     * @return
     */
    InputReceivableCostVO getWriteBackSCostData(Long costId);

    /**
     * 获取审核通过费用
     */
    List<OrderReceivableCost> getApprovalFee(String mainOrder);

    /**
     * 获取审核通过费用
     */
    List<OrderReceivableCost> getApprovalFee(String mainOrder, List<Long> excludeIds);

    /**
     * 获取审核通过费用数目
     */
    public Integer getApprovalFeeCount(String mainOrder);

    /**
     * 获取审核通过费用数目
     */
    public Integer getApprovalFeeCount(String mainOrder, List<Long> excludeIds);

    /**
     * 是否录用过费用
     * @param orderNo
     * @param type    0.主订单,1子订单
     * @return
     */
    boolean isCost(String orderNo, Integer type);

    /**
     * 订单费用状态
     */
    public Map<String,Object> getOrderCostStatus(List<String> mainOrderNo,List<String> subOrderNo);

    List<OrderReceivableCost> getByType(List<String> orderNos, String subType);
}
