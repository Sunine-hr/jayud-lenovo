package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.GpsPositioning;

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
}
