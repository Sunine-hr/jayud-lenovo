package com.jayud.Inlandtransport.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.Inlandtransport.model.bo.AddOrderInlandTransportForm;
import com.jayud.Inlandtransport.model.bo.ProcessOptForm;
import com.jayud.Inlandtransport.model.bo.QueryOrderForm;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.Inlandtransport.model.vo.OrderInlandTransportDetails;
import com.jayud.Inlandtransport.model.vo.OrderInlandTransportFormVO;
import com.jayud.Inlandtransport.model.vo.OrderRejectedOpt;
import com.jayud.common.entity.AuditInfoForm;

import java.util.List;

/**
 * <p>
 * 内陆订单 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-03-01
 */
public interface IOrderInlandTransportService extends IService<OrderInlandTransport> {


    //创建订单
    public String createOrder(AddOrderInlandTransportForm form);

    IPage<OrderInlandTransportFormVO> findByPage(QueryOrderForm form);

    void updateProcessStatus(OrderInlandTransport orderInlandTransport, ProcessOptForm form);

    /**
     * 节点操作记录
     *
     * @param form
     */
    public void processOptRecord(ProcessOptForm form);

    /**
     * 执行派车
     *
     * @param form
     */
    void doDispatchOpt(ProcessOptForm form);

    List<OrderInlandTransport> getByCondition(OrderInlandTransport orderInlandTransport);

    /**
     * 查询订单详情
     *
     * @param subOrderId
     * @return
     */
    OrderInlandTransportDetails getOrderDetails(Long subOrderId);

    /**
     * 接单驳回
     * @param tmp
     * @param auditInfoForm
     * @param rejectedOpt
     */
    void orderReceiving(OrderInlandTransport tmp, AuditInfoForm auditInfoForm, OrderRejectedOpt rejectedOpt);


    /**
     * 订单驳回操作
     * @param tmp
     * @param auditInfoForm
     * @param rejectedOpt
     */
    void rejectedOpt(OrderInlandTransport tmp, AuditInfoForm auditInfoForm, OrderRejectedOpt rejectedOpt);

    List<OrderInlandTransport> getInlandOrderByMainOrderNos(List<String> mainOrderNos);

    /**
     * 根据子订单号集合查询内陆运输
     * @param orderNos
     * @return
     */
    List<OrderInlandTransport> getOrdersByOrderNos(List<String> orderNos);


    /**
     * 根据主订单号集合查询内陆订单详情
     * @param mainOrderNos
     * @return
     */
    List<OrderInlandTransportDetails> getInlandOrderInfoByMainOrderNos(List<String> mainOrderNos);

    /**
     * 获取内陆状态待处理数量
     * @param status
     * @param legalIds
     * @return
     */
    Integer getNumByStatus(String status, List<Long> legalIds);

    List<OrderInlandTransport> getByLegalEntityId(List<Long> legalIds);
}
