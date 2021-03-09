package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.FileClient;
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

        for (OrderDeliveryAddress deliveryAddress : deliveryAddressList) {
            //绑定商品信息
            Goods goods = new Goods().setId(deliveryAddress.getGoodsId())
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
            orderAddress.setId(deliveryAddress.getOrderAddressId());
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
            OrderDeliveryAddress orderDeliveryAddress = new OrderDeliveryAddress();
            orderDeliveryAddress.setOrderNo(e.getOrderNo())
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
        });
        return list;
    }
}
