package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.vo.DriverOrderPaymentCostVO;
import com.jayud.oms.model.vo.InputPaymentCostVO;

import java.util.List;

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
     * @param form
     * @return
     */
    List<InputPaymentCostVO> findPaymentCost(GetCostDetailForm form);

    /**
     * 根据订单编码查询录用费用明细
     * @param orderNo
     * @return
     */
    List<DriverOrderPaymentCostVO>  getDriverOrderPaymentCost(String orderNo);

    /**
     * 判断是否司机已经提交费用
     */
    boolean isCostSubmitted(String orderNo);

}
