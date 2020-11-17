package com.jayud.oms.service;

import com.jayud.oms.model.po.DriverOrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
    boolean updateStatus(Long orderId,String status);
}
