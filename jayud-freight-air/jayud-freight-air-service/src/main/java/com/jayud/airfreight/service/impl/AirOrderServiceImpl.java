package com.jayud.airfreight.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.airfreight.model.bo.AddAirOrderForm;
import com.jayud.airfreight.model.bo.AddGoodsForm;
import com.jayud.airfreight.model.bo.AddOrderAddressForm;
import com.jayud.airfreight.model.bo.QueryAirOrderForm;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.airfreight.mapper.AirOrderMapper;
import com.jayud.airfreight.model.po.Goods;
import com.jayud.airfreight.model.po.OrderAddress;
import com.jayud.airfreight.model.vo.AirOrderFormVO;
import com.jayud.airfreight.service.IAirOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.airfreight.service.IGoodsService;
import com.jayud.airfreight.service.IOrderAddressService;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 空运订单表 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-30
 */
@Service
public class AirOrderServiceImpl extends ServiceImpl<AirOrderMapper, AirOrder> implements IAirOrderService {

    @Autowired
    private IOrderAddressService orderAddressService;
    @Autowired
    private IGoodsService goodsService;

    //创建订单
    @Override
    @Transactional
    public void createOrder(AddAirOrderForm addAirOrderForm) {

        LocalDateTime now = LocalDateTime.now();
        AirOrder airOrder = ConvertUtil.convert(addAirOrderForm, AirOrder.class);
        //创建空运单
        if (addAirOrderForm.getId() == null) {
            //生成订单号
            String orderNo = generationOrderNo();
            airOrder.setOrderNo(orderNo).setCreateTime(now)
                    .setStatus(OrderStatusEnum.AIR_A_0.getCode())
                    .setProcessStatus(0);
            this.save(airOrder);
        } else {
            //修改空运单
            airOrder.setUpdateTime(now).setUpdateUser(addAirOrderForm.getCreateUser());
            this.updateById(airOrder);
        }

        List<AddOrderAddressForm> orderAddressForms = addAirOrderForm.getOrderAddressForms();
        List<OrderAddress> orderAddresses = new ArrayList<>();
        for (AddOrderAddressForm orderAddressForm : orderAddressForms) {
            OrderAddress orderAddress = ConvertUtil.convert(orderAddressForm, OrderAddress.class);
            orderAddress.setBusinessType(BusinessTypeEnum.KY.getCode())
                    .setBusinessId(airOrder.getId())
                    .setCreateTime(orderAddressForm.getId() == null ? now : null);
            orderAddresses.add(orderAddress);
        }
        //批量保存用户地址
        this.orderAddressService.saveOrUpdateBatch(orderAddresses);

        List<AddGoodsForm> goodsForms = addAirOrderForm.getGoodsForms();
        List<Goods> goodsList = new ArrayList<>();
        for (AddGoodsForm goodsForm : goodsForms) {
            Goods goods = ConvertUtil.convert(goodsForm, Goods.class);
            goods.setBusinessId(airOrder.getId())
                    .setBusinessType(BusinessTypeEnum.KY.getCode())
                    .setCreateTime(goodsForm.getId() == null ? now : null);
            goodsList.add(goods);
        }

        //批量保存货物信息
        goodsService.saveOrUpdateBatch(goodsList);
    }

    /**
     * 生成订单号
     */
    @Override
    public String generationOrderNo() {
        //生成订单号
        String orderNo = StringUtils.loadNum(CommonConstant.A, 12);
        while (true) {
            if (isExistOrder(orderNo)) {//重复
                orderNo = StringUtils.loadNum(CommonConstant.A, 12);
            } else {
                break;
            }
        }
        return orderNo;
    }

    /**
     * 是否存在订单
     */
    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(AirOrder::getOrderNo, orderNo);
        return this.count(condition) > 0;
    }

    @Override
    public IPage<AirOrderFormVO> findByPage(QueryAirOrderForm form) {
        Page<AirOrder> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }
}
