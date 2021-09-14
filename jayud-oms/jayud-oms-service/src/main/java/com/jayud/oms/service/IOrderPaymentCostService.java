package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.bo.QueryStatisticalReport;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.vo.DriverBillCostVO;
import com.jayud.oms.model.vo.DriverOrderPaymentCostVO;
import com.jayud.oms.model.vo.InputPaymentCostVO;
import com.jayud.oms.model.vo.StatisticsOrderBaseCostVO;

import java.math.BigDecimal;
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
    public List<Map<String, Object>> getPendingExpenseApproval(String subType, List<String> orderNos, List<Long> legalIds, String userName);

    /**
     * 根据主订单号获取应收绑定数据
     *
     * @param paymentCost
     * @return
     */
    List<OrderPaymentCost> getReceivableBinding(OrderPaymentCost paymentCost);

    List<OrderPaymentCost> getBySubType(String subType);

    /**
     * 查询异常费用
     *
     * @param form
     * @return
     */
    List<InputPaymentCostVO> getSupplierAbnormalCostDetail(GetCostDetailForm form);

    /**
     * 根据条件查询应付费用
     *
     * @return
     */
    List<OrderPaymentCost> getByCondition(OrderPaymentCost paymentCost);

    /**
     * 根据子订单号集合查询供应商费用
     *
     * @param supplierId
     * @param subOrderNos
     * @return
     */
    List<OrderPaymentCost> getSupplierPayCostByOrderNos(Long supplierId, List<String> subOrderNos, Integer status);

    /**
     * 计算应付费用
     *
     * @param list
     * @param isMain
     * @return
     */
    Map<String, Map<String, BigDecimal>> statisticalPayCostByOrderNos(List<OrderPaymentCost> list, Boolean isMain);

    /**
     * 获取主订单费用
     *
     * @param mainOrderNo
     * @return
     */
    List<OrderPaymentCost> getMainOrderCost(String mainOrderNo, List<String> mainCostStatus);

    List<OrderPaymentCost> getByMainOrderNo(String mainOrderNo, List<String> exclusionStatus);

    /**
     * 补充供应商信息
     *
     * @param paymentCosts
     */
    void supplySupplierInfo(List<OrderPaymentCost> paymentCosts);

    /**
     * 查询子订单费用
     *
     * @param paymentCost
     * @param exclusionStatus
     * @return
     */
    List<OrderPaymentCost> getSubCostByMainOrderNo(OrderPaymentCost paymentCost, List<String> exclusionStatus);

    /**
     * 根据订单号修改
     *
     * @param mainOrderNo
     * @param subOrder
     * @param orderPaymentCost
     */
    void updateByOrderNo(String mainOrderNo, String subOrder, OrderPaymentCost orderPaymentCost);

    /**
     * 根据主订单号集合获取应付费用
     *
     * @param mainOrderNos
     * @return
     */
    List<OrderPaymentCost> getOrderPaymentCostByMainOrderNos(List<String> mainOrderNos);

    /**
     * 统计主订单费用
     *
     * @param form
     * @param legalIds
     * @param status
     * @return
     */
    public List<Map<String, Object>> statisticsMainOrderCost(QueryStatisticalReport form, List<Long> legalIds, List<String> status);


    List<StatisticsOrderBaseCostVO> getBaseStatisticsAllCost(QueryStatisticalReport form, List<Long> legalIds, List<String> status);

    /**
     * 查询司机费用
     * @param orderNos
     * @param status
     * @param time
     * @param employIds
     * @return
     */
    public List<DriverBillCostVO> getDriverBillCost(List<String> orderNos, List<String> status, String time, List<Long> employIds);
}
