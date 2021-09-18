package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oms.model.po.GpsPositioning;
import com.jayud.oms.mapper.GpsPositioningMapper;
import com.jayud.oms.service.IGpsPositioningService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-08-30
 */
@Service
public class GpsPositioningServiceImpl extends ServiceImpl<GpsPositioningMapper, GpsPositioning> implements IGpsPositioningService {

    @Override
    public List<GpsPositioning> getByPlateNumbers(Set<String> licensePlateSet, Integer status) {
        QueryWrapper<GpsPositioning> condition = new QueryWrapper<>();
        condition.lambda().in(GpsPositioning::getPlateNumber, licensePlateSet);
        if (status != null) {
            condition.lambda().eq(GpsPositioning::getStatus, status);
        }
        return this.baseMapper.selectList(condition);
    }

    @Override
    public List<GpsPositioning> getByOrderNo(List<String> orderNos, Integer status) {
        QueryWrapper<GpsPositioning> condition = new QueryWrapper<>();
        condition.lambda().in(GpsPositioning::getOrderNo, orderNos);
        if (status != null) {
            condition.lambda().eq(GpsPositioning::getStatus, status);
        }
        return this.baseMapper.selectList(condition);
    }

    @Override
    public List<GpsPositioning> getGroupByOrderNo(List<String> orderNos, Integer status) {
        QueryWrapper<GpsPositioning> condition = new QueryWrapper<>();
        condition.lambda().in(GpsPositioning::getOrderNo, orderNos);
        if (status != null) {
            condition.lambda().eq(GpsPositioning::getStatus, status);
        }
        condition.lambda().groupBy(GpsPositioning::getOrderNo);
        return this.baseMapper.selectList(condition);
    }
}
