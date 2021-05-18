package com.jayud.finance.vo.template.order;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.finance.po.OrderPaymentBill;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class InlandTPTemplate {

    @ApiModelProperty(value = "内陆订单主键")
    private Long id;

    @ApiModelProperty(value = "提货时间", required = true)
    private String deliveryDate;

    @ApiModelProperty(value = "订单号", required = true)
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单号", required = true)
    private String subOrderNo;

    @ApiModelProperty(value = "客户", required = true)
    private String customerName;

    @ApiModelProperty(value = "启运地", required = true)
    private String pickUpAddress;

    @ApiModelProperty(value = "目的地", required = true)
    private String deliveryAddr;

    @ApiModelProperty(value = "车牌号", required = true)
    private String licensePlate;

    @ApiModelProperty(value = "车型尺寸", required = true)
    private String vehicleSize;

    @ApiModelProperty(value = "件数", required = true)
    private Integer totalNum;

    @ApiModelProperty(value = "毛重(KGS)", required = true)
    private Double totalWeight;

    @ApiModelProperty(value = "操作主体")
    private String legalName;

    @ApiModelProperty(value = "结算单位")
    private String unitName;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

//    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
//    private Integer processStatus;

//    @ApiModelProperty(value = "车型(1吨车 2柜车)")
//    private Integer vehicleType;

    @ApiModelProperty(value = "订单状态")
    private String status;

    @ApiModelProperty(value = "结算单位CODE")
    private String unitCode;

    @ApiModelProperty(value = "法人主体ID")
    private Long legalEntityId;

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

//    @ApiModelProperty(value = "接单日期")
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime receivingOrdersDate;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

//    @ApiModelProperty(value = "创建时间")
//    private LocalDateTime createTime;


//    @ApiModelProperty(value = "运输公司")
//    private String supplierName;
//
//    @ApiModelProperty(value = "司机姓名")
//    private String driverName;
//
//    @ApiModelProperty(value = "司机电话")
//    private String driverPhone;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty("提货地址")
    private List<OrderDeliveryAddress> pickUpAddressList;

    @ApiModelProperty("送货地址")
    private List<OrderDeliveryAddress> orderDeliveryAddressList;


    public void assembleData(JSONObject jsonObject) {
        JSONObject orderSendCars = jsonObject.getJSONObject("orderInlandSendCarsVO");
        if (orderSendCars != null) {
            this.licensePlate = orderSendCars.getStr("licensePlate");
        }
        this.assemblyPickUpInfo();
        this.assemblyDeliveryAddrInfo();
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


    public void assemblyPickUpInfo() {
        StringBuilder addrs = new StringBuilder();
        StringBuilder goodsInfo = new StringBuilder();
        Integer totalNum = 0;
        Double totalWeight = 0.0;

        for (OrderDeliveryAddress deliveryAddr : this.pickUpAddressList) {
            addrs.append(deliveryAddr.getAddress()).append(",");
//            goodsInfo.append(e.getGoodsName())
//                    .append("/").append(e.getPlateAmount() == null ? 0 : e.getPlateAmount())
//                    .append(StringUtils.isEmpty(e.getPlateUnit()) ? "板" : e.getPlateUnit())
//                    .append("/").append(e.getBulkCargoAmount())
//                    .append(StringUtils.isEmpty(e.getBulkCargoUnit()) ? "件" : e.getBulkCargoUnit())
//                    .append("/").append("重量").append(e.getTotalWeight()).append("KG")
//                    .append(",");
            totalNum += deliveryAddr.getBulkCargoAmount() == null ? 0 : deliveryAddr.getBulkCargoAmount();
            totalWeight += deliveryAddr.getTotalWeight() == null ? 0 : deliveryAddr.getTotalWeight();
        }

        this.pickUpAddress = addrs.length() > 6 ? addrs.substring(0, 6) : addrs.toString();
        this.totalNum = totalNum;
        this.totalWeight = totalWeight;
        this.deliveryDate = DateUtils.format(
                Optional.ofNullable(pickUpAddressList.get(0))
                        .map(OrderDeliveryAddress::getDeliveryDate).orElse(""),
                "yyyy-MM-dd");
        //        this.goodsInfo = goodsInfo.toString();
    }

    public void assemblyDeliveryAddrInfo() {
        StringBuilder addrs = new StringBuilder();
        this.orderDeliveryAddressList.forEach(e -> {
            addrs.append(e.getAddress()).append(",");
        });
        this.deliveryAddr = addrs.length() > 6 ? addrs.substring(0, 6) : addrs.toString();
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        this.subOrderNo = orderNo;
    }

    public static void main(String[] args) {
        OrderPaymentBill tmp = null;
        Integer num = Optional.ofNullable(tmp).map(e -> e.getBillNum()).orElse(2);
        System.out.println(num);
    }


}
