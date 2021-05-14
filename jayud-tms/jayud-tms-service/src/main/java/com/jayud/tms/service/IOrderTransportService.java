package com.jayud.tms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.tms.model.bo.InputOrderTransportForm;
import com.jayud.tms.model.bo.OprStatusForm;
import com.jayud.tms.model.bo.QueryDriverOrderTransportForm;
import com.jayud.tms.model.bo.QueryOrderTmsForm;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.vo.*;

import java.util.List;

/**
 * <p>
 * 中港运输订单 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderTransportService extends IService<OrderTransport> {


    /**
     * 创建订单
     *
     * @param form
     * @return
     */
    boolean createOrderTransport(InputOrderTransportForm form);

    /**
     * 订单是否存在
     *
     * @param orderNo
     * @return
     */
    public boolean isExistOrder(String orderNo);

    /**
     * 获取订单详情
     *
     * @param mainOrderNo
     * @return
     */
    public InputOrderTransportVO getOrderTransport(String mainOrderNo);

    /**
     * 根据条件获取中港信息
     *
     * @return
     */
    public List<OrderTransport> getOrderTmsByCondition(OrderTransport orderTransport);

    /**
     * 中港分页查询
     *
     * @param form
     * @return
     */
    IPage<OrderTransportVO> findTransportOrderByPage(QueryOrderTmsForm form);

    /**
     * 初始化确认派车时的PDF数据
     *
     * @param orderNo
     * @param classCode
     * @return
     */
    SendCarPdfVO initPdfData(String orderNo, String classCode);

    /**
     * 分页查询司机的中港订单信息
     *
     * @param form
     * @return
     */
    List<DriverOrderTransportVO> getDriverOrderTransport(QueryDriverOrderTransportForm form);

    /**
     * 获取中港订单状态
     */
    String getOrderTransportStatus(String orderNo);

    /**
     * 司机反馈状态
     *
     * @param form
     */
    void doDriverFeedbackStatus(OprStatusForm form);

    /**
     * 小程序司机车辆通关（补出仓和入仓数据）,送货地址只有一个时候才做这个操作
     */
    void driverCustomsClearanceVehicles(OprStatusForm form);

    /**
     * 中港运输各个菜单列表数据量统计
     *
     * @return
     */
    StatisticsDataNumberVO statisticsDataNumber();

    /**
     * 根据主订单号集合查询中港信息
     */
    public List<OrderTransport> getTmsOrderByMainOrderNos(List<String> mainOrders);

    /**
     * 根据主订单号集合查询中港详情
     */
    public List<OrderVO> getOrderTransportByMainOrderNo(List<String> mainOrders);


    /**
     * 查询订单状态数量
     *
     * @param status
     * @param legalIds
     * @return
     */
    public Integer getNumByStatus(String status, List<Long> legalIds);

    /**
     * 根据主订单号集合查询中港详情信息
     * @param mainOrderNos
     * @return
     */
    List<OrderTransportInfoVO> getTmsOrderInfoByMainOrderNos(List<String> mainOrderNos);

    List<OrderTransport> getByLegalEntityId(List<Long> legalIds);

    List<OrderTransport> preconditionsGoCustomsAudit();

}
