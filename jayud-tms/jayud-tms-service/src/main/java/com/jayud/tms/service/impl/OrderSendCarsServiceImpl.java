package com.jayud.tms.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.CreateUserTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.tms.feign.FreightAirApiClient;
import com.jayud.tms.feign.MsgClient;
import com.jayud.tms.feign.OmsClient;
import com.jayud.tms.mapper.OrderSendCarsMapper;
import com.jayud.tms.model.bo.RejectOrderForm;
import com.jayud.tms.model.bo.SendCarForm;
import com.jayud.tms.model.po.OrderSendCars;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.vo.*;
import com.jayud.tms.service.IOrderSendCarsService;
import com.jayud.tms.service.IOrderTransportService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OrderSendCarsServiceImpl extends ServiceImpl<OrderSendCarsMapper, OrderSendCars> implements IOrderSendCarsService {

    @Autowired
    private IOrderTransportService orderTransportService;
    @Autowired
    private MsgClient msgClient;
    @Autowired
    private OmsClient omsClient;
    @Autowired
    private FreightAirApiClient freightAirApiClient;

    @Override
    public OrderSendCarsVO getOrderSendInfo(String orderNo) {
        return baseMapper.getOrderSendInfo(orderNo);
    }

    @Override
    public int getDriverPendingOrderNum(Long driverId, List<String> orderNos) {
//        QueryWrapper<OrderSendCars> condition = new QueryWrapper<>();
//        condition.lambda().and(e -> e.eq(OrderSendCars::getDriverInfoId, driverId).or().eq(OrderSendCars::getJockeyId, driverId));
//        if (CollectionUtils.isNotEmpty(orderNos)) {
//            condition.lambda().notIn(OrderSendCars::getOrderNo, orderNos);
//        }
        int count = this.baseMapper.getDriverPendingOrderNum(driverId, orderNos);
        return count;
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

    @Override
    public DriverInfoPdfVO initDriverInfo(String orderNo) {
        DriverInfoPdfVO driverInfoPdfVO = baseMapper.initDriverInfo(orderNo);
        if (driverInfoPdfVO != null && driverInfoPdfVO.getJockeyId() != null) {
            Object data = this.omsClient.getDriverById(driverInfoPdfVO.getJockeyId()).getData();
            if (data == null) {
                throw new JayudBizException(400, "该骑师信息不存在");
            }
            JSONObject object = new JSONObject(data);
            driverInfoPdfVO.setJockeyIDCard(object.getStr("idNo"));
            driverInfoPdfVO.setJockeyPhone(object.getStr("phone"));
        }
        return driverInfoPdfVO;
    }

    @Override
    public SendCarListPdfVO initSendCarList(String orderNo) {
        List<SendCarListTempVO> tempList = baseMapper.initSendCarList(orderNo);
        SendCarListPdfVO sendCarListPdfVO = new SendCarListPdfVO();
        if (tempList != null && tempList.size() > 0) {
            sendCarListPdfVO.setLegalName(tempList.get(0).getLegalName());
            sendCarListPdfVO.setLegalEnName(tempList.get(0).getLegalEnName());
            sendCarListPdfVO.setJobNumber(tempList.get(0).getJobNumber());
            sendCarListPdfVO.setCreateTimeStr(tempList.get(0).getCreateTimeStr());
            List<SendCarListVO> sendCarListVOList = ConvertUtil.convertList(tempList, SendCarListVO.class);
            sendCarListPdfVO.setSendCarListVOList(sendCarListVOList);
        }
        return sendCarListPdfVO;
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
                this.sendCarsMsg2Vivo(form, orderTransport);
                break;
        }
    }

    /**
     * 派车驳回信息推送
     *
     * @param form
     * @param orderTransport
     */
    @Override
    public boolean dispatchRejectionMsgPush(RejectOrderForm form, OrderTransport orderTransport) {

        switch (CreateUserTypeEnum.getEnum(orderTransport.getCreateUserType())) {
            case VIVO:
                if (OrderStatusEnum.TMS_T_4_1.getCode().equals(form.getCmd())
                        || OrderStatusEnum.TMS_T_5_1.getCode().equals(form.getCmd())) {
                    ApiResult result = freightAirApiClient.forwarderDispatchRejected(orderTransport.getThirdPartyOrderNo());
                    if (result.getCode() != HttpStatus.SC_OK) {
                        log.error("请求vivo派车推送接口失败 msg={}", result.getMsg());
                        return false;
                    }
                } else if (Integer.parseInt(form.getCmd().
                        split("_")[1])
                        > Integer.parseInt(OrderStatusEnum.TMS_T_4_1.getCode().split("_")[1])) {
                    throw new JayudBizException("vivo派车信息无法驳回");
                }
        }

        return true;
    }


    private void sendCarsMsg2Vivo(SendCarForm form, OrderTransport orderTransport) {
        //查询接单法人
        ApiResult resultOne = omsClient.getLegalEntityInfoByOrderNo(orderTransport.getMainOrderNo());
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("请求查询法人主体信息失败");
        }
        //查询派车信息
        OrderSendCars orderSendCars = this.getById(form.getId());
        //查询车辆信息
        ApiResult resultTwo = this.omsClient.getVehicleInfoById(orderSendCars.getVehicleId());
        if (resultTwo.getCode() != HttpStatus.SC_OK) {
            log.warn("请求车辆信息失败");
        }
//                Map<String, String> request = new HashMap<>();
//                request.put("topic", KafkaMsgEnums.VIVO_FREIGHT_TMS_MESSAGE_ONE.getTopic());
//                request.put("key", KafkaMsgEnums.VIVO_FREIGHT_TMS_MESSAGE_ONE.getKey());
        Map<String, Object> msg = new HashMap<>();
        msg.put("dispatchNo", orderTransport.getThirdPartyOrderNo());
        msg.put("licensePlate", new JSONObject(resultTwo.getData()).getStr("plateNumber"));
        msg.put("transportationCompany", new JSONObject(resultOne.getData()).getStr("legalName"));
        msg.put("containerNo", orderSendCars.getCntrNo());
//                request.put("msg", JSONUtil.toJsonStr(msg));
//                msgClient.consume(request);
        ApiResult result = freightAirApiClient.forwarderVehicleInfo(JSONUtil.toJsonStr(msg));
        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("推送派车消息给vivo失败 msg={}", result.getMsg());
            throw new JayudBizException(400, result.getMsg());
        }
    }

}
