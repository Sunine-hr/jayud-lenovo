package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.GpsPositioning;
import com.jayud.oms.model.vo.gps.GPSYGTResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * GPS定位
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
public interface GPSPositioningApiService {


    /**
     * 批量获取车辆定位定位信息
     *
     * @param params
     * @return
     */
    public Object getPositionsObj(List<String> licensePlates, Integer gpsType, Map<String, Object> params);

    /**
     * 转换成本系统实体
     * @param obj
     * @return
     */
    List<GpsPositioning> convertDatas(Object list);

    List<GPSYGTResponse> getYGTPositions(List<String> licensePlates, Map<String, Object> params);

    Object getBeiDouPositions(List<String> licensePlates, Map<String, Object> params);

    /**
     * 获取北斗登录会话id 30分钟失效
     * @param params
     * @return
     */
    public String getBeiDouSessionId(Map<String, Object> params) ;

    /**
     * 获取历史轨迹
     * @param plateNumber 车牌
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param gpsType
     * @param paraMap
     * @return
     */
    Object getHistory(String plateNumber, LocalDateTime startTime,
                      LocalDateTime endTime, Integer gpsType, Map<String, Object> paraMap);

    List<GPSYGTResponse>  getYGTHistory(String plateNumber, LocalDateTime startTime, LocalDateTime endTime,
                         Map<String, Object> params);

    Object getBeiDouHistory(String plateNumber, LocalDateTime startTime, LocalDateTime endTime, Map<String, Object> params);
}
