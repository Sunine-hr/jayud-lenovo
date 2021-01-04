package com.jayud.airfreight.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.airfreight.model.bo.*;
import com.jayud.airfreight.model.po.AirOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.airfreight.model.vo.AirOrderFormVO;
import com.jayud.airfreight.model.vo.AirOrderVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 空运订单表 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-30
 */
public interface IAirOrderService extends IService<AirOrder> {

    /**
     * 创建订单
     */
    void createOrder(AddAirOrderForm addAirOrderForm);

    /**
     * 生成订单号
     */
    String generationOrderNo();

    /**
     * 是否存在订单
     */
    boolean isExistOrder(String orderNo);

    /**
     * 分页查询空运订单信息
     */
    IPage<AirOrderFormVO> findByPage(QueryAirOrderForm form);

    /**
     * 更新流程状态
     */
    void updateProcessStatus(AirOrder airOrder, AirProcessOptForm form);

    /**
     * 空运流程操作记录
     */
    void airProcessOptRecord(AirProcessOptForm form);

    /**
     * 订舱操作
     */
    void doAirBookingOpt(AirProcessOptForm form);

    /**
     * 是否入仓
     */
    boolean isWarehousing(AirOrder airOrder);

    /**
     * 根据第三方唯一编码查询空运订单
     */
    public AirOrder getByThirdPartyOrderNo(String thirdPartyOrderNo);


    /**
     * 通用驳回操作
     */
    void rejectedOpt(AirOrder airOrder, AuditInfoForm auditInfoForm, AirCargoRejected airCargoRejected);


    /**
     * 跟踪推送
     *
     * @param airOrderId
     */
    void trackingPush(Long airOrderId);

    /**
     * 根据主订单号查询空运订单
     */
    AirOrder getByMainOrderNo(String mainOrderNo);

    /**
     * 根据空运订单号修改空运
     */
    boolean updateByOrderNo(String airOrderNo, AirOrder airOrder);

    /**
     * 空运订单详情
     */
    AirOrderVO getAirOrderDetails(Long airOrderId);

    /**
     * 查询空运订单信息
     */
    public List<AirOrder> getAirOrderInfo(AirOrder airOrder);

    /**
     * 根据空运订单号集合查询空运订单信息
     */
    List<AirOrder> getAirOrdersByOrderNos(List<String> airOrderNos);

    /**
     * 订单单驳回
     */
    public void orderReceiving(AirOrder airOrder, AuditInfoForm auditInfoForm, AirCargoRejected airCargoRejected);

    /**
     * 接单驳回
     */
//    public void rejectionOrderReceiving(AirOrder airOrder, AuditInfoForm auditInfoForm);


    /**
     * 根据空运第三方标识查询主订单信息
     */
    public Map<String, Object> getMainOrderByThirdOrderNo(String thirdPartyOrderNo);

    /**
     * 异常反馈
     */
    public void exceptionFeedback(AddAirExceptionFeedbackForm form);

    /**
     * 根据主订单号查询空运订单信息
     */
    public List<AirOrder> getAirOrderByMainOrderNos(List<String> mainOrderNos);

}
