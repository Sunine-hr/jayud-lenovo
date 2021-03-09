package com.jayud.Inlandtransport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.Inlandtransport.feign.OmsClient;
import com.jayud.Inlandtransport.mapper.OrderInlandSendCarsMapper;
import com.jayud.Inlandtransport.model.po.OrderInlandSendCars;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.jayud.Inlandtransport.model.vo.GoodsVO;
import com.jayud.Inlandtransport.model.vo.OrderAddressVO;
import com.jayud.Inlandtransport.model.vo.SendCarPdfVO;
import com.jayud.Inlandtransport.service.IOrderInlandSendCarsService;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderTypeEnum;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

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
        List<OrderInlandSendCars> sendCar = this.getByCondition(new OrderInlandSendCars().setOrderId(order.getId()));

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
        sendCarPdfVO.assembleGoods(goodsInfoList, orderAddressList);

        return sendCarPdfVO;
    }

    @Override
    public List<OrderInlandSendCars> getByCondition(OrderInlandSendCars orderInlandSendCars) {
        QueryWrapper<OrderInlandSendCars> condition = new QueryWrapper<>(orderInlandSendCars);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 生成派车单号
     */
    @Override
    public String createTransportNo(String trailerOrderNo) {
        String substring = trailerOrderNo.substring(0, trailerOrderNo.length() - 8);
        String preOrderNo = OrderTypeEnum.P.getCode() + substring;
        String classCode = OrderTypeEnum.P.getCode();
        return (String) omsClient.getOrderNo(preOrderNo, classCode).getData();
    }

    /**
     * 根据订单号
     *
     * @param orderNo
     * @return
     */
    @Override
    public boolean deleteByOrderNo(String orderNo) {
        QueryWrapper<OrderInlandSendCars> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderInlandSendCars::getOrderNo, orderNo);
        return this.baseMapper.delete(condition) > 0;
    }


    /**
     * 派车单号是否存在
     *
     * @param transportNo
     * @return
     */
    private boolean isExistTransportNo(String transportNo) {
        QueryWrapper<OrderInlandSendCars> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(OrderInlandSendCars::getTransportNo, transportNo);
        int count = this.count(queryWrapper);
        if (count == 0) {
            return true;
        }
        return false;
    }
}
