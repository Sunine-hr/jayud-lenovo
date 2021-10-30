package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oms.model.po.TrackingInfo;
import com.jayud.oms.mapper.TrackingInfoMapper;
import com.jayud.oms.service.ITrackingInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 油卡跟踪信息 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-10-29
 */
@Service
public class TrackingInfoServiceImpl extends ServiceImpl<TrackingInfoMapper, TrackingInfo> implements ITrackingInfoService {

    @Override
    public List<TrackingInfo> getByCondition(TrackingInfo trackingInfo) {
        QueryWrapper<TrackingInfo> condition = new QueryWrapper<>(trackingInfo);
        return this.baseMapper.selectList(condition);
    }
}
