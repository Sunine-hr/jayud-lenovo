package com.jayud.tms.model.vo.supplier;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.tms.model.vo.OrderTakeAdrInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SupplierBillInfo {

    @ApiModelProperty("子订单id")
    private Long subOrderId;

    @ApiModelProperty("子订单号")
    private String orderNo;

    @ApiModelProperty(value = "提货时间,多个逗号拼接")
    private String takeTimeStr;

    @ApiModelProperty("客户名称 (供应商是子订单操作主体)")
    private String customerName;

    @ApiModelProperty(value = "提货地址")
    private String entireAddress1;

    @ApiModelProperty(value = "送货地址")
    private String entireAddress2;

    @ApiModelProperty("尺寸")
    private String vehicleSize;

    @ApiModelProperty("车牌号")
    private String plateNumber;

    @ApiModelProperty("费用金额")
    private String billAmount;

    @ApiModelProperty("中转仓库id")
    @JsonIgnore
    private Long warehouseInfoId;

    @ApiModelProperty("车辆id")
    @JsonIgnore
    private Long vehicleId;

//    @ApiModelProperty("中转仓库")
//    private String warehouseName;

    @ApiModelProperty("是否虚拟仓库")
    @JsonIgnore
    private Boolean isVirtual;

    @ApiModelProperty("仓库地址")
    @JsonIgnore
    private String warehouseAddr;


    public void assemblyCost(Map<String, BigDecimal> costMap) {
        StringBuilder sb = new StringBuilder();
        if (costMap == null) {
            return;
        }
        costMap.forEach((k, v) -> {
            sb.append(v).append(k).append("</br>");
        });
        this.billAmount = sb.toString();
    }

    public void assemblyTakeAdrInfos(List<OrderTakeAdrInfoVO> takeAdrsList) {
        if (CollectionUtil.isEmpty(takeAdrsList)) {
            return;
        }
        StringBuilder pickUpAddr = new StringBuilder();
        StringBuilder deliveryAddr = new StringBuilder();
        for (OrderTakeAdrInfoVO orderTakeAdr : takeAdrsList) {
            if (this.orderNo.equals(orderTakeAdr.getOrderNo())) {
                if (1 == orderTakeAdr.getOprType()) {
                    //提货地址
                    pickUpAddr.append(orderTakeAdr.getAddress()).append(",");
                }
                if (2 == orderTakeAdr.getOprType()) {
                    //送货地址
                    deliveryAddr.append(orderTakeAdr.getAddress()).append(",");
                }
            }
        }
        this.entireAddress1 = pickUpAddr.toString();
        this.entireAddress2 = deliveryAddr.toString();
    }

    /**
     * 组装中转仓库地址
     *
     * @param warehouseMap
     */
    public void assemblyWarehouse(Map<Long, Map<String, Object>> warehouseMap) {
        if (CollectionUtil.isEmpty(warehouseMap)) {
            return;
        }
        Object tmp = warehouseMap.get(this.warehouseInfoId);
        if (tmp == null) {
            return;
        }
        JSONObject jsonObject = new JSONObject(tmp);
//        this.warehouseName = jsonObject.getStr("warehouseName");
        this.isVirtual = jsonObject.getBool("isVirtual");
        this.warehouseAddr = jsonObject.getStr("address");
        this.shippingAddressHandle();
    }


    /**
     * 送货地址处理
     * 多个地址并且不是虚拟仓,展示中转仓地址
     */
    private void shippingAddressHandle() {
        if (this.entireAddress2.length() > 1) {
            if (this.isVirtual == null || !this.isVirtual) {
                this.entireAddress2 = this.warehouseAddr;
            }
        }

    }
}
