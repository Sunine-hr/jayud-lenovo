package com.jayud.tms.model.vo;

import com.jayud.common.enums.OrderStatusEnum;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderTransportVO {

    @ApiModelProperty(value = "子订单ID")
    private Long id;

    @ApiModelProperty(value = "主订单ID")
    private Long mainOrderId;

    @ApiModelProperty(value = "主订单")
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

    @ApiModelProperty(value = "货物流向(1进口 2出口)")
    private Integer goodsType;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "作业类型")
    private String classCode;

    @ApiModelProperty(value = "作业类型描述")
    private String classCodeDesc;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "车型")
    private String vehicleType;

    @ApiModelProperty(value = "车型尺寸")
    private String vehicleSize;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "是否有费用详情")
    private boolean isCost;

    @ApiModelProperty(value = "中港运输状态,用于标识驳回可编辑")
    private String subTmsStatus;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    //货物信息
    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;

    @ApiModelProperty(value = "板数")
    private String plateAmount;

    @ApiModelProperty(value = "件数")
    private String pieceAmount;

    @ApiModelProperty(value = "重量")
    private String weight;

    //提供货信息
    @ApiModelProperty(value = "省")//提货
    private String stateName1;

    @ApiModelProperty(value = "市")
    private String cityName1;

    @ApiModelProperty(value = "详细地址")
    private String address1;

    @ApiModelProperty(value = "提货完整地址")
    private String entireAddress1;

    @ApiModelProperty(value = "省")//送货
    private String stateName2;

    @ApiModelProperty(value = "市")
    private String cityName2;

    @ApiModelProperty(value = "详细地址")
    private String address2;

    @ApiModelProperty(value = "送货完整地址")
    private String entireAddress2;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;


    public String getEntireAddress1() {
        if(StringUtil.isNullOrEmpty(this.stateName1) || StringUtil.isNullOrEmpty(this.cityName1) ||
                StringUtil.isNullOrEmpty(this.address1)){
            return "";
        }
        return this.entireAddress1 = this.stateName1 + this.cityName1 + this.address1;
    }

    public String getEntireAddress2() {
        if(StringUtil.isNullOrEmpty(this.stateName2) || StringUtil.isNullOrEmpty(this.cityName2) ||
                StringUtil.isNullOrEmpty(this.address2)){
            return "";
        }
        return this.entireAddress2 = this.stateName2 + this.cityName2 + this.address2;
    }

    public String getStatusDesc() {
       return OrderStatusEnum.getDesc(this.status);
    }

}
