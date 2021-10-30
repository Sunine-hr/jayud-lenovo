package com.jayud.oms.service;

import com.jayud.oms.model.po.TrackingInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 油卡跟踪信息 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-10-29
 */
public interface ITrackingInfoService extends IService<TrackingInfo> {

    List<TrackingInfo> getByCondition(TrackingInfo trackingInfo);
}
