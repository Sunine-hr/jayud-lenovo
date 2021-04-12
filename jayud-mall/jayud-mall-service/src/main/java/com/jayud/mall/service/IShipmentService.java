package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryShipmentForm;
import com.jayud.mall.model.po.Shipment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.ShipmentVO;

/**
 * <p>
 * 南京新智慧-运单装货信息 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-09
 */
public interface IShipmentService extends IService<Shipment> {

    /**
     * 同步新智慧订单
     * @param shipmentVO
     * @return
     */
    ShipmentVO saveShipment(ShipmentVO shipmentVO);

    /**
     * 分页查询南京新智慧订单装货信息
     * @param form
     * @return
     */
    IPage<ShipmentVO> findShipmentByPage(QueryShipmentForm form);
}
