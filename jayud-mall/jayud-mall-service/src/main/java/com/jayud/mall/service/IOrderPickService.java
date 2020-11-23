package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.po.OrderPick;
import com.jayud.mall.model.vo.DeliveryAddressVO;
import com.jayud.mall.model.vo.OrderPickVO;

import java.util.List;

/**
 * <p>
 * 订单对应提货信息表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-17
 */
public interface IOrderPickService extends IService<OrderPick> {

    /**
     * 批量创建-订单对应提货地址
     * @param form
     * @return
     */
    List<OrderPickVO> createOrderPickList(List<DeliveryAddressVO> form);
}
