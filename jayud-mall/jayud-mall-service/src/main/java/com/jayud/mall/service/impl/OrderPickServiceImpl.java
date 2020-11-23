package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.OrderPickMapper;
import com.jayud.mall.model.po.OrderPick;
import com.jayud.mall.model.vo.DeliveryAddressVO;
import com.jayud.mall.model.vo.OrderPickVO;
import com.jayud.mall.service.INumberGeneratedService;
import com.jayud.mall.service.IOrderPickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    OrderPickMapper orderPickMapper;

    @Autowired
    INumberGeneratedService numberGeneratedService;

    @Override
    public List<OrderPickVO> createOrderPickList(List<DeliveryAddressVO> form) {
        List<OrderPickVO> orderPickVOList = new ArrayList<>();
        form.forEach(deliveryAddressVO -> {
            OrderPickVO orderPickVO = new OrderPickVO();
            String warehouseNo = numberGeneratedService.getOrderNoByCode("warehouse_receipt");
            orderPickVO.setWarehouseNo(warehouseNo);//进仓单号
            orderPickVO.setAddressId(deliveryAddressVO.getId());
            orderPickVO.setContacts(deliveryAddressVO.getContacts());
            orderPickVO.setPhone(deliveryAddressVO.getPhone());
            orderPickVO.setCountryName(deliveryAddressVO.getCountryName());
            orderPickVO.setStateName(deliveryAddressVO.getStateName());
            orderPickVO.setCityName(deliveryAddressVO.getCityName());
            orderPickVO.setAddress(deliveryAddressVO.getAddress());
            orderPickVOList.add(orderPickVO);
        });
        return orderPickVOList;
    }
}
