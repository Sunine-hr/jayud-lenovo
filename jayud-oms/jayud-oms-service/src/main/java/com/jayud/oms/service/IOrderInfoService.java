package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.model.vo.*;

import java.util.List;

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
     * @param form
     * @return
     */
    public String oprMainOrder(InputMainOrderForm form);

    /**
     * 订单是否存在
     * @param orderNo
     * @return
     */
    public boolean isExistOrder(String orderNo);

    /**
     * 分页查询未提交订单
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findOrderInfoByPage(QueryOrderInfoForm form);


    /**
     * 根据主键获取主订单信息
     * @param idValue
     * @return
     */
    InputMainOrderVO getMainOrderById(Long idValue);

    /**
     * 获取主订单主键
     * @param orderNo
     * @return
     */
    Long getIdByOrderNo(String orderNo);

    /**
     * 录入费用
     * @param form
     * @return
     */
    boolean saveOrUpdateCost(InputCostForm form);

    /**
     * 获取费用详情
     * @param form
     * @return
     */
    InputCostVO getCostDetail(GetCostDetailForm form);

    /**
     * 审核费用
     * @param form
     * @return
     */
    boolean auditCost(AuditCostForm form);

    /**
     * 获取主订单流程节点
     * @param form
     * @return
     */
    List<OrderStatusVO> handleProcess(QueryOrderStatusForm form);

    /**
     * 获取子订单流程节点
     * @param form
     * @return
     */
    List<OrderStatusVO> handleSubProcess(HandleSubProcessForm form);

    /**
     * 订单详情
     * @param form
     * @return
     */
    InputOrderVO getOrderDetail(GetOrderDetailForm form);

    /**
     * 创建订单
     * @param form
     * @return
     */
    boolean createOrder(InputOrderForm form);

    /**
     * 查询主订单下面的所有子订单
     * @param form
     * @return
     */
    List<InitChangeStatusVO> findSubOrderNo(GetOrderDetailForm form);

    /**
     * 确认更改状态
     * @param form
     * @return
     */
    boolean changeStatus(ChangeStatusListForm form);

    /**
     * 统计订单数据
     * @return
     */
    OrderDataCountVO countOrderData();

    /**
     * 二期优化1：通关前审核，通关前复核
     * @return
     */
    InitGoCustomsAuditVO initGoCustomsAudit(InitGoCustomsAuditForm form);


}
