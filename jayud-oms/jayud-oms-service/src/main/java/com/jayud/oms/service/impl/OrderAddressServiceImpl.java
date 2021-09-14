package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.model.bo.AddTrailerOrderAddressForm;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Autowired
    private FileClient fileClient;

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

        OrderDeliveryAddress orderDeliveryAddress = deliveryAddressList.get(0);
        //先清除旧的数据
        this.goodsService.removeByBusinessId(orderDeliveryAddress.getBusinessId(), orderDeliveryAddress.getBusinessType());
        this.removeByBusinessId(orderDeliveryAddress.getBusinessId(), orderDeliveryAddress.getBusinessType());
        for (OrderDeliveryAddress deliveryAddress : deliveryAddressList) {
            //绑定商品信息
            Goods goods = new Goods()
                    .setBusinessId(deliveryAddress.getBusinessId())
                    .setBusinessType(deliveryAddress.getBusinessType())
                    .setName(deliveryAddress.getGoodsName())
                    .setPlateAmount(deliveryAddress.getPlateAmount())
                    .setPlateUnit(deliveryAddress.getPlateUnit())
                    .setBulkCargoAmount(deliveryAddress.getBulkCargoAmount())
                    .setBulkCargoUnit(deliveryAddress.getBulkCargoUnit())
                    .setTotalWeight(deliveryAddress.getTotalWeight())
                    .setSize(deliveryAddress.getSize())
                    .setOrderNo(deliveryAddress.getOrderNo());
            if (deliveryAddress.getGoodsId() == null) {
                goods.setCreateTime(LocalDateTime.now());
            }
            this.goodsService.saveOrUpdate(goods);

            //订单地址
            OrderAddress orderAddress = new OrderAddress();
            orderAddress.setContacts(deliveryAddress.getContacts());
            orderAddress.setPhone(deliveryAddress.getPhone());
            orderAddress.setAddress(deliveryAddress.getAddress());
            orderAddress.setDeliveryDate(DateUtils.str2LocalDateTime(deliveryAddress.getDeliveryDate(), null));
            orderAddress.setEnterWarehouseNo(deliveryAddress.getEnterWarehouseNo());
            orderAddress.setBindGoodsId(goods.getId());
            orderAddress.setBusinessId(deliveryAddress.getBusinessId());
            orderAddress.setBusinessType(deliveryAddress.getBusinessType());
            orderAddress.setFileName(StringUtils.getFileNameStr(deliveryAddress.getFileViewList()));
            orderAddress.setFilePath(StringUtils.getFileStr(deliveryAddress.getFileViewList()));
            orderAddress.setType(deliveryAddress.getAddressType());
            orderAddress.setOrderNo(deliveryAddress.getOrderNo());
            orderAddress.setProvince(deliveryAddress.getProvince());
            orderAddress.setCity(deliveryAddress.getCity());
            orderAddress.setArea(deliveryAddress.getArea());
            if (orderAddress.getId() == null) {
                orderAddress.setCreateTime(LocalDateTime.now());
            }
            list.add(orderAddress);
        }
        this.saveOrUpdateBatch(list);
    }

    /**
     * 获取订单提货/送货地址
     *
     * @param orderId
     * @param businessType
     * @return
     */
    @Override
    public List<OrderDeliveryAddress> getDeliveryAddress(List<Long> orderId,
                                                         Integer businessType) {

        List<OrderAddress> addressList = this.getOrderAddressByBusIds(orderId, businessType);
        List<Goods> goodsList = this.goodsService.getGoodsByBusIds(orderId, businessType);
        Map<Long, Goods> tmp = goodsList.stream().collect(Collectors.toMap(Goods::getId, e -> e));
        String prePath = fileClient.getBaseUrl().getData().toString();

        List<OrderDeliveryAddress> list = new ArrayList<>();
        addressList.forEach(e -> {
            Goods goods = tmp.get(e.getBindGoodsId());
            if (goods != null) {
                OrderDeliveryAddress orderDeliveryAddress = new OrderDeliveryAddress();
                orderDeliveryAddress
                        .setBusinessId(e.getBusinessId())
                        .setBusinessType(e.getBusinessType())
                        .setOrderNo(e.getOrderNo())
                        .setGoodsId(goods.getId())
                        .setOrderAddressId(e.getId())
                        .setContacts(e.getContacts())
                        .setPhone(e.getPhone())
                        .setAddress(e.getAddress())
                        .setDeliveryDate(DateUtils.LocalDateTime2Str(e.getDeliveryDate(), DateUtils.DATE_TIME_PATTERN))
                        .setEnterWarehouseNo(e.getEnterWarehouseNo())
                        .setAddressType(e.getType())
                        .setGoodsName(goods.getName())
                        .setPlateAmount(goods.getPlateAmount())
                        .setPlateUnit(goods.getPlateUnit())
                        .setBulkCargoAmount(goods.getBulkCargoAmount())
                        .setBulkCargoUnit(goods.getBulkCargoUnit())
                        .setSize(goods.getSize())
                        .setTotalWeight(goods.getTotalWeight())
                        .setVolume(goods.getVolume())
                        .setRemarks(e.getRemarks())
                        .setFileViewList(StringUtils.getFileViews(e.getFilePath(), e.getFileName(), prePath));
                list.add(orderDeliveryAddress);
            }
        });
        return list;
    }

    @Override
    public void removeByBusinessId(Long businessId, Integer businessType) {
        QueryWrapper<OrderAddress> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderAddress::getBusinessId, businessId);
        if (businessType != null) {
            condition.lambda().eq(OrderAddress::getBusinessType, businessType);
        }
        this.baseMapper.delete(condition);
    }

    @Override
    public void removeByOrderNo(String orderNo, Integer businessType) {
        QueryWrapper<OrderAddress> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderAddress::getOrderNo, orderNo);
        if (businessType != null) {
            condition.lambda().eq(OrderAddress::getBusinessType, businessType);
        }
        this.baseMapper.delete(condition);
    }

    /**
     * 获取最新的联系人信息
     */
    @Override
    public List<OrderAddress> getLastContactInfoByBusinessType(Integer businessType) {
        QueryWrapper<OrderAddress> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderAddress::getBusinessType, businessType);
        condition.lambda().orderByDesc(OrderAddress::getId);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public List<OrderAddress> getOrderAddressByBusOrders(List<String> orderNo, Integer businessType) {
        QueryWrapper<OrderAddress> condition = new QueryWrapper<>();
        condition.lambda().in(OrderAddress::getOrderNo, orderNo);
        condition.lambda().in(OrderAddress::getBusinessType, businessType);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public void deleteOrderAddressByBusOrders(List<String> orderNo, Integer businessType) {
        QueryWrapper<OrderAddress> condition = new QueryWrapper<>();
        condition.lambda().in(OrderAddress::getOrderNo, orderNo);
        condition.lambda().in(OrderAddress::getBusinessType, businessType);
        this.baseMapper.delete(condition);
    }

    @Override
    public Set<String> getOrderNosByTakeTime(String[] takeTimeStr, Integer code) {
        QueryWrapper<OrderAddress> condition = new QueryWrapper<>();
        condition.lambda().between(OrderAddress::getDeliveryDate, takeTimeStr[0], takeTimeStr[1]);
        if (code != null) {
            condition.lambda().eq(OrderAddress::getBusinessType, code);
        }
        return this.baseMapper.selectList(condition).stream().map(OrderAddress::getOrderNo).collect(Collectors.toSet());
    }

    @Override
    public void saveOrUpdateOrderAddressAndGoodsBatch(List<AddTrailerOrderAddressForm> orderAddressForms) {
        //循环地址
        List<OrderAddress> list = new ArrayList<>();

        AddTrailerOrderAddressForm orderDeliveryAddress = orderAddressForms.get(0);
        //先清除旧的数据
        this.goodsService.removeByBusinessId(orderDeliveryAddress.getBusinessId(), orderDeliveryAddress.getBusinessType());
        this.removeByBusinessId(orderDeliveryAddress.getBusinessId(), orderDeliveryAddress.getBusinessType());
        for (AddTrailerOrderAddressForm deliveryAddress : orderAddressForms) {
            //绑定商品信息
            Goods goods = new Goods()
                    .setBusinessId(deliveryAddress.getBusinessId())
                    .setBusinessType(deliveryAddress.getBusinessType())
                    .setName(deliveryAddress.getName())
                    .setBulkCargoAmount(deliveryAddress.getBulkCargoAmount())
                    .setBulkCargoUnit(deliveryAddress.getBulkCargoUnit())
                    .setTotalWeight(deliveryAddress.getTotalWeight())
                    .setSize(deliveryAddress.getSize())
                    .setOrderNo(deliveryAddress.getOrderNo())
                    .setCreateTime(LocalDateTime.now());
            this.goodsService.saveOrUpdate(goods);

            //订单地址
            OrderAddress orderAddress = new OrderAddress();
            orderAddress.setContacts(deliveryAddress.getContacts());
            orderAddress.setPhone(deliveryAddress.getPhone());
            orderAddress.setAddress(deliveryAddress.getAddress());
            orderAddress.setDeliveryDate(DateUtils.str2LocalDateTime(deliveryAddress.getDeliveryDate(), null));
            orderAddress.setBindGoodsId(goods.getId());
            orderAddress.setBusinessId(deliveryAddress.getBusinessId());
            orderAddress.setBusinessType(deliveryAddress.getBusinessType());
            orderAddress.setFileName(StringUtils.getFileNameStr(deliveryAddress.getTakeFiles()));
            orderAddress.setFilePath(StringUtils.getFileStr(deliveryAddress.getTakeFiles()));
            orderAddress.setType(deliveryAddress.getType());
            orderAddress.setOrderNo(deliveryAddress.getOrderNo());
            orderAddress.setProvince(deliveryAddress.getProvince());
            orderAddress.setCity(deliveryAddress.getCity());
            orderAddress.setArea(deliveryAddress.getArea());
            if (orderAddress.getId() == null) {
                orderAddress.setCreateTime(LocalDateTime.now());
            }
            list.add(orderAddress);
        }
        this.saveOrUpdateBatch(list);
    }

    /**
     * 根据条件查询订单id
     *
     * @param orderAddress
     * @param timeInterval
     * @return
     */
    @Override
    public Set<Long> getOrderAddressOrderIdByTimeInterval(OrderAddress orderAddress, List<String> timeInterval) {
        QueryWrapper<OrderAddress> condition = new QueryWrapper<>(orderAddress);
        condition.lambda().select(OrderAddress::getBusinessId);
        condition.lambda().between(OrderAddress::getDeliveryDate, timeInterval.get(0), timeInterval.get(1));
        return this.baseMapper.selectList(condition).stream().map(OrderAddress::getBusinessId).collect(Collectors.toSet());
    }
}
