package com.jayud.receive;

import com.alibaba.fastjson.JSONObject;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.enums.KafkaMsgEnums;
import com.jayud.feign.AirfreightClient;
import com.jayud.feign.FinanceClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * kafka监听
 *
 * @author william
 * 2019-6-12 14:23:41
 */
@Slf4j
@Component
public class RawDataListener {
    @Autowired
    AirfreightClient airfreightClient;
    @Autowired
    FinanceClient financeClient;

    /**
     * 实时获取kafka数据(生产一条，监听生产topic自动消费一条)
     *
     * @param record
     * @throws IOException
     */
//    @KafkaListener(topics = {"${kafka.consumer.topic}"}, groupId = "${kafka.consumer.group.id}")
//    public void listen(ConsumerRecord<?, ?> record) throws IOException {
//        String value = (String) record.value();
//        String key = (String) record.key();
//        String topic = record.topic();
//
//        //消费信息
//        log.info("kafka信息消费：{}{}{}", topic, key, value);
//        if (StringUtils.isEmpty(key)) {
//            return;
//        }
//
////        if (match(KafkaMsgEnums.FINANCE_CUSTOMS_RECEIVABLE, record)) {
////            financeClient.saveReceivableBill(JSONUtil.toBean(value, Map.class));
////        }
//
////        if(StringUtils.isEmpty(key)){
////            return;
////        }
////        if(INBOUND_ORDER_CREATE.equals(key)){
////            //入库单创建
////            inboundOrderService.createInboundOrder(JSONUtil.toBean(value, InboundOrderForm.class));
////        }else if(INBOUND_ORDER_UPDATE.equals(key)){
////            //入库订单更新
////            inboundOrderService.updateInboundOrder(JSONUtil.toBean(value, InboundOrderForm.class));
////        }else if(INBOUND_ORDER_UPDATE.equals(key)){
////            //入库订单状态更新
////           inboundOrderStatusUpdateService.updateInboundOrderStatus(JSONUtil.toBean(value, InboundOrderStatusUpdateForm.class));
////        }else if(OUTBOUND_ORDER_CREATE.equals(key)){
////            //出库订单创建
////           outboundOrderService.createOutboundOrder(JSONUtil.toBean(value, OutBoundOrderForm.class));
////        }else if(INBOUND_ORDER_UPDATE.equals(key)){
////            //出库订单更新
////            outboundOrderService.updateOutboundOrder(JSONUtil.toBean(value, OutBoundOrderForm.class));
////        }else if(INBOUND_ORDER_UPDATE.equals(key)){
////            //出库订单状态更新
////            outboundOrderStatusUpdateService.updateOutboundOrderStatus(JSONUtil.toBean(value, OutBoundOrderStatusUpdateForm.class));
////        }else if(SHIPMENT_ORDER_CREATE.equals(key)){
////            //出库订单状态更新
////            shipmentOrderService.createShipmentOrder(JSONUtil.toBean(value, ShipmentOrderCreateForm.class));
////        }else if(INBOUND_ORDER.equals(key)){
////            //入库订单同步历史记录
////            inboundOrderSyncHistoryService.createHistory(JSONUtil.toBean(value, ApiRequestHistory.class));
////        }else if(OUTBOUND_ORDER.equals(key)){
////            //入库订单同步历史记录
////            outbondOrderSyncHistoryService.createHistory(JSONUtil.toBean(value, ApiRequestHistory.class));
////        }else if(SHIPMENT_ORDER.equals(key)){
////            //入库订单同步历史记录
////            shipmentOrderSyncHistoryService.createHistory(JSONUtil.toBean(value, ApiRequestHistory.class));
////        }
//    }

    /**
     * 实时获取kafka数据(生产一条，监听生产topic自动消费一条)
     *
     * @param record
     * @throws IOException
     */
    @KafkaListener(topics = {"${kafka.consumer.topic.finance}"}, groupId = "${kafka.consumer.group.id}")
    public void financeListener(ConsumerRecord<?, ?> record) throws IOException {
        String value = (String) record.value();
        String key = (String) record.key();
        String topic = record.topic();
        long offset = record.offset();
        //消费信息
        log.info("kafka信息消费：offset={} topic={} key={} value={}", offset, topic, key, value);
        if (StringUtils.isEmpty(key)) {
            return;
        }

        if (match(KafkaMsgEnums.FINANCE_CUSTOMS_RECEIVABLE, record)) {
            log.info("写入金蝶报关应收数据...");
            Map<String, String> msg = new HashMap<>();
            msg.put("msg", value);
            Boolean aBoolean = financeClient.saveReceivableBill(JSONObject.toJSONString(msg));
            System.out.println(aBoolean);
//            doLog(commonResult);
        }
        if (match(KafkaMsgEnums.FINANCE_CUSTOMS_PAYABLE, record)) {
            log.info("写入金蝶报关应付数据...");
            Map<String, String> msg = new HashMap<>();
            msg.put("msg", value);
            Boolean aBoolean = financeClient.savePayableBill(JSONObject.toJSONString(msg));
            System.out.println(aBoolean);

//            doLog(commonResult);
        }
    }

    private void doLog(CommonResult commonResult) {
        if (commonResult.getCode() != ResultEnum.SUCCESS.getCode()) {
            log.error(commonResult.getMsg());
        }
    }


    private Boolean match(KafkaMsgEnums enums, ConsumerRecord<?, ?> record) {
        if (Objects.equals(enums.getTopic(), record.topic()) && Objects.equals(enums.getKey(), record.key())) {
            return true;
        }
        return false;
    }
}
