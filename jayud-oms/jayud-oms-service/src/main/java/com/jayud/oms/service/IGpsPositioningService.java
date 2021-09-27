package com.jayud.oms.service;

import com.jayud.oms.model.bo.QueryGPSRecord;
import com.jayud.oms.model.po.GpsPositioning;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.TrackPlaybackVO;

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
    public List<GpsPositioning> getVehicleHistoryTrack(QueryGPSRecord form);
}
