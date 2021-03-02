package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.po.Goods;
import com.jayud.oms.model.po.OrderAddress;
import com.jayud.oms.mapper.OrderAddressMapper;
import com.jayud.oms.service.IGoodsService;
import com.jayud.oms.service.IOrderAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单地址表 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2020-12-16
 */
@Service
public class OrderAddressServiceImpl extends ServiceImpl<OrderAddressMapper, OrderAddress> implements IOrderAddressService {

    @Autowired
    private IGoodsService goodsService;

    /**
     * 查询地址
     */
    @Override
    public List<OrderAddress> getOrderAddressByBusIds(List<Long> orderId, Integer businessType) {
        QueryWrapper<OrderAddress> condition = new QueryWrapper<>();
        condition.lambda().in(OrderAddress::getBusinessId, orderId);
        condition.lambda().in(OrderAddress::getBusinessType, businessType);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 添加订单提货地址
     *
     * @param deliveryAddressList
     */
    @Override
    @Transactional
    public void addDeliveryAddress(List<OrderDeliveryAddress> deliveryAddressList) {
        //循环地址
        List<OrderAddress> list = new ArrayList<>();

        for (OrderDeliveryAddress deliveryAddress : deliveryAddressList) {
            //绑定商品信息
            Goods goods = new Goods().setId(deliveryAddress.getGoodsId())
                    .setBusinessId(deliveryAddress.getBusinessId())
                    .setBusinessType(deliveryAddress.getBusinessType())
                    .setName(deliveryAddress.getName())
                    .setPlateAmount(deliveryAddress.getPlateAmount())
                    .setPlateUnit(deliveryAddress.getPlateUnit())
                    .setBulkCargoAmount(deliveryAddress.getBulkCargoAmount())
                    .setBulkCargoUnit(deliveryAddress.getBulkCargoUnit())
                    .setTotalWeight(deliveryAddress.getTotalWeight())
                    .setSize(deliveryAddress.getSize());
            if (deliveryAddress.getGoodsId() == null) {
                goods.setCreateTime(LocalDateTime.now());
            }
            this.goodsService.saveOrUpdate(goods);

            //订单地址
            OrderAddress orderAddress = new OrderAddress();
            orderAddress.setId(deliveryAddress.getOrderAddressId());
            orderAddress.setContacts(deliveryAddress.getContacts());
            orderAddress.setPhone(deliveryAddress.getPhone());
            orderAddress.setAddress(deliveryAddress.getAddress());
            orderAddress.setDeliveryDate(deliveryAddress.getDeliveryDate());
            orderAddress.setEnterWarehouseNo(deliveryAddress.getEnterWarehouseNo());
            orderAddress.setBindGoodsId(goods.getId());
            orderAddress.setFileName(StringUtils.getFileNameStr(deliveryAddress.getFileViewList()));
            orderAddress.setFilePath(StringUtils.getFileNameStr(deliveryAddress.getFileViewList()));
            if (orderAddress.getId() == null) {
                orderAddress.setCreateTime(LocalDateTime.now());
            }
            list.add(orderAddress);
        }
        this.saveOrUpdateBatch(list);
    }
}
