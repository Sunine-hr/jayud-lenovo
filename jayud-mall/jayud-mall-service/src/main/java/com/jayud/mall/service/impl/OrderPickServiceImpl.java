package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.OrderPick;
import com.jayud.mall.mapper.OrderPickMapper;
import com.jayud.mall.model.vo.DeliveryAddressVO;
import com.jayud.mall.model.vo.OrderPickVO;
import com.jayud.mall.service.IOrderPickService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单对应提货信息表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-17
 */
@Service
public class OrderPickServiceImpl extends ServiceImpl<OrderPickMapper, OrderPick> implements IOrderPickService {

    @Override
    public List<OrderPickVO> createOrderPickList(List<DeliveryAddressVO> form) {


        return null;
    }
}
