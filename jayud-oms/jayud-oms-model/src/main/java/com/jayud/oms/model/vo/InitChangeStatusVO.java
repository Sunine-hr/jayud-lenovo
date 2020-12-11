package com.jayud.oms.model.vo;

import com.jayud.common.constant.CommonConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class InitChangeStatusVO {

    @ApiModelProperty(value = "子订单ID")
    private Long id;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty(value = "订单类型")
    private String orderType;

    @ApiModelProperty(value = "订单类型描述")
    private String orderTypeDesc;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

    public String getOrderTypeDesc() {
        if(CommonConstant.ZGYS.equals(this.orderType)){
            return CommonConstant.ZGYS_DESC;
        } else if (CommonConstant.BG.equals(this.orderType)) {
            return CommonConstant.BG_DESC;
        } else if (CommonConstant.KY.equals(this.orderType)) {
            return CommonConstant.KY_DESC;
        }
        return "";
    }

}
