package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oms.model.po.DriverOrderInfo;
import com.jayud.oms.mapper.DriverOrderInfoMapper;
import com.jayud.oms.service.IDriverOrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 司机接单信息(微信小程序) 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-10
 */
@Service
public class DriverOrderInfoServiceImpl extends ServiceImpl<DriverOrderInfoMapper, DriverOrderInfo> implements IDriverOrderInfoService {

    /**
     * 查询司机接单信息
     */
    @Override
    public List<DriverOrderInfo> getDriverOrderInfoByStatus(Long driverId, String status) {
        QueryWrapper<DriverOrderInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(DriverOrderInfo::getDriverId, driverId);
        if (!StringUtils.isEmpty(status)) {
            condition.lambda().eq(DriverOrderInfo::getStatus, status);
        }
        return this.baseMapper.selectList(condition);
    }


    @Override
    public boolean saveOrUpdateDriverOrder(DriverOrderInfo driverOrderInfo) {
        if (driverOrderInfo.getId() == null) {
            driverOrderInfo.setTime(LocalDateTime.now());
            return this.save(driverOrderInfo);
        } else {
            return this.updateById(driverOrderInfo);
        }
    }

    @Override
    public DriverOrderInfo getByOrderId(Long orderId) {
        QueryWrapper<DriverOrderInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(DriverOrderInfo::getOrderId, orderId);
        return this.baseMapper.selectOne(condition);
    }
}
