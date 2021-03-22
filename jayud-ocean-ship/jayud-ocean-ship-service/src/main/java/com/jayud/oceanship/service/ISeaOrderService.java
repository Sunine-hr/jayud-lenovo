package com.jayud.oceanship.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oceanship.bo.*;
import com.jayud.oceanship.po.SeaOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oceanship.vo.SeaOrderFormVO;
import com.jayud.oceanship.vo.SeaOrderVO;

import java.util.List;

/**
 * <p>
 * 海运订单表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
public interface ISeaOrderService extends IService<SeaOrder> {

    /**
     * 生成海运单
     * @param addSeaOrderForm
     */
    String createOrder(AddSeaOrderForm addSeaOrderForm);

    /**
     * 生成订单号
     * @return
     */
    String generationOrderNo(Long leaglId , Integer integer);

    /**
     * 订单是否存在
     * @param orderNo
     * @return
     */
    boolean isExistOrder(String orderNo);
    /**
     * 根据主订单号获取订单信息
     * @param orderNo
     * @return
     */
    SeaOrder getByMainOrderNO(String orderNo);

    /**
     * 根据订单id获取订单信息
     * @param id
     * @return
     */
    SeaOrderVO getSeaOrderByOrderNO(Long id);

    /**
     * 分页获取海运订单信息
     * @param form
     * @return
     */
    IPage<SeaOrderFormVO> findByPage(QuerySeaOrderForm form);

    /**
     * 改变流程状态
     * @param seaOrder
     * @param form
     */
    void updateProcessStatus(SeaOrder seaOrder, SeaProcessOptForm form);

    /**
     * 海运流程操作状态
     * @param form
     */
    void seaProcessOptRecord(SeaProcessOptForm form);

    /**
     * 订船操作
     * @param form
     */
    void doSeaBookShipOpt(SeaProcessOptForm form);

    /**
     * 订单补料
     * @param form
     */
    void updateOrSaveProcessStatus(SeaProcessOptForm form);

    /**
     * 获取订单详情
     * @param seaOrderId
     * @return
     */
    SeaOrderVO getSeaOrderDetails(Long seaOrderId);

    /**
     * 订单驳回
     * @param seaOrder
     * @param auditInfoForm
     * @param seaCargoRejected
     */
    void orderReceiving(SeaOrder seaOrder, AuditInfoForm auditInfoForm, SeaCargoRejected seaCargoRejected);

    /**
     * 通用驳回操作
     * @param seaOrder
     * @param auditInfoForm
     * @param seaCargoRejected
     */
    void rejectedOpt(SeaOrder seaOrder, AuditInfoForm auditInfoForm, SeaCargoRejected seaCargoRejected);

    /**
     * 根据主订单号集合获取海运订单信息
     * @param mainOrderNoList
     * @return
     */
    List<SeaOrder> getSeaOrderByOrderNOs(List<String> mainOrderNoList);

    /**
     *
     * @param form
     */
    void updateOrSaveReplenishmentAudit(SeaProcessOptForm form);
}
