package com.jayud.tms.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.CreateUserTypeEnum;
import com.jayud.common.enums.KafkaMsgEnums;
import com.jayud.tms.feign.MsgClient;
import com.jayud.tms.feign.OmsClient;
import com.jayud.tms.model.bo.SendCarForm;
import com.jayud.tms.model.po.OrderSendCars;
import com.jayud.tms.mapper.OrderSendCarsMapper;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.vo.OrderSendCarsVO;
import com.jayud.tms.service.IOrderSendCarsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.tms.service.IOrderTransportService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单派车信息 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderSendCarsServiceImpl extends ServiceImpl<OrderSendCarsMapper, OrderSendCars> implements IOrderSendCarsService {

    @Autowired
    private IOrderTransportService orderTransportService;
    @Autowired
    private MsgClient msgClient;
    @Autowired
    private OmsClient omsClient;

    @Override
    public OrderSendCarsVO getOrderSendInfo(String orderNo) {
        return baseMapper.getOrderSendInfo(orderNo);
    }

    @Override
    public int getDriverPendingOrderNum(Long driverId, List<String> orderNos) {
        QueryWrapper<OrderSendCars> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderSendCars::getDriverInfoId, driverId);
        if (CollectionUtils.isNotEmpty(orderNos)) {
            condition.lambda().notIn(OrderSendCars::getOrderNo, orderNos);
        }
        return this.count(condition);
    }

    /**
     * 根据订单编号获取
     */
    @Override
    public OrderSendCars getOrderSendCarsByOrderNo(String orderNo) {
        QueryWrapper<OrderSendCars> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderSendCars::getOrderNo, orderNo);
        return this.baseMapper.selectOne(condition);
    }

    /**
     * 根据中港订单号删除派车信息
     *
     * @param orderNo
     * @return
     */
    @Override
    public boolean deleteDispatchInfoByOrderNo(String orderNo) {
        QueryWrapper<OrderSendCars> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderSendCars::getOrderNo, orderNo);
        return this.baseMapper.delete(condition) > 0;
    }

    /**
     * 派车消息推送
     */
    @Override
    public void sendCarsMessagePush(SendCarForm form) {
        //查询中港订单用户类型
        OrderTransport orderTransport = this.orderTransportService.getById(form.getOrderId());
        Integer createUserType = orderTransport.getCreateUserType();
        switch (CreateUserTypeEnum.getEnum(createUserType)) {
            case VIVO:
                //查询接单法人
                ApiResult resultOne = omsClient.getLegalEntityInfoByOrderNo(orderTransport.getMainOrderNo());
                if (resultOne.getCode() != HttpStatus.SC_OK) {
                    log.warn("请求查询法人主体信息失败");
                }
                //查询派车信息
                OrderSendCars orderSendCars = this.getById(form.getId());
                //查询车辆信息
                ApiResult resultTwo = this.omsClient.getVehicleInfoById(3L);//TODO 等派车单增加车辆id字段再修改
                if (resultTwo.getCode() != HttpStatus.SC_OK) {
                    log.warn("请求车辆信息失败");
                }
                Map<String, String> request = new HashMap<>();
                request.put("topic", KafkaMsgEnums.VIVO_FREIGHT_TMS_MESSAGE_ONE.getTopic());
                request.put("key", KafkaMsgEnums.VIVO_FREIGHT_TMS_MESSAGE_ONE.getKey());
                Map<String, Object> msg = new HashMap<>();
                msg.put("dispatchNo", orderTransport.getThirdPartyOrderNo());
                msg.put("licensePlate", new JSONObject(resultTwo.getData()).getStr("plateNumber")); //TODO 等派车单增加车辆id字段再修改
                msg.put("transportationCompany", new JSONObject(resultOne.getData()).getStr("legalName"));
                msg.put("containerNo", orderSendCars.getCntrNo());
                request.put("msg", JSONUtil.toJsonStr(msg));
                msgClient.consume(request);
                break;
        }
    }


}
