package com.jayud.Inlandtransport.service.impl;

import com.jayud.Inlandtransport.feign.OmsClient;
import com.jayud.Inlandtransport.model.po.OrderInlandSendCars;
import com.jayud.Inlandtransport.mapper.OrderInlandSendCarsMapper;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.jayud.Inlandtransport.model.vo.GoodsInfoVO;
import com.jayud.Inlandtransport.model.vo.GoodsVO;
import com.jayud.Inlandtransport.model.vo.OrderAddressVO;
import com.jayud.Inlandtransport.model.vo.SendCarPdfVO;
import com.jayud.Inlandtransport.service.IOrderInlandSendCarsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.BusinessTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 内陆派车信息 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-03-04
 */
@Service
public class OrderInlandSendCarsServiceImpl extends ServiceImpl<OrderInlandSendCarsMapper, OrderInlandSendCars> implements IOrderInlandSendCarsService {

    @Autowired
    private OmsClient omsClient;

    @Override
    public SendCarPdfVO initPdfData(OrderInlandTransport order, String type) {
        SendCarPdfVO sendCarPdfVO = new SendCarPdfVO();

        sendCarPdfVO.setVehicleType(order.getVehicleType());
        sendCarPdfVO.setOrderNo(order.getOrderNo());
        sendCarPdfVO.setLegalName(order.getLegalName());

        //查询派送单信息
        OrderInlandSendCars sendCar = this.getById(order.getId());

        sendCarPdfVO.assembleCar(sendCar);

        //查询订单地址
        List<OrderAddressVO> orderAddressList = this.omsClient.getOrderAddressByBusIds(Collections.singletonList(order.getId()),
                BusinessTypeEnum.NL.getCode()).getData();
        //查询订单地址
        List<GoodsVO> goodsInfoList = this.omsClient.getGoodsByBusIds(Collections.singletonList(order.getId()),
                BusinessTypeEnum.NL.getCode()).getData();
        //组装地址
        sendCarPdfVO.assembleAddress(orderAddressList);
        //组装货品
        sendCarPdfVO.assembleGoods(goodsInfoList);

        return sendCarPdfVO;
    }
}
