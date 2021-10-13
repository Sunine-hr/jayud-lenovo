package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oms.model.po.OilCardTrackingInfo;
import com.jayud.oms.mapper.OilCardTrackingInfoMapper;
import com.jayud.oms.service.IOilCardTrackingInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 油卡跟踪信息 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-10-12
 */
@Service
public class OilCardTrackingInfoServiceImpl extends ServiceImpl<OilCardTrackingInfoMapper, OilCardTrackingInfo> implements IOilCardTrackingInfoService {


    @Override
    public List<OilCardTrackingInfo> getByCondition(OilCardTrackingInfo oilCardTrackingInfo) {
        QueryWrapper<OilCardTrackingInfo> condition = new QueryWrapper<>(oilCardTrackingInfo);
        return this.baseMapper.selectList(condition);
    }
}
