package com.jayud.airfreight.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.airfreight.model.po.AirBooking;
import com.jayud.airfreight.mapper.AirBookingMapper;
import com.jayud.airfreight.service.IAirBookingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.common.UserOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 空运订舱表 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2020-12-03
 */
@Service
public class AirBookingServiceImpl extends ServiceImpl<AirBookingMapper, AirBooking> implements IAirBookingService {


    /**
     * 执行订舱操作
     */
    @Override
    @Transactional
    public void saveOrUpdateAirBooking(AirBooking airBooking) {
        if (airBooking.getId() == null) {
            airBooking.setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken());
            this.save(airBooking);
        } else {
            airBooking.setUpdateTime(LocalDateTime.now())
                    .setUpdateUser(UserOperator.getToken());
            this.updateById(airBooking);
        }
    }

    /**
     * 根据空运订单id查询订舱信息
     */
    @Override
    public AirBooking getByAirOrderId(Long airOrderId) {
        QueryWrapper<AirBooking> condition = new QueryWrapper<>();
        condition.lambda().eq(AirBooking::getAirOrderId, airOrderId);
        return this.baseMapper.selectOne(condition);
    }

    /**
     * 根据空运订单id修改订舱
     */
    @Override
    public boolean updateByAirOrderId(Long airOrderId, AirBooking airBooking) {
        QueryWrapper<AirBooking> condition = new QueryWrapper<>();
        condition.lambda().eq(AirBooking::getAirOrderId, airOrderId);
        return this.baseMapper.update(airBooking, condition) > 0;
    }
}
