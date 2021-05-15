package com.jayud.finance.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 应收账单列表预览
 */
@Data
public class ViewBilToOrderVO {
    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "创建日期")
    private String createdTimeStr;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单编号")
    private String subOrderNo;

    @ApiModelProperty(value = "结算单位")
    private String unitAccount;

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

    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;

    @ApiModelProperty(value = "报关单号")
    private String yunCustomsNo;

    @ApiModelProperty(value = "合计费用")
    @JsonIgnore
    private List<String> totalCost;

    @ApiModelProperty(value = "结算币种code")
    private String settlementCurrencyCode;

    @ApiModelProperty(value = "结算币种")
    private String settlementCurrency;


    public void interceptAddress(String startAddress, String endAddress) {
        if (startAddress != null && startAddress.length() > 6) {
            startAddress = startAddress.substring(0, 6);
        }
        if (endAddress != null && endAddress.length() > 6) {
            endAddress = endAddress.substring(0, 6);
        }
        this.startAddress = startAddress;
        this.endAddress = endAddress;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        this.mainOrderNo = orderNo;
    }
}
