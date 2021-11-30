package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.entity.DataControl;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.model.vo.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 主订单基础数据表 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 操作主订单
     *
     * @param form
     * @param loginUserName
     * @return
     */
    public String oprMainOrder(InputMainOrderForm form, String loginUserName);

    /**
     * 订单是否存在
     *
     * @param orderNo
     * @return
     */
    public boolean isExistOrder(String orderNo);

    /**
     * 分页查询未提交订单
     *
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findOrderInfoByPage(QueryOrderInfoForm form);


    /**
     * 根据主键获取主订单信息
     *
     * @param idValue
     * @return
     */
    InputMainOrderVO getMainOrderById(Long idValue);

    /**
     * 获取主订单主键
     *
     * @param orderNo
     * @return
     */
    Long getIdByOrderNo(String orderNo);

    /**
     * 录入费用
     *
     * @param form
     * @return
     */
    boolean saveOrUpdateCost(InputCostForm form);

    /**
     * 获取费用详情
     *
     * @param form
     * @return
     */
    InputCostVO getCostDetail(GetCostDetailForm form);

    void calculateCost(InputCostVO inputCostVO);

    /**
     * 审核费用
     *
     * @param form
     * @return
     */
    boolean auditCost(AuditCostForm form);

    /**
     * 获取主订单流程节点
     *
     * @param form
     * @return
     */
    List<OrderStatusVO> handleProcess(QueryOrderStatusForm form);

    /**
     * 获取子订单流程节点
     *
     * @param form
     * @return
     */
    List<OrderStatusVO> handleSubProcess(HandleSubProcessForm form);

    /**
     * 订单详情
     *
     * @param form
     * @return
     */
    InputOrderVO getOrderDetail(GetOrderDetailForm form);

    /**
     * 创建订单
     *
     * @param form
     * @return
     */
    boolean createOrder(InputOrderForm form);

    /**
     * 查询主订单下面的所有子订单
     *
     * @param form
     * @return
     */
    List<InitChangeStatusVO> findSubOrderNo(GetOrderDetailForm form);

    /**
     * 确认更改状态
     *
     * @param form
     * @return
     */
    boolean changeStatus(ChangeStatusListForm form);

    /**
     * 统计订单数据
     *
     * @param loginUserName
     * @return
     */
    OrderDataCountVO countOrderData(QueryOrderInfoForm loginUserName);

    /**
     * 根据客户名称获取订单信息
     */
    List<OrderInfo> getByCustomerName(String customerName);

    /**
     * 二期优化1：通关前审核，通关前复核
     *
     * @return
     */
    InitGoCustomsAuditVO initGoCustomsAudit(InitGoCustomsAuditForm form);


    /**
     * 根据主订单集合查询主订单信息
     */
    List<OrderInfo> getByOrderNos(List<String> orderNos);

    /**
     * 根据主订单号修改主订单
     */
    boolean updateByMainOrderNo(String mainOrderNo, OrderInfo orderInfo);

    /**
     * 根据主订单查询子订单数据
     *
     * @param mainOrderNoList
     * @return
     */
    Map<String, Map<String, Object>> getSubOrderByMainOrderNos(List<String> mainOrderNoList);

    /**
     * 获取法人主体下的待外部报关数
     *
     * @param dataControl
     * @return
     */
    int pendingExternalCustomsDeclarationNum(DataControl dataControl);

    /**
     * 获取法人主体下的待通关前审核
     *
     * @param legalIds
     * @return
     */
    int pendingGoCustomsAuditNum(List<Long> legalIds);


    /**
     * 是否录用过费用
     *
     * @param orderNo
     * @param type    0.主订单,1子订单
     * @return
     */
    public boolean isCost(String orderNo, Integer type);


    /**
     * 根据主订单号码集合和状态查询订单
     *
     * @return
     */
    public List<OrderInfo> getOrderByStatus(List<String> orderNo, Integer status);

    /**
     * 获取费用状态
     */
    public Map<String, Object> getCostStatus(List<String> mainOrderNo, List<String> subOrderNo);


    /**
     * 过滤通关前审核
     *
     * @param callbackParam
     * @param legalIds
     * @param userName
     * @return
     */
    public Set<Long> filterGoCustomsAudit(Map<String, Object> callbackParam, DataControl dataControl, String userName);

    /**
     * 供应商录入费用
     *
     * @param form
     */
    void doSupplierEntryFee(InputCostForm form);

    /**
     * 供应商费用详情
     *
     * @param form
     */
    InputCostVO getPayCostDetail(GetCostDetailForm form);

    /**
     * 查询供应商异常费用
     *
     * @param form
     * @return
     */
    InputCostVO getSupplierAbnormalCostDetail(GetCostDetailForm form);

    /**
     * 获取当前订单模块节点
     *
     * @param mainOrderId
     * @return
     */
    List<ProductClassifyVO> getOrderModuleNode(Long mainOrderId);

    /**
     * 追加订单模块节点
     *
     * @param form
     */
    void addOrderModule(InputOrderForm form);

    /**
     * 查询供应商待处理操作
     * @param dataControl
     * @return
     */
//    List<Map<String, Object>> getSupplyPendingOpt(DataControl dataControl);

    /**
     * 根据费用id查询所有费用并且统计
     *
     * @param reCostIds
     * @param payCostIds
     * @return
     */
    public InputCostVO getCostDetailByCostIds(List<Long> reCostIds, List<Long> payCostIds);

    /**
     * 根据法人主体ids查询订单
     * @param legalIds
     * @return
     */
    List<OrderInfo> getByLegalEntityIds(List<Long> legalIds);

    /**
     * 主订单汇总
     * @param form
     * @param legalIds
     * @return
     */
    List<Map<String, Integer>> getMainOrderSummary(QueryStatisticalReport form, List<Long> legalIds);

    /**
     * 获取统计基础数据
     * @param form
     * @param legalIds
     * @param orderInfo
     * @return
     */
    List<OrderInfoVO> getBasicStatistics(QueryStatisticalReport form, List<Long> legalIds, OrderInfo orderInfo);

    /**
     * 获取法人主体下和登录用户的待外部报关数
     *
     * @param legalIds
     * @return
     */
    Integer pendingExternalCustomsDeclarationNum(List<Long> legalIds, String userName);


    /**
     * 推送六联单号去供应商
     * @param orderId 主订单单号
     * @param encode  六联单号
     */
    void pushMessageNumbers(Long orderId,String encode) ;
}
