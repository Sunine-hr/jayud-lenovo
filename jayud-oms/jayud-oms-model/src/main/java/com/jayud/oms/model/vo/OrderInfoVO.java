package com.jayud.oms.model.vo;

import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class OrderInfoVO {

    @ApiModelProperty(value = "主订单ID")
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "业务类型")
    private String bizCode;

    @ApiModelProperty(value = "进出口类型")
    private String goodsType;

    @ApiModelProperty(value = "进出口类型描述")
    private String goodsTypeDesc;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "作业类型")
    private String classCodeDesc;

    @ApiModelProperty(value = "作业类型CODE")
    private String classCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户名称CODE")
    private String customerCode;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    @ApiModelProperty(value = "提货地址")
    private String takeAddress;

    @ApiModelProperty(value = "送货地址")
    private String giveAddress;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;

    @ApiModelProperty(value = "是否有费用详情")
    private boolean isCost;

    @ApiModelProperty(value = "报关状态,用于标识驳回可编辑")
    private String subCustomsStatus;

    @ApiModelProperty(value = "中港运输状态,用于标识驳回可编辑")
    private String subTmsStatus;

    @ApiModelProperty(value = "报关状态描述,用于标识驳回可编辑")
    private String subCustomsDesc;

    @ApiModelProperty(value = "中港运输状态描述,用于标识驳回可编辑")
    private String subTmsDesc;


    @ApiModelProperty(value = "空运状态,用于标识驳回可编辑")
    private String subAirStatus;

    @ApiModelProperty(value = "空运状态描述,用于标识驳回可编辑")
    private String subAirDesc;


    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

    public String getStatusDesc() {
        return OrderStatusEnum.getDesc(this.status);
    }

    public String getGoodsTypeDesc() {
        if(CommonConstant.VALUE_1.equals(this.goodsType)){
            goodsTypeDesc = CommonConstant.GOODS_TYPE_DESC_1;
        }else if(CommonConstant.VALUE_2.equals(this.goodsType)){
            goodsTypeDesc = CommonConstant.GOODS_TYPE_DESC_2;
        }
        return goodsTypeDesc;
    }

    public String getSubCustomsDesc() {
        return OrderStatusEnum.getDesc(this.subCustomsStatus);
    }

    public String getSubTmsDesc() {
        return OrderStatusEnum.getDesc(this.subTmsStatus);
    }

    public void setSubAirStatus(String subAirStatus) {
        this.subAirStatus = subAirStatus;
        this.subAirDesc=OrderStatusEnum.getDesc(this.subAirStatus);
    }

}
