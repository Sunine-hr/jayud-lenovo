package com.jayud.tms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.tms.mapper.OrderTakeAdrMapper;
import com.jayud.tms.model.po.OrderTakeAdr;
import com.jayud.tms.model.vo.DriverOrderTakeAdrVO;
import com.jayud.tms.model.vo.InputOrderTakeAdrVO;
import com.jayud.tms.model.vo.OrderTakeAdrInfoVO;
import com.jayud.tms.service.IOrderTakeAdrService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单对应收货地址 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderTakeAdrServiceImpl extends ServiceImpl<OrderTakeAdrMapper, OrderTakeAdr> implements IOrderTakeAdrService {

    @Override
    public List<InputOrderTakeAdrVO> findTakeGoodsInfo(String orderNo) {
        return baseMapper.findTakeGoodsInfo(orderNo);
    }

    /**
     * 中港小程序用的
     *
     * @param orderNoList
     * @param oprType
     * @return
     */
    @Override
    public List<DriverOrderTakeAdrVO> getDriverOrderTakeAdr(List<String> orderNoList, Integer oprType) {
        return this.baseMapper.getDriverOrderTakeAdr(orderNoList, oprType);
    }

    /**
     * 获取送货地址数量
     */
    @Override
    public int getDeliveryAddressNum(String orderNo) {
        QueryWrapper<OrderTakeAdr> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderTakeAdr::getOrderNo, orderNo).eq(OrderTakeAdr::getOprType, 2);
        return this.count(condition);
    }

    /**
     * 根据订单编号获取地址
     */
    @Override
    public List<OrderTakeAdr> getOrderTakeAdrByOrderNo(String orderNo, Integer oprType) {
        QueryWrapper<OrderTakeAdr> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderTakeAdr::getOrderNo, orderNo);
        if (oprType != null) {
            condition.lambda().eq(OrderTakeAdr::getOprType, oprType);
        }
        return this.baseMapper.selectList(condition);
    }

    @Override
    public List<OrderTakeAdr> getOrderTakeAdrByOrderNos(List<String> orderNoList, Integer oprType) {
        if (CollectionUtil.isEmpty(orderNoList)) {
            return new ArrayList<>();
        }
        QueryWrapper<OrderTakeAdr> condition = new QueryWrapper<>();
        condition.lambda().in(OrderTakeAdr::getOrderNo, orderNoList);
        if (oprType != null) {
            condition.lambda().eq(OrderTakeAdr::getOprType, oprType);
        }
        return this.baseMapper.selectList(condition);
    }


    @Override
    public List<OrderTakeAdrInfoVO> getOrderTakeAdrInfos(List<String> orderNoList, Integer oprType) {
        return this.baseMapper.getOrderTakeAdrInfos(orderNoList, oprType);
    }

    /**
     * 根据时间获取提货时间
     *
     * @param month
     */
    @Override
    public List<OrderTakeAdr> getByTakeTime(List<String> month, String format) {
        return this.baseMapper.getByTakeTime(month, format);
    }

    /**
     * 根据提货时间范围返回订单号
     *
     * @param takeTimeStr
     * @param oprType
     * @return
     */
    @Override
    public Set<String> getOrderNosByTakeTime(List<String> takeTimeStr, Integer oprType) {
        QueryWrapper<OrderTakeAdr> condition = new QueryWrapper<>();
        condition.lambda().between(OrderTakeAdr::getTakeTime, takeTimeStr.get(0), takeTimeStr.get(1));
        if (oprType != null) {
            condition.lambda().eq(OrderTakeAdr::getOprType, oprType);
        }
        return this.baseMapper.selectList(condition).stream().map(OrderTakeAdr::getOrderNo).collect(Collectors.toSet());
    }

}
