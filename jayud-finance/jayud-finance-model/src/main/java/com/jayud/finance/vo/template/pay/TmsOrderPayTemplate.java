package com.jayud.finance.vo.template.pay;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.Utilities;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 中港模板(以导出对账单模板为标准,后续改动需要原有模板改动)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TmsOrderPayTemplate {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "序号", required = true)
    private String num;

    @ApiModelProperty(value = "提货日期", required = true)
    @JsonFormat(pattern = "yyyy年MM月dd日")
    private String takeTimeStr;

    @ApiModelProperty(value = "订单号", required = true)
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单号", required = true)
    @JsonProperty("orderNo")
    private String subOrderNo;

    @ApiModelProperty(value = "客户",required = true)
    private String customerName;

    @ApiModelProperty(value = "启运地", required = true)
    private String pickUpAddress;

    @ApiModelProperty(value = "目的地", required = true)
    private String deliveryAddress;

    @ApiModelProperty(value = "车牌号", required = true)
    private String licensePlate;

    @ApiModelProperty(value = "车型", required = true)
    private String vehicleSize;

//    @ApiModelProperty(value = "进出口类型", required = true)
//    private String goodsTypeDesc;

//    @ApiModelProperty(value = "通关口岸", required = true)
//    private String portName;

//    @ApiModelProperty(value = "操作主体", required = true)
//    private String legalName;

//    @ApiModelProperty(value = "结算单位", required = true)
//    private String unitName;

    @ApiModelProperty(value = "件数", required = true)
    private Integer totalPieceAmount;

    @ApiModelProperty(value = "毛重(KGS)", required = true)
    private Double totalWeight;

    @ApiModelProperty(value = "无缝清关号",required = true)
    private String seamlessNo;

//    @ApiModelProperty(value = "报关单号", required = true)
//    private String yunCustomsNo;

    @ApiModelProperty(value = "货物信息", required = false)
    private String goodsInfo;

    @ApiModelProperty("通关口岸CODE")
    private String portCode;

    @ApiModelProperty("结算单位CODE")
    private String unitCode;

    @ApiModelProperty("接单法人ID")
    private Long legalEntityId;

    @ApiModelProperty(value = "主订单id")
    private Long mainOrderId;


    public void assembleData(JSONObject jsonObject) {
        JSONObject orderSendCars = jsonObject.getJSONObject("orderSendCars");
        Object pickUpAddress = jsonObject.get("pickUpAddress");
        Object deliveryAddress = jsonObject.get("deliveryAddress");
        if (orderSendCars != null) {
            this.licensePlate = orderSendCars.getStr("plateNumber");
        }
        this.assemblyPickUpInfo(pickUpAddress);
        this.assemblyDeliveryAddrInfo(deliveryAddress);
    }

    public void assemblyMainOrderData(Object mainOrderObjs) {
        if (mainOrderObjs == null) {
            return;
        }
        JSONArray mainOrders = new JSONArray(JSON.toJSONString(mainOrderObjs));
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject json = mainOrders.getJSONObject(i);
            if (this.mainOrderNo.equals(json.getStr("orderNo"))) { //主订单配对
                this.customerName = json.getStr("customerName");
//                this.contractNo = json.getStr("contractNo");
//                this.customerCode = json.getStr("customerCode");
//                this.mainOrderId = json.getLong("id");
//                this.bizUname = json.getStr("bizUname");
//                this.bizCode = json.getStr("bizCode");
//                this.classCode = json.getStr("classCode");
                break;
            }
        }

    }

    public void assemblyDeliveryAddrInfo(Object orderTakeAdrForms) {
        if (orderTakeAdrForms == null) {
            return;
        }
        JSONArray jsonArray = new JSONArray(orderTakeAdrForms);
        StringBuilder addrs = new StringBuilder();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            addrs.append(jsonObject.getStr("address", "")).append(",");
        }

        this.deliveryAddress = addrs.length() > 6 ? addrs.substring(0, 6) : addrs.toString();
    }

    public void assemblyPickUpInfo(Object orderTakeAdrForms) {
        if (orderTakeAdrForms == null) {
            return;
        }
        JSONArray jsonArray = new JSONArray(orderTakeAdrForms);
        StringBuilder goodsInfo = new StringBuilder();
        StringBuilder addrs = new StringBuilder();
//        StringBuilder takeTime = new StringBuilder();

        Integer totalPieceAmount = 0;
        Double totalWeight = 0.0;
//        Double volume = 0.0;

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            totalPieceAmount += jsonObject.getInt("pieceAmount", 0);
            totalWeight += jsonObject.getDouble("weight", 0.0);

            addrs.append(jsonObject.getStr("address", "")).append(",");
            goodsInfo.append(jsonObject.getStr("goodsDesc", "")).append(",");
        }
        this.goodsInfo = goodsInfo.toString();
        this.pickUpAddress = addrs.length() > 6 ? addrs.substring(0, 6) : addrs.toString();
        this.takeTimeStr =  DateUtils.format(jsonArray.getJSONObject(0).getDate("takeTimeStr"),"yyyy-MM-dd");
        this.totalPieceAmount = totalPieceAmount;
        this.totalWeight = totalWeight;
    }


    /**
     * 过滤头部(账单)
     */
    @SneakyThrows
    public void filterBillHead(Class clazz) {
        Map map = new HashMap();
        map.put("required", true);
        Utilities.dynamicUpdateAnnotations(clazz, Arrays.asList("goodsInfo"),
                ApiModelProperty.class, map);
    }


}
