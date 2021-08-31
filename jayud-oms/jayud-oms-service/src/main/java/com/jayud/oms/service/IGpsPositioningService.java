package com.jayud.oms.service;

import com.jayud.oms.model.po.GpsPositioning;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-08-30
 */
public interface IGpsPositioningService extends IService<GpsPositioning> {

    /**
     * 根据车牌获取gps记录
     *
     * @param licensePlateSet
     * @param status          1:实时,2:历史轨迹
     * @return
     */
    List<GpsPositioning> getByPlateNumbers(Set<String> licensePlateSet, Integer status);


    /**
     * 根据车牌获取gps记录
     *
     * @param orderNos
     * @param status          1:实时,2:历史轨迹
     * @return
     */
    List<GpsPositioning> getByOrderNo(List<String> orderNos, Integer status);

}
