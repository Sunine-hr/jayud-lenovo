package com.jayud.tms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.tms.mapper.OrderTakeAdrMapper;
import com.jayud.tms.model.po.OrderTakeAdr;
import com.jayud.tms.model.vo.DriverOrderTakeAdrVO;
import com.jayud.tms.model.vo.InputOrderTakeAdrVO;
import com.jayud.tms.service.IOrderTakeAdrService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<DriverOrderTakeAdrVO> getDriverOrderTakeAdr(List<String> orderNoList) {
        return this.baseMapper.getDriverOrderTakeAdr(orderNoList);
    }

    /**
     * 获取送货地址数量
     */
    @Override
    public int getDeliveryAddressNum(String orderNo) {
        QueryWrapper<OrderTakeAdr> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderTakeAdr::getOrderNo, orderNo);
        return this.count(condition);
    }

    /**
     * 入仓
     */
}
