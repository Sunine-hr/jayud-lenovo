package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.QueryOrderPickForm;
import com.jayud.mall.model.po.OrderPick;
import com.jayud.mall.model.vo.DeliveryAddressVO;
import com.jayud.mall.model.vo.OrderPickVO;
import com.jayud.mall.model.vo.OrderWarehouseNoVO;

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

    /**
     * 订单提货分页查询
     * @param form
     * @return
     */
    IPage<OrderPickVO> findOrderPickByPage(QueryOrderPickForm form);

    /**
     * 提货地址-下载进仓单号
     * @param id
     * @return
     */
    OrderWarehouseNoVO downloadWarehouseNoByPickId(Long id);

    /**
     * 订单-下载进仓(不存在提货地址时)
     * @param id
     * @return
     */
    OrderWarehouseNoVO downloadWarehouseNoByOrderId(Long id);
}
