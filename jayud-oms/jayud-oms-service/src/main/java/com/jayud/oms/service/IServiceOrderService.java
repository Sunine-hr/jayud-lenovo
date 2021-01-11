package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.InputOrderServiceForm;
import com.jayud.oms.model.po.ServiceOrder;
import com.jayud.oms.model.vo.InputOrderServiceVO;

import java.util.List;

/**
 * <p>
 * 服务单信息表 服务类
 * </p>
 *
 * @author
 * @since 2020-12-16
 */
public interface IServiceOrderService extends IService<ServiceOrder> {

    /**
     * 创建服务单订单
     * @param orderServiceForm
     * @return
     */
    boolean createOrder(InputOrderServiceForm orderServiceForm);

    /**
     * 判断订单是否存在
     * @param orderNo
     * @return
     */
    boolean isExistOrder(String orderNo);

    /**
     * 获取服务单详情
     * @param orderNo
     */
    InputOrderServiceVO getSerOrderDetails(String orderNo);
}
