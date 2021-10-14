package com.jayud.oms.service;

import com.jayud.oms.model.bo.QueryGPSRecord;
import com.jayud.oms.model.po.GpsPositioning;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.TrackPlaybackVO;
import com.jayud.oms.model.vo.gps.GPSOrderInfoVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
     * @param status   1:实时,2:历史轨迹
     * @return
     */
    List<GpsPositioning> getByOrderNo(List<String> orderNos, Integer status);

    /**
     * 根据车牌号查询历史定位
     */


    /**
     * 根据车牌获取gps记录
     *
     * @param orderNos
     * @param status   1:实时,2:历史轨迹
     * @return
     */
    List<GpsPositioning> getGroupByOrderNo(List<String> orderNos, Integer status);

    /**
     * 查询车辆历史轨迹详情
     *
     * @param form
     * @return
     */
    public TrackPlaybackVO getVehicleHistoryTrackInfo(QueryGPSRecord form);

    /**
     * 获取车辆历史轨迹
     *
     * @return
     */
    public List<GpsPositioning> getVehicleHistoryTrack(QueryGPSRecord form, GPSOrderInfoVO gpsOrderInfoVO);


    /**
     * 根据车牌号，获取车辆最后GPS定位坐标
     *
     * @param plateNumber 车牌号
     * @param status      状态(1:实时,2:历史轨迹)
     * @return
     */
    List<GpsPositioning> getByPlateNumber(String plateNumber, Integer status);

    /**
     * 根据供应商id，获取供应商所有车辆最后GPS定位坐标
     * @param supplierId
     * @return
     */
    List<GpsPositioning> getPositionBySupplierId(Integer supplierId);
}
