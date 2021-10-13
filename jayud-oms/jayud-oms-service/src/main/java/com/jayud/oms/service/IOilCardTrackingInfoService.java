package com.jayud.oms.service;

import com.jayud.oms.model.po.OilCardTrackingInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 油卡跟踪信息 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-10-12
 */
public interface IOilCardTrackingInfoService extends IService<OilCardTrackingInfo> {

    List<OilCardTrackingInfo> getByCondition(OilCardTrackingInfo oilCardTrackingInfo);
}
