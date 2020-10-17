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

    @ApiModelProperty(value = "客户名称")
    private String customerCode;

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

    public String getStatusDesc() {
        if(OrderStatusEnum.MAIN_1.getCode().equals(this.status)){
            statusDesc = OrderStatusEnum.MAIN_1.getDesc();
        }else if(OrderStatusEnum.MAIN_2.getCode().equals(this.status)){
            statusDesc = OrderStatusEnum.MAIN_2.getDesc();
        }else if(OrderStatusEnum.MAIN_3.getCode().equals(this.status)){
            statusDesc = OrderStatusEnum.MAIN_3.getDesc();
        }else if(OrderStatusEnum.MAIN_4.getCode().equals(this.status)){
            statusDesc = OrderStatusEnum.MAIN_4.getDesc();
        }
        return statusDesc;
    }

    public String getGoodsTypeDesc() {
        if(CommonConstant.VALUE_1.equals(this.goodsType)){
            goodsTypeDesc = CommonConstant.GOODS_TYPE_DESC_1;
        }else if(CommonConstant.VALUE_2.equals(this.goodsType)){
            goodsTypeDesc = CommonConstant.GOODS_TYPE_DESC_2;
        }
        return goodsTypeDesc;
    }

}
