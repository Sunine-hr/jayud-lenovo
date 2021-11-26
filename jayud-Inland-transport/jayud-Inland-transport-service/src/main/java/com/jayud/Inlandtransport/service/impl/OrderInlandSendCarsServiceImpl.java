package com.jayud.Inlandtransport.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.Inlandtransport.feign.MsgClient;
import com.jayud.Inlandtransport.feign.OmsClient;
import com.jayud.Inlandtransport.mapper.OrderInlandSendCarsMapper;
import com.jayud.Inlandtransport.model.po.OrderInlandSendCars;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.jayud.Inlandtransport.model.vo.GoodsVO;
import com.jayud.Inlandtransport.model.vo.OrderAddressVO;
import com.jayud.Inlandtransport.model.vo.OrderInlandTransportDetails;
import com.jayud.Inlandtransport.model.vo.SendCarPdfVO;
import com.jayud.Inlandtransport.service.IOrderInlandSendCarsService;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.KafkaMsgEnums;
import com.jayud.common.enums.OrderTypeEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private MsgClient msgClient;

    @Override
    public SendCarPdfVO initPdfData(OrderInlandTransport order, String type) {
        SendCarPdfVO sendCarPdfVO = new SendCarPdfVO();

        sendCarPdfVO.setVehicleType(order.getVehicleType());
        sendCarPdfVO.setVehicleSize(order.getVehicleSize());
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





    @Override
    public void msgPush(OrderInlandSendCars orderInlandSendCars) {
        Map<String, String> request = new HashMap<>();
        request.put("topic", KafkaMsgEnums.MESSAGE_PUSH_TASK.getTopic());
        request.put("key", KafkaMsgEnums.MESSAGE_PUSH_TASK.getKey());
        Map<String, Object> msg = new HashMap<>();
        msg.put("deliverTime", DateUtils.LocalDateTime2Str(LocalDateTime.now(), DateUtils.DATE_TIME_PATTERN));
        msg.put("now", DateUtils.LocalDateTime2Str(LocalDateTime.now(), DateUtils.DATE_TIME_PATTERN));
        //单号
        msg.put("orderNo", orderInlandSendCars.getOrderNo());
        //证件号   运输单号
        msg.put("idCode", orderInlandSendCars.getTransportNo());
        //司机姓名
        msg.put("driverName",orderInlandSendCars.getDriverName() );
        //车牌
        msg.put("truckNo", orderInlandSendCars.getLicensePlate());
        //司机电话
        msg.put("driverTel", orderInlandSendCars.getDriverPhone());
        msg.put("subType", SubOrderSignEnum.NLYS.getSignOne());
        //配送人 司机姓名
        msg.put("deliverName",  orderInlandSendCars.getDriverName());
        request.put("msg", JSONUtil.toJsonStr(msg));
        this.msgClient.consume(request);
    }

    /**
     * 根据内陆id查询派车信息
     *
     * @param Id
     */
    @Override
    public OrderInlandSendCars getOrderInlandSendCars(Long Id) {
        QueryWrapper condition  = new QueryWrapper<>();
        condition.eq("order_id", Id);
        return this.getOne(condition);
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
