package com.jayud.finance.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 账单列表预览
 */
@Data
public class ViewBilToOrderVO {

    @ApiModelProperty(value = "创建日期")
    private String createdTimeStr;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "客户")
    private String customerName;

    @ApiModelProperty(value = "起运地")
    private String startAddress;

    @ApiModelProperty(value = "目的地")
    private String endAddress;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "车型 如：3T")
    private String vehicleSize;

    @ApiModelProperty(value = "件数")
    private Integer pieceNum;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "报关单号")
    private String yunCustomsNo;


    @ApiModelProperty(value = "费用项")
    @JsonIgnore
    private LinkedHashMap<String, String> costItems;

    @ApiModelProperty(value = "合计费用")
    @JsonIgnore
    private List<String> totalCost;

    public List<String> getStrList() {
        List<String> valueStr = new ArrayList<>();
        valueStr.add(this.createdTimeStr);
        valueStr.add(this.orderNo);
        valueStr.add(this.customerName);
        valueStr.add(this.startAddress);
        valueStr.add(this.endAddress);
        valueStr.add(this.licensePlate);
        valueStr.add(this.vehicleSize);
        valueStr.add(this.pieceNum == null ? "0" : this.pieceNum.toString());
        valueStr.add(this.weight == null ? "0" : this.weight.toString());
        valueStr.add(this.yunCustomsNo);

        costItems.forEach((k, v) -> {
            valueStr.add(v);
        });

        return valueStr;
    }

    public void calculateCost(){

    }

}
