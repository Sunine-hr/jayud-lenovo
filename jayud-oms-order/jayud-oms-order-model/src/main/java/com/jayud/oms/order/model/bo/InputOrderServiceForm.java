package com.jayud.oms.order.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InputOrderServiceForm {
    @ApiModelProperty(value = "服务单订单ID,修改时传")
    private Long id;

    @ApiModelProperty(value = "服务单订单号,修改时传")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "服务类型(0:费用补录,1:单证费用,2:存仓费用,3:快递费用)")
    private Integer type;

    @ApiModelProperty(value = "关联订单")
    private String associatedOrder;

//    @ApiModelProperty(value = "提货地址")
//    private List<InputOrderTakeAdrForm> takeAdrForms1 = new ArrayList<>();
//
//    @ApiModelProperty(value = "送货地址")
//    private List<InputOrderTakeAdrForm> takeAdrForms2 = new ArrayList<>();

    @ApiModelProperty(value = "当前登录用户,FeignClient必传,要么就传token,否则跨系统拿不到用户")
    private String loginUser;
}
