package com.jayud.oms.service;

import com.jayud.oms.model.po.DriverOrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 司机接单信息(微信小程序) 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-10
 */
public interface IDriverOrderInfoService extends IService<DriverOrderInfo> {
    /**
     * 查询司机接单信息
     */
    List<DriverOrderInfo> getDriverOrderInfoByStatus(Long driverId, String status);

    /**
     * 新增编辑
     */
    boolean saveOrUpdateDriverOrder(DriverOrderInfo driverOrderInfo);

    DriverOrderInfo getByOrderId(Long orderId);

    /**
     * 修改司机接单状态
     */
    boolean updateStatus(Long orderId, String status);

    /**
     * 订单信息是否存在
     */
    boolean isExistOrder(Long orderId);

    /**
     * 同步中港订单状态
     */
    void synchronizeTmsStatus(Map<String, Object> processNode, Long orderId);
}
